package org.BankersApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "insights_customers")
public class InsightsCustomers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    public Long id;
    @Column(nullable = false)
    public String year;
    @Column(nullable = false)
    public String month;
    @Column(name="customer_id",nullable = false)
    public Long customerId;
    @Column(nullable = false)
    public String city;
    @Column(nullable = false)
    public Instant createdAt;
    @Enumerated(EnumType.STRING)
    public Type type;
}
