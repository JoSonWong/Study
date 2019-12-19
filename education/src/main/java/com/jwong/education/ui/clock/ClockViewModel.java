package com.jwong.education.ui.clock;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jwong.education.StudyApplication;
import com.jwong.education.dao.ClockRecord;
import com.jwong.education.dao.Student;
import com.jwong.education.dao.StudentCurriculum;
import com.jwong.education.db.ClockDbService;
import com.jwong.education.db.StudentCurriculumDbService;
import com.jwong.education.dto.CurriculumDTO;
import com.jwong.education.util.DateFormatUtil;

import java.util.Date;
import java.util.List;

public class ClockViewModel extends ViewModel {

    private MutableLiveData<List<ClockRecord>> clockRecordList;
    private MutableLiveData<List<ClockRecord>> studentClockRecordList;


    public ClockViewModel() {
        this.clockRecordList = new MutableLiveData<>();
        this.studentClockRecordList = new MutableLiveData<>();
    }

    public LiveData<List<ClockRecord>> getClockRecordList(int limit) {
        this.clockRecordList.postValue(ClockDbService.getInstance(StudyApplication.getDbController())
                .searchAllGroupByCurriculumAndTime(limit));
        return clockRecordList;
    }

    public LiveData<List<ClockRecord>> getClockRecordDetailList(long curriculumId, Date clockTime) {
        this.clockRecordList.postValue(ClockDbService.getInstance(StudyApplication.getDbController())
                .searchClockRecordDetail(curriculumId, clockTime));
        return clockRecordList;
    }

    public long update(ClockRecord clockRecord) {
        return ClockDbService.getInstance(StudyApplication.getDbController()).update(clockRecord);
    }

    public void delete(long recordId) {
        ClockDbService.getInstance(StudyApplication.getDbController()).delete(recordId);
    }

    public void insertClockRecord(Date date, long curriculumId, String curriculumName, double curriculumPrice,
                                  List<Student> students, Integer clockType) {
        for (Student student : students) {
            ClockRecord clockRecord = new ClockRecord();
            clockRecord.setClockTime(date);
            clockRecord.setCurriculumId(curriculumId);
            clockRecord.setCurriculumName(curriculumName);
            clockRecord.setCurriculumPrice(curriculumPrice);
            clockRecord.setStudentId(student.getId());
            clockRecord.setStudentName(student.getName());
            clockRecord.setClockType(clockType);
            List<StudentCurriculum> studentCurriculumList = StudentCurriculumDbService.getInstance(
                    StudyApplication.getDbController()).query(student.getId(), curriculumId);
            if (studentCurriculumList != null && !studentCurriculumList.isEmpty()) {
                Double discountPrice = studentCurriculumList.get(0).getDiscountPrice();
                Log.d(getClass().getSimpleName(), "学生：" + student.getName()
                        + " 课程：" + curriculumName + " 课时价格：" + discountPrice);
                clockRecord.setCurriculumDiscountPrice(discountPrice);
                ClockDbService.getInstance(StudyApplication.getDbController()).insert(clockRecord);
            }
        }
    }


    public void insertClockRecord(CurriculumDTO curriculum, List<Student> students) {
        Date date = new Date();
        insertClockRecord(date, curriculum.getId(), curriculum.getName(), curriculum.getPrice(), students, 0);
        this.clockRecordList.postValue(ClockDbService.getInstance(StudyApplication.getDbController())
                .searchAllGroupByCurriculumAndTime(5));
    }


    public LiveData<List<ClockRecord>> getStudentClockRecordList(long studentId) {
        this.studentClockRecordList.postValue(ClockDbService.getInstance(StudyApplication.getDbController())
                .searchClockRecordByStudentId(studentId));
        return studentClockRecordList;
    }

    public List<ClockRecord> getStudentClockRecordList(long studentId, Date from, Date to) {

        return ClockDbService.getInstance(StudyApplication.getDbController())
                .searchClockRecord(studentId, from, to);
    }

}