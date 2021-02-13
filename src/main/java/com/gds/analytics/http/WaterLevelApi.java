package com.gds.analytics.http;

import com.gds.analytics.constants.Constants;
import com.gds.analytics.domain.WaterLevelEvent;
import com.gds.analytics.domain.WaterLevelSeries;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Sujith Ramanathan
 */
@Service
public class WaterLevelApi {

    private static final Logger LOGGER = Logger.getLogger(WaterLevelApi.class);

    private static final DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

    private static final JSONParser parser = new JSONParser();

    private static final String STATUS_ID = "statusId";

    private static final String WATER_LEVEL_PATTERN = "waterLevelPattern";

    @Autowired
    private RestTemplate restTemplate;

    @Value("${" + Constants.GDS_RULE_URL + "}")
    private String ruleUrl;

    @Value("${" + Constants.GDS_WATER_LEVEL_TRIGGER_URL + "}")
    private String waterLevelTriggerUrl;

    @Value("${" + Constants.GDS_BEARER_TOKEN + "}")
    private String bearerToken;

    @Value("${" + Constants.GDS_USER_KEY + "}")
    private String userKeyVal;

    @Value("${" + Constants.API_WATER_LEVEL_EVENT + "}")
    private String waterLevelApiEventId;

    public String isValidAction(String action) {
        String payload = createRuleBody(action);
        HttpHeaders headers = getHeaders();

        HttpEntity<String> request = new HttpEntity<String>(payload, headers);
        LOGGER.debug("Get Pattern " + payload);
        ResponseEntity<String> val = null;
        try {
            val = restTemplate.exchange(ruleUrl, HttpMethod.POST, request, String.class);
        } catch (Exception e) {
            LOGGER.error("Error while occurred getting water level pattern", e);
            LOGGER.debug("Returning null");
            return null;
        }
        if (null != val) {
            LOGGER.debug("Pattern Response received :: " + val.getBody());
            JSONObject respJson = null;
            try {
                respJson = (JSONObject) parser.parse(val.getBody());
                LOGGER.debug("Successfully parsing completed");
                if (HttpStatus.OK.value() == ((Long) respJson.get(STATUS_ID)).intValue()) {
                    LOGGER.debug("Water Level Rule success");
                    return (String) respJson.get(WATER_LEVEL_PATTERN);
                } else {
                    LOGGER.debug("Water Level Pattern API Failed");
                }
            } catch (ParseException e) {
                LOGGER.error("Error occurred while parsing rule json Api ", e);
                return null;
            }
        } else {
            LOGGER.debug("Water Level Pattern API returned null");
        }
        return null;
    }

    public boolean isTriggered(WaterLevelSeries waterLevelSeries, String waterlevelPattern) {
        String payload = createWaterLevelBody(waterLevelSeries, waterlevelPattern, waterLevelApiEventId);
        HttpHeaders headers = getHeaders();
        HttpEntity<String> request = new HttpEntity<String>(payload, headers);
        LOGGER.debug("WaterLevelPayload ".concat(payload));
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(waterLevelTriggerUrl, HttpMethod.POST, request, String.class);
        } catch (Exception e) {
            LOGGER.debug("Error occurred while posting water_level API", e);
            return false;
        }
        if (null != response) {
            JSONObject resp = null;
            LOGGER.debug("water level trigger api response " + response.getBody());
            LOGGER.debug("Successfully water level event sent");
            if ("success".equalsIgnoreCase(response.getBody().toLowerCase())) {
                return true;
            } else {
                LOGGER.debug("WaterLevel trigger failed");
                return false;
            }
        } else {
            LOGGER.debug("Water level API returned null");
        }
        return false;
    }


    private String createRuleBody(String action) {
        JSONObject json = new JSONObject();
        json.put("waterLevelPattern", action);
        LOGGER.debug(json.toJSONString());
        return json.toJSONString();
    }

    private String createWaterLevelBody(WaterLevelSeries waterLevelSeries, String waterLevelPattern, String eventId) {
        JSONObject payload = new JSONObject();
        JSONArray waterLevels = new JSONArray();
        JSONObject waterLevel = null;
        WaterLevelEvent waterLevelEvent = waterLevelSeries.getWaterLevelSeries().get(0);
        payload.put("RFDeviceId", waterLevelEvent.getSystemIdAsInt());
        payload.put("MCUDeviceId", waterLevelEvent.getDeviceIdAsInt());
        payload.put("eventId", eventId);
        payload.put("waterLevelPattern", waterLevelPattern);
        for (int i = 0; i < waterLevelSeries.getWaterLevelSeries().size(); i++) {
            waterLevelEvent = waterLevelSeries.getWaterLevelSeries().get(i);
            waterLevel = new JSONObject();
            waterLevel.put("level", String.valueOf(waterLevelEvent.getWaterLevelPercentage()));
            waterLevel.put("timestamp", getDateString(waterLevelEvent.getTs()));
            waterLevel.put("packet", String.valueOf(i + 1));
            waterLevels.add(waterLevel);
        }
        payload.put("waterLevels", waterLevels);
        return payload.toJSONString();
    }

    private String getDateString(long ts) {
        return df.format(new Date(ts));
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(Constants.AUTHORIZATION, bearerToken);
        headers.add(Constants.USER_KEY, userKeyVal);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
