package com.gds.analytics.domain;

public class HeartBeat extends GDSBase {

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
