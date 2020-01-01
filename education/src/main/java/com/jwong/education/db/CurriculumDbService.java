package com.jwong.education.db;

import com.jwong.education.dao.Curriculum;
import com.jwong.education.dao.CurriculumDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class CurriculumDbService {

    private CurriculumDao curriculumDao;

    private static CurriculumDbService studentDbService;

    /**
     * 获取单例
     */
    public static CurriculumDbService getInstance(DbController dbController) {
        if (studentDbService == null) {
            synchronized (CurriculumDbService.class) {
                if (studentDbService == null) {
                    studentDbService = new CurriculumDbService(dbController);
                }
            }
        }
        return studentDbService;
    }

    /**
     * 初始化
     */
    public CurriculumDbService(DbController dbController) {
        curriculumDao = dbController.getDaoSession().getCurriculumDao();
    }

    /**
     * 会自动判定是插入还是替换
     */
    public void insertOrReplace(Curriculum curriculum) {
        curriculumDao.insertOrReplace(curriculum);
    }

    /**
     * 插入一条记录，表里面要没有与之相同的记录
     */
    public long insert(Curriculum curriculum) {
        return curriculumDao.insert(curriculum);
    }

    /**
     * 更新数据
     */
    public long update(Curriculum curriculum) {
        if (curriculum != null) {
            curriculumDao.update(curriculum);
            return curriculum.getId();
        }
        return 0;
    }

    /**
     * 按条件查询数据
     */
    public List<Curriculum> searchByWhere(String name) {
        QueryBuilder<Curriculum> queryMenu = curriculumDao.queryBuilder();
        queryMenu.where(CurriculumDao.Properties.Name.eq(name));
        return queryMenu.list();
    }

    /**
     * 查询所有数据
     */
    public List<Curriculum> searchAll() {
        return curriculumDao.queryBuilder().list();
    }

    /**
     * 删除数据
     */
    public void delete(Long id) {
        curriculumDao.queryBuilder().where(CurriculumDao.Properties.Id.eq(id)).buildDelete().executeDeleteWithoutDetachingEntities();
    }
}
