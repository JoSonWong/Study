package com.jwong.education.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Curriculum extends BaseDao {

    @Id(autoincrement = true)
    private Long id;//课程id，自增

    @Index(unique = true)//设置唯一性
    private String name;//课程名称

    private Double price = 0.0;//课程价格

    @Generated(hash = 1094616265)
    public Curriculum(Long id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Generated(hash = 369254021)
    public Curriculum() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return this.price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}
