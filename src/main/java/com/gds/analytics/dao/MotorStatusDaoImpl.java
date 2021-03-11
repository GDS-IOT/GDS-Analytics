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
        String query = "insert into smt.hb_mcu_log(id_device, latency_counter, devicetype, rssi," +
                "network_level, hopcounter, pac_type, message_type, timestamp, rfid_device_with_hypen, " +
                "event_code, event_message_1, mcu_deviceid_with_hyphen, " +
                "mcu_deviceid_without_hyphen, rfid_device_without_hypen, createddate) " +
                "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, motorStatusEvent.getSystemId());
            pst.setString(2, motorStatusEvent.getLatencyCounter());
            pst.setInt(3, motorStatusEvent.getDeviceType());
            pst.setInt(4, motorStatusEvent.getOriginRSSI());
            pst.setInt(5, motorStatusEvent.getOriginNetworkLevel());
            pst.setInt(6, motorStatusEvent.getHopCounter());
            pst.setInt(7, motorStatusEvent.getPacketType());
            pst.setInt(8, motorStatusEvent.getMessageType());
            pst.setTimestamp(9, Timestamp.valueOf(motorStatusEvent.getDateTime()));
            pst.setString(10, motorStatusEvent.getSystemId());
            pst.setInt(11, motorStatusEvent.getEventId());
            pst.setString(12, String.valueOf(motorStatusEvent.getMotorStatus()));
            pst.setString(13, motorStatusEvent.getDeviceId());
            pst.setString(14, String.valueOf(motorStatusEvent.getDeviceIdAsInt()));
            pst.setString(15, String.valueOf(motorStatusEvent.getSystemIdAsInt()));
            pst.setTimestamp(16, Timestamp.valueOf(motorStatusEvent.getDateTime()));

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
