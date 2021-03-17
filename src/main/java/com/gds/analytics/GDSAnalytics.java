package com.gds.analytics;

import com.gds.analytics.processor.GDSAnalyticsProcessor;
import com.gds.analytics.processor.Processor;
import com.gds.domain.GDSData;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.Duration;

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


//        WaterLevelEvent waterLevelEvent = new WaterLevelEvent();
//        waterLevelEvent.setWaterLevelPercentage(108);
//        waterLevelEvent.setDeviceType(27);
//        waterLevelEvent.setEventId(69);
//        waterLevelEvent.setSystemId("11375");
//        waterLevelEvent.setDateTime("2021-01-26 16:52:00");
//        waterLevelDao.insertWaterLvlData(waterLevelEvent);

//        test();

    }

    public void test(){
        System.out.println("Coming here");
        byte []data = new byte[22];
        data[0] = 18;
        data[1] = 1;
        data[2] = 13;
        data[3] = 75;
        data[4] = 0;
        // Device Id
        data[5] = 0;
        data[6] = 0;
        data[7] = 0;
        // Device Type
        data[8] = 27;
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
        data[16] = 10;
        // message Type
        data[17] = 0;

        // Event Id
        data[18] = 74;

        data[19] = 0;

        data[20] = 20;

        // Water Level Data
        data[21] = 80;

        GDSData gdsData = new GDSData();
//        yyyy-MM-dd HH:mm:ss
        gdsData.setTs("2021-03-14 22:30:54");
        gdsData.setGdsData(data);


//        process.processData(gdsData);
//        motorStatusProcessor.processData(gdsData);
    }

    public static void main(String[] args) {

        try {
            SpringApplication.run(GDSAnalytics.class, args);
        } catch (Exception e) {
            LOGGER.error("Error while starting up the application ", e);
        }
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(10))
                .build();
    }

}
