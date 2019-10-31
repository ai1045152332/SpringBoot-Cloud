package com.example.demo.core;

/**
 * 操作日志注解
 *
 * @author zhaojy
 * @date 2019/10/17
 */
public enum OperationType {

    /**
     * 查询，包括查看详情
     */
    SELECT,

    /**
     * 创建、新增资源
     */
    CREATE,

    /**
     * 删除
     */
    DELETE,

    /**
     * 更新，界面操作更新，不包括导入
     */
    UPDATE,

    /**
     * 导出数据
     */
    EXPORT,

    /**
     * 导入数据，与更新有区别
     */
    IMPORT,

    /**
     * 上传数据
     */
    UPLOAD;

}
