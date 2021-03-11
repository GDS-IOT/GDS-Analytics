package com.gds.analytics.converter;

import com.gds.analytics.dao.HeartBeatDao;
import com.gds.analytics.domain.HeartBeat;
import com.gds.domain.GDSData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("HeartBeatConverterImpl")
public class HeartBeatConverterImpl extends Converter<HeartBeat> {

    @Override
    public HeartBeat convert(GDSData gdsData) {
        HeartBeat hb = new HeartBeat();
        super.setBaseData(hb, gdsData);
        return hb;
    }
}
