package com.ice.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ice.iceapicommon.model.entity.UserInterfaceInfo;
import com.ice.project.common.ErrorCode;
import com.ice.project.exception.BusinessException;
import com.ice.project.mapper.UserInterfaceInfoMapper;

import com.ice.project.service.UserInterfaceInfoService;
import org.springframework.stereotype.Service;

/**
* @author HONOR
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service实现
* @createDate 2024-04-15 19:41:23
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService {


    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userinterfaceinfo, boolean add) {

        if (userinterfaceinfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        if (add) {
            if (userinterfaceinfo.getInterfaceInfoId()<=0||userinterfaceinfo.getUserId()<=0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"无调用接口或无该用户");
            }
        }

        if (userinterfaceinfo.getLeftNum()<=0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无调用次数");
        }

    }

    /**
     * 接口调用次数统计 逻辑
     * 开通接口 管理员分配次数 每调用一次 剩余次数减 1 ，总调用次数+1
     * 条件 接口id 用户id 进行找到 该接口用户信息
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        if (interfaceInfoId<=0||userId<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //查询数据库接口调用次数是否小于0
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("interfaceInfoId", interfaceInfoId);
        queryWrapper.eq("userId", userId);
        UserInterfaceInfo userInterfaceInfo = this.getOne(queryWrapper);
        if (userInterfaceInfo.getLeftNum()<=0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"无调用次数");
        }


        UpdateWrapper<UserInterfaceInfo> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("interfaceInfoId", interfaceInfoId);
        updateWrapper.eq("userId", userId);
        updateWrapper.setSql("leftNum=leftNum-1,totalNum=totalNum+1");

        return this.update(updateWrapper);
    }


}









