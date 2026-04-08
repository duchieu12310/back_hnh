package com.hnh.service.inventory;

import com.hnh.constant.ResourceName;
import com.hnh.constant.SearchFields;
import com.hnh.dto.ListResponse;
import com.hnh.dto.inventory.CountVariantRequest;
import com.hnh.dto.inventory.CountVariantResponse;
import com.hnh.entity.inventory.CountVariantKey;
import com.hnh.mapper.inventory.CountVariantMapper;
import com.hnh.repository.inventory.CountVariantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CountVariantServiceImpl implements CountVariantService {

    private CountVariantRepository countVariantRepository;

    private CountVariantMapper countVariantMapper;

    @Override
    public ListResponse<CountVariantResponse> findAll(int page, int size, String sort, String filter, String search, boolean all) {
        return defaultFindAll(page, size, sort, filter, search, all, SearchFields.COUNT_VARIANT, countVariantRepository, countVariantMapper);
    }

    @Override
    public CountVariantResponse findById(CountVariantKey id) {
        return defaultFindById(id, countVariantRepository, countVariantMapper, ResourceName.COUNT_VARIANT);
    }

    @Override
    public CountVariantResponse save(CountVariantRequest request) {
        return defaultSave(request, countVariantRepository, countVariantMapper);
    }

    @Override
    public CountVariantResponse save(CountVariantKey id, CountVariantRequest request) {
        return defaultSave(id, request, countVariantRepository, countVariantMapper, ResourceName.COUNT_VARIANT);
    }

    @Override
    public void delete(CountVariantKey id) {
        countVariantRepository.deleteById(id);
    }

    @Override
    public void delete(List<CountVariantKey> ids) {
        countVariantRepository.deleteAllById(ids);
    }

    @Override
    public CountVariantResponse updateStatus(CountVariantKey id, Integer status) {
        com.hnh.entity.inventory.CountVariant entity = countVariantRepository.findById(id)
                .orElseThrow(() -> new com.hnh.exception.ResourceNotFoundException(ResourceName.COUNT_VARIANT, com.hnh.constant.FieldName.ID, id));
        try {
            java.lang.reflect.Field statusField = entity.getClass().getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(entity, status);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Entity " + ResourceName.COUNT_VARIANT + " does not have a 'status' field or it's not accessible", e);
        }
        entity = countVariantRepository.save(entity);
        return countVariantMapper.entityToResponse(entity);
    }

}

