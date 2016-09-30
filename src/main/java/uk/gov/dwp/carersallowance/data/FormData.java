package uk.gov.dwp.carersallowance.data;

import java.util.ArrayList;
import java.util.List;

public class FormData {
    private List<Page> pages;

    public FormData() {
        pages = new ArrayList<>();
    }

    public List<Page> getPages()    { return pages; }

    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(this.getClass().getName()).append("@").append(System.identityHashCode(this));
        buffer.append("=[");
        buffer.append("pages = ").append(pages);
        buffer.append("]");

        return buffer.toString();
    }

    public static class Page {
        private String            sectionId;
        private String            title;
        private String            beforeLink;
        private String            nextLink;
        private List<Question<?>> questions;

        public Page() {
            questions = new ArrayList<Question<?>>();
        }

        public String            getSectionId()    { return sectionId; }
        public String            getTitle()        { return title; }
        public String            getBeforeLink()   { return beforeLink; }
        public String            getNextLink()     { return nextLink; }
        public List<Question<?>> getQuestions()    { return questions; }

        public void setSectionId(String value)              { this.sectionId = value; }
        public void setTitle(String value)                  { this.title = value; }
        public void setBeforeLink(String value)             { this.beforeLink = value; }
        public void setNextLink(String value)               { this.nextLink = value; }
        public void setQuestions(List<Question<?>> values)  { this.questions = values; }

        public void addQuestion(Question<?> question) {
            questions.add(question);
        }

        public String toString() {
            StringBuffer buffer = new StringBuffer();

            buffer.append(this.getClass().getName()).append("@").append(System.identityHashCode(this));
            buffer.append("=[");
            buffer.append("sectionId = ").append(sectionId);
            buffer.append(", title = ").append(title);
            buffer.append(", beforeLink = ").append(beforeLink);
            buffer.append(", nextLink = ").append(nextLink);
            buffer.append(", questions = ").append(questions);
            buffer.append("]");

            return buffer.toString();
        }

    }
}
