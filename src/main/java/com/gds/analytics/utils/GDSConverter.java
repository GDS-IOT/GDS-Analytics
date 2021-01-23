package com.gds.analytics.utils;

import com.gds.analytics.constants.Constants;
import com.gds.analytics.domain.HeartBeat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GDSConverter {

    @Value("${"+ Constants.SYSTEM_ID_START +"}")
    private int systemIdStart;

    @Value("${"+ Constants.SYSTEM_ID_END +"}")
    private int systemIdEnd;

    @Value("${"+ Constants.SYSTEM_ID_DELIMITER +"}")
    private String idDelimiter;

    public HeartBeat convertToHeartBeat(byte[] data) {
        HeartBeat hb = new HeartBeat();
        hb.setSystemId(delimitedValue(data, systemIdStart, systemIdEnd, idDelimiter));
        return hb;
    }

    private String delimitedValue(byte[] data, int startByte, int endByte, String delimiter) {
        String value="";
        for(int i=startByte; i<=endByte;i++){
            System.out.println("data["+i+"] = "+(int)data[i]);
            value = value.concat(String.valueOf((int) data[i])).concat(delimiter);
        }
        return value;
    }
}
