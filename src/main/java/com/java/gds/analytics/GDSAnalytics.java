package com.java.gds.analytics;

import com.java.gds.analytics.processor.GDSAnalyticsProcessor;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class GDSAnalytics {

    private static final Logger LOGGER = Logger.getLogger(GDSAnalytics.class);

    @Autowired
    private GDSAnalyticsProcessor kafkaProcessor;


    @PostConstruct
    public void initProperties() {
        PropertyConfigurator.configure(this.getClass().getClassLoader().getResourceAsStream("log4j.properties"));
        LOGGER.debug("GDS-Analytics Init Properties - Success ");
        byte []data = new byte[18];
        data[0] = 18;
        data[1] = 1;
        data[2] = 13;
        data[3] = 74;
        data[4] = 0;
        // Device Id
        data[5] = 0;
        data[6] = 0;
        data[7] = 0;
        // Device Type
        data[8] = 26;
        // Origin RSSI
        data[9] = 45;
        // Origin Network Level
        data[10] = 1;
        // Hop Counter
        data[11] = 1;
        // Message Counter
        data[12] = 0;
        data[13] = 0;
        // Latency Counter
        data[14] = 1;
        data[15] = 86;
        // Packet Type
        data[16] = 2;
        // message Type
        data[17] = 9;

        kafkaProcessor.processMessage();

    }

    public static void main(String[] args) {
        SpringApplication.run(GDSAnalytics.class, args);
    }

}
