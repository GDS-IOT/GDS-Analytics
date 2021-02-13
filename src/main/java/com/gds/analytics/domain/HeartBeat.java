package com.gds.analytics.domain;

public class HeartBeat extends GDSBase {

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HeartBeat{");
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
        sb.append(", ts=").append(ts);
        sb.append('}');
        return sb.toString();
    }
}
