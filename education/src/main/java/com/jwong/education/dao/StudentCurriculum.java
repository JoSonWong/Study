package com.jwong.education.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class StudentCurriculum extends BaseDao {

    @Id(autoincrement = true)
    private Long id;//课程id，自增

    private Long studentId;//学生id

    @ToOne(joinProperty = "studentId")
    private Student student;

    private Long curriculumId;//课程id

    @ToOne(joinProperty = "curriculumId")
    private Curriculum curriculum;

    private Double discountPrice;//折后价

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 643420253)
    private transient StudentCurriculumDao myDao;

    @Generated(hash = 1554683346)
    public StudentCurriculum(Long id, Long studentId, Long curriculumId,
            Double discountPrice) {
        this.id = id;
        this.studentId = studentId;
        this.curriculumId = curriculumId;
        this.discountPrice = discountPrice;
    }

    @Generated(hash = 1025845966)
    public StudentCurriculum() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return this.studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCurriculumId() {
        return this.curriculumId;
    }

    public void setCurriculumId(Long curriculumId) {
        this.curriculumId = curriculumId;
    }

    public Double getDiscountPrice() {
        return this.discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }

    @Generated(hash = 79695740)
    private transient Long student__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 313494093)
    public Student getStudent() {
        Long __key = this.studentId;
        if (student__resolvedKey == null || !student__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            StudentDao targetDao = daoSession.getStudentDao();
            Student studentNew = targetDao.load(__key);
            synchronized (this) {
                student = studentNew;
                student__resolvedKey = __key;
            }
        }
        return student;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 490304967)
    public void setStudent(Student student) {
        synchronized (this) {
            this.student = student;
            studentId = student == null ? null : student.getId();
            student__resolvedKey = studentId;
        }
    }

    @Generated(hash = 2017217984)
    private transient Long curriculum__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 53848635)
    public Curriculum getCurriculum() {
        Long __key = this.curriculumId;
        if (curriculum__resolvedKey == null
                || !curriculum__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CurriculumDao targetDao = daoSession.getCurriculumDao();
            Curriculum curriculumNew = targetDao.load(__key);
            synchronized (this) {
                curriculum = curriculumNew;
                curriculum__resolvedKey = __key;
            }
        }
        return curriculum;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1870035091)
    public void setCurriculum(Curriculum curriculum) {
        synchronized (this) {
            this.curriculum = curriculum;
            curriculumId = curriculum == null ? null : curriculum.getId();
            curriculum__resolvedKey = curriculumId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 982031152)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getStudentCurriculumDao() : null;
    }

}
