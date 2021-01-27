package com.gds.analytics.processor;

import com.gds.analytics.converter.Converter;
import com.gds.analytics.dao.WaterLevelDao;
import com.gds.analytics.domain.WaterLevelEvent;
import com.gds.domain.GDSData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author Sujith Ramanathan
 */
@Qualifier("WaterLevelProcessor")
public class WaterLevelProcessor implements Processor {

    @Autowired
    private Converter<WaterLevelEvent> waterLevelConverter;

    @Autowired
    private WaterLevelDao waterLevelDao;

    @Override
    public void processData(GDSData gdsData) {
        WaterLevelEvent waterLevelEvent = waterLevelConverter.convert(gdsData);
        waterLevelDao.insertWaterLvlData(waterLevelEvent);
    }
}

// select * from smt.sow_event_transaction set2

// select * from smt.sow_event_transaction_meta setm

// select * from smt.sow_event_mapping sem