package com.ice.iceapicommon.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ice.iceapicommon.model.entity.UserInterfaceInfo;

/**
* @author HONOR
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service
* @createDate 2024-04-15 19:41:23
*/
public interface InnerUserInterfaceInfoService  {

    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId,long userId);
}
