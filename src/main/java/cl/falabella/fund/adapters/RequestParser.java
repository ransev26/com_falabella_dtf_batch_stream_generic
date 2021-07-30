package cl.falabella.fund.adapters;

import cl.falabella.fund.models.ServiceRequest;
import com.google.gson.Gson;

import cl.falabella.fund.exceptions.ParserException;
import cl.falabella.fund.ports.IParser;

public class RequestParser implements IParser {

    private void isValid(String json) throws ParserException {
        if (json == null)
            throw new ParserException("json cannot be null!");
        if (json.isEmpty())
            throw new ParserException("json cannot be empty!");
    }

    @Override
    public ServiceRequest parse(String json) throws ParserException {
        try {
            isValid(json);
            return new Gson().fromJson(json, ServiceRequest.class);

        } catch(Exception e) {
            throw new ParserException("An error has ocurred while parsing json: " + e.getMessage());
        }
    }


}