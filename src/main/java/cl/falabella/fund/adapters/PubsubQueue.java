package cl.falabella.fund.adapters;

import org.apache.beam.sdk.transforms.DoFn;

import cl.falabella.fund.ports.IQueue;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;


import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class PubsubQueue implements IQueue{


    public static class PubsubToMap extends DoFn<PubsubMessage, Map<String, Map<String, String>>> {

        private static final long serialVersionUID = 1L;

        @ProcessElement
        public void processElement(@Element PubsubMessage pubsubmessage, OutputReceiver<Map<String, Map<String, String>>> receiver) {

            String payload = new String(pubsubmessage.getPayload(), StandardCharsets.UTF_8);

            Map<String,String> attributesMap = pubsubmessage.getAttributeMap();
            Map<String, Map<String, String>> output = new HashMap<>();
            output.put(payload, attributesMap);
            receiver.output(output);
        }
    }

}