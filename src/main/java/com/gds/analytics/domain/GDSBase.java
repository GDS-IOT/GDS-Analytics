package com.gds.analytics.domain;

public abstract class GDSBase {

    protected int startByte;
    protected String systemId;
    protected int systemIdAsInt;
    protected String deviceId;
    protected int deviceIdAsInt;
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

    public int getDeviceIdAsInt() {
        return deviceIdAsInt;
    }

    public void setDeviceIdAsInt(int deviceIdAsInt) {
        this.deviceIdAsInt = deviceIdAsInt;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public int getSystemIdAsInt() {
        return systemIdAsInt;
    }

    public void setSystemIdAsInt(int systemIdAsInt) {
        this.systemIdAsInt = systemIdAsInt;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GDSBase{");
        sb.append("startByte=").append(startByte);
        sb.append(", systemId='").append(systemId).append('\'');
        sb.append(", systemIdAsInt=").append(systemIdAsInt);
        sb.append(", deviceId='").append(deviceId).append('\'');
        sb.append(", deviceIdAsInt=").append(deviceIdAsInt);
        sb.append(", deviceType=").append(deviceType);
        sb.append(", originRSSI=").append(originRSSI);
        sb.append(", originNetworkLevel=").append(originNetworkLevel);
        sb.append(", hopCounter=").append(hopCounter);
        sb.append(", messageCounter='").append(messageCounter).append('\'');
        sb.append(", latencyCounter='").append(latencyCounter).append('\'');
        sb.append(", packetType=").append(packetType);
        sb.append(", messageType=").append(messageType);
        sb.append(", dateTime='").append(dateTime).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
