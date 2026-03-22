package com.thatannhien.service.waybill;

import com.thatannhien.dto.waybill.GhnCallbackOrderRequest;
import com.thatannhien.dto.waybill.WaybillRequest;
import com.thatannhien.dto.waybill.WaybillResponse;
import com.thatannhien.service.CrudService;

public interface WaybillService extends CrudService<Long, WaybillRequest, WaybillResponse> {

    void callbackStatusWaybillFromGHN(GhnCallbackOrderRequest ghnCallbackOrderRequest);

}
