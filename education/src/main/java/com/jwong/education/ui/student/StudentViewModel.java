package com.jwong.education.ui.student;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jwong.education.StudyApplication;
import com.jwong.education.dao.Student;
import com.jwong.education.db.StudentDbService;

import java.util.List;

public class StudentViewModel extends ViewModel {

    private MutableLiveData<List<Student>> data;
    private MutableLiveData<Student> studentData;

    public StudentViewModel() {
        data = new MutableLiveData<>();
        studentData = new MutableLiveData<>();
    }

    public LiveData<List<Student>> getStudentList() {
        data.postValue(StudentDbService.getInstance(StudyApplication.getDbController()).searchAll());
        return data;
    }

    public LiveData<Student> getStudent(long id) {
        studentData.postValue(StudentDbService.getInstance(StudyApplication.getDbController()).searchById(id));
        return studentData;
    }

    public void addStudent(Student student) {
        StudentDbService.getInstance(StudyApplication.getDbController()).insert(student);
        data.postValue(StudentDbService.getInstance(StudyApplication.getDbController()).searchAll());
    }

    public void updateStudent(Student student) {
        StudentDbService.getInstance(StudyApplication.getDbController()).update(student);
        data.postValue(StudentDbService.getInstance(StudyApplication.getDbController()).searchAll());
    }
}