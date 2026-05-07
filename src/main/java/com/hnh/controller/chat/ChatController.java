package com.hnh.controller.chat;

import com.hnh.constant.AppConstants;
import com.hnh.dto.ListResponse;
import com.hnh.dto.chat.MessageRequest;
import com.hnh.dto.chat.MessageResponse;
import com.hnh.service.chat.MessageService;
import com.hnh.service.openai.OpenAiService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@CrossOrigin(AppConstants.FRONTEND_HOST)
public class ChatController {

    private SimpMessagingTemplate simpMessagingTemplate;
    private MessageService messageService;
    private OpenAiService openAiService;

    @MessageMapping("/{roomId}")
    public void sendMessage(@DestinationVariable String roomId, @Payload MessageRequest message) {
        MessageResponse messageResponse = messageService.save(message);
        simpMessagingTemplate.convertAndSend("/chat/receive/" + roomId, messageResponse);
    }

    @GetMapping("/messages")
    public ResponseEntity<ListResponse<MessageResponse>> getAllMessages(
            @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sort", defaultValue = AppConstants.DEFAULT_SORT) String sort,
            @RequestParam(name = "filter", required = false) @Nullable String filter,
            @RequestParam(name = "search", required = false) @Nullable String search,
            @RequestParam(name = "all", required = false) boolean all
    ) {
        ListResponse<MessageResponse> messageResponses = messageService.findAll(page, size, sort, filter, search, all);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponses);
    }

    @org.springframework.web.bind.annotation.PostMapping("/chat/book-search")
    public java.util.Map<String, String> bookSearch(@org.springframework.web.bind.annotation.RequestBody java.util.Map<String, String> request) {
        String message = request.get("message");
        String aiResponse = openAiService.chatWithGpt(message);

        java.util.Map<String, String> result = new java.util.HashMap<>();
        result.put("reply", aiResponse);
        return result;
    }

}

