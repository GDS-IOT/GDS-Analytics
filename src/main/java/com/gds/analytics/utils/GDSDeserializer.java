package com.gds.analytics.utils;


import org.apache.kafka.common.serialization.Deserializer;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

public class GDSDeserializer<T> implements Deserializer<T> {

    private static final Logger LOGGER = Logger.getLogger(GDSDeserializer.class.getName());

    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public T deserialize(String s, byte[] data) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        } catch (IOException e) {
            LOGGER.error("Error occurred while deserializing ",e);
        } catch (ClassNotFoundException e) {
            LOGGER.error("ClassNotFoundException occurred while deserializing ",e);
        }
        return null;
    }

    @Override
    public void close() {

    }
}
