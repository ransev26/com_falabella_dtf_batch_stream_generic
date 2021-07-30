package cl.falabella.fund.ports;

import cl.falabella.fund.exceptions.ParserException;
import cl.falabella.fund.models.ServiceRequest;

public interface IParser {

    ServiceRequest parse(String json) throws ParserException;

}