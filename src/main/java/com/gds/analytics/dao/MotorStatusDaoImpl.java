package com.gds.analytics.dao;

import com.gds.analytics.db.DBConnection;
import com.gds.analytics.domain.HeartBeat;
import com.gds.analytics.domain.MotorStatusEvent;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @author Sujith Ramanathan
 */
@Service
public class MotorStatusDaoImpl implements MotorStatusDao {

    private static final Logger LOGGER = Logger.getLogger(MotorStatusDaoImpl.class);

    @Autowired
    private DBConnection db;

    public void insertMotorHB(MotorStatusEvent motorStatusEvent) {
        insertQuery(motorStatusEvent);
    }

    private void insertQuery(MotorStatusEvent motorStatusEvent) {
        Connection con = db.getConnection();
        String query = "insert into smt.hb_mcu_log(latency_counter, devicetype, rssi," +
                "network_level, hopcounter, pac_type, message_type, timestamp, rfid_device_with_hypen, " +
                "event_code, event_message_1, mcu_deviceid_with_hyphen, " +
                "mcu_deviceid_without_hyphen, rfid_device_without_hypen, createddate, event_code) " +
                "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, motorStatusEvent.getLatencyCounter());
            pst.setInt(2, motorStatusEvent.getDeviceType());
            pst.setInt(3, motorStatusEvent.getOriginRSSI());
            pst.setInt(4, motorStatusEvent.getOriginNetworkLevel());
            pst.setInt(5, motorStatusEvent.getHopCounter());
            pst.setInt(6, motorStatusEvent.getPacketType());
            pst.setInt(7, motorStatusEvent.getMessageType());
            pst.setTimestamp(8, Timestamp.valueOf(motorStatusEvent.getDateTime()));
            pst.setString(9, motorStatusEvent.getSystemId());
            pst.setInt(10, motorStatusEvent.getEventId());
            pst.setString(11, String.valueOf(motorStatusEvent.getMotorStatus()));
            pst.setString(12, motorStatusEvent.getDeviceId());
            pst.setString(13, String.valueOf(motorStatusEvent.getDeviceIdAsInt()));
            pst.setString(14, String.valueOf(motorStatusEvent.getSystemIdAsInt()));
            pst.setTimestamp(15, Timestamp.valueOf(motorStatusEvent.getDateTime()));
            pst.setInt(16, motorStatusEvent.getEventId());

            LOGGER.debug("Motor status Query :: ".concat(pst.toString()));
            if (pst.executeUpdate() == 0) {
                LOGGER.debug("Unable to insert motor_status data ");
            }
        } catch (SQLException se) {
            LOGGER.error("Error occurred while inserting Motor HeartBeat ", se);
        } finally {
            db.close();
        }
    }
}
