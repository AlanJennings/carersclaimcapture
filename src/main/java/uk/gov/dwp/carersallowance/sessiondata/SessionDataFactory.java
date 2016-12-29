package uk.gov.dwp.carersallowance.sessiondata;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created by peterwhitehead on 29/12/2016.
 */
@Component
public class SessionDataFactory {
    private final Boolean useSessionDataDB;
    private final SessionDataDatabaseServiceImpl sessionDataDatabaseService;
    private final SessionDataMapServiceImpl sessionDataMapService;

    @Inject
    public SessionDataFactory(final @Value("${session.data.to.db}") Boolean useSessionDataDB,
                              final SessionDataDatabaseServiceImpl sessionDataDatabaseService,
                              final SessionDataMapServiceImpl sessionDataMapService) {
        this.useSessionDataDB = useSessionDataDB;
        this.sessionDataDatabaseService = sessionDataDatabaseService;
        this.sessionDataMapService = sessionDataMapService;
    }

    public SessionDataService getSessionDataService() {
        if (useSessionDataDB) {
            return sessionDataDatabaseService;
        }
        return sessionDataMapService;
    }
}
