package com.gds.analytics.dao;

import com.gds.analytics.domain.WaterLevelEvent;
import org.springframework.stereotype.Service;

/**
 * @author Sujith Ramanathan
 */
public interface WaterLevelDao {

    public void insertWaterLvlData(WaterLevelEvent waterLevelEventData);
}
