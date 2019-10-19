package com.itchina;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class MyKafkaProducer {

	private static final String KAFKA_BROKER_LIST = "127.0.0.1:9092";

	private static final String TOPIC = "MyTopic-java";

	private final KafkaProducer<Integer, String> kafkaProducer;

	public MyKafkaProducer() {
	/*	Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKER_LIST);
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put("client.id", "kafka-producerDemo");*/
		Properties properties = new Properties();
		properties.put("bootstrap.servers", KAFKA_BROKER_LIST);
		properties.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
		properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.put("client.id", "producerDemo");
		this.kafkaProducer = new KafkaProducer<Integer, String>(properties);
	}

	public void sendMessage() {
		kafkaProducer.send(new ProducerRecord<Integer, String>(TOPIC, 1,"   message===xiadongming   "),new Callback() {
			@Override
			public void onCompletion(RecordMetadata recordMetadata, Exception arg1) {
				System.out.println("消息发送到："+recordMetadata.partition()+"分区，offset是："+recordMetadata.offset());
			}
		});
	}

	public static void main(String[] args) {

		MyKafkaProducer mykafkaPoducer = new MyKafkaProducer();
		mykafkaPoducer.sendMessage();
	}

}
