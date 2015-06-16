package com.xiaobudian.yamikitchen.service.thirdparty.dada;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Johnson on 2015/6/16.
 */
@Component(value = "oAuthClient")
public class OAuthClient {
    @Value(value = "${dada.callback.url}")
    public String callbackUrl;
    @Value(value = "${dada.grant.code.url}")
    public String grantCodeUrl;
    @Value(value = "${dada.access.token.url}")
    private String accessTokenUrl;
    @Value(value = "${dada.add.order.url}")
    public String addOrderUrl;
    @Value(value = "${dada.cancel.order.url}")
    private String cancelOrderUrl;
    @Value(value = "${dada.signature.key}")
    public String key;
    
    public String getAccessTokenUrl(String grantCode) {
        return MessageFormat.format(accessTokenUrl, grantCode);
    }
    
    public String getCancelOrderUrl(String token, Long timestamp, String signature, String orderNo, String reason) {
        return MessageFormat.format(cancelOrderUrl, token, String.valueOf(timestamp), signature, orderNo, reason);
    }

    public String getSignature(Date date, String token) {
        List<String> list = Arrays.asList(token, String.valueOf(date.getTime()), key);
        Collections.sort(list);
        return DigestUtils.md5Hex(StringUtils.join(list, StringUtils.EMPTY));
    }
    
}
