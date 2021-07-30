package cl.falabella.fund.ports;

import cl.falabella.fund.exceptions.DataProcesingException;
import com.google.api.services.bigquery.model.TableRow;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.transforms.DoFn;

public interface IDataProcesing {

    public String assignDataToRow(DoFn<PubsubMessage, TableRow>.ProcessContext c, TableRow row, PubsubMessage pubsubmessage, String payload) throws DataProcesingException;
    public String assignDataToString(DoFn<PubsubMessage, TableRow>.ProcessContext c, String payload) throws DataProcesingException;

}