import java.util.HashMap;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.allinpay.yunst.sdk.util.RSAUtil;

public class MyTestClass {

	@Test
	public void testMethod() {
		
		try {
			HashMap<String, Object> realnamePay = new HashMap<>();
			realnamePay.put("bankCardNo", RSAUtil.encrypt("6228480402637874221"));
			realnamePay.put("amount", 100000);	// 分

			HashMap<String, Object> payMethod = new HashMap<>();
			payMethod.put("REALNAMEPAY", realnamePay);
			
			System.out.println(JSONObject.toJSON(payMethod).toString());
			
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
