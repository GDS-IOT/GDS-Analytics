package com.java.gds.analytics.domain;

public class HeartBeat {

    private int startByte;
    private String systemId;
    private int deviceId;
    private int deviceType;
    private int originRSSI;
    private int originNetworkLevel;
    private int hopCounter;
    private int messageCounter;
    private int latencyCounter;
    private int packetType;
    private int messageType;

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

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
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

    public int getMessageCounter() {
        return messageCounter;
    }

    public void setMessageCounter(int messageCounter) {
        this.messageCounter = messageCounter;
    }

    public int getLatencyCounter() {
        return latencyCounter;
    }

    public void setLatencyCounter(int latencyCounter) {
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
        final StringBuilder sb = new StringBuilder("HeartBeat{");
        sb.append("startByte=").append(startByte);
        sb.append(", systemId='").append(systemId).append('\'');
        sb.append(", deviceId=").append(deviceId);
        sb.append(", deviceType=").append(deviceType);
        sb.append(", originRSSI=").append(originRSSI);
        sb.append(", originNetworkLevel=").append(originNetworkLevel);
        sb.append(", hopCounter=").append(hopCounter);
        sb.append(", messageCounter=").append(messageCounter);
        sb.append(", latencyCounter=").append(latencyCounter);
        sb.append(", packetType=").append(packetType);
        sb.append(", messageType=").append(messageType);
        sb.append('}');
        return sb.toString();
    }
}
