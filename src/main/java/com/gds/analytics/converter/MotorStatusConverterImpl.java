package com.gds.analytics.converter;

import com.gds.analytics.constants.Constants;
import com.gds.analytics.domain.MotorStatusEvent;
import com.gds.domain.GDSData;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Sujith Ramanathan
 */
@Service
@Qualifier("MotorStatusConverterImpl")
public class MotorStatusConverterImpl extends Converter<MotorStatusEvent> {

    @Value("${"+ Constants.MOTOR_STATUS_IDX +"}")
    private int motorStatusDataIdx;

    @Override
    public MotorStatusEvent convert(GDSData gdsData) {
        MotorStatusEvent motorEvent = new MotorStatusEvent();
        super.setBaseData(motorEvent, gdsData);
        byte []data = gdsData.getGdsData();
        motorEvent.setMotorStatus(data[motorStatusDataIdx]);
        return motorEvent;
    }
}
