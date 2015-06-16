package com.xiaobudian.yamikitchen.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.xiaobudian.yamikitchen.common.Result;
import com.xiaobudian.yamikitchen.service.thirdparty.dada.DadaService;
import com.xiaobudian.yamikitchen.service.thirdparty.dada.OAuthClient;
import com.xiaobudian.yamikitchen.web.dto.thirdparty.DadaDto;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Liuminglu 2015/5/19.
 */
@RestController
public class ThirdPartyCallBackController {
    @Inject
    DadaService dadaService;
    @Inject
    OAuthClient oAuthClient;

    @RequestMapping(value = "/thirdParty/dadaCallBack", method = RequestMethod.POST)
    public Result dadaCallBack(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("------------------------dadaCallBack---------------------------");
        System.out.println("order_id:" + request.getParameter("order_id"));
        System.out.println("order_status:" + request.getParameter("order_status"));
        System.out.println("cancel_reason:" + request.getParameter("cancel_reason"));
        System.out.println("dm_id:" + request.getParameter("dm_id"));
        System.out.println("dm_name:" + request.getParameter("dm_name"));
        System.out.println("dm_mobile:" + request.getParameter("dm_mobile"));
        System.out.println("update_time:" + request.getParameter("update_time"));
        System.out.println("------------------------dadaCallBack---------------------------");

        String client_id = request.getParameter("client_id");
        String order_id = request.getParameter("order_id");
        String order_status = request.getParameter("order_status");
        String cancel_reason = request.getParameter("cancel_reason");
        String dm_id = request.getParameter("dm_id");
        String dm_name = request.getParameter("dm_name");
        String dm_mobile = request.getParameter("dm_mobile");
        String update_time = request.getParameter("update_time");

        DadaDto dadaDto = new DadaDto();
        dadaDto.setClient_id(client_id);
        dadaDto.setOrder_id(order_id);
        dadaDto.setOrder_status(Integer.parseInt(order_status));
        dadaDto.setCancel_reason(cancel_reason);
        dadaDto.setDm_id(Integer.parseInt(dm_id));
        dadaDto.setDm_name(dm_name);
        dadaDto.setDm_mobile(dm_mobile);
        dadaDto.setUpdate_time(Integer.parseInt(update_time));
        dadaService.dadaCallBack(dadaDto);
        return Result.successResult("ok");
    }
    
    @RequestMapping(value = "/thirdParty/dadaCallBackTest/{orderNo}", method = RequestMethod.GET)
    public Result dadaCallBackTest(@PathVariable String orderNo) {
    	String token = dadaService.getAccessToken();
        Date currentDate = new Date();
        Long timestamp = currentDate.getTime();
        String signature = oAuthClient.getSignature(currentDate, token);
        String acceptOrder = "/v1_0/acceptOrder/";
        String url1 = "http://public.ga.dev.imdada.cn" + acceptOrder + "?token="
                + token + "&timestamp=" + timestamp + "&order_id=" + orderNo + "&signature=" + signature;
       
        String fetchOrder = "/v1_0/fetchOrder/";
        String url2 = "http://public.ga.dev.imdada.cn" + fetchOrder + "?token="
                + token + "&timestamp=" + timestamp + "&order_id=" + orderNo + "&signature=" + signature;

        String getOrderUrl = "/v1_0/getOrderInfo/";
        String url3 = "http://public.ga.dev.imdada.cn" + getOrderUrl + "?token="
                + token + "&timestamp=" + timestamp + "&order_id=" + orderNo + "&signature=" + signature;
        
        String finishOrder = "/v1_0/finishOrder/";
        String url4 = "http://public.ga.dev.imdada.cn" + finishOrder + "?token="
                + token + "&timestamp=" + timestamp + "&order_id=" + orderNo + "&signature=" + signature;
        
        Map<String, String> returnMap = new HashMap<String, String>();
        returnMap.put("acceptOrder", url1);
        returnMap.put("fetchOrder", url2);
        returnMap.put("getOrderInfo", url3);
        returnMap.put("finishOrder", url4);
        return Result.successResult(returnMap);
    }
}
