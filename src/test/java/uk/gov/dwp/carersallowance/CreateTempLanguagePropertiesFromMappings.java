package uk.gov.dwp.carersallowance;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.carersallowance.utils.LoadFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by peterwhitehead on 24/01/2017.
 */
public class CreateTempLanguagePropertiesFromMappings {
    private static final Logger LOG = LoggerFactory.getLogger(CreateTempLanguagePropertiesFromMappings.class);

    private List<String[]> mappingsScalaJava;
    private List<String> englishFile;

    public CreateTempLanguagePropertiesFromMappings() throws Exception {
        englishFile = LoadFile.readLines(CreateTempLanguagePropertiesFromMappings.class.getClassLoader().getResource("messages_en.properties"));
        List<String> mappings = LoadFile.readLines(CreateTempLanguagePropertiesFromMappings.class.getClassLoader().getResource("scalaJavaTextMappings.txt"));
        mappingsScalaJava = mappings.stream().filter(s -> {String s2 = s.trim(); return StringUtils.isNotBlank(s2) && !s2.startsWith("#");}).map(s -> {String[] s2 = s.split("=", 2); return new String[]{s2[0].trim(), s2[1].trim()};}).collect(Collectors.toList());
    }

    private void createTempClaimLanguageFile(final String language) throws Exception {
        //get all language for language  values in scala properties file
        List<File> filesInFolder = Files.walk(Paths.get("../ClaimCapture/c3/conf/" + language)).filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
        List<List<String>> temp = filesInFolder.stream().map(f -> { try { return LoadFile.readLines((new File(f.getAbsolutePath())).toURL()); } catch (Exception e) { LOG.error("Exception e", e); return null; } } ).collect(Collectors.toList());
        List<String> filesValues = temp.stream().flatMap(List::stream).filter(s -> {String s2 = s.trim(); return StringUtils.isNotBlank(s2) && !s2.startsWith("#");}).collect(Collectors.toList());

        //create temp welsh properties file
        final String file = "src/test/resources/temp_" + language + ".properties";
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file))) {
            englishFile.forEach(line -> {
                final String[] lineValues = line.split("=", 2);
                try {
                    final String[] scalaJava = findMappingInScalaJava(lineValues[0].trim(), mappingsScalaJava);
                    if (scalaJava != null) {
                        String[] newLine = findLineInFile(scalaJava[0].trim(), filesValues);
                        if (newLine != null) {
                            writer.write(scalaJava[1].trim() + " = " + newLine[1].trim().replace("''", "&apos;") + System.lineSeparator());
                        }
                    } else {
                        writer.write(line + System.lineSeparator());
                    }
                } catch (IOException ioe) {
                    throw new RuntimeException(ioe);
                }
            });
        }
    }

    private String[] findMappingInScalaJava(String value, List<String[]> mappingsScalaJava) {
        for (final String[] scalaJavaValue : mappingsScalaJava) {
            if (scalaJavaValue[1].equals(value)) {
                return scalaJavaValue;
            }
        }
        return null;
    }

    private String[] findLineInFile(final String value, final List<String> filesValues) {
        for (final String fileValue : filesValues) {
            final String[] newFileValues = fileValue.split("=", 2);
            if (newFileValues[0].trim().equals(value)) {
                return newFileValues;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            CreateTempLanguagePropertiesFromMappings createTempLanguagePropertiesFromMappings = new CreateTempLanguagePropertiesFromMappings();
            //create temp welsh language file
            createTempLanguagePropertiesFromMappings.createTempClaimLanguageFile("cy");
            //create temp english language file
            createTempLanguagePropertiesFromMappings.createTempClaimLanguageFile("en");
        } catch (Exception e) {
            LOG.error("Failed to create mapping file.", e);
        }
    }

//    @Test
//    public void createTestTempClaimWelshFile() throws Exception {
//        CreateTempLanguagePropertiesFromMappings createTempWelshPropertiesFromMappings = new CreateTempLanguagePropertiesFromMappings();
//        createTempWelshPropertiesFromMappings.createTestTempClaimWelshFile();
//    }
}