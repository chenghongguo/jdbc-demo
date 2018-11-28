package com.hongguo.db;

import lombok.*;

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
public class PrimaryKeyColumnInfo {
    private String tableName;
    private String columnName;
    private Integer keySeq;
    private String pkName;
}
