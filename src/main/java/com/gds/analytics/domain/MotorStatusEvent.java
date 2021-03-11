package com.gds.analytics.domain;

/**
 * @author Sujith Ramanathan
 */
public class MotorStatusEvent extends GDSBase {

    private int motorStatus;

    public int getMotorStatus() {
        return motorStatus;
    }

    public void setMotorStatus(int motorStatus) {
        this.motorStatus = motorStatus;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MotorEvent{");
        sb.append("motorStatus=").append(motorStatus);
        sb.append(", startByte=").append(startByte);
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
        sb.append(", eventId=").append(eventId);
        sb.append('}');
        return sb.toString();
    }
}
