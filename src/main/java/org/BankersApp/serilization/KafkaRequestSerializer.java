package org.BankersApp.serilization;

import io.quarkus.kafka.client.serialization.ObjectMapperSerializer;
import org.BankersApp.dto.KafkaRequestDto;

public class KafkaRequestSerializer extends ObjectMapperSerializer<KafkaRequestDto> {
    @Override
    public byte[] serialize(String topic, KafkaRequestDto data) {
        return super.serialize(topic, data);
    }
}
