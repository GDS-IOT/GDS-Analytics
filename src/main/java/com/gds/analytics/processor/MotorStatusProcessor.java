package com.gds.analytics.processor;

import com.gds.analytics.converter.Converter;
import com.gds.analytics.dao.MotorStatusDao;
import com.gds.analytics.domain.MotorStatusEvent;
import com.gds.analytics.http.MotorStatusApi;
import com.gds.domain.GDSData;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author Sujith Ramanathan
 */
@Service
@Qualifier("MotorStatusProcessor")
public class MotorStatusProcessor implements Processor {

    private static final Logger LOGGER = Logger.getLogger(MotorStatusProcessor.class);

    @Autowired
    private Converter<MotorStatusEvent> motorStatusEventConverter;

    @Autowired
    private MotorStatusDao motorStatusDao;

    @Autowired
    private MotorStatusApi motorStatusApi;

    @Override
    public void processData(GDSData gdsData) {
        LOGGER.debug("Processing Motor Status Processor ");
        MotorStatusEvent motorStatusEvent = motorStatusEventConverter.convert(gdsData);
        int eventId = motorStatusEvent.getEventId();
        if (eventId < 74) {
            LOGGER.debug("Motor HB LOG ".concat(String.valueOf(eventId)));
            motorStatusDao.insertMotorHB(motorStatusEvent);
        } else {
            LOGGER.debug("Motor status to API ".concat(String.valueOf(eventId)));
            sendMotorStatusToApi(motorStatusEvent);
        }
    }

    private void sendMotorStatusToApi(MotorStatusEvent motorEvent) {
        motorStatusApi.sendMotorStatusToApi(motorEvent);
    }
}
