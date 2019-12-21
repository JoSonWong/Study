package com.jwong.education.db;


import android.util.Log;

import com.jwong.education.dao.StudentMonthCost;
import com.jwong.education.dao.StudentMonthCostDao;

import java.util.List;

public class MonthCostDbService {

    private StudentMonthCostDao studentMonthCostDao;

    private static MonthCostDbService monthCostDbService;

    /**
     * 获取单例
     */
    public static MonthCostDbService getInstance(DbController dbController) {
        if (monthCostDbService == null) {
            synchronized (MonthCostDbService.class) {
                if (monthCostDbService == null) {
                    monthCostDbService = new MonthCostDbService(dbController);
                }
            }
        }
        return monthCostDbService;
    }

    /**
     * 初始化
     *
     * @param dbController
     */
    public MonthCostDbService(DbController dbController) {
        studentMonthCostDao = dbController.getDaoSession().getStudentMonthCostDao();
    }

    /**
     * 会自动判定是插入还是替换
     *
     * @param cost
     */
    public void insertOrReplace(StudentMonthCost cost) {
        studentMonthCostDao.insertOrReplace(cost);
    }

    /**
     * 插入一条记录，表里面要没有与之相同的记录
     *
     * @param cost
     */
    public long insert(StudentMonthCost cost) {
        return studentMonthCostDao.insert(cost);
    }

    /**
     * 更新数据
     *
     * @param cost
     */
    public long update(StudentMonthCost cost) {
        if (cost != null) {
            studentMonthCostDao.update(cost);
            return cost.getId();
        }
        return 0;
    }


    /**
     * 查询所有数据
     */
    public List<StudentMonthCost> searchAll() {
        return studentMonthCostDao.queryBuilder().list();
    }

    /**
     * 删除数据
     */
    public void delete(long id) {
        studentMonthCostDao.queryBuilder().where(StudentMonthCostDao.Properties.Id.eq(id))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }


    /**
     * 查询所有数据
     */
    public List<StudentMonthCost> searchCost(long studentId, int year, int month) {
        return studentMonthCostDao.queryBuilder().where(
                StudentMonthCostDao.Properties.StudentId.eq(studentId),
                StudentMonthCostDao.Properties.CostType.notEq(0),
                StudentMonthCostDao.Properties.Year.eq(year),
                StudentMonthCostDao.Properties.Month.eq(month)).build().list();
    }


    /**
     * 查询所有数据
     */
    public List<StudentMonthCost> searchCostByStudentId(long studentId) {
        return studentMonthCostDao.queryBuilder().orderDesc(StudentMonthCostDao.Properties.Id)
                .where(StudentMonthCostDao.Properties.StudentId.eq(studentId),
                        StudentMonthCostDao.Properties.CostType.notEq(0)).build().list();
    }


    /**
     * 查询所有数据
     */
    public StudentMonthCost searchCost(long studentId, int year, int month, int costType) {
        Log.d(getClass().getSimpleName(), "searchCost studentId：" + studentId
                + " year:" + year + " month:" + month + " costType:" + costType);
        return studentMonthCostDao.queryBuilder().where(
                StudentMonthCostDao.Properties.StudentId.eq(studentId),
                StudentMonthCostDao.Properties.CostType.eq(costType),
                StudentMonthCostDao.Properties.Year.eq(year),
                StudentMonthCostDao.Properties.Month.eq(month))
                .build().unique();
    }


    /**
     * 查询所有数据
     */
    public List<StudentMonthCost> searchCost(int year, int month) {
        return studentMonthCostDao.queryBuilder().where(
                StudentMonthCostDao.Properties.CostType.notEq(0),
                StudentMonthCostDao.Properties.Year.eq(year),
                StudentMonthCostDao.Properties.Month.eq(month)).build().list();
    }

}
