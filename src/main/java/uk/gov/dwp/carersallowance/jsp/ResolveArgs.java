package uk.gov.dwp.carersallowance.jsp;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.servlet.ServletContext;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.dwp.carersallowance.utils.Parameters;

public class ResolveArgs extends BodyTagSupport {
    private static final long serialVersionUID = 3407131412410043137L;
    private static final Logger LOG = LoggerFactory.getLogger(ResolveArgs.class);

    private static final String CADS_TLD_PREFIX_END = "}";
    private static final String CADS_TLD_PREFIX_START = "${cads:";

    private String varName;

    public void setVar(String value) {
        varName = value;
    }

    @Override
    public int doAfterBody() throws JspException {
        try {
            String rawString = getBodyContent().getString();

            ServletContext servletContext = pageContext.getServletContext();
            JspApplicationContext jspAppContext = JspFactory.getDefaultFactory().getJspApplicationContext(servletContext);
            ELContext elContext = pageContext.getELContext();
            String parsedString = evaluateExpression(jspAppContext, elContext, rawString);

            if(StringUtils.isBlank(varName)) {
                getPreviousOut().print(parsedString);
            } else {
                pageContext.setAttribute(varName, parsedString);
            }
            return SKIP_BODY;
        } catch(IOException | RuntimeException e) {
            LOG.error("Unexpected exception: ", e);
            throw new JspException("Unexpected Exception: ", e);
        }
    }

    private List<String> splitExpressions(String expressionStr) {
        if(expressionStr == null) {
            return null;
        }

        List<String> results = new ArrayList<>();

        // split into token around ${}
        char[] characters = expressionStr.toCharArray();    // don't trim, it will change the output
        Deque<Integer> startStack = new ArrayDeque<>();
        int end = 0;
        for(int index = 0; index < characters.length; index++) {
            switch(characters[index]) {
            case '$':
                if((index + 1) < characters.length && characters[index + 1] == '{') {
                    if(index > end && startStack.isEmpty()) {
                        String subExpression = expressionStr.substring(end, index);
                        results.add(subExpression);
                        end = index;
                    }
                    startStack.push(index);
                    index++;
                }
                break;

            case '}':
                if(startStack.isEmpty() == false) {
                    Integer start = startStack.pop();
                    if(startStack.isEmpty()) {
                        end = index + 1;    // include 'end'
                        String subExpression = expressionStr.substring(start, end);
                        results.add(subExpression);
                    }
                }
                break;
            default:
                break;
            }
        }

        if(startStack.isEmpty() == false) {
            Integer firstStart = startStack.getFirst();
            String subExpression = expressionStr.substring(firstStart);
            results.add(subExpression);

        } else if(end < characters.length) {
            String subExpression = expressionStr.substring(end);
            results.add(subExpression);
        }

        return results;
    }

    /**
     * Its possible that this must be a single expression, not a string that contains multiple expressions
     * this doesn't work with user defined functions (not sure about jsp functions), but should.  Needs fixing
     * Also it seems very difficult to get the tag-lib information, which we sort of need (it all gets very app-server specific).
     */
    private String evaluateExpression(JspApplicationContext jspAppContext, ELContext elContext, String expressionStr) {
        Parameters.validateMandatoryArgs(expressionStr, "expressionStr");
        List<String> subExpressions = splitExpressions(expressionStr);
        if(subExpressions == null || subExpressions.isEmpty()) {
            return "";
        }

        if(subExpressions.size() > 1) {
            StringBuffer buffer = new StringBuffer();
            for(String subExpression :subExpressions) {
                String result = evaluateExpression(jspAppContext, elContext, subExpression);
                buffer.append(result);
            }

            return buffer.toString();
        }

        if(expressionStr != null && expressionStr.startsWith(CADS_TLD_PREFIX_START)) {
            return evaluateCadsExpression(jspAppContext, elContext, expressionStr);
        } else {
            return evaluateSingleExpression(jspAppContext, elContext, expressionStr);
        }
    }

    private String evaluateSingleExpression(JspApplicationContext jspAppContext, ELContext elContext, String expression) {
        ValueExpression valueExpression = jspAppContext.getExpressionFactory().createValueExpression(elContext, expression, String.class);
        Object evaluatedValue =  valueExpression.getValue(elContext);

        if(evaluatedValue == null) {
            return "";
        }

        String evaluatedExpression;
        if(evaluatedValue instanceof String) {
            evaluatedExpression = (String)evaluatedValue;
        } else {
            evaluatedExpression = evaluatedValue.toString();
        }

        if(evaluatedExpression.equals(expression)) {
            // this results in at least one evaluation more than is needed, but allows for recursive evaluation
            return evaluatedExpression;
        }

        // re-evaluate in case the results contains further expressions to be evaluated
        return evaluateExpression(jspAppContext, elContext, evaluatedExpression);
    }

    private String evaluateCadsExpression(JspApplicationContext jspAppContext, ELContext elContext, String expression) {
        // e.g ${cads:dateOffset(dateOfClaim_day, dateOfClaim_month, dateOfClaim_year, "dd MMMMMMMMMM yyyy", "")}"
        if(StringUtils.isEmpty(expression)) {
            return "";
        }

        if(expression.startsWith(CADS_TLD_PREFIX_START) == false || expression.endsWith(CADS_TLD_PREFIX_END) == false) {
            throw new IllegalArgumentException("expression(" + expression + ") is  not of the expected form: " + CADS_TLD_PREFIX_START + " ... " + CADS_TLD_PREFIX_END);
        }

        // expecting ${cads:fnName(arg, arg, ...)}
        String function = assertNotNull(expression.substring(CADS_TLD_PREFIX_START.length(), expression.length() - CADS_TLD_PREFIX_END.length()), "Unable to locate function"); // fnName(arg, arg, ...)
        String functionName = assertNotNull(StringUtils.substringBefore(function, "("), "unable to locate function name");           // fnName
        String allArguments = assertNotNull(function.substring(functionName.length()), "Unable to locate function brackets");            // (arg, arg, ...)
        String rawArguments = allArguments.substring(1, allArguments.length() - 1);
        String[] arguments = rawArguments.split(",");
        for(int index = 0; index < arguments.length; index++) {
            arguments[index] = arguments[index].trim();
        }

        switch(functionName) {
            case "dateOffset":
            {
                if(arguments.length != 5) {
                    throw new IllegalArgumentException("Wrong number of arguments for dateOffset. Expecting dateOffset(String dayField, String monthField, String yearField, String format, String offset)");
                }
                String dayField = toString(pageContext.findAttribute(arguments[0]));
                String monthField = toString(pageContext.findAttribute(arguments[1]));
                String yearField = toString(pageContext.findAttribute(arguments[2]));
                String format = stripEnclosingQuotes(arguments[3]);
                String offset = stripEnclosingQuotes(arguments[4]);

                return Functions.dateOffset(dayField, monthField, yearField, format, offset);
            }
            default:
                    throw new IllegalArgumentException("Unknown function: " + functionName);
        }
    }

    private String stripEnclosingQuotes(String string) {
        if(string == null) {
            return null;
        }

        if(string.length() < 2) {
            return string;
        }

        // either single or double quotes
        if((string.charAt(0) == '"' && (string.charAt(string.length() - 1) == '"'))
        || (string.charAt(0) == '\'' && (string.charAt(string.length() - 1) == '\''))) {
            String trimmed = string.substring(1, string.length() - 1);
            return trimmed;
        }

        return string;
    }

    private String toString(Object value) {
        if(value == null || value instanceof String) {
            return (String)value;
        }

        return value.toString();
    }

    private String assertNotNull(String string, String exceptionMessage) {
        if(string == null) {
            throw new IllegalArgumentException(exceptionMessage);
        }
        return string;
    }

    public static void main(String[] args) {
        ResolveArgs resolveArgs = new ResolveArgs();
        String result = resolveArgs.stripEnclosingQuotes("");
        System.out.println("'\"\"' = '" + result + "'");
    }
}
