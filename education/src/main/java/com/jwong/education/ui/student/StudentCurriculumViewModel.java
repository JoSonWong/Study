package com.jwong.education.ui.student;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jwong.education.StudyApplication;
import com.jwong.education.dao.StudentCurriculum;
import com.jwong.education.db.StudentCurriculumDbService;

import java.util.List;

public class StudentCurriculumViewModel extends ViewModel {

    private MutableLiveData<List<StudentCurriculum>> data;

    public StudentCurriculumViewModel() {
        data = new MutableLiveData<>();
    }

    public LiveData<List<StudentCurriculum>> getStudentCurriculumList(long studentId) {
        data.postValue(StudentCurriculumDbService.getInstance(StudyApplication.getDbController()).searchByWhere(studentId));
        return data;
    }

    public void addStudent(StudentCurriculum studentCurriculum) {
        StudentCurriculumDbService.getInstance(StudyApplication.getDbController()).insert(studentCurriculum);
        data.postValue(StudentCurriculumDbService.getInstance(StudyApplication.getDbController()).searchByWhere(studentCurriculum.getStudentId()));
    }

    public void updateStudent(StudentCurriculum studentCurriculum) {
        StudentCurriculumDbService.getInstance(StudyApplication.getDbController()).update(studentCurriculum);
        data.postValue(StudentCurriculumDbService.getInstance(StudyApplication.getDbController()).searchByWhere(studentCurriculum.getStudentId()));
    }
}