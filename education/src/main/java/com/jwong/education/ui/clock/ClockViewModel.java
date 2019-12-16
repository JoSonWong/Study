package com.jwong.education.ui.clock;

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

import java.util.Date;
import java.util.List;

public class ClockViewModel extends ViewModel {

    private MutableLiveData<List<ClockRecord>> clockRecordList;

    public ClockViewModel() {
        this.clockRecordList = new MutableLiveData<>();
    }

    public LiveData<List<ClockRecord>> getClockRecordList(int limit) {
        this.clockRecordList.postValue(ClockDbService.getInstance(StudyApplication.getDbController())
                .searchAllGroupByCurriculumAndTime(limit));
        return clockRecordList;
    }

    public void insertClockRecord(CurriculumDTO curriculum, List<Student> students) {
        Date date = new Date();
        for (Student student : students) {
            ClockRecord clockRecord = new ClockRecord();
            clockRecord.setClockTime(date);
            clockRecord.setCurriculumId(curriculum.getId());
            clockRecord.setCurriculumName(curriculum.getName());
            clockRecord.setCurriculumPrice(curriculum.getPrice());
            clockRecord.setStudentId(student.getId());
            clockRecord.setStudentName(student.getName());
            List<StudentCurriculum> studentCurriculumList = StudentCurriculumDbService.getInstance(
                    StudyApplication.getDbController()).query(student.getId(), curriculum.getId());
            if (studentCurriculumList != null && !studentCurriculumList.isEmpty()) {
                clockRecord.setCurriculumDiscountPrice(studentCurriculumList.get(0).getDiscountPrice());
                ClockDbService.getInstance(StudyApplication.getDbController()).insert(clockRecord);
            }
        }
        this.clockRecordList.postValue(ClockDbService.getInstance(StudyApplication.getDbController())
                .searchAllGroupByCurriculumAndTime(5));
    }
}