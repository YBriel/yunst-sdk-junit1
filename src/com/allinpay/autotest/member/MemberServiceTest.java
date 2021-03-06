package com.allinpay.autotest.member;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
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

	//	System.setProperty("http.proxyHost", "127.0.0.1");
		//System.setProperty("http.proxyPort", "8888");
		//创建会员
	//	String member = MemberServiceTest.createMember("16019293");
//		System.out.println(member);

		//绑定手机
		MemberServiceTest.bindPhone("16019293","13576911130");

		//实名认证
		//MemberServiceTest.setRealName("16019293","余振清","1","360101199604066014");

		//获取用户信息
		//MemberServiceTest.getMemberInfo("16019293");




		//String member = MemberServiceTest.createMember("26019293111");
		String member = MemberServiceTest.createMember("26019293111");
	//MemberServiceTest.bindPhone("26019293111","15070032716");
		//MemberServiceTest.setRealName("26019293111","涂莉莉","1","360124199106145769");

//	MemberServiceTest.idCardCollect("26019293111",9L,"files\\faRenBack.png");
//		String member = MemberServiceTest.createMember("10123456899");
//		System.out.println(member);
//		MemberServiceTest.applyBindBankCard("1601929311",7L,"13576911130","6217002020037294048",
//				"余振清",1L,"360101199604066014","0421","",false,"105421005247");

		//确认绑卡
		MemberServiceTest.bindBankCard("1601929311","860907547252","20210329","13576911130","091119");

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
		request.put("memberType", 3);//企业会员 2  个人会员3
		request.put("source", 2);//Mobile 1 整型    PC 2 整型

		logger.info("[bizUserId="+bizUserId+", memberType=3"+", source=2]");
		try {
			String res = YunClient.request(request);
			JSONObject resp = JSON.parseObject(res);
			logger.info("创建会员返回{}"+resp.toJSONString());
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
			logger.info("绑定手机返回"+resp.toJSONString());
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

	/**
	 *
	 * @param bizUserId 用户id
	 * @param identityType 证件类型
	 * 1-统一社会信用证(三证合一)/营业执照（三证）（一证或三证必传）
	 * 2-组织机构代码证（三证时必传）
	 * 3-税务登记证（三证时必传）
	 * 4-银行开户证明（非必传，上传《银行
	 * 开户许可证》或《基本存款账户信息》
	 * 等可证明平台银行账号和户名的文件）
	 * 5-机构信用代码（非必传）
	 * 6-ICP 备案许可（非必传）
	 * 7-行业许可证（非必传）
	 * 8-身份证正面（人像面）（必传）
	 * 9-身份证反面（国徽面）（必传）
	 */
	public static void idCardCollect(String bizUserId, Long identityType,String fileLocation) {
		logger.info("================MemberAPI: idCardCollect begin================");
		long start = System.currentTimeMillis();
		final YunRequest request = new YunRequest("MemberService", "idcardCollect");
		try {
			String picture = JPGToBase64(fileLocation);
			request.put("bizUserId", bizUserId);
			request.put("picType", identityType);
			request.put("picture", picture);
			String res = YunClient.request(request);
			JSONObject resp = JSON.parseObject(res);
			logger.info("个人实名返回"+resp.toJSONString());
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
			logger.info("获取用户信息返回"+resp.toJSONString());
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
			logger.info("申请绑定银行卡返回"+resp.toJSONString());
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
			//request.put("validate", RSAUtil.encrypt("0329"));
			request.put("verificationCode", verificationCode); //TODO smscode
			String res = YunClient.request(request);
			JSONObject resp = JSON.parseObject(res);
			logger.info("绑定银行卡返回信息"+resp.toJSONString());
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

	// 字符串有最大长度限制65534字节
	public static String JPGToBase64(String imgPath) {
		byte[] data = null;

		try {
			InputStream in = new FileInputStream(imgPath);
			data = new byte[in.available()];
			in.read(data);
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return Base64.getEncoder().encodeToString(data);
	}
}
