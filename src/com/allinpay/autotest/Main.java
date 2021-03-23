package com.allinpay.autotest;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import com.allinpay.autotest.member.MemberServiceTest;
import com.allinpay.autotest.order.OrderServiceTest;
import com.allinpay.autotest.util.PropertiesUtil;

public class Main {
	
	public static void testSuit(String inputDataPath) {
		
		InputStream input = PropertiesUtil.class.getResourceAsStream("/input/"+inputDataPath);
		Properties inputProps = PropertiesUtil.getSpecificProperties(input, true);
		
		//s1: run member api and output execute result
		//1:createMember
		String bizUserId = MemberServiceTest.createMember(inputProps.getProperty("bizUserId"));
		
		//2:bindPhone
		MemberServiceTest.bindPhone(bizUserId, inputProps.getProperty("phoneNo"));
		
		//3:realName
		MemberServiceTest.setRealName(bizUserId, inputProps.getProperty("name"), inputProps.getProperty("identityType"), inputProps.getProperty("identityNo"));
		
		//4:getMemberInfo
		MemberServiceTest.getMemberInfo(bizUserId);
		
		//5:applyBindBankCard
		long bindCardCheck = new Long(inputProps.getProperty("bindCardCheck")).longValue();
		String bindCardPhone = inputProps.getProperty("bindCardPhone");
		String bindCardNo = inputProps.getProperty("bindCardNo");
		String bindCardName = inputProps.getProperty("bindCardName");
		long bindCardIdentityType = new Long(inputProps.getProperty("bindCardIdentityType")).longValue();
		String bindCardIdentityNo = inputProps.getProperty("bindCardIdentityNo");
		String bindCardValidate = inputProps.getProperty("bindCardValidate");
		String bindCardCvv2 = inputProps.getProperty("bindCardCvv2");
		boolean bindCardisSafeCard = new Boolean(inputProps.getProperty("bindCardisSafeCard")).booleanValue();
		String bindCardUnionBank = inputProps.getProperty("bindCardUnionBank");
		HashMap<String, String> bindBankCardMap = MemberServiceTest.applyBindBankCard(bizUserId, bindCardCheck, bindCardPhone, bindCardNo, bindCardName, 
				bindCardIdentityType, bindCardIdentityNo, bindCardValidate, bindCardCvv2, bindCardisSafeCard,bindCardUnionBank);
		if(bindBankCardMap.get("bizUserId") != null) {
		
			//6:bindBankCard
			String verificationCode = "111111";  //TODO smscode
			MemberServiceTest.bindBankCard(bindBankCardMap.get("bizUserId"), bindBankCardMap.get("tranceNum"), bindBankCardMap.get("transDate"), bindBankCardMap.get("cardPhone"), verificationCode);
			
		}
		
		//7:setSafeCard
		MemberServiceTest.setSafeCard(bizUserId, bindCardNo, true);
		
		//8:queryBindBankCards
		MemberServiceTest.queryBankCard(bizUserId, bindCardNo);
		MemberServiceTest.queryBankCard(bizUserId, ""); //it will return all of this userid's cardlist
		
		//9:unbindBankCard
		MemberServiceTest.unbindBankCard(bizUserId, bindCardNo);
		
		//10:lockMember
		MemberServiceTest.lockMember(bizUserId);
		
		//11:unlockMember
		MemberServiceTest.unlockMember(bizUserId);
		
		//12:vspTermidService
		String vspTermidService_operationType = inputProps.getProperty("vspTermidService_operationType");
		String vspTermidService_vspMerchantid = inputProps.getProperty("vspTermidService_vspMerchantid");
		String vspTermidService_appid = inputProps.getProperty("vspTermidService_appid");
		String vspTermidService_vspTermid = inputProps.getProperty("vspTermidService_vspTermid");
		MemberServiceTest.vspTermidService(bizUserId, vspTermidService_operationType, vspTermidService_vspMerchantid, vspTermidService_appid, vspTermidService_vspTermid);
		MemberServiceTest.vspTermidService(bizUserId, "query", vspTermidService_vspMerchantid, vspTermidService_appid, vspTermidService_vspTermid);
		
		//13:setPayPwd
		
		
		//s2: run order api
		//1:deposite
		//payMethod=GATEWAY
		String bankCardNo = inputProps.getProperty("depositApply_cardNo");
		String frontUrl = inputProps.getProperty("depositApply_frontUrl");
		String backUrl = inputProps.getProperty("depositApply_backUrl");
		String wx_openid = inputProps.getProperty("depositApply_wx_openid");
		String ali_user_id = inputProps.getProperty("depositApply_ali_user_id");
		String vspCusid = inputProps.getProperty("depositApply_vspCusid");
		String subAppId = inputProps.getProperty("depositApply_subAppId");
		String subMchId = inputProps.getProperty("depositApply_subMchId");
		String authCode = inputProps.getProperty("depositApply_authCode");
		//云商通分配的托管专用账户集合的编号
		String accountSetNo = inputProps.getProperty("depositApply_accountSetNo");
		
		
		OrderServiceTest.depositApply(bizUserId, "GATEWAY", bankCardNo, "", "", "", "", "", "", "", "", frontUrl, backUrl, 2L);
		//payMethod=WECHATPAY_APP
		OrderServiceTest.depositApply(bizUserId, "WECHATPAY_APP", bankCardNo, "", wx_openid, ali_user_id, vspCusid, subAppId, subMchId, authCode, accountSetNo, frontUrl, backUrl, 2L);
		
		//payMethod=WECHAT_PUBLIC
		OrderServiceTest.depositApply(bizUserId, "WECHAT_PUBLIC", bankCardNo, "", wx_openid, ali_user_id, vspCusid, subAppId, subMchId, authCode, accountSetNo, frontUrl, backUrl, 2L);
		
		//2:withdraw
		String banCardNo = inputProps.getProperty("withdrawApply_bankCardNo");
		String bankCardPro = inputProps.getProperty("withdrawApply_bankCardPro");
		String unionBank = inputProps.getProperty("withdrawApply_unionBank");
		String bankName =  inputProps.getProperty("withdrawApply_bankName");
		String province = inputProps.getProperty("withdrawApply_province");
		String city = inputProps.getProperty("withdrawApply_city");
		String withdrawType = inputProps.getProperty("withdrawApply_withdrawType");
		//payMethod=WITHDRAW_TLT
		OrderServiceTest.withdrawApply(bizUserId, accountSetNo, 1L, "WITHDRAW_TLT", bankCardNo, new Long(bankCardPro), unionBank, bankName, province, city, withdrawType, backUrl);
		//payMethod=VIRTUAL_OUT
		OrderServiceTest.withdrawApply(bizUserId, accountSetNo, 1L, "VIRTUAL_OUT", bankCardNo, new Long(bankCardPro), unionBank, bankName, province, city, withdrawType, backUrl);
		
		//3:consumeApply
		String payerId =  inputProps.getProperty("consumeApply_payerId");
		String recieverId = inputProps.getProperty("consumeApply_recieverId");
		String validateType = inputProps.getProperty("consumeApply_validateType");
		HashMap consumeApplyMap = OrderServiceTest.consumeApply(payerId, recieverId, 2L, new Long(validateType), frontUrl, backUrl, "POSPAY", bankCardNo, "", wx_openid, ali_user_id, vspCusid, subAppId, subMchId, authCode, new Long(bankCardPro), unionBank, bankName, accountSetNo, province, city, withdrawType);
		String verificationCode = "";
		OrderServiceTest.backPay(bizUserId, (String)consumeApplyMap.get("bizOrderNo"), (String)consumeApplyMap.get("tradeNo"), verificationCode, "192.168.14.61");
		
		//s3: run other api
		
		
				
		//s4: 
	}
	
	public static void testSuit2(String inputDataPath) {
		
		InputStream input = PropertiesUtil.class.getResourceAsStream("/input/"+inputDataPath);
		Properties inputProps = PropertiesUtil.getSpecificProperties(input, true);
		
		//s1: run member api and output execute result
		//1:createMember
		String bizUserId = MemberServiceTest.createMember(inputProps.getProperty("bizUserId"));
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		InputStream conf = PropertiesUtil.class.getResourceAsStream("/conf.properties");
		Properties confProps = PropertiesUtil.getSpecificProperties(conf);
		String inputPath = confProps.getProperty("inputPath");
		File file = new File(inputPath);
		
		if(file.isDirectory()) {
			File[] inputs = file.listFiles();
			for(int i=0; i<inputs.length; i++) {
				File inputData = inputs[i];
				String fileName = inputData.getName();
				System.out.println("suit file name is "+fileName);
				
				//preExecute(); 初始化数据；
				testSuit(fileName);
				
				
			}
		}
		
		
		
		
		
		
		
		
		

	}

}
