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
public class CreateTempWelshPropertiesFromMappings {
    private static final Logger LOG = LoggerFactory.getLogger(CreateTempWelshPropertiesFromMappings.class);

    private List<String[]> mappingsScalaJava;
    private List<String> englishFile;

    public CreateTempWelshPropertiesFromMappings() throws Exception {
        englishFile = LoadFile.readLines(CreateTempWelshPropertiesFromMappings.class.getClassLoader().getResource("messages_en.properties"));
        List<String> mappings = LoadFile.readLines(CreateTempWelshPropertiesFromMappings.class.getClassLoader().getResource("scalaJavaTextMappings.txt"));
        mappingsScalaJava = mappings.stream().filter(s -> {String s2 = s.trim(); return StringUtils.isNotBlank(s2) && !s2.startsWith("#");}).map(s -> {String[] s2 = s.split("=", 2); return new String[]{s2[0].trim(), s2[1].trim()};}).collect(Collectors.toList());
    }

    private void createTempClaimWelshFile() throws Exception {
        //get all welsh values in scala properties file
        List<File> filesInFolder = Files.walk(Paths.get("../ClaimCapture/c3/conf/cy")).filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
        List<List<String>> temp = filesInFolder.stream().map(f -> { try { return LoadFile.readLines((new File(f.getAbsolutePath())).toURL()); } catch (Exception e) { LOG.error("Exception e", e); return null; } } ).collect(Collectors.toList());
        List<String> welshFilesValues = temp.stream().flatMap(List::stream).filter(s -> {String s2 = s.trim(); return StringUtils.isNotBlank(s2) && !s2.startsWith("#");}).collect(Collectors.toList());

        //create temp welsh properties file
        final String file = "src/test/resources/temp_cy.properties";
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file))) {
            englishFile.forEach(line -> {
                final String[] lineValues = line.split("=", 2);
                try {
                    final String[] scalaJava = findMappingInScalaJava(lineValues[0].trim(), mappingsScalaJava);
                    if (scalaJava != null) {
                        String[] welshLine = findLineInWelsh(scalaJava[0].trim(), welshFilesValues);
                        if (welshLine != null) {
                            writer.write(scalaJava[1].trim() + " = " + welshLine[1].trim().replace("''", "&apos;") + System.lineSeparator());
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

    private String[] findLineInWelsh(final String value, final List<String> welshFilesValues) {
        for (final String welshValue : welshFilesValues) {
            final String[] welshValues = welshValue.split("=", 2);
            if (welshValues[0].trim().equals(value)) {
                return welshValues;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            CreateTempWelshPropertiesFromMappings createTempWelshPropertiesFromMappings = new CreateTempWelshPropertiesFromMappings();
            createTempWelshPropertiesFromMappings.createTempClaimWelshFile();
        } catch (Exception e) {
            LOG.error("Failed to create mapping file.", e);
        }
    }
}
