package com.gds.analytics.processor;

import com.gds.analytics.converter.Converter;
import com.gds.analytics.converter.WaterLevelConverterImpl;
import com.gds.analytics.dao.WaterLevelDao;
import com.gds.analytics.domain.WaterLevelEvent;
import com.gds.analytics.engine.BackgroundEngine;
import com.gds.domain.GDSData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.stereotype.Service;

/**
 * @author Sujith Ramanathan
 */
@Service
@Qualifier("waterLevelProcessor")
public class WaterLevelProcessor implements Processor {

    @Autowired
    private Converter<WaterLevelEvent> waterLevelConverter;

    @Autowired
    private WaterLevelDao waterLevelDao;

    @Autowired
    private BackgroundEngine<WaterLevelEvent> waterLevelEngine;

    @Override
    public void processData(GDSData gdsData) {
        WaterLevelEvent waterLevelEvent = waterLevelConverter.convert(gdsData);
        waterLevelDao.insertWaterLvlData(waterLevelEvent);
//        waterLevelEngine.runBackground(waterLevelEvent);
    }

}