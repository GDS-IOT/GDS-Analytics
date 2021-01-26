package com.gds.analytics.dao;

import com.gds.analytics.domain.WaterLevelEvent;

/**
 * @author Sujith Ramanathan
 */
public interface WaterLevelDao {

    public void insertWaterLvlData(WaterLevelEvent waterLevelEventData);
}
