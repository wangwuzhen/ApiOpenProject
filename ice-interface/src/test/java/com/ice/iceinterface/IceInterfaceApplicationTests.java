package com.ice.iceinterface;

import com.ice.iceclientsdk.client.IceApiClient;
import com.ice.iceclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class IceInterfaceApplicationTests {

    @Resource
    private IceApiClient iceApiClient;
    @Test
    void contextLoads() {
        String ice = iceApiClient.getName("ice");
        System.out.println(ice);
        User user = new User();
        user.setUsername("ice");
        String s = iceApiClient.PostName2(user);
        System.out.println(s);

    }

}
