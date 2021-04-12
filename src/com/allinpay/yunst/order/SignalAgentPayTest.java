package com.allinpay.yunst.order;

import java.util.HashMap;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.allinpay.yunst.sdk.YunClient;
import com.allinpay.yunst.sdk.bean.YunRequest;

public class SignalAgentPayTest {

	@Test
	public void testMethod() {
		
		final YunRequest request = new YunRequest("OrderService", "signalAgentPay");
		
		try {
			
			// 源托管代收订单付款信息
			JSONArray collectPayList = new JSONArray();
			HashMap<String, Object> collect1 = new HashMap<>();
			collect1.put("bizOrderNo", "1617341092806ds");
			collect1.put("amount", 4L);
			collectPayList.add(new JSONObject(collect1));

			//JSONArray singleRuleList = new JSONArray();
			//HashMap<String, Object> pay1 = new HashMap<>();

//			pay1.put("bizUserId", "1601929311");
//			pay1.put("remark", "消费订单二级分账余");
//			pay1.put("fee", 0L);
//			pay1.put("amount", 2L);
		//	singleRuleList.add(new JSONObject(pay1));
			request.put("bizOrderNo", System.currentTimeMillis() + "df");
			request.put("collectPayList", collectPayList);;
			request.put("bizUserId", "26019293111");//26019293111  1601929311
			request.put("accountSetNo", "200126");
			request.put("backUrl", "http://172.16.190.46:8080/yuncallback/mock/notify?");
			request.put("amount", 4L);
			request.put("fee", 0L);
			//request.put("splitRuleList", singleRuleList);
			request.put("goodsType", 5L);
			request.put("bizGoodsNo", "WHYGOODS002");
			request.put("tradeCode", "4001");
			request.put("summary", "signalAgentPay");
			request.put("extendInfo", "this is extendInfo");
			
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
