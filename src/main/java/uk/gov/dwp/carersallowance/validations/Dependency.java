package uk.gov.dwp.carersallowance.validations;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import uk.gov.dwp.carersallowance.utils.KeyValue;

/**
 * @author David Hutchinson (drh@elegantsolutions.co.uk) on 5 Oct 2016.
 */
public class Dependency {
    private String dependantField;
    private String fieldValue;

    public Dependency() {
    }

    public String getDependantField()   { return dependantField; }
    public String getFieldValue()       { return fieldValue; }

    /**
     * @param singleLine e.g. thirdParty=no
     * @throws ParseException
     */
    public static Dependency parseSingleLine(String singleLine) throws ParseException {
        if(StringUtils.isBlank(singleLine)) {
            return null;
        }

        KeyValue keyValue = new KeyValue(singleLine, "=");
        if(keyValue.getKey() == null) {
            throw new IllegalArgumentException("dependant field cannot be blank");
        }

        if(keyValue.getValue() == null) {
            return null;
        }

        Dependency dependency = new Dependency();
        dependency.dependantField = keyValue.getKey();
        dependency.fieldValue = keyValue.getValue();

        return dependency;
    }

    public void validateFieldNames(List<String> fieldNames) throws UnknownFieldException {
        if(fieldNames == null) {
            return;
        }

        if(fieldNames.contains(dependantField) == false) {
            throw new UnknownFieldException("Unknown field name: '" + dependantField + "'", dependantField);
        }
    }

    public boolean isFulfilled(String[] values) {
        // a condition of null, matches anything
        if(fieldValue == null) {
            return true;
        }

        // a not null condition, must match something, so
        // if the values are empty its a mismatch
        if(values == null || values.length == 0) {
            return false;
        }

        // all values must match the criteria
        // an empty fieldValue is treated like any other and must match
        for(String value: values) {
            if(fieldValue.equals(value) == false) {
                return false;
            }
        }

        return true;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(this.getClass().getName()).append("@").append(System.identityHashCode(this));
        buffer.append("=[");
        buffer.append("dependantField = ").append(dependantField);
        buffer.append(", fieldValue = ").append(fieldValue);
        buffer.append("]");

        return buffer.toString();
    }

    public static class AggregateDependency extends Dependency {
        private List<Dependency> dependencies;
        private List<Dependency> orDependencies;

        private AggregateDependency(Dependency dependency) {
            dependencies = new ArrayList<>();
            orDependencies = new ArrayList<>();

            add(dependency);
        }

        public List<Dependency> getDependencies() {
            return dependencies;
        }

        private void addAggregate(AggregateDependency otherDependencies) {
            if(otherDependencies != null) {
                otherDependencies.getDependencies().forEach(this::add);
            }
        }

        private void add(Dependency dependency) {
            if(dependency instanceof AggregateDependency) {
                addAggregate((AggregateDependency)dependency);
            } else if(dependency != null) {
                dependencies.add(dependency);
            }
        }

        public void add(Dependency...otherDependencies) {
            if(otherDependencies != null) {
                for(Dependency dependency: otherDependencies) {
                    add(dependency);
                }
            }
        }

        private void addAggregateOr(AggregateDependency otherDependencies) {
            if(otherDependencies != null) {
                otherDependencies.getDependencies().forEach(this::addOr);
            }
        }

        private void addOr(Dependency dependency) {
            if (dependency instanceof AggregateDependency) {
                addAggregateOr((AggregateDependency)dependency);
            } else if(dependency != null) {
                orDependencies.add(dependency);
            }
        }

        public void addOr(Dependency...otherDependencies) {
            if (otherDependencies != null) {
                for(Dependency dependency: otherDependencies) {
                    addOr(dependency);
                }
            }
        }

        public static AggregateDependency aggregate(Dependency first, Dependency...others) {
            AggregateDependency result;
            if(first instanceof AggregateDependency) {
                result = (AggregateDependency)first;
            } else {
                result = new AggregateDependency(first);
            }
            result.add(others);

            return result;
        }

        public static AggregateDependency aggregateOr(Dependency first, Dependency...others) {
            AggregateDependency result;
            if(first instanceof AggregateDependency) {
                result = (AggregateDependency)first;
            } else {
                result = new AggregateDependency(first);
            }
            result.addOr(others);

            return result;
        }

        public void validateFieldNames(List<String> fieldNames) throws UnknownFieldException {
            if(fieldNames == null) {
                return;
            }

            List<String> unknownFields = new ArrayList<>();
            for(Dependency dependency: dependencies) {
                try {
                    dependency.validateFieldNames(fieldNames);
                } catch(UnknownFieldException e) {
                    unknownFields.addAll(e.getFieldName());
                }
            }

            if(unknownFields.isEmpty() == false) {
                throw new UnknownFieldException("Unknown fields: " + unknownFields, unknownFields);
            }
        }

        public boolean isFulfilled(Map<String, String[]> fieldValues) {
            for (Dependency dependency: dependencies) {
                if (dependency.getDependantField() == null) {
                    return Boolean.TRUE;
                }
            }

            Boolean fulfilled = Boolean.TRUE;
            for (Dependency dependency: dependencies) {
                final String[] values = fieldValues.get(dependency.getDependantField());
                if (values == null || values.length == 0) {
                    fulfilled = Boolean.FALSE;
                    break;
                }
                for (String value: values) {
                    if (dependency.getFieldValue().equals(value) == false) {
                        fulfilled = Boolean.FALSE;
                        break;
                    }
                }
            }

            Boolean fullFilledOr = Boolean.FALSE;
            for (Dependency dependency: orDependencies) {
                final String[] values = fieldValues.get(dependency.getDependantField());
                if (values == null || values.length == 0) {
                    fullFilledOr = Boolean.FALSE;
                    continue;
                }
                for (String value: values) {
                    if (dependency.getFieldValue().equals(value) == true) {
                        fullFilledOr = Boolean.TRUE;
                        break;
                    }
                }
            }
            return fulfilled || fullFilledOr;
        }

        public String toString() {
            StringBuffer buffer = new StringBuffer();

            buffer.append(this.getClass().getName()).append("@").append(System.identityHashCode(this));
            buffer.append("=[");
            buffer.append("dependencies = ").append(dependencies);
            buffer.append("]");

            return buffer.toString();
        }
    }

    public static Boolean pageDependencyFulfilled(final Map<String, Object> data, final String pageDependency, Boolean returnOnNull) {
        if (pageDependency == null) {
            return returnOnNull;
        }
        try {
            String[] nextDependencies = StringUtils.substringBefore(pageDependency, "|").split("&");
            String[] orDependencies = StringUtils.substringAfter(pageDependency, "|").split("\\|");
            Boolean dependenciesMet = Boolean.FALSE;
            for (final String nextDependency : nextDependencies) {
                final Dependency dependency = Dependency.parseSingleLine(nextDependency);
                if (dependenciesMet == Boolean.FALSE && dependency.getDependantField() != null && data.get(dependency.getDependantField()) != null) {
                    dependenciesMet = data.get(dependency.getDependantField()).equals(dependency.getFieldValue());
                }
            }
            Boolean dependenciesOrMet = Boolean.FALSE;
            for (final String orDependency : orDependencies) {
                final Dependency dependency = Dependency.parseSingleLine(orDependency);
                if (dependency != null) {
                    if (dependency.getDependantField() != null && data.get(dependency.getDependantField()) != null) {
                        dependenciesOrMet = data.get(dependency.getDependantField()).equals(dependency.getFieldValue());
                    }
                    if (dependenciesOrMet == Boolean.TRUE) {
                        break;
                    }
                }
            }
            return dependenciesMet || dependenciesOrMet;
        } catch (ParseException pe) {
            //do nothing
        }
        return true;
    }
}