package com.kasiarakos.accountservice.adapters.functions;

import com.kasiarakos.accountservice.domain.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import java.util.function.Consumer;

@Slf4j
@Configuration
public class AccountsFunctions {
    @Bean
    public Consumer<Message<Long>> updateCommunication(AccountService accountService) {

        return (message) -> {
            accountService.updateCommunicationStatus(message.getPayload());
            log.info("Updating communication status for the account number with message {}", message);
        };
    }

}
