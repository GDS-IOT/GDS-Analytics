package com.gds.analytics.dao;

import com.gds.analytics.domain.MotorStatusEvent;

/**
 * @author Sujith Ramanathan
 */
public interface MotorStatusDao {

    public void insertMotorHB(MotorStatusEvent motorStatusEvent);

}
