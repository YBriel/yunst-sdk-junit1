package com.allinpay.yunst.order;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.allinpay.yunst.sdk.YunClient;
import com.allinpay.yunst.sdk.bean.YunRequest;

public class RefundTest {

	@Test
	public void testMethod() {

		final YunRequest request = new YunRequest("OrderService", "refund");

		try {
			request.put("bizUserId", "WHYGR2019001");
			request.put("bizOrderNo", System.currentTimeMillis() + "whydsrefund");
			request.put("oriBizOrderNo", "1616993623461whyxf");
			request.put("amount", 1L);
			request.put("couponAmount", 0L);
			request.put("feeAmount", 0L);
			request.put("refundType", "D0"); //默认 D1 D1：D+1 日 14 点 30 分批量向渠道发起说明（1）此参数仅对支持退款金额原路返 回的支付订单有效（接口说明 3）（2）不支持退款的渠道及内部账户，实时退款至余额（接口说明 4）
			request.put("backUrl", "http://172.16.190.46:8080/yuncallback/mock/notify?");

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
