package com.hnh.service.chat;

import com.hnh.constant.ResourceName;
import com.hnh.constant.SearchFields;
import com.hnh.dto.ListResponse;
import com.hnh.dto.chat.MessageRequest;
import com.hnh.dto.chat.MessageResponse;
import com.hnh.entity.chat.Message;
import com.hnh.mapper.chat.MessageMapper;
import com.hnh.repository.authentication.UserRepository;
import com.hnh.repository.chat.MessageRepository;
import com.hnh.repository.chat.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private MessageRepository messageRepository;
    private RoomRepository roomRepository;
    private UserRepository userRepository;
    private MessageMapper messageMapper;

    @Override
    public ListResponse<MessageResponse> findAll(int page, int size, String sort, String filter, String search, boolean all) {
        return defaultFindAll(page, size, sort, filter, search, all, SearchFields.MESSAGE, messageRepository, messageMapper);
    }

    @Override
    public MessageResponse findById(Long id) {
        return defaultFindById(id, messageRepository, messageMapper, ResourceName.MESSAGE);
    }

    @Override
    public MessageResponse save(MessageRequest request) {
        Message message = messageMapper.requestToEntity(request);

        userRepository.findById(request.getUserId()).ifPresent(message::setUser);

        // (1) Save message
        Message messageAfterSave = messageRepository.save(message);

        // (2) Save room
        roomRepository.findById(request.getRoomId())
                .ifPresent(room -> {
                    room.setUpdatedAt(Instant.now());
                    room.setLastMessage(messageAfterSave);
                    roomRepository.save(room);
                });

        return messageMapper.entityToResponse(messageAfterSave);
    }

    @Override
    public MessageResponse save(Long id, MessageRequest request) {
        return defaultSave(id, request, messageRepository, messageMapper, ResourceName.MESSAGE);
    }

    @Override
    public void delete(Long id) {
        messageRepository.deleteById(id);
    }

    @Override
    public void delete(List<Long> ids) {
        messageRepository.deleteAllById(ids);
    }

    @Override
    public MessageResponse updateStatus(Long id, Integer status) {
        Message entity = messageRepository.findById(id)
                .orElseThrow(() -> new com.hnh.exception.ResourceNotFoundException(ResourceName.MESSAGE, com.hnh.constant.FieldName.ID, id));
        try {
            java.lang.reflect.Field statusField = entity.getClass().getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(entity, status);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Entity " + ResourceName.MESSAGE + " does not have a 'status' field or it's not accessible", e);
        }
        entity = messageRepository.save(entity);
        return messageMapper.entityToResponse(entity);
    }

}

