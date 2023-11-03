package org.BankersApp.mapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.BankersApp.dto.ErrorMessage;
import org.BankersApp.exception.CustomeException;
import org.BankersApp.util.CommonUtil;
import java.util.*;



@Provider
public class CustomeExceptionMapper implements ExceptionMapper<CustomeException> {

    @Inject
    CommonUtil commonUtil;

    @Context
    UriInfo uriInfo;

    public Response toResponse(CustomeException exception) {
        System.out.println("-----------customermappper----------");
        if (exception instanceof CustomeException) {
            List<ErrorMessage> errors = new ArrayList<>();
            errors.add(new ErrorMessage(exception.getMessage()));
            System.out.println("Welcome");

            return commonUtil.buildErrorResponse("Unable to fetch data", Response.Status.NOT_FOUND, errors, null,uriInfo);
        } else {
            List<ErrorMessage> errors = new ArrayList<>();
            errors.add(new ErrorMessage("Internal server error"));

            return commonUtil.buildErrorResponse("Internal Server Error", Response.Status.INTERNAL_SERVER_ERROR, errors, null,uriInfo);
        }
    }


}
