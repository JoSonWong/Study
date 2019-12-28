package com.jwong.education.ui.report;

import android.util.Log;
import android.util.LongSparseArray;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.chad.library.adapter.base.entity.node.BaseNode;
import com.jwong.education.StudyApplication;
import com.jwong.education.dao.ClockRecord;
import com.jwong.education.dao.StudentMonthCost;
import com.jwong.education.db.ClockDbService;
import com.jwong.education.db.MonthCostDbService;
import com.jwong.education.dto.entity.CostDetailNode;
import com.jwong.education.dto.entity.CostNode;
import com.jwong.education.util.FormatUtils;
import com.jwong.education.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportViewModel extends ViewModel {

    private MutableLiveData<List<StudentMonthCost>> data;
    private MutableLiveData<List<StudentMonthCost>> dataStatistic;
    private MutableLiveData<Map<String, Double>> dataMonth;

    private MutableLiveData<List<CostNode>> dataStudentCost;


    public ReportViewModel() {
        data = new MutableLiveData<>();
        dataStatistic = new MutableLiveData<>();
        dataMonth = new MutableLiveData<>();
        dataStudentCost = new MutableLiveData<>();
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

    private StudentMonthCost getStudentMonthCurriculumCost(long studentId, int year, int month) {
        List<ClockRecord> clockRecords = ClockDbService.getInstance(StudyApplication.getDbController())
                .searchClockRecord(studentId, Utils.getYearMonthFirstDate(year, month), Utils.getYearMonthLastDate(year, month));
        return computeClockCost(studentId, year, month, clockRecords);
    }

    public LiveData<List<CostNode>> getStudentCost(long studentId, int year, int month) {
        List<CostNode> list = new ArrayList<>();
        List<ClockRecord> clockRecords = ClockDbService.getInstance(StudyApplication.getDbController())
                .searchClockRecord(studentId, Utils.getYearMonthFirstDate(year, month), Utils.getYearMonthLastDate(year, month));
        if (clockRecords != null && !clockRecords.isEmpty()) {
            StudentMonthCost curriculumCost = new StudentMonthCost();
            curriculumCost.setId(0L);
            curriculumCost.setStudentId(studentId);
            curriculumCost.setYear(year);
            curriculumCost.setMonth(month);
            curriculumCost.setCostType(0);
            curriculumCost.setCostName("课时费");

            LongSparseArray<ClockRecord> curriculumClockMap = new LongSparseArray<>();
            LongSparseArray<Integer> curriculumCountMap = new LongSparseArray<>();
            for (ClockRecord record : clockRecords) {
                if (curriculumClockMap.get(record.getCurriculumId()) != null) {
                    int count = curriculumCountMap.get(record.getCurriculumId(), 0) + 1;
                    curriculumCountMap.put(record.getCurriculumId(), count);
                } else {
                    ClockRecord record1 = new ClockRecord(record.getId(), record.getClockTime(), record.getStudentId(),
                            record.getStudentName(), record.getCurriculumId(), record.getCurriculumName(), record.getCurriculumPrice(),
                            record.getCurriculumDiscountPrice(), record.getClockType());
                    curriculumClockMap.put(record1.getCurriculumId(), record1);
                    curriculumCountMap.put(record1.getCurriculumId(), 1);
                }
            }

            List<BaseNode> thirdNodeList = new ArrayList<>();

            double price = 0;
            double discountPrice = 0;
            for (int i = 0; i < curriculumClockMap.size(); i++) {
                Long key = curriculumClockMap.keyAt(i);
                int count = curriculumCountMap.get(key);
                ClockRecord record = curriculumClockMap.get(key);
                price = price + record.getCurriculumDiscountPrice() * count;
                discountPrice = discountPrice + record.getCurriculumDiscountPrice() * count;
                thirdNodeList.add(new CostDetailNode(record.getCurriculumName(), count, record.getCurriculumPrice(),
                        record.getCurriculumDiscountPrice()));
            }

            curriculumCost.setPrice(FormatUtils.doubleFormat(price));
            curriculumCost.setDiscountPrice(FormatUtils.doubleFormat(discountPrice));

            CostNode entity = new CostNode(thirdNodeList, curriculumCost.getId(), curriculumCost.getStudentId(),
                    curriculumCost.getYear(), curriculumCost.getMonth(), curriculumCost.getCostType(), curriculumCost.getCostName(),
                    curriculumCost.getPrice(), curriculumCost.getDiscountPrice());
            list.add(entity);
        }

        List<StudentMonthCost> studentMonthCosts = MonthCostDbService.getInstance(StudyApplication.getDbController()).searchCost(studentId, year, month);
        if (studentMonthCosts != null && !studentMonthCosts.isEmpty()) {
            for (StudentMonthCost cost : studentMonthCosts) {
                CostNode entity = new CostNode(null, cost.getId(), cost.getStudentId(), cost.getYear(), cost.getMonth(),
                        cost.getCostType(), cost.getCostName(), cost.getPrice(), cost.getDiscountPrice());
                list.add(entity);
            }
        }
        dataStudentCost.postValue(list);
        return dataStudentCost;
    }


    public void update(StudentMonthCost cost) {
        MonthCostDbService.getInstance(StudyApplication.getDbController()).update(cost);
        data.postValue(MonthCostDbService.getInstance(StudyApplication.getDbController()).searchCostByStudentId(cost.getStudentId()));
    }


    public LiveData<List<StudentMonthCost>> getStudentCostStatistic(long studentId) {
        List<StudentMonthCost> costs = MonthCostDbService.getInstance(StudyApplication.getDbController())
                .searchCostByStudentId(studentId);
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
                    double discountPrice = FormatUtils.doubleFormat(existDiscountPrice + cost.getDiscountPrice());
                    mapDiscountPrice.put(key, discountPrice);
                } else {
                    mapDiscountPrice.put(key, FormatUtils.doubleFormat(cost.getDiscountPrice()));
                }

                Double existPrice;
                if ((existPrice = mapPrice.get(key)) != null) {
                    double price = FormatUtils.doubleFormat(existPrice + cost.getPrice());
                    mapPrice.put(key, price);
                } else {
                    mapPrice.put(key, FormatUtils.doubleFormat(cost.getPrice()));
                }
                Log.d(getClass().getSimpleName(), "原价：" + mapPrice.get(key)
                        + " 优惠价：" + mapDiscountPrice.get(key));
            }

            for (Map.Entry<String, Double> entry : mapDiscountPrice.entrySet()) {
                String key = entry.getKey();
                StudentMonthCost cost = new StudentMonthCost();
                cost.setStudentId(studentId);
                cost.setCostType(0);
                cost.setCostName("合计费用");
                cost.setDiscountPrice(FormatUtils.doubleFormat(entry.getValue()));
                Double price = 0D;
                if (mapPrice.get(key) != null) {
                    price = FormatUtils.doubleFormat(mapPrice.get(key));
                }
                String[] yearMonth = key.split("-");
                cost.setYear(Integer.valueOf(yearMonth[0]));
                cost.setMonth(Integer.valueOf(yearMonth[1]));
                cost.setPrice(FormatUtils.doubleFormat(price));
                monthCosts.add(cost);
            }
        }
        dataStatistic.postValue(monthCosts);
        return dataStatistic;
    }

    private StudentMonthCost computeClockCost(long studentId, int year, int month, List<ClockRecord> clockRecords) {
        if (clockRecords != null && !clockRecords.isEmpty()) {
            StudentMonthCost curriculumCost = new StudentMonthCost();
            curriculumCost.setId(0L);
            curriculumCost.setStudentId(studentId);
            curriculumCost.setYear(year);
            curriculumCost.setMonth(month);
            curriculumCost.setCostType(0);
            curriculumCost.setCostName("课时费");
            double price = 0;
            double discountPrice = 0;
            for (ClockRecord record : clockRecords) {
                price = FormatUtils.doubleFormat(price + record.getCurriculumPrice());
                discountPrice = FormatUtils.doubleFormat(discountPrice + record.getCurriculumDiscountPrice());
            }
            curriculumCost.setPrice(FormatUtils.doubleFormat(price));
            curriculumCost.setDiscountPrice(FormatUtils.doubleFormat(discountPrice));
            return curriculumCost;
        }
        return null;
    }


    public LiveData<Map<String, Double>> getDateCost(int year, int month) {
        Map<String, Double> map = new HashMap<>();
        List<ClockRecord> clockRecords = ClockDbService.getInstance(StudyApplication.getDbController())
                .searchClockRecord(Utils.getYearMonthFirstDate(year, month), Utils.getYearMonthLastDate(year, month));
        StudentMonthCost clockCost = computeClockCost(0, year, month, clockRecords);
        if (clockCost != null) {
            map.put(clockCost.getCostName(), FormatUtils.doubleFormat(clockCost.getDiscountPrice()));
            Log.d(getClass().getSimpleName(), "统计" + clockCost.getCostName()
                    + " 合计：" + FormatUtils.doubleFormat(clockCost.getDiscountPrice()));
        }
        List<StudentMonthCost> list = MonthCostDbService.getInstance(StudyApplication.getDbController()).searchCost(year, month);
        for (StudentMonthCost cost : list) {
            String key = cost.getCostName();
            Double existDiscountPrice;
            if ((existDiscountPrice = map.get(key)) != null) {
                double discountPrice = FormatUtils.doubleFormat(existDiscountPrice + cost.getDiscountPrice());
                map.put(key, discountPrice);
                Log.d(getClass().getSimpleName(), key + " 费用：" + FormatUtils.doubleFormat(discountPrice));
            } else {
                map.put(key, FormatUtils.doubleFormat(cost.getDiscountPrice()));
                Log.d(getClass().getSimpleName(), key + " 费用："
                        + FormatUtils.doubleFormat(cost.getDiscountPrice()));
            }
        }
        dataMonth.postValue(map);
        return dataMonth;
    }

}