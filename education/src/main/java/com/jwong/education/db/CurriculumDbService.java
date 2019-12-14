package com.jwong.education.db;

import com.jwong.education.dao.Curriculum;
import com.jwong.education.dao.CurriculumDao;
import com.jwong.education.dao.Student;
import com.jwong.education.dao.StudentDao;

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
     *
     * @param dbController
     */
    public CurriculumDbService(DbController dbController) {
        curriculumDao = dbController.getDaoSession().getCurriculumDao();
    }

    /**
     * 会自动判定是插入还是替换
     *
     * @param curriculum
     */
    public void insertOrReplace(Curriculum curriculum) {
        curriculumDao.insertOrReplace(curriculum);
    }

    /**
     * 插入一条记录，表里面要没有与之相同的记录
     *
     * @param curriculum
     */
    public long insert(Curriculum curriculum) {
        return curriculumDao.insert(curriculum);
    }

    /**
     * 更新数据
     *
     * @param curriculum
     */
    public long update(Curriculum curriculum) {
        Curriculum oldStudent = curriculumDao.queryBuilder().where(CurriculumDao.Properties.Id.eq(curriculum.getId())).build().unique();//拿到之前的记录
        if (oldStudent != null) {
            curriculumDao.update(oldStudent);
            return oldStudent.getId();
        }
        return 0;
    }

    /**
     * 按条件查询数据
     */
    public List<Curriculum> searchByWhere(String name) {
        QueryBuilder<Curriculum> queryMenu = curriculumDao.queryBuilder();
        queryMenu.where(CurriculumDao.Properties.Name.eq(name));
        List<Curriculum> list = queryMenu.list();
        return list;
    }

    /**
     * 查询所有数据
     */
    public List<Curriculum> searchAll() {
        List<Curriculum> curriculumList = curriculumDao.queryBuilder().list();
        return curriculumList;
    }

    /**
     * 删除数据
     */
    public void delete(String wherecluse) {
        curriculumDao.queryBuilder().where(StudentDao.Properties.Name.eq(wherecluse)).buildDelete().executeDeleteWithoutDetachingEntities();
    }
}
