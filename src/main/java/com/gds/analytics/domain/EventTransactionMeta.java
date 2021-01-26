package com.gds.analytics.domain;

/**
 * @author Sujith Ramanathan
 */
public class EventTransactionMeta {

    private long idSowEvtTxnMeta;
    private long idSowEvtTxn;
    private String meta1;
    private String meta2;
    private String meta3;
    private String meta4;
    private String meta5;
    private String meta6;
    private String modifiedDate;

    public long getIdSowEvtTxnMeta() {
        return idSowEvtTxnMeta;
    }

    public void setIdSowEvtTxnMeta(long idSowEvtTxnMeta) {
        this.idSowEvtTxnMeta = idSowEvtTxnMeta;
    }

    public long getIdSowEvtTxn() {
        return idSowEvtTxn;
    }

    public void setIdSowEvtTxn(long idSowEvtTxn) {
        this.idSowEvtTxn = idSowEvtTxn;
    }

    public String getMeta1() {
        return meta1;
    }

    public void setMeta1(String meta1) {
        this.meta1 = meta1;
    }

    public String getMeta2() {
        return meta2;
    }

    public void setMeta2(String meta2) {
        this.meta2 = meta2;
    }

    public String getMeta3() {
        return meta3;
    }

    public void setMeta3(String meta3) {
        this.meta3 = meta3;
    }

    public String getMeta4() {
        return meta4;
    }

    public void setMeta4(String meta4) {
        this.meta4 = meta4;
    }

    public String getMeta5() {
        return meta5;
    }

    public void setMeta5(String meta5) {
        this.meta5 = meta5;
    }

    public String getMeta6() {
        return meta6;
    }

    public void setMeta6(String meta6) {
        this.meta6 = meta6;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EventTransactionMeta{");
        sb.append("idSowEvtTxnMeta=").append(idSowEvtTxnMeta);
        sb.append(", idSowEvtTxn=").append(idSowEvtTxn);
        sb.append(", meta1='").append(meta1).append('\'');
        sb.append(", meta2='").append(meta2).append('\'');
        sb.append(", meta3='").append(meta3).append('\'');
        sb.append(", meta4='").append(meta4).append('\'');
        sb.append(", meta5='").append(meta5).append('\'');
        sb.append(", meta6='").append(meta6).append('\'');
        sb.append(", modifiedDate='").append(modifiedDate).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
