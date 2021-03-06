package com.gds.analytics.engine;

import com.gds.analytics.domain.WaterLevelEvent;
import com.gds.analytics.domain.WaterLevelSeries;
import com.gds.analytics.http.WaterLevelApi;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Sujith Ramanathan
 * <p>
 * Version 1
 */
@Component
public class WaterLevelEngine extends BackgroundEngine<WaterLevelSeries> {

    private static final Logger LOGGER = Logger.getLogger(WaterLevelEngine.class);

    @Autowired
    private WaterLevelApi waterLevelApi;

    private static final int MINIMUM_PACKETES_TO_CONFIRM = 6;
    private static final int TRIGGER_THRESHOLD_SIZE = 3;
    private static final int THRESHOLD_TIME = -15;
    private static final int DEFAULT_PACKET_SIZE = 10;
    private static final int ROTATIONAL_PACKET_SIZE = DEFAULT_PACKET_SIZE - 1;

    private void analyzeWaterLevelSeries(WaterLevelSeries waterLevelSeries, long latestTs) {
        long thresholdTime = getThresholdTime(latestTs);

        List<WaterLevelEvent> filteredData = new ArrayList<WaterLevelEvent>();

        for (WaterLevelEvent data : waterLevelSeries.getWaterLevelSeries()) {
            if (data.getTs() >= thresholdTime && data.getTs() <= latestTs) {
                filteredData.add(data);
            } else {
                LOGGER.debug(
                        "analyzeWaterLevelSeries() :: Resetting data as timestamp constraint violated data.getTs() "
                                + data.getTs() + ", ThresholdTs " + thresholdTime);
                waterLevelSeries.reset();
//                filteredData.add(data);
            }
        }

        analyze(filteredData, waterLevelSeries);
    }

    private void analyze(List<WaterLevelEvent> filteredData, WaterLevelSeries waterLevelSeries) {
        checkStableToIncrease(filteredData, waterLevelSeries);
        checkIncreaseToStable(filteredData, waterLevelSeries);

        checkStableToStable(filteredData, waterLevelSeries);

        checkStableToDecrease(filteredData, waterLevelSeries);
        checkDecreaseToStable(filteredData, waterLevelSeries);
    }

    private void checkStableToIncrease(List<WaterLevelEvent> filteredData, WaterLevelSeries waterLevelSeries) {
        LOGGER.debug("checkStableToIncrease () :: isIncreaseTriggered() " + waterLevelSeries.isStableToIncrease());
        if (!waterLevelSeries.isStableToIncrease()) {
            int filteredDataSize = filteredData.size();
            LOGGER.debug("checkStableToIncrease() :: filertedDataSize :: " + filteredDataSize + " , MINIMUM_PACKETES_TO_CONFIRM :: "
                    + MINIMUM_PACKETES_TO_CONFIRM);
            if (filteredDataSize >= MINIMUM_PACKETES_TO_CONFIRM) {
                int defaultWaterLevel = filteredData.get(0).getWaterLevelCm();
                int triggerCount = 0;
                int waterLevel = 0;

                for (int i = 1; i < filteredDataSize; i++) {
                    waterLevel = filteredData.get(i).getWaterLevelCm();
                    LOGGER.debug("S-To-I WaterLevel :: " + waterLevel + ", DefaultWaterLevel :: " + defaultWaterLevel);

                    if (waterLevel > defaultWaterLevel) {
                        defaultWaterLevel = waterLevel;
                        ++triggerCount;
                    }
                    if (triggerCount == TRIGGER_THRESHOLD_SIZE) {
                        LOGGER.debug("3 packetes were increased");
                        break;
                    }
                }

                if (triggerCount == TRIGGER_THRESHOLD_SIZE) {
                    defaultWaterLevel = filteredData.get(0).getWaterLevelCm();
                    triggerCount = 0;
                    waterLevel = 0;
                    int[] stableAndValue = getStablePatternAndValue(filteredData);
                    if (1 == stableAndValue[0]) {
                        triggerCount = 3;
                        defaultWaterLevel = stableAndValue[1];
                    }
                    for (int i = 0; i < filteredDataSize; i++) {
                        waterLevel = filteredData.get(i).getWaterLevelCm();
                        LOGGER.debug("checkStableToIncrease() :: filteredDataSize " + filteredDataSize + " waterLevel " + waterLevel + " defaultWaterLvl " + defaultWaterLevel + " triggerCount " + triggerCount);

                        if (triggerCount >= 3 && waterLevel > defaultWaterLevel) {
                            ++triggerCount;
                            defaultWaterLevel = waterLevel;
                            LOGGER.debug("Rechecking-2 " + triggerCount + " waterLevel " + waterLevel + " defaultWaterLevel " + defaultWaterLevel);
                        }
                        if (triggerCount == MINIMUM_PACKETES_TO_CONFIRM) {
                            LOGGER.debug(
                                    "Water level increased :::");
                            String pattern = waterLevelApi.isValidAction("Level_Stable_to_Increase");
                            if (null != pattern) {
                                if (waterLevelApi.isTriggered(waterLevelSeries, pattern)) {
                                    waterLevelSeries.setStableTriggered(false);
                                    waterLevelSeries.setIncreaseToStableTriggered(false);

                                    waterLevelSeries.setStableToIncrease(true);
                                    LOGGER.debug("Stable to Increase triggered ".concat(waterLevelSeries.toString()));
                                } else {
                                    LOGGER.debug("Water level not triggered");
                                }
                            } else {
                                LOGGER.debug("Water level rule returned null");
                            }
                        }


//                        if (waterLevel > defaultWaterLevel) {
//                            beginningWaterLevel = beginningWaterLevel == 0 ? waterLevel : beginningWaterLevel;
//                            defaultWaterLevel = waterLevel;
//                            ++triggerCount;
//                            if (triggerCount == MINIMUM_PACKETES_TO_CONFIRM) {
//                                LOGGER.debug(
//                                        "Water level increased ::: water level increased from " + beginningWaterLevel);
//                                String pattern = waterLevelApi.isValidAction("Level_Stable_to_Increase");
//                                if (null != pattern) {
//                                    if (waterLevelApi.isTriggered(waterLevelSeries, pattern)) {
//                                        waterLevelSeries.setStableToIncrease(true);
//                                        waterLevelSeries.setStableTriggered(false);
//                                    } else {
//                                        LOGGER.debug("Water level not triggered");
//                                    }
//                                } else {
//                                    LOGGER.debug("Water level rule returned null");
//                                }
//                            }
//                        }
                    }
                }
            } else {
                LOGGER.debug("Insufficient packets " + filteredDataSize);
            }
        } else {
            LOGGER.debug("Water level increase not stopped yet.");
        }
    }

    private void checkIncreaseToStable(List<WaterLevelEvent> filteredData, WaterLevelSeries waterLevelSeries) {
        LOGGER.debug("checkincreaseToStable () :: isIncreaseToStableTriggered() "
                + waterLevelSeries.isIncreaseToStableTriggered());
        if (!waterLevelSeries.isIncreaseToStableTriggered()) {
            int filteredDataSize = filteredData.size();
            LOGGER.debug("filertedDataSize :: " + filteredDataSize + " , MINIMUM_PACKETES_TO_CONFIRM :: "
                    + MINIMUM_PACKETES_TO_CONFIRM);
            if (filteredDataSize >= MINIMUM_PACKETES_TO_CONFIRM) {
                int defaultWaterLevel = filteredData.get(0).getWaterLevelCm();
                int triggerCount = 1;
                int waterLevel = 0;

                for (int i = 1; i < filteredDataSize; i++) {
                    waterLevel = filteredData.get(i).getWaterLevelCm();
                    if (triggerCount <= TRIGGER_THRESHOLD_SIZE && waterLevel > defaultWaterLevel) {
                        defaultWaterLevel = waterLevel;
                        ++triggerCount;
                    }
                    if (triggerCount == TRIGGER_THRESHOLD_SIZE) {
                        LOGGER.debug("checkincreaseToStable() :: 3 packetes were increased");
                        break;
                    }
                }

                if (triggerCount == TRIGGER_THRESHOLD_SIZE) {
                    defaultWaterLevel = filteredData.get(filteredDataSize - 1).getWaterLevelCm();
                    int beginningWaterLevel = 0;
                    for (int i = 0; i < filteredDataSize; i++) {
                        waterLevel = filteredData.get(i).getWaterLevelCm();
                        LOGGER.debug("checkIncreaseToStable() :: filteredDataSize " + filteredDataSize + " waterLevel " + waterLevel + " defaultWaterLvl " + defaultWaterLevel + " triggerCount " + triggerCount);
                        if (defaultWaterLevel == waterLevel) {
                            ++triggerCount;
                        }
                        if (triggerCount >= MINIMUM_PACKETES_TO_CONFIRM) {
                            LOGGER.debug("Inc To Stable " + beginningWaterLevel);
                            String pattern = waterLevelApi.isValidAction("Level_Increase_to_Stable");
                            if (null != pattern) {
                                if (waterLevelApi.isTriggered(waterLevelSeries, pattern)) {
                                    waterLevelSeries.setStableToIncrease(false);
                                    waterLevelSeries.setStableTriggered(false);
                                    waterLevelSeries.setIncreaseToStableTriggered(true);
                                    LOGGER.debug("IncreaseToStable Triggered ".concat(waterLevelSeries.toString()));
                                }
                            } else {
                                LOGGER.debug("Increase to Stable pattern type is null");
                            }
                            break;
                        }
                    }
                }
            }
        } else {
            LOGGER.debug("Water level increase not stopped yet.");
        }
    }

    private void checkStableToStable(List<WaterLevelEvent> filteredData, WaterLevelSeries waterLevelSeries) {
        LOGGER.debug("checkStableToStable () ");
        int defaultWaterLevel = filteredData.get(0).getWaterLevelCm(),
                filteredDataSize = filteredData.size(), triggerCount = 0;
        for (int i = 0; i < filteredDataSize; i++) {
            if (defaultWaterLevel == filteredData.get(i).getWaterLevelCm()) {
                ++triggerCount;
            }
        }
        LOGGER.debug("Trigger Count :: " + triggerCount + ", filteredDataSize :: " + filteredDataSize);
        if (filteredDataSize == triggerCount) {
            waterLevelSeries.reset();
            LOGGER.debug("Stable to Stable triggered and resetted");
        }
    }

    private void checkStableToDecrease(List<WaterLevelEvent> filteredData, WaterLevelSeries waterLevelSeries) {
        LOGGER.debug(
                "checkDecrease () :: isStableToDecreaseTriggered() " + waterLevelSeries.isStableToDecreaseTriggered());
        if (!waterLevelSeries.isStableToDecreaseTriggered()) {
            int filteredDataSize = filteredData.size();
            LOGGER.debug("checkDecrease () :: filertedDataSize :: " + filteredDataSize + " , MINIMUM_PACKETES_TO_CONFIRM :: "
                    + MINIMUM_PACKETES_TO_CONFIRM);
            if (filteredDataSize >= MINIMUM_PACKETES_TO_CONFIRM) {
                int defaultWaterLevel = 0;
                int triggerCount = 0;
                int waterLevel = 0;

                defaultWaterLevel = getStableValue(filteredData, filteredDataSize);
                for (int i = 1; i < filteredDataSize; i++) {
                    waterLevel = filteredData.get(i).getWaterLevelCm();
                    LOGGER.debug("WaterLevel :: " + waterLevel + ", DefaultWaterLevel :: " + defaultWaterLevel);
                    if (waterLevel < defaultWaterLevel) {
                        defaultWaterLevel = waterLevel;
                        ++triggerCount;
                    }
                    if (triggerCount == TRIGGER_THRESHOLD_SIZE) {
                        LOGGER.debug("checkStableToDecrease() :: 3 packetes were decreased");
                        break;
                    }
                }

                if (triggerCount == TRIGGER_THRESHOLD_SIZE) {
                    defaultWaterLevel = 0;
                    triggerCount = 0;
                    waterLevel = 0;
                    int decreasingWaterLevel = 0;
                    int stableValue = getStableValue(filteredData, filteredDataSize);
                    if (0 != stableValue) {
                        defaultWaterLevel = stableValue;
                    }
                    for (int i = 0; i < filteredDataSize; i++) {
                        waterLevel = filteredData.get(i).getWaterLevelCm();
                        LOGGER.debug("checkStableToDecrease() :: filteredDataSize " + filteredDataSize + " waterLevel " + waterLevel + " defaultWaterLvl " + defaultWaterLevel + " triggerCount " + triggerCount);

                        // Should start check decrease pattern from stable value. Hence duplicate will be avoided
                        if(stableValue == waterLevel){
                            triggerCount = 3;
                            stableValue = -1;
                        }
                        if (triggerCount >= TRIGGER_THRESHOLD_SIZE && waterLevel < defaultWaterLevel) {
                            ++triggerCount;
                            defaultWaterLevel = waterLevel;
                        }
                        if (triggerCount == MINIMUM_PACKETES_TO_CONFIRM) {
                            LOGGER.debug("water level decreased from " + decreasingWaterLevel);
                            String pattern = waterLevelApi.isValidAction("Level_Stable_to_Decrease");
                            if (null != pattern) {
                                if (waterLevelApi.isTriggered(waterLevelSeries, pattern)) {
                                    waterLevelSeries.setStableToDecreaseTriggered(true);
                                    waterLevelSeries.setDecreaseToStableTriggered(false);
                                    waterLevelSeries.setStableTriggered(false);
                                }
                            } else {
                                LOGGER.debug("Stable to decrease pattern type is null");
                            }
                        }

//                        if (waterLevel < defaultWaterLevel) {
//                            decreasingWaterLevel = decreasingWaterLevel == 0 ? waterLevel : decreasingWaterLevel;
//                            defaultWaterLevel = waterLevel;
//                            ++triggerCount;
//                            if (triggerCount == MINIMUM_PACKETES_TO_CONFIRM) {
//                                LOGGER.debug("water level decreased from " + decreasingWaterLevel);
//                                String pattern = waterLevelApi.isValidAction("Level_Stable_to_Decrease");
//                                if (null != pattern) {
//                                    if (waterLevelApi.isTriggered(waterLevelSeries, pattern)) {
//                                        waterLevelSeries.setStableToDecreaseTriggered(true);
//                                        waterLevelSeries.setDecreaseToStableTriggered(false);
//                                        waterLevelSeries.setStableTriggered(false);
//                                    }
//                                } else {
//                                    LOGGER.debug("Stable to decrease pattern type is null");
//                                }
//                            }
//                        }
                    }
                }
            }
        } else {
            LOGGER.debug("Water level decrease not stopped yet.");
        }
    }

    private void checkDecreaseToStable(List<WaterLevelEvent> filteredData, WaterLevelSeries waterLevelSeries) {
        LOGGER.debug("new checkDecreaseToStable() :: isDecreaseToStableTriggered() "
                + waterLevelSeries.isDecreaseToStableTriggered());
        if (!waterLevelSeries.isDecreaseToStableTriggered()) {
            int filteredDataSize = filteredData.size();
            LOGGER.debug("filertedDataSize :: " + filteredDataSize + " , MINIMUM_PACKETES_TO_CONFIRM :: "
                    + MINIMUM_PACKETES_TO_CONFIRM);
            if (filteredDataSize >= MINIMUM_PACKETES_TO_CONFIRM) {
                int defaultWaterLevel = filteredData.get(0).getWaterLevelCm();
                int triggerCount = 1;
                int waterLevel = 0;

                for (int i = 1; i < filteredDataSize; i++) {
                    waterLevel = filteredData.get(i).getWaterLevelCm();
                    if (triggerCount <= 3 && waterLevel < defaultWaterLevel) {
                        defaultWaterLevel = waterLevel;
                        ++triggerCount;
                    }
                    if (triggerCount == TRIGGER_THRESHOLD_SIZE) {
                        LOGGER.debug("checkDecreaseToStable() :: 3 packetes were decreased");
                        break;
                    }
                }

                if (triggerCount == TRIGGER_THRESHOLD_SIZE) {
                    defaultWaterLevel = filteredData.get(filteredDataSize - 1).getWaterLevelCm();
                    waterLevel = 0;
                    int decreasingWaterLevel = 0;
                    for (int i = 0; i < filteredDataSize; i++) {
                        waterLevel = filteredData.get(i).getWaterLevelCm();
                        LOGGER.debug("checkDecreaseToStable() :: filteredDataSize " + filteredDataSize + " waterLevel " + waterLevel + " defaultWaterLvl " + defaultWaterLevel + " triggerCount " + triggerCount);

                        if (defaultWaterLevel == waterLevel) {
                            ++triggerCount;
                        }

                    }

                    if (triggerCount >= MINIMUM_PACKETES_TO_CONFIRM) {
                        LOGGER.debug("water level decreased from " + decreasingWaterLevel);
                        String pattern = waterLevelApi.isValidAction("Level_Decrease_to_Stable");
                        if (null != pattern) {
                            if (waterLevelApi.isTriggered(waterLevelSeries, pattern)) {
                                waterLevelSeries.setStableToDecreaseTriggered(false);
                                waterLevelSeries.setStableTriggered(false);

                                waterLevelSeries.setDecreaseToStableTriggered(true);
                            }
                        } else {
                            LOGGER.debug("Dec To stable pattern is null");
                        }
                    }
                }
            }
        } else {
            LOGGER.debug("Water level decrease not stopped yet.");
        }
    }

    private int[] getStablePatternAndValue(List<WaterLevelEvent> filteredData) {
        Set<Integer> stablePattern = new HashSet<Integer>();
        int stableLevel = 0;
        for (WaterLevelEvent event : filteredData) {
            if (!stablePattern.add(event.getWaterLevelCm())) {
                stableLevel = event.getWaterLevelCm();
            }
        }
        int[] stablePatternAndValue = new int[2];

        // +1 to match stablePattern.size()
        stablePatternAndValue[0] = ((filteredData.size() - TRIGGER_THRESHOLD_SIZE) + 1) >= stablePattern.size() ? 1 : 0;
        stablePatternAndValue[1] = stableLevel;

        LOGGER.debug("isStableAchieved() ".concat(String.valueOf(stablePatternAndValue[0])).concat(" stableLevel ").concat(String.valueOf(stableLevel)));
        return stablePatternAndValue;
    }

    private int getStableValue(List<WaterLevelEvent> filteredData, int size) {
        int count = 0;
        int nextIndex = 0;
        int value = 0;
        for (int i = 0; i <= size - 1; i++) {
            nextIndex = i + 1;
            if (nextIndex < size && filteredData.get(i).getWaterLevelCm() == filteredData.get(nextIndex).getWaterLevelCm()) {
                count++;
                if (count >= 2) {
                    value = filteredData.get(i).getWaterLevelCm();
                    break;
                }
            } else {
                count = 0;
            }
        }
        LOGGER.debug("getStableValue() :: stableValue = ".concat(String.valueOf(value)));
        return value;
    }


    private long getThresholdTime(long ts) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(ts);
        cal.add(Calendar.MINUTE, THRESHOLD_TIME);
        return cal.getTimeInMillis();
    }

    @Override
    protected void analyze(WaterLevelSeries object, long ts) {
        analyzeWaterLevelSeries(object, ts);
    }


}

