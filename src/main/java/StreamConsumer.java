import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Arrays;
import java.util.Properties;

public class StreamConsumer {


    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(StreamConsumer.setPropertiesOfConsumer());


    public static Properties setPropertiesOfConsumer() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("group.id", "test");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("enable.auto.commit", "true");
        properties.put("auto.commit.interval.ms", "1000");
        properties.put("session.timeout.ms", "30000");
        return properties;
    }

    public static void main(String[] args) {
        String topic = "deviceTopic";
        TopicPartition topicName = new TopicPartition(topic, 5);
        StreamConsumer streamConsumer = new StreamConsumer();
        streamConsumer.receivedMessages(topicName);
    }

    public void receivedMessages(TopicPartition topicName) {
        if (topicName.topic().length() == 0)
            return;
        else {
            try {
                consumer.subscribe(Arrays.asList(topicName.topic()));
                ConsumerRecords<String, String> records = consumer.poll(1000);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("Record Partition: " + record.partition() + " Record Offset:  " + record.offset() + " Record Key:  " + record.key());
                    System.out.println(record.value());
                    JSONParser parser = new JSONParser();

                    JSONObject jsonObject = (JSONObject) parser.parse(record.value());
                    System.out.println(jsonObject);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
                consumer.close();

            }

        }

    }
}
