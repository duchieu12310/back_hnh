package com.hnh.service.order;

import com.hnh.constant.ResourceName;
import com.hnh.constant.SearchFields;
import com.hnh.dto.ListResponse;
import com.hnh.dto.order.OrderVariantRequest;
import com.hnh.dto.order.OrderVariantResponse;
import com.hnh.entity.order.OrderVariantKey;
import com.hnh.mapper.order.OrderVariantMapper;
import com.hnh.repository.order.OrderVariantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderVariantServiceImpl implements OrderVariantService {

    private OrderVariantRepository orderVariantRepository;

    private OrderVariantMapper orderVariantMapper;

    @Override
    public ListResponse<OrderVariantResponse> findAll(int page, int size, String sort, String filter, String search, boolean all) {
        return defaultFindAll(page, size, sort, filter, search, all, SearchFields.ORDER_VARIANT, orderVariantRepository, orderVariantMapper);
    }

    @Override
    public OrderVariantResponse findById(OrderVariantKey id) {
        return defaultFindById(id, orderVariantRepository, orderVariantMapper, ResourceName.ORDER_VARIANT);
    }

    @Override
    public OrderVariantResponse save(OrderVariantRequest request) {
        return defaultSave(request, orderVariantRepository, orderVariantMapper);
    }

    @Override
    public OrderVariantResponse save(OrderVariantKey id, OrderVariantRequest request) {
        return defaultSave(id, request, orderVariantRepository, orderVariantMapper, ResourceName.ORDER_VARIANT);
    }

    @Override
    public void delete(OrderVariantKey id) {
        orderVariantRepository.deleteById(id);
    }

    @Override
    public void delete(List<OrderVariantKey> ids) {
        orderVariantRepository.deleteAllById(ids);
    }

    @Override
    public OrderVariantResponse updateStatus(OrderVariantKey id, Integer status) {
        throw new RuntimeException("Entity " + ResourceName.ORDER_VARIANT + " does not have a 'status' field");
    }

}
