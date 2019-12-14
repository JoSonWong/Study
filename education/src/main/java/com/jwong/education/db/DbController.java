package com.jwong.education.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.jwong.education.dao.Curriculum;
import com.jwong.education.dao.CurriculumDao;
import com.jwong.education.dao.DaoMaster;
import com.jwong.education.dao.DaoSession;

import java.util.List;

public class DbController {

    private final static String DB_NAME = "study.db";
    /**
     * Helper
     */
    private DaoMaster.DevOpenHelper mHelper;//获取Helper对象

    /**
     * DaoMaster
     */
    private DaoMaster mDaoMaster;
    /**
     * DaoSession
     */
    private DaoSession mDaoSession;
    /**
     * 上下文
     */
    private Context context;
    /**
     * dao
     */
    private CurriculumDao curriculumDao;

    private static DbController mDbController;

    /**
     * 获取单例
     */
    public static DbController getInstance(Context context) {
        if (mDbController == null) {
            synchronized (DbController.class) {
                if (mDbController == null) {
                    mDbController = new DbController(context);
                }
            }
        }
        return mDbController;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public DbController(Context context) {
        this.context = context;
        mHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        mDaoMaster = new DaoMaster(getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
        curriculumDao = mDaoSession.getCurriculumDao();
    }

    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (mHelper == null) {
            mHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        }
        SQLiteDatabase db = mHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     *
     * @return
     */
    private SQLiteDatabase getWritableDatabase() {
        if (mHelper == null) {
            mHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        }
        SQLiteDatabase db = mHelper.getWritableDatabase();
        return db;
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
    public void update(Curriculum curriculum) {
        Curriculum oldCurriculum = curriculumDao.queryBuilder().where(CurriculumDao.Properties.Id.eq(curriculum.getId())).build().unique();//拿到之前的记录
        if (oldCurriculum != null) {
            oldCurriculum.setName("张三");
            curriculumDao.update(oldCurriculum);
        }
    }

    /**
     * 按条件查询数据
     */
    public List<Curriculum> searchByWhere(String wherecluse) {
        List<Curriculum> curriculumList = (List<Curriculum>) curriculumDao.queryBuilder().where(CurriculumDao.Properties.Name.eq(wherecluse)).build().unique();
        return curriculumList;
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
        curriculumDao.queryBuilder().where(CurriculumDao.Properties.Name.eq(wherecluse)).buildDelete().executeDeleteWithoutDetachingEntities();
    }
}
