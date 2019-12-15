package com.jwong.education.db;

import com.jwong.education.dao.StudentCurriculum;
import com.jwong.education.dao.StudentCurriculumDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class StudentCurriculumDbService {

    private StudentCurriculumDao studentCurriculumDao;

    private static StudentCurriculumDbService studentDbService;

    /**
     * 获取单例
     */
    public static StudentCurriculumDbService getInstance(DbController dbController) {
        if (studentDbService == null) {
            synchronized (StudentCurriculumDbService.class) {
                if (studentDbService == null) {
                    studentDbService = new StudentCurriculumDbService(dbController);
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
    public StudentCurriculumDbService(DbController dbController) {
        studentCurriculumDao = dbController.getDaoSession().getStudentCurriculumDao();
    }

    /**
     * 会自动判定是插入还是替换
     *
     * @param studentCurriculum
     */
    public void insertOrReplace(StudentCurriculum studentCurriculum) {
        studentCurriculumDao.insertOrReplace(studentCurriculum);
    }

    /**
     * 插入一条记录，表里面要没有与之相同的记录
     *
     * @param studentCurriculum
     */
    public long insert(StudentCurriculum studentCurriculum) {
        return studentCurriculumDao.insert(studentCurriculum);
    }

    /**
     * 更新数据
     *
     * @param studentCurriculum
     */
    public long update(StudentCurriculum studentCurriculum) {
        if (studentCurriculum != null) {
            studentCurriculumDao.update(studentCurriculum);
            return studentCurriculum.getId();
        }
        return 0;
    }

    /**
     * 按条件查询数据
     */
    public List<StudentCurriculum> searchByWhere(Long studentId) {
        QueryBuilder<StudentCurriculum> queryMenu = studentCurriculumDao.queryBuilder();
        queryMenu.where(StudentCurriculumDao.Properties.StudentId.eq(studentId));
        return queryMenu.list();
    }

    /**
     * 查询所有数据
     */
    public List<StudentCurriculum> searchAll() {
        return studentCurriculumDao.queryBuilder().list();
    }

    /**
     * 删除数据
     */
    public void delete(String wherecluse) {
        studentCurriculumDao.queryBuilder().where(StudentCurriculumDao.Properties.Id.eq(wherecluse)).buildDelete().executeDeleteWithoutDetachingEntities();
    }
}
