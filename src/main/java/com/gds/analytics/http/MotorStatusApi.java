package com.gds.analytics.http;

import com.gds.analytics.constants.Constants;
import com.gds.analytics.domain.MotorStatusEvent;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author Sujith Ramanathan
 */
@Component
public class MotorStatusApi extends ApiSpec {

    private static final Logger LOGGER = Logger.getLogger(MotorStatusApi.class);

    @Autowired
    private RestTemplate restTemplate;
    @Value("${" + Constants.MOTOR_STATUS_URL + "}")
    private String motorStatusUrl;

    public void sendMotorStatusToApi(MotorStatusEvent motorEvent) {
        String payload = createPayload(motorEvent);
        HttpHeaders headers = getHeaders();
        HttpEntity<String> request = new HttpEntity<String>(payload, headers);
        LOGGER.debug("MotorStatus-Payload ".concat(payload));
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(motorStatusUrl, HttpMethod.POST, request, String.class);

            if (HttpStatus.OK == response.getStatusCode()) {
                LOGGER.debug("Successfully sent to MotorStatus API");
            } else {
                LOGGER.debug("Failed to send motor details to API ".concat(String.valueOf(response.getStatusCode())));
            }
        } catch (Exception e) {
            LOGGER.debug("Error occurred while posting Motor Status API", e);
        }
    }

    private String createPayload(MotorStatusEvent motorEvent) {
        JSONObject payload = getBasicPayload(motorEvent);
        payload.put("packet_type", String.valueOf(motorEvent.getPacketType()));
        payload.put("message_type", String.valueOf(motorEvent.getMessageType()));

        JSONArray motorStatusArray = new JSONArray();

        JSONObject motorStatus = new JSONObject();
        motorStatus.put("serialdata_location", "8");
        motorStatus.put("serialdata_value", String.valueOf(motorEvent.getMotorStatus()));

        motorStatusArray.add(motorStatus);

        payload.put("device_action_serial_data", motorStatusArray);
        return payload.toJSONString();
    }


}
