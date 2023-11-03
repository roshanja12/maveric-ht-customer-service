package org.BankersApp.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.BankersApp.dto.CustomerDTO;
import org.BankersApp.entity.Customer;
import org.BankersApp.exception.CustomeException;
import org.BankersApp.repository.CustomerRepository;
import org.jboss.logging.Logger;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;


@ApplicationScoped
public class CustomerService {
    @Inject
    CustomerRepository customerRepository;

    @Inject
    Logger logger;


    @Transactional
    public CustomerDTO createCustomer(Customer customer) {
        logger.info("createCustomer method called");
        try
        {
             customer.setCreatedAt(Instant.now());
             customer.setModifiedAt(Instant.now());
             customerRepository.persist(customer);
            return entityToDTO(customer);}
        catch (Exception ex)
        {
            logger.error("Failed to create a customer: " + ex.getMessage());
            throw new CustomeException("Failed to create a customer.");
        }
    }

    @Transactional
    public CustomerDTO getCustomerByCustomerId(Long customerId) {
        logger.info("getCustomerByCustomerId method called");
        Customer customers = customerRepository.findById(customerId);

        if(customers!=null)
        {
            return entityToDTO(customers);
        }
        else
        {
            logger.error("Customer not found in the database with ID: " + customerId);
            throw new CustomeException("Customer not found in the database.");
        }
    }

    @Transactional
    public List<CustomerDTO> getCustomerByCriteria(String searchValue) {
        logger.info("getCustomerByCriteria method called");
        List<CustomerDTO> customerList;
        if (searchValue == null || searchValue.isEmpty()) {
            customerList = customerRepository.findAll().stream().map(this::entityToDTO).collect(Collectors.toList());
        } else {
            customerList = customerRepository.getCustomerByCriteria(searchValue).stream().map(this::entityToDTO).collect(Collectors.toList());
        }
        if (!customerList.isEmpty()) {
            return customerList;
        } else {
            logger.error("No customers found in the database with the given Field: " + searchValue);
            throw new CustomeException("No customers found in the database with the given Field.");
        }
    }


    public CustomerDTO entityToDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setCustomerId(customer.getCustomerId());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setCity(customer.getCity());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        return customerDTO;
    }


}
