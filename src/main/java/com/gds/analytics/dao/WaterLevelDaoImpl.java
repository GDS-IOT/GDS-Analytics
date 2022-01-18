package com.gds.analytics.dao;

import com.gds.analytics.constants.GDSEnum;
import com.gds.analytics.db.DBConnection;
import com.gds.analytics.domain.EventTransactionMeta;
import com.gds.analytics.domain.WaterLevelEvent;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;

/**
 * @author Sujith Ramanathan
 */
@Service
public class WaterLevelDaoImpl implements WaterLevelDao {

    private static final Logger LOGGER = Logger.getLogger(WaterLevelDaoImpl.class);

    @Autowired
    private DBConnection db;

    @Override
    public void insertWaterLvlData(WaterLevelEvent waterLevelEventData) {
        WaterLevelEvent.WaterLevelEventTxnProps waterLvlProps = getWaterEventQuery(waterLevelEventData);
        insertWaterLvlTxn(waterLevelEventData, waterLvlProps);
    }

    private void insertWaterLvlTxn(WaterLevelEvent waterLevelEventData, WaterLevelEvent.WaterLevelEventTxnProps waterLvlTxnProps) {
        String eventTxnInsertQuery = "insert into smt.sow_event_transaction(idprpty_event_mapping, idsow_event_dev_mapping, idsow_event_mst, modifieddate) " +
                "values(?,?,?,?)";
        try {
            Connection con = db.getConnection();
            PreparedStatement pst = con.prepareStatement(eventTxnInsertQuery, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, waterLvlTxnProps.getIdPrptyEventMapping());
            pst.setInt(2, waterLvlTxnProps.getIdSowEventDevMapping());
            pst.setInt(3, waterLvlTxnProps.getIdSowEventMst());
            pst.setTimestamp(4, Timestamp.valueOf(waterLevelEventData.getDateTime()));
            LOGGER.debug("eventTxnInsertQuery, sow_event_transaction query ::".concat(pst.toString()));
            int insertedRows = pst.executeUpdate();
            if (0 == insertedRows) {
                LOGGER.debug("None of the rows were inserted in sow_event_transaction");
            }
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                waterLevelEventData.setTxnId(rs.getLong(1));
            }
            LOGGER.debug("waterLevelEventData.toString() ".concat(waterLevelEventData.toString()));
            // Insert into EventTxnMeta
            EventTransactionMeta eventMeta = getEventTxnMeta(waterLevelEventData);
            String insertEventMeta = "insert into smt.sow_event_transaction_meta (idsow_evt_transaction, meta1, meta2, meta3, meta4, meta5) " +
                    "values(?, ?, ?, ?, ?, ?)";
            pst = con.prepareStatement(insertEventMeta);
            pst.setLong(1, eventMeta.getIdSowEvtTxn());
            pst.setString(2, eventMeta.getMeta1());
            pst.setString(3, eventMeta.getMeta2());
            pst.setString(4, eventMeta.getMeta3());
            pst.setString(5, eventMeta.getMeta4());
            pst.setString(6, eventMeta.getMeta5());
            LOGGER.debug("insertEventMeta :: ".concat(pst.toString()));
            insertedRows = pst.executeUpdate();
            if (0 == insertedRows) {
                LOGGER.debug("None of the rows were inserted in sow_event_transaction_meta");
            }
        } catch (SQLException se) {
            LOGGER.error("Error occurred while inserting sow_event_transaction table", se);
        } finally {
            db.close();
        }
    }

    private WaterLevelEvent.WaterLevelEventTxnProps getWaterEventQuery(WaterLevelEvent waterLevelEventData) {
        WaterLevelEvent.WaterLevelEventTxnProps waterLvlProps = waterLevelEventData.new WaterLevelEventTxnProps();
        String waterLvlEvtQuery = "select spem.idprpty_event_mapping, edm.idsow_event_dev_mapping, evm.idsow_event_mst,evm.event_code, " +
                "edm.fac_mcu_device_id, pcer.idsmat_property_mst, spm.description from smt.sow_property_event_mapping spem, " +
                "smt.property_consumer_er_mapping pcer, smt.sow_event_dev_mapping edm, smt.sow_event_mapping evm, smt.sow_consumer_mapping scm, " +
                "smt.sow_mst sm, smt.smat_property_mst spm, smt.smat_meta_mst mm " +
                "where pcer.id_prpty_cnsmr_er_mapping = spem.id_prpty_cnsmr_er_mapping and edm.idsow_event_dev_mapping = spem.idsow_event_dev_mapping " +
                "and evm.idsow_event_mst = edm.idsow_event_mst and scm.id_sow_cons_map = evm.id_sow_cons_map and sm.id_sow_mst = scm.id_sow_mst " +
                "and spm.idsmat_property_mst = pcer.idsmat_property_mst and mm.idsmat_meta_data = spm.id_property_meta_type " +
                "and edm.fac_mcu_device_categoryid = ? and edm.fac_mcu_device_id = ? and evm.id_event_defination = ?";

        try {
            Connection con = db.getConnection();
            PreparedStatement pst = con.prepareStatement(waterLvlEvtQuery);
            pst.setInt(1, waterLevelEventData.getDeviceType());
            pst.setString(2, String.valueOf(waterLevelEventData.getDeviceIdAsInt()));
            pst.setString(3, String.valueOf(waterLevelEventData.getEventId()));


            LOGGER.debug("fac_mcu_device_categoryid = waterLevelEventData.getDeviceType() "+waterLevelEventData.getDeviceType());
            LOGGER.debug("fac_mcu_device_id = waterLevelEventData.getDeviceIdAsInt() "+waterLevelEventData.getDeviceIdAsInt());
            LOGGER.debug("id_event_defination = waterLevelEventData.getEventId "+waterLevelEventData.getEventId());

            LOGGER.debug("waterLevelEvent.toString() ".concat(waterLevelEventData.toString()));
            LOGGER.debug("water_level_query :: ".concat(waterLvlEvtQuery));
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                waterLvlProps.setIdPrptyEventMapping(rs.getInt("idprpty_event_mapping"));
                waterLvlProps.setIdSowEventDevMapping(rs.getInt("idsow_event_dev_mapping"));
                waterLvlProps.setIdSowEventMst(rs.getInt("idsow_event_mst"));
                waterLvlProps.setEventCode(rs.getString("event_code"));
                waterLvlProps.setFacMcuDeviceId(rs.getString("fac_mcu_device_id"));
                waterLvlProps.setIdSmatPropertyMst(rs.getInt("idsmat_property_mst"));
                waterLvlProps.setDescription(rs.getString("description"));
            }
            LOGGER.debug("waterLvlProps.toString() ".concat(waterLvlProps.toString()));
        } catch (SQLException se) {
            waterLvlProps.setError(true);
            LOGGER.error("Error while getting event query", se);
        } finally {
            db.close();
        }
        return waterLvlProps;
    }

    private EventTransactionMeta getEventTxnMeta(WaterLevelEvent waterLevelEvent) {
        EventTransactionMeta eventMeta = new EventTransactionMeta();
        eventMeta.setIdSowEvtTxn(waterLevelEvent.getTxnId());
        eventMeta.setMeta1(String.valueOf(waterLevelEvent.getWaterLevelPercentage()));
        eventMeta.setMeta2(GDSEnum.WATER_LEVEL_STATUS.getValue());
        eventMeta.setMeta3(waterLevelEvent.getDateTime());
        eventMeta.setMeta4(String.valueOf(waterLevelEvent.getWaterLevelCm()));
        eventMeta.setMeta5(String.valueOf(waterLevelEvent.getDeviceIdAsInt()));
        return eventMeta;
    }
}
