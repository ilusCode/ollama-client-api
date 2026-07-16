package com.iluscode.ollama_client_api.controller;

import com.iluscode.ollama_client_api.model.OllamaClientRequestBody;
import com.iluscode.ollama_client_api.model.OllamaClientResponseBody;
import com.iluscode.ollama_client_api.model.OllamaResponse;
import com.iluscode.ollama_client_api.service.IConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping("/api")
@RequiredArgsConstructor
public class OllamaController {

    private final IConversationService iConversationService;

    @PostMapping("/chat")
    public Mono<ResponseEntity<OllamaClientResponseBody>> postConversation(@RequestBody OllamaClientRequestBody requestBody){
        return iConversationService.sendMessage(requestBody)
                .map(ResponseEntity::ok);
    }
}
