package com.jwong.education.ui.student;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jwong.education.R;
import com.jwong.education.dao.ClockRecord;
import com.jwong.education.dao.Student;
import com.jwong.education.dao.StudentMonthCost;
import com.jwong.education.ui.clock.ClockViewModel;
import com.jwong.education.ui.report.CostAdapter;
import com.jwong.education.ui.report.CostStatisticsAdapter;
import com.jwong.education.ui.report.ReportViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class StudentCostFragment extends Fragment implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener {

    private ClockViewModel clockViewModel;
    private ReportViewModel reportViewModel;
    private RecyclerView rvTuition;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_student_tuition, container, false);
        rvTuition = root.findViewById(R.id.rv_tuition);
        rvTuition.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        rvTuition.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        clockViewModel = ViewModelProviders.of(this).get(ClockViewModel.class);

        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);

        reportViewModel.getStudentCost(StudentActivity.studentId).observe(this, costs -> {

            List<StudentMonthCost> monthCosts = new ArrayList<>();
            if (costs != null) {
                Map<String, StudentMonthCost> map = new HashMap<>();
                for (StudentMonthCost cost : costs) {
                    String key = cost.getYear() + "-" + cost.getMonth();
                    StudentMonthCost exist;
                    if ((exist = map.get(key)) != null) {
                        exist.setDiscountPrice(exist.getDiscountPrice() + cost.getDiscountPrice());
                        exist.setPrice(exist.getPrice() + cost.getDiscountPrice());
                        map.put(key, exist);
                    } else {
                        map.put(key, cost);
                    }
                }
                Iterator<Map.Entry<String, StudentMonthCost>> entries = map.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry<String, StudentMonthCost> entry = entries.next();
                    monthCosts.add(entry.getValue());
                }
            }
            CostStatisticsAdapter adapter = new CostStatisticsAdapter(monthCosts);
            adapter.setOnItemClickListener(this);
            rvTuition.setAdapter(adapter);
        });
        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            default:
                break;
        }
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        StudentMonthCost cost = (StudentMonthCost) adapter.getData().get(position);
        Intent intent = new Intent(getContext(), StudentCostDetailActivity.class);
        intent.putExtra("studentId", cost.getStudentId());
        intent.putExtra("year", cost.getYear());
        intent.putExtra("month", cost.getMonth());
        startActivity(intent);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.top_nav_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showMonthPicker();
        return super.onOptionsItemSelected(item);
    }


    private void showMonthPicker() {
        View dlgView = LayoutInflater.from(getContext()).inflate(R.layout.dlg_day_picker, null);
        NumberPicker npYear = dlgView.findViewById(R.id.picker_year);
        NumberPicker npMonth = dlgView.findViewById(R.id.picker_month);
        Calendar cal = Calendar.getInstance();
        npMonth.setMinValue(1);
        npMonth.setMaxValue(12);
        npMonth.setValue(cal.get(Calendar.MONTH) + 1);
        npMonth.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npMonth.setWrapSelectorWheel(false);
        int year = cal.get(Calendar.YEAR);
        npYear.setMinValue(2019);
        npYear.setMaxValue(2099);
        npYear.setValue(year);
        npYear.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npYear.setWrapSelectorWheel(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setTitle(R.string.create_curriculum_cost)
                .setView(dlgView)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    int monthValue = npMonth.getValue();
                    int yearValue = npYear.getValue();
                    StudentMonthCost studentMonthCost = reportViewModel.queryStudentMonthCost(StudentActivity.studentId, yearValue, monthValue, 0);
                    if (studentMonthCost == null) {
                        StudentMonthCost monthCost = createMonthCost(yearValue, monthValue);
                        if (monthCost != null) {
                            reportViewModel.insert(monthCost);
                        } else {
                            Toast.makeText(getContext(), getString(R.string.student_not_clock_record,
                                    yearValue + "-" + monthValue), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity())
                                .setTitle(R.string.tip)
                                .setMessage(getString(R.string.month_curriculum_exists, yearValue + "-" + monthValue))
                                .setNegativeButton(android.R.string.cancel, null)
                                .setPositiveButton(android.R.string.ok, (dialogInterface2, i2) -> {
                                    StudentMonthCost monthCost = createMonthCost(yearValue, monthValue);
                                    monthCost.setId(studentMonthCost.getId());
                                    if (monthCost != null) {
                                        reportViewModel.insert(monthCost);
                                    } else {
                                        Toast.makeText(getContext(), getString(R.string.student_not_clock_record,
                                                yearValue + "-" + monthValue), Toast.LENGTH_SHORT).show();
                                    }
                                });
                        builder2.create().show();
                    }
                });
        builder.create().show();
    }


    private StudentMonthCost createMonthCost(int yearValue, int monthValue) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, yearValue);
        calendar.set(Calendar.MONTH, monthValue - 1);
        int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, lastDay);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date to = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, firstDay);
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        Date from = calendar.getTime();
        List<ClockRecord> list = clockViewModel.getStudentClockRecordList(StudentActivity.studentId, from, to);
        if (list != null && !list.isEmpty()) {
            StudentMonthCost monthCost = new StudentMonthCost();
            monthCost.setStudentId(StudentActivity.studentId);
            monthCost.setCostType(0);
            monthCost.setCostName(getString(R.string.curriculum_cost));
            monthCost.setYear(calendar.get(Calendar.YEAR));
            monthCost.setMonth(calendar.get(Calendar.MONTH) + 1);
            double price = 0;
            double discountPrice = 0;
            for (ClockRecord record : list) {
                price = price + record.getCurriculumPrice();
                discountPrice = discountPrice + record.getCurriculumDiscountPrice();
            }
            monthCost.setPrice(price);
            monthCost.setDiscountPrice(discountPrice);
            return monthCost;
        }
        return null;
    }

}