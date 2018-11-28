package com.hongguo.db;

import lombok.*;

import java.util.List;

/**
 * @author: chenghongguo
 * @date: 2018-11-27
 * @description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TableInfo {
    /**
     * 表名
     */
    private String name;
    /**
     * 列集合
     */
    private List<ColumnInfo> columnInfoList;
    /**
     * 主键列信息
     */
    private List<PrimaryKeyColumnInfo> primaryKeyColumnInfoList;
    /**
     * 索引列信息
     */
    private List<IndexColumnInfo> indexColumnInfoList;
}
