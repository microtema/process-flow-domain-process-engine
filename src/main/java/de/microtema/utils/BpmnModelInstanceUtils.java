package de.microtema.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperties;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperty;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@UtilityClass
public class BpmnModelInstanceUtils {

    public static String getExtensionPropertyValue(FlowElement flowElement, String propertyName) {

        CamundaProperty camundaProperty = getCamundaProperty(flowElement.getExtensionElements(), propertyName);

        return Optional.ofNullable(camundaProperty).map(CamundaProperty::getCamundaValue).orElse(null);
    }

    public static String getExtensionPropertyValue(BaseElement baseElement, String propertyName) {

        CamundaProperty camundaProperty = getCamundaProperty(baseElement.getExtensionElements(), propertyName);

        return Optional.ofNullable(camundaProperty).map(CamundaProperty::getCamundaValue).orElse(null);
    }

    public static String getDescription(Process process) {

        return process.getDocumentations()
                .stream()
                .map(ModelElementInstance::getTextContent)
                .filter(StringUtils::isNotEmpty)
                .findFirst()
                .orElse(process.getName());
    }

    private static CamundaProperty getCamundaProperty(ExtensionElements extensionElements, String propertyName) {

        if (Objects.isNull(extensionElements)) {
            return null;
        }

        List<CamundaProperties> camundaProperties = extensionElements.getElementsQuery().filterByType(CamundaProperties.class).list();

        return camundaProperties.stream()
                .map(CamundaProperties::getCamundaProperties)
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(Collection::stream)
                .filter(it -> StringUtils.equalsIgnoreCase(it.getCamundaName(), propertyName)).findFirst().orElse(null);
    }
}
