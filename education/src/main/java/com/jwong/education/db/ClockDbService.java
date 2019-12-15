package com.jwong.education.db;

import com.jwong.education.dao.ClockRecord;
import com.jwong.education.dao.ClockRecordDao;

import org.greenrobot.greendao.query.QueryBuilder;

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
    public void delete(String wherecluse) {
        clockRecordDao.queryBuilder().where(ClockRecordDao.Properties.Id.eq(wherecluse)).buildDelete().executeDeleteWithoutDetachingEntities();
    }
}
