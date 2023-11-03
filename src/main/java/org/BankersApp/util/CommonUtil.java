package org.BankersApp.util;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.BankersApp.dto.CustomerDTO;
import org.BankersApp.dto.ErrorMessage;
import org.BankersApp.dto.ResponseDTO;

import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class CommonUtil {


    public Response buildErrorResponse(String message, Response.Status status, List<ErrorMessage> errors, Object data, UriInfo uriInfo) {
        ResponseDTO responseDTO = new ResponseDTO("Failed", message, status.getStatusCode(), errors, data, uriInfo.getPath(), Instant.now());
        return Response.status(status).entity(responseDTO).build();
    }

    public Response buildSuccessResponse(String message, Response.Status status, List<ErrorMessage> errors, Object data, UriInfo uriInfo) {
        ResponseDTO responseDTO = new ResponseDTO("Success", message, status.getStatusCode(), errors, data, uriInfo.getPath(), Instant.now());
        return Response.status(status).entity(responseDTO).build();


    }

    public ResponseDTO successResponse(String retrivedDataSuccessfully, int i, String string, List<CustomerDTO> customers) {
        return null;
    }
}
