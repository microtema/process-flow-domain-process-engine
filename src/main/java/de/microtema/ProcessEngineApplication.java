package de.microtema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ProcessEngineApplication {

    private static ApplicationContext context;

    public static void main(String[] args) {
        context = SpringApplication.run(ProcessEngineApplication.class, args);
    }

    public static ApplicationContext getContext(){

        return context;
    }
}
