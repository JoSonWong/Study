package com.jwong.education.dao;

import org.greenrobot.greendao.annotation.Entity;

import java.util.Calendar;
import java.util.Date;

import org.greenrobot.greendao.annotation.Generated;

@Entity
public class BaseDao {

    String remarks = "";

    Date createdAt = Calendar.getInstance().getTime();

    Date updatedAt = Calendar.getInstance().getTime();

    Date deletedAt;

    @Generated(hash = 1222868009)
    public BaseDao(String remarks, Date createdAt, Date updatedAt, Date deletedAt) {
        this.remarks = remarks;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    @Generated(hash = 443729769)
    public BaseDao() {
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getDeletedAt() {
        return this.deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

}
