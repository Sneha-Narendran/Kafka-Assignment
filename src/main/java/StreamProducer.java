import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;

import java.util.ArrayList;
import java.util.Properties;

public class StreamProducer {

    private static Producer producer = new KafkaProducer<>(StreamProducer.setPropertiesOfProducer());

    public static Properties setPropertiesOfProducer() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return properties;
    }

    public static ArrayList<Device> setDeviceDetails() {
        Device device1 = new Device();
        device1.setDeviceId("101");
        device1.setStatus("Active");
        Device device2 = new Device();
        device2.setDeviceId("102");
        device2.setStatus("Active");
        Device device3 = new Device();
        device3.setDeviceId("103");
        device3.setStatus("Non-Active");
        Device device4 = new Device();
        device4.setDeviceId("104");
        device4.setStatus("Active");
        Device device5 = new Device();
        device5.setDeviceId("105");
        device5.setStatus("Non-Active");

        ArrayList<Device> deviceArrayList = new ArrayList<>();
        deviceArrayList.add(device1);
        deviceArrayList.add(device2);
        deviceArrayList.add(device3);
        deviceArrayList.add(device4);
        deviceArrayList.add(device5);
        return deviceArrayList;


    }

    public static void main(String[] args) {

        String topic = "deviceTopic";
        TopicPartition topicName = new TopicPartition(topic, 5);


        sendMessages(topicName);
    }

    public static void sendMessages(TopicPartition topicName) {


        if (topicName.topic().length() == 0) {
            System.out.println("No Topic Assigned.");
            return;
        }
        try {
            ArrayList<Device> deviceArrayList = setDeviceDetails();

            for (int i = 0; i < deviceArrayList.size(); i++) {

                String key = deviceArrayList.get(i).getDeviceId();
                String value = deviceArrayList.get(i).toString();
                System.out.println(value);
                producer.send(new ProducerRecord<>(topicName.topic(), key, value));
                System.out.println("Producer sent successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }

}



