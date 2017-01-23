package uk.gov.dwp.carersallowance.language;

import gov.dwp.carers.helper.Utils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.carersallowance.utils.LoadFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by peterwhitehead on 19/01/2017.
 */
public class MessageFileTest {
    private static final Logger LOG = LoggerFactory.getLogger(MessageFileTest.class);

    private String singleAposPattern = ".*[^']'[^'].*";
    private List<String> welshKeys;
    private List<String> englishKeys;
    private List<String[]> mappingsScalaJava;
    private List<String> englishFile;

    @Before
    public void setUp() throws Exception {
        englishFile = LoadFile.readLines(MessageFileTest.class.getClassLoader().getResource("messages_en.properties"));
        List<String> welshFile = LoadFile.readLines(MessageFileTest.class.getClassLoader().getResource("messages_cy.properties"));
        List<String> mappings = LoadFile.readLines(MessageFileTest.class.getClassLoader().getResource("scalaJavaTextMappings.txt"));
        welshKeys = welshFile.stream().filter(s -> {String s2 = s.trim(); return StringUtils.isNotBlank(s2) && !s2.startsWith("#");}).map(s -> s.split("=", 2)).map(a -> a[0].trim()).collect(Collectors.toList());
        englishKeys = englishFile.stream().filter(s -> {String s2 = s.trim(); return StringUtils.isNotBlank(s2) && !s2.startsWith("#");}).map(s -> s.split("=", 2)).map(a -> a[0].trim()).collect(Collectors.toList());
        mappingsScalaJava = mappings.stream().filter(s -> {String s2 = s.trim(); return StringUtils.isNotBlank(s2) && !s2.startsWith("#");}).map(s -> {String[] s2 = s.split("=", 2); return new String[]{s2[0].trim(), s2[1].trim()};}).collect(Collectors.toList());
    }

    @Test
    public void checkEnglishKeysExistInWelshFile() {
        List<String> keys = englishKeys.stream().filter(enKey -> !welshKeys.contains(enKey)).collect(Collectors.toList());
        assertThat(keys.size(), is(0));
    }

    @Test
    public void checkWelshKeysExistInEnglishFile() {
        List<String> keys = welshKeys.stream().filter(cyKey -> !englishKeys.contains(cyKey)).collect(Collectors.toList());
        assertThat(keys.size(), is(0));
    }

    @Test
    public void allJavaMappingsExistInEnglishFile() {
        List<String> filter = englishKeys.stream().filter(enKey -> !enKey.startsWith("${") || !enKey.contains(".args")).collect(Collectors.toList());
        List<String> keys = mappingsScalaJava.stream().filter(enKey -> !filter.contains(enKey[1])).map(a -> a[1]).collect(Collectors.toList());
        keys.forEach(message -> LOG.error("ERROR - Mapping key not found in en mappings file, message:{}", message));
        assertThat(keys.size(), is(0));
    }

    @Test
    public void allJavaMappingsExistInWelshFile() {
        List<String> filter = welshKeys.stream().filter(ckKey -> !ckKey.startsWith("${") || !ckKey.contains(".args")).collect(Collectors.toList());
        List<String> keys = mappingsScalaJava.stream().filter(cyKey -> !filter.contains(cyKey[1])).map(a -> a[1]).collect(Collectors.toList());
        keys.forEach(message -> LOG.error("ERROR - Mapping key not found in cy mappings file, message:{}", message));
        assertThat(keys.size(), is(0));
    }

//    @Test
//    public void createTempClaimWelshFile() throws Exception {
//        List<File> filesInFolder = Files.walk(Paths.get("../ClaimCapture/c3/conf/cy")).filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
//        List<List<String>> temp = filesInFolder.stream().map(f -> { try { return LoadFile.readLines((new File(f.getAbsolutePath())).toURL()); } catch (Exception e) { LOG.error("Exception e", e); return null; } } ).collect(Collectors.toList());
//        List<String> welshFilesValues = temp.stream().flatMap(List::stream).filter(s -> {String s2 = s.trim(); return StringUtils.isNotBlank(s2) && !s2.startsWith("#");}).collect(Collectors.toList());
//        final String file = "src/test/resources/temp_cy.properties";
//        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file))) {
//            englishFile.forEach(line -> {
//                final String[] lineValues = line.split("=", 2);
//                try {
//                    final String[] scalaJava = findMappingInScalaJava(lineValues[0].trim(), mappingsScalaJava);
//                    if (scalaJava != null) {
//                        String[] welshLine = findLineInWelsh(scalaJava[0].trim(), welshFilesValues);
//                        if (welshLine != null) {
//                            writer.write(scalaJava[1].trim() + " = " + welshLine[1].trim().replace("''", "&apos;") + System.lineSeparator());
//                        }
//                    } else {
//                        writer.write(line + System.lineSeparator());
//                    }
//                } catch (IOException ioe) {
//                    throw new RuntimeException(ioe);
//                }
//            });
//        }
//    }
//
//    private String[] findMappingInScalaJava(String value, List<String[]> mappingsScalaJava) {
//        for (final String[] scalaJavaValue : mappingsScalaJava) {
//            if (scalaJavaValue[1].equals(value)) {
//                return scalaJavaValue;
//            }
//        }
//        return null;
//    }
//
//    private String[] findLineInWelsh(final String value, final List<String> welshFilesValues) {
//        for (final String welshValue : welshFilesValues) {
//            final String[] welshValues = welshValue.split("=", 2);
//            if (welshValues[0].trim().equals(value)) {
//                return welshValues;
//            }
//        }
//        return null;
//    }
}
