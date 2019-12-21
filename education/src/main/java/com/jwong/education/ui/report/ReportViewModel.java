package com.jwong.education.ui.report;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jwong.education.StudyApplication;
import com.jwong.education.dao.StudentMonthCost;
import com.jwong.education.db.MonthCostDbService;
import com.jwong.education.util.FormatUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportViewModel extends ViewModel {

    private MutableLiveData<List<StudentMonthCost>> data;
    private MutableLiveData<List<StudentMonthCost>> dataStatistic;

    public ReportViewModel() {
        data = new MutableLiveData<>();
        dataStatistic = new MutableLiveData<>();
    }

    public LiveData<List<StudentMonthCost>> getStudentCost(long studentId) {
        data.postValue(MonthCostDbService.getInstance(StudyApplication.getDbController()).searchCostByStudentId(studentId));
        return data;
    }

    public void insert(StudentMonthCost cost) {
        MonthCostDbService.getInstance(StudyApplication.getDbController()).insert(cost);
        data.postValue(MonthCostDbService.getInstance(StudyApplication.getDbController()).searchCostByStudentId(cost.getStudentId()));
    }

    public void insertOrReplace(StudentMonthCost cost) {
        MonthCostDbService.getInstance(StudyApplication.getDbController()).insertOrReplace(cost);
        data.postValue(MonthCostDbService.getInstance(StudyApplication.getDbController()).searchCostByStudentId(cost.getStudentId()));
    }

    public StudentMonthCost queryStudentMonthCost(long studentId, int year, int month, int costType) {
        return MonthCostDbService.getInstance(StudyApplication.getDbController())
                .searchCost(studentId, year, month, costType);
    }


    public LiveData<List<StudentMonthCost>> getStudentCost(long studentId, int year, int month) {
        data.postValue(MonthCostDbService.getInstance(StudyApplication.getDbController()).searchCost(studentId, year, month));
        return data;
    }


    public void update(StudentMonthCost cost) {
        MonthCostDbService.getInstance(StudyApplication.getDbController()).update(cost);
        data.postValue(MonthCostDbService.getInstance(StudyApplication.getDbController()).searchCostByStudentId(cost.getStudentId()));
    }


    public LiveData<List<StudentMonthCost>> getStudentCostStatistic(long studentId) {
        List<StudentMonthCost> costs = MonthCostDbService.getInstance(StudyApplication.getDbController()).searchCostByStudentId(studentId);
        List<StudentMonthCost> monthCosts = new ArrayList<>();
        if (costs != null) {
            Log.d(getClass().getSimpleName(), "学生费用：" + costs.size());
            Map<String, Double> mapPrice = new HashMap<>();
            Map<String, Double> mapDiscountPrice = new HashMap<>();
            for (StudentMonthCost cost : costs) {
                Log.d(getClass().getSimpleName(), cost.getCostName() + " 费用："
                        + cost.getPrice() + " 优惠价：" + cost.getDiscountPrice());
                String key = cost.getYear() + "-" + cost.getMonth();
                Double existDiscountPrice;
                if ((existDiscountPrice = mapDiscountPrice.get(key)) != null) {
                    double discountPrice = existDiscountPrice + cost.getDiscountPrice();
                    mapDiscountPrice.put(key, discountPrice);
                } else {
                    mapDiscountPrice.put(key, cost.getDiscountPrice());
                }

                Double existPrice;
                if ((existPrice = mapPrice.get(key)) != null) {
                    double price = existPrice + cost.getPrice();
                    mapPrice.put(key, price);
                } else {
                    mapPrice.put(key, cost.getPrice());
                }
                Log.d(getClass().getSimpleName(), "原价：" + FormatUtils.doubleFormat(mapPrice.get(key))
                        + " 优惠价：" + FormatUtils.doubleFormat(mapDiscountPrice.get(key)));
            }

            for (Map.Entry<String, Double> entry : mapDiscountPrice.entrySet()) {
                System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
                String key = entry.getKey();
                StudentMonthCost cost = new StudentMonthCost();
                cost.setStudentId(studentId);
                cost.setCostType(0);
                cost.setCostName("合计费用");
                cost.setDiscountPrice(entry.getValue());
                Double price = mapPrice.get(key);
                String[] yearMonth = key.split("-");
                cost.setYear(Integer.valueOf(yearMonth[0]));
                cost.setMonth(Integer.valueOf(yearMonth[1]));
                cost.setPrice(price == null ? entry.getValue() : price);
                monthCosts.add(cost);
            }
        }
        dataStatistic.postValue(monthCosts);
        return dataStatistic;
    }




}