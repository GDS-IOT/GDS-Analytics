package com.gds.analytics.processor;

import com.gds.analytics.converter.Converter;
import com.gds.analytics.domain.FlowmeterEvent;
import com.gds.domain.GDSData;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Sujith Ramanathan
 */
public class FlowMeterProcessor implements Processor {

    @Autowired
    private Converter<FlowmeterEvent> flowmeterEventConverter;


    @Override
    public void processData(GDSData gdsData) {
        FlowmeterEvent fe = flowmeterEventConverter.convert(gdsData);
    }
}
