package com.gds.analytics.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sujith Ramanathan
 */
public enum GDSEnum {

    WATER_LEVEL_STATUS(69, "SMAT_FETCH_WTRLVL_01", "Water_Level_Status"),
    MOTOR_STATUS_HB_EVENT(74, "MOTOR_STATUS_HB", "MOTOR_STATUS_HB"),
    DEFAULT(0, "No_Event", "No_Action_Or_Not_a_valid_event");

    private int eventId;
    private String value;
    private String description;

    private static final Map<Integer, GDSEnum> eventMap = new HashMap<Integer, GDSEnum>();

    GDSEnum(int eventId, String value, String description) {
        this.eventId = eventId;
        this.value = value;
        this.description = description;
    }

    public int getEventId() {
        return eventId;
    }

    public String getValue() {
        return value;
    }

    public String getDescription(){return description;}

    public static GDSEnum getEventById(int eventId) {
        return eventMap.get(eventId);
    }

    static {
        for (GDSEnum kv : GDSEnum.values()) {
            eventMap.put(kv.getEventId(), kv);
        }
    }

}
