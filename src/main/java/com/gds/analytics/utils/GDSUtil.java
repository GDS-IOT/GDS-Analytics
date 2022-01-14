package com.gds.analytics.utils;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Sujith Ramanathan
 */
@Service
public class GDSUtil {

    public String getCurrentDateTimeString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}
