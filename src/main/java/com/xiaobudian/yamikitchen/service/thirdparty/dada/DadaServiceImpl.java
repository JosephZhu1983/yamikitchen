package com.xiaobudian.yamikitchen.service.thirdparty.dada;

import com.google.gson.Gson;
import com.xiaobudian.yamikitchen.domain.merchant.Merchant;
import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.domain.thirdparty.ThirdParty;
import com.xiaobudian.yamikitchen.repository.merchant.MerchantRepository;
import com.xiaobudian.yamikitchen.repository.order.OrderRepository;
import com.xiaobudian.yamikitchen.repository.thirdgroup.ThirdPartyRepository;
import com.xiaobudian.yamikitchen.service.thirdparty.HttpClientService;
import com.xiaobudian.yamikitchen.thirdparty.util.DadaConstans;
import com.xiaobudian.yamikitchen.thirdparty.util.MD5Util;
import com.xiaobudian.yamikitchen.web.dto.thirdparty.DadaDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.inject.Inject;
import java.util.*;

/**
 * @author Liuminglu 2015/5/19.
 */
@Service(value = "dadaService")
public class DadaServiceImpl implements DadaService {
    @Inject
    private HttpClientService httpClientService;
    @Inject
    private ThirdPartyRepository httpClientRepository;
    @Inject
    private OrderRepository orderRepository;
    @Inject
    private MerchantRepository merchantRepository;

    @Override
    public String getGrantCode() {
        String requestUrl = DadaConstans.getGrantCodeUrl();
        String resultJson = httpClientService.httpGet(requestUrl);
        DadaDto dadaDto = fromJson(resultJson);
        if (dadaDto == null || dadaDto.getResult() == null) {
            return null;
        }
        return dadaDto.getResult().getGrant_code();
    }

    @Override
    public DadaDto createAccessToken(String grantCode) {
        Assert.notNull(grantCode, "params can't be null : grantCode");
        String requestUrl = DadaConstans.getAccessTokenUrl(grantCode);
        String resultJson = httpClientService.httpGet(requestUrl);
        return fromJson(resultJson);
    }

    @Override
    public DadaDto createAccessToken() {
        return createAccessToken(getGrantCode());
    }

    @Override
    public String getAccessToken() {
        ThirdParty thirdGroup = httpClientRepository.findOne(1L);
        if (thirdGroup == null) {
            thirdGroup = new ThirdParty();
            saveThirdGroup(thirdGroup);
        } else if (!tokenIsValid(thirdGroup.getExpiresIn(), thirdGroup.getCreateDate())) {
            saveThirdGroup(thirdGroup);
        }
        return thirdGroup.getAccessToken();
    }

    @Override
    public void addOrderToDada(Order order) {
        String token = getAccessToken();
        DadaDto dadaDto = addOrder(order, token);
        if (dadaDto != null && !DadaConstans.DADA_RESPONSE_STATUS_OK.equals(dadaDto.getStatus())) {
            throw new RuntimeException("Add order to DADA error, errorCode:" + dadaDto.getErrorCode());
        }
        order.setDeliverGroupOrderStatus(1);
        orderRepository.save(order);
    }

    @Override
    public void cancelOrder(Order order) {
        String token = getAccessToken();
        DadaDto dadaDto = cancelOrder(order, token);
        if (dadaDto != null && !DadaConstans.DADA_RESPONSE_STATUS_OK.equals(dadaDto.getStatus())) {
            throw new RuntimeException("cancel order to DADA error, errorCode:" + dadaDto.getErrorCode());
        }
    }

    @Override
    public Order dadaCallBack(DadaDto dadaDto) {
        Assert.notNull(dadaDto, "dadaResultDto can't be null");
        Assert.notNull(dadaDto.getOrder_id(), "dadaResultDto.order_id can't be null");
        Assert.notNull(dadaDto.getOrder_status(), "dadaResultDto.order_status can't be null");

        Order order = orderRepository.findByOrderNo(dadaDto.getOrder_id());
        Assert.notNull(order, "Order not longer exist : " + dadaDto.getOrder_id());
        order.setDeliverGroupOrderStatus(dadaDto.getOrder_status());
        order.setDeliveryManId(dadaDto.getDm_id());
        order.setDeliveryManName(dadaDto.getDm_name());
        order.setDeliveryManMobile(dadaDto.getDm_mobile());
        order.setUpdateTime(new Date(dadaDto.getUpdate_time()));
        if (dadaDto.getOrder_status() == 3) {
            order.deliver();
        } else if (dadaDto.getOrder_status() == 4) {
            order.finish();
        }
        return orderRepository.save(order);
    }
    
    @Override
    public String getSignature(Date currentDate, String token) {
        String timestamp = String.valueOf(new Date().getTime());
        List<String> list = new ArrayList<String>();
        list.add(token);
        list.add(timestamp);
        list.add(DadaConstans.DADA);
        Collections.sort(list);
        String signString = list.toString();
        signString = signString.replace(" ", "").replace(",", "").replace("[", "").replace("]", "");
        return MD5Util.md5(signString);
    }

    private void saveThirdGroup(ThirdParty thirdGroup) {
        DadaDto dadaDto = createAccessToken();
        thirdGroup.setAccessToken(dadaDto.getResult().getAccess_token());
        thirdGroup.setExpiresIn(dadaDto.getResult().getExpires_in());
        thirdGroup.setRefreshToken(dadaDto.getResult().getRefresh_token());
        thirdGroup.setCreateDate(new Date());
        thirdGroup.setThirdGroup(DadaConstans.DADA);
        httpClientRepository.save(thirdGroup);
    }

    private boolean tokenIsValid(Long expiresIn, Date createData) {
        Long timestamp = createData.getTime() + (expiresIn * 1000);
        Date date = new Date(timestamp);
        return date.after(new Date());
    }

    private DadaDto cancelOrder(Order order, String token) {
        Date currentDate = new Date();
        String signature = getSignature(currentDate, token);
        String requestUrl = DadaConstans.cancelOrderToDadaUrl(
                token, currentDate.getTime(), signature, order.getOrderNo(), null);
        String resultJson = httpClientService.httpGet(requestUrl);
        return fromJson(resultJson);
    }

    private DadaDto addOrder(Order order, String token) {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("token", token);
        Date currentDate = new Date();
        requestMap.put("timestamp", String.valueOf(currentDate.getTime()));
        requestMap.put("signature", getSignature(currentDate, token));
        requestMap.put("origin_id", order.getOrderNo());
        requestMap.put("city_name", "上海");
        requestMap.put("city_code", "021");
        if (order.isPayOnDeliver()) {
            requestMap.put("pay_for_supplier_fee", String.valueOf(order.getPrice()));
            requestMap.put("fetch_from_receiver_fee", String.valueOf(order.getPrice()));
            requestMap.put("is_prepay", "1");
        } else {
            requestMap.put("pay_for_supplier_fee", "0");
            requestMap.put("fetch_from_receiver_fee", "0");
            requestMap.put("is_prepay", "0");
        }
        requestMap.put("deliver_fee", String.valueOf(order.getDeliverPrice()));
        requestMap.put("create_time", String.valueOf(order.getCreateDate().getTime()));
        requestMap.put("info", StringUtils.isBlank(order.getRemark()) ? "订单备注" : order.getRemark());
        requestMap.put("cargo_type", "1");
        requestMap.put("cargo_weight", "1");
        requestMap.put("cargo_price", String.valueOf(order.getPrice()));
        requestMap.put("cargo_num", String.valueOf(order.getTotalQuantity()));
        requestMap.put("expected_fetch_time", "0");
        requestMap.put("expected_finish_time", String.valueOf(order.getDeliverDate()));
        requestMap.put("supplier_id", String.valueOf(order.getMerchantId()));
        requestMap.put("supplier_name", order.getMerchantName());
        Merchant merchant = merchantRepository.findOne(order.getMerchantId());
        requestMap.put("supplier_address", merchant.getAddress());
        requestMap.put("supplier_phone", String.valueOf(order.getMerchantPhone()));
        requestMap.put("supplier_tel", null);
        requestMap.put("supplier_lat", String.valueOf(merchant.getLatitude()));
        requestMap.put("supplier_lng", String.valueOf(merchant.getLongitude()));
        requestMap.put("invoice_title", null);
        requestMap.put("receiver_name", order.getNickName());
        requestMap.put("receiver_address", order.getAddress());
        requestMap.put("receiver_phone", String.valueOf(order.getPhone()));
        requestMap.put("receiver_tel", null);
        requestMap.put("receiver_lat", order.getLatitude() == null ? "0" : String.valueOf(order.getLatitude()));
        requestMap.put("receiver_lng", order.getLongitude() == null ? "0" : String.valueOf(order.getLongitude()));
        requestMap.put("callback", DadaConstans.DADA_CALL_BACK_URL);

        String resultJson = httpClientService.httpPost(DadaConstans.addOrderToDadaUrl(), requestMap);
        return fromJson(resultJson);
    }

    private DadaDto fromJson(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, DadaDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
