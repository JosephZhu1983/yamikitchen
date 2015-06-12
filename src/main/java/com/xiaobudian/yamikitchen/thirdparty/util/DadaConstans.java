package com.xiaobudian.yamikitchen.thirdparty.util;

/**
 * @author Liuminglu 2015/5/19.
 */
public class DadaConstans {
    /**
     * 达达接口请求返回状态：成功
     */
    public static final String DADA_RESPONSE_STATUS_OK = "ok";
    /**
     * 达达接口请求返回状态：失败
     */
    public static final String DADA_RESPONSE_STATUS_FAIL = "fail";
    /**
     * 达达回调接口 115.28.38.232
     */
    public static final String DADA_CALL_BACK_URL = "http://115.28.38.232:8081/yamikitchen/api/thirdParty/dadaCallBack";

    // 达达签名算法字符串
    public static final String DADA = "dada";
    // 达达appkey
    public static final String DADA_APPKEY = "dada19bcdc6149a4895f";
    // 测试环境地址
//    public static final String DADA_URL = "http://public.ga.dev.imdada.cn";
    // 正式环境地址
 	private static final String DADA_URL = "http://public.imdada.cn";
    // 获取授权码
    public static final String GET_GRANT_CODE = "%s/oauth/authorize/?scope=dada_base&app_key=%s";
    // 获取access_token
    public static final String GET_ACCESS_TOKEN = "%s/oauth/access_token/?grant_type=authorization_code&app_key=%s&grant_code=%s";
    // 添加订单
    public static final String ADD_ORDER_DADA = "%s/v1_0/addOrder/";
    // 取消订单
    public static final String CANCEL_ORDER_DADA = "%s/v1_0/cancelOrder/?token=%s&timestamp=%s&signature=%s&order_id=%s&reason=%s";

    public static String getGrantCodeUrl() {
        return String.format(GET_GRANT_CODE, DADA_URL, DADA_APPKEY);
    }

    public static String getAccessTokenUrl(String grantCode) {
        return String.format(GET_ACCESS_TOKEN, DADA_URL, DADA_APPKEY, grantCode);
    }

    public static String addOrderToDadaUrl() {
        return String.format(ADD_ORDER_DADA, DADA_URL);
    }

    public static String cancelOrderToDadaUrl(String token, Long timestamp, String signature, String orderNo, String reason) {
        return String.format(CANCEL_ORDER_DADA, DADA_URL, token, timestamp, signature, orderNo, reason);
    }

}
