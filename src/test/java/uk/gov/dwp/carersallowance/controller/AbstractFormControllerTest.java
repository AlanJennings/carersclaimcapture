package uk.gov.dwp.carersallowance.controller;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class AbstractFormControllerTest {
    /**
     * Verify that all the field names used by the controllers are unique.
     *
     * iterate over all the classes from this directory downwards, for each that extends AbstractFormController
     * add all the fields to the map and verify that they are not reused.
     */
    @Test
    public void uniqueFieldNameTest() {
        File dir = new File("/Users/drh/development-java/CarersClaimCapture/CarersClaimCapture/src/main/java/uk/gov/dwp/carersallowance/controller");
        String packageRoot = "uk.gov.dwp.carersallowance.controller";

        // field vs Controller
        Map<String, String> fields = new HashMap<>();
        List<String> errors = new ArrayList<>();
        buildFieldMap(fields, dir, packageRoot, errors);

        assertEquals(errors.toString(), 0, errors.size());
    }

    private void buildFieldMap(Map<String, String> fields, File dir, String packageName, List<String> errors) {
        if(fields == null || dir == null || dir.exists() == false || dir.isDirectory() == false) {
            return;
        }

        File[] children = dir.listFiles();
        for(File child: children) {
            if(child.isDirectory()) {
                String childPackage = packageName + "." + child.getName();
                buildFieldMap(fields, child, childPackage, errors);
            } else {
                String filename = child.getName();
                try {
                    if(filename.toUpperCase().endsWith(".CLASS")) {
                        String shortClassName = filename.substring(0, filename.length() - ".CLASS".length());
                        String className = packageName + "." + shortClassName;
                        Class<?> classObj = Class.forName(className);
                        if(AbstractFormController.class.isAssignableFrom(classObj)) {
                            AbstractFormController controller = (AbstractFormController)classObj.newInstance();
                            String[] controllerFields = controller.getFields();
                            for(String controllerField: controllerFields) {
                                String oldShortClassName = fields.put(controllerField, className);
                                if(oldShortClassName != null) {
                                    errors.add("Duplicate for: " + controllerField + " controllers = " + oldShortClassName + ", " + shortClassName);
                                }
                            }
                        }
                    }
                } catch(RuntimeException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    errors.add("ERROR Failed to check " + filename + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}
