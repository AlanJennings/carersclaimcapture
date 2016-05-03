package uk.gov.dwp.carersallowance.data;

import java.util.Arrays;
import java.util.List;

public class MultiValuedQuestion<T> extends AbstractQuestion<List<Question<T>>> {

    public MultiValuedQuestion(String id, String label, String hintBefore, String hintAfter, List<Question<T>> value) {
        super(id, label, hintBefore, hintAfter, value);
    }

    @SafeVarargs
    public MultiValuedQuestion(String id, String label, String hintBefore, String hintAfter, Question<T>... values) {
        super(id, label, hintBefore, hintAfter, varArgToList(values));
    }

    @SafeVarargs
    private static <E> List<Question<E>> varArgToList(Question<E>... values) {
        if(values == null) {
            return null;
        }

        if(values.length == 0) {
            return null;
        }

        List<Question<E>> list = Arrays.asList(values);
        return list;
    }

    public List<Question<T>> getValues() {
        return getValue();
    }
}
