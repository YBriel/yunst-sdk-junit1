package com.allinpay.yunst.member;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.allinpay.yunst.sdk.YunClient;
import com.allinpay.yunst.sdk.bean.YunRequest;
import com.allinpay.yunst.sdk.util.RSAUtil;

public class SetCompanyInfoTest {

	@Test
	public void testMethod() {
		
		final YunRequest request = new YunRequest("MemberService", "setCompanyInfo");
		
		try {
			request.put("bizUserId", "26019293111");
			request.put("backUrl", "http://www.baidu.com");
			request.put("isAuth", false);
			// 组companyBasicInfo企业信息jsonobject对象
			JSONObject companyBasicInfo = new JSONObject();
			companyBasicInfo.put("companyName", "江西土驴国际旅行社有限公司");
			companyBasicInfo.put("companyAddress", "江西省南昌市");
			companyBasicInfo.put("authType", 1L);//1-三证，2-一证
			companyBasicInfo.put("uniCredit", "91360125MA35WTCN5E");
			companyBasicInfo.put("businessLicense", "91360125MA35WTCN5E");
			companyBasicInfo.put("organizationCode", "91360125MA35WTCN5E");
			companyBasicInfo.put("taxRegister", "91360125MA35WTCN5E");
			companyBasicInfo.put("expLicense", "9999-12-31");
			companyBasicInfo.put("telephone", "15070032716");
			companyBasicInfo.put("legalName", "涂莉莉");
			companyBasicInfo.put("identityType", 1L);
			companyBasicInfo.put("legalIds", RSAUtil.encrypt("360124199106145769"));
			companyBasicInfo.put("legalPhone", "15070032716");
			companyBasicInfo.put("accountNo", RSAUtil.encrypt("202245545422"));
			companyBasicInfo.put("parentBankName", "中国银行");
			companyBasicInfo.put("bankCityNo", "360100");
			companyBasicInfo.put("bankName", "中国银行南昌市北湖支行");
			companyBasicInfo.put("unionBank", "104421074690");
			companyBasicInfo.put("province", "江西");
			companyBasicInfo.put("city", "南昌");
			
			request.put("companyBasicInfo", companyBasicInfo);
			
			String res = YunClient.request(request);
			System.out.println("res: " + res);
			
			JSONObject resp = JSON.parseObject(res);
			System.out.println("返回信息为"+resp);
			System.out.println("status=[" + resp.getString("status") + "]");
			System.out.println("signedValue=[" + resp.getString("signedValue") + "]");
			System.out.println("sign=[" + resp.getString("sign") + "]");
			System.out.println("errorCode=[" + resp.getString("errorCode") + "]");
			System.out.println("message=[" + resp.getString("message") + "]");
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
