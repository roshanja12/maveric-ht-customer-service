package org.BankersApp;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.BankersApp.controller.CustomerController;
import org.BankersApp.dto.CustomerDTO;
import org.BankersApp.dto.ErrorMessage;
import org.BankersApp.entity.Customer;
import org.BankersApp.repository.CustomerRepository;
import org.BankersApp.service.CustomerService;
import org.BankersApp.util.CommonUtil;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.xml.sax.SAXException;

import javax.xml.transform.Source;
import javax.xml.validation.Validator;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
/**
 * @Rohit
 */

@QuarkusTest
public class CustomerControllerTest {
    @InjectMock
    private Validator validator;

    @InjectMock
    private CommonUtil commonUtil;

    @Inject
    CustomerController customerController;

    @InjectMock
    CustomerRepository customerRepository;

    @InjectMock
    CustomerService customerService;

    @Test
    public void testDeleteCustomerSuccess() {
        Long customerId = 10L;
        UriInfo uriInfo = Mockito.mock(UriInfo.class);
        CustomerDTO deletedCustomerDTO = new CustomerDTO();
        deletedCustomerDTO.setCustomerId(customerId);
        deletedCustomerDTO.setFirstName("Rohit");
        when(customerService.deleteCustomer(customerId)).thenReturn(deletedCustomerDTO);
        Response response = customerController.deleteCustomer(customerId, uriInfo);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testDeleteglobalexception() {
        Long customerId = 2L;
        UriInfo uriInfo = Mockito.mock(UriInfo.class);
        when(customerService.deleteCustomer(customerId)).thenReturn(null);
        Response response = customerController.deleteCustomer(customerId, uriInfo);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUpdateCustomerSuccess() {
        Long customerId = 10L;
        UriInfo uriInfo = Mockito.mock(UriInfo.class);

        // Mock validation to return an empty set of violations, indicating success
        when(validator.validate(any(Customer.class))).thenReturn(Collections.emptySet());

        // Mock the customer service to return an updated customer DTO
        CustomerDTO updatedCustomerDTO = new CustomerDTO();
        updatedCustomerDTO.setEmail("rohan123@gmail.com");
        updatedCustomerDTO.setPhoneNumber(Long.valueOf("9403821914"));
        updatedCustomerDTO.setCity("Delhi");
        when(customerService.updateCustomer(any(Customer.class))).thenReturn(updatedCustomerDTO);

        // Mock the commonUtil to return a success response
        when(commonUtil.buildSuccessResponse(
                "Updated Successfully",
                Response.Status.OK,
                null,null, // Replace with the expected response entity
                uriInfo
        )).thenReturn(Response.status(Response.Status.OK).build());

        // Invoke the controller method
        Response response = customerController.updateCustomer(customerId, new Customer(), uriInfo);

        // Verify that the response status is as expected
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testUpdateCustomerFailure() {
        Long customerId = 10L;
        UriInfo uriInfo = Mockito.mock(UriInfo.class);

        // Mock validation to return a set of violations, indicating failure
        Set<ConstraintViolation<Customer>> violations = Collections.singleton(
                Mockito.mock(ConstraintViolation.class)
        );
        when(validator.validate(any(Customer.class))).thenReturn(violations);

        // Mock the commonUtil to return an error response
        List<ErrorMessage> errorList = violations.stream()
                .map(violation -> new ErrorMessage(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()
                ))
                .collect(Collectors.toList());
        when(commonUtil.buildErrorResponse(
                "Validation failed",
                Response.Status.BAD_REQUEST,
                errorList,
                null,
                uriInfo
        )).thenReturn(Response.status(Response.Status.BAD_REQUEST).build());

        // Invoke the controller method
        Response response = customerController.updateCustomer(customerId, new Customer(), uriInfo);

        // Verify that the response status is as expected
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
}