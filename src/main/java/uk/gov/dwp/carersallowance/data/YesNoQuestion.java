package uk.gov.dwp.carersallowance.data;

import uk.gov.dwp.carersallowance.utils.Parameters;

public class YesNoQuestion extends MultiValuedQuestion<String> {

    private Question<String> yes;   // first
    private Question<String> no;    // second

    public YesNoQuestion(String id, String label, String hintBefore, String hintAfter, Question<String> yes, Question<String> no) {
        super(id, label, hintBefore, hintAfter, yes, no);

        Parameters.validateMandatoryArgs(new Object[]{yes, no}, new String[]{"yes", "no"});

        this.yes = yes;
        this.no = no;
    }

    public Question<String> getYes()    { return yes; }   // first
    public Question<String> getNo()     { return no; }    // second
}
