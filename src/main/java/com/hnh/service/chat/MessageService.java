package com.hnh.service.chat;

import com.hnh.dto.chat.MessageRequest;
import com.hnh.dto.chat.MessageResponse;
import com.hnh.service.CrudService;

public interface MessageService extends CrudService<Long, MessageRequest, MessageResponse> {}

