package com.gds.analytics.converter;

import com.gds.analytics.constants.Constants;
import com.gds.analytics.domain.WaterLevelEvent;
import com.gds.domain.GDSData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Sujith Ramanathan
 */
@Service
public class WaterLevelConverterImpl extends Converter<WaterLevelEvent> {

    @Value("${"+ Constants.WATER_LEVEL_EVENT_ID +"}")
    private int eventId;

    @Value("${" + Constants.EVENT_ID_IDX + "}")
    private int eventIdIdx;

    @Value("${"+Constants.WATER_LVL_PERCENTAGE_IDX+"}")
    private int waterLvlPercentageIdx;

    @Override
    public WaterLevelEvent convert(GDSData gdsData) {
        WaterLevelEvent wl = new WaterLevelEvent();
        super.setBaseData(wl, gdsData);
        byte []data = gdsData.getGdsData();
        wl.setEventId((int)data[eventIdIdx]);
        wl.setWaterLevelPercentage((int)data[waterLvlPercentageIdx]);
        return wl;
    }
}