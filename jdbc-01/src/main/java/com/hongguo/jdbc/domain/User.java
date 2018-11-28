package com.hongguo.jdbc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @auther: chenghongguo
 * @date: 2018-11-19
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private Integer id;
    private String userName;
    private Integer userGender;
    private Integer userSalary;
    private Date userBirthday;
}
