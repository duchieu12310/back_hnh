package com.hnh.service.address;

import com.hnh.constant.ResourceName;
import com.hnh.constant.SearchFields;
import com.hnh.dto.ListResponse;
import com.hnh.dto.address.ProvinceRequest;
import com.hnh.dto.address.ProvinceResponse;
import com.hnh.mapper.address.ProvinceMapper;
import com.hnh.repository.address.ProvinceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProvinceServiceImpl implements ProvinceService {

    private ProvinceRepository provinceRepository;

    private ProvinceMapper provinceMapper;

    @Override
    public ListResponse<ProvinceResponse> findAll(int page, int size, String sort, String filter, String search, boolean all) {
        return defaultFindAll(page, size, sort, filter, search, all, SearchFields.PROVINCE, provinceRepository, provinceMapper);
    }

    @Override
    public ProvinceResponse findById(Long id) {
        return defaultFindById(id, provinceRepository, provinceMapper, ResourceName.PROVINCE);
    }

    @Override
    public ProvinceResponse save(ProvinceRequest request) {
        return defaultSave(request, provinceRepository, provinceMapper);
    }

    @Override
    public ProvinceResponse save(Long id, ProvinceRequest request) {
        return defaultSave(id, request, provinceRepository, provinceMapper, ResourceName.PROVINCE);
    }

    @Override
    public void delete(Long id) {
        provinceRepository.deleteById(id);
    }

    @Override
    public void delete(List<Long> ids) {
        provinceRepository.deleteAllById(ids);
    }

    @Override
    public ProvinceResponse updateStatus(Long id, Integer status) {
        com.hnh.entity.address.Province entity = provinceRepository.findById(id)
                .orElseThrow(() -> new com.hnh.exception.ResourceNotFoundException(ResourceName.PROVINCE, com.hnh.constant.FieldName.ID, id));
        try {
            java.lang.reflect.Field statusField = entity.getClass().getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(entity, status);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Entity " + ResourceName.PROVINCE + " does not have a 'status' field or it's not accessible", e);
        }
        entity = provinceRepository.save(entity);
        return provinceMapper.entityToResponse(entity);
    }

}

