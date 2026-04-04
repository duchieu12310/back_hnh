package com.hnh.service;

import com.hnh.dto.ListResponse;
import com.hnh.mapper.GenericMapper;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Setter
@Scope("prototype")
public class GenericService<E, I, O> implements CrudService<Long, I, O> {

    private JpaRepository<E, Long> repository;
    private JpaSpecificationExecutor<E> specificationExecutor;
    private GenericMapper<E, I, O> mapper;
    private List<String> searchFields;
    private String resourceName;

    public <R extends JpaRepository<E, Long> & JpaSpecificationExecutor<E>> GenericService<E, I, O> init(
            R repository,
            GenericMapper<E, I, O> mapper,
            List<String> searchFields,
            String resourceName) {
        this.setRepository(repository);
        this.setSpecificationExecutor(repository);
        this.setMapper(mapper);
        this.setSearchFields(searchFields);
        this.setResourceName(resourceName);
        return this;
    }

    @Override
    public ListResponse<O> findAll(int page, int size, String sort, String filter, String search, boolean all) {
        return defaultFindAll(page, size, sort, filter, search, all, searchFields, specificationExecutor, mapper);
    }

    @Override
    public O findById(Long id) {
        return defaultFindById(id, repository, mapper, resourceName);
    }

    @Override
    public O save(I request) {
        return defaultSave(request, repository, mapper);
    }

    @Override
    public O save(Long id, I request) {
        return defaultSave(id, request, repository, mapper, resourceName);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void delete(List<Long> ids) {
        repository.deleteAllById(ids);
    }

    @Override
    public O updateStatus(Long id, Integer status) {
        E entity = repository.findById(id)
                .orElseThrow(() -> new com.hnh.exception.ResourceNotFoundException(resourceName, com.hnh.constant.FieldName.ID, id));
        try {
            java.lang.reflect.Field statusField = entity.getClass().getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(entity, status);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Entity " + resourceName + " does not have a 'status' field or it's not accessible", e);
        }
        entity = repository.save(entity);
        return mapper.entityToResponse(entity);
    }

}

