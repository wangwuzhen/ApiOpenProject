package com.ice.project.service;



import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@SpringBootTest
public class UserInterfaceInfoServiceTest {

    @Resource
    private  UserInterfaceInfoService userInterfaceInfoService;

    @Test
    public void invokeCount() {

        boolean b = userInterfaceInfoService.invokeCount(1L, 1L);

        //表示断言b为true ，即测试案例期望invokeCount方法返回 true
        Assertions.assertTrue(b);


    }
}