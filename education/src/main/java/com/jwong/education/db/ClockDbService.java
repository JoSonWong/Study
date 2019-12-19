package com.jwong.education.db;

import android.util.Log;

import com.jwong.education.dao.ClockRecord;
import com.jwong.education.dao.ClockRecordDao;
import com.jwong.education.util.DateFormatUtil;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.Date;
import java.util.List;

public class ClockDbService {

    private ClockRecordDao clockRecordDao;

    private static ClockDbService studentDbService;

    /**
     * 获取单例
     */
    public static ClockDbService getInstance(DbController dbController) {
        if (studentDbService == null) {
            synchronized (ClockDbService.class) {
                if (studentDbService == null) {
                    studentDbService = new ClockDbService(dbController);
                }
            }
        }
        return studentDbService;
    }

    /**
     * 初始化
     *
     * @param dbController
     */
    public ClockDbService(DbController dbController) {
        clockRecordDao = dbController.getDaoSession().getClockRecordDao();
    }

    /**
     * 会自动判定是插入还是替换
     *
     * @param clockRecord
     */
    public void insertOrReplace(ClockRecord clockRecord) {
        clockRecordDao.insertOrReplace(clockRecord);
    }

    /**
     * 插入一条记录，表里面要没有与之相同的记录
     *
     * @param clockRecord
     */
    public long insert(ClockRecord clockRecord) {
        return clockRecordDao.insert(clockRecord);
    }

    /**
     * 更新数据
     *
     * @param clockRecord
     */
    public long update(ClockRecord clockRecord) {
        if (clockRecord != null) {
            clockRecordDao.update(clockRecord);
            return clockRecord.getId();
        }
        return 0;
    }

    /**
     * 按条件查询数据
     */
    public List<ClockRecord> searchByWhere(String name) {
        QueryBuilder<ClockRecord> queryMenu = clockRecordDao.queryBuilder();
        queryMenu.where(ClockRecordDao.Properties.StudentName.eq(name));
        return queryMenu.list();
    }

    /**
     * 查询所有数据
     */
    public List<ClockRecord> searchAll() {
        return clockRecordDao.queryBuilder().list();
    }

    /**
     * 删除数据
     */
    public void delete(long id) {
        clockRecordDao.queryBuilder().where(ClockRecordDao.Properties.Id.eq(id))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }

    /**
     * 查询所有数据
     */
    public List<ClockRecord> searchAllGroupByCurriculumAndTime(int limit) {
        return clockRecordDao.queryBuilder().limit(limit).orderDesc(ClockRecordDao.Properties.ClockTime)
                .where(new WhereCondition.StringCondition(ClockRecordDao.Properties.CurriculumId.columnName + ">" + 0
                        + " GROUP BY " + ClockRecordDao.Properties.ClockTime.columnName)).list();
    }


    /**
     * 查询所有数据
     */
    public List<ClockRecord> searchClockRecordDetail(long curriculumId, Date date) {
        return clockRecordDao.queryBuilder().where(ClockRecordDao.Properties.CurriculumId.eq(curriculumId)
                , ClockRecordDao.Properties.ClockTime.eq(date)).build().list();
    }


    /**
     * 查询所有数据
     */
    public List<ClockRecord> searchClockRecordByStudentId(long studentId) {
        return clockRecordDao.queryBuilder().orderDesc(ClockRecordDao.Properties.Id)
                .where(ClockRecordDao.Properties.StudentId.eq(studentId)).build().list();
    }


    /**
     * 查询所有数据
     */
    public List<ClockRecord> searchClockRecord(long studentId, Date from, Date to) {
        Log.d(getClass().getSimpleName(), "查询学生：" + studentId + " 打卡范围[" + DateFormatUtil.convert2DateTime(from) + ","
                + DateFormatUtil.convert2DateTime(to)+"]");
        List<ClockRecord> list = clockRecordDao.queryBuilder().where(ClockRecordDao.Properties.StudentId.eq(studentId),
                ClockRecordDao.Properties.ClockType.eq(0), ClockRecordDao.Properties.ClockTime.ge(from),
                ClockRecordDao.Properties.ClockTime.le(to)).build().list();
        Log.d(getClass().getSimpleName(), "查询学生打卡记录：" + (list == null ? "NULL" : list.size()));
        return list;
    }

}
