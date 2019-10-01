import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class KafkaProducerTest {

    @InjectMocks
    StreamProducer streamProducer;
    @Mock
    KafkaProducer kafkaProducer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldNotSendMessagesToConsumerIfTopicNameIsNotGiven() throws IOException, ParseException, InterruptedException {
        String topic = "";
        TopicPartition topicName = new TopicPartition(topic, 5);

        streamProducer.sendMessages(topicName);

        verify(kafkaProducer, never()).send(any(ProducerRecord.class));
    }

    @Test
    public void shouldSendMessageToConsumerIfTopicIsMentioned() throws IOException, ParseException, InterruptedException {
        String topic = "deviceTopic";
        TopicPartition topicName = new TopicPartition(topic, 5);

        streamProducer.sendMessages(topicName);

        verify(kafkaProducer, atLeastOnce()).send(any(ProducerRecord.class));

    }

    @Test
    public void shouldSendMessagesToMultiplePartitions() throws IOException, ParseException, InterruptedException {
        String topic = "deviceTopic";
        TopicPartition topicName = new TopicPartition(topic, 5);

        streamProducer.sendMessages(topicName);

        verify(kafkaProducer, atLeastOnce()).send(any(ProducerRecord.class));
    }


}

