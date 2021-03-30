package com.allinpay.yunst.order;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.allinpay.yunst.sdk.YunClient;
import com.allinpay.yunst.sdk.bean.YunRequest;

public class ApplicationTransferTest {

	@Test
	public void testMethod() {

		/**
		 * 标准余额账户 100001 用于平台管理自有资金，一般为经营收入、手续 支持 支持 支持
		 * 182 / 196
		 * 集 费收入
		 * 标准保证金账
		 * 户集
		 * 100002 用于平台向平台用户收取、计提保证金
		 * 支持 不支持 支持
		 * 准备金额度账
		 * 户集
		 * 100003
		 * 用于通商云向平台收取交易手续费、保证金等费
		 * 用。
		 * 注：平台预充值给通商云。
		 * 支持（使用通
		 * 商 云 网 关 渠
		 * 道商户）
		 * 不支持 不支持
		 * 中间账户集 A 100004 用于正向交易资金的中间账户 不支持 不支持 不支持
		 * 中间账户集 B 100005 用于逆向交易资金的中间账户 不支持 不支持 不支持
		 * 标准营销账户
		 * 集
		 * 2000000 用于平台管理营销活动相关的资金
		 * 支持 支持 支持
		 */
		final YunRequest request = new YunRequest("OrderService", "applicationTransfer");
		
		try {
			request.put("bizTransferNo", System.currentTimeMillis() + "cz");
			request.put("sourceAccountSetNo", "100002");
			request.put("targetBizUserId", "1601929311");
			request.put("targetAccountSetNo", "200126");
			request.put("amount", 1L);
			request.put("remark", "平台转账");
			
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
