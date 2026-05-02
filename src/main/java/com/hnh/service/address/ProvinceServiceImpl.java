package com.hnh.service.address;

import com.hnh.constant.FieldName;
import com.hnh.constant.ResourceName;
import com.hnh.constant.SearchFields;
import com.hnh.dto.ListResponse;
import com.hnh.dto.address.ProvinceRequest;
import com.hnh.dto.address.ProvinceResponse;
import com.hnh.entity.address.Province;
import com.hnh.exception.ResourceNotFoundException;
import com.hnh.mapper.address.ProvinceMapper;
import com.hnh.repository.address.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinceServiceImpl implements ProvinceService {

    private final ProvinceRepository provinceRepository;
    private final ProvinceMapper provinceMapper;

    @Autowired
    public ProvinceServiceImpl(ProvinceRepository provinceRepository, ProvinceMapper provinceMapper) {
        this.provinceRepository = provinceRepository;
        this.provinceMapper = provinceMapper;
    }

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
        Province entity = provinceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceName.PROVINCE, FieldName.ID, id));
        
        try {
            java.lang.reflect.Field statusField = entity.getClass().getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(entity, status);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Entity " + ResourceName.PROVINCE + " does not have a 'status' field", e);
        }
        
        entity = provinceRepository.save(entity);
        return provinceMapper.entityToResponse(entity);
    }

}
