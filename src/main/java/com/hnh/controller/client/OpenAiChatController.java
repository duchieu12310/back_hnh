package com.hnh.controller.client;

import com.hnh.payload.request.ChatRequest;
import com.hnh.payload.response.ChatResponse;
import com.hnh.service.openai.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@CrossOrigin("*")
public class OpenAiChatController {

    private final OpenAiService openAiService;

    @PostMapping("/book-search")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        String reply = openAiService.chatWithGpt(request.getMessage());
        return ResponseEntity.ok(new ChatResponse(reply));
    }
}
