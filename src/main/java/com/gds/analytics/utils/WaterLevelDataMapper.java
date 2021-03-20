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

    private Map<String, WaterLevelSeries> map = new HashMap<String, WaterLevelSeries>();

    public Map<String, WaterLevelSeries> getWaterLevelMapper() {
        return map;
    }
}
