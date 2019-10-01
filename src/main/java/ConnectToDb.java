import com.mongodb.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.net.UnknownHostException;

public class ConnectToDb {

//    public static void insertingDocuments(Device device) throws UnknownHostException {
//        MongoClient mongoClient = new MongoClient("localhost", 27017);
//        DB database = mongoClient.getDB("kafkaProducer");
//        DBCollection collection = database.getCollection("messages");
//        System.out.println(database);
//        BasicDBObject document = new BasicDBObject("deviceId", device.getDeviceId()).
//                append("status", device.getStatus()).append("timestamp", device.getTimeStamp());
//        collection.insert(document);
//        System.out.println("Inserted Successfully in the Database! ");
//        DBCursor cursor = collection.find();
//        try {
//            while (cursor.hasNext()) {
//                System.out.println(cursor.next());
//            }
//        } finally {
//            cursor.close();
//        }
//    }

    public void insertingDocumentsInStream(String value) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        DB database = mongoClient.getDB("kafkaConsumer");
        DBCollection collection = database.getCollection("messages");
        System.out.println(database);
        BasicDBObject dbObject=new BasicDBObject("Device Details",value);
        collection.insert(dbObject);

        System.out.println("Inserted Successfully in the Database! ");
        DBCursor cursor = collection.find();
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        } finally {
            cursor.close();
        }
    }
}