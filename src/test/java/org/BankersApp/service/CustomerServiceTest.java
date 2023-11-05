package org.BankersApp.service;


import io.quarkus.test.InjectMock;
import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.BankersApp.dto.CustomerDTO;
import org.BankersApp.entity.Customer;
import org.BankersApp.exception.CustomeException;
import org.BankersApp.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.smallrye.common.constraint.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author ankushk
 */
@QuarkusTest
public class CustomerServiceTest {

    @Inject
    CustomerService customerService;

    @InjectMock
    CustomerRepository customerRepository;


    @Test
    public void testCreateCustomerSuccess() {

        Customer customer = new Customer();
        customer.setFirstName("FirstName");
        customer.setEmail("test@test.com");

        doNothing().when(customerRepository).persist(any(Customer.class));

        CustomerDTO customerDTO = customerService.createCustomer(customer);

        verify(customerRepository).persist(customer);

        assertEquals("FirstName", customerDTO.getFirstName());
        assertEquals("test@test.com", customerDTO.getEmail());
    }

    @Test
    public void testCreateCustomerFailed() {
        Customer customer = new Customer();

        doThrow(new CustomeException("Failed to create a data")).when(customerRepository).persist(customer);

        assertThrows(CustomeException.class, () -> customerService.createCustomer(customer));
    }

    @Test
    public void testGetCustomerByCriteriaSuccess() {
        String searchValue = "searchValue";
        List<Customer> customerList=new ArrayList<>();
        customerList.add(new Customer(1l,"FirstName","LastName","test@test.com",9899989898l,"City", Instant.now(), Instant.now()));
        customerList.add(new Customer(2l,"FirstName","LastName","test@test.com",9899989898l,"City",Instant.now(), Instant.now()));

        Mockito.when(customerRepository.getCustomerByCriteria(searchValue)).thenReturn(customerList);

        List<CustomerDTO> result = customerService.getCustomerByCriteria(searchValue);

        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    public void testGetCustomerByCriteriaFailed() {
        String searchValue = "searchValue";
        when(customerRepository.getCustomerByCriteria(searchValue)).thenReturn(Collections.emptyList());

        CustomeException exception = assertThrows(CustomeException.class, () -> {
            customerService.getCustomerByCriteria(searchValue);
        });

        assertEquals("Data not in DB", exception.getMessage());
    }






}