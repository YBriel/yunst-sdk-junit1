package com.allinpay.yunst.member;

import org.junit.Test;

import com.allinpay.yunst.sdk.YunClient;
import com.allinpay.yunst.sdk.bean.YunRequest;

public class SignContractTest {

	@Test
	public void testMethod() {
		
		//String webParamUrl = "http://122.227.225.142:23661/yungateway/member/signContract.html?";
		String webParamUrl = "http://116.228.64.55:6900/yungateway/member/signContract.html?";
		final YunRequest request = new YunRequest("MemberService", "signContract");
		
		try {
			request.put("bizUserId", "1601929311");
			request.put("memberType", "3");
			request.put("jumpUrl", "http://www.baidu.com");
			request.put("backUrl", "http://192.168.14.165:8080/yundemo/servletUI/notice");
			request.put("source", "2");
			
			String res = YunClient.encodeOnce(request);
			webParamUrl += res;
			System.out.println("webParamUrl: " + webParamUrl);
			
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}


	@Test
	public void testMethodQuery() {

		//String webParamUrl = "http://122.227.225.142:23661/yungateway/member/signContract.html?";
		String webParamUrl = "http://116.228.64.55:6900/yungateway/member/signContractQuery.html?";
		final YunRequest request = new YunRequest("MemberService", "signContractQuery");

		try {
			request.put("bizUserId", "1601929311");
		//	request.put("memberType", "3");
		//	request.put("jumpUrl", "http://www.baidu.com");
			//request.put("backUrl", "http://192.168.14.165:8080/yundemo/servletUI/notice");
			request.put("source", "2");

			String res = YunClient.encodeOnce(request);
			webParamUrl += res;
			System.out.println("webParamUrl: " + webParamUrl);

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

}
