package com.java.gds.analytics.events;

import com.java.gds.analytics.constants.Constants;
import com.java.gds.analytics.processor.Processor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EventFactory {

    private static final Logger LOGGER = Logger.getLogger(EventFactory.class);

    @Value("${"+ Constants.PACKET_TYPE_IDX +"}")
    private int pacTypeIdx;

    @Value("${"+Constants.HEART_BEAT_EVENT+"}")
    private int heartBeatEvent;

    @Autowired
    private Processor heartBeatProcessor;


    public void processEvent(String key, byte[] data) {
        if(heartBeatEvent == data[pacTypeIdx]) {
            LOGGER.debug("HeartBeat Event Initiated.");
            heartBeatProcessor.processData(data);
        }
    }
}
