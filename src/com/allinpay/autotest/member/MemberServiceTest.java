package com.allinpay.autotest.member;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.allinpay.autotest.util.IDGenerator;
import com.allinpay.yunst.sdk.YunClient;
import com.allinpay.yunst.sdk.bean.YunRequest;
import com.allinpay.yunst.sdk.util.RSAUtil;

public class MemberServiceTest {
	public static void main(String[] args) {
		String member = MemberServiceTest.createMember("16019293");
		System.out.println(member);
	}
	private static Logger logger = Logger.getLogger(MemberServiceTest.class.getName());
	
	/**
	 * 创建会员接口范例
	 * @return
	 */
	public static String createMember(String bizUserId) {
		logger.info("================MemberAPI: createMember begin================");
		long start = System.currentTimeMillis();
		final YunRequest request = new YunRequest("MemberService", "createMember");
		if(bizUserId != null && "auto".equalsIgnoreCase(bizUserId))
			bizUserId = IDGenerator.generateGUID();
		
		request.put("bizUserId", bizUserId);
		request.put("memberType", 3);
		request.put("source", 2);
		logger.info("[bizUserId="+bizUserId+", memberType=3"+", source=2]");
		try {
			String res = YunClient.request(request);
			JSONObject resp = JSON.parseObject(res);
//			System.out.println("status=[" + resp.getString("status") + "]");
//			System.out.println("signedValue=[" + resp.getString("signedValue") + "]");
//			System.out.println("sign=[" + resp.getString("sign") + "]");
//			System.out.println("errorCode=[" + resp.getString("errorCode") + "]");
//			System.out.println("message=[" + resp.getString("message") + "]");
			String status = resp.getString("status");
			if(status != null && status.equals("OK")) {
				logger.info("API[createMember SUCCESS!]");
			} else {
				logger.error("API_ERROR[createMember RESULT=["+resp.getString("errorCode")+", "+resp.getString("message")+"]]");
			}
		} catch (final Exception e) {
			logger.error("EXCEPTION", e);
			//e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		long elapse = end - start;
		logger.info("================MemberAPI: createMember end================[elapse: "+ elapse+" ms]");
		return bizUserId;
	}
	
	/**
	 * 绑定手机范例， 验证码需要通过发送短信的接口触发，然后调用绑定手机接口
	 * @param bizUserId
	 * @param phoneNo
	 */
	public static void bindPhone(String bizUserId, String phoneNo) {
		logger.info("================MemberAPI: bindPhone begin================");
		long start = System.currentTimeMillis();
		final YunRequest request = new YunRequest("MemberService", "bindPhone");
		try {
			request.put("bizUserId", bizUserId);
			request.put("phone", phoneNo);
			request.put("verificationCode", "11111");  //TODO:测试环境需要增加判断逻辑
			String res = YunClient.request(request);
			
			JSONObject resp = JSON.parseObject(res);
//			System.out.println("status=[" + resp.getString("status") + "]");
//			System.out.println("signedValue=[" + resp.getString("signedValue") + "]");
//			System.out.println("sign=[" + resp.getString("sign") + "]");
//			System.out.println("errorCode=[" + resp.getString("errorCode") + "]");
//			System.out.println("message=[" + resp.getString("message") + "]");
			
			String status = resp.getString("status");
			if(status != null && status.equals("OK")) {
				logger.info("API[bindPhone SUCCESS!]");
			} else {
				logger.error("API_ERROR[bindPhone RESULT=["+resp.getString("errorCode")+", "+resp.getString("message")+"]]");
			}
			
		} catch (final Exception e) {
			logger.error("EXCEPTION", e);
		}
		long end = System.currentTimeMillis();
		long elapse = end - start;
		logger.info("================MemberAPI: bindPhone end================[elapse: "+ elapse+" ms]");
		
	}
	
	/**
	 * 个人实名
	 * @param bizUserId
	 * @param phoneNo
	 */
	public static void setRealName(String bizUserId, String name, String identityType, String identityNo) {
		logger.info("================MemberAPI: setRealName begin================");
		long start = System.currentTimeMillis();
		final YunRequest request = new YunRequest("MemberService", "setRealName");
		try {
			request.put("bizUserId", bizUserId);
			request.put("isAuth", "true");
			request.put("name", name);
			request.put("identityType", identityType);
			request.put("identityNo", RSAUtil.encrypt(identityNo));
			String res = YunClient.request(request);
			
			JSONObject resp = JSON.parseObject(res);
//			System.out.println("status=[" + resp.getString("status") + "]");
//			System.out.println("signedValue=[" + resp.getString("signedValue") + "]");
//			System.out.println("sign=[" + resp.getString("sign") + "]");
//			System.out.println("errorCode=[" + resp.getString("errorCode") + "]");
//			System.out.println("message=[" + resp.getString("message") + "]");
			
			String status = resp.getString("status");
			if(status != null && status.equals("OK")) {
				logger.info("API[setRealName SUCCESS!]");
			} else {
				logger.error("API_ERROR[setRealName RESULT=["+resp.getString("errorCode")+", "+resp.getString("message")+"]]");
			}
			
		} catch (final Exception e) {
			logger.error("EXCEPTION", e);
		}
		long end = System.currentTimeMillis();
		long elapse = end - start;
		logger.info("================MemberAPI: setRealName end================[elapse: "+ elapse+" ms]");	
	}
	
	
	public static void getMemberInfo(String bizUserId) {
		logger.info("================MemberAPI: getMemberInfo begin================");
		long start = System.currentTimeMillis();
		final YunRequest request = new YunRequest("MemberService", "getMemberInfo");
		try {
			request.put("bizUserId", bizUserId);
			String res = YunClient.request(request);
			
			JSONObject resp = JSON.parseObject(res);
//			System.out.println("status=[" + resp.getString("status") + "]");
//			System.out.println("signedValue=[" + resp.getString("signedValue") + "]");
//			System.out.println("sign=[" + resp.getString("sign") + "]");
//			System.out.println("errorCode=[" + resp.getString("errorCode") + "]");
//			System.out.println("message=[" + resp.getString("message") + "]");
			
			String status = resp.getString("status");
			if(status != null && status.equals("OK")) {
				logger.info("API[getMemberInfo SUCCESS!]");
			} else {
				logger.error("API_ERROR[getMemberInfo RESULT=["+resp.getString("errorCode")+", "+resp.getString("message")+"]]");
			}
			
		} catch (final Exception e) {
			logger.error("EXCEPTION", e);
		}
		long end = System.currentTimeMillis();
		long elapse = end - start;
		logger.info("================MemberAPI: getMemberInfo end================[elapse: "+ elapse+" ms]");	
	}
	
	/**
	 * 申请绑定银行卡
	 * @param bizUserId
	 */
	public static HashMap<String, String> applyBindBankCard(String bizUserId, long cardCheck, String cardPhone, 
			String cardNo, String cardName, long identityType, String identityNo, String validate, String cvv2, boolean isSafeCard, String bindCardUnionBank) {
		logger.info("================MemberAPI: applyBindBankCard begin================");
		long start = System.currentTimeMillis();
		HashMap<String, String> result = new HashMap<String, String>();
		final YunRequest request = new YunRequest("MemberService", "applyBindBankCard");
		try {
			request.put("bizUserId", bizUserId);
			//request.put("phone", "13616515002");	// 这个手机号 13616515002 是绑卡银行预留手机，实名支付用
			request.put("phone", cardPhone);
			request.put("cardCheck", cardCheck);	// 1L 三要素，2L 四要素
			// 四要素
			
				//request.put("cardNo", RSAUtil.encrypt("6228480402637874221"));
				request.put("cardNo", RSAUtil.encrypt(cardNo));
				request.put("name", cardName);
				//request.put("identityNo", RSAUtil.encrypt("130124198908210051"));
				request.put("identityNo", RSAUtil.encrypt(identityNo));
			
				// 三要素
				//request.put("cardNo", RSAUtil.encrypt("6228480402637874214"));
				//request.put("name", "王小二");
				//request.put("identityNo", RSAUtil.encrypt("610422198600000000"));
			
			request.put("identityType", identityType);			
			//request.put("validate", RSAUtil.encrypt("2012"));
			request.put("validate", RSAUtil.encrypt(validate));
			request.put("cvv2", cvv2);
			request.put("isSafeCard", isSafeCard);
			request.put("unionBank", bindCardUnionBank);
			
			String res = YunClient.request(request);
			//System.out.println("res: " + res);
			
			JSONObject resp = JSON.parseObject(res);
			
//			System.out.println("status=[" + resp.getString("status") + "]");
//			System.out.println("signedValue=[" + resp.getString("signedValue") + "]");
//			System.out.println("sign=[" + resp.getString("sign") + "]");
//			System.out.println("errorCode=[" + resp.getString("errorCode") + "]");
//			System.out.println("message=[" + resp.getString("message") + "]");
			String tranceNum = "";
			String transDate = "";
//			String bankName = "";
//			String bankCode = "";
//			String cardType = "";
			String status = resp.getString("status");
			if(status != null && status.equals("OK")) {
				logger.info("API[applyBindBankCard SUCCESS!]");
				tranceNum = resp.getJSONObject("signedValue").getString("tranceNum");
				transDate = resp.getJSONObject("signedValue").getString("transDate");
//				bankName = resp.getJSONObject("signedValue").getString("bankName");
//				bankCode = resp.getJSONObject("signedValue").getString("bankCode");
//				cardType = resp.getJSONObject("signedValue").getString("cardType");
				result.put("bizUserId", bizUserId);
				result.put("tranceNum", tranceNum);
				result.put("transDate", transDate);
				result.put("cardPhone", cardPhone);
			} else {
				logger.error("API_ERROR[applyBindBankCard RESULT=["+resp.getString("errorCode")+", "+resp.getString("message")+"]]");
			}
			
		} catch (final Exception e) {
			logger.error("EXCEPTION", e);
		}
		
		long end = System.currentTimeMillis();
		long elapse = end - start;
		logger.info("================MemberAPI: applyBindBankCard end================[elapse: "+ elapse+" ms]");	
		return result;
	}
	
	
	public static void bindBankCard(String bizUserId, String tranceNum, String transDate, 
			String phone, String verificationCode) {
		logger.info("================MemberAPI: bindBankCard begin================");
		long start = System.currentTimeMillis();
		//HashMap<String, String> result = new HashMap<String, String>();
		final YunRequest request = new YunRequest("MemberService", "bindBankCard");
		try {
			request.put("bizUserId", bizUserId);
			request.put("tranceNum", tranceNum);	
			request.put("transDate", transDate);
			request.put("phone", phone);	
			request.put("verificationCode", verificationCode); //TODO smscode
			
			String res = YunClient.request(request);
			
			JSONObject resp = JSON.parseObject(res);
//			System.out.println("status=[" + resp.getString("status") + "]");
//			System.out.println("signedValue=[" + resp.getString("signedValue") + "]");
//			System.out.println("sign=[" + resp.getString("sign") + "]");
//			System.out.println("errorCode=[" + resp.getString("errorCode") + "]");
//			System.out.println("message=[" + resp.getString("message") + "]");
			
			String status = resp.getString("status");
			if(status != null && status.equals("OK")) {
				logger.info("API[bindBankCard SUCCESS!]");
				
			} else {
				logger.error("API_ERROR[bindBankCard RESULT=["+resp.getString("errorCode")+", "+resp.getString("message")+"]]");
			}
			
		} catch (final Exception e) {
			logger.error("EXCEPTION", e);
		}
		
		long end = System.currentTimeMillis();
		long elapse = end - start;
		logger.info("================MemberAPI: bindBankCard end================[elapse: "+ elapse+" ms]");	
		//return result;
		
	}
	
	
	public static void setSafeCard(String bizUserId, String cardNo, boolean safeCard) {
		logger.info("================MemberAPI: setSafeCard begin================");
		long start = System.currentTimeMillis();
		//HashMap<String, String> result = new HashMap<String, String>();
		final YunRequest request = new YunRequest("MemberService", "setSafeCard");
		try {
			request.put("bizUserId", bizUserId);
			request.put("cardNo", RSAUtil.encrypt(cardNo));
			request.put("setSafeCard", safeCard);
			String res = YunClient.request(request);
			
			JSONObject resp = JSON.parseObject(res);
//			System.out.println("status=[" + resp.getString("status") + "]");
//			System.out.println("signedValue=[" + resp.getString("signedValue") + "]");
//			System.out.println("sign=[" + resp.getString("sign") + "]");
//			System.out.println("errorCode=[" + resp.getString("errorCode") + "]");
//			System.out.println("message=[" + resp.getString("message") + "]");
			
			String status = resp.getString("status");
			if(status != null && status.equals("OK")) {
				logger.info("API[setSafeCard SUCCESS!]");
				
			} else {
				logger.error("API_ERROR[setSafeCard RESULT=["+resp.getString("errorCode")+", "+resp.getString("message")+"]]");
			}
			
		} catch (final Exception e) {
			logger.error("EXCEPTION", e);
		}
		
		long end = System.currentTimeMillis();
		long elapse = end - start;
		logger.info("================MemberAPI: setSafeCard end================[elapse: "+ elapse+" ms]");	
		//return result;
	}
	
	
	public static void queryBankCard(String bizUserId, String cardNo) {
		logger.info("================MemberAPI: queryBankCard begin================");
		long start = System.currentTimeMillis();
		//HashMap<String, String> result = new HashMap<String, String>();
		final YunRequest request = new YunRequest("MemberService", "queryBankCard");
		try {
			request.put("bizUserId", bizUserId);
			request.put("cardNo", RSAUtil.encrypt(cardNo));
			
			String res = YunClient.request(request);
			
			JSONObject resp = JSON.parseObject(res);
//			System.out.println("status=[" + resp.getString("status") + "]");
//			System.out.println("signedValue=[" + resp.getString("signedValue") + "]");
//			System.out.println("sign=[" + resp.getString("sign") + "]");
//			System.out.println("errorCode=[" + resp.getString("errorCode") + "]");
//			System.out.println("message=[" + resp.getString("message") + "]");
			
			String status = resp.getString("status");
			if(status != null && status.equals("OK")) {
				logger.info("API[queryBankCard SUCCESS!]");
				
			} else {
				logger.error("API_ERROR[queryBankCard RESULT=["+resp.getString("errorCode")+", "+resp.getString("message")+"]]");
			}
			
		} catch (final Exception e) {
			logger.error("EXCEPTION", e);
		}
		
		long end = System.currentTimeMillis();
		long elapse = end - start;
		logger.info("================MemberAPI: queryBankCard end================[elapse: "+ elapse+" ms]");	
		//return result;
		
	}
	
	
	public static void unbindBankCard(String bizUserId, String cardNo) {
		logger.info("================MemberAPI: unbindBankCard begin================");
		long start = System.currentTimeMillis();
		//HashMap<String, String> result = new HashMap<String, String>();
		final YunRequest request = new YunRequest("MemberService", "unbindBankCard");
		try {
			request.put("bizUserId", bizUserId);
			request.put("cardNo", RSAUtil.encrypt(cardNo));
			
			String res = YunClient.request(request);
			
			JSONObject resp = JSON.parseObject(res);
//			System.out.println("status=[" + resp.getString("status") + "]");
//			System.out.println("signedValue=[" + resp.getString("signedValue") + "]");
//			System.out.println("sign=[" + resp.getString("sign") + "]");
//			System.out.println("errorCode=[" + resp.getString("errorCode") + "]");
//			System.out.println("message=[" + resp.getString("message") + "]");
			
			String status = resp.getString("status");
			if(status != null && status.equals("OK")) {
				logger.info("API[unbindBankCard SUCCESS!]");
				
			} else {
				logger.error("API_ERROR[unbindBankCard RESULT=["+resp.getString("errorCode")+", "+resp.getString("message")+"]]");
			}
			
		} catch (final Exception e) {
			logger.error("EXCEPTION", e);
		}
		
		long end = System.currentTimeMillis();
		long elapse = end - start;
		logger.info("================MemberAPI: unbindBankCard end================[elapse: "+ elapse+" ms]");	
		//return result;
		
	}

	
	public static void lockMember(String bizUserId) {
		logger.info("================MemberAPI: lockMember begin================");
		long start = System.currentTimeMillis();
		//HashMap<String, String> result = new HashMap<String, String>();
		final YunRequest request = new YunRequest("MemberService", "lockMember");
		try {
			request.put("bizUserId", bizUserId);
			
			String res = YunClient.request(request);
			
			JSONObject resp = JSON.parseObject(res);
//			System.out.println("status=[" + resp.getString("status") + "]");
//			System.out.println("signedValue=[" + resp.getString("signedValue") + "]");
//			System.out.println("sign=[" + resp.getString("sign") + "]");
//			System.out.println("errorCode=[" + resp.getString("errorCode") + "]");
//			System.out.println("message=[" + resp.getString("message") + "]");
			
			String status = resp.getString("status");
			if(status != null && status.equals("OK")) {
				logger.info("API[lockMember SUCCESS!]");
				
			} else {
				logger.error("API_ERROR[lockMember RESULT=["+resp.getString("errorCode")+", "+resp.getString("message")+"]]");
			}
			
		} catch (final Exception e) {
			logger.error("EXCEPTION", e);
		}
		
		long end = System.currentTimeMillis();
		long elapse = end - start;
		logger.info("================MemberAPI: lockMember end================[elapse: "+ elapse+" ms]");	
		//return result;
		
	}
	
	public static void unlockMember(String bizUserId) {
		logger.info("================MemberAPI: unlockMember begin================");
		long start = System.currentTimeMillis();
		//HashMap<String, String> result = new HashMap<String, String>();
		final YunRequest request = new YunRequest("MemberService", "unlockMember");
		try {
			request.put("bizUserId", bizUserId);
			
			String res = YunClient.request(request);
			
			JSONObject resp = JSON.parseObject(res);
//			System.out.println("status=[" + resp.getString("status") + "]");
//			System.out.println("signedValue=[" + resp.getString("signedValue") + "]");
//			System.out.println("sign=[" + resp.getString("sign") + "]");
//			System.out.println("errorCode=[" + resp.getString("errorCode") + "]");
//			System.out.println("message=[" + resp.getString("message") + "]");
			
			String status = resp.getString("status");
			if(status != null && status.equals("OK")) {
				logger.info("API[unlockMember SUCCESS!]");
				
			} else {
				logger.error("API_ERROR[unlockMember RESULT=["+resp.getString("errorCode")+", "+resp.getString("message")+"]]");
			}
			
		} catch (final Exception e) {
			logger.error("EXCEPTION", e);
		}
		
		long end = System.currentTimeMillis();
		long elapse = end - start;
		logger.info("================MemberAPI: unlockMember end================[elapse: "+ elapse+" ms]");	
		//return result;
	}
	
	public static void vspTermidService(String bizUserId, String operationType, String vspMerchantid, String appid, String vspTermid) {
		logger.info("================MemberAPI: vspTermidService begin================");
		long start = System.currentTimeMillis();
		//HashMap<String, String> result = new HashMap<String, String>();
		final YunRequest request = new YunRequest("MemberService", "vspTermidService");
		try {
			request.put("bizUserId", bizUserId);
			request.put("operationType", operationType);
			request.put("vspMerchantid", vspMerchantid);
			request.put("appid", appid);
			request.put("vspTermid", vspTermid);
			String res = YunClient.request(request);
			
			JSONObject resp = JSON.parseObject(res);
//			System.out.println("status=[" + resp.getString("status") + "]");
//			System.out.println("signedValue=[" + resp.getString("signedValue") + "]");
//			System.out.println("sign=[" + resp.getString("sign") + "]");
//			System.out.println("errorCode=[" + resp.getString("errorCode") + "]");
//			System.out.println("message=[" + resp.getString("message") + "]");
			
			String status = resp.getString("status");
			if(status != null && status.equals("OK")) {
				logger.info("API[vspTermidService SUCCESS!]");
				
			} else {
				logger.error("API_ERROR[vspTermidService RESULT=["+resp.getString("errorCode")+", "+resp.getString("message")+"]]");
			}
			
		} catch (final Exception e) {
			logger.error("EXCEPTION", e);
		}
		
		long end = System.currentTimeMillis();
		long elapse = end - start;
		logger.info("================MemberAPI: vspTermidService end================[elapse: "+ elapse+" ms]");	
		//return result;
		
	}
	
	public static void setPayPwd() {
		
		// 宁波商户接入环境
		//String webParamUrl = "http://122.227.225.142:23661/pwd/setPayPwd.html?";
		
		// 上海商户接入环境
		String webParamUrl = "http://192.168.14.70:8080/pwd/setPayPwd.html?";
		
		final YunRequest request = new YunRequest("MemberPwdService", "setPayPwd");
		
		try {
			request.put("bizUserId", "2018072001");
			request.put("phone", "13800138002");
			request.put("name", "梅西");
			request.put("identityType", 1L);
			request.put("identityNo", RSAUtil.encrypt("130124198908210051"));
			request.put("jumpUrl", "http://122.227.225.142:23663/testFront.jsp");
			request.put("backUrl", "http://122.227.225.142:23663/testFront.jsp");
			
			String res = YunClient.encodeTwice(request);
			webParamUrl += res;
			System.out.println("webParamUrl: " + webParamUrl);
			
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
