package com.example.geco.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttractionResponse {
    private int attractionId;
    private String name;
    private String description;
}