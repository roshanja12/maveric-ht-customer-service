package org.BankersApp.dto;

import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.*;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.Instant;

/**
 * @author ankushk
 */
@Data
public class CustomerDTO {

    @Schema
    private Long customerId;
    private String firstName;
    private String lastName;
    private String email;
    private Long phoneNumber;
    private String city;

}
