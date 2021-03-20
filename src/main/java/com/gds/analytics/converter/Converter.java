package com.gds.analytics.converter;

import com.gds.analytics.constants.Constants;
import com.gds.analytics.domain.GDSBase;
import com.gds.domain.GDSData;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author Sujith Ramanathan
 */
public abstract class Converter<T> {

    private static final Logger LOGGER = Logger.getLogger(Converter.class);

    @Value("${" + Constants.SYSTEM_ID_DELIMITER + "}")
    protected String systemIdDelimiter;

    @Value("${" + Constants.HB_VALUE_SKIP + "}")
    protected int invalidValue;

    @Value("${" + Constants.SYSTEM_ID_START + "}")
    protected int systemIdStart;

    @Value("${" + Constants.SYSTEM_ID_END + "}")
    protected int systemIdEnd;

    @Value("${" + Constants.DEVICE_ID_START + "}")
    protected int deviceIdStart;

    @Value("${" + Constants.DEVICE_ID_END + "}")
    protected int deviceIdEnd;

    @Value("${" + Constants.DEVICE_TYPE_INDEX + "}")
    protected int deviceTypeIdx;

    @Value("${" + Constants.ORIGIN_RSSI_IDX + "}")
    protected int originRSSIIdx;

    @Value("${" + Constants.ORIGIN_NETWORK_LVL_IDX + "}")
    protected int originNetworkLevelIdx;

    @Value("${" + Constants.HOP_COUNTER_IDX + "}")
    protected int hopCounterIdx;

    @Value("${" + Constants.MESSAGE_COUNTER_START + "}")
    protected int messageCounterStart;

    @Value("${" + Constants.MESSAGE_COUNTER_END + "}")
    protected int messageCounterEnd;

    @Value("${" + Constants.LATENCY_COUNTER_START + "}")
    protected int latencyCounterStart;

    @Value("${" + Constants.LATENCY_COUNTER_END + "}")
    protected int latencyCounterEnd;

    @Value("${" + Constants.PACKET_TYPE_IDX + "}")
    protected int packetTypeIdx;

    @Value("${" + Constants.MESSAGE_TYPE_IDX + "}")
    protected int messageTypeIdx;

    @Value("${" + Constants.EVENT_ID_IDX + "}")
    protected int eventIdIdx;

    public String getString(byte[] data, int startIndex, int endIndex, String delimiter) {
        String value = "";
        for (int i = startIndex; i <= endIndex; i++) {
            value = value.concat(delimiter).concat(String.valueOf((int) data[i]));
        }
        if ("".equals(value)) {
            for (int i = startIndex; i <= endIndex; i++) {
                value = value.concat(delimiter).concat("0");
            }
        }
        return value.replaceFirst(delimiter, "");
    }

    protected void setBaseData(GDSBase gdsBase, GDSData gdsData) {
        byte[] data = gdsData.getGdsData();
        gdsBase.setSystemId(getString(data, systemIdStart, systemIdEnd, systemIdDelimiter));
        gdsBase.setSystemIdAsInt(Integer.parseInt(gdsBase.getSystemId().replaceAll(systemIdDelimiter, "")));
        gdsBase.setDeviceId(getString(data, deviceIdStart, deviceIdEnd, systemIdDelimiter));
        gdsBase.setDeviceIdAsInt(Integer.parseInt(gdsBase.getDeviceId().replaceAll(systemIdDelimiter, "")));
        gdsBase.setDeviceType((int) data[deviceTypeIdx]);
        gdsBase.setOriginRSSI((int) data[originRSSIIdx]);
        gdsBase.setOriginNetworkLevel((int) data[originNetworkLevelIdx]);
        gdsBase.setHopCounter((int) data[hopCounterIdx]);
        gdsBase.setLatencyCounter(getString(data, latencyCounterStart, latencyCounterEnd, systemIdDelimiter));
        gdsBase.setPacketType((int) data[packetTypeIdx]);
        gdsBase.setMessageType((int) data[messageTypeIdx]);
        gdsBase.setDateTime(gdsData.getTs());
        gdsBase.setTs(convertDateToTs(gdsData.getTs()));
        gdsBase.setData(data);
    }

    private long convertDateToTs(String date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return df.parse(date).getTime();
        } catch (ParseException pe) {
            LOGGER.error("Error occurred while parsing timestamp ", pe);
        }
        return 0;
    }

    public abstract T convert(GDSData gdsData);
}
