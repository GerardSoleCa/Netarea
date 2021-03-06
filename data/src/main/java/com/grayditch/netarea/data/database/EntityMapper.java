package com.grayditch.netarea.data.database;

import com.grayditch.netarea.data.database.dao.MarkDAO;
import com.grayditch.netarea.data.database.dao.SubjectDAO;
import com.grayditch.netarea.domain.Mark;
import com.grayditch.netarea.domain.Subject;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

/**
 * Created by gerard on 20/04/16.
 */
class EntityMapper {

    static List<Subject> transform(List<SubjectDAO> subjectDAOs) {
        List<Subject> subjects = new ArrayList<>();
        for (SubjectDAO subjectDAO : subjectDAOs) {
            Subject subject = EntityMapper.transform(subjectDAO);
            subjects.add(subject);
        }
        return subjects;
    }

    private static Subject transform(SubjectDAO subjectDAO) {
        List<MarkDAO> markDAOs = subjectDAO.getMarks();
        List<Mark> marks = new ArrayList<>();
        for (MarkDAO markDao : markDAOs) {
            marks.add(EntityMapper.transform(markDao));
        }
        return new Subject(subjectDAO.getName(), marks);
    }

    private static Mark transform(MarkDAO markDAO) {
        Mark mark = new Mark();
        mark.setNew(markDAO.isNew());
        mark.setDescription(markDAO.getDescription());
        mark.setPercentage(markDAO.getPercentage());
        mark.setMark(markDAO.getMark());
        return mark;
    }

    static List<SubjectDAO> transformToDao(List<Subject> subjects) {
        List<SubjectDAO> subjectDAOs = new ArrayList<>();
        for (Subject subject : subjects) {
            SubjectDAO subjectDAO = EntityMapper.transformSubjectToDao(subject);
            subjectDAOs.add(subjectDAO);
        }
        return subjectDAOs;
    }

    private static SubjectDAO transformSubjectToDao(Subject subject) {
        List<Mark> marks = subject.getMarks();
        RealmList<MarkDAO> markDAOs = new RealmList<>();
        for (Mark mark : marks) {
            markDAOs.add(EntityMapper.transformMarkToDao(subject, mark));
        }
        return new SubjectDAO(subject.getName(), markDAOs);
    }

    private static MarkDAO transformMarkToDao(Subject subject, Mark mark) {
        MarkDAO markDAO = new MarkDAO();
        markDAO.setId(subject.getName(), mark.getDescription());
        markDAO.setNew(mark.isNew());
        markDAO.setDescription(mark.getDescription());
        markDAO.setPercentage(mark.getPercentage());
        markDAO.setMark(mark.getMark());
        return markDAO;
    }
}
