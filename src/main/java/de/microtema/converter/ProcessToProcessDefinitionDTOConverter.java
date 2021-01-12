package de.microtema.converter;

import de.microtema.model.converter.MetaConverter;
import de.microtema.utils.BpmnModelInstanceUtils;
import de.microtema.vo.ProcessDefinitionContext;
import de.microtema.vo.ProcessDefinitionDTO;
import org.apache.commons.collections4.KeyValue;
import org.camunda.bpm.engine.impl.persistence.entity.ResourceEntity;
import org.camunda.bpm.engine.repository.Resource;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static de.microtema.utils.BpmnModelInstanceUtils.getExtensionPropertyValue;

@Component
public class ProcessToProcessDefinitionDTOConverter implements MetaConverter<ProcessDefinitionDTO, Process, ProcessDefinitionContext> {

    @Override
    public void update(ProcessDefinitionDTO dest, Process orig, ProcessDefinitionContext meta) {

        Resource resource = meta.getResource();
        BpmnModelInstance bpmnModelInstance = meta.getBpmnModelInstance();


        dest.setProcessId(orig.getId());
        dest.setDisplayName(Optional.ofNullable(orig.getName()).orElseGet(orig::getId));
        dest.setMajorVersion(getMajorVersion(orig));
        dest.setBoundedContext(getExtensionPropertyValue(orig, "boundedContext"));
        dest.setDescription(BpmnModelInstanceUtils.getDescription(orig));

        dest.setDefinitionVersion(meta.getDefinitionVersion().apply(orig));

        dest.setDiagram(Bpmn.convertToString(bpmnModelInstance));

        dest.setDeploymentId(resource.getDeploymentId());
        dest.setDeployTime(getLocalDateTimeOrDefault(resource));
    }

    private Integer getMajorVersion(Process orig) {

        String camundaVersionTag = orig.getCamundaVersionTag();

        return Integer.valueOf(camundaVersionTag);
    }

    private LocalDateTime getLocalDateTimeOrDefault(Resource resource) {

        if (!(resource instanceof ResourceEntity)) {

            return LocalDateTime.now();
        }

        ResourceEntity resourceEntity = (ResourceEntity) resource;

        Date createTime = resourceEntity.getCreateTime();

        return LocalDateTime.ofInstant(createTime.toInstant(), ZoneId.systemDefault());
    }
}
