package com.itchina;

import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;

import kafka.utils.ShutdownableThread;

public class MyKafkaConsumer extends ShutdownableThread {

	// 分为high level consumer和low level consumer
	private static final String KAFKA_BROKER_LIST = "127.0.0.1:9092";
	private static final String TOPIC = "MyTopic-java";
	private final KafkaConsumer<Integer, String> consumer;

	public MyKafkaConsumer() {
		super("KafkaConsumer", false);
		Properties properties = new Properties();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKER_LIST);
		// 消息所属的分组
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, "GROUP1");
		// 消息是否自动提交offset
		properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
		// 自动提交的间隔时间
		properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
		// 设置使用最开始的offset偏移量为当前group.id的最早消息
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		// 设置心跳时间
		properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
		// 对key和value设置序列化和反序列化
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.IntegerDeserializer");
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringDeserializer");
		this.consumer = new KafkaConsumer<Integer, String>(properties);
	}

	@Override
	public void doWork() {
		consumer.subscribe(Collections.singletonList(TOPIC));
		ConsumerRecords<Integer, String> records = consumer.poll(1000);
		for (ConsumerRecord<Integer, String> consumerRecord : records) {
			System.out.println("获取的消息是==分区是" + consumerRecord.partition() + "消息key是" + consumerRecord.key()
					+ "消息valus是=" + consumerRecord.value() + "消息的offset是" + consumerRecord.offset());
		}

	}

	public static void main(String[] args) {

		MyKafkaConsumer myKafkaConsumer = new MyKafkaConsumer();
		myKafkaConsumer.start();
	}

}
