import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
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

    public static void main(String[] args) throws IOException {
        String topic = "deviceTopic";
        StreamConsumer streamConsumer = new StreamConsumer();
        streamConsumer.receivedMessages(topic);
    }

    public void receivedMessages(String topicName) {
        if (topicName.length() == 0)
            return;
        else {
            try {
                consumer.subscribe(Arrays.asList(topicName));
                ConsumerRecords<String, String> records = consumer.poll(1000);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("Record Partition: " + record.partition() + " Record Offset:  " + record.offset() + " Record Key:  " + record.key());
                    System.out.println(record.value());
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    Device device = gson.fromJson(record.value(), Device.class);
                    System.out.println(device.getDeviceId());
                }
            } finally {
                consumer.close();

            }

        }

    }
}
