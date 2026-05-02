package com.hnh.mapper.authentication;

import com.hnh.dto.authentication.UserRequest;
import com.hnh.dto.authentication.UserResponse;
import com.hnh.dto.client.ClientPersonalSettingUserRequest;
import com.hnh.dto.client.ClientPhoneSettingUserRequest;
import com.hnh.dto.client.ClientEmailSettingUserRequest;
import com.hnh.entity.authentication.User;
import com.hnh.entity.address.Address;
import com.hnh.mapper.GenericMapper;
import com.hnh.utils.MapperUtils;
import com.hnh.service.geocode.GeocodeService;
import com.hnh.dto.geocode.GeocodeResponse;
import com.hnh.repository.address.*;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {MapperUtils.class})
public abstract class UserMapper implements GenericMapper<User, UserRequest, UserResponse> {

    @Autowired
    protected GeocodeService geocodeService;

    @Autowired
    protected AddressRepository addressRepository;

    @Autowired
    protected ProvinceRepository provinceRepository;
    @Autowired
    protected DistrictRepository districtRepository;
    @Autowired
    protected WardRepository wardRepository;

    @Autowired
    protected MapperUtils mapperUtils;

    @Override
    @Mapping(source = "password", target = "password", qualifiedByName = "hashPassword")
    @Mapping(target = "roles", qualifiedByName = "attachUserRoles")
    @Mapping(target = "addresses", ignore = true)
    public abstract User requestToEntity(UserRequest request);

    @Override
    @Mapping(source = "defaultAddress", target = "address")
    public abstract UserResponse entityToResponse(User entity);

    @Override
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(source = "password", target = "password", qualifiedByName = "hashPassword",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract User partialUpdate(@MappingTarget User entity, UserRequest request);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract User partialUpdate(@MappingTarget User entity, ClientPersonalSettingUserRequest request);

    @Mapping(target = "roles", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract User partialUpdate(@MappingTarget User entity, ClientPhoneSettingUserRequest request);

    @Mapping(target = "roles", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract User partialUpdate(@MappingTarget User entity, ClientEmailSettingUserRequest request);

    @AfterMapping
    protected void afterRegistration(@MappingTarget User entity, UserRequest request) {
        if (request.getAddress() != null) {
            Address address = new Address();
            updateAddressFields(address, request.getAddress());
            address.setIsDefault(true);
            address.setUser(entity);
            if (entity.getAddresses() == null) entity.setAddresses(new ArrayList<>());
            entity.getAddresses().add(address);
            
            // Lưu tọa độ
            fetchAndSetCoordinates(address, request.getAddress());
            
            // Lưu địa chỉ trước để tránh lỗi Transient
            addressRepository.save(address);
        }
    }

    @AfterMapping
    protected void afterPartialUpdate(@MappingTarget User entity, UserRequest request) {
        if (request.getRoles() != null) {
            entity.setRoles(mapperUtils.attachUserRoles(request.getRoles()));
        }
        if (request.getAddress() != null) {
            Address defaultAddr = entity.getDefaultAddress();
            boolean isNew = false;
            if (defaultAddr == null) {
                defaultAddr = new Address();
                defaultAddr.setIsDefault(true);
                defaultAddr.setUser(entity);
                entity.getAddresses().add(defaultAddr);
                isNew = true;
            }
            updateAddressFields(defaultAddr, request.getAddress());
            fetchAndSetCoordinates(defaultAddr, request.getAddress());
            
            // Lưu địa chỉ (quan trọng nhất để fix lỗi 500)
            addressRepository.save(defaultAddr);
        }
    }

    @AfterMapping
    protected void handleClientPersonalGeocoding(@MappingTarget User entity, ClientPersonalSettingUserRequest request) {
        if (request.getAddress() != null) {
            Address defaultAddr = entity.getDefaultAddress();
            if (defaultAddr != null) {
                updateAddressFields(defaultAddr, request.getAddress());
                fetchAndSetCoordinates(defaultAddr, request.getAddress());
                addressRepository.save(defaultAddr);
            }
        }
    }

    private void updateAddressFields(Address address, com.hnh.dto.address.AddressRequest request) {
        address.setLine(request.getLine());
        if (request.getProvinceId() != null) {
            address.setProvince(provinceRepository.findById(request.getProvinceId()).orElse(null));
        }
        if (request.getDistrictId() != null) {
            address.setDistrict(districtRepository.findById(request.getDistrictId()).orElse(null));
        }
        if (request.getWardId() != null) {
            address.setWard(wardRepository.findById(request.getWardId()).orElse(null));
        }
    }

    private void fetchAndSetCoordinates(Address address, com.hnh.dto.address.AddressRequest request) {
        GeocodeResponse coords = geocodeService.getCoordinates(
            request.getProvinceId(),
            request.getDistrictId(),
            request.getWardId()
        );
        if (coords != null) {
            address.setLatitude(coords.getLatitude());
            address.setLongitude(coords.getLongitude());
        }
    }
}
