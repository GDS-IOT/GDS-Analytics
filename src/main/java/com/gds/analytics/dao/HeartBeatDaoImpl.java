package com.gds.analytics.dao;

import com.gds.analytics.db.DBConnection;
import com.gds.analytics.domain.HeartBeat;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

@Service
public class HeartBeatDaoImpl implements HeartBeatDao {

    private static final Logger LOGGER = Logger.getLogger(HeartBeatDaoImpl.class);

    @Autowired
    private DBConnection db;

    public void insertHeartBeat(HeartBeat heartBeat) {
        Connection con = db.getConnection();
        String query = "insert into smt.hb_log(id_device, latency_counter, devicetype, rssi,timestamp" +
                "network_level, hopcounter, pac_type, message_type) values (?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, heartBeat.getSystemId());
            pst.setString(2, heartBeat.getLatencyCounter());
            pst.setInt(3, heartBeat.getDeviceType());
            pst.setInt(4, heartBeat.getOriginRSSI());
            pst.setInt(5, heartBeat.getOriginNetworkLevel());
            pst.setInt(6, heartBeat.getHopCounter());
            pst.setInt(7, heartBeat.getPacketType());
            pst.setInt(8, heartBeat.getMessageType());
            pst.setTimestamp(9, Timestamp.valueOf(heartBeat.getTs()));
            LOGGER.debug("Query :: ".concat(pst.toString()));
            if (pst.executeUpdate() == 0) {
                LOGGER.debug("Unable to insert row ");
            }
        } catch (SQLException se) {
            LOGGER.error("Error occurred while inserting HeartBeat ", se);
        }
    }
}
