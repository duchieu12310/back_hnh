package com.hnh.service.waybill;

import com.hnh.dto.waybill.GhnCallbackOrderRequest;
import com.hnh.dto.waybill.WaybillRequest;
import com.hnh.dto.waybill.WaybillResponse;
import com.hnh.service.CrudService;

public interface WaybillService extends CrudService<Long, WaybillRequest, WaybillResponse> {

    void callbackStatusWaybillFromGHN(GhnCallbackOrderRequest ghnCallbackOrderRequest);

}

