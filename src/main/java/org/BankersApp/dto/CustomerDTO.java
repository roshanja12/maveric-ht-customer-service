package org.BankersApp.dto;

import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

/**
 * @author ankushk
 */
@Data
public class CustomerDTO {
    private Long customerId;
    private String firstName;
    private String lastName;
    private String email;
    private Long phoneNumber;
    private String city;

}
