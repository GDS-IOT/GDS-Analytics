package com.gds.analytics.constants;

/**
 * @author Sujith Ramanathan
 */
public class Constants {

    // Constants
    public static final String AUTHORIZATION = "Authorization";

    public static final String USER_KEY = "UserKey";

    // HEART-BEAT-CONSTANTS
    public static final String HB_VALUE_SKIP = "hb.value.skip";

    public static final String SYSTEM_ID_START = "system.id.start";

    public static final String SYSTEM_ID_END = "system.id.end";

    public static final String DEVICE_ID_START = "device.id.start";

    public static final String DEVICE_ID_END = "device.id.end";

    public static final String SYSTEM_ID_DELIMITER = "system.id.delimiter";

    public static final String DEVICE_TYPE_INDEX = "device.type.idx";

    public static final String ORIGIN_RSSI_IDX = "origin.rssi.idx";

    public static final String ORIGIN_NETWORK_LVL_IDX =  "origin.network.lvl.idx";

    public static final String HOP_COUNTER_IDX = "hop.counter.idx";

    public static final String MESSAGE_COUNTER_START = "message.counter.start";

    public static final String MESSAGE_COUNTER_END = "message.counter.end";

    public static final String LATENCY_COUNTER_START = "latency.counter.start";

    public static final String LATENCY_COUNTER_END = "latency.counter.end";

    public static final String PACKET_TYPE_IDX = "packet.type.idx";

    public static final String MESSAGE_TYPE_IDX = "message.type.idx";


    // Water Level
    public static final String WATER_LVL_PERCENTAGE_IDX = "water.lvl.percentage.idx";

    // Kafka Consumer
    public static final String KAFKA_TOPIC = "kafka.topic";

    public static final String KAFKA_BOOTSTRAP_URL = "kafka.bootstrap.url";

    public static final String KAFKA_AUTO_OFFSET_RESET_CONFIG = "kafka.auto.offset.reset.config";

    public static final String KAFKA_CONSUMER_GROUP_ID = "kafka.consumer.group.id";

    // JDBC
    public static final String JDBC_URL = "jdbc.url";

    public static final String DB_USERNAME = "db.username";

    public static final String DB_PASSWORD = "db.password";

    // Events
    public static final String EVENT_ID_IDX = "event.id.idx";

    public static final String HEART_BEAT_EVENT = "heart.beat.event";

    public static final String WATER_LEVEL_EVENT_ID = "water.level.event.id";


    // API Events
    public static final String API_WATER_LEVEL_EVENT = "event.api.water.level.id";


    // HTTP URL
    public static final String GDS_RULE_URL = "gds.rule.url";

    public static final String GDS_WATER_LEVEL_TRIGGER_URL = "gds.water.level.trigger.url";

    public static final String GDS_BEARER_TOKEN = "gds.bearer.token";

    public static final String GDS_USER_KEY = "gds.user.key";

}