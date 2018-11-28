package com.hongguo.db;

import lombok.*;

/**
 * @author: chenghongguo
 * @date: 2018-11-27
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class IndexColumnInfo {
    private String tableName;
    private String columnName;
}
