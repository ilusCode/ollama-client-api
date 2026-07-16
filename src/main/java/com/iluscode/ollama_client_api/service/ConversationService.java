package com.iluscode.ollama_client_api.service;

import com.iluscode.ollama_client_api.model.OllamRequest;
import com.iluscode.ollama_client_api.model.OllamaClientRequestBody;
import com.iluscode.ollama_client_api.model.OllamaClientResponseBody;
import com.iluscode.ollama_client_api.model.OllamaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConversationService implements IConversationService {

    private final WebClient client;

    @Value("${api.ia.model}")
    private String modelIA;

    @Value("${api.ia.stream}")
    private boolean streamMode;

    @Value("${service.ollama-service}")
    private String ollamaService;

    @Override
    public Mono<OllamaClientResponseBody> sendMessage(OllamaClientRequestBody requestBody){

        OllamRequest req = OllamRequest.builder()
                .model(modelIA)
                .prompt(requestBody.getMessage())
                .stream(streamMode)
                .build();

        String request = new ObjectMapper().writeValueAsString(req);

        return client.post().uri(ollamaService)
                .bodyValue(request)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(OllamaResponse.class)
                .map(ollamaResponse -> OllamaClientResponseBody.builder()
                        .message(ollamaResponse.getResponse())
                        .time(LocalDateTime.now().toString())
                        .build());
    }
}
