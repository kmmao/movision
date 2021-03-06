package com.movision.mybatis.pay;

import java.io.Serializable;

/**
 * @Author shuxf
 * @Date 2017/4/18 11:35
 */
public class AlipayContent implements Serializable {

    private String app_id;//商户appid

    private String method;//支付宝请求方法

    private String format;//仅支持JSON

    private String charset;//编码格式

    private String sign_type;//签名类型

    private String timestamp;//时间戳

    private String biz_content;//内容体

    private String sign;//支付宝签名

    private String version;//版本

    private String notify_url;//支付宝服务器主动通知商户服务器里指定的页面http/https路径。建议商户使用https

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getBiz_content() {
        return biz_content;
    }

    public void setBiz_content(String biz_content) {
        this.biz_content = biz_content;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }
}
