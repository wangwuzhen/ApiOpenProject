package com.ice.project.model.dto.userinterfaceinfo;


import com.ice.project.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode
@Data
public class UserInterfaceInfoQueryRequest extends PageRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 调用用户Id  用于查询为哪些用户开通了接口权限
     */
    private Long userId;

    /**
     * 接口Id 该接口都有哪些用户调用了
     */
    private Long interfaceInfoId;

    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

    /**
     * 0-正常 ，1-禁用
     */
    private Integer status;


}
