package com.example.store.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO запроса создания магазина")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreRequest {

    @Schema
    @JsonProperty("name")
    @NotBlank
    private String name;

    @Schema
    @JsonProperty("location")
    @NotBlank
    private String location;

    @Schema
    @JsonProperty("email")
    @NotBlank
    private String email;

}
