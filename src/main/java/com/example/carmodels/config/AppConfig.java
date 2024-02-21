package com.example.carmodels.config;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {


    /**
     * Creates and configures a ModelMapper instance with a custom LocalDateTime to String converter.
     * The converter formats LocalDateTime objects to a custom date format.
     *
     * @return A ModelMapper instance with custom configuration.
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}