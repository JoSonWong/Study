package com.jwong.education.ui.clock;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jwong.education.StudyApplication;
import com.jwong.education.dao.Curriculum;
import com.jwong.education.db.DbController;

import java.util.Date;
import java.util.List;

public class ClockViewModel extends ViewModel {

    private MutableLiveData<List<Curriculum>> curriculumList;

    public ClockViewModel() {
        this.curriculumList = new MutableLiveData<>();
        this.curriculumList.postValue(queryCurriculum());
    }

    public LiveData<List<Curriculum>> getCurriculumList() {
        return curriculumList;
    }

    public void addCurriculum() {
        Curriculum curriculum = new Curriculum();
        curriculum.setName("数学");
        curriculum.setPrice(130);
        curriculum.setRemarks("");
        curriculum.setCreatedAt(new Date());
        curriculum.setUpdatedAt(new Date());
        curriculum.setDeletedAt(null);
        StudyApplication.getDbController().insert(curriculum);
    }

    public List<Curriculum> queryCurriculum() {
        return StudyApplication.getDbController().searchAll();
    }
}