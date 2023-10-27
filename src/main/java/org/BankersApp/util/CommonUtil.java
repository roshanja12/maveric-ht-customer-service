package org.BankersApp.util;


import jakarta.enterprise.context.ApplicationScoped;
import org.BankersApp.dto.CustomerDTO;
import org.BankersApp.dto.ResponseDTO;

import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class CommonUtil {


    public ResponseDTO successResponse( String message, int statusCode, String path,List<CustomerDTO> customerDTO) {
        ResponseDTO response = new ResponseDTO();
        response.setMsg(message);
//        response.setStatus(statusCode);
        response.setPath(path);
        response.setData(customerDTO);
        response.setTimestamp(Instant.now());
        return response;
    }

    public ResponseDTO errorResponse(String errorMessage, int statusCode, String requestUri) {
        ResponseDTO response = new ResponseDTO();
        response.setMsg(errorMessage);
//        response.setStatus(statusCode);
        response.setPath(requestUri);
        return response;
    }


}
