package com.java.gds.analytics.processor;

import com.java.gds.analytics.converter.Converter;
import com.java.gds.analytics.converter.HeartBeatConverterImpl;
import com.java.gds.analytics.dao.HeartBeatDao;
import com.java.gds.analytics.domain.HeartBeat;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class HeartBeatProcessor implements Processor {

    private static final Logger LOGGER = Logger.getLogger(HeartBeatProcessor.class);

    @Autowired
    private HeartBeatDao heartBeatDao;

    @Autowired
    @Qualifier("HeartBeatConverterImpl")
    private Converter<HeartBeat> heartBeatConverter;

    public void processData(byte[] data) {
        HeartBeat hb = heartBeatConverter.convert(data);
        LOGGER.debug("HeartBeat event ".concat(hb.toString()));
        heartBeatDao.insertHeartBeat(hb);
    }
}
