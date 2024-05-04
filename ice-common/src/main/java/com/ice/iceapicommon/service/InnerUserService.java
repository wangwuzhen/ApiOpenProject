package com.ice.iceapicommon.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ice.iceapicommon.model.entity.User;


/**
 * 用户服务
 *
 * @author ice
 */
public interface InnerUserService {
    /**
     * 数据库中查是否已分配给用户秘钥（accessKey）
     * 获取调用用户信息
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);

}
