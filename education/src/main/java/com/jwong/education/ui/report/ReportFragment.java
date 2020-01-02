package com.jwong.education.ui.report;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.jwong.education.R;
import com.jwong.education.ui.clock.ClockViewModel;
import com.jwong.education.util.FormatUtils;
import com.jwong.education.util.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class ReportFragment extends Fragment {

    private ReportViewModel reportViewModel;
    private ClockViewModel clockViewModel;
    private TextView tvMonth, tvTotalIncome, tvCurriculumIncome;
    private PieChart pieChart, pieChartCurriculum;
    private int year, month;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_report, container, false);
        tvMonth = root.findViewById(R.id.tv_month);
        tvTotalIncome = root.findViewById(R.id.tv_total);
        tvCurriculumIncome = root.findViewById(R.id.tv_curriculum);
        pieChart = root.findViewById(R.id.pie_chart);
        pieChartCurriculum = root.findViewById(R.id.pie_chart_curriculum);
        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);
        clockViewModel = ViewModelProviders.of(this).get(ClockViewModel.class);
        Calendar cal = Calendar.getInstance();
        setMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
        reportViewModel.getDateCost(this.year, this.month).observe(this, costs ->
                drawIncome(costs, pieChart, getString(R.string.total_income), R.string.total_income_x, tvTotalIncome));
        clockViewModel.getDateCost(
                Utils.getYearMonthFirstDate(this.year, this.month), Utils.getYearMonthLastDate(this.year, this.month))
                .observe(this, map ->
                        drawIncome(map, pieChartCurriculum, getString(R.string.curriculum_income),
                                R.string.curriculum_income_x, tvCurriculumIncome));
        return root;
    }

    private void setMonth(int year, int month) {
        this.year = year;
        this.month = month;
        tvMonth.setText(getString(R.string.year_x_month_x, year, month));
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, @NonNull MenuInflater inflater) {
        MenuItem moreItem = menu.add(Menu.NONE, Menu.FIRST, Menu.FIRST, null);
        moreItem.setIcon(R.drawable.ic_time_white_24dp);
        moreItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(getClass().getSimpleName(), "onOptionsItemSelected item:" + item.getItemId());
        showMonthPicker();
        return super.onOptionsItemSelected(item);
    }

    private void drawIncome(Map<String, Double> mapCost, PieChart pieChart, String title,
                            int strResId, TextView textView) {
        //饼状图
        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 5, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(1f);
        //设置中间文件
        pieChart.setCenterText(title);
        pieChart.setCenterTextSize(20);
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
        pieChart.setOnChartValueSelectedListener(onChartValueSelectedListener);
        //模拟数据
        double total = 0;
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Double> entry : mapCost.entrySet()) {
            total = FormatUtils.doubleFormat(total + entry.getValue());
            Log.d(getClass().getSimpleName(), "费用名称：" + entry.getKey()
                    + " 费用：" + entry.getValue().floatValue());
            entries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
        }
        //设置数据
        textView.setText(getString(strResId, FormatUtils.priceFormat(total)));
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
        pieChart.setEntryLabelTextSize(12f);
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
        data.setValueTextSize(14f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.highlightValues(null);
        //刷新
        pieChart.invalidate();
    }

    private OnChartValueSelectedListener onChartValueSelectedListener = new OnChartValueSelectedListener() {
        @Override
        public void onValueSelected(Entry e, Highlight h) {

        }

        @Override
        public void onNothingSelected() {

        }
    };


    private void showMonthPicker() {
        View dlgView = LayoutInflater.from(getContext()).inflate(R.layout.dlg_day_picker, null);
        NumberPicker npYear = dlgView.findViewById(R.id.picker_year);
        int[] years = getResources().getIntArray(R.array.year_min_max);
        npYear.setMinValue(years[0]);
        npYear.setMaxValue(years[1]);
        npYear.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npYear.setWrapSelectorWheel(true);

        NumberPicker npMonth = dlgView.findViewById(R.id.picker_month);
        int[] months = getResources().getIntArray(R.array.month_min_max);
        npMonth.setMinValue(months[0]);
        npMonth.setMaxValue(months[1]);
        npMonth.setWrapSelectorWheel(true);
        npMonth.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        Calendar cal = Calendar.getInstance();
        Date textMonth = FormatUtils.convert2Month(getString(R.string.year_x_month_x, this.year, this.month));
        if (textMonth != null) {
            cal.setTime(textMonth);
        }
        npYear.setValue(cal.get(Calendar.YEAR));
        npMonth.setValue(cal.get(Calendar.MONTH) + 1);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.other_month_cost)
                .setView(dlgView)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    setMonth(npYear.getValue(), npMonth.getValue());
                    reportViewModel.getDateCost(this.year, this.month);
                    clockViewModel.getDateCost(
                            Utils.getYearMonthFirstDate(this.year, this.month), Utils.getYearMonthLastDate(this.year, this.month));
                });
        builder.create().show();
    }
}