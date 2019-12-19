package com.jwong.education.ui.curriculum;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jwong.education.StudyApplication;
import com.jwong.education.dao.Curriculum;
import com.jwong.education.db.CurriculumDbService;

import java.util.Date;
import java.util.List;

public class CurriculumViewModel extends ViewModel {

    private MutableLiveData<List<Curriculum>> curriculumList;

    public CurriculumViewModel() {
        curriculumList = new MutableLiveData<>();
    }

    public LiveData<List<Curriculum>> getCurriculumList() {
        curriculumList.postValue(CurriculumDbService.getInstance(StudyApplication.getDbController()).searchAll());
        return curriculumList;
    }

    public String updateCurriculum(Curriculum curriculum) {
        if (CurriculumDbService.getInstance(StudyApplication.getDbController()).update(curriculum) > 0) {
            curriculumList.postValue(CurriculumDbService.getInstance(StudyApplication.getDbController()).searchAll());
            return "修改成功！";
        }
        return "修改失败！";
    }

    public String addCurriculum(String name, double price) {
        List<Curriculum> list = CurriculumDbService.getInstance(StudyApplication.getDbController()).searchByWhere(name);
        if (list != null && !list.isEmpty()) {
            return "课程：" + name + "已存在！";
        } else {
            Curriculum curriculum = new Curriculum();
            curriculum.setName(name);
            curriculum.setPrice(price);
            curriculum.setCreatedAt(new Date());
            curriculum.setUpdatedAt(new Date());
            curriculum.setDeletedAt(null);
            if (CurriculumDbService.getInstance(StudyApplication.getDbController()).insert(curriculum) > 0) {
                curriculumList.postValue(CurriculumDbService.getInstance(StudyApplication.getDbController()).searchAll());
                return "添加成功！";
            } else {
                return "添加失败！";
            }
        }
    }
}