package org.BankersApp.serilization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.BankersApp.entity.InsightsCustomers;
import org.apache.kafka.common.serialization.Serializer;

public class InsightsCustomersSerializer implements Serializer<InsightsCustomers> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public InsightsCustomersSerializer() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public byte[] serialize(String topic, InsightsCustomers data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing InsightsCustomers", e);
        }
    }
}
