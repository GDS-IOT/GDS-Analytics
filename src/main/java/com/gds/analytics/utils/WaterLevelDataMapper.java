package com.gds.analytics.utils;

import com.gds.analytics.domain.WaterLevelSeries;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sujith Ramanathan
 */
@Service
public class WaterLevelDataMapper {

    private Map<Integer, WaterLevelSeries> map = new HashMap<Integer, WaterLevelSeries>();

    public Map<Integer, WaterLevelSeries> getWaterLevelMapper() {
        return map;
    }
}
