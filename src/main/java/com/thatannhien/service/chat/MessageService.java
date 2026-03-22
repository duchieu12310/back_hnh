package com.thatannhien.service.chat;

import com.thatannhien.dto.chat.MessageRequest;
import com.thatannhien.dto.chat.MessageResponse;
import com.thatannhien.service.CrudService;

public interface MessageService extends CrudService<Long, MessageRequest, MessageResponse> {}
