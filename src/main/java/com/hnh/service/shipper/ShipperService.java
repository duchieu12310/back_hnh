package com.hnh.service.shipper;

import com.hnh.dto.waybill.WaybillResponse;
import java.util.List;

public interface ShipperService {
    List<WaybillResponse> getAvailableWaybills(Double currentLat, Double currentLng);
    WaybillResponse confirmPickup(Long waybillId, String username);
}
