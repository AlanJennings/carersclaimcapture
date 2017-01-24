package uk.gov.dwp.carersallowance.language;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.carersallowance.utils.LoadFile;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by peterwhitehead on 19/01/2017.
 */
public class MessageFileTest {
    private static final Logger LOG = LoggerFactory.getLogger(MessageFileTest.class);

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
}
