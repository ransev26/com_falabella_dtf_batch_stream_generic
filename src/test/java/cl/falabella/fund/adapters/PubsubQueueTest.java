package cl.falabella.fund.adapters;

import cl.falabella.fund.adapters.PubsubQueue.PubsubToMap;
import java.util.HashMap;
import java.util.Map;

import org.apache.beam.sdk.testing.PAssert;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.testing.ValidatesRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class PubsubQueueTest {
    @Category(ValidatesRunner.class)
    @Test
        public void testPubsubToBqRowFn() throws Exception {
            String testPubsubMessage1 = "{\"id\":\"3240771\",\"lastModifiedDate\":\"2022-05-04 15:27:25\",\"forceUnpublish\":false}";
            String testPubsubMessage2 = "{\"id\":\"3240772\",\"lastModifiedDate\":\"2022-05-04 15:27:25\",\"forceUnpublish\":false}";
            String encodedPayload = Base64.getEncoder().encodeToString(testPubsubMessage1.getBytes(StandardCharsets.UTF_8));
            byte[] payload1 = testPubsubMessage1.getBytes(StandardCharsets.UTF_8);
            byte[] payload2 = testPubsubMessage2.getBytes(StandardCharsets.UTF_8);
            Map<String, String> attributes = new HashMap<String,String>();
            attributes.put("country", "CL");
            PubsubMessage message = new PubsubMessage(payload1, attributes);
            PubsubMessage message2 = new PubsubMessage(payload2, attributes);
            //List<PubsubMessage> words = Arrays.asList(message, message2);
            PCollection<PubsubMessage> collectionMessages = p.apply(Create.<PubsubMessage>of(message, message2));
            PCollection<Map<String, Map<String, String>>> collectionRows = collectionMessages.apply(ParDo.of(new PubsubToMap()));


          
            Map<String, Map<String, String>> pubsubMap1 = new HashMap<String, Map<String, String>>();
            pubsubMap1.put(testPubsubMessage1, attributes);
            Map<String, Map<String, String>> pubsubMap2 = new HashMap<String, Map<String, String>>();
            pubsubMap2.put(testPubsubMessage2, attributes);




            PAssert.that(collectionRows).containsInAnyOrder(pubsubMap1, pubsubMap2);
            p.run().waitUntilFinish();
        }
  
  
    @Rule public TestPipeline p = TestPipeline.create();
}