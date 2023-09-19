package com.beetech.springsecurity.web.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(MailSenderAutoConfiguration.class)

public class ModelMapperConfiguration {
    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
