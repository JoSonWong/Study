package com.jwong.education.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.jwong.education.dao.DaoMaster;
import com.jwong.education.dao.DaoSession;


public class DbController {

    private final static String DB_NAME = "study.db";
    /**
     * Helper
     */
    private MySQLiteOpenHelper mHelper;//获取Helper对象

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
     */
    public DbController(Context context) {
        this.context = context;
        mHelper = new MySQLiteOpenHelper(context, DB_NAME, null);
        mDaoMaster = new DaoMaster(getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (mHelper == null) {
            mHelper = new MySQLiteOpenHelper(context, DB_NAME, null);
        }
        return mHelper.getReadableDatabase();
    }

    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (mHelper == null) {
            mHelper = new MySQLiteOpenHelper(context, DB_NAME, null);
        }
        return mHelper.getWritableDatabase();
    }

}
