package uk.gov.dwp.carersallowance.controller.preview;

/**
 * Created by peterwhitehead on 16/01/2017.
 */
public class PreviewMapping {
    private final String url;
    private final String hash;

    public PreviewMapping(final String url, final String hash) {
        this.url = url;
        this.hash = hash;
    }

    public PreviewMapping(final String fullLine) {
        final String[] mappings = fullLine.split(",");
        this.url = mappings[0].trim();
        this.hash = mappings[1].trim();
    }

    public String getUrl() {
        return url;
    }

    public String getHash() {
        return hash;
    }
}
