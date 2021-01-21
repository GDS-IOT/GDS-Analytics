package com.java.gds.analytics;

import com.java.gds.analytics.constants.Constants;
import com.java.gds.analytics.utils.GDSConverter;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class GDSAnalytics {

    private static final Logger LOGGER = Logger.getLogger(GDSAnalytics.class);

    @Autowired
    private GDSConverter converter;

    @PostConstruct
    public void initProperties() {
        PropertyConfigurator.configure(this.getClass().getClassLoader().getResourceAsStream("log4j.properties"));
        LOGGER.debug("GDS-Analytics Init Properties - Success ");
        byte []data = new byte[10];
        data[0] = 10;
        data[1] = 1;
        data[2] = 13;
        data[3] = 74;
        data[4] = 0;
        data[5] = 0;
        data[6] = 0;
        data[7] = 0;
        data[8] = 9;
        data[9] = 9;
        System.out.println(converter.convertToHeartBeat(data).toString());
    }

    public static void main(String[] args) {
        SpringApplication.run(GDSAnalytics.class, args);
    }

}
