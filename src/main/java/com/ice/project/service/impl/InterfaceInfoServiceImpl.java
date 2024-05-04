package com.ice.project.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ice.iceapicommon.model.entity.InterfaceInfo;
import com.ice.project.common.ErrorCode;
import com.ice.project.exception.BusinessException;
import com.ice.project.mapper.InterfaceInfoMapper;


import com.ice.project.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author HONOR
* @description 针对表【interface_info(接口信息)】的数据库操作Service实现
* @createDate 2024-03-20 14:46:07
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService {

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceinfo, boolean add) {


        if (interfaceinfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
//        Long id = interfaceinfo.getId();
        String name = interfaceinfo.getName();
//        String description = interfaceinfo.getDescription();
//        String url = interfaceinfo.getUrl();
//        String requestHeader = interfaceinfo.getRequestHeader();
//        String responseHeader = interfaceinfo.getResponseHeader();
//        Integer status = interfaceinfo.getStatus();
//        String method = interfaceinfo.getMethod();
//        Long userId = interfaceinfo.getUserId();
//        Date createTime = interfaceinfo.getCreateTime();
//        Date updateTime = interfaceinfo.getUpdateTime();
//        Integer isDelete = interfaceinfo.getIsDelete();
        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(name)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }

        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }

    }

}




