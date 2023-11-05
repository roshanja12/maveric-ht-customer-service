package org.BankersApp.dto;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonbPropertyOrder({ "status", "msg", "code", "errors", "data", "path", "timestamp" })
public class ResponseDTO {
    private String status;
    private String msg;
    private int code;
    private List<ErrorMessage> errors;
    private Object data;
    private String path;
    private Instant timestamp;
}
