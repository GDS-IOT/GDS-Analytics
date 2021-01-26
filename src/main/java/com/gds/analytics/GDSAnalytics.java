package com.gds.analytics;

import com.gds.analytics.dao.WaterLevelDao;
import com.gds.analytics.domain.WaterLevelEvent;
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

    @Autowired
    private WaterLevelDao waterLevelDao;

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
    }

    public static void main(String[] args) {
        SpringApplication.run(GDSAnalytics.class, args);
    }

}
