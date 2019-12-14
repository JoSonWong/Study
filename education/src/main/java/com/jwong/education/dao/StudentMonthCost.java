package com.jwong.education.dao;

import org.greenrobot.greendao.annotation.Id;

public class StudentMonthCost extends BaseDao {

    @Id(autoincrement = true)
    private Long id;//id

    private Long studentId;//学生id

    private Integer year;//年

    private Integer month;//月

    private Integer costType;//费用类型

    private String costName;//费用名称

    private Double price;//价格

    private Double discountPrice;//折后价

}
