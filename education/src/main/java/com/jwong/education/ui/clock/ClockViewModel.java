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
    }

    public LiveData<List<Curriculum>> getCurriculumList() {
        return curriculumList;
    }


}