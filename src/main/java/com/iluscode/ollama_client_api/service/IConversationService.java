package com.iluscode.ollama_client_api.service;

import com.iluscode.ollama_client_api.model.OllamaClientRequestBody;
import com.iluscode.ollama_client_api.model.OllamaClientResponseBody;
import reactor.core.publisher.Mono;

public interface IConversationService {

    Mono<OllamaClientResponseBody> sendMessage(OllamaClientRequestBody requestBody);
}
