package com.ice.iceinterface.controller;



import com.ice.iceclientsdk.model.User;
import com.ice.iceclientsdk.utils.SignUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping("/get")
    public  String getName(String name,HttpServletRequest request){
        System.out.println(request.getHeader("ice"));
        return "Get传参 name:我是"+name;
    }
    @PostMapping("/post")
    public  String PostName(@RequestParam String name){
        return "post传参 name:我是"+name;
    }
    @PostMapping("/user")
    public  String PostName2(@RequestBody User user, HttpServletRequest request){
        String accessKey = request.getHeader("accessKey");
      //  String secretKey = request.getHeader("secretKey");
        String nonce = request.getHeader("nonce");
        String timestamp = request.getHeader("timestamp");
        String body = request.getHeader("body");
        String sign=request.getHeader("sign");
        //todo 实际是从数据库读取
        if(!accessKey.equals("ice")){
            throw new RuntimeException("无权限");
        }
        if (Long.parseLong(nonce)>10000) {
            throw new RuntimeException("无权限");
        }
        //todo 时间和当前时间 不能超过5分钟
//        if (timestamp){
//        }
        //todo 实际是从数据库中去查
        String serverSign = SignUtils.genSign(body, "abcdefg");
        if (!sign.equals(serverSign)){
            throw new  RuntimeException("无权限");
        }

        return "RestFul name:我是"+user.getUsername();
    }
}
