package com.hongguo.db;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.List;

/**
 * @author: chenghongguo
 * @date: 2018-11-27
 * @description:
 */
public class DbTest {

    @Test
    public void testGetTableInfo() {
        List<TableInfo> tableInfoList = DbUtil.getTableInfoList();
        tableInfoList.stream().forEach(tableInfo -> System.out.println(JSONObject.toJSONString(tableInfo)));
    }
}
