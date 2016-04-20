package com.grayditch.netarea.data.network;

import com.grayditch.netarea.domain.Mark;
import com.grayditch.netarea.domain.Subject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by gerard on 9/04/16.
 */
public class NetareaPageParser {

    private static final String SECTION_POINTER_SELECTOR = "Qualificacions de l'avaluació continuada:";
    private static final String SUBJECT_TITLE_SELECTOR = "titolTaula";
    private static final String ROW = "tr";
    private static final String COLUMN = "td";

    @Inject
    public NetareaPageParser(){

    }

    /**
     * Parse the Netarea Marks page into a List<SubjectDAO>
     * @param page
     * @return
     */
    public List<Subject> parse(String page) {
        Document document = Jsoup.parse(page);
        Elements qualificationsTable = NetareaPageParser.selectQualificationsTable(document);
        return NetareaPageParser.getSubjectsAndMarks(qualificationsTable);
    }

    /**
     * Get the qualifications table from a given HTML
     * @param document
     * @return
     */
    private static Elements selectQualificationsTable(Document document) {
        Elements tableBody = document.select("h4");
        for (Element element : tableBody) {
            if (element.select("b").size() != 0 && element.select("b").first().text().equals(SECTION_POINTER_SELECTOR)) {
                return element.select("table");
            }
        }
        return new Elements();
    }

    /**
     * Get subjects and marks from a given table
     * @param qualificationsDOM
     * @return
     */
    private static List<Subject> getSubjectsAndMarks(Elements qualificationsDOM) {
        List<Subject> subjects = new ArrayList<>();
        for (Element subjectDOM : qualificationsDOM) {
            String subjectName = subjectDOM.getElementsByClass(SUBJECT_TITLE_SELECTOR).text();
            Elements marksDOMRow = subjectDOM.select(ROW);
            List<Mark> marks = NetareaPageParser.getMarks(subjectName, marksDOMRow);
            subjects.add(new Subject(subjectName, marks));
        }
        return subjects;
    }

    /**
     * Get a list of marks from a given set of rows
     * @param subjectName
     * @param row
     * @return
     */
    private static List<Mark> getMarks(String subjectName, Elements row) {
        List<Mark> marks = new ArrayList<>();
        for (Element markRow : row) {
            if (!markRow.text().equals(subjectName)) {
                Elements td = markRow.select(COLUMN);
                if (td.size() != 1) {
                    Mark mark = NetareaPageParser.getMark(td);
                    marks.add(mark);
                }
            }
        }
        return marks;
    }

    /**
     * Get Marks from a given set of columns
     * @param td
     * @return
     */
    private static Mark getMark(Elements td) {
        boolean isMarkNew = td.eq(0).select("img").size() == 1;
        if (td.size() == 2) {
            return new Mark(isMarkNew, td.eq(1).text());
        } else {
            return new Mark(isMarkNew, td.eq(1).text(), td.eq(2).text(), td.eq(3).text());
        }
    }
}
