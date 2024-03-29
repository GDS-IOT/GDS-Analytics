package com.gds.analytics.processor;

import com.gds.analytics.adapter.FlowmeterToWaterLvlAdapter;
import com.gds.analytics.converter.Converter;
import com.gds.analytics.dao.WaterLevelDao;
import com.gds.analytics.domain.FlowmeterEvent;
import com.gds.analytics.domain.WaterLevelEvent;
import com.gds.analytics.domain.WaterLevelSeries;
import com.gds.analytics.engine.BackgroundEngine;
import com.gds.analytics.utils.WaterLevelDataMapper;
import com.gds.domain.GDSData;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Sujith Ramanathan
 */
@Service
@Qualifier("FlowMeterProcessor")
public class FlowMeterProcessor implements Processor {

    private static final Logger logger = Logger.getLogger(FlowMeterProcessor.class);

    @Autowired
    private Converter<FlowmeterEvent> flowmeterEventConverter;

    @Autowired
    private FlowmeterToWaterLvlAdapter flowmeterToWaterLvlAdapter;

    @Autowired
    private BackgroundEngine<WaterLevelSeries> waterLevelEngine;

    @Autowired
    private WaterLevelDataMapper waterLevelDataMapper;

    @Autowired
    private WaterLevelDao waterLevelDao;

    @Override
    public void processData(GDSData gdsData) {
        FlowmeterEvent fe = flowmeterEventConverter.convert(gdsData);
        WaterLevelEvent waterLevelEvent = flowmeterToWaterLvlAdapter.convertToWaterLevelEvent(fe);
        logger.debug("Flowmeter Inserting into event meta");
        waterLevelDao.insertWaterLvlData(waterLevelEvent);
        logger.debug("Flowmeter Succefully inserted into event meta txn's");
        analyzeFlowMeter(waterLevelEvent, waterLevelEvent.getTs());
    }

    private void analyzeFlowMeter(WaterLevelEvent waterLevelEvent, long ts) {
        Map<String, WaterLevelSeries> waterMapper = waterLevelDataMapper.getWaterLevelMapper();
        WaterLevelSeries wls;
        String rfIdAndDeviceIdAndFm = String.format("%d-%d-fm", waterLevelEvent.getSystemIdAsInt(), waterLevelEvent.getDeviceIdAsInt());
        if (waterMapper.containsKey(rfIdAndDeviceIdAndFm)) {
            logger.debug(rfIdAndDeviceIdAndFm.concat(" Device already exists, Hence Appending to it"));
            wls = waterMapper.get(rfIdAndDeviceIdAndFm);
            wls.add(waterLevelEvent);
        } else {
            logger.debug(rfIdAndDeviceIdAndFm.concat(" New device id"));
            wls = new WaterLevelSeries();
            wls.add(waterLevelEvent);
        }
        waterMapper.put(rfIdAndDeviceIdAndFm, wls);
        waterLevelEngine.runBackground(wls, ts);
    }
}
