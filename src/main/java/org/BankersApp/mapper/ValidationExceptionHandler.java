package org.BankersApp.mapper;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import java.text.SimpleDateFormat;
import java.util.*;

public class ValidationExceptionHandler implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "Failed");
        response.put("code", Response.Status.BAD_REQUEST.getStatusCode());
        response.put("msg", "Customer creation failed!");
        response.put("errors", getValidationErrors(exception));
        response.put("data", null); // Replace with your actual response entity
        response.put("path", "/api/v1/customers/{customerId}");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String timestamp = dateFormat.format(new Date());
        response.put("timestamp", timestamp);

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(response)
                .build();

    }
    private List<String> getValidationErrors(ConstraintViolationException exception) {
        List<String> errors = new ArrayList<>();
        errors.add(exception.getMessage());
        return errors;
    }
}

