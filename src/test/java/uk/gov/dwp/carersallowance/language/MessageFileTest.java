package uk.gov.dwp.carersallowance.language;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.carersallowance.utils.LoadFile;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
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
    private List<String> mappingsKeysToScala;
    private List<String> mappingsKeysToJava;

    @Before
    public void setUp() throws Exception {
        List<String> englishFile = LoadFile.readLines(MessageFileTest.class.getClassLoader().getResource("messages_en.properties"));
        List<String> welshFile = LoadFile.readLines(MessageFileTest.class.getClassLoader().getResource("messages_cy.properties"));
        List<String> mappings = LoadFile.readLines(MessageFileTest.class.getClassLoader().getResource("scalaJavaTextMappings.txt"));
        welshKeys = welshFile.stream().filter(s -> {String s2 = s.trim(); return StringUtils.isNotBlank(s2) && !s2.startsWith("#");}).map(s -> s.split("=", 2)).map(a -> a[0].trim()).collect(Collectors.toList());
        englishKeys = englishFile.stream().filter(s -> {String s2 = s.trim(); return StringUtils.isNotBlank(s2) && !s2.startsWith("#");}).map(s -> s.split("=", 2)).map(a -> a[0].trim()).collect(Collectors.toList());
        mappingsKeysToScala = mappings.stream().filter(s -> {String s2 = s.trim(); return StringUtils.isNotBlank(s2) && !s2.startsWith("#");}).map(s -> s.split("=", 2)).map(a -> a[0].trim()).collect(Collectors.toList());
        mappingsKeysToJava = mappings.stream().filter(s -> {String s2 = s.trim(); return StringUtils.isNotBlank(s2) && !s2.startsWith("#");}).map(s -> s.split("=", 2)).map(a -> a[1].trim()).collect(Collectors.toList());
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
    public void checkApostropheInEnglishFile() {
        List<String> filter = englishKeys.stream().filter(ckKey -> !ckKey.startsWith("${") || !ckKey.contains(".args")).collect(Collectors.toList());
        List<String> keys = mappingsKeysToJava.stream().filter(cyKey -> !filter.contains(cyKey)).collect(Collectors.toList());
        keys.forEach(message -> LOG.error("ERROR - Mapping key not found in cy mappings file, message:{}", message));
        assertThat(keys.size(), is(0));
    }

    @Test
    public void checkApostropheInWelshFile() {
        List<String> filter = welshKeys.stream().filter(ckKey -> !ckKey.startsWith("${") || !ckKey.contains(".args")).collect(Collectors.toList());
        List<String> keys = mappingsKeysToJava.stream().filter(cyKey -> !filter.contains(cyKey)).collect(Collectors.toList());
        keys.forEach(message -> LOG.error("ERROR - Mapping key not found in cy mappings file, message:{}", message));
        assertThat(keys.size(), is(0));
    }

    @Test
    public void createTempClaimWelshFile() throws Exception {
        String input = "06-12-2016";
        Locale l = new Locale ("cy");
        DateTimeFormatter f = DateTimeFormatter.ofPattern ("dd-MM-yyyy", l);
        LocalDate ld = LocalDate.parse (input , f );
        //myDate.format(DateTimeFormatter.ofPattern("dd-MMM-YYYY",new Locale("cy")));

        SimpleDateFormat dateParser = new SimpleDateFormat("dd-MM-YYYY", l);
        Date date = dateParser.parse(input);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("d MMMMMMMMMM, yyyy", l);
        String test = dateFormatter.format(date);
        Formatter formatter = new Formatter(System.out, l);
        formatter.format("month: %tB\n", date);
        LOG.error(test);
    }
}
