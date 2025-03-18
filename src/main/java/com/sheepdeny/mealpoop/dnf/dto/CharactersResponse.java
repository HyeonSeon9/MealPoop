package com.sheepdeny.mealpoop.dnf.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class CharactersResponse {
    @JsonProperty("rows")
    private List<CharacterInfo> rows;

}
