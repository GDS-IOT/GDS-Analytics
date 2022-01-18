package com.gds.analytics.adapter;

import com.gds.analytics.converter.Converter;
import com.gds.analytics.domain.FlowmeterEvent;
import com.gds.analytics.domain.WaterLevelEvent;
import com.gds.analytics.utils.GDSUtil;
import com.gds.domain.GDSData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Sujith Ramanathan
 */
@Service
public class FlowmeterToWaterLvlAdapter {

    @Autowired
    private Converter<WaterLevelEvent> waterLevelEventConverter;

    @Autowired
    private GDSUtil gdsUtil;

    public WaterLevelEvent convertToWaterLevelEvent(FlowmeterEvent flowmeterEvent){
        GDSData gdsData = new GDSData();
        gdsData.setTs(gdsUtil.getCurrentDateTimeString());
        gdsData.setGdsData(flowmeterEvent.getData());
        WaterLevelEvent waterLevelEvent = waterLevelEventConverter.convert(gdsData);
        waterLevelEvent.setWaterLevelPercentage(flowmeterEvent.getMeta1());
        waterLevelEvent.setWaterLevelCm(flowmeterEvent.getMeta2());
        return waterLevelEvent;
    }

}
