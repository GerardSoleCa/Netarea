package com.grayditch.netarea.data.comparators;

import com.grayditch.netarea.domain.Subject;

import java.util.Comparator;

/**
 * Created by gerard on 17/12/16.
 */

public class SubjectComparator implements Comparator<Subject> {
    @Override
    public int compare(Subject s1, Subject s2) {
        return s1.getName().compareTo(s2.getName());
    }
}
