package com.jwong.education.ui.clock;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jwong.education.StudyApplication;
import com.jwong.education.dao.ClockRecord;
import com.jwong.education.dao.Curriculum;
import com.jwong.education.dao.Student;
import com.jwong.education.db.ClockDbService;
import com.jwong.education.db.DbController;

import java.util.Date;
import java.util.List;

public class ClockViewModel extends ViewModel {

    private MutableLiveData<List<ClockRecord>> clockRecordList;

    public ClockViewModel() {
        this.clockRecordList = new MutableLiveData<>();
        this.clockRecordList.postValue(ClockDbService.getInstance(StudyApplication.getDbController()).searchAll());
    }

    public LiveData<List<ClockRecord>> getClockRecordList() {
        return clockRecordList;
    }

    public void addClockRecord(Curriculum curriculum, List<Student> students) {
        Date date=new Date();
        for (Student student:students) {
            ClockRecord clockRecord = new ClockRecord();
//            clockRecord.setClockTime(date);
//            clockRecord.setStudentId(student.getId());
//            clockRecord.setStudentName(student.getName());

        }
    }
}