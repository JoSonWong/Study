package com.jwong.education.dto;

import java.io.Serializable;
import java.util.Date;

public class CurriculumDTO extends BaseDTO implements Serializable {

    private Long id;//课程id，自增

    private String name;//课程名称

    private Double price;//课程价格

    public CurriculumDTO(Long id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public CurriculumDTO() {
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
