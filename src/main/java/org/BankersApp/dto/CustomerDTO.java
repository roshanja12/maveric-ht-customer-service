package org.BankersApp.dto;
import lombok.Data;
@Data
public class CustomerDTO {
    private Long customerId;
    private String firstName;
    private String lastName;
    private String email;
    private Long phoneNumber;
    private String city;

}
