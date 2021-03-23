package com.allinpay.yunst.order;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.allinpay.yunst.sdk.YunClient;
import com.allinpay.yunst.sdk.bean.YunRequest;

public class PlatformPulloutTest {

	@Test
	public void testMethod() {
		
		final YunRequest request = new YunRequest("MerchantService", "platformPullout");
		
		try {
			request.put("bizOrderNo", System.currentTimeMillis() + "cz");
			request.put("backUrl", "http://192.168.14.165:8080/yundemo/servletUI/notice");
			request.put("source", "1");
			request.put("amount", 60000L);
			
			String res = YunClient.request(request);
			System.out.println("res: " + res);
			
			JSONObject resp = JSON.parseObject(res);
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
