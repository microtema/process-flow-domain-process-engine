package de.microtema.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.microtema.vo.ProcessDefinitionDTO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class ProcessDefinitionProducer {

    private final ObjectMapper objectMapper;

    private final RegisterProcessProperties registerProcessProperties;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendProcessDefinition(ProcessDefinitionDTO processDefinition) {

        String topicName = registerProcessProperties.getTopicName();

        String event = convert(processDefinition);

        log.info(() -> "fire event: " + topicName);

        kafkaTemplate.send(topicName, event);
    }

    @SneakyThrows
    private String convert(ProcessDefinitionDTO processDefinition) {

        return objectMapper.writeValueAsString(processDefinition);
    }
}
