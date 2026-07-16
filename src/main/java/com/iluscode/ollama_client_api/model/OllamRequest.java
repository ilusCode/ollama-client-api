package com.iluscode.ollama_client_api.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OllamRequest {
    private String model;
    private String prompt;
    private boolean stream;
}
