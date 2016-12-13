package uk.gov.dwp.carersallowance.controller.started;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

//import gov.dwp.carers.monitor.MonitorRegistration; TODO artifactory

/**
 * Created by peterwhitehead on 10/11/2016.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"uk.gov.dwp.carersallowance", "gov.dwp.carers"})
@PropertySource("classpath:/config/application-info.properties")
public class C3Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(C3Application.class);

    @Value("${server.port}")
    private String serverPort;

    @Value("${env.name}")
    private String envName;

    @Value("${application.name}")
    private String appName;

    @Inject
//    private MonitorRegistration monitorRegistration;  TODO artifactory

    //private CarersScheduler carersScheduler;

    @PostConstruct
    public void onStart() throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Starting application with - serverPort:" + serverPort + " envName:" + envName + " appName:" + appName);
            LOGGER.info(appName + " is now starting.");
        }

//        monitorRegistration.registerReporters();  TODO artifactory

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(appName + " started.");
        }
    }

    @PreDestroy
    public void onStop() {
//        monitorRegistration.unRegisterReporters();  TODO artifactory
//        monitorRegistration.unRegisterHealthChecks();  TODO artifactory
        //carersScheduler.stop();
    }

//    @Inject
//    private void registerHealthChecks(final DBHealthCheck dbHealthCheck) {
//        if (LOGGER.isInfoEnabled()) {
//            LOGGER.info(appName + " - registering health checks.");
//        }
//        monitorRegistration.registerHealthChecks(Arrays.asList(dbHealthCheck));
//    }
//
//    @Inject
//    private void startPurgeDatabaseSchedule(final DatabasePurgeServiceImpl databasePurgeServiceImpl,
//                                            @Value("${database.purge.scheduler.hours}") final Long databasePurgeSchedulerHours) {
//        if (LOGGER.isInfoEnabled()) {
//            LOGGER.info(appName + " - starting purge database scheduler.");
//        }
//        this.carersScheduler = new CarersScheduler(appName, "purge-database", databasePurgeServiceImpl);
//        this.carersScheduler.start(databasePurgeSchedulerHours, TimeUnit.HOURS);
//    }

    public static void main(final String... args) throws Exception {
        final SpringApplication springApplication = new SpringApplication(C3Application.class);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.run(args);
    }
}

