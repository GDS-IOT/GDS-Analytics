package com.gds.analytics.converter;

import com.gds.analytics.constants.Constants;
import com.gds.analytics.dao.HeartBeatDao;
import com.gds.analytics.domain.HeartBeat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Qualifier("HeartBeatConverterImpl")
public class HeartBeatConverterImpl implements Converter<HeartBeat> {

    @Autowired
    private HeartBeatDao heartBeatDao;

    @Value("${" + Constants.SYSTEM_ID_DELIMITER + "}")
    private String systemIdDelimiter;

    @Value("${" + Constants.HB_VALUE_SKIP + "}")
    private int invalidValue;

    @Value("${" + Constants.SYSTEM_ID_START + "}")
    private int systemIdStart;

    @Value("${" + Constants.SYSTEM_ID_END + "}")
    private int systemIdEnd;

    @Value("${" + Constants.DEVICE_ID_START + "}")
    private int deviceIdStart;

    @Value("${" + Constants.DEVICE_ID_END + "}")
    private int deviceIdEnd;

    @Value("${" + Constants.DEVICE_TYPE_INDEX + "}")
    private int deviceTypeIdx;

    @Value("${" + Constants.ORIGIN_RSSI_IDX + "}")
    private int originRSSIIdx;

    @Value("${" + Constants.ORIGIN_NETWORK_LVL_IDX + "}")
    private int originNetworkLevelIdx;

    @Value("${" + Constants.HOP_COUNTER_IDX + "}")
    private int hopCounterIdx;

    @Value("${" + Constants.MESSAGE_COUNTER_START + "}")
    private int messageCounterStart;

    @Value("${" + Constants.MESSAGE_COUNTER_END + "}")
    private int messageCounterEnd;

    @Value("${" + Constants.LATENCY_COUNTER_START + "}")
    private int latencyCounterStart;

    @Value("${" + Constants.LATENCY_COUNTER_END + "}")
    private int latencyCounterEnd;

    @Value("${" + Constants.PACKET_TYPE_IDX + "}")
    private int packetTypeIdx;

    @Value("${" + Constants.MESSAGE_TYPE_IDX + "}")
    private int messageTypeIdx;

    @Override
    public HeartBeat convert(byte[] data) {
        HeartBeat hb = new HeartBeat();
        hb.setSystemId(getString(data, systemIdStart, systemIdEnd, systemIdDelimiter));
//        hb.setDeviceId(getString(data, deviceIdStart, deviceIdEnd, systemIdDelimiter));
        hb.setDeviceType((int) data[deviceTypeIdx]);
        hb.setOriginRSSI((int) data[originRSSIIdx]);
        hb.setOriginNetworkLevel((int) data[originNetworkLevelIdx]);
        hb.setHopCounter((int) data[hopCounterIdx]);
//        hb.setMessageCounter(getString(data, messageCounterStart, messageCounterEnd, systemIdDelimiter));
        hb.setLatencyCounter(getString(data, latencyCounterStart, latencyCounterEnd, systemIdDelimiter));
        hb.setPacketType((int) data[packetTypeIdx]);
        hb.setMessageType((int) data[messageTypeIdx]);

        return hb;
    }

    private String getString(byte[] data, int startIndex, int endIndex, String delimiter) {
        String value = "";
        for (int i = startIndex; i <= endIndex; i++) {
            if((int) data[i] != invalidValue)
                value = value.concat(delimiter).concat(String.valueOf((int)data[i]));
        }
        if("".equals(value)){
            for (int i = startIndex; i <= endIndex; i++) {
                value = value.concat(delimiter).concat("0");
            }
        }
        return value.replaceFirst(delimiter, "");
    }
}
