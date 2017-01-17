package uk.gov.dwp.carersallowance.utils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by peterwhitehead on 16/01/2017.
 */
public class LoadFile {
    public static List<String> readLines(final URL url) throws IOException {
        if (url == null) {
            return null;
        }
        InputStream inputStream = null;
        try {
            inputStream = url.openStream();
            List<String> list = IOUtils.readLines(inputStream, Charset.defaultCharset());
            return list;
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }


}
