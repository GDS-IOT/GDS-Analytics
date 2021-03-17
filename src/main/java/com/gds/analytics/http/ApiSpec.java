package com.gds.analytics.http;

import com.gds.analytics.constants.Constants;
import com.gds.analytics.domain.GDSBase;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * @author Sujith Ramanathan
 */
public abstract class ApiSpec {

    @Value("${" + Constants.GDS_BEARER_TOKEN + "}")
    private String bearerToken;

    @Value("${" + Constants.GDS_USER_KEY + "}")
    private String userKeyVal;

    protected HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(Constants.AUTHORIZATION, bearerToken);
        headers.add(Constants.USER_KEY, userKeyVal);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    protected JSONObject getBasicPayload(GDSBase event){
        JSONObject payload = new JSONObject();
        payload.put("fac_gateway_device_id", String.valueOf(event.getSystemIdAsInt()));
        payload.put("fac_mcu_device_id", String.valueOf(event.getDeviceIdAsInt()));
        payload.put("fac_mcu_device_category", String.valueOf(event.getDeviceType()));
        payload.put("id_event_defination", String.valueOf(event.getEventId()));
        return payload;
    }
}
