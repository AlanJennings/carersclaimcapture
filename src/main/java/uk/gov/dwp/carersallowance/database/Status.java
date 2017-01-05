package uk.gov.dwp.carersallowance.database;

/**
 * Created by peterwhitehead on 05/01/2017.
 */
public enum Status {
    SUCCESS("0002"),
    ACKNOWLEDGED("0001"),
    GENERATED("0100"),
    SUBMITTED("0000"),
    SERVICE_UNAVAILABLE("9006");

    private final String status;

    Status(final String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
