package uk.gov.dwp.carersallowance.monitoring;

import gov.dwp.carers.CADSHealthCheck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.dwp.carersallowance.database.DatabaseService;

import javax.inject.Inject;

/**
 * Created by peterwhitehead on 28/06/2016.
 */
@Component
public class DBHealthCheck extends CADSHealthCheck {
    private final DatabaseService databaseService;

    @Inject
    public DBHealthCheck(final @Value("${application.name}") String applicationName,
                         final @Value("${application.version}") String applicationVersion,
                         final DatabaseService databaseService) {
        super(applicationName, applicationVersion.replace("-SNAPSHOT", ""), "-transaction-db");
        this.databaseService = databaseService;
    }

    @Override
    protected CADSHealthCheck.Result check() {
        CADSHealthCheck.Result rtn;
        try {
            databaseService.health();
            rtn = CADSHealthCheck.Result.healthy();
        } catch (Exception e) {
            rtn = CADSHealthCheck.Result.unhealthy(e);
        }
        return rtn;
    }
}

