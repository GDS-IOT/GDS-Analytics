package com.gds.analytics.processor;

import com.gds.analytics.converter.Converter;
import com.gds.analytics.dao.WaterLevelDao;
import com.gds.analytics.domain.WaterLevelEvent;
import com.gds.analytics.domain.WaterLevelSeries;
import com.gds.analytics.engine.BackgroundEngine;
import com.gds.analytics.utils.WaterLevelDataMapper;
import com.gds.domain.GDSData;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Sujith Ramanathan
 */
@Service
@Qualifier("waterLevelProcessor")
public class WaterLevelProcessor implements Processor {

    private static final Logger LOGGER = Logger.getLogger(WaterLevelProcessor.class);

    @Autowired
    private Converter<WaterLevelEvent> waterLevelConverter;

    @Autowired
    private WaterLevelDao waterLevelDao;

    @Autowired
    private BackgroundEngine<WaterLevelSeries> waterLevelEngine;

    @Autowired
    private WaterLevelDataMapper waterLevelDataMapper;

    @Override
    public void processData(GDSData gdsData) {
        WaterLevelEvent waterLevelEvent = waterLevelConverter.convert(gdsData);
        waterLevelDao.insertWaterLvlData(waterLevelEvent);
        analyzeWaterLevelData(waterLevelEvent, waterLevelEvent.getTs());
    }

    private void analyzeWaterLevelData(WaterLevelEvent waterLevelEvent, long ts) {
        Map<String, WaterLevelSeries> waterMapper = waterLevelDataMapper.getWaterLevelMapper();
        WaterLevelSeries wls = null;

        String rfIdAndDeviceId = String.valueOf(waterLevelEvent.getSystemIdAsInt()).concat("-")
                .concat(String.valueOf(waterLevelEvent.getDeviceIdAsInt()));
        if (waterMapper.containsKey(rfIdAndDeviceId)) {
            LOGGER.debug(rfIdAndDeviceId.concat(" Device already exists, Hence Appending to it"));
            wls = waterMapper.get(rfIdAndDeviceId);
            wls.add(waterLevelEvent);
        } else {
            wls = new WaterLevelSeries();
            wls.add(waterLevelEvent);
        }
        waterMapper.put(rfIdAndDeviceId, wls);
        waterLevelEngine.runBackground(wls, ts);
    }
}