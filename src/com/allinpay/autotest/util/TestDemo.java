package com.allinpay.autotest.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

/**
 * author: yuzq
 * create: 2021-03-31 21:11
 **/
public class TestDemo {


    public static void main(String[] args) {

        /*JSONArray collectPayList = new JSONArray();
        HashMap<String, Object> collect1 = new HashMap<>();
        collect1.put("bizOrderNo", "1617195530961ds");
        collect1.put("amount", 2L);
        collect1.put("remark", "消费一级分账供应商");

        collectPayList.add(new JSONObject(collect1));

        HashMap<String, Object> collect2 = new HashMap<>();
        collect2.put("bizOrderNo", "1617195530961ds");
        collect2.put("amount", 1L);
        collect2.put("remark", "消费订单二级分账余");
        collectPayList.add(new JSONObject(collect2));
        System.out.println(JSONObject.toJSONString(collectPayList));*/


        JSONArray batchPayList = new JSONArray();
        HashMap<String, Object> pay1 = new HashMap<>();
        //pay1.put("bizOrderNo", System.currentTimeMillis() + "cz");

/*        JSONArray collectPayList = new JSONArray();
        HashMap<String, Object> collect1 = new HashMap<>();
       // collect1.put("bizOrderNo", "1545033676799cz");
        collect1.put("amount", 1L);
        collect1.put("bizUserId", "1601929311");
        collect1.put("remark", "消费订单二级分账余");
        collectPayList.add(new JSONObject(collect1));*/

      //  pay1.put("splitRuleList", collectPayList);
        pay1.put("bizUserId", "1601929311");
        pay1.put("remark", "消费订单二级分账余");
       // pay1.put("backUrl", "http://192.168.14.165:8080/yundemo/servletUI/notice");
        pay1.put("amount", 1L);
        batchPayList.add(new JSONObject(pay1));

/*      HashMap<String, Object> splitRule1 = new HashMap<>();
        splitRule1.put("bizUserId", "1601929311");
        splitRule1.put("amount", 1L);
        splitRule1.put("fee", 0L);
        splitRule1.put("remark", " 消费一级分账");
        splitRule1.put("splitRuleList", splitRule1);*/

        System.out.println(batchPayList.toJSONString());
    }
}
