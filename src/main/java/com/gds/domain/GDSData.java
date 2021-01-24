package com.gds.domain;

import java.io.Serializable;

public class GDSData implements Serializable {

    private String ts;
    private byte[] gdsData;

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public byte[] getGdsData() {
        return gdsData;
    }

    public void setGdsData(byte[] gdsData) {
        this.gdsData = gdsData;
    }
}
