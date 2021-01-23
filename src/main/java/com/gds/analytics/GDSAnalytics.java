package com.gds.analytics;

import com.gds.analytics.processor.GDSAnalyticsProcessor;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class GDSAnalytics {

    private static final Logger LOGGER = Logger.getLogger(GDSAnalytics.class);

    @Autowired
    private GDSAnalyticsProcessor gdsAnalyticsProcessor;


    @PostConstruct
    public void initProperties() {
        PropertyConfigurator.configure(this.getClass().getClassLoader().getResourceAsStream("log4j.properties"));
        LOGGER.debug("GDS-Analytics Init Properties - Success ");
        gdsAnalyticsProcessor.processMessage();

    }

    public static void main(String[] args) {
        SpringApplication.run(GDSAnalytics.class, args);
    }

}
