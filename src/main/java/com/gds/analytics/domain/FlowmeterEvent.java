package com.gds.analytics.domain;

import com.gds.domain.GDSData;

/**
 * @author Sujith Ramanathan
 */
public class FlowmeterEvent extends GDSBase {

    private int meta1;

    private int meta2;

    private String flowMeterApiTriggerEventId;

    public int getMeta1() {
        return meta1;
    }

    public void setMeta1(int meta1) {
        this.meta1 = meta1;
    }

    public int getMeta2() {
        return meta2;
    }

    public void setMeta2(int meta2) {
        this.meta2 = meta2;
    }

    public String getFlowMeterApiTriggerEventId() {
        return flowMeterApiTriggerEventId;
    }

    public void setFlowMeterApiTriggerEventId(String flowMeterApiTriggerEventId) {
        this.flowMeterApiTriggerEventId = flowMeterApiTriggerEventId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FlowmeterEvent{");
        sb.append("meta1=").append(meta1);
        sb.append(", meta2=").append(meta2);
        sb.append('}');
        return super.toString().concat(sb.toString());
    }
}
