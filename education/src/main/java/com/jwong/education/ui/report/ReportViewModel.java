package com.jwong.education.ui.report;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jwong.education.StudyApplication;
import com.jwong.education.dao.StudentMonthCost;
import com.jwong.education.db.MonthCostDbService;

import java.util.List;

public class ReportViewModel extends ViewModel {

    private MutableLiveData<List<StudentMonthCost>> data;

    public ReportViewModel() {
        data = new MutableLiveData<>();
    }

    public LiveData<List<StudentMonthCost>> getStudentCost(long studentId) {
        data.postValue(MonthCostDbService.getInstance(StudyApplication.getDbController()).searchCostByStudentId(studentId));
        return data;
    }

    public void insert(StudentMonthCost cost) {
        MonthCostDbService.getInstance(StudyApplication.getDbController()).insertOrReplace(cost);
        data.postValue(MonthCostDbService.getInstance(StudyApplication.getDbController()).searchCostByStudentId(cost.getStudentId()));
    }

    public StudentMonthCost queryStudentMonthCost(long studentId, int year, int month, int costType) {
        List<StudentMonthCost> list = MonthCostDbService.getInstance(StudyApplication.getDbController())
                .searchCost(studentId, year, month, costType);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }


    public LiveData<List<StudentMonthCost>> getStudentCost(long studentId, int year, int month) {
        data.postValue(MonthCostDbService.getInstance(StudyApplication.getDbController()).searchCost(studentId, year, month));
        return data;
    }

}