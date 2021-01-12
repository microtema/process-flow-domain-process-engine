package de.microtema.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProcessDefinitionDTO {

    @NotNull
    private String deploymentId;

    @NotNull
    private String boundedContext;

    @NotNull
    private String processId;

    @NotNull
    private String displayName;

    @NotNull
    private Integer majorVersion;

    @NotNull
    private Integer definitionVersion;

    private String description;

    private String diagram;

    @NotNull
    private LocalDateTime deployTime;
}
