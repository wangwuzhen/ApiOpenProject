package com.ice.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ice.project.model.entity.InterfaceInfo;
import com.ice.project.model.entity.UserInterfaceInfo;

/**
* @author HONOR
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service
* @createDate 2024-04-15 19:41:23
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {
    void validUserInterfaceInfo(UserInterfaceInfo userinterfaceinfo, boolean add);

    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId,long userId);
}
