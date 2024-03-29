package com.kasiarakos.messageservice.dto;

public record AccountMessageDto(Long accountNumber,
                                String name,
                                String email,
                                String mobileNumber) {
}
