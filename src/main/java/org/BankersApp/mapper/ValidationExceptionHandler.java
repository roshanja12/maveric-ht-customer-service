package org.BankersApp.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.hibernate.exception.ConstraintViolationException;
import jakarta.ws.rs.ext.ExceptionMapper;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Provider
public class ValidationExceptionHandler implements ExceptionMapper<ConstraintViolationException> {


    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "Failed");
        response.put("code", Response.Status.BAD_REQUEST.getStatusCode());
        response.put("msg", "Customer creation failed!");
        response.put("errors", getValidationErrors(exception));
        response.put("data", null); // Replace with your actual response entity
        response.put("path", "/api/v1/customers");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String timestamp = dateFormat.format(new Date());
        response.put("timestamp", timestamp);

        // Build the Response object
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(response)
                .build();
    }
//    @Override
//    public Response toResponse(ConstraintViolationException e) {
//        List<> errorMessages = e.getConstraintViolations().stream()
//                .map(constraintViolation -> new Response.ErrorMessage(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage()))
//                .collect(Collectors.toList());
//        return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(errorMessages)).build();
//    }




    private List<String> getValidationErrors(ConstraintViolationException exception) {

        List<String> errors = new ArrayList<>();
        errors.add(exception.getMessage());
        return errors;
    }
}
