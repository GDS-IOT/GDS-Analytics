package com.gds.analytics.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sujith Ramanathan
 */
public class WaterLevelSeries {


    private List<WaterLevelEvent> waterLevelSeries;
    private boolean stableToIncrease;
    private boolean increaseToStableTriggered;
    private boolean stableToDecreaseTriggered;
    private boolean decreaseToStableTriggered;
    private boolean stableTriggered;

    private static final int DEFAULT_PACKET_SIZE = 10;
    private static final int ROTATIONAL_PACKET_SIZE = DEFAULT_PACKET_SIZE - 1;

    public WaterLevelSeries() {
        this.waterLevelSeries = new ArrayList<WaterLevelEvent>();
    }

    public boolean isStableToIncrease() {
        return stableToIncrease;
    }

    public boolean isIncreaseToStableTriggered() {
        return increaseToStableTriggered;
    }

    public boolean isStableToDecreaseTriggered() {
        return stableToDecreaseTriggered;
    }

    public boolean isDecreaseToStableTriggered() {
        return decreaseToStableTriggered;
    }

    public boolean isStableTriggered() {
        return stableTriggered;
    }

    public void setWaterLevelSeries(List<WaterLevelEvent> waterLevelSeries) {
        this.waterLevelSeries = waterLevelSeries;
    }

    public void setStableToIncrease(boolean stableToIncrease) {
        this.stableToIncrease = stableToIncrease;
    }

    public void setIncreaseToStableTriggered(boolean increaseToStableTriggered) {
        this.increaseToStableTriggered = increaseToStableTriggered;
    }

    public void setStableToDecreaseTriggered(boolean stableToDecreaseTriggered) {
        this.stableToDecreaseTriggered = stableToDecreaseTriggered;
    }

    public void setDecreaseToStableTriggered(boolean decreaseToStableTriggered) {
        this.decreaseToStableTriggered = decreaseToStableTriggered;
    }

    public void setStableTriggered(boolean stableTriggered) {
        this.stableTriggered = stableTriggered;
    }

    public List<WaterLevelEvent> getWaterLevelSeries() {
        return waterLevelSeries;
    }

    public void reset() {
        this.stableToIncrease = false;
        this.stableToDecreaseTriggered = false;
        this.stableTriggered = false;
        this.increaseToStableTriggered = true;
        this.decreaseToStableTriggered = true;
    }

    public void add(WaterLevelEvent data) {
        if (waterLevelSeries.size() < DEFAULT_PACKET_SIZE) {
            waterLevelSeries.add(data);
        } else {
            for (int i = 0; i < DEFAULT_PACKET_SIZE; i++) {
                if (i < ROTATIONAL_PACKET_SIZE) {
                    waterLevelSeries.set(i, waterLevelSeries.get(i + 1));
                } else {
                    waterLevelSeries.set(i, data);
                }
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WaterLevelSeries{");
        sb.append("waterLevelSeries=").append(waterLevelSeries);
        sb.append(", stableToIncrease=").append(stableToIncrease);
        sb.append(", increaseToStableTriggered=").append(increaseToStableTriggered);
        sb.append(", stableToDecreaseTriggered=").append(stableToDecreaseTriggered);
        sb.append(", decreaseToStableTriggered=").append(decreaseToStableTriggered);
        sb.append(", stableTriggered=").append(stableTriggered);
        sb.append('}');
        return sb.toString();
    }
}

