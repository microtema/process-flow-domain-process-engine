package de.microtema.filter;

import de.microtema.converter.ProcessToProcessDefinitionDTOConverter;
import de.microtema.kafka.ProcessDefinitionProducer;
import de.microtema.vo.ProcessDefinitionContext;
import de.microtema.vo.ProcessDefinitionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.Resource;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Component
@RequiredArgsConstructor
public class ProcessDefinitionRegistrationListener {

    private final RepositoryService repositoryService;

    private final ManagementService managementService;

    private final ProcessDefinitionProducer processDefinitionProducer;

    private final ProcessToProcessDefinitionDTOConverter definitionConverter;

    @EventListener
    void onApplicationReadyEvent(ApplicationReadyEvent event) {

        List<Resource> registeredDeployments = getRegisteredDeployments();

        List<ProcessDefinitionDTO> processDefinitions = getProcessDefinitions(registeredDeployments);

        processDefinitions.forEach(processDefinitionProducer::sendProcessDefinition);
    }

    private List<Resource> getRegisteredDeployments() {

        Set<String> registeredDeployments = managementService.getRegisteredDeployments();

        return registeredDeployments
                .stream()
                .map(repositoryService::getDeploymentResources)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<ProcessDefinitionDTO> getProcessDefinitions(Collection<Resource> resources) {

        return resources.stream()
                .map(this::getProcessDefinitions)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<ProcessDefinitionDTO> getProcessDefinitions(Resource resource) {

        BpmnModelInstance bpmnModelInstance = getBpmnModelInstance(resource);
        Collection<Process> executableProcesses = getExecutableProcesses(bpmnModelInstance);

        ProcessDefinitionContext definitionContext = ProcessDefinitionContext.builder()
                .resource(resource)
                .bpmnModelInstance(bpmnModelInstance)
                .definitionVersion(it -> getProcessDefinitionVersion(resource, it))
                .build();

        return definitionConverter.convertList(executableProcesses, definitionContext);
    }

    private Integer getProcessDefinitionVersion(Resource resource, Process process) {

        return repositoryService
                .createProcessDefinitionQuery()
                .deploymentId(resource.getDeploymentId())
                .processDefinitionKey(process.getId())
                .singleResult()
                .getVersion();
    }

    private BpmnModelInstance getBpmnModelInstance(Resource resource) {

        String deploymentId = resource.getDeploymentId();
        String name = resource.getName();

        InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, name);

        return Bpmn.readModelFromStream(inputStream);
    }

    private Collection<Process> getExecutableProcesses(BpmnModelInstance bpmnModelInstance) {

        Collection<Process> process = bpmnModelInstance.getModelElementsByType(Process.class);

        return process.stream().filter(Process::isExecutable).collect(Collectors.toList());
    }
}
