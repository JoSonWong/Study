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
import com.jwong.education.util.FormatUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClockViewModel extends ViewModel {

    private MutableLiveData<List<ClockRecord>> clockRecordList;
    private MutableLiveData<List<ClockRecord>> studentClockRecordList;
    private MutableLiveData<Map<String, Double>> mothCosts;
    private MutableLiveData<Map<String, Float>> studentCurriculumStatistic;
    private MutableLiveData<List<ClockRecord>> monthRecordList;


    public ClockViewModel() {
        this.clockRecordList = new MutableLiveData<>();
        this.studentClockRecordList = new MutableLiveData<>();
        this.mothCosts = new MutableLiveData<>();
        this.studentCurriculumStatistic = new MutableLiveData<>();
        this.monthRecordList = new MutableLiveData<>();
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
                                  List<Student> students, Integer clockType, float unit) {
        for (Student student : students) {
            ClockRecord clockRecord = new ClockRecord();
            clockRecord.setClockTime(date);
            clockRecord.setCurriculumId(curriculumId);
            clockRecord.setCurriculumName(curriculumName);
            clockRecord.setCurriculumPrice(curriculumPrice);
            clockRecord.setStudentId(student.getId());
            clockRecord.setStudentName(student.getName());
            clockRecord.setClockType(clockType);
            clockRecord.setUnit(unit);
            List<StudentCurriculum> studentCurriculumList = StudentCurriculumDbService.getInstance(
                    StudyApplication.getDbController()).query(student.getId(), curriculumId);
            if (studentCurriculumList != null && !studentCurriculumList.isEmpty()) {
                Double discountPrice = studentCurriculumList.get(0).getDiscountPrice();
                Log.d(getClass().getSimpleName(), "学生：" + student.getName()
                        + " 课程：" + curriculumName + " 课时价格：" + (student.getCostType() == 1 ? 0 : discountPrice));
                clockRecord.setCurriculumDiscountPrice(student.getCostType() == 1 ? 0 : discountPrice);
                ClockDbService.getInstance(StudyApplication.getDbController()).insert(clockRecord);
            }
        }
    }


    public void insertClockRecord(CurriculumDTO curriculum, List<Student> students, float unit) {
        Date date = new Date();
        insertClockRecord(date, curriculum.getId(), curriculum.getName(), curriculum.getPrice(), students, 0, unit);
        this.clockRecordList.postValue(ClockDbService.getInstance(StudyApplication.getDbController())
                .searchAllGroupByCurriculumAndTime(10));
    }


//    public LiveData<List<ClockRecord>> getStudentClockRecordList(long studentId) {
//        this.studentClockRecordList.postValue(ClockDbService.getInstance(StudyApplication.getDbController())
//                .searchClockRecordByStudentId(studentId));
//        return studentClockRecordList;
//    }

    public LiveData<List<ClockRecord>> getStudentAllClockRecordList(long studentId, Date from, Date to) {
        this.studentClockRecordList.postValue(ClockDbService.getInstance(StudyApplication.getDbController())
                .searchAllClockRecord(studentId, from, to));
        return studentClockRecordList;
    }

    public void delete(long curriculumId, Date clockTime) {
        ClockDbService.getInstance(StudyApplication.getDbController()).deleteCurriculumTimeRecord(curriculumId, clockTime);
        this.clockRecordList.postValue(ClockDbService.getInstance(StudyApplication.getDbController())
                .searchAllGroupByCurriculumAndTime(10));
    }


    public LiveData<Map<String, Double>> getDateCost(Date from, Date to) {
        List<ClockRecord> list = ClockDbService.getInstance(StudyApplication.getDbController())
                .searchClockRecord(from, to);
        Map<String, Double> map = new HashMap<>();
        for (ClockRecord cost : list) {
            String key = cost.getCurriculumName();
            Double existDiscountPrice;
            if ((existDiscountPrice = map.get(key)) != null) {
                double discountPrice = existDiscountPrice + cost.getCurriculumDiscountPrice() * cost.getUnit();
                map.put(key, discountPrice);
            } else {
                map.put(key, cost.getCurriculumDiscountPrice() * cost.getUnit());
            }
        }
        mothCosts.postValue(map);
        return mothCosts;
    }


    public LiveData<Map<String, Float>> getStudentCurriculumStatistic(long studentId, Date from, Date to) {
        List<ClockRecord> list = ClockDbService.getInstance(StudyApplication.getDbController())
                .searchClockRecord(studentId, from, to);
        Map<String, Float> map = new HashMap<>();
        for (ClockRecord record : list) {
            String key = record.getCurriculumName();
            Float count;
            if ((count = map.get(key)) != null) {
                count = count + record.getUnit();
                map.put(key, count);
            } else {
                map.put(key, record.getUnit());
            }
        }
        studentCurriculumStatistic.postValue(map);
        return studentCurriculumStatistic;
    }

    public LiveData<List<ClockRecord>> getMonthClockRecordListGroupByClockTime(Date from, Date to) {
        Log.d(getClass().getSimpleName(), "getMonthClockRecordList from:" + FormatUtils.convert2DateTime(from) +
                " to:" + FormatUtils.convert2DateTime(to));
        this.monthRecordList.postValue(ClockDbService.getInstance(StudyApplication.getDbController())
                .searchClockRecordGroupByClockTime(from, to));
        return this.monthRecordList;
    }
}