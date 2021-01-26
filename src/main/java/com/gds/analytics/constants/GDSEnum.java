package com.gds.analytics.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sujith Ramanathan
 */
public enum GDSEnum {

    WATER_LEVEL_STATUS(69, "Water_Level_Status");

    private int eventId;
    private String value;

    private static final Map<Integer, GDSEnum> eventMap = new HashMap<Integer, GDSEnum>();

    GDSEnum(int eventId, String value){
        this.eventId = eventId;
        this.value = value;
    }

    public int getEventId(){
        return eventId;
    }

    public String getValue(){
        return value;
    }

    public static GDSEnum getEventById(int eventId){
        return eventMap.get(eventId);
    }

    static {
        for(GDSEnum kv : GDSEnum.values()){
            eventMap.put(kv.getEventId(), kv);
        }
    }

}
