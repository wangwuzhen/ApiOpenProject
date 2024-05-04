package com.ice.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ice.iceapicommon.model.entity.UserInterfaceInfo;

import java.util.List;


/**
* @author HONOR
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Mapper
* @createDate 2024-04-15 19:41:23
* @Entity generator.domain.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {
    //select interfaceInfoId, sum(totalNum) as totalNum from user_interface_info group by interfaceInfoId order by totalNum desc limit 3;
    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);
}




