package com.jwong.education.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.chad.library.adapter.base.entity.node.BaseNode;
import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.jwong.education.dao.BaseDaoDao;
import com.jwong.education.dao.ClockRecord;
import com.jwong.education.dao.ClockRecordDao;
import com.jwong.education.dao.CurriculumDao;
import com.jwong.education.dao.DaoMaster;
import com.jwong.education.dao.StudentCurriculumDao;
import com.jwong.education.dao.StudentDao;
import com.jwong.education.dao.StudentMonthCostDao;

import org.greenrobot.greendao.database.Database;

public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
                    @Override
                    public void onCreateAllTables(Database db, boolean ifNotExists) {
                        DaoMaster.createAllTables(db, ifNotExists);
                    }

                    @Override
                    public void onDropAllTables(Database db, boolean ifExists) {
                        DaoMaster.dropAllTables(db, ifExists);
                    }
                }, BaseDaoDao.class, ClockRecordDao.class, CurriculumDao.class, StudentDao.class,
                StudentCurriculumDao.class, StudentMonthCostDao.class);
    }
}

