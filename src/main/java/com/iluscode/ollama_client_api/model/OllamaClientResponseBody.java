package com.iluscode.ollama_client_api.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OllamaClientResponseBody {
    private String message;
    private String time;
}
