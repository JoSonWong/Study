package com.jwong.education.ui.student;

import android.util.LongSparseArray;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jwong.education.StudyApplication;
import com.jwong.education.dao.Curriculum;
import com.jwong.education.dao.StudentCurriculum;
import com.jwong.education.db.StudentCurriculumDbService;
import com.jwong.education.dto.CurriculumDTO;

import java.util.List;

public class StudentCurriculumViewModel extends ViewModel {

    private MutableLiveData<List<StudentCurriculum>> data;

    public StudentCurriculumViewModel() {
        data = new MutableLiveData<>();
    }

    public LiveData<List<StudentCurriculum>> getStudentCurriculumList(long studentId) {
        data.postValue(getStudentCurriculum(studentId));
        return data;
    }

    public List<StudentCurriculum> getStudentCurriculum(long studentId) {
        return StudentCurriculumDbService.getInstance(StudyApplication.getDbController()).searchByWhere(studentId);
    }

    public void insert(long studentId, Curriculum curriculum) {
        StudentCurriculum studentCurriculum = new StudentCurriculum();
        studentCurriculum.setStudentId(studentId);
        studentCurriculum.setCurriculumId(curriculum.getId());
        studentCurriculum.setDiscountPrice(curriculum.getPrice());
        StudentCurriculumDbService.getInstance(StudyApplication.getDbController()).insert(studentCurriculum);
    }

    public void update(StudentCurriculum studentCurriculum) {
        StudentCurriculumDbService.getInstance(StudyApplication.getDbController()).update(studentCurriculum);
        data.postValue(StudentCurriculumDbService.getInstance(StudyApplication.getDbController()).searchByWhere(studentCurriculum.getStudentId()));
    }

    public void delete(StudentCurriculum studentCurriculum) {
        StudentCurriculumDbService.getInstance(StudyApplication.getDbController()).delete(studentCurriculum.getStudentId(), studentCurriculum.getCurriculumId());
    }

    public void handleSelectedCurriculum(long studentId, List<CurriculumDTO> curriculumDTOS) {
        List<StudentCurriculum> oldList = getStudentCurriculum(studentId);
        LongSparseArray<StudentCurriculum> oldSparse = new LongSparseArray();
        if (oldList != null && !oldList.isEmpty()) {
            for (StudentCurriculum item : oldList) {
                oldSparse.put(item.getCurriculumId(), item);
            }
        }
        LongSparseArray<Curriculum> selectedSparse = new LongSparseArray();
        if (curriculumDTOS != null && !curriculumDTOS.isEmpty()) {
            for (CurriculumDTO curriculumDTO : curriculumDTOS) {
                Curriculum curriculum = new Curriculum();
                curriculum.setId(curriculumDTO.getId());
                curriculum.setName(curriculumDTO.getName());
                curriculum.setPrice(curriculumDTO.getPrice());
                selectedSparse.put(curriculum.getId(), curriculum);
            }
        }
        LongSparseArray<Curriculum> insertSparseArray = new LongSparseArray();
        LongSparseArray<StudentCurriculum> deleteSparseArray = new LongSparseArray();
        for (int i = 0; i < selectedSparse.size(); i++) {
            Curriculum curriculum = selectedSparse.get(selectedSparse.keyAt(i));
            if (oldSparse.get(curriculum.getId()) == null) {
                insertSparseArray.put(curriculum.getId(), curriculum);
            }
        }

        for (int i = 0; i < oldSparse.size(); i++) {
            StudentCurriculum curriculum = oldSparse.get(oldSparse.keyAt(i));
            if (selectedSparse.get(curriculum.getCurriculumId()) == null) {
                deleteSparseArray.put(curriculum.getCurriculumId(), curriculum);
            }
        }

        if (deleteSparseArray.size() > 0) {
            for (int i = 0; i < deleteSparseArray.size(); i++) {
                delete(deleteSparseArray.get(deleteSparseArray.keyAt(i)));
            }
        }
        if (insertSparseArray.size() > 0) {
            for (int i = 0; i < insertSparseArray.size(); i++) {
                Curriculum curriculum = insertSparseArray.get(insertSparseArray.keyAt(i));
                insert(studentId, curriculum);
            }
        }
        data.postValue(StudentCurriculumDbService.getInstance(StudyApplication.getDbController()).searchByWhere(studentId));
    }
}