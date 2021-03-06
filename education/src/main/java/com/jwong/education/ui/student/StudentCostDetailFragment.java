package com.jwong.education.ui.student;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
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
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jwong.education.R;
import com.jwong.education.dao.StudentMonthCost;
import com.jwong.education.dto.CostNotificationDTO;
import com.jwong.education.dto.CostNotificationItemDTO;
import com.jwong.education.dto.entity.CostDetailNode;
import com.jwong.education.dto.entity.CostNode;
import com.jwong.education.ui.report.ReportViewModel;
import com.jwong.education.util.FormatUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StudentCostDetailFragment extends Fragment implements OnItemClickListener {

    private ReportViewModel reportViewModel;
    private StudentViewModel studentViewModel;
    private RecyclerView rvCostDetail;
    private TextView tvStudentName, tvStudentId, tvGrade, tvYearMonth, tvTotalCost;
    private int year, month;
    private CostTreeAdapter adapter = new CostTreeAdapter();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_student_cost_detail, container, false);
        tvTotalCost = root.findViewById(R.id.tv_cost);
        tvStudentName = root.findViewById(R.id.tv_name);
        tvStudentId = root.findViewById(R.id.tv_student_id);
        tvGrade = root.findViewById(R.id.tv_grade);
        tvYearMonth = root.findViewById(R.id.tv_month);
        rvCostDetail = root.findViewById(R.id.rv_cost);
        rvCostDetail.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        rvCostDetail.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        rvCostDetail.setAdapter(adapter);

        Calendar calendar = Calendar.getInstance();
        setMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);

        studentViewModel = ViewModelProviders.of(this).get(StudentViewModel.class);
        studentViewModel.getStudent(StudentActivity.studentId).observe(this, student -> {
            tvStudentName.setText(student.getName());
            tvStudentId.setText(getString(R.string.student_code_x, FormatUtils.studentCodeFormat(student.getId())));
            tvGrade.setText(student.getCurrentGrade());
        });
        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);
        reportViewModel.getStudentCost(StudentActivity.studentId, this.year, this.month)
                .observe(this, costs -> setCostInfo(costs));
        return root;
    }

    private void setMonth(int year, int month) {
        this.year = year;
        this.month = month;
        tvYearMonth.setText(getString(R.string.year_x_month_x, this.year, this.month));
    }


    private void setCostInfo(List<CostNode> nodes) {
        double totalCost = 0;
        List<BaseNode> baseNodes = new ArrayList<>();
        if (nodes != null && !nodes.isEmpty()) {
            for (CostNode node : nodes) {
                totalCost += node.getDiscountPrice();
                baseNodes.add(node);
            }
        }
        adapter.setNewData(baseNodes);
        adapter.setOnItemClickListener(this);
        tvTotalCost.setText(getString(R.string.rmb_x, FormatUtils.priceFormat(totalCost)));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.student_cost_detail_top_nav_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                showInput();
                break;
            case R.id.action_month:
                showMonthPicker();
                break;
            case R.id.action_share:
                Intent intent = new Intent(getContext(), StudentCostNotificationActivity.class);
                List<CostNotificationItemDTO> costList = new ArrayList<>();
                List<BaseNode> baseNodes = adapter.getData();
                boolean hasCurriculumCost = false;//有课时费？
                for (BaseNode baseNode : baseNodes) {
                    if (baseNode instanceof CostNode) {
                        CostNode costNode = (CostNode) baseNode;
                        if (costNode.getChildNode() == null || costNode.getChildNode().isEmpty()) {
                            costList.add(new CostNotificationItemDTO(costNode.getCostName(), costNode.getPrice() + "",
                                    costNode.getDiscountPrice() + "", 0, costNode.getDiscountPrice()));
                        }
                    } else if (baseNode instanceof CostDetailNode) {
                        CostDetailNode costDetailNode = (CostDetailNode) baseNode;
                        if (costDetailNode.getDiscountPrice() > 0 && costDetailNode.getCount() > 0) {
                            hasCurriculumCost = true;
                            costList.add(new CostNotificationItemDTO(costDetailNode.getCurriculumName(), costDetailNode.getPrice() + "",
                                    costDetailNode.getDiscountPrice() + "", costDetailNode.getCount(),
                                    costDetailNode.getCount() * costDetailNode.getDiscountPrice()));
                        }
                    }
                }
                CostNotificationDTO notificationDTO = new CostNotificationDTO(StudentActivity.studentId, tvStudentName.getText().toString(),
                        tvGrade.getText().toString(), tvYearMonth.getText().toString(), costList);
                notificationDTO.setHasCurriculumCost(hasCurriculumCost);
                intent.putExtra("cost_notification", notificationDTO);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter.getData().get(position) instanceof CostNode) {
            CostNode costNode = (CostNode) adapter.getData().get(position);
            View dlgView = LayoutInflater.from(getContext()).inflate(R.layout.dlg_input_cost, null);
            EditText etName = dlgView.findViewById(R.id.et_name);
            etName.setText(costNode.getCostName());
            etName.setEnabled(costNode.getCostType() != 0);
            EditText etPrice = dlgView.findViewById(R.id.et_price);
            etPrice.setText(FormatUtils.priceFormat(costNode.getPrice()));
            etPrice.setEnabled(costNode.getCostType() != 0);
            EditText etDiscountPrice = dlgView.findViewById(R.id.et_discount_price);
            etDiscountPrice.setText(FormatUtils.priceFormat(costNode.getDiscountPrice()));
            etDiscountPrice.setEnabled(costNode.getCostType() != 0);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.update_cost)
                    .setView(dlgView)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                        if (!TextUtils.isEmpty(etName.getText()) && (!TextUtils.isEmpty(etPrice.getText())
                                || !TextUtils.isEmpty(etDiscountPrice.getText()))) {
                            StudentMonthCost monthCost = new StudentMonthCost(costNode.getCostId(), costNode.getStudentId(), costNode.getYear(),
                                    costNode.getMonth(), costNode.getCostType(), costNode.getCostName(), costNode.getPrice(), costNode.getDiscountPrice());
                            monthCost.setCostName(etName.getText().toString());
                            if (TextUtils.isEmpty(etDiscountPrice.getText())) {
                                double price = Double.parseDouble(etPrice.getText().toString());
                                monthCost.setPrice(price);
                                monthCost.setDiscountPrice(price);
                            } else if (TextUtils.isEmpty(etPrice.getText())) {
                                double discountPrice = Double.parseDouble(etDiscountPrice.getText().toString());
                                monthCost.setPrice(discountPrice);
                                monthCost.setDiscountPrice(discountPrice);
                            } else {
                                double price = Double.parseDouble(etPrice.getText().toString());
                                monthCost.setPrice(price);
                                double discountPrice = Double.parseDouble(etDiscountPrice.getText().toString());
                                monthCost.setDiscountPrice(discountPrice);
                            }
                            reportViewModel.update(monthCost);
                            Toast.makeText(getContext(), R.string.update_cost_success, Toast.LENGTH_SHORT).show();
                            reportViewModel.getStudentCost(StudentActivity.studentId, this.year, this.month);
                        } else {
                            Toast.makeText(getContext(), TextUtils.isEmpty(etName.getText()) ? R.string.pls_input_cost_name
                                    : R.string.pls_input_cost, Toast.LENGTH_SHORT).show();
                        }
                    });
            builder.create().show();
        } else if (adapter.getData().get(position) instanceof CostDetailNode) {
            Log.d(getClass().getSimpleName(), "onItemClick >>> " + position);

        }
    }


    private void showInput() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dlg_input_cost, null);
        EditText etName = view.findViewById(R.id.et_name);
        EditText etPrice = view.findViewById(R.id.et_price);
        EditText etDiscountPrice = view.findViewById(R.id.et_discount_price);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setTitle(R.string.add_cost)
                .setView(view)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    if (!TextUtils.isEmpty(etName.getText()) && (!TextUtils.isEmpty(etPrice.getText())
                            || !TextUtils.isEmpty(etDiscountPrice.getText()))) {
                        StudentMonthCost cost = new StudentMonthCost();
                        cost.setStudentId(StudentActivity.studentId);
                        cost.setYear(this.year);
                        cost.setMonth(this.month);
                        cost.setCostType(1);
                        cost.setCostName(etName.getText().toString());
                        if (TextUtils.isEmpty(etDiscountPrice.getText())) {
                            double price = Double.parseDouble(etPrice.getText().toString());
                            cost.setPrice(price);
                            cost.setDiscountPrice(price);
                        } else if (TextUtils.isEmpty(etPrice.getText())) {
                            double discountPrice = Double.parseDouble(etDiscountPrice.getText().toString());
                            cost.setPrice(discountPrice);
                            cost.setDiscountPrice(discountPrice);
                        } else {
                            double price = Double.parseDouble(etPrice.getText().toString());
                            cost.setPrice(price);
                            double discountPrice = Double.parseDouble(etDiscountPrice.getText().toString());
                            cost.setDiscountPrice(discountPrice);
                        }
                        reportViewModel.insert(cost);
                        Toast.makeText(getActivity(), R.string.add_cost_success, Toast.LENGTH_SHORT).show();
                        reportViewModel.getStudentCost(StudentActivity.studentId, this.year, this.month);
                    } else {
                        Toast.makeText(getContext(), TextUtils.isEmpty(etName.getText()) ? R.string.pls_input_cost_name
                                : R.string.pls_input_cost, Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show();
    }


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
                    reportViewModel.getStudentCost(StudentActivity.studentId, this.year, this.month);
                });
        builder.create().show();
    }
}