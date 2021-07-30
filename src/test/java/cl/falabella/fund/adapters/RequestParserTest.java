package cl.falabella.fund.adapters;

import static org.junit.Assert.assertEquals;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import cl.falabella.fund.exceptions.ParserException;
import cl.falabella.fund.models.ServiceRequest;

public class RequestParserTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void onEmptyInputParserThrowsParserException() throws ParserException {
        //Arrange
        String input = "";
        //Act
        RequestParser parser = new RequestParser();
        String expected = "An error has ocurred while parsing json: json cannot be empty!";
        expectedException.expect(ParserException.class);
        expectedException.expectMessage(expected);
        parser.parse(input);
    }

    @Test
    public void onNullInputParserThrowsParserException() throws ParserException {
        //Arrange
        String input = null;
        //Act
        RequestParser parser = new RequestParser();
        String expected = "An error has ocurred while parsing json: json cannot be null!";
        expectedException.expect(ParserException.class);
        expectedException.expectMessage(expected);
        parser.parse(input);
    }

    @Test
    public void onInvalidInputParserThrowsParserException() throws ParserException {
        //Arrange
        String input = "invalid json";

        //Act
        RequestParser parser = new RequestParser();
        String expected = "An error has ocurred while parsing json: java.lang.IllegalStateException: Expected BEGIN_OBJECT but was STRING at line 1 column 1 path $";
        expectedException.expect(ParserException.class);
        expectedException.expectMessage(expected);
        parser.parse(input);
    }

    @Test
    public void onValidInputParserRunSuccessfullyFile() throws ParserException {
        //Arrange
        String input = "{\n"+
                "\"inputSubscription\":\"projects/test/subscriptions/test\",\n"+
                "\"project\":\"flb-rtl-dtl-dfp-dev\",\n"+
                "\"stagingLocation\":\"gs://test/stg/\",\n"+
                "\"tempLocation\":\"gs://test/tmp/\",\n"+
                "\"runner\":\"DataflowRunner\",\n"+
                "\"windowDuration\":\"2m\",\n"+
                "\"numShards\":\"1\",\n"+
                "\"entityType\":\"ANY\",\n"+
                "\"eventType\":\"ANY\",\n"+
                "\"country\":\"ANY\",\n"+
                "\"bqTable\":\"test\",\n"+
                "\"bqDataset\":\"test\"\n"+
                "}";
        //Act
        RequestParser parser = new RequestParser();
        ServiceRequest request = parser.parse(input);

        //Assert
        assertEquals("DataflowRunner", request.getRunner());

    }
    
}
