package com.jwong.education.db;

import android.util.Log;

import com.jwong.education.dao.StudentCurriculum;
import com.jwong.education.dao.StudentCurriculumDao;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

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
        Log.d(getClass().getSimpleName(), "添加学生课程，学生id：" + studentCurriculum.getStudentId()
                + " 课程id：" + studentCurriculum.getCurriculumId());
        List<StudentCurriculum> list = query(studentCurriculum.getStudentId(), studentCurriculum.getCurriculumId());
        if (list == null || list.isEmpty()) {
            Log.d(getClass().getSimpleName(), "插入学生课程，学生id：" + studentCurriculum.getStudentId()
                    + " 课程id：" + studentCurriculum.getCurriculumId());
            return studentCurriculumDao.insert(studentCurriculum);
        }
        return 0;
    }

    public List<StudentCurriculum> query(long studentId, long curriculumId) {
//        List<StudentCurriculum> list = studentCurriculumDao.queryBuilder().where(studentCurriculumDao.queryBuilder()
//                .and(StudentCurriculumDao.Properties.StudentId.eq(studentId),
//                        studentCurriculumDao.queryBuilder().or(StudentCurriculumDao.Properties.统一编号.like("%" + keywords + "%"),
//                                JBXXDao.Properties.位置.like("%" + keywords + "%"),
//                                JBXXDao.Properties.名称.like("%" + keywords + "%")))).list();
        Log.d(getClass().getSimpleName(), "query studentId:" + studentId + " curriculumId:" + curriculumId);
//        QueryBuilder<StudentCurriculum> queryBuilder = studentCurriculumDao.queryBuilder();
//        queryBuilder.and(StudentCurriculumDao.Properties.StudentId.eq(studentId),
//                StudentCurriculumDao.Properties.CurriculumId.eq(curriculumId));
        Query query = studentCurriculumDao.queryBuilder().where(StudentCurriculumDao.Properties.StudentId.eq(studentId)
                , StudentCurriculumDao.Properties.CurriculumId.eq(curriculumId)).build();
        return query.list();
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
    public void delete(long id) {
        studentCurriculumDao.queryBuilder().where(StudentCurriculumDao.Properties.Id.eq(id)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public void delete(long studentId, long curriculumId) {
        List<StudentCurriculum> list = query(studentId, curriculumId);
        if (list != null && !list.isEmpty()) {
            for (StudentCurriculum item : list) {
                delete(item.getId());
            }
        }
    }


    public List<StudentCurriculum> queryGroupByStudent(long curriculumId) {
        return studentCurriculumDao.queryBuilder().where(
                new WhereCondition.StringCondition(StudentCurriculumDao.Properties.CurriculumId.columnName + "=" + curriculumId
                        + " GROUP BY " + StudentCurriculumDao.Properties.StudentId.columnName)).list();
//        Query query = studentCurriculumDao.queryBuilder().where(
//                new WhereCondition.StringCondition(StudentCurriculumDao.Properties.CurriculumId.eq(curriculumId)
//                        + "GROUP BY STUDENT_ID"))
//                .orderAsc(StudentCurriculumDao.Properties.Id).build();
//        return query.list();
    }
}
