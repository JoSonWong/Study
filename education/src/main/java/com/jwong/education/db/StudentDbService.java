package com.jwong.education.db;

import com.jwong.education.dao.Student;
import com.jwong.education.dao.StudentDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class StudentDbService {

    private StudentDao studentDao;

    private static StudentDbService studentDbService;

    /**
     * 获取单例
     */
    public static StudentDbService getInstance(DbController dbController) {
        if (studentDbService == null) {
            synchronized (StudentDbService.class) {
                if (studentDbService == null) {
                    studentDbService = new StudentDbService(dbController);
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
    public StudentDbService(DbController dbController) {
        studentDao = dbController.getDaoSession().getStudentDao();
    }

    /**
     * 会自动判定是插入还是替换
     *
     * @param student
     */
    public void insertOrReplace(Student student) {
        studentDao.insertOrReplace(student);
    }

    /**
     * 插入一条记录，表里面要没有与之相同的记录
     *
     * @param student
     */
    public long insert(Student student) {
        return studentDao.insert(student);
    }

    /**
     * 更新数据
     *
     * @param student
     */
    public long update(Student student) {
        if (student != null) {
            studentDao.update(student);
            return student.getId();
        }
        return 0;
    }

    /**
     * 按条件查询数据
     */
    public List<Student> searchByWhere(String name) {
        QueryBuilder<Student> queryMenu = studentDao.queryBuilder();
        queryMenu.where(StudentDao.Properties.Name.eq(name));
        return queryMenu.list();
    }

    /**
     * 查询所有数据
     */
    public List<Student> searchAll() {
        return studentDao.queryBuilder().list();
    }

    /**
     * 删除数据
     */
    public void delete(String wherecluse) {
        studentDao.queryBuilder().where(StudentDao.Properties.Name.eq(wherecluse)).buildDelete().executeDeleteWithoutDetachingEntities();
    }
}
