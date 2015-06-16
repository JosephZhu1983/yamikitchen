package com.xiaobudian.yamikitchen.service.thirdparty.dada;

import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.web.dto.thirdparty.DadaDto;

/**
 * @author Liuminglu 2015/5/19.
 */
public interface DadaService {

    String getGrantCode();

    DadaDto createAccessToken(String grantCode);

    DadaDto createAccessToken();

    void addOrderToDada(Order order);

    Order dadaCallBack(DadaDto dadaDto);

    String getAccessToken();

    void cancelOrder(Order order);

}
