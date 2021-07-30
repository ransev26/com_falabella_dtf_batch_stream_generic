package cl.falabella.fund;

import cl.falabella.fund.adapters.PubsubQueue;
import cl.falabella.fund.service.Options;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.extensions.sql.SqlTransform;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.Row;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.stream.Collectors;


public class App {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);


    public static final Schema SCHEMA = Schema.builder()
            .addStringField("sku")
            .addDoubleField("price")
            .build();

    public static final Schema RESULT_SCHEMA = Schema.builder()
            .addDoubleField("metric")
            .build();

    public static class RowToString extends DoFn<Row, String> {
        @ProcessElement
        public void processElement(ProcessContext c) {
            String line = c.element().getValues()
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            c.output(line);
        }
    }


    /**
     * Main entry point for executing the pipeline.
     * @param args  The command-line arguments to the pipeline.
     */
    public static void main(String[] args) {
        Options options = PipelineOptionsFactory
                .fromArgs(args)
                .withValidation()
                .as(Options.class);
        options.setStreaming(true);
        options.setJobName(options.getDataFlowName());
        run(options);
    }


    /**
     * Runs the pipeline with the supplied options.
     *
     * @param options The execution parameters to the pipeline.
     * @return  The result of the pipeline execution.
     */
    public static PipelineResult run(Options options) {

        // Create the pipeline
        Pipeline pipeline = Pipeline.create(options);

        final double[] actualPrice = {0};

        PCollection<PubsubMessage> readMessages = pipeline.apply("Read PubSub Events", PubsubIO.readMessages().fromSubscription(options.getInputSubscription()));

        PCollection<Map<String, Map<String, String>>> mapMessage = readMessages.apply("Transform PubSub Message To Map", ParDo.of(new PubsubQueue.PubsubToMap()));

        PCollection<Row> addMemoryData = mapMessage.apply("Add Stream Batch Data",ParDo.of(new DoFn<Map<String, Map<String, String>>,Row>() {
            @ProcessElement
            public void processElement(ProcessContext c) throws Exception {
                Map<String, Map<String, String>> pubsubmessage = c.element();
                String json = "";
                JSONObject jo;

                for (Map.Entry<String, Map<String, String>> entry : pubsubmessage.entrySet()) {
                    json = entry.getKey();
                }
                jo = new JSONObject(json);
                actualPrice[0] = Double.parseDouble(jo.get("price").toString());
                LOG.info("actualPrice[0]"+actualPrice[0]);

                Row appRow = Row.withSchema(SCHEMA)
                        .addValues(jo.get("sku").toString(),Double.valueOf(jo.get("price").toString()))
                        .build();
                c.output(appRow);
                //}
            }
        })).setRowSchema(SCHEMA);

        String query = "SELECT (MAX(price) / AVG(price)) FROM PCOLLECTION GROUP BY SESSION(CURRENT_TIMESTAMP, INTERVAL '10' SECOND)";

        PCollection<Row> calculateMetric = addMemoryData.apply("Calculate Metric Window Time", SqlTransform.query(query)).setRowSchema(RESULT_SCHEMA);

        PCollection<String> messageToPubsub = calculateMetric.apply("Transform Row in String", ParDo.of(new RowToString()));

        messageToPubsub.apply("Write PubSub Events", PubsubIO.writeStrings().to(options.getOutputTopic()));

        return pipeline.run();
    }


}
