import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;

import static org.mockito.Mockito.*;

public class KafkaConsumerTest {
    @InjectMocks
    StreamConsumer streamConsumer;
    @InjectMocks
    StreamProducer streamProducer;
    @Mock
    KafkaConsumer kafkaConsumer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldNotReceiveMessagesIfNoTopicIsMentioned() throws ParseException, InterruptedException, IOException {
        String topic="";
        TopicPartition topicName = new TopicPartition(topic, 5);
        streamProducer.sendMessages(topicName);
        streamConsumer.receivedMessages(topicName);

        verify(kafkaConsumer, never()).subscribe(Arrays.asList(topicName));
    }

    @Test
    public void shouldReceiveMessagesIfTopicIsMentioned() throws ParseException, InterruptedException, IOException {
        String topic="deviceTopic";
        TopicPartition topicName = new TopicPartition(topic, 5);
        streamProducer.sendMessages(topicName);
        streamConsumer.receivedMessages(topicName);

        verify(kafkaConsumer, atLeastOnce()).subscribe(Arrays.asList(topicName));
    }

    @Test
    public void shouldReceiveMessagesIfMultiplePartitionsAreMentioned() throws ParseException, InterruptedException, IOException {
        String topic="deviceTopic";
        TopicPartition topicName = new TopicPartition(topic, 5);
        streamProducer.sendMessages(topicName);
        streamConsumer.receivedMessages(topicName);

        verify(kafkaConsumer, atLeastOnce()).subscribe(Arrays.asList(topicName));
    }

}
