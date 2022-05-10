package com.gds.analytics.converter;

import com.gds.analytics.constants.Constants;
import com.gds.analytics.domain.FlowmeterEvent;
import com.gds.domain.GDSData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Sujith Ramanathan
 */
@Service
public class FlowMeterConverterImpl extends Converter<FlowmeterEvent> {

    @Value("${" + Constants.FLOW_EVENT_EVENT + "}")
    private int eventId;

    @Value("${" + Constants.EVENT_ID_IDX + "}")
    private int eventIdIdx;

    @Value("${" + Constants.FLOW_METER_META1_START_IDX + "}")
    private int meta1StartIdx;

    @Value("${" + Constants.FLOW_METER_META1_END_IDX + "}")
    private int meta1EndIdx;

    @Value("${" + Constants.FLOW_METER_META2_START_IDX + "}")
    private int meta2StartIdx;

    @Value("${" + Constants.FLOW_METER_META2_END_IDX + "}")
    private int meta2EndIdx;

    @Value("${" + Constants.FLOW_METER_LEVEL_EVENT + "}")
    private String flowMeterApiTriggerEventId;

    @Override
    public FlowmeterEvent convert(GDSData gdsData) {
        FlowmeterEvent fe = new FlowmeterEvent();
        super.setBaseData(fe, gdsData);
        setMetaData(gdsData, fe);
        return fe;
    }

    public void setMetaData(GDSData gdsData, FlowmeterEvent flowmeterEvent) {
        byte []data = gdsData.getGdsData();
        int meta1 = Integer.parseInt(super.getString(data, meta1StartIdx, meta1EndIdx, ""));
        int meta2 = Integer.parseInt(super.getString(data, meta2StartIdx, meta2EndIdx, ""));
        flowmeterEvent.setMeta1(meta1);
        flowmeterEvent.setMeta2(meta2);
        flowmeterEvent.setFlowMeterApiTriggerEventId(flowMeterApiTriggerEventId);
    }
}
