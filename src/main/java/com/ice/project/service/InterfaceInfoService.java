package com.ice.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ice.iceapicommon.model.entity.InterfaceInfo;


/**
* @author HONOR
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-03-20 14:46:07
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {


   void validInterfaceInfo(InterfaceInfo interfaceinfo, boolean add);

}
