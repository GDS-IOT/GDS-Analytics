package com.gds.analytics.domain;

import java.util.List;

public class WaterLevelEvent extends GDSBase {

    private long txnId;
    private int eventId;
    private int waterLevelPercentage;
    private int waterLevelCm;
    private List<Integer> waterLvlPercentageList;

    public long getTxnId() {
        return txnId;
    }

    public void setTxnId(long txnId) {
        this.txnId = txnId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getWaterLevelPercentage() {
        return waterLevelPercentage;
    }

    public void setWaterLevelPercentage(int waterLevelPercentage) {
        this.waterLevelPercentage = waterLevelPercentage;
    }

    public int getWaterLevelCm() {
        return waterLevelCm;
    }

    public void setWaterLevelCm(int waterLevelCm) {
        this.waterLevelCm = waterLevelCm;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WaterLevelEvent{");
        sb.append("txnId=").append(txnId);
        sb.append(", eventId=").append(eventId);
        sb.append(", waterLevelPercentage=").append(waterLevelPercentage);
        sb.append(", waterLevelCm=").append(waterLevelCm);
        sb.append(", waterLvlPercentageList=").append(waterLvlPercentageList);
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
        sb.append('}');
        return sb.toString();
    }

    public class WaterLevelEventTxnProps {

        private int idPrptyEventMapping;
        private int idSowEventDevMapping;
        private int idSowEventMst;
        private String eventCode;
        private String facMcuDeviceId;
        private int idSmatPropertyMst;
        private String description;
        private boolean isError;

        public boolean isError() {
            return isError;
        }

        public void setError(boolean error) {
            isError = error;
        }

        public int getIdPrptyEventMapping() {
            return idPrptyEventMapping;
        }

        public void setIdPrptyEventMapping(int idPrptyEventMapping) {
            this.idPrptyEventMapping = idPrptyEventMapping;
        }

        public int getIdSowEventDevMapping() {
            return idSowEventDevMapping;
        }

        public void setIdSowEventDevMapping(int idSowEventDevMapping) {
            this.idSowEventDevMapping = idSowEventDevMapping;
        }

        public int getIdSowEventMst() {
            return idSowEventMst;
        }

        public void setIdSowEventMst(int idSowEventMst) {
            this.idSowEventMst = idSowEventMst;
        }

        public String getEventCode() {
            return eventCode;
        }

        public void setEventCode(String eventCode) {
            this.eventCode = eventCode;
        }

        public String getFacMcuDeviceId() {
            return facMcuDeviceId;
        }

        public void setFacMcuDeviceId(String facMcuDeviceId) {
            this.facMcuDeviceId = facMcuDeviceId;
        }

        public int getIdSmatPropertyMst() {
            return idSmatPropertyMst;
        }

        public void setIdSmatPropertyMst(int idSmatPropertyMst) {
            this.idSmatPropertyMst = idSmatPropertyMst;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("WaterLevelProps{");
            sb.append("idPrptyEventMapping=").append(idPrptyEventMapping);
            sb.append(", idSowEventDevMapping=").append(idSowEventDevMapping);
            sb.append(", idSowEventMst=").append(idSowEventMst);
            sb.append(", eventCode='").append(eventCode).append('\'');
            sb.append(", facMcuDeviceId='").append(facMcuDeviceId).append('\'');
            sb.append(", idSmatPropertyMst=").append(idSmatPropertyMst);
            sb.append(", description='").append(description).append('\'');
            sb.append(", isError=").append(isError);
            sb.append('}');
            return sb.toString();
        }

    }
}
