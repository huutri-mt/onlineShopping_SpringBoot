package com.example.onlineshopping.service;


import com.example.onlineshopping.constan.VNPayParams;
import com.example.onlineshopping.constan.VnpIpnResponseConst;
import com.example.onlineshopping.dto.Response.IpnResponse;
import com.example.onlineshopping.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class VNPayIpnHandler implements IpnHandler {

    private final VNPayService vnPayService;

    private final OrderService orderService;


    public IpnResponse process(Map<String, String> params) {
        if (!vnPayService.verifyIpn(params)) {
            return VnpIpnResponseConst.SIGNATURE_FAILED;
        }

        IpnResponse response;
        var txnRef = params.get(VNPayParams.TXN_REF);
        try {
            int orderId = (int) Long.parseLong(txnRef);
            orderService.markOrder(orderId, params);
            response = VnpIpnResponseConst.SUCCESS;
        }
        catch (AppException e) {
            switch (e.getErrorCode()) {
                case ORDER_NOT_FOUND -> response = VnpIpnResponseConst.ORDER_NOT_FOUND;
                default -> response = VnpIpnResponseConst.UNKNOWN_ERROR;
            }
        }
        catch (Exception e) {
            response = VnpIpnResponseConst.UNKNOWN_ERROR;
        }

        log.info("[VNPay Ipn] txnRef: {}, response: {}", txnRef, response);
        return response;
    }
}
