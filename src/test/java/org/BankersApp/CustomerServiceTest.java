package org.BankersApp;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.UriInfo;
import org.BankersApp.dto.CustomerDTO;
import org.BankersApp.entity.Customer;
import org.BankersApp.exception.CustomeException;
import org.BankersApp.repository.CustomerRepository;
import org.BankersApp.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
/**
 * @Rohit
 */

@QuarkusTest
public class CustomerServiceTest {
    @Inject
    CustomerService customerService;

    @InjectMock
    CustomerRepository customerRepository;

    @Test
    public void testDeleteCustomerSuccess() {
        Long customerId = 10L;
        Customer existingCustomer = new Customer();
        existingCustomer.setCustomerId(customerId);
        when(customerRepository.findById(customerId)).thenReturn(existingCustomer);
        CustomerDTO deletedCustomerDTO = customerService.deleteCustomer(customerId);
        assertNotNull(deletedCustomerDTO);
        assertEquals(customerId, deletedCustomerDTO.getCustomerId());
    }

    @Test
    public void testDeleteCustomerNotFound() {
        Long customerId = 2L;
        when(customerRepository.findById(customerId)).thenReturn(null);
        assertThrows(CustomeException.class, () -> customerService.deleteCustomer(customerId));
    }

    @Test
    public void testUpdateCustomerSuccess() {
        Long customerId = 10L;
        UriInfo uriInfo = Mockito.mock(UriInfo.class);
        Customer updatedCustomer = new Customer();
        updatedCustomer.setCustomerId(customerId);
        updatedCustomer.setEmail("rohan123@gmail.com");
        updatedCustomer.setPhoneNumber(Long.valueOf("9403821914"));
        updatedCustomer.setCity("Delhi");
        when(customerRepository.findById(customerId)).thenReturn(updatedCustomer);
        CustomerDTO updatedCustomerDTO = customerService.updateCustomer(updatedCustomer);
        assertNotNull(updatedCustomerDTO);
        assertEquals("rohan123@gmail.com", updatedCustomerDTO.getEmail());
        assertEquals(Long.valueOf("9403821914"), updatedCustomerDTO.getPhoneNumber());
        assertEquals("Delhi", updatedCustomerDTO.getCity());
    }

    @Test
    public void testUpdateCustomerNotFound() {
        Long customerId = 20L;
        UriInfo uriInfo = Mockito.mock(UriInfo.class);
        when(customerRepository.findById(customerId)).thenReturn(null);
        assertThrows(CustomeException.class, () -> customerService.updateCustomer(new Customer()));
    }
}



