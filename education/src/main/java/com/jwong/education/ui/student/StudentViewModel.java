package com.jwong.education.ui.student;

import android.util.Log;
import android.util.LongSparseArray;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.chad.library.adapter.base.entity.node.BaseNode;
import com.jwong.education.StudyApplication;
import com.jwong.education.dao.ClockRecord;
import com.jwong.education.dao.Student;
import com.jwong.education.dao.StudentMonthCost;
import com.jwong.education.db.ClockDbService;
import com.jwong.education.db.MonthCostDbService;
import com.jwong.education.db.StudentDbService;
import com.jwong.education.dto.entity.CostDetailNode;
import com.jwong.education.dto.entity.CostNode;
import com.jwong.education.dto.entity.StudentDetailNode;
import com.jwong.education.dto.entity.StudentNode;
import com.jwong.education.util.FormatUtils;
import com.jwong.education.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StudentViewModel extends ViewModel {

    private MutableLiveData<List<Student>> data;
    private MutableLiveData<Student> studentData;
    private MutableLiveData<List<BaseNode>> dataStudentNode;


    public StudentViewModel() {
        data = new MutableLiveData<>();
        studentData = new MutableLiveData<>();
        dataStudentNode = new MutableLiveData<>();
    }

    public LiveData<List<Student>> getStudentList(int type) {
        data.postValue(StudentDbService.getInstance(StudyApplication.getDbController()).searchByType(type));
        return data;
    }

    public LiveData<Student> getStudent(long id) {
        studentData.postValue(StudentDbService.getInstance(StudyApplication.getDbController()).searchById(id));
        return studentData;
    }

    public void insert(Student student) {
        StudentDbService.getInstance(StudyApplication.getDbController()).insert(student);
        data.postValue(StudentDbService.getInstance(StudyApplication.getDbController()).searchByType(student.getStudentType()));
    }

    public void update(Student student) {
        StudentDbService.getInstance(StudyApplication.getDbController()).update(student);
        data.postValue(StudentDbService.getInstance(StudyApplication.getDbController()).searchByType(student.getStudentType()));
    }


    public LiveData<List<BaseNode>> getStudentGroup() {
        List<BaseNode> list = new ArrayList<>();
        List<Student> students = StudentDbService.getInstance(StudyApplication.getDbController()).searchByType(-1);
        Map<String, List<BaseNode>> map = new HashMap<>();
        for (Student student : students) {
            if (map.get(student.getStudentTypeName()) != null) {
                StudentDetailNode detailNode = new StudentDetailNode(student.getId(), student.getName(),
                        student.getAvatar(), student.getSex(), student.getCurrentGrade());
                map.get(student.getStudentTypeName()).add(detailNode);
            } else {
                List<BaseNode> list1 = new ArrayList<>();
                list1.add(new StudentDetailNode(student.getId(), student.getName(), student.getAvatar(),
                        student.getSex(), student.getCurrentGrade()));
                map.put(student.getStudentTypeName(), list1);
            }
        }
        for (Map.Entry<String, List<BaseNode>> set : map.entrySet()) {
            StudentNode studentNode = new StudentNode(set.getValue(), 0, set.getKey());
            list.add(studentNode);
        }
        dataStudentNode.postValue(list);
        return dataStudentNode;
    }

}