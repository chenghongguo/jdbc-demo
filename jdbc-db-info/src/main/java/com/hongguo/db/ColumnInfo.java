package com.hongguo.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: chenghongguo
 * @date: 2018-11-27
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ColumnInfo {
    /**
     * 列名
     */
    private String name;
    /**
     * 列类型id
     */
    private Integer typeId;
    /**
     * 列类型名称
     */
    private String typeName;
    /**
     * 字段长度
     */
    private Integer size;
    
}
