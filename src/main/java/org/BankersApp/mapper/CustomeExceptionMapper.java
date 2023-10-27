package org.BankersApp.mapper;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.BankersApp.exception.CustomeException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import java.util.Map;

/**
 * @author ankushk
 */

@Provider
public class CustomeExceptionMapper implements ExceptionMapper<CustomeException> {

    public Response toResponse(CustomeException exception) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "Failed");
        response.put("code", Response.Status.NOT_FOUND.getStatusCode());
        response.put("msg", "Data not found in DB");
        response.put("errors",exception.getMessage());
        response.put("data", null);
        response.put("path", "/api/v1/customers");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String timestamp = dateFormat.format(new Date());
        response.put("timestamp", timestamp);

        return Response.status(Response.Status.NOT_FOUND)
                .entity(response)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

}
