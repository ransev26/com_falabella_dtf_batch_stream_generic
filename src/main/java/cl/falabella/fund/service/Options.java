package cl.falabella.fund.service;

import org.apache.beam.sdk.extensions.gcp.options.GcpOptions;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.StreamingOptions;
import org.apache.beam.sdk.options.Validation;
import org.apache.beam.sdk.options.ValueProvider;
import org.apache.beam.sdk.options.Validation.Required;

/**
 * Options supported by the pipeline.
 *
 * <p>Inherits standard configuration options.</p>
 */
public interface Options extends PipelineOptions, StreamingOptions, GcpOptions {



  @Description("Dataflow Name job.")
  @Required
  String getDataFlowName();
  void setDataFlowName(String value);

  @Description("GCS File Path")
  @Required
  String getGcsFilePath();
  void setGcsFilePath(String value);

  @Description("Output Topic")
  @Required
  String getOutputTopic();
  void setOutputTopic(String value);

  @Description("The Cloud Pub/Sub Subscription to read from.")
  @Required
  ValueProvider<String> getInputSubscription();
  void setInputSubscription(ValueProvider<String> value);


}

