package com.example.vaadin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

@Configuration
public class TestVaadinApplication {

    @Bean
    @ServiceConnection
    @RestartScope
    PostgreSQLContainer postgreSQLContainer() {
        return new PostgreSQLContainer("postgres:15.1-alpine");
    }

    public static void main(String[] arrrrghImAPirate) {
        SpringApplication
                .from(VaadinApplication::main)
                .with(TestVaadinApplication.class)
                .run(arrrrghImAPirate);

    }
}
