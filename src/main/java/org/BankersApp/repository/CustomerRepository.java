package org.BankersApp.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.BankersApp.dto.CustomerDTO;
import org.BankersApp.entity.Customer;

import java.util.List;

@ApplicationScoped
public class CustomerRepository implements PanacheRepositoryBase<Customer, Long> {

    @Inject
    EntityManager entityManager;

    public List<Customer> getCustomerByCriteria(String searchValue) {

        String nativeQuery = "SELECT * FROM customers WHERE CAST(customerId AS TEXT) ILIKE :searchValue OR firstName ILIKE :searchValue OR CAST(phoneNumber AS TEXT) ILIKE :searchValue OR lastName ILIKE :searchValue OR city ILIKE :searchValue";

        return entityManager.createNativeQuery(nativeQuery, Customer.class)
                .setParameter("searchValue", "%" + searchValue + "%")
                .getResultList();
    }
}
