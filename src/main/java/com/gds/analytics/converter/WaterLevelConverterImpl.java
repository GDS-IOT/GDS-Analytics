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

    @Value("${" + Constants.WATER_LEVEL_EVENT_ID + "}")
    private int eventId;

    @Value("${" + Constants.EVENT_ID_IDX + "}")
    private int eventIdIdx;

    @Value("${" + Constants.WATER_LVL_CM_IDX + "}")
    private int waterLvlCmIdx;

    @Value("${" + Constants.WATER_LVL_PERCENTAGE_IDX + "}")
    private int waterLvlPercentageIdx;

    @Value("${" + Constants.API_WATER_LEVEL_EVENT + "}")
    private String waterLevelApiTriggerEventId;

    @Override
    public WaterLevelEvent convert(GDSData gdsData) {
        WaterLevelEvent wl = new WaterLevelEvent();
        super.setBaseData(wl, gdsData);
        byte[] data = gdsData.getGdsData();
        wl.setEventId((int) data[eventIdIdx]);
        int waterLevelCm = ((int) data[waterLvlCmIdx] * 100) + (int) data[waterLvlCmIdx + 1];
//        wl.setWaterLevelPercentage((int)data[waterLvlPercentageIdx]);
        wl.setWaterLevelPercentage(data[waterLvlPercentageIdx]);
        wl.setWaterLevelCm(waterLevelCm);
        wl.setWaterLevelTriggerId(waterLevelApiTriggerEventId);
        return wl;
    }
}
