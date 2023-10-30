package org.BankersApp.service;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.BankersApp.dto.ResponseDTO;
import org.hibernate.exception.ConstraintViolationException;

import jakarta.ws.rs.NotFoundException;
import org.BankersApp.dto.CustomerDTO;
import org.BankersApp.entity.Customer;
import org.BankersApp.exception.CustomeException;
import org.BankersApp.repository.CustomerRepository;
import org.jboss.logging.Logger;


import java.text.CharacterIterator;
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
        logger.info("created  method service called!!!!!!!!!!!!");
        try
        {
            customer.setCreatedAt(Instant.now());
             customer.setModifiedAt(Instant.now());
             customerRepository.persist(customer);
            return entityToDTO(customer);}
        catch (CustomeException ex)
        {
            throw new CustomeException("Failed to create a data");
        }
    }

    @Transactional
    public List<CustomerDTO> getAllCustomers() {
        logger.info("Service called for getCustomer all.......!");
        List<CustomerDTO> customerList= customerRepository.findAll().stream().map(this::entityToDTO).collect(Collectors.toList());
        logger.info("CustomerList++++++"+customerList);
        if(customerList.size()!=0)
        {
            return customerList;
        }
        else
        {
            throw new CustomeException("Data not in DB");
        }
    }

    @Transactional
    public List<CustomerDTO> getCustomerByCriteria(String searchValue) {
        List<CustomerDTO> customerList = customerRepository.getCustomerByCriteria(searchValue).stream().map(this::entityToDTO).collect(Collectors.toList());
        logger.info("CustomerList" + customerList);
        if (customerList.size() != 0) {
            return customerList;
        } else {
            throw new CustomeException("Data not in DB");
        }
    }

    @Transactional
    public CustomerDTO updateCustomer(Customer customer) throws CustomeException {
        Customer existingCustomer = customerRepository.findById(customer.getCustomerId());
        if (existingCustomer != null) {
            existingCustomer.setModifiedAt(Instant.now());
            existingCustomer.setEmail(customer.getEmail());
            existingCustomer.setPhoneNumber(customer.getPhoneNumber());
            existingCustomer.setCity(customer.getCity());
            customerRepository.getEntityManager().merge(existingCustomer);
            return entityToDTO(existingCustomer);
        } else {
            throw new CustomeException("Customer with ID " + customer.getCustomerId() + " not found");
        }
    }

    @Transactional
    public String deleteCustomer(Long customerId) throws CustomeException {
        Customer existingCustomer = customerRepository.findById(customerId);
        if (existingCustomer != null) {
            customerRepository.deleteById(customerId);
            return "Customer with ID " + customerId + " successfully deleted";
        } else {
            throw new CustomeException("Customer with ID " + customerId + " not found");
        }
    }

    private CustomerDTO entityToDTO(Customer customer) {
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
