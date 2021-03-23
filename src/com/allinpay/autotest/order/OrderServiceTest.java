package com.allinpay.autotest.order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.allinpay.yunst.sdk.YunClient;
import com.allinpay.yunst.sdk.bean.YunRequest;
import com.allinpay.yunst.sdk.util.RSAUtil;

public class OrderServiceTest {
	private static Logger logger = Logger.getLogger(OrderServiceTest.class.getName());

	/**
	 * 充值申请接口范例
	 * 
	 * @param bizUserId
	 * @param payMethodStr
	 * @param bankCard
	 * @param limitPay
	 * @param wx_openid
	 * @param ali_user_id
	 * @param vspCusid
	 * @param subAppId
	 * @param subMchId
	 * @param authcode
	 * @param accountSetNo
	 * @param frontUrl
	 * @param backUrl
	 * @param amount
	 * @return
	 */
	public static HashMap<String, String> depositApply(String bizUserId, String payMethodStr, String bankCardNo,
			String limitPay, String wx_openid, String ali_user_id, String vspCusid, String subAppId, String subMchId,
			String authcode, String accountSetNo, String frontUrl, String backUrl, Long amount) {
		logger.info("================OrderAPI: depositApply begin================");
		long start = System.currentTimeMillis();
		HashMap<String, String> result = new HashMap<String, String>();
		final YunRequest request = new YunRequest("OrderService", "depositApply");
		if (bizUserId == null || bizUserId.equals(""))
			logger.error("API_ERROR[depositApply RESULT=[bizUserId is null]");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, 15);
		Date date = calendar.getTime();
		String ordErexpireDatetime = sdf.format(date);
		HashMap<String, Object> payMethod = new HashMap<>();
		String bizOrderNo = "CZ" + System.currentTimeMillis();
		// Long amount = 1L;
		// 支付方式
		try {
			// 网关
			if ("GATEWAY".equals(payMethodStr)) {
				// 网关
				// String frontUrl =
				// "http://122.227.225.142:23661/gateway/getPayFront.jsp";
				// String frontUrl = "http://10.55.3.236:6003/gateway/getPayFront.jsp";
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("bankCode", "vbank"); // 虚拟银行，专门用于测试环境
				// gatewayPay.put("bankCode", "cmb"); //生产环境
				payMap.put("payType", 1L);
				payMap.put("bankCardNo", RSAUtil.encrypt(bankCardNo)); // 借记卡
				payMap.put("amount", amount);

				payMethod.put("GATEWAY", payMap);
			} else if ("POSPAY".equals(payMethodStr) || "POSPAY_SZ".equals(payMethodStr)) {
				// 订单POS
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("amount", amount);

				payMethod.put("POSPAY", payMap);
			} else if ("REALNAMEPAY".equals(payMethodStr)) {
				// 实名付（单笔）
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("bankCardNo", RSAUtil.encrypt(bankCardNo));
				payMap.put("amount", amount); // 分
				payMethod.put("REALNAMEPAY", payMap);
			} else if ("REALNAMEPAY_BATCH".equals(payMethodStr)) {
				// 实名付（batch）
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("bankCardNo", RSAUtil.encrypt(bankCardNo));
				payMap.put("amount", amount); // 分
				payMethod.put("REALNAMEPAY_BATCH", payMap);
			} else if ("WITHHOLD_TLT".equals(payMethodStr)) {
				// 通联通代扣
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("bankCardNo", RSAUtil.encrypt(bankCardNo));
				payMap.put("amount", amount); // 分
				payMethod.put("WITHHOLD_TLT", payMap);
			} else if ("QUICKPAY_TLT".equals(payMethodStr)) {
				// 通联通协议支付
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("bankCardNo", RSAUtil.encrypt(bankCardNo));
				payMap.put("amount", amount); // 分
				payMethod.put("QUICKPAY_TLT", payMap);
			} else if ("WECHATPAY_MiniProgram".equals(payMethodStr)) {
				// 微信小程序支付
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("limitPay", limitPay);
				payMap.put("amount", amount); // 分
				payMap.put("Acct", wx_openid);
				payMethod.put("WECHATPAY_MiniProgram", payMap);
			} else if ("WECHATPAY_MiniProgram_ORG".equals(payMethodStr)) {
				// 微信小程序支付（集团）
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("vspCusid", vspCusid);
				payMap.put("limitPay", limitPay);
				payMap.put("amount", amount); // 分
				payMap.put("Acct", wx_openid);
				payMethod.put("WECHATPAY_MiniProgram_ORG", payMap);
			} else if ("WECHATPAY_APP".equals(payMethodStr)) {
				// 微信APP支付
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("limitPay", limitPay);
				payMap.put("amount", amount); // 分
				payMap.put("Acct", wx_openid);
				payMap.put("subAppId", subAppId);// 微信appid,平台有多个APP时需上送
				payMap.put("subMchId", subMchId);// 微信子商户号,平台有多个APP时需上送
				payMethod.put("WECHATPAY_APP", payMap);
			} else if ("WeChatPAY-APP_OPEN".equals(payMethodStr)) {
				// 微信原生APP支付
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("subAppId", subAppId);// 微信端应用ID：appid
				payMap.put("limitPay", limitPay);
				payMap.put("amount", amount); // 分
				payMethod.put("WeChatPAY-APP_OPEN", payMap);
			} else if ("SCAN_WEIXIN".equals(payMethodStr)) {
				// 微信扫码支付正扫
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("payType", "W02"); // 非贷记卡：W02, 借、贷卡：W01
				payMap.put("amount", amount); // 分
				payMethod.put("SCAN_WEIXIN", payMap);
			} else if ("SCAN_WEIXIN_ORG".equals(payMethodStr)) {
				// 微信扫码支付正扫 集团
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("vspCusid", vspCusid);
				payMap.put("payType", limitPay); // 非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				payMethod.put("SCAN_WEIXIN_ORG", payMap);
			} else if ("WECHAT_PUBLIC".equals(payMethodStr)) {
				// 微信JS支付（公众号）
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("payType", limitPay); // 非贷记卡：no_credit 借、贷记卡：””
				payMap.put("acct", wx_openid); // 微信JS支付openid——微信分配
				payMap.put("amount", amount); // 分
				payMethod.put("WECHAT_PUBLIC", payMap);
			} else if ("SCAN_ALIPAY".equals(payMethodStr)) {
				// 支付宝支付正扫
				HashMap<String, Object> payMap = new HashMap<>();
				// wechatMiniPay.put("vspCusid", vspCusid);
				payMap.put("payType", "A01"); // 传值：A01
				payMap.put("amount", amount); // 分
				payMethod.put("SCAN_ALIPAY", payMap);
			} else if ("SCAN_ALIPAY_ORG".equals(payMethodStr)) {
				// 支付宝支付正扫 集团
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("vspCusid", vspCusid);
				// aliPay.put("payType", "A01"); //传值：A01
				payMap.put("amount", amount); // 分
				payMethod.put("SCAN_ALIPAY_ORG", payMap);
			} else if ("SCAN_ALIPAY_ORG".equals(payMethodStr)) {
				// 支付宝JS支付（生活号）
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("acct", ali_user_id);
				// aliPay.put("payType", "A01"); //传值：A01
				payMap.put("amount", amount); // 分
				payMethod.put("SCAN_ALIPAY_ORG", payMap);
			} else if ("QQ_WALLET".equals(payMethodStr)) {
				// QQ钱包JS支付
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// aliPay.put("payType", "A01"); //传值：A01
				payMap.put("amount", amount); // 分
				payMethod.put("QQ_WALLET", payMap);
			} else if ("CODEPAY_W".equals(payMethodStr)) {
				// 微信刷卡支付（被扫）
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				payMap.put("payType", limitPay); // 非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				payMap.put("authcode", authcode);
				payMethod.put("CODEPAY_W", payMap);
			} else if ("CODEPAY_A".equals(payMethodStr)) {
				// 支付宝刷卡支付(被扫)
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// payMap.put("payType", limitPay); //非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				payMap.put("authcode", authcode);
				payMethod.put("CODEPAY_A", payMap);
			} else if ("CODEPAY_Q".equals(payMethodStr)) {
				// QQ刷卡支付(被扫)
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// payMap.put("payType", limitPay); //非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				payMap.put("authcode", authcode);
				payMethod.put("CODEPAY_Q", payMap);
			} else if ("BALANCE".equals(payMethodStr)) {
				// 账户余额
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// payMap.put("payType", limitPay); //非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("accountSetNo", accountSetNo);
				payMap.put("amount", amount); // 分
				payMethod.put("BALANCE", payMap);
			} else if ("WECHATPAY_H5".equals(payMethodStr)) {
				// 账户余额
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				payMap.put("limitPay", limitPay); // 非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				payMap.put("apptype", "IOS");// 枚举值：IOS，Android，Wap
				payMap.put("appname", "喜马拉雅");// 商户app名称；app名称或者网站名称,例如 王者荣耀
				payMap.put("apppackage", "com.tencent.mobileqq");
				payMap.put("cusip", "192.168.14.100");
				payMethod.put("WECHATPAY_H5", payMap);
			} else if ("WECHAT_PUBLIC_ORG".equals(payMethodStr)) {
				// 微信JS支付（公众号）_集团
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				payMap.put("vspCusid", vspCusid); // 收银宝子商户号，与公众号对应
				payMap.put("amount", amount); // 分
				payMap.put("acct", wx_openid);
				payMap.put("payType", limitPay);// 非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("WECHAT_PUBLIC_ORG", payMap);
			} else if ("ALIPAY_SERVICE_ORG".equals(payMethodStr)) {
				// 支付宝JS支付_集团
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				payMap.put("vspCusid", vspCusid); // 收银宝子商户号，与生活号对应
				payMap.put("amount", amount); // 分
				payMap.put("acct", ali_user_id);
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("ALIPAY_SERVICE_ORG", payMap);
			} else if ("QQ_WALLET_ORG".equals(payMethodStr)) {
				// QQ钱包JS支付_集团
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				payMap.put("vspCusid", vspCusid); // 收银宝子商户号
				payMap.put("amount", amount); // 分
				// payMap.put("acct", ali_user_id);
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("QQ_WALLET_ORG", payMap);
			} else if ("ORDER_VSPPAY".equals(payMethodStr)) {
				// 收银宝POS当面付及订单模式支付
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// payMap.put("vspCusid", vspCusid); //收银宝子商户号
				payMap.put("amount", amount); // 分
				// payMap.put("acct", ali_user_id);
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("ORDER_VSPPAY", payMap);
			} else if ("QUICKPAY_H5".equals(payMethodStr) || "QUICKPAY_PC".equals(payMethodStr)) {
				// 新移动H5/PC快捷支付
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// payMap.put("vspCusid", vspCusid); //收银宝子商户号
				payMap.put("amount", amount); // 分
				payMap.put("bankCardNo", RSAUtil.encrypt(bankCardNo));
				// payMap.put("acct", ali_user_id);
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("QUICKPAY_H5", payMap);
			} else if ("WECHATPAY_APP_ORG".equals(payMethodStr)) {
				// 微信APP支付_集团
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				payMap.put("vspCusid", vspCusid); // 收银宝子商户号
				payMap.put("limitPay", limitPay);// 非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				payMap.put("subAppId", subAppId);
				payMap.put("subMchId", subMchId);
				// payMap.put("acct", ali_user_id);
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("WECHATPAY_APP_ORG", payMap);
			} else if ("ALIPAY_APP_OPEN".equals(payMethodStr)) {
				// 支付宝原生APP支付
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// payMap.put("vspCusid", vspCusid); //收银宝子商户号
				// payMap.put("limitPay", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				// payMap.put("enablePayChannels", subAppId);
				// payMap.put("paysummary", subMchId);
				// payMap.put("acct", ali_user_id);
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("ALIPAY_APP_OPEN", payMap);
			} else if ("VIRTUAL_IN".equals(payMethodStr)) {
				// 通用虚拟入金
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// payMap.put("vspCusid", vspCusid); //收银宝子商户号
				// payMap.put("limitPay", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				payMap.put("extChannelNo", "AUTO");
				payMap.put("extAccount", "AUTO");
				payMap.put("paysummary", "AUTO");
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("VIRTUAL_IN", payMap);
			} else if ("WITHHOLD_SD".equals(payMethodStr)) {
				// 山东代收
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				payMap.put("bankCardNo", RSAUtil.encrypt(bankCardNo));
				payMap.put("PROVINCE", "上海");
				payMap.put("CITY", "浦东新区");
				payMap.put("amount", amount); // 分
				// payMap.put("extChannelNo", "AUTO");
				// payMap.put("extAccount", "AUTO");
				payMap.put("paysummary", "AUTO");
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("WITHHOLD_SD", payMap);
			}

			request.put("bizUserId", bizUserId);
			request.put("bizOrderNo", bizOrderNo);
			request.put("accountSetNo", accountSetNo);
			request.put("amount", amount);
			request.put("fee", 0L);
			request.put("validateType", 1L); // 0: 无验证；1：短信；2：支付密码
			request.put("frontUrl", frontUrl);// 前台通知地址
			request.put("backUrl", backUrl);// 后台通知地址
			request.put("ordErexpireDatetime", ordErexpireDatetime);
			request.put("payMethod", JSONObject.toJSON(payMethod));
			request.put("goodsName", "computer");
			request.put("industryCode", "2413");
			request.put("industryName", "电子产品");
			request.put("source", 2L);
			request.put("summary", "AUTO-TEST depositApply");
			request.put("extendInfo", "AUTO-TEST this is extendInfo");

			String res = YunClient.request(request);
			// System.out.println("res: " + res);
			JSONObject resp = JSON.parseObject(res);
			// System.out.println("status=[" + resp.getString("status") + "]");
			// System.out.println("signedValue=[" + resp.getString("signedValue") + "]");
			// System.out.println("sign=[" + resp.getString("sign") + "]");
			// System.out.println("errorCode=[" + resp.getString("errorCode") + "]");
			// System.out.println("message=[" + resp.getString("message") + "]");
			String status = resp.getString("status");
			if (status != null && status.equals("OK")) {
				logger.info("API[depositApply SUCCESS!]");
			} else {
				logger.error("API_ERROR[depositApply RESULT=[" + resp.getString("errorCode") + ", "
						+ resp.getString("message") + "]]");
			}
		} catch (final Exception e) {
			logger.error("EXCEPTION", e);
			// e.printStackTrace();
		}
		result.put("bizUserId", bizUserId);
		result.put("bizOrderNo", bizOrderNo);
		result.put("tradeNo", "");
		// result.put("amount", amount.toString());
		long end = System.currentTimeMillis();
		long elapse = end - start;
		logger.info("================OrderAPI: depositApply end================[elapse: " + elapse + " ms]");
		return result;
	}

	/**
	 * 提现申请接口
	 * 
	 * @param bizUserId
	 * @param accountSetNo
	 * @param amount
	 * @param payMethodStr
	 * @param bankCardNo
	 * @param bankCardPro
	 * @param unionBank
	 * @param bankName
	 * @param province
	 * @param city
	 * @param withdrawType
	 * @param backUrl
	 * @return
	 */
	public static HashMap<String, String> withdrawApply(String bizUserId, String accountSetNo, Long amount,
			String payMethodStr, String bankCardNo, Long bankCardPro, String unionBank, String bankName,
			String province, String city, String withdrawType, String backUrl) {
		logger.info("================OrderAPI: withdrawApply begin================");
		long start = System.currentTimeMillis();
		HashMap<String, String> result = new HashMap<String, String>();
		final YunRequest request = new YunRequest("OrderService", "withdrawApply");
		if (bizUserId == null || bizUserId.equals(""))
			logger.error("API_ERROR[withdrawApply RESULT=[bizUserId is null]");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, 15);
		Date date = calendar.getTime();
		String ordErexpireDatetime = sdf.format(date);
		HashMap<String, Object> payMethod = new HashMap<>();
		String bizOrderNo = "TX" + System.currentTimeMillis();

		String payStatus = "";
		String payFailMessage = "";
		String orderNo = "";

		// 支付方式
		try {
			// 通联通代付
			if ("WITHDRAW_TLT".equals(payMethodStr)) {
				// 网关
				// String frontUrl =
				// "http://122.227.225.142:23661/gateway/getPayFront.jsp";
				// String frontUrl = "http://10.55.3.236:6003/gateway/getPayFront.jsp";
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("payTypeName", "withdraw_tlt"); // 必须传withdraw_tlt
				// gatewayPay.put("bankCode", "cmb"); //生产环境
				payMap.put("unionBank", unionBank);
				payMap.put("province", province); //
				payMap.put("city", city);

				payMethod.put("WITHDRAW_TLT", payMap);
			} else if ("VIRTUAL_OUT".equals(payMethodStr) || "POSPAY_SZ".equals(payMethodStr)) {
				// 虚拟通道出金
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("amount", amount);
				payMap.put("extChannelNo", "AUTO-TEST");
				payMap.put("extAccount", "AUTO-TEST");
				payMap.put("paysummary", "AUTO-TEST");
				payMethod.put("VIRTUAL_OUT", payMap);
			} else if ("WITHDRAW_SD".equals(payMethodStr)) {
				// 山东代付
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("amount", amount); // 分
				payMap.put("unionBank", unionBank);
				payMap.put("bankName", bankName);
				payMap.put("PROVINCE", province); //
				payMap.put("CITY", city);
				payMap.put("paysummary", "AUTO-TEST");
				payMethod.put("WITHDRAW_SD", payMap);
			}

			// 组装支付方式

			// payMethod.put("QUICKPAY", quickPay);

			// payMethod.put("GATEWAY", gatewayPay);
			// payMethod.put("WITHHOLD_UP", daikouPay);
			// payMethod.put("WITHHOLD_TLT", tltDaikouPay);

			request.put("bizUserId", bizUserId);
			request.put("bizOrderNo", bizOrderNo);
			request.put("accountSetNo", accountSetNo);
			request.put("amount", amount);
			request.put("fee", 0L);
			request.put("validateType", 1L); // 0: 无验证；1：短信；2：支付密码
			// request.put("frontUrl", frontUrl);//前台通知地址
			request.put("backUrl", backUrl);// 后台通知地址
			request.put("ordErexpireDatetime", ordErexpireDatetime);
			request.put("payMethod", JSONObject.toJSON(payMethod));

			request.put("bankCardNo", RSAUtil.encrypt(bankCardNo));
			request.put("bankCardPro", bankCardPro);
			request.put("withdrawType", withdrawType);
			request.put("industryCode", "2413");
			request.put("industryName", "电子产品");

			request.put("source", 2L);
			request.put("summary", "AUTO-TEST withdrawApply");
			request.put("extendInfo", "AUTO-TEST this is extendInfo");

			String res = YunClient.request(request);
			// System.out.println("res: " + res);
			JSONObject resp = JSON.parseObject(res);
			// System.out.println("status=[" + resp.getString("status") + "]");
			// System.out.println("signedValue=[" + resp.getString("signedValue") + "]");
			// System.out.println("sign=[" + resp.getString("sign") + "]");
			// System.out.println("errorCode=[" + resp.getString("errorCode") + "]");
			// System.out.println("message=[" + resp.getString("message") + "]");
			String status = resp.getString("status");

			if (status != null && status.equals("OK")) {
				payStatus = resp.getJSONObject("signedValue").getString("payStatus");
				payFailMessage = resp.getJSONObject("signedValue").getString("payFailMessage");
				orderNo = resp.getJSONObject("signedValue").getString("orderNo");
				logger.info("API[withdrawApply SUCCESS!]");
			} else {
				logger.error("API_ERROR[withdrawApply RESULT=[" + resp.getString("errorCode") + ", "
						+ resp.getString("message") + "]]");
			}
		} catch (final Exception e) {
			logger.error("EXCEPTION", e);
			// e.printStackTrace();
		}
		result.put("bizUserId", bizUserId);
		result.put("bizOrderNo", bizOrderNo);
		result.put("orderNo", orderNo);
		result.put("payStatus", payStatus);
		result.put("payFailMessage", payFailMessage);
		long end = System.currentTimeMillis();
		long elapse = end - start;
		logger.info("================OrderAPI: withdrawApply end================[elapse: " + elapse + " ms]");
		return result;
	}

	public static HashMap<String, String> consumeApply(String payerId, String recieverId, Long amount,
			Long validateType, String frontUrl, String backUrl, String payMethodStr, String bankCardNo,
			String limitPay, String wx_openid, String ali_user_id, String vspCusid, String subAppId, String subMchId,
			String authcode, Long bankCardPro, String unionBank, String bankName, String accountSetNo, 
			String province, String city, String withdrawType) {
		logger.info("================OrderAPI: consumeApply begin================");
		long start = System.currentTimeMillis();
		HashMap<String, String> result = new HashMap<String, String>();
		final YunRequest request = new YunRequest("OrderService", "consumeApply");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, 15);
		Date date = calendar.getTime();
		String ordErexpireDatetime = sdf.format(date);
		HashMap<String, Object> payMethod = new HashMap<>();
		String bizOrderNo = "XF" + System.currentTimeMillis();

		String payStatus = "";
		String payFailMessage = "";
		String orderNo = "";
		String payCode = "";
		String tradeNo = "";
		String payInfo = "";
		String extendInfo = "";
		
		String weixinstr = "";
		String appid = "";
		String packageStr = "";
		String prepayid = "";
		String partnerid = "";
		String noncestr = "";
		String timestamp = "";
		String sign = "";
		
		// 支付方式
		try {
			// 网关
			if ("GATEWAY".equals(payMethodStr)) {
				// 网关
				// String frontUrl =
				// "http://122.227.225.142:23661/gateway/getPayFront.jsp";
				// String frontUrl = "http://10.55.3.236:6003/gateway/getPayFront.jsp";
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("bankCode", "vbank"); // 虚拟银行，专门用于测试环境
				// gatewayPay.put("bankCode", "cmb"); //生产环境
				payMap.put("payType", 1L);
				payMap.put("bankCardNo", RSAUtil.encrypt(bankCardNo)); // 借记卡
				payMap.put("amount", amount);

				payMethod.put("GATEWAY", payMap);
			} else if ("POSPAY".equals(payMethodStr) || "POSPAY_SZ".equals(payMethodStr)) {
				// 订单POS
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("amount", amount);

				payMethod.put("POSPAY", payMap);
			} else if ("REALNAMEPAY".equals(payMethodStr)) {
				// 实名付（单笔）
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("bankCardNo", RSAUtil.encrypt(bankCardNo));
				payMap.put("amount", amount); // 分
				payMethod.put("REALNAMEPAY", payMap);
			} else if ("REALNAMEPAY_BATCH".equals(payMethodStr)) {
				// 实名付（batch）
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("bankCardNo", RSAUtil.encrypt(bankCardNo));
				payMap.put("amount", amount); // 分
				payMethod.put("REALNAMEPAY_BATCH", payMap);
			} else if ("WITHHOLD_TLT".equals(payMethodStr)) {
				// 通联通代扣
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("bankCardNo", RSAUtil.encrypt(bankCardNo));
				payMap.put("amount", amount); // 分
				payMethod.put("WITHHOLD_TLT", payMap);
			} else if ("QUICKPAY_TLT".equals(payMethodStr)) {
				// 通联通协议支付
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("bankCardNo", RSAUtil.encrypt(bankCardNo));
				payMap.put("amount", amount); // 分
				payMethod.put("QUICKPAY_TLT", payMap);
			} else if ("WECHATPAY_MiniProgram".equals(payMethodStr)) {
				// 微信小程序支付
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("limitPay", limitPay);
				payMap.put("amount", amount); // 分
				payMap.put("Acct", wx_openid);
				payMethod.put("WECHATPAY_MiniProgram", payMap);
			} else if ("WECHATPAY_MiniProgram_ORG".equals(payMethodStr)) {
				// 微信小程序支付（集团）
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("vspCusid", vspCusid);
				payMap.put("limitPay", limitPay);
				payMap.put("amount", amount); // 分
				payMap.put("Acct", wx_openid);
				payMethod.put("WECHATPAY_MiniProgram_ORG", payMap);
			} else if ("WECHATPAY_APP".equals(payMethodStr)) {
				// 微信APP支付
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("limitPay", limitPay);
				payMap.put("amount", amount); // 分
				payMap.put("Acct", wx_openid);
				payMap.put("subAppId", subAppId);// 微信appid,平台有多个APP时需上送
				payMap.put("subMchId", subMchId);// 微信子商户号,平台有多个APP时需上送
				payMethod.put("WECHATPAY_APP", payMap);
			} else if ("WeChatPAY-APP_OPEN".equals(payMethodStr)) {
				// 微信原生APP支付
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("subAppId", subAppId);// 微信端应用ID：appid
				payMap.put("limitPay", limitPay);
				payMap.put("amount", amount); // 分
				payMethod.put("WeChatPAY-APP_OPEN", payMap);
			} else if ("SCAN_WEIXIN".equals(payMethodStr)) {
				// 微信扫码支付正扫
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("payType", "W02"); // 非贷记卡：W02, 借、贷卡：W01
				payMap.put("amount", amount); // 分
				payMethod.put("SCAN_WEIXIN", payMap);
			} else if ("SCAN_WEIXIN_ORG".equals(payMethodStr)) {
				// 微信扫码支付正扫 集团
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("vspCusid", vspCusid);
				payMap.put("payType", limitPay); // 非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				payMethod.put("SCAN_WEIXIN_ORG", payMap);
			} else if ("WECHAT_PUBLIC".equals(payMethodStr)) {
				// 微信JS支付（公众号）
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("payType", limitPay); // 非贷记卡：no_credit 借、贷记卡：””
				payMap.put("acct", wx_openid); // 微信JS支付openid——微信分配
				payMap.put("amount", amount); // 分
				payMethod.put("WECHAT_PUBLIC", payMap);
			} else if ("SCAN_ALIPAY".equals(payMethodStr)) {
				// 支付宝支付正扫
				HashMap<String, Object> payMap = new HashMap<>();
				// wechatMiniPay.put("vspCusid", vspCusid);
				payMap.put("payType", "A01"); // 传值：A01
				payMap.put("amount", amount); // 分
				payMethod.put("SCAN_ALIPAY", payMap);
			} else if ("SCAN_ALIPAY_ORG".equals(payMethodStr)) {
				// 支付宝支付正扫 集团
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("vspCusid", vspCusid);
				// aliPay.put("payType", "A01"); //传值：A01
				payMap.put("amount", amount); // 分
				payMethod.put("SCAN_ALIPAY_ORG", payMap);
			} else if ("SCAN_ALIPAY_ORG".equals(payMethodStr)) {
				// 支付宝JS支付（生活号）
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("acct", ali_user_id);
				// aliPay.put("payType", "A01"); //传值：A01
				payMap.put("amount", amount); // 分
				payMethod.put("SCAN_ALIPAY_ORG", payMap);
			} else if ("QQ_WALLET".equals(payMethodStr)) {
				// QQ钱包JS支付
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// aliPay.put("payType", "A01"); //传值：A01
				payMap.put("amount", amount); // 分
				payMethod.put("QQ_WALLET", payMap);
			} else if ("CODEPAY_W".equals(payMethodStr)) {
				// 微信刷卡支付（被扫）
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				payMap.put("payType", limitPay); // 非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				payMap.put("authcode", authcode);
				payMethod.put("CODEPAY_W", payMap);
			} else if ("CODEPAY_A".equals(payMethodStr)) {
				// 支付宝刷卡支付(被扫)
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// payMap.put("payType", limitPay); //非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				payMap.put("authcode", authcode);
				payMethod.put("CODEPAY_A", payMap);
			} else if ("CODEPAY_Q".equals(payMethodStr)) {
				// QQ刷卡支付(被扫)
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// payMap.put("payType", limitPay); //非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				payMap.put("authcode", authcode);
				payMethod.put("CODEPAY_Q", payMap);
			} else if ("BALANCE".equals(payMethodStr)) {
				// 账户余额
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// payMap.put("payType", limitPay); //非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("accountSetNo", accountSetNo);
				payMap.put("amount", amount); // 分
				payMethod.put("BALANCE", payMap);
			} else if ("WECHATPAY_H5".equals(payMethodStr)) {
				// 账户余额
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				payMap.put("limitPay", limitPay); // 非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				payMap.put("apptype", "IOS");// 枚举值：IOS，Android，Wap
				payMap.put("appname", "喜马拉雅");// 商户app名称；app名称或者网站名称,例如 王者荣耀
				payMap.put("apppackage", "com.tencent.mobileqq");
				payMap.put("cusip", "192.168.14.100");
				payMethod.put("WECHATPAY_H5", payMap);
			} else if ("WECHAT_PUBLIC_ORG".equals(payMethodStr)) {
				// 微信JS支付（公众号）_集团
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				payMap.put("vspCusid", vspCusid); // 收银宝子商户号，与公众号对应
				payMap.put("amount", amount); // 分
				payMap.put("acct", wx_openid);
				payMap.put("payType", limitPay);// 非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("WECHAT_PUBLIC_ORG", payMap);
			} else if ("ALIPAY_SERVICE_ORG".equals(payMethodStr)) {
				// 支付宝JS支付_集团
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				payMap.put("vspCusid", vspCusid); // 收银宝子商户号，与生活号对应
				payMap.put("amount", amount); // 分
				payMap.put("acct", ali_user_id);
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("ALIPAY_SERVICE_ORG", payMap);
			} else if ("QQ_WALLET_ORG".equals(payMethodStr)) {
				// QQ钱包JS支付_集团
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				payMap.put("vspCusid", vspCusid); // 收银宝子商户号
				payMap.put("amount", amount); // 分
				// payMap.put("acct", ali_user_id);
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("QQ_WALLET_ORG", payMap);
			} else if ("ORDER_VSPPAY".equals(payMethodStr)) {
				// 收银宝POS当面付及订单模式支付
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// payMap.put("vspCusid", vspCusid); //收银宝子商户号
				payMap.put("amount", amount); // 分
				// payMap.put("acct", ali_user_id);
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("ORDER_VSPPAY", payMap);
			} else if ("QUICKPAY_H5".equals(payMethodStr) || "QUICKPAY_PC".equals(payMethodStr)) {
				// 新移动H5/PC快捷支付
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// payMap.put("vspCusid", vspCusid); //收银宝子商户号
				payMap.put("amount", amount); // 分
				payMap.put("bankCardNo", RSAUtil.encrypt(bankCardNo));
				// payMap.put("acct", ali_user_id);
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("QUICKPAY_H5", payMap);
			} else if ("WECHATPAY_APP_ORG".equals(payMethodStr)) {
				// 微信APP支付_集团
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				payMap.put("vspCusid", vspCusid); // 收银宝子商户号
				payMap.put("limitPay", limitPay);// 非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				payMap.put("subAppId", subAppId);
				payMap.put("subMchId", subMchId);
				// payMap.put("acct", ali_user_id);
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("WECHATPAY_APP_ORG", payMap);
			} else if ("ALIPAY_APP_OPEN".equals(payMethodStr)) {
				// 支付宝原生APP支付
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// payMap.put("vspCusid", vspCusid); //收银宝子商户号
				// payMap.put("limitPay", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				// payMap.put("enablePayChannels", subAppId);
				// payMap.put("paysummary", subMchId);
				// payMap.put("acct", ali_user_id);
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("ALIPAY_APP_OPEN", payMap);
			} else if ("VIRTUAL_IN".equals(payMethodStr)) {
				// 通用虚拟入金
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// payMap.put("vspCusid", vspCusid); //收银宝子商户号
				// payMap.put("limitPay", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				payMap.put("extChannelNo", "AUTO");
				payMap.put("extAccount", "AUTO");
				payMap.put("paysummary", "AUTO");
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("VIRTUAL_IN", payMap);
			} else if ("WITHHOLD_SD".equals(payMethodStr)) {
				// 山东代收
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				payMap.put("bankCardNo", RSAUtil.encrypt(bankCardNo));
				payMap.put("PROVINCE", "上海");
				payMap.put("CITY", "浦东新区");
				payMap.put("amount", amount); // 分
				// payMap.put("extChannelNo", "AUTO");
				// payMap.put("extAccount", "AUTO");
				payMap.put("paysummary", "AUTO");
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("WITHHOLD_SD", payMap);
			}

			// 组装支付方式

			// payMethod.put("QUICKPAY", quickPay);

			// payMethod.put("GATEWAY", gatewayPay);
			// payMethod.put("WITHHOLD_UP", daikouPay);
			// payMethod.put("WITHHOLD_TLT", tltDaikouPay);

			request.put("payerId", payerId);
			request.put("recieverId", recieverId);
			request.put("bizOrderNo", bizOrderNo);
			request.put("amount", amount);
			//request.put("accountSetNo", accountSetNo);
			
			request.put("fee", 0L);
			request.put("validateType", validateType); // 0: 无验证；1：短信；2：支付密码
			request.put("frontUrl", frontUrl);//前台通知地址
			request.put("backUrl", backUrl);// 后台通知地址
			request.put("ordErexpireDatetime", ordErexpireDatetime);
			request.put("payMethod", JSONObject.toJSON(payMethod));

			//request.put("bankCardNo", RSAUtil.encrypt(bankCardNo));
			//request.put("bankCardPro", bankCardPro);
			//request.put("withdrawType", withdrawType);
			request.put("goodsName", "computer");
			request.put("industryCode", "2413");
			request.put("industryName", "电子产品");
			request.put("source", 2L);
			request.put("summary", "AUTO-TEST consumeApply");
			request.put("extendInfo", "AUTO-TEST this is extendInfo");
			

			String res = YunClient.request(request);
			// System.out.println("res: " + res);
			JSONObject resp = JSON.parseObject(res);
			// System.out.println("status=[" + resp.getString("status") + "]");
			// System.out.println("signedValue=[" + resp.getString("signedValue") + "]");
			// System.out.println("sign=[" + resp.getString("sign") + "]");
			// System.out.println("errorCode=[" + resp.getString("errorCode") + "]");
			// System.out.println("message=[" + resp.getString("message") + "]");
			String status = resp.getString("status");

			if (status != null && status.equals("OK")) {
				payStatus = resp.getJSONObject("signedValue").getString("payStatus");
				payFailMessage = resp.getJSONObject("signedValue").getString("payFailMessage");
				orderNo = resp.getJSONObject("signedValue").getString("orderNo");
				tradeNo = resp.getJSONObject("signedValue").getString("tradeNo");
				payCode = resp.getJSONObject("signedValue").getString("payCode");
				extendInfo = resp.getJSONObject("signedValue").getString("extendInfo");
				payInfo = resp.getJSONObject("signedValue").getString("payInfo");
				if(payMethodStr.equals("WECHATPAY_APP")) {
					appid = resp.getJSONObject("signedValue").getJSONObject("weixinstr").getString("appid");
					packageStr = resp.getJSONObject("signedValue").getJSONObject("weixinstr").getString("package");
					prepayid = resp.getJSONObject("signedValue").getJSONObject("weixinstr").getString("prepayid");
					partnerid = resp.getJSONObject("signedValue").getJSONObject("weixinstr").getString("partnerid");
					noncestr = resp.getJSONObject("signedValue").getJSONObject("weixinstr").getString("noncestr");
					timestamp = resp.getJSONObject("signedValue").getJSONObject("weixinstr").getString("timestamp");
					sign = resp.getJSONObject("signedValue").getJSONObject("weixinstr").getString("sign");
				}
				logger.info("API[consumeApply SUCCESS!]");
			} else {
				logger.error("API_ERROR[consumeApply RESULT=[" + resp.getString("errorCode") + ", "
						+ resp.getString("message") + "]]");
			}
		} catch (final Exception e) {
			logger.error("EXCEPTION", e);
			// e.printStackTrace();
		}
		result.put("payerId", payerId);
		result.put("recieverId", recieverId);
		result.put("bizOrderNo", bizOrderNo);
		result.put("orderNo", orderNo);
		result.put("tradeNo", tradeNo);
		result.put("payCode", payCode);
		result.put("extendInfo", extendInfo);
		result.put("payInfo", payInfo);
		result.put("payStatus", payStatus);
		result.put("payFailMessage", payFailMessage);
		//wechat pay must info
		result.put("appid", appid);
		result.put("packageStr", packageStr);
		result.put("prepayid", prepayid);
		result.put("partnerid", partnerid);
		result.put("noncestr", noncestr);
		result.put("timestamp", timestamp);
		result.put("sign", sign);
		
		long end = System.currentTimeMillis();
		long elapse = end - start;
		logger.info("================OrderAPI: consumeApply end================[elapse: " + elapse + " ms]");
		return result;
	}
	
	/**
	 * 带有分账规则的消费申请
	 * @param bizUserId
	 * @param payerId
	 * @param recieverId
	 * @param amount
	 * @param validateType
	 * @param frontUrl
	 * @param backUrl
	 * @param payMethodStr
	 * @param bankCardNo
	 * @param limitPay
	 * @param wx_openid
	 * @param ali_user_id
	 * @param vspCusid
	 * @param subAppId
	 * @param subMchId
	 * @param authcode
	 * @param bankCardPro
	 * @param unionBank
	 * @param bankName
	 * @param accountSetNo
	 * @param province
	 * @param city
	 * @param withdrawType
	 * @return
	 */
	public static HashMap<String, String> consumeApplyWithSplitRules(String bizUserId, String payerId, String recieverId, Long amount,
			Long validateType, String frontUrl, String backUrl, String payMethodStr, String bankCardNo,
			String limitPay, String wx_openid, String ali_user_id, String vspCusid, String subAppId, String subMchId,
			String authcode, Long bankCardPro, String unionBank, String bankName, String accountSetNo, 
			String province, String city, String withdrawType) {
		logger.info("================OrderAPI: consumeApplyWithSplitRules begin================");
		long start = System.currentTimeMillis();
		HashMap<String, String> result = new HashMap<String, String>();
		final YunRequest request = new YunRequest("OrderService", "consumeApply");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, 15);
		Date date = calendar.getTime();
		String ordErexpireDatetime = sdf.format(date);
		HashMap<String, Object> payMethod = new HashMap<>();
		String bizOrderNo = "XF" + System.currentTimeMillis();

		String payStatus = "";
		String payFailMessage = "";
		String orderNo = "";
		String payCode = "";
		String tradeNo = "";
		String payInfo = "";
		String extendInfo = "";
		
		String weixinstr = "";
		String appid = "";
		String packageStr = "";
		String prepayid = "";
		String partnerid = "";
		String noncestr = "";
		String timestamp = "";
		String sign = "";
		
		// 支付方式
		try {
			// 网关
			if ("GATEWAY".equals(payMethodStr)) {
				// 网关
				// String frontUrl =
				// "http://122.227.225.142:23661/gateway/getPayFront.jsp";
				// String frontUrl = "http://10.55.3.236:6003/gateway/getPayFront.jsp";
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("bankCode", "vbank"); // 虚拟银行，专门用于测试环境
				// gatewayPay.put("bankCode", "cmb"); //生产环境
				payMap.put("payType", 1L);
				payMap.put("bankCardNo", RSAUtil.encrypt(bankCardNo)); // 借记卡
				payMap.put("amount", amount);

				payMethod.put("GATEWAY", payMap);
			} else if ("POSPAY".equals(payMethodStr) || "POSPAY_SZ".equals(payMethodStr)) {
				// 订单POS
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("amount", amount);

				payMethod.put("POSPAY", payMap);
			} else if ("REALNAMEPAY".equals(payMethodStr)) {
				// 实名付（单笔）
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("bankCardNo", RSAUtil.encrypt(bankCardNo));
				payMap.put("amount", amount); // 分
				payMethod.put("REALNAMEPAY", payMap);
			} else if ("REALNAMEPAY_BATCH".equals(payMethodStr)) {
				// 实名付（batch）
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("bankCardNo", RSAUtil.encrypt(bankCardNo));
				payMap.put("amount", amount); // 分
				payMethod.put("REALNAMEPAY_BATCH", payMap);
			} else if ("WITHHOLD_TLT".equals(payMethodStr)) {
				// 通联通代扣
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("bankCardNo", RSAUtil.encrypt(bankCardNo));
				payMap.put("amount", amount); // 分
				payMethod.put("WITHHOLD_TLT", payMap);
			} else if ("QUICKPAY_TLT".equals(payMethodStr)) {
				// 通联通协议支付
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("bankCardNo", RSAUtil.encrypt(bankCardNo));
				payMap.put("amount", amount); // 分
				payMethod.put("QUICKPAY_TLT", payMap);
			} else if ("WECHATPAY_MiniProgram".equals(payMethodStr)) {
				// 微信小程序支付
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("limitPay", limitPay);
				payMap.put("amount", amount); // 分
				payMap.put("Acct", wx_openid);
				payMethod.put("WECHATPAY_MiniProgram", payMap);
			} else if ("WECHATPAY_MiniProgram_ORG".equals(payMethodStr)) {
				// 微信小程序支付（集团）
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("vspCusid", vspCusid);
				payMap.put("limitPay", limitPay);
				payMap.put("amount", amount); // 分
				payMap.put("Acct", wx_openid);
				payMethod.put("WECHATPAY_MiniProgram_ORG", payMap);
			} else if ("WECHATPAY_APP".equals(payMethodStr)) {
				// 微信APP支付
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("limitPay", limitPay);
				payMap.put("amount", amount); // 分
				payMap.put("Acct", wx_openid);
				payMap.put("subAppId", subAppId);// 微信appid,平台有多个APP时需上送
				payMap.put("subMchId", subMchId);// 微信子商户号,平台有多个APP时需上送
				payMethod.put("WECHATPAY_APP", payMap);
			} else if ("WeChatPAY-APP_OPEN".equals(payMethodStr)) {
				// 微信原生APP支付
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("subAppId", subAppId);// 微信端应用ID：appid
				payMap.put("limitPay", limitPay);
				payMap.put("amount", amount); // 分
				payMethod.put("WeChatPAY-APP_OPEN", payMap);
			} else if ("SCAN_WEIXIN".equals(payMethodStr)) {
				// 微信扫码支付正扫
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("payType", "W02"); // 非贷记卡：W02, 借、贷卡：W01
				payMap.put("amount", amount); // 分
				payMethod.put("SCAN_WEIXIN", payMap);
			} else if ("SCAN_WEIXIN_ORG".equals(payMethodStr)) {
				// 微信扫码支付正扫 集团
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("vspCusid", vspCusid);
				payMap.put("payType", limitPay); // 非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				payMethod.put("SCAN_WEIXIN_ORG", payMap);
			} else if ("WECHAT_PUBLIC".equals(payMethodStr)) {
				// 微信JS支付（公众号）
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("payType", limitPay); // 非贷记卡：no_credit 借、贷记卡：””
				payMap.put("acct", wx_openid); // 微信JS支付openid——微信分配
				payMap.put("amount", amount); // 分
				payMethod.put("WECHAT_PUBLIC", payMap);
			} else if ("SCAN_ALIPAY".equals(payMethodStr)) {
				// 支付宝支付正扫
				HashMap<String, Object> payMap = new HashMap<>();
				// wechatMiniPay.put("vspCusid", vspCusid);
				payMap.put("payType", "A01"); // 传值：A01
				payMap.put("amount", amount); // 分
				payMethod.put("SCAN_ALIPAY", payMap);
			} else if ("SCAN_ALIPAY_ORG".equals(payMethodStr)) {
				// 支付宝支付正扫 集团
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("vspCusid", vspCusid);
				// aliPay.put("payType", "A01"); //传值：A01
				payMap.put("amount", amount); // 分
				payMethod.put("SCAN_ALIPAY_ORG", payMap);
			} else if ("SCAN_ALIPAY_ORG".equals(payMethodStr)) {
				// 支付宝JS支付（生活号）
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("acct", ali_user_id);
				// aliPay.put("payType", "A01"); //传值：A01
				payMap.put("amount", amount); // 分
				payMethod.put("SCAN_ALIPAY_ORG", payMap);
			} else if ("QQ_WALLET".equals(payMethodStr)) {
				// QQ钱包JS支付
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// aliPay.put("payType", "A01"); //传值：A01
				payMap.put("amount", amount); // 分
				payMethod.put("QQ_WALLET", payMap);
			} else if ("CODEPAY_W".equals(payMethodStr)) {
				// 微信刷卡支付（被扫）
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				payMap.put("payType", limitPay); // 非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				payMap.put("authcode", authcode);
				payMethod.put("CODEPAY_W", payMap);
			} else if ("CODEPAY_A".equals(payMethodStr)) {
				// 支付宝刷卡支付(被扫)
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// payMap.put("payType", limitPay); //非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				payMap.put("authcode", authcode);
				payMethod.put("CODEPAY_A", payMap);
			} else if ("CODEPAY_Q".equals(payMethodStr)) {
				// QQ刷卡支付(被扫)
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// payMap.put("payType", limitPay); //非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				payMap.put("authcode", authcode);
				payMethod.put("CODEPAY_Q", payMap);
			} else if ("BALANCE".equals(payMethodStr)) {
				// 账户余额
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// payMap.put("payType", limitPay); //非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("accountSetNo", accountSetNo);
				payMap.put("amount", amount); // 分
				payMethod.put("BALANCE", payMap);
			} else if ("WECHATPAY_H5".equals(payMethodStr)) {
				// 账户余额
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				payMap.put("limitPay", limitPay); // 非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				payMap.put("apptype", "IOS");// 枚举值：IOS，Android，Wap
				payMap.put("appname", "喜马拉雅");// 商户app名称；app名称或者网站名称,例如 王者荣耀
				payMap.put("apppackage", "com.tencent.mobileqq");
				payMap.put("cusip", "192.168.14.100");
				payMethod.put("WECHATPAY_H5", payMap);
			} else if ("WECHAT_PUBLIC_ORG".equals(payMethodStr)) {
				// 微信JS支付（公众号）_集团
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				payMap.put("vspCusid", vspCusid); // 收银宝子商户号，与公众号对应
				payMap.put("amount", amount); // 分
				payMap.put("acct", wx_openid);
				payMap.put("payType", limitPay);// 非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("WECHAT_PUBLIC_ORG", payMap);
			} else if ("ALIPAY_SERVICE_ORG".equals(payMethodStr)) {
				// 支付宝JS支付_集团
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				payMap.put("vspCusid", vspCusid); // 收银宝子商户号，与生活号对应
				payMap.put("amount", amount); // 分
				payMap.put("acct", ali_user_id);
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("ALIPAY_SERVICE_ORG", payMap);
			} else if ("QQ_WALLET_ORG".equals(payMethodStr)) {
				// QQ钱包JS支付_集团
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				payMap.put("vspCusid", vspCusid); // 收银宝子商户号
				payMap.put("amount", amount); // 分
				// payMap.put("acct", ali_user_id);
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("QQ_WALLET_ORG", payMap);
			} else if ("ORDER_VSPPAY".equals(payMethodStr)) {
				// 收银宝POS当面付及订单模式支付
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// payMap.put("vspCusid", vspCusid); //收银宝子商户号
				payMap.put("amount", amount); // 分
				// payMap.put("acct", ali_user_id);
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("ORDER_VSPPAY", payMap);
			} else if ("QUICKPAY_H5".equals(payMethodStr) || "QUICKPAY_PC".equals(payMethodStr)) {
				// 新移动H5/PC快捷支付
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// payMap.put("vspCusid", vspCusid); //收银宝子商户号
				payMap.put("amount", amount); // 分
				payMap.put("bankCardNo", RSAUtil.encrypt(bankCardNo));
				// payMap.put("acct", ali_user_id);
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("QUICKPAY_H5", payMap);
			} else if ("WECHATPAY_APP_ORG".equals(payMethodStr)) {
				// 微信APP支付_集团
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				payMap.put("vspCusid", vspCusid); // 收银宝子商户号
				payMap.put("limitPay", limitPay);// 非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				payMap.put("subAppId", subAppId);
				payMap.put("subMchId", subMchId);
				// payMap.put("acct", ali_user_id);
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("WECHATPAY_APP_ORG", payMap);
			} else if ("ALIPAY_APP_OPEN".equals(payMethodStr)) {
				// 支付宝原生APP支付
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// payMap.put("vspCusid", vspCusid); //收银宝子商户号
				// payMap.put("limitPay", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				// payMap.put("enablePayChannels", subAppId);
				// payMap.put("paysummary", subMchId);
				// payMap.put("acct", ali_user_id);
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("ALIPAY_APP_OPEN", payMap);
			} else if ("VIRTUAL_IN".equals(payMethodStr)) {
				// 通用虚拟入金
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// payMap.put("vspCusid", vspCusid); //收银宝子商户号
				// payMap.put("limitPay", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				payMap.put("extChannelNo", "AUTO");
				payMap.put("extAccount", "AUTO");
				payMap.put("paysummary", "AUTO");
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("VIRTUAL_IN", payMap);
			} else if ("WITHHOLD_SD".equals(payMethodStr)) {
				// 山东代收
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				payMap.put("bankCardNo", RSAUtil.encrypt(bankCardNo));
				payMap.put("PROVINCE", "上海");
				payMap.put("CITY", "浦东新区");
				payMap.put("amount", amount); // 分
				// payMap.put("extChannelNo", "AUTO");
				// payMap.put("extAccount", "AUTO");
				payMap.put("paysummary", "AUTO");
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("WITHHOLD_SD", payMap);
			}

			// 组装支付方式

			// payMethod.put("QUICKPAY", quickPay);

			// payMethod.put("GATEWAY", gatewayPay);
			// payMethod.put("WITHHOLD_UP", daikouPay);
			// payMethod.put("WITHHOLD_TLT", tltDaikouPay);

			request.put("payerId", payerId);
			request.put("recieverId", recieverId);
			request.put("bizOrderNo", bizOrderNo);
			request.put("amount", amount);
			//request.put("accountSetNo", accountSetNo);
			
			request.put("fee", 0L);
			request.put("validateType", validateType); // 0: 无验证；1：短信；2：支付密码
			request.put("frontUrl", frontUrl);//前台通知地址
			request.put("backUrl", backUrl);// 后台通知地址
			
			//设置分账规则，固定：
			ArrayList splitRuleList = new ArrayList();
			
			HashMap<String, Object> splitRules1 = new HashMap<String, Object>();
			splitRules1.put("bizUserId", bizUserId);//TODO: 可以固定设置一个用户
			splitRules1.put("accountSetNo", accountSetNo);
			splitRules1.put("amount", 5L);
			splitRules1.put("fee", 0L);
			splitRules1.put("remark", "SplitRule AUTO-TEST");
			splitRuleList.add(splitRules1);
			
			HashMap<String, Object> splitRules2 = new HashMap<String, Object>();
			splitRules2.put("bizUserId", bizUserId);//TODO: 可以固定设置一个用户
			splitRules2.put("accountSetNo", accountSetNo);
			splitRules2.put("amount", 2L);
			splitRules2.put("fee", 0L);
			splitRules2.put("remark", "SplitRule AUTO-TEST2");
			splitRuleList.add(splitRules2);
			
			//splitRules.put("splitRule", splitRuleList);
			//request.put("bankCardNo", RSAUtil.encrypt(bankCardNo));
			//request.put("bankCardPro", bankCardPro);
			//request.put("withdrawType", withdrawType);
			request.put("splitRule", splitRuleList);
			request.put("ordErexpireDatetime", ordErexpireDatetime);
			request.put("payMethod", JSONObject.toJSON(payMethod));
			
			request.put("goodsName", "computer");
			request.put("industryCode", "2413");
			request.put("industryName", "电子产品");
			request.put("source", 2L);
			request.put("summary", "AUTO-TEST consumeApply");
			request.put("extendInfo", "AUTO-TEST this is extendInfo");
			

			String res = YunClient.request(request);
			// System.out.println("res: " + res);
			JSONObject resp = JSON.parseObject(res);
			// System.out.println("status=[" + resp.getString("status") + "]");
			// System.out.println("signedValue=[" + resp.getString("signedValue") + "]");
			// System.out.println("sign=[" + resp.getString("sign") + "]");
			// System.out.println("errorCode=[" + resp.getString("errorCode") + "]");
			// System.out.println("message=[" + resp.getString("message") + "]");
			String status = resp.getString("status");

			if (status != null && status.equals("OK")) {
				payStatus = resp.getJSONObject("signedValue").getString("payStatus");
				payFailMessage = resp.getJSONObject("signedValue").getString("payFailMessage");
				orderNo = resp.getJSONObject("signedValue").getString("orderNo");
				tradeNo = resp.getJSONObject("signedValue").getString("tradeNo");
				payCode = resp.getJSONObject("signedValue").getString("payCode");
				extendInfo = resp.getJSONObject("signedValue").getString("extendInfo");
				payInfo = resp.getJSONObject("signedValue").getString("payInfo");
				if(payMethodStr.equals("WECHATPAY_APP")) {
					appid = resp.getJSONObject("signedValue").getJSONObject("weixinstr").getString("appid");
					packageStr = resp.getJSONObject("signedValue").getJSONObject("weixinstr").getString("package");
					prepayid = resp.getJSONObject("signedValue").getJSONObject("weixinstr").getString("prepayid");
					partnerid = resp.getJSONObject("signedValue").getJSONObject("weixinstr").getString("partnerid");
					noncestr = resp.getJSONObject("signedValue").getJSONObject("weixinstr").getString("noncestr");
					timestamp = resp.getJSONObject("signedValue").getJSONObject("weixinstr").getString("timestamp");
					sign = resp.getJSONObject("signedValue").getJSONObject("weixinstr").getString("sign");
				}
				logger.info("API[consumeApplyWithSplitRules SUCCESS!]");
			} else {
				logger.error("API_ERROR[consumeApplyWithSplitRules RESULT=[" + resp.getString("errorCode") + ", "
						+ resp.getString("message") + "]]");
			}
		} catch (final Exception e) {
			logger.error("EXCEPTION", e);
			// e.printStackTrace();
		}
		result.put("payerId", payerId);
		result.put("recieverId", recieverId);
		result.put("bizOrderNo", bizOrderNo);
		result.put("orderNo", orderNo);
		result.put("tradeNo", tradeNo);
		result.put("payCode", payCode);
		result.put("extendInfo", extendInfo);
		result.put("payInfo", payInfo);
		result.put("payStatus", payStatus);
		result.put("payFailMessage", payFailMessage);
		//wechat pay must info
		result.put("appid", appid);
		result.put("packageStr", packageStr);
		result.put("prepayid", prepayid);
		result.put("partnerid", partnerid);
		result.put("noncestr", noncestr);
		result.put("timestamp", timestamp);
		result.put("sign", sign);
		
		long end = System.currentTimeMillis();
		long elapse = end - start;
		logger.info("================OrderAPI: consumeApplyWithSplitRules end================[elapse: " + elapse + " ms]");
		return result;
	}
	
	/**
	 * 托管代收接口
	 * @param bizUserId
	 * @param payerId
	 * @param recieverId
	 * @param amount
	 * @param validateType
	 * @param frontUrl
	 * @param backUrl
	 * @param payMethodStr
	 * @param bankCardNo
	 * @param limitPay
	 * @param wx_openid
	 * @param ali_user_id
	 * @param vspCusid
	 * @param subAppId
	 * @param subMchId
	 * @param authcode
	 * @param bankCardPro
	 * @param unionBank
	 * @param bankName
	 * @param accountSetNo
	 * @param province
	 * @param city
	 * @param withdrawType
	 * @return
	 */
	public static HashMap<String, String> agentCollectApplySimplify(String bizUserId, String payerId, String recieverId, Long amount,
			Long validateType, String frontUrl, String backUrl, String payMethodStr, String bankCardNo,
			String limitPay, String wx_openid, String ali_user_id, String vspCusid, String subAppId, String subMchId,
			String authcode, Long bankCardPro, String unionBank, String bankName, String accountSetNo, 
			String province, String city, String withdrawType) {
		logger.info("================OrderAPI: agentCollectApplySimplify begin================");
		long start = System.currentTimeMillis();
		HashMap<String, String> result = new HashMap<String, String>();
		final YunRequest request = new YunRequest("OrderService", "consumeApply");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, 15);
		Date date = calendar.getTime();
		String ordErexpireDatetime = sdf.format(date);
		HashMap<String, Object> payMethod = new HashMap<>();
		String bizOrderNo = "TGDSS" + System.currentTimeMillis();

		String payStatus = "";
		String payFailMessage = "";
		String orderNo = "";
		String payCode = "";
		String tradeNo = "";
		String payInfo = "";
		String extendInfo = "";
		
		String weixinstr = "";
		String appid = "";
		String packageStr = "";
		String prepayid = "";
		String partnerid = "";
		String noncestr = "";
		String timestamp = "";
		String sign = "";
		
		// 支付方式
		try {
			// 网关
			if ("GATEWAY".equals(payMethodStr)) {
				// 网关
				// String frontUrl =
				// "http://122.227.225.142:23661/gateway/getPayFront.jsp";
				// String frontUrl = "http://10.55.3.236:6003/gateway/getPayFront.jsp";
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("bankCode", "vbank"); // 虚拟银行，专门用于测试环境
				// gatewayPay.put("bankCode", "cmb"); //生产环境
				payMap.put("payType", 1L);
				payMap.put("bankCardNo", RSAUtil.encrypt(bankCardNo)); // 借记卡
				payMap.put("amount", amount);

				payMethod.put("GATEWAY", payMap);
			} else if ("POSPAY".equals(payMethodStr) || "POSPAY_SZ".equals(payMethodStr)) {
				// 订单POS
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("amount", amount);

				payMethod.put("POSPAY", payMap);
			} else if ("REALNAMEPAY".equals(payMethodStr)) {
				// 实名付（单笔）
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("bankCardNo", RSAUtil.encrypt(bankCardNo));
				payMap.put("amount", amount); // 分
				payMethod.put("REALNAMEPAY", payMap);
			} else if ("REALNAMEPAY_BATCH".equals(payMethodStr)) {
				// 实名付（batch）
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("bankCardNo", RSAUtil.encrypt(bankCardNo));
				payMap.put("amount", amount); // 分
				payMethod.put("REALNAMEPAY_BATCH", payMap);
			} else if ("WITHHOLD_TLT".equals(payMethodStr)) {
				// 通联通代扣
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("bankCardNo", RSAUtil.encrypt(bankCardNo));
				payMap.put("amount", amount); // 分
				payMethod.put("WITHHOLD_TLT", payMap);
			} else if ("QUICKPAY_TLT".equals(payMethodStr)) {
				// 通联通协议支付
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("bankCardNo", RSAUtil.encrypt(bankCardNo));
				payMap.put("amount", amount); // 分
				payMethod.put("QUICKPAY_TLT", payMap);
			} else if ("WECHATPAY_MiniProgram".equals(payMethodStr)) {
				// 微信小程序支付
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("limitPay", limitPay);
				payMap.put("amount", amount); // 分
				payMap.put("Acct", wx_openid);
				payMethod.put("WECHATPAY_MiniProgram", payMap);
			} else if ("WECHATPAY_MiniProgram_ORG".equals(payMethodStr)) {
				// 微信小程序支付（集团）
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("vspCusid", vspCusid);
				payMap.put("limitPay", limitPay);
				payMap.put("amount", amount); // 分
				payMap.put("Acct", wx_openid);
				payMethod.put("WECHATPAY_MiniProgram_ORG", payMap);
			} else if ("WECHATPAY_APP".equals(payMethodStr)) {
				// 微信APP支付
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("limitPay", limitPay);
				payMap.put("amount", amount); // 分
				payMap.put("Acct", wx_openid);
				payMap.put("subAppId", subAppId);// 微信appid,平台有多个APP时需上送
				payMap.put("subMchId", subMchId);// 微信子商户号,平台有多个APP时需上送
				payMethod.put("WECHATPAY_APP", payMap);
			} else if ("WeChatPAY-APP_OPEN".equals(payMethodStr)) {
				// 微信原生APP支付
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("subAppId", subAppId);// 微信端应用ID：appid
				payMap.put("limitPay", limitPay);
				payMap.put("amount", amount); // 分
				payMethod.put("WeChatPAY-APP_OPEN", payMap);
			} else if ("SCAN_WEIXIN".equals(payMethodStr)) {
				// 微信扫码支付正扫
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("payType", "W02"); // 非贷记卡：W02, 借、贷卡：W01
				payMap.put("amount", amount); // 分
				payMethod.put("SCAN_WEIXIN", payMap);
			} else if ("SCAN_WEIXIN_ORG".equals(payMethodStr)) {
				// 微信扫码支付正扫 集团
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("vspCusid", vspCusid);
				payMap.put("payType", limitPay); // 非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				payMethod.put("SCAN_WEIXIN_ORG", payMap);
			} else if ("WECHAT_PUBLIC".equals(payMethodStr)) {
				// 微信JS支付（公众号）
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("payType", limitPay); // 非贷记卡：no_credit 借、贷记卡：””
				payMap.put("acct", wx_openid); // 微信JS支付openid——微信分配
				payMap.put("amount", amount); // 分
				payMethod.put("WECHAT_PUBLIC", payMap);
			} else if ("SCAN_ALIPAY".equals(payMethodStr)) {
				// 支付宝支付正扫
				HashMap<String, Object> payMap = new HashMap<>();
				// wechatMiniPay.put("vspCusid", vspCusid);
				payMap.put("payType", "A01"); // 传值：A01
				payMap.put("amount", amount); // 分
				payMethod.put("SCAN_ALIPAY", payMap);
			} else if ("SCAN_ALIPAY_ORG".equals(payMethodStr)) {
				// 支付宝支付正扫 集团
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("vspCusid", vspCusid);
				// aliPay.put("payType", "A01"); //传值：A01
				payMap.put("amount", amount); // 分
				payMethod.put("SCAN_ALIPAY_ORG", payMap);
			} else if ("SCAN_ALIPAY_ORG".equals(payMethodStr)) {
				// 支付宝JS支付（生活号）
				HashMap<String, Object> payMap = new HashMap<>();
				payMap.put("acct", ali_user_id);
				// aliPay.put("payType", "A01"); //传值：A01
				payMap.put("amount", amount); // 分
				payMethod.put("SCAN_ALIPAY_ORG", payMap);
			} else if ("QQ_WALLET".equals(payMethodStr)) {
				// QQ钱包JS支付
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// aliPay.put("payType", "A01"); //传值：A01
				payMap.put("amount", amount); // 分
				payMethod.put("QQ_WALLET", payMap);
			} else if ("CODEPAY_W".equals(payMethodStr)) {
				// 微信刷卡支付（被扫）
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				payMap.put("payType", limitPay); // 非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				payMap.put("authcode", authcode);
				payMethod.put("CODEPAY_W", payMap);
			} else if ("CODEPAY_A".equals(payMethodStr)) {
				// 支付宝刷卡支付(被扫)
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// payMap.put("payType", limitPay); //非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				payMap.put("authcode", authcode);
				payMethod.put("CODEPAY_A", payMap);
			} else if ("CODEPAY_Q".equals(payMethodStr)) {
				// QQ刷卡支付(被扫)
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// payMap.put("payType", limitPay); //非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				payMap.put("authcode", authcode);
				payMethod.put("CODEPAY_Q", payMap);
			} else if ("BALANCE".equals(payMethodStr)) {
				// 账户余额
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// payMap.put("payType", limitPay); //非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("accountSetNo", accountSetNo);
				payMap.put("amount", amount); // 分
				payMethod.put("BALANCE", payMap);
			} else if ("WECHATPAY_H5".equals(payMethodStr)) {
				// 账户余额
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				payMap.put("limitPay", limitPay); // 非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				payMap.put("apptype", "IOS");// 枚举值：IOS，Android，Wap
				payMap.put("appname", "喜马拉雅");// 商户app名称；app名称或者网站名称,例如 王者荣耀
				payMap.put("apppackage", "com.tencent.mobileqq");
				payMap.put("cusip", "192.168.14.100");
				payMethod.put("WECHATPAY_H5", payMap);
			} else if ("WECHAT_PUBLIC_ORG".equals(payMethodStr)) {
				// 微信JS支付（公众号）_集团
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				payMap.put("vspCusid", vspCusid); // 收银宝子商户号，与公众号对应
				payMap.put("amount", amount); // 分
				payMap.put("acct", wx_openid);
				payMap.put("payType", limitPay);// 非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("WECHAT_PUBLIC_ORG", payMap);
			} else if ("ALIPAY_SERVICE_ORG".equals(payMethodStr)) {
				// 支付宝JS支付_集团
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				payMap.put("vspCusid", vspCusid); // 收银宝子商户号，与生活号对应
				payMap.put("amount", amount); // 分
				payMap.put("acct", ali_user_id);
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("ALIPAY_SERVICE_ORG", payMap);
			} else if ("QQ_WALLET_ORG".equals(payMethodStr)) {
				// QQ钱包JS支付_集团
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				payMap.put("vspCusid", vspCusid); // 收银宝子商户号
				payMap.put("amount", amount); // 分
				// payMap.put("acct", ali_user_id);
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("QQ_WALLET_ORG", payMap);
			} else if ("ORDER_VSPPAY".equals(payMethodStr)) {
				// 收银宝POS当面付及订单模式支付
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// payMap.put("vspCusid", vspCusid); //收银宝子商户号
				payMap.put("amount", amount); // 分
				// payMap.put("acct", ali_user_id);
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("ORDER_VSPPAY", payMap);
			} else if ("QUICKPAY_H5".equals(payMethodStr) || "QUICKPAY_PC".equals(payMethodStr)) {
				// 新移动H5/PC快捷支付
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// payMap.put("vspCusid", vspCusid); //收银宝子商户号
				payMap.put("amount", amount); // 分
				payMap.put("bankCardNo", RSAUtil.encrypt(bankCardNo));
				// payMap.put("acct", ali_user_id);
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("QUICKPAY_H5", payMap);
			} else if ("WECHATPAY_APP_ORG".equals(payMethodStr)) {
				// 微信APP支付_集团
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				payMap.put("vspCusid", vspCusid); // 收银宝子商户号
				payMap.put("limitPay", limitPay);// 非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				payMap.put("subAppId", subAppId);
				payMap.put("subMchId", subMchId);
				// payMap.put("acct", ali_user_id);
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("WECHATPAY_APP_ORG", payMap);
			} else if ("ALIPAY_APP_OPEN".equals(payMethodStr)) {
				// 支付宝原生APP支付
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// payMap.put("vspCusid", vspCusid); //收银宝子商户号
				// payMap.put("limitPay", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				// payMap.put("enablePayChannels", subAppId);
				// payMap.put("paysummary", subMchId);
				// payMap.put("acct", ali_user_id);
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("ALIPAY_APP_OPEN", payMap);
			} else if ("VIRTUAL_IN".equals(payMethodStr)) {
				// 通用虚拟入金
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				// payMap.put("vspCusid", vspCusid); //收银宝子商户号
				// payMap.put("limitPay", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMap.put("amount", amount); // 分
				payMap.put("extChannelNo", "AUTO");
				payMap.put("extAccount", "AUTO");
				payMap.put("paysummary", "AUTO");
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("VIRTUAL_IN", payMap);
			} else if ("WITHHOLD_SD".equals(payMethodStr)) {
				// 山东代收
				HashMap<String, Object> payMap = new HashMap<>();
				// aliPay.put("acct", ali_user_id);
				payMap.put("bankCardNo", RSAUtil.encrypt(bankCardNo));
				payMap.put("PROVINCE", "上海");
				payMap.put("CITY", "浦东新区");
				payMap.put("amount", amount); // 分
				// payMap.put("extChannelNo", "AUTO");
				// payMap.put("extAccount", "AUTO");
				payMap.put("paysummary", "AUTO");
				// payMap.put("payType", limitPay);//非贷记卡：no_credit; 借、贷记卡：””
				payMethod.put("WITHHOLD_SD", payMap);
			}

			// 组装支付方式

			// payMethod.put("QUICKPAY", quickPay);

			// payMethod.put("GATEWAY", gatewayPay);
			// payMethod.put("WITHHOLD_UP", daikouPay);
			// payMethod.put("WITHHOLD_TLT", tltDaikouPay);

			request.put("payerId", payerId);
			//request.put("recieverId", recieverId);
			request.put("bizOrderNo", bizOrderNo);
			request.put("tradeCode", "3001"); //消费代收
			request.put("amount", amount);
			//request.put("accountSetNo", accountSetNo);
			
			request.put("fee", 0L);
			request.put("validateType", validateType); // 0: 无验证；1：短信；2：支付密码
			request.put("frontUrl", frontUrl);//前台通知地址
			request.put("backUrl", backUrl);// 后台通知地址
	
			request.put("ordErexpireDatetime", ordErexpireDatetime);
			request.put("payMethod", JSONObject.toJSON(payMethod));
			
			request.put("goodsName", "computer");
			request.put("industryCode", "2413");
			request.put("industryName", "电子产品");
			request.put("source", 2L);
			request.put("summary", "AUTO-TEST agentCollectApplySimplify");
			request.put("extendInfo", "AUTO-TEST agentCollectApplySimplify this is extendInfo");
			

			String res = YunClient.request(request);
			// System.out.println("res: " + res);
			JSONObject resp = JSON.parseObject(res);
			// System.out.println("status=[" + resp.getString("status") + "]");
			// System.out.println("signedValue=[" + resp.getString("signedValue") + "]");
			// System.out.println("sign=[" + resp.getString("sign") + "]");
			// System.out.println("errorCode=[" + resp.getString("errorCode") + "]");
			// System.out.println("message=[" + resp.getString("message") + "]");
			String status = resp.getString("status");

			if (status != null && status.equals("OK")) {
				payStatus = resp.getJSONObject("signedValue").getString("payStatus");
				payFailMessage = resp.getJSONObject("signedValue").getString("payFailMessage");
				orderNo = resp.getJSONObject("signedValue").getString("orderNo");
				tradeNo = resp.getJSONObject("signedValue").getString("tradeNo");
				payCode = resp.getJSONObject("signedValue").getString("payCode");
				extendInfo = resp.getJSONObject("signedValue").getString("extendInfo");
				payInfo = resp.getJSONObject("signedValue").getString("payInfo");
				if(payMethodStr.equals("WECHATPAY_APP")) {
					appid = resp.getJSONObject("signedValue").getJSONObject("weixinstr").getString("appid");
					packageStr = resp.getJSONObject("signedValue").getJSONObject("weixinstr").getString("package");
					prepayid = resp.getJSONObject("signedValue").getJSONObject("weixinstr").getString("prepayid");
					partnerid = resp.getJSONObject("signedValue").getJSONObject("weixinstr").getString("partnerid");
					noncestr = resp.getJSONObject("signedValue").getJSONObject("weixinstr").getString("noncestr");
					timestamp = resp.getJSONObject("signedValue").getJSONObject("weixinstr").getString("timestamp");
					sign = resp.getJSONObject("signedValue").getJSONObject("weixinstr").getString("sign");
				}
				logger.info("API[agentCollectApplySimplify SUCCESS!]");
			} else {
				logger.error("API_ERROR[agentCollectApplySimplify RESULT=[" + resp.getString("errorCode") + ", "
						+ resp.getString("message") + "]]");
			}
		} catch (final Exception e) {
			logger.error("EXCEPTION", e);
			// e.printStackTrace();
		}
		result.put("payerId", payerId);
		result.put("recieverId", recieverId);
		result.put("bizOrderNo", bizOrderNo);
		result.put("orderNo", orderNo);
		result.put("tradeNo", tradeNo);
		result.put("payCode", payCode);
		result.put("extendInfo", extendInfo);
		result.put("payInfo", payInfo);
		result.put("payStatus", payStatus);
		result.put("payFailMessage", payFailMessage);
		//wechat pay must info
		result.put("appid", appid);
		result.put("packageStr", packageStr);
		result.put("prepayid", prepayid);
		result.put("partnerid", partnerid);
		result.put("noncestr", noncestr);
		result.put("timestamp", timestamp);
		result.put("sign", sign);
		
		long end = System.currentTimeMillis();
		long elapse = end - start;
		logger.info("================OrderAPI: agentCollectApplySimplify end================[elapse: " + elapse + " ms]");
		return result;
	}
	
	/**
	 * 前台页面支付
	 * @param bizUserId
	 * @param bizOrderNo
	 * @param tradeNo
	 * @param verificationCode
	 * @param consumerIp
	 * @return
	 */
	public static HashMap<String, String> frontPay(String bizUserId, String bizOrderNo, String tradeNo,
			String verificationCode, String consumerIp) {
		logger.info("================OrderAPI: backPay begin================");
		long start = System.currentTimeMillis();
		HashMap<String, String> result = new HashMap<String, String>();
		final YunRequest request = new YunRequest("OrderService", "pay");

		HashMap<String, Object> payMethod = new HashMap<>();

		String payStatus = "";
		String payFailMessage = "";
		
		try {
			request.put("bizUserId", bizUserId);
			request.put("bizOrderNo", bizOrderNo);
			request.put("tradeNo", tradeNo); //快捷支付必传
			
			request.put("verificationCode", verificationCode); // 0: 无验证；1：短信；2：支付密码
			request.put("consumerIp", consumerIp);

			String res = YunClient.request(request);
			JSONObject resp = JSON.parseObject(res);
			
			String status = resp.getString("status");

			if (status != null && status.equals("OK")) {
				payStatus = resp.getJSONObject("signedValue").getString("payStatus");
				payFailMessage = resp.getJSONObject("signedValue").getString("payFailMessage");
				
				logger.info("API[backPay SUCCESS!]");
			} else {
				logger.error("API_ERROR[backPay RESULT=[" + resp.getString("errorCode") + ", "
						+ resp.getString("message") + "]]");
			}
		} catch (final Exception e) {
			logger.error("EXCEPTION", e);
			// e.printStackTrace();
		}
		result.put("bizUserId", bizUserId);
		result.put("bizOrderNo", bizOrderNo);
		result.put("tradeNo", tradeNo);
		result.put("payStatus", payStatus);
		result.put("payFailMessage", payFailMessage);
		long end = System.currentTimeMillis();
		long elapse = end - start;
		logger.info("================OrderAPI: backPay end================[elapse: " + elapse + " ms]");
		return result;
	}
	
	/**
	 * 后台确认支付-短信验证方式
	 * @param bizUserId
	 * @param bizOrderNo
	 * @param tradeNo
	 * @param verificationCode
	 * @param consumerIp
	 * @return
	 */
	public static HashMap<String, String> backPay(String bizUserId, String bizOrderNo, String tradeNo,
			String verificationCode, String consumerIp) {
		logger.info("================OrderAPI: backPay begin================");
		long start = System.currentTimeMillis();
		HashMap<String, String> result = new HashMap<String, String>();
		final YunRequest request = new YunRequest("OrderService", "pay");

		HashMap<String, Object> payMethod = new HashMap<>();

		String payStatus = "";
		String payFailMessage = "";
		
		try {
			request.put("bizUserId", bizUserId);
			request.put("bizOrderNo", bizOrderNo);
			request.put("tradeNo", tradeNo); //快捷支付必传
			
			request.put("verificationCode", verificationCode); // 0: 无验证；1：短信；2：支付密码
			request.put("consumerIp", consumerIp);

			String res = YunClient.request(request);
			JSONObject resp = JSON.parseObject(res);
			
			String status = resp.getString("status");

			if (status != null && status.equals("OK")) {
				payStatus = resp.getJSONObject("signedValue").getString("payStatus");
				payFailMessage = resp.getJSONObject("signedValue").getString("payFailMessage");
				
				logger.info("API[backPay SUCCESS!]");
			} else {
				logger.error("API_ERROR[backPay RESULT=[" + resp.getString("errorCode") + ", "
						+ resp.getString("message") + "]]");
			}
		} catch (final Exception e) {
			logger.error("EXCEPTION", e);
			// e.printStackTrace();
		}
		result.put("bizUserId", bizUserId);
		result.put("bizOrderNo", bizOrderNo);
		result.put("tradeNo", tradeNo);
		result.put("payStatus", payStatus);
		result.put("payFailMessage", payFailMessage);
		long end = System.currentTimeMillis();
		long elapse = end - start;
		logger.info("================OrderAPI: backPay end================[elapse: " + elapse + " ms]");
		return result;
	}
	
	/**
	 * 后台虚拟通道确认支付
	 * @param bizUserId
	 * @param bizOrderNo
	 * @param tradeNo
	 * @param verificationCode
	 * @param consumerIp
	 * @return
	 */
	public static HashMap<String, String> backVirtualPay(String bizUserId, String bizOrderNo, String tradeNo,
			String verificationCode, String consumerIp) {
		logger.info("================OrderAPI: backVirtualPay begin================");
		long start = System.currentTimeMillis();
		HashMap<String, String> result = new HashMap<String, String>();
		final YunRequest request = new YunRequest("OrderService", "payVirtual");

		HashMap<String, Object> payMethod = new HashMap<>();

		String payStatus = "";
		String payFailMessage = "";
		String extTradeNo = "AUTO-"+System.currentTimeMillis();
		try {
			request.put("bizUserId", bizUserId);
			request.put("bizOrderNo", bizOrderNo);
			
			request.put("flag", "confirm");
			request.put("extPayMethod", "AUTO");
			
			request.put("extTradeNo", extTradeNo); //快捷支付必传
			
			request.put("summary", "AUTO-TEST");
			request.put("consumerIp", consumerIp);

			String res = YunClient.request(request);
			JSONObject resp = JSON.parseObject(res);
			
			String status = resp.getString("status");

			if (status != null && status.equals("OK")) {
				payStatus = resp.getJSONObject("signedValue").getString("payStatus");
				payFailMessage = resp.getJSONObject("signedValue").getString("payFailMessage");
				
				logger.info("API[backVirtualPay SUCCESS!]");
			} else {
				logger.error("API_ERROR[backVirtualPay RESULT=[" + resp.getString("errorCode") + ", "
						+ resp.getString("message") + "]]");
			}
		} catch (final Exception e) {
			logger.error("EXCEPTION", e);
			// e.printStackTrace();
		}
		result.put("bizUserId", bizUserId);
		result.put("bizOrderNo", bizOrderNo);
		result.put("extTradeNo", extTradeNo);
		result.put("payStatus", payStatus);
		result.put("payFailMessage", payFailMessage);
		long end = System.currentTimeMillis();
		long elapse = end - start;
		logger.info("================OrderAPI: backVirtualPay end================[elapse: " + elapse + " ms]");
		return result;
	}
	
	/**
	 * 单笔托管代付到账户
	 * @param bizUserId
	 * @param bizOrderNo
	 * @param accountSetNo
	 * @param backUrl
	 * @param amount
	 * @return
	 */
	public static HashMap<String, String> signalAgentPaySimplify(String bizUserId, String bizOrderNo, String accountSetNo,
			String backUrl, Long amount) {
		logger.info("================OrderAPI: signalAgentPaySimplify begin================");
		long start = System.currentTimeMillis();
		HashMap<String, String> result = new HashMap<String, String>();
		final YunRequest request = new YunRequest("OrderService", "signalAgentPaySimplify");

		HashMap<String, Object> payMethod = new HashMap<>();

		String payStatus = "";
		String payFailMessage = "";
		String orderNo = "";
		//String bizOrderNo = "";
		String payWhereabouts = "";
		String extTradeNo = "AUTO-"+System.currentTimeMillis();
		try {
			request.put("bizUserId", bizUserId); //商户系统用户标识，商户系统中唯一编号,收款账户bizUserId，支持个人会员、企业会员
			request.put("bizOrderNo", bizOrderNo);
			
			request.put("accountSetNo", accountSetNo);
			request.put("backUrl", backUrl);
			
			request.put("amount", amount); 
			request.put("fee", 0L); 
			//request.put("splitRuleList", ""); 
			request.put("goodsType", 3L);
			request.put("goodsNo", "AUTO-TEST-goodsNo");
			request.put("tradeCode", "4001");
			request.put("summary", "AUTO-TEST");
			request.put("extendInfo", "AUTO-TEST-extendInfo");

			String res = YunClient.request(request);
			JSONObject resp = JSON.parseObject(res);
			
			String status = resp.getString("status");

			if (status != null && status.equals("OK")) {
				payStatus = resp.getJSONObject("signedValue").getString("payStatus");
				payFailMessage = resp.getJSONObject("signedValue").getString("payFailMessage");
				orderNo = resp.getJSONObject("signedValue").getString("orderNo");
				bizOrderNo = resp.getJSONObject("signedValue").getString("bizOrderNo");
				payWhereabouts = resp.getJSONObject("signedValue").getString("payWhereabouts");
				logger.info("API[signalAgentPaySimplify SUCCESS! payStatus="+payStatus+",orderNo=" + orderNo +", payWhereabouts="+payWhereabouts+"]");
			} else {
				logger.error("API_ERROR[signalAgentPaySimplify RESULT=[" + resp.getString("errorCode") + ", "
						+ resp.getString("message") + "]]");
			}
		} catch (final Exception e) {
			logger.error("EXCEPTION", e);
			// e.printStackTrace();
		}
		result.put("bizUserId", bizUserId);
		result.put("bizOrderNo", bizOrderNo);
		//result.put("extTradeNo", extTradeNo);
		result.put("payStatus", payStatus);
		result.put("payFailMessage", payFailMessage);
		long end = System.currentTimeMillis();
		long elapse = end - start;
		logger.info("================OrderAPI: signalAgentPaySimplify end================[elapse: " + elapse + " ms]");
		return result;
	}
	
	/**
	 * 代笔代付到银行卡
	 * @param bizUserId
	 * @param bizOrderNo
	 * @param accountSetNo
	 * @param backUrl
	 * @param amount
	 * @param bankCardNo
	 * @return
	 */
	public static HashMap<String, String> signalAgentPaySimplify2BankCard(String bizUserId, String bizOrderNo, String accountSetNo,
			String backUrl, Long amount, String bankCardNo ) {
		logger.info("================OrderAPI: signalAgentPaySimplify2BankCard begin================");
		long start = System.currentTimeMillis();
		HashMap<String, String> result = new HashMap<String, String>();
		final YunRequest request = new YunRequest("OrderService", "signalAgentPaySimplify");

		HashMap<String, Object> payMethod = new HashMap<>();

		String payStatus = "";
		String payFailMessage = "";
		String orderNo = "";
		//String bizOrderNo = "";
		String payWhereabouts = "";
		String extTradeNo = "AUTO-"+System.currentTimeMillis();
		try {
			request.put("bizUserId", bizUserId); //商户系统用户标识，商户系统中唯一编号,收款账户bizUserId，支持个人会员、企业会员
			request.put("bizOrderNo", bizOrderNo);
			
			request.put("accountSetNo", accountSetNo);
			request.put("backUrl", backUrl);
			
			HashMap<String, Object> payToBankCardInfo = new HashMap<String, Object>();
			payToBankCardInfo.put("bankCardNo", bankCardNo);
			payToBankCardInfo.put("amount", amount);
			payToBankCardInfo.put("backUrl", backUrl);
			payToBankCardInfo.put("withdrawType", "T0");//T0：T+0到账;T1：T+1到账;T1customized：T+1到账，仅工作日代付;D0customized：D+0到账，根据平台资金头寸付款;默认为T0
			
			payToBankCardInfo.put("unionBank", "03080000"); // CMB
			payToBankCardInfo.put("province", "上海");
			payToBankCardInfo.put("city", "浦东新区");
			payToBankCardInfo.put("bankCardPro", 0L); //0：个人银行卡;1：企业对公账户;如果不传默认为0
			
			request.put("payToBankCardInfo", payToBankCardInfo); 
			request.put("amount", amount); 
			request.put("fee", 0L); 
			//request.put("splitRuleList", ""); 
			request.put("goodsType", 3L);
			request.put("goodsNo", "AUTO-TEST-goodsNo");
			request.put("tradeCode", "4001");
			request.put("summary", "AUTO-TEST");
			request.put("extendInfo", "AUTO-TEST-extendInfo");

			String res = YunClient.request(request);
			JSONObject resp = JSON.parseObject(res);
			
			String status = resp.getString("status");

			if (status != null && status.equals("OK")) {
				payStatus = resp.getJSONObject("signedValue").getString("payStatus");
				payFailMessage = resp.getJSONObject("signedValue").getString("payFailMessage");
				orderNo = resp.getJSONObject("signedValue").getString("orderNo");
				bizOrderNo = resp.getJSONObject("signedValue").getString("bizOrderNo");
				payWhereabouts = resp.getJSONObject("signedValue").getString("payWhereabouts");
				logger.info("API[signalAgentPaySimplify2BankCard SUCCESS! payStatus="+payStatus+",orderNo=" + orderNo +", payWhereabouts="+payWhereabouts+"]");
			} else {
				logger.error("API_ERROR[signalAgentPaySimplify2BankCard RESULT=[" + resp.getString("errorCode") + ", "
						+ resp.getString("message") + "]]");
			}
		} catch (final Exception e) {
			logger.error("EXCEPTION", e);
			// e.printStackTrace();
		}
		result.put("bizUserId", bizUserId);
		result.put("bizOrderNo", bizOrderNo);
		//result.put("extTradeNo", extTradeNo);
		result.put("payStatus", payStatus);
		result.put("payFailMessage", payFailMessage);
		long end = System.currentTimeMillis();
		long elapse = end - start;
		logger.info("================OrderAPI: signalAgentPaySimplify2BankCard end================[elapse: " + elapse + " ms]");
		return result;
	}
	
	/**
	 * 单笔代付-带分账；
	 * @param bizUserId
	 * @param bizOrderNo
	 * @param accountSetNo
	 * @param backUrl
	 * @param amount
	 * @param bankCardNo
	 * @return
	 */
	public static HashMap<String, String> signalAgentPaySimplifyWithSplitRules(String bizUserId, String bizOrderNo, String accountSetNo,
			String backUrl, Long amount, String bankCardNo ) {
		logger.info("================OrderAPI: signalAgentPaySimplifyWithSplitRules begin================");
		long start = System.currentTimeMillis();
		HashMap<String, String> result = new HashMap<String, String>();
		final YunRequest request = new YunRequest("OrderService", "signalAgentPaySimplify");

		HashMap<String, Object> payMethod = new HashMap<>();

		String payStatus = "";
		String payFailMessage = "";
		String orderNo = "";
		//String bizOrderNo = "";
		String payWhereabouts = "";
		String extTradeNo = "AUTO-"+System.currentTimeMillis();
		try {
			request.put("bizUserId", bizUserId); //商户系统用户标识，商户系统中唯一编号,收款账户bizUserId，支持个人会员、企业会员
			request.put("bizOrderNo", bizOrderNo);
			
			request.put("accountSetNo", accountSetNo);
			request.put("backUrl", backUrl);
			
			HashMap<String, Object> payToBankCardInfo = new HashMap<String, Object>();
			payToBankCardInfo.put("bankCardNo", bankCardNo);
			payToBankCardInfo.put("amount", amount);
			payToBankCardInfo.put("backUrl", backUrl);
			payToBankCardInfo.put("withdrawType", "T0");//T0：T+0到账;T1：T+1到账;T1customized：T+1到账，仅工作日代付;D0customized：D+0到账，根据平台资金头寸付款;默认为T0
			
			payToBankCardInfo.put("unionBank", "03080000"); // CMB
			payToBankCardInfo.put("province", "上海");
			payToBankCardInfo.put("city", "浦东新区");
			payToBankCardInfo.put("bankCardPro", 0L); //0：个人银行卡;1：企业对公账户;如果不传默认为0
			
			request.put("payToBankCardInfo", payToBankCardInfo); 
			request.put("amount", amount); 
			request.put("fee", 0L); 
			
			//设置分账规则，固定：
			ArrayList splitRuleList = new ArrayList();
			HashMap<String, Object> splitRules1 = new HashMap<String, Object>();
			splitRules1.put("bizUserId", bizUserId);//TODO: 可以固定设置一个用户
			splitRules1.put("accountSetNo", accountSetNo);
			splitRules1.put("amount", 5L);
			splitRules1.put("fee", 0L);
			splitRules1.put("remark", "SplitRule AUTO-TEST");
			
			HashMap<String, Object> splitRules2 = new HashMap<String, Object>();
			splitRules2.put("bizUserId", bizUserId);//TODO: 可以固定设置一个用户
			splitRules2.put("accountSetNo", accountSetNo);
			splitRules2.put("amount", 2L);
			splitRules2.put("fee", 0L);
			splitRules2.put("remark", "SplitRule AUTO-TEST2");
			splitRules1.put("splitRuleList", splitRules2);
			//splitRuleList.add(splitRules2);
			splitRuleList.add(splitRules1);
			
			
			request.put("splitRuleList", splitRuleList); 
			request.put("goodsType", 3L);
			request.put("goodsNo", "AUTO-TEST-goodsNo");
			request.put("tradeCode", "4001");
			request.put("summary", "AUTO-TEST");
			request.put("extendInfo", "AUTO-TEST-extendInfo");

			String res = YunClient.request(request);
			JSONObject resp = JSON.parseObject(res);
			
			String status = resp.getString("status");

			if (status != null && status.equals("OK")) {
				payStatus = resp.getJSONObject("signedValue").getString("payStatus");
				payFailMessage = resp.getJSONObject("signedValue").getString("payFailMessage");
				orderNo = resp.getJSONObject("signedValue").getString("orderNo");
				bizOrderNo = resp.getJSONObject("signedValue").getString("bizOrderNo");
				payWhereabouts = resp.getJSONObject("signedValue").getString("payWhereabouts");
				logger.info("API[signalAgentPaySimplifyWithSplitRules SUCCESS! payStatus="+payStatus+",orderNo=" + orderNo +", payWhereabouts="+payWhereabouts+"]");
			} else {
				logger.error("API_ERROR[signalAgentPaySimplifyWithSplitRules RESULT=[" + resp.getString("errorCode") + ", "
						+ resp.getString("message") + "]]");
			}
		} catch (final Exception e) {
			logger.error("EXCEPTION", e);
			// e.printStackTrace();
		}
		result.put("bizUserId", bizUserId);
		result.put("bizOrderNo", bizOrderNo);
		//result.put("extTradeNo", extTradeNo);
		result.put("payStatus", payStatus);
		result.put("payFailMessage", payFailMessage);
		long end = System.currentTimeMillis();
		long elapse = end - start;
		logger.info("================OrderAPI: signalAgentPaySimplifyWithSplitRules end================[elapse: " + elapse + " ms]");
		return result;
	}
	
	/**
	 * 批量托管代付-简化
	 * @param bizUserId
	 * @param accountSetNo
	 * @param backUrl
	 * @param amount
	 * @param bankCardNo
	 * @return
	 */
	public static HashMap<String, String> batchAgentPaySimplify(String bizUserId, String accountSetNo,
			String backUrl, Long amount, String bankCardNo ) {
		logger.info("================OrderAPI: batchAgentPaySimplify begin================");
		long start = System.currentTimeMillis();
		HashMap<String, String> result = new HashMap<String, String>();
		final YunRequest request = new YunRequest("OrderService", "batchAgentPaySimplify");

		HashMap<String, Object> payMethod = new HashMap<>();

		//String payStatus = "";
		//String payFailMessage = "";
		//String orderNo = "";
		String bizOrderNo = "AUTO-"+System.currentTimeMillis();;
		//String payWhereabouts = "";
		
		String bizBatchNo = "AUTO-"+System.currentTimeMillis();
		Long goodsType = 3L;  // real goods
		String goodsNo  = "AUTO-goodsNo";
		String tradeCode = "3001";
		try {
			request.put("bizBatchNo", bizBatchNo); //商户批次号
			ArrayList list = new ArrayList();
			HashMap<String, Object> batchPayMap = new HashMap<String, Object>();
			batchPayMap.put("bizOrderNo", bizOrderNo);
			batchPayMap.put("bizUserId", bizUserId);
			batchPayMap.put("accountSetNo", accountSetNo);
			batchPayMap.put("backUrl", backUrl);
			batchPayMap.put("amount", amount);
			batchPayMap.put("fee", 0L);
			list.add(batchPayMap);
			request.put("batchPayList", list); 
			request.put("summary", "AUTO-TEST--summary"); 
			request.put("extendInfo", "AUTO-TEST-extendInfo");

			String res = YunClient.request(request);
			JSONObject resp = JSON.parseObject(res);
			
			String status = resp.getString("status");

			if (status != null && status.equals("OK")) {
				bizBatchNo = resp.getJSONObject("signedValue").getString("bizBatchNo");
//				payFailMessage = resp.getJSONObject("signedValue").getString("payFailMessage");
//				orderNo = resp.getJSONObject("signedValue").getString("orderNo");
//				bizOrderNo = resp.getJSONObject("signedValue").getString("bizOrderNo");
//				payWhereabouts = resp.getJSONObject("signedValue").getString("payWhereabouts");
				logger.info("API[batchAgentPaySimplify SUCCESS! bizBatchNo=" + bizBatchNo + "]");
			} else {
				logger.error("API_ERROR[batchAgentPaySimplify RESULT=[" + resp.getString("errorCode") + ", "
						+ resp.getString("message") + "]]");
			}
		} catch (final Exception e) {
			logger.error("EXCEPTION", e);
			// e.printStackTrace();
		}
		result.put("bizBatchNo", bizBatchNo);
		long end = System.currentTimeMillis();
		long elapse = end - start;
		logger.info("================OrderAPI: batchAgentPaySimplify end================[elapse: " + elapse + " ms]");
		return result;
	}
	
	/**
	 * 批量代付到银行卡
	 * @param bizUserId
	 * @param accountSetNo
	 * @param backUrl
	 * @param amount
	 * @param bankCardNo
	 * @return
	 */
	public static HashMap<String, String> batchAgentPaySimplify2BankCard(String bizUserId, String accountSetNo,
			String backUrl, Long amount, String bankCardNo) {
		logger.info("================OrderAPI: batchAgentPaySimplify2BankCard begin================");
		long start = System.currentTimeMillis();
		HashMap<String, String> result = new HashMap<String, String>();
		final YunRequest request = new YunRequest("OrderService", "batchAgentPaySimplify");

		HashMap<String, Object> payMethod = new HashMap<>();

		//String payStatus = "";
		//String payFailMessage = "";
		//String orderNo = "";
		String bizOrderNo = "AUTO-"+System.currentTimeMillis();
		//String payWhereabouts = "";
		
		String bizBatchNo = "AUTO-"+System.currentTimeMillis();
		Long goodsType = 3L;  // real goods
		String goodsNo  = "AUTO-goodsNo";
		String tradeCode = "3001";
		try {
			request.put("bizBatchNo", bizBatchNo); //商户批次号
			ArrayList list = new ArrayList();
			HashMap<String, Object> batchPayMap = new HashMap<String, Object>();
			batchPayMap.put("bizOrderNo", bizOrderNo);
			batchPayMap.put("bizUserId", bizUserId); 
			batchPayMap.put("accountSetNo", accountSetNo);
			batchPayMap.put("backUrl", backUrl);
			
			HashMap<String, Object> payToBankCardInfo = new HashMap<String, Object>();
			payToBankCardInfo.put("bankCardNo", bankCardNo);
			payToBankCardInfo.put("amount", amount);
			payToBankCardInfo.put("backUrl", backUrl);
			payToBankCardInfo.put("withdrawType", "T0");//T0：T+0到账;T1：T+1到账;T1customized：T+1到账，仅工作日代付;D0customized：D+0到账，根据平台资金头寸付款;默认为T0
			
			payToBankCardInfo.put("unionBank", "03080000"); // CMB
			payToBankCardInfo.put("province", "上海");
			payToBankCardInfo.put("city", "浦东新区");
			payToBankCardInfo.put("bankCardPro", 0L); //0：个人银行卡;1：企业对公账户;如果不传默认为0
			
			//request.put("payToBankCardInfo", payToBankCardInfo); 
			
			batchPayMap.put("payToBankCardInfo", payToBankCardInfo);
			batchPayMap.put("amount", amount);
			batchPayMap.put("fee", 0L);
			list.add(batchPayMap);
			
			bizOrderNo = "AUTO-"+System.currentTimeMillis();;
			HashMap<String, Object> batchPayMap2 = new HashMap<String, Object>();
			batchPayMap2.put("bizOrderNo", bizOrderNo);
			batchPayMap2.put("bizUserId", bizUserId); 
			batchPayMap2.put("accountSetNo", accountSetNo);
			batchPayMap2.put("backUrl", backUrl);
			batchPayMap2.put("amount", amount);
			batchPayMap2.put("fee", 0L);
			list.add(batchPayMap2);
			
			request.put("batchPayList", list); 
			request.put("summary", "AUTO-TEST--summary"); 
			request.put("extendInfo", "AUTO-TEST-extendInfo");

			String res = YunClient.request(request);
			JSONObject resp = JSON.parseObject(res);
			
			String status = resp.getString("status");

			if (status != null && status.equals("OK")) {
				bizBatchNo = resp.getJSONObject("signedValue").getString("bizBatchNo");
//				payFailMessage = resp.getJSONObject("signedValue").getString("payFailMessage");
//				orderNo = resp.getJSONObject("signedValue").getString("orderNo");
//				bizOrderNo = resp.getJSONObject("signedValue").getString("bizOrderNo");
//				payWhereabouts = resp.getJSONObject("signedValue").getString("payWhereabouts");
				logger.info("API[batchAgentPaySimplify2BankCard SUCCESS! bizBatchNo=" + bizBatchNo + "]");
			} else {
				logger.error("API_ERROR[batchAgentPaySimplify2BankCard RESULT=[" + resp.getString("errorCode") + ", "
						+ resp.getString("message") + "]]");
			}
		} catch (final Exception e) {
			logger.error("EXCEPTION", e);
			// e.printStackTrace();
		}
		result.put("bizBatchNo", bizBatchNo);
		long end = System.currentTimeMillis();
		long elapse = end - start;
		logger.info("================OrderAPI: batchAgentPaySimplify2BankCard end================[elapse: " + elapse + " ms]");
		return result;
	}
	
	/**
	 * 批量代付-带分账
	 * @param bizUserId
	 * @param accountSetNo
	 * @param backUrl
	 * @param amount
	 * @param bankCardNo
	 * @return
	 */
	public static HashMap<String, String> batchAgentPaySimplifyWithSplitRule(String bizUserId, String accountSetNo,
			String backUrl, Long amount, String bankCardNo) {
		logger.info("================OrderAPI: batchAgentPaySimplifyWithSplitRule begin================");
		long start = System.currentTimeMillis();
		HashMap<String, String> result = new HashMap<String, String>();
		final YunRequest request = new YunRequest("OrderService", "batchAgentPaySimplify");

		HashMap<String, Object> payMethod = new HashMap<>();

		//String payStatus = "";
		//String payFailMessage = "";
		//String orderNo = "";
		String bizOrderNo = "AUTO-"+System.currentTimeMillis();
		//String payWhereabouts = "";
		
		String bizBatchNo = "AUTO-"+System.currentTimeMillis();
		Long goodsType = 3L;  // real goods
		String goodsNo  = "AUTO-goodsNo";
		String tradeCode = "3001";
		try {
			request.put("bizBatchNo", bizBatchNo); //商户批次号
			ArrayList list = new ArrayList();
			HashMap<String, Object> batchPayMap = new HashMap<String, Object>();
			batchPayMap.put("bizOrderNo", bizOrderNo);
			batchPayMap.put("bizUserId", bizUserId); 
			batchPayMap.put("accountSetNo", accountSetNo);
			batchPayMap.put("backUrl", backUrl);
			
			HashMap<String, Object> payToBankCardInfo = new HashMap<String, Object>();
			payToBankCardInfo.put("bankCardNo", bankCardNo);
			payToBankCardInfo.put("amount", amount);
			payToBankCardInfo.put("backUrl", backUrl);
			payToBankCardInfo.put("withdrawType", "T0");//T0：T+0到账;T1：T+1到账;T1customized：T+1到账，仅工作日代付;D0customized：D+0到账，根据平台资金头寸付款;默认为T0
			
			payToBankCardInfo.put("unionBank", "03080000"); // CMB
			payToBankCardInfo.put("province", "上海");
			payToBankCardInfo.put("city", "浦东新区");
			payToBankCardInfo.put("bankCardPro", 0L); //0：个人银行卡;1：企业对公账户;如果不传默认为0
			
			//request.put("payToBankCardInfo", payToBankCardInfo); 
			batchPayMap.put("payToBankCardInfo", payToBankCardInfo);
			batchPayMap.put("amount", amount);
			batchPayMap.put("fee", 0L);
			//设置分账规则，固定：
			ArrayList splitRuleList = new ArrayList();
			HashMap<String, Object> splitRules1 = new HashMap<String, Object>();
			splitRules1.put("bizUserId", bizUserId);//TODO: 可以固定设置一个用户
			splitRules1.put("accountSetNo", accountSetNo);
			splitRules1.put("amount", 5L);
			splitRules1.put("fee", 0L);
			splitRules1.put("remark", "SplitRule AUTO-TEST");
			splitRuleList.add(splitRules1);
			
			HashMap<String, Object> splitRules2 = new HashMap<String, Object>();
			splitRules2.put("bizUserId", bizUserId);//TODO: 可以固定设置一个用户
			splitRules2.put("accountSetNo", accountSetNo);
			splitRules2.put("amount", 2L);
			splitRules2.put("fee", 0L);
			splitRules2.put("remark", "SplitRule AUTO-TEST2");
			splitRuleList.add(splitRules2);
			batchPayMap.put("splitRuleList", splitRuleList);
			
			list.add(batchPayMap);
			
			bizOrderNo = "AUTO-"+System.currentTimeMillis();;
			HashMap<String, Object> batchPayMap2 = new HashMap<String, Object>();
			batchPayMap2.put("bizOrderNo", bizOrderNo);
			batchPayMap2.put("bizUserId", bizUserId); 
			batchPayMap2.put("accountSetNo", accountSetNo);
			batchPayMap2.put("backUrl", backUrl);
			batchPayMap2.put("amount", amount);
			batchPayMap2.put("fee", 0L);
			//设置分账规则，固定：
			ArrayList splitRuleList2 = new ArrayList();
			HashMap<String, Object> splitRules3 = new HashMap<String, Object>();
			splitRules3.put("bizUserId", bizUserId);//TODO: 可以固定设置一个用户
			splitRules3.put("accountSetNo", accountSetNo);
			splitRules3.put("amount", 5L);
			splitRules3.put("fee", 0L);
			splitRules3.put("remark", "SplitRule AUTO-TEST");
			splitRuleList2.add(splitRules3);
			
			HashMap<String, Object> splitRules4 = new HashMap<String, Object>();
			splitRules4.put("bizUserId", bizUserId);//TODO: 可以固定设置一个用户
			splitRules4.put("accountSetNo", accountSetNo);
			splitRules4.put("amount", 2L);
			splitRules4.put("fee", 0L);
			splitRules4.put("remark", "SplitRule AUTO-TEST2");
			splitRuleList2.add(splitRules4);
			batchPayMap2.put("splitRuleList",splitRuleList2);
			list.add(batchPayMap2);
			
			request.put("batchPayList", list); 
			request.put("summary", "AUTO-TEST--summary"); 
			request.put("extendInfo", "AUTO-TEST-extendInfo");

			String res = YunClient.request(request);
			JSONObject resp = JSON.parseObject(res);
			
			String status = resp.getString("status");

			if (status != null && status.equals("OK")) {
				bizBatchNo = resp.getJSONObject("signedValue").getString("bizBatchNo");
//				payFailMessage = resp.getJSONObject("signedValue").getString("payFailMessage");
//				orderNo = resp.getJSONObject("signedValue").getString("orderNo");
//				bizOrderNo = resp.getJSONObject("signedValue").getString("bizOrderNo");
//				payWhereabouts = resp.getJSONObject("signedValue").getString("payWhereabouts");
				logger.info("API[batchAgentPaySimplifyWithSplitRule SUCCESS! bizBatchNo=" + bizBatchNo + "]");
			} else {
				logger.error("API_ERROR[batchAgentPaySimplifyWithSplitRule RESULT=[" + resp.getString("errorCode") + ", "
						+ resp.getString("message") + "]]");
			}
		} catch (final Exception e) {
			logger.error("EXCEPTION", e);
			// e.printStackTrace();
		}
		result.put("bizBatchNo", bizBatchNo);
		long end = System.currentTimeMillis();
		long elapse = end - start;
		logger.info("================OrderAPI: batchAgentPaySimplifyWithSplitRule end================[elapse: " + elapse + " ms]");
		return result;
	}

}
