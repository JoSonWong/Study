package com.jwong.education.ui.student;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.jwong.education.R;
import com.jwong.education.dao.ClockRecord;
import com.jwong.education.ui.clock.ClockRecordAdapter;
import com.jwong.education.ui.clock.ClockViewModel;
import com.jwong.education.util.FormatUtils;
import com.jwong.education.util.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class StudentClockFragment extends Fragment implements OnItemClickListener {

    private ClockViewModel clockViewModel;
    private TextView tvMonth, tvTotal;
    private PieChart pieChart;
    private RecyclerView rvClockHistory;
    private int year, month;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_student_clock, container, false);
        tvMonth = root.findViewById(R.id.tv_month);
        tvTotal = root.findViewById(R.id.tv_total);
        pieChart = root.findViewById(R.id.pie_chart);
        rvClockHistory = root.findViewById(R.id.rv_clock_record);
        rvClockHistory.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        rvClockHistory.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        Calendar cal = Calendar.getInstance();
        this.year = cal.get(Calendar.YEAR);
        this.month = cal.get(Calendar.MONTH) + 1;
        tvMonth.setText(getString(R.string.year_x_month_x, year, month));

        clockViewModel = ViewModelProviders.of(this).get(ClockViewModel.class);
        clockViewModel.getStudentAllClockRecordList(StudentActivity.studentId,
                Utils.getYearMonthFirstDate(this.year, this.month),
                Utils.getYearMonthLastDate(this.year, this.month))
                .observe(this, clockRecords -> {
                    if (clockRecords != null) {
                        ClockRecordAdapter adapter = new ClockRecordAdapter(clockRecords, true);
                        adapter.setOnItemClickListener(this);
                        rvClockHistory.setAdapter(adapter);
                    }
                });
        clockViewModel.getStudentCurriculumStatistic(StudentActivity.studentId,
                Utils.getYearMonthFirstDate(year, month), Utils.getYearMonthLastDate(year, month))
                .observe(this, map -> drawCurriculumStatistic(map));
        return root;
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ClockRecord clockRecord = (ClockRecord) adapter.getData().get(position);
        View viewInput = LayoutInflater.from(getContext()).inflate(R.layout.dlg_clock_record_detail, null);
        TextView tvStudentId = viewInput.findViewById(R.id.tv_student_id);
        tvStudentId.setText(getString(R.string.student_code_x,
                FormatUtils.studentCodeFormat(clockRecord.getStudentId())));
        TextView tvStudentName = viewInput.findViewById(R.id.tv_student_name);
        tvStudentName.setText(getString(R.string.student_name_x, clockRecord.getStudentName()));
        TextView tvCurriculumId = viewInput.findViewById(R.id.tv_curriculum_id);
        tvCurriculumId.setText(getString(R.string.curriculum_id_x, clockRecord.getCurriculumId() + ""));
        TextView tvCurriculumName = viewInput.findViewById(R.id.tv_curriculum_name);
        tvCurriculumName.setText(getString(R.string.curriculum_name_x, clockRecord.getCurriculumName()));
        TextView tvCurriculumPrice = viewInput.findViewById(R.id.tv_curriculum_price);
        tvCurriculumPrice.setText(getString(R.string.curriculum_price_x,
                FormatUtils.priceFormat(clockRecord.getCurriculumPrice())));
        EditText etDiscountPrice = viewInput.findViewById(R.id.et_discount_price);
        etDiscountPrice.setText(FormatUtils.priceFormat(clockRecord.getCurriculumDiscountPrice()));
        RadioGroup radioGroup = viewInput.findViewById(R.id.rg_clock_type);
        radioGroup.check(clockRecord.getClockType() == 1 ? R.id.rb_adjustment
                : (clockRecord.getClockType() == 2 ? R.id.rb_leave : R.id.rb_normal));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.student_clock_detail)
                .setView(viewInput)
                .setNegativeButton(android.R.string.cancel, null)
                .setNeutralButton(R.string.delete_record, (dialogInterface, i) -> {
                    clockViewModel.delete(clockRecord.getId());
                    clockViewModel.getStudentAllClockRecordList(StudentActivity.studentId,
                            Utils.getYearMonthFirstDate(this.year, this.month),
                            Utils.getYearMonthLastDate(this.year, this.month));

                    clockViewModel.getStudentCurriculumStatistic(StudentActivity.studentId,
                            Utils.getYearMonthFirstDate(year, month), Utils.getYearMonthLastDate(year, month));
                })
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    if (!TextUtils.isEmpty(etDiscountPrice.getText())) {
                        int id = radioGroup.getCheckedRadioButtonId();
                        int clockType = 0;
                        if (id == R.id.rb_adjustment) {
                            clockType = 1;
                        } else if (id == R.id.rb_leave) {
                            clockType = 2;
                        }
                        clockRecord.setClockType(clockType);
                        clockRecord.setCurriculumDiscountPrice(Double.parseDouble(etDiscountPrice.getText().toString()));
                        long logId = clockViewModel.update(clockRecord);
                        Toast.makeText(getContext(), logId > 0 ? R.string.update_success : R.string.update_fail
                                , Toast.LENGTH_SHORT).show();
                        if (logId > 0) {
                            clockViewModel.getStudentAllClockRecordList(StudentActivity.studentId,
                                    Utils.getYearMonthFirstDate(this.year, this.month),
                                    Utils.getYearMonthLastDate(this.year, this.month));

                            clockViewModel.getStudentCurriculumStatistic(StudentActivity.studentId,
                                    Utils.getYearMonthFirstDate(year, month), Utils.getYearMonthLastDate(year, month));
                        }
                    } else {
                        Toast.makeText(getContext(), R.string.pls_input_discount_price, Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.student_clock_top_nav_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_month:
                showMonthPicker();
                break;
            case R.id.action_statistic:

                break;
            case R.id.action_record:

                break;
            default:
                break;
        }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.other_month_cost)
                .setView(dlgView)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    this.year = npYear.getValue();
                    this.month = npMonth.getValue();
                    tvMonth.setText(getString(R.string.year_x_month_x, year, month));
                    clockViewModel.getStudentAllClockRecordList(StudentActivity.studentId,
                            Utils.getYearMonthFirstDate(year, month), Utils.getYearMonthLastDate(this.year, this.month));

                    clockViewModel.getStudentCurriculumStatistic(StudentActivity.studentId,
                            Utils.getYearMonthFirstDate(year, month), Utils.getYearMonthLastDate(year, month));
                });
        builder.create().show();
    }


    private void drawCurriculumStatistic(Map<String, Integer> mapCurriculum) {
        //饼状图
        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 5, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(1f);
        //设置中间文件
        pieChart.setCenterText("课时统计");
        pieChart.setCenterTextSize(22);
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(255);
        pieChart.setHoleRadius(50f);
        pieChart.setTransparentCircleRadius(50f);
        pieChart.setDrawCenterText(true);
        pieChart.setRotationAngle(0);
        // 触摸旋转
        pieChart.setRotationEnabled(false);
        pieChart.setHighlightPerTapEnabled(true);
        //变化监听
//            pieChart.setOnChartValueSelectedListener(thi);
        //模拟数据
        int total = 0;
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : mapCurriculum.entrySet()) {
            total = total + entry.getValue();
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }
        //设置数据
        tvTotal.setText(getString(R.string.total_curriculum_x, total));
        setData(entries, pieChart, "");
        pieChart.animateXY(800, 800);
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(10f);
        l.setYEntrySpace(10f);
        l.setYOffset(10f);

        // 输入标签样式
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(16f);
        //设置数据
    }

    private void setData(ArrayList<PieEntry> entries, PieChart pieChart, String label) {
        PieDataSet dataSet = new PieDataSet(entries, label);
        dataSet.setSliceSpace(5f);
        dataSet.setSelectionShift(5f);
        //数据和颜色
        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        dataSet.setDrawValues(true);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(16f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.highlightValues(null);
        //刷新
        pieChart.invalidate();
    }

}