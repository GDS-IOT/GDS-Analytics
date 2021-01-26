package com.gds.analytics.domain;

public abstract class GDSBase {

    protected int startByte;
    protected String systemId;
    protected String deviceId;
    protected int deviceType;
    protected int originRSSI;
    protected int originNetworkLevel;
    protected int hopCounter;
    protected String messageCounter;
    protected String latencyCounter;
    protected int packetType;
    protected int messageType;
    protected String dateTime;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getStartByte() {
        return startByte;
    }

    public void setStartByte(int startByte) {
        this.startByte = startByte;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public int getOriginRSSI() {
        return originRSSI;
    }

    public void setOriginRSSI(int originRSSI) {
        this.originRSSI = originRSSI;
    }

    public int getOriginNetworkLevel() {
        return originNetworkLevel;
    }

    public void setOriginNetworkLevel(int originNetworkLevel) {
        this.originNetworkLevel = originNetworkLevel;
    }

    public int getHopCounter() {
        return hopCounter;
    }

    public void setHopCounter(int hopCounter) {
        this.hopCounter = hopCounter;
    }

    public String getMessageCounter() {
        return messageCounter;
    }

    public void setMessageCounter(String messageCounter) {
        this.messageCounter = messageCounter;
    }

    public String getLatencyCounter() {
        return latencyCounter;
    }

    public void setLatencyCounter(String latencyCounter) {
        this.latencyCounter = latencyCounter;
    }

    public int getPacketType() {
        return packetType;
    }

    public void setPacketType(int packetType) {
        this.packetType = packetType;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

}
