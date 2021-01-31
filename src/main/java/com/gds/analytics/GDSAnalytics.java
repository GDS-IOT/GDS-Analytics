package com.gds.analytics;

import com.gds.analytics.processor.GDSAnalyticsProcessor;
import com.gds.domain.GDSData;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
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
        data[18] = 69;

        data[19] = 0;

        data[20] = 20;

        // Water Level Data
        data[21] = 80;

        GDSData gdsData = new GDSData();
        gdsData.setGdsData(data);


//        process.processData(gdsData);
    }

}
