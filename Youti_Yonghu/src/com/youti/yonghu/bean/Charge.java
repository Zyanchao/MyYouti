package com.youti.yonghu.bean;

import java.io.Serializable;

/**
 * 
* @ClassName;// Charge 
* @Description;// TODO(支付 凭证 对象) 
* @author zychao 
* @date 2015-6-24 上午11;//56;//16 
*
 */
public class Charge implements Serializable{

	/** 
	* @Fields serialVersionUID ;// TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 1L;
	
	
	private String order_no;// required商户订单号，适配每个渠道对此参数的要求，必须在商户系统内唯一。(alipay;// 1-64 位， wx;// 1-32 位，bfb;// 1-20 位，upacp;// 8-40 位，yeepay_wap;//1-50 位，jdpay_wap;//1-30 位，推荐使用 8-20 位，要求数字或字母，不允许特殊字符)。
	private String appid;// required支付使用的 app 对象的 id。
	private String channel;// required支付使用的第三方支付渠道，取值范围
	private String amount;// required订单总金额, 单位为对应币种的最小货币单位，例如：人民币为分（如订单总金额为 1 元，此处请填 100）。
	private String client_ip;// required发起支付请求终端的 IP 地址，格式为 IPV4，如;// 127.0.0.1。
	private String currency;// required
	private String cny;//	三位 ISO 货币代码，目前仅支持人民币 。
	private String subject;// required商品的标题，该参数最长为 32 个 Unicode 字符，银联限制在 32 个字节。
	private String body;// required商品的描述信息，该参数最长为 128 个 Unicode 字符，yeepay_wap 对于该参数长度限制为 100 个 Unicode 字符。
	private String extra;// optional特定渠道发起交易时需要的额外参数。
	private String time_expire;// optional订单失效时间，用 UTC 时间表示。时间范围在订单创建后的 1 分钟到 15 天，默认为 1 天，创建时间以 Ping++ 服务器时间为准。该参数不适用于微信支付渠道。
	private String metadata;// optional
	private String Metadata ;//元数据。
	private String description;// optional订单附加说明，最多 255 个 Unicode 字符。
}
