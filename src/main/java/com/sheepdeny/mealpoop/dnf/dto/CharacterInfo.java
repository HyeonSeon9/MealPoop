package com.sheepdeny.mealpoop.dnf.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CharacterInfo {
    @JsonProperty("serverId")
    private String serverId;

    @JsonProperty("characterId")
    private String characterId;

    @JsonProperty("characterName")
    private String characterName;

    @JsonProperty("level")
    private int level;

    @JsonProperty("jobId")
    private String jobId;

    @JsonProperty("jobGrowId")
    private String jobGrowId;

    @JsonProperty("jobName")
    private String jobName;

    @JsonProperty("jobGrowName")
    private String jobGrowName;

    @JsonProperty("fame")
    private int fame;
}
