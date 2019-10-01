import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

public class StreamProducer {

    private Producer producer = new KafkaProducer<>(StreamProducer.setPropertiesOfProducer());

    public static Properties setPropertiesOfProducer() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return properties;
    }

public  static  void run() throws InterruptedException, IOException {
    String[] cmd = new String[]{"/bin/bash", "cd ", "/Applications/kafka_2.12-2.3.0/", "bin/zookeeper-server-start.sh config/zookeeper.properties", "/bin/kafka-server-start.sh config/server.properties"};
    Runtime run = Runtime.getRuntime();
    Process pr = null;
    try {
        pr = run.exec(cmd);
    } catch (IOException e) {
        e.printStackTrace();
    }

    pr.waitFor();

    BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));


    String line = "";
    while ((line = buf.readLine()) != null) {
        System.out.println(line);

    }
}
    public static void main(String[] args) throws IOException, InterruptedException, ParseException {

        String topic = "deviceTopic";
        TopicPartition topicName = new TopicPartition(topic, 5);

        StreamProducer streamProducer = new StreamProducer();
        streamProducer.sendMessages(topicName);

    }


    public void sendMessages(TopicPartition topicName) throws IOException, ParseException, InterruptedException {
        run();
        if (topicName.topic().length() == 0) {
            return;
        }
        JSONParser parser = new JSONParser();
        Object device = parser.parse(new FileReader("src/resources/input.json"));
        JSONObject jsonObject = (JSONObject) device;

        JSONArray deviceList = (JSONArray) jsonObject.get("deviceList");


        try {
            Iterator iterator = deviceList.iterator();
            while (iterator.hasNext()) {
                Object it = iterator.next();

                JSONObject data = (JSONObject) it;

                //Putting TimeStamp dynamically into JSON
                Date date = new Date();
                long time = date.getTime();
                Timestamp timestamp = new Timestamp(time);
                String deviceTimestamp = timestamp.toString();
                data.put("timestamp", deviceTimestamp);

                //Assigning key and value as a String
                String device_id = (String) data.get("device_id");
                String status = (String) data.get("status");
                String value = "{\n" +
                        "    \"device_id\":" + "\"" + device_id + "\"" + ",\n" +
                        "    \"status\":" + "\"" + status + "\"" + ",\n" +
                        "    \"timestamp\": " + "\"" + deviceTimestamp + "\"" + "\n}";


                String key = (String) data.get("device_id");

                producer.send(new ProducerRecord<>(topicName.topic(), key, value));
                System.out.println("Producer sent successfully!!!");
            }

        } finally {
            producer.flush();
            producer.close();
        }


    }
}

