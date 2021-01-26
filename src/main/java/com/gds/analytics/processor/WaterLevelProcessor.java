package com.gds.analytics.processor;

import com.gds.analytics.converter.Converter;
import com.gds.analytics.domain.WaterLevelEvent;
import com.gds.domain.GDSData;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Sujith Ramanathan
 */
public class WaterLevelProcessor implements Processor{

    @Autowired
    private Converter<WaterLevelEvent> waterLevelConverter;

    @Override
    public void processData(GDSData gdsData) {
        waterLevelConverter.convert(gdsData);
    }
}

// select * from smt.sow_event_transaction set2

// select * from smt.sow_event_transaction_meta setm

// select * from smt.sow_event_mapping sem