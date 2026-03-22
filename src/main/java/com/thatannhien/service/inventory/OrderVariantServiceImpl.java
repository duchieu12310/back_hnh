package com.thatannhien.service.inventory;

import com.thatannhien.constant.ResourceName;
import com.thatannhien.constant.SearchFields;
import com.thatannhien.dto.ListResponse;
import com.thatannhien.dto.order.OrderVariantRequest;
import com.thatannhien.dto.order.OrderVariantResponse;
import com.thatannhien.entity.order.OrderVariantKey;
import com.thatannhien.mapper.order.OrderVariantMapper;
import com.thatannhien.repository.order.OrderVariantRepository;
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

}
