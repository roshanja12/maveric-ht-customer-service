package org.BankersApp.dto;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * @author ankushk
 */
@Data
@Schema(description = "Customer data transfer object")
public class CustomerDTO {

    @Schema(description = "Customer's unique identifier")
    private Long customerId;

    @Schema(description = "Customer's first name")
    private String firstName;

    @Schema(description = "Customer's last name")
    private String lastName;

    @Schema(description = "Customer's email address")
    private String email;

    @Schema(description = "Customer's phone number")
    private Long phoneNumber;

    @Schema(description = "Customer's city")
    private String city;

}
