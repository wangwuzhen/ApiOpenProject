package com.ice.iceclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.ice.iceclientsdk.model.User;


import java.util.HashMap;
import java.util.Map;

import static com.ice.iceclientsdk.utils.SignUtils.genSign;

/**
 * 通过工具类调用接口
 * Hutool工具类的使用
 */
public class IceApiClient {

    public IceApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    private String accessKey;
    private String secretKey;


    //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
    public  String getName(String name){
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        //Hutool工具类
        String result1= HttpUtil.get("http://localhost:8080/api/name/",paramMap);
        System.out.println(result1);
        return result1;
    }

    public  String PostName( String name){
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result= HttpUtil.post("http://localhost:8080/api/name/", paramMap);
        System.out.println(result);
        return result;

    }
private Map<String,String> getHeaderMap(String body){
        Map<String,String> hashMap=new HashMap<>();
        hashMap.put("accessKey",accessKey);
        //记住前台一定不要往后端传输密码
        //hashMap.put("secretKey",secretKey);
        //生成一个随机数唯一
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        //请求体内容
        hashMap.put("body",body);
        //当前时间戳
        hashMap.put("timestamp",String.valueOf(System.currentTimeMillis()/1000));
        //md5+SHA256(accessKey+nonce+body+.+secretKey)=sign
        hashMap.put("sign",genSign(body,secretKey));
        return  hashMap;
}
    // RestFull 传递参数（对象）
    public  String PostName2( User user){

        String json = JSONUtil.toJsonStr(user);
        HttpResponse httpResponse = HttpRequest.post("http://localhost:8080/api/name/user")
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute();
        int status = httpResponse.getStatus();
        System.out.println(status);
        System.out.println(httpResponse.body());
        return httpResponse.body();

    }

}
