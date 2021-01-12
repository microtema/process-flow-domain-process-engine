package de.microtema.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "register-process")
public class RegisterProcessProperties {

    private String topicName;
}
