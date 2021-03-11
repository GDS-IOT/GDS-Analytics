package com.gds.analytics.events;

import com.gds.analytics.constants.GDSEnum;
import com.gds.analytics.processor.Processor;
import com.gds.analytics.constants.Constants;
import com.gds.domain.GDSData;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EventFactory {

    private static final Logger LOGGER = Logger.getLogger(EventFactory.class);

    @Value("${" + Constants.PACKET_TYPE_IDX + "}")
    private int pacTypeIdx;

    @Value("${" + Constants.EVENT_ID_IDX + "}")
    private int eventIdIdx;

    @Value("${"+ Constants.MOTOR_HEART_BEAT_EVENT +"}")
    private int motorStatusHeartBeatEvent;

    @Value("${" + Constants.HEART_BEAT_EVENT + "}")
    private int heartBeatEvent;

    @Autowired
    @Qualifier("heartBeatProcessor")
    private Processor heartBeatProcessor;

    @Autowired
    @Qualifier("waterLevelProcessor")
    private Processor waterLevelProcessor;

    @Autowired
    @Qualifier("MotorStatusProcessor")
    private Processor motorStatusProcessor;

    public void processEvent(String key, GDSData data) {
        if (heartBeatEvent == data.getGdsData()[pacTypeIdx]) {
            LOGGER.debug("HeartBeat Event Initiated.");
            heartBeatProcessor.processData(data);
        } else {
            int eventId = (int) data.getGdsData()[eventIdIdx];
            LOGGER.debug("Event Id ".concat(String.valueOf(eventId)));
            GDSEnum event = GDSEnum.getEventById(eventId);
            LOGGER.debug("event.toString() " + event);
            if (null == event) {
                event = GDSEnum.DEFAULT;
            }
            switch (event) {
                case WATER_LEVEL_STATUS:
                    waterLevelProcessor.processData(data);
                    break;
                case MOTOR_STATUS_HB_EVENT:
                    motorStatusProcessor.processData(data);
                    break;
                case DEFAULT:
                    LOGGER.error("Not a valid event");
            }
        }
    }
}
