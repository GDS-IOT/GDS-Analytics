package com.gds.analytics.processor;

import com.gds.analytics.constants.Constants;
import com.gds.analytics.events.EventFactory;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Service
public class GDSAnalyticsProcessor {

    private static final Logger LOGGER = Logger.getLogger(GDSAnalyticsProcessor.class);

    @Value("${" + Constants.KAFKA_TOPIC + "}")
    private String topicName;

    @Value("${" + Constants.KAFKA_BOOTSTRAP_URL + "}")
    private String bootstrapServer;

    @Value("${" + Constants.KAFKA_AUTO_OFFSET_RESET_CONFIG + "}")
    private String autoOffsetResetConfig;

    @Value("${" + Constants.KAFKA_CONSUMER_GROUP_ID + "}")
    private String groupId;

    @Autowired
    private EventFactory eventFactory;

    private Consumer<String, byte[]> consumer;

    @PostConstruct
    public void init() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetResetConfig);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        LOGGER.debug("Initializing Consumer ".concat(props.toString()));
        consumer = new KafkaConsumer<String, byte[]>(props);
    }

    public void processMessage() {
        LOGGER.debug("KafkaConsumer Listening for messages");
        consumer.subscribe(Collections.singletonList(topicName));
        boolean flag = true;
        ConsumerRecords<String, byte[]> consumerRecords;
        while(flag) {
            try {
                consumerRecords = consumer.poll(Duration.ofSeconds(1000));

                if (0 == consumerRecords.count())
                    continue;

                for (ConsumerRecord<String, byte[]> record : consumerRecords) {
                    eventFactory.processEvent(record.key(), record.value());
                }
                consumer.commitAsync();
            }catch(Exception e){
                LOGGER.error("Error occured while processing events. ",e);
                consumer.close();
                flag=false;
                LOGGER.error("Shutting down kafka processor");
            }
        }
    }
}
