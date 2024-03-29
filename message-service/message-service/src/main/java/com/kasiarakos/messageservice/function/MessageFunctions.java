package com.kasiarakos.messageservice.function;

import com.kasiarakos.messageservice.dto.AccountMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.function.Function;

@Configuration
public class MessageFunctions {
    private static final Logger log = LoggerFactory.getLogger(MessageFunctions.class);

    @Bean
    public Function<AccountMessageDto, AccountMessageDto> email() {
        return accountMessageDto -> {
            log.info("Sending email with the details {}", accountMessageDto);
            return accountMessageDto;
        };
    }

    @Bean
    public Function<AccountMessageDto, Long> sms() {
        return accountMessageDto -> {
            log.info("Sending sms with the details {}", accountMessageDto);
            return accountMessageDto.accountNumber();
        };
    }
}
