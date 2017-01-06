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

import gov.dwp.carers.monitor.MonitorRegistration;
import org.springframework.scheduling.annotation.EnableAsync;
import uk.gov.dwp.carersallowance.monitoring.ClaimReceivedConnectionCheck;
import uk.gov.dwp.carersallowance.monitoring.DBHealthCheck;
import uk.gov.dwp.carersallowance.monitoring.SessionDataConnectionCheck;

import java.util.Arrays;

/**
 * Created by peterwhitehead on 10/11/2016.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"uk.gov.dwp.carersallowance", "gov.dwp.carers"})
@PropertySource("classpath:/config/application-info.properties")
@EnableAsync
public class C3Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(C3Application.class);

    @Value("${server.port}")
    private String serverPort;

    @Value("${env.name}")
    private String envName;

    @Value("${application.name}")
    private String appName;

    @Inject
    private MonitorRegistration monitorRegistration;

    @PostConstruct
    public void onStart() throws Exception {
        LOGGER.info("Starting application with - serverPort:{} envName:{} appName:{}", serverPort, envName, appName);
        LOGGER.info("{} is now starting.", appName);

        monitorRegistration.registerReporters();

        LOGGER.info("{} started.", appName);
    }

    @PreDestroy
    public void onStop() {
        monitorRegistration.unRegisterReporters();
        monitorRegistration.unRegisterHealthChecks();
    }

    @Inject
    private void registerHealthChecks(final DBHealthCheck dbHealthCheck,
                                      final ClaimReceivedConnectionCheck claimReceivedConnectionCheck,
                                      final SessionDataConnectionCheck sessionDataConnectionCheck) {
        LOGGER.info("{} - registering health checks.", appName);

        monitorRegistration.registerHealthChecks(Arrays.asList(dbHealthCheck, claimReceivedConnectionCheck, sessionDataConnectionCheck));
    }

    public static void main(final String... args) throws Exception {
        final SpringApplication springApplication = new SpringApplication(C3Application.class);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.run(args);
    }
}

