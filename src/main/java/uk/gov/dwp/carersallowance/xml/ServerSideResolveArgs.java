package uk.gov.dwp.carersallowance.xml;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.carersallowance.jsp.Functions;
import uk.gov.dwp.carersallowance.jsp.ResolveArgs;

import java.util.*;

/**
 * Created by peterwhitehead on 12/01/2017.
 */
public class ServerSideResolveArgs extends ResolveArgs {
    private static final Logger LOG = LoggerFactory.getLogger(ServerSideResolveArgs.class);

    public List<Object> evaluateExpressions(final String expressionStr, final Map<String, Object> values) {
        final String[] expressions = expressionStr.split("\\|");
        List<Object> parameters = new ArrayList<>();
        for (final String expression : expressions) {
            parameters.add(evaluateExpression(expression, values));
        }
        return parameters;
    }

    private String evaluateExpression(final String expressionStr, final Map<String, Object> values) {
        try {
            if ("${careeFirstName} ${careeSurname}".equals(expressionStr)) {
                return "@dpname";
            }
            if ("${carerFirstName} ${carerSurname}".equals(expressionStr)) {
                return "@yourname";
            }

            List<String> subExpressions = splitExpressions(expressionStr);
            if (subExpressions == null || subExpressions.isEmpty()) {
                return "";
            }

            if (subExpressions.size() > 1) {
                StringBuffer buffer = new StringBuffer();
                for (String subExpression :subExpressions) {
                    String result = evaluateExpression(subExpression, values);
                    buffer.append(result);
                }

                return buffer.toString();
            }

            if (expressionStr != null && expressionStr.startsWith(CADS_TLD_PREFIX_START)) {
                return evaluateCadsExpression(expressionStr, values);
            } else {
                return evaluateSingleExpression(expressionStr, values);
            }
        } catch(RuntimeException e) {
            LOG.error("Unexpected RuntimeException evaluating: " + expressionStr, e);
            throw e;
        }
    }

    private String evaluateSingleExpression(final String expression, final Map<String, Object> values) {
        try {
            Object evaluatedValue;
            if (expression.startsWith("${")) {
                final String prop = StringUtils.substringBefore(StringUtils.substringAfter(expression, "${"), "}");
                evaluatedValue = values.get(prop);
            } else {
                return expression;
            }

            if (evaluatedValue == null) {
                return "";
            }

            String evaluatedExpression;
            if (evaluatedValue instanceof String) {
                evaluatedExpression = (String)evaluatedValue;
            } else {
                evaluatedExpression = evaluatedValue.toString();
            }

            if (evaluatedExpression.equals(expression)) {
                // this results in at least one evaluation more than is needed, but allows for recursive evaluation
                return evaluatedExpression;
            }

            // re-evaluate in case the results contains further expressions to be evaluated
            return evaluateExpression(evaluatedExpression, values);
        } catch (RuntimeException e) {
            LOG.error("unexpected RuntimeException, evaluating single expression: " + expression);
            throw e;
        }
    }

    private String evaluateCadsExpression(final String expression, final Map<String, Object> values) {
        try {
            // e.g ${cads:dateOffset(dateOfClaim_day, dateOfClaim_month, dateOfClaim_year, "d MMMMMMMMMM yyyy", "")}"
            if (StringUtils.isEmpty(expression)) {
                return "";
            }

            if (expression.startsWith(CADS_TLD_PREFIX_START) == false || expression.endsWith(CADS_TLD_PREFIX_END) == false) {
                throw new IllegalArgumentException("expression(" + expression + ") is  not of the expected form: " + CADS_TLD_PREFIX_START + " ... " + CADS_TLD_PREFIX_END);
            }

            // expecting ${cads:fnName(arg, arg, ...)}
            String function = assertNotNull(expression.substring(CADS_TLD_PREFIX_START.length(), expression.length() - CADS_TLD_PREFIX_END.length()), "Unable to locate function"); // fnName(arg, arg, ...)
            String functionName = assertNotNull(StringUtils.substringBefore(function, "("), "unable to locate function name");           // fnName
            String allArguments = assertNotNull(function.substring(functionName.length()), "Unable to locate function brackets");            // (arg, arg, ...)
            String rawArguments = allArguments.substring(1, allArguments.length() - 1);
            String[] arguments = rawArguments.split(",");
            for (int index = 0; index < arguments.length; index++) {
                arguments[index] = arguments[index].trim();
            }

            switch (functionName) {
                case "dateOffset": {
                    if (arguments.length != 5) {
                        throw new IllegalArgumentException("Wrong number of arguments for dateOffset. Expecting dateOffset(String dayField, String monthField, String yearField, String format, String offset)");
                    }
                    String dayField = toString(values.get(arguments[0]));
                    String monthField = toString(values.get(arguments[1]));
                    String yearField = toString(values.get(arguments[2]));
                    String format = stripEnclosingQuotes(arguments[3]);
                    String offset = stripEnclosingQuotes(arguments[4]);

                    return Functions.dateOffset(dayField, monthField, yearField, format, offset);
                }
                case "dateOffsetFromCurrent" : {
                    if (arguments.length != 2) {
                        throw new IllegalArgumentException("Wrong number of arguments for dateOffsetFromCurrent. Expecting dateOffsetFromCurrent(String format, String offset)");
                    }
                    String format = stripEnclosingQuotes(arguments[0]);
                    String offset = stripEnclosingQuotes(arguments[1]);
                    return Functions.dateOffsetFromCurrent(format, offset);
                }
                case "prop" : {
                    if (arguments.length != 1) {
                        throw new IllegalArgumentException("Wrong number of arguments for prop. Expecting prop(String propertyValue)");
                    }
                    String prop = stripEnclosingQuotes(arguments[0]);
                    return evaluateExpression(Functions.prop(prop), values);
                }
                default:
                    throw new IllegalArgumentException("Unknown function: " + functionName);
            }
        } catch (RuntimeException e) {
            LOG.error("Problems evaluating CADS expression: " + expression, e);
            throw e;
        }
    }
}
