package de.microtema.vo;

import lombok.Builder;
import lombok.Data;
import org.camunda.bpm.engine.repository.Resource;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Process;

import java.util.function.Function;

@Data
@Builder
public class ProcessDefinitionContext {

    private Resource resource;

    private BpmnModelInstance bpmnModelInstance;

    public Function<Process, Integer> definitionVersion;
}
