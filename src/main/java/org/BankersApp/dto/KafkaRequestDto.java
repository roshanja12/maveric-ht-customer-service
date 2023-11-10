package org.BankersApp.dto;

import lombok.Data;
import org.BankersApp.entity.Type;

@Data
public class KafkaRequestDto {
    public Type type;
	public Object  message;
}
