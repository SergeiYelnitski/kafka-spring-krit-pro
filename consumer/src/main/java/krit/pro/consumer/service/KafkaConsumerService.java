package krit.pro.consumer.service;

import krit.pro.consumer.model.Greeting;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    @KafkaListener(
            topics = "topic-1",
            containerFactory = "greetingKafkaListenerContainerFactory")
    public void greetingListener(Greeting greeting) {
        // process greeting message
        System.out.println(greeting.toString());
    }

//    //    @KafkaListener(topics = "topic1, topic2", groupId = "foo")
//    @KafkaListener(topics = "topic-1", groupId = "group-1")
//    public void listenGroup1(Greeting message) {
//        System.out.println("Received Message in group group-1: " + message);
//    }

//    @KafkaListener(topics = "topic-1")
//    public void listenWithHeaders(
//            @Payload Greeting message,
//            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
//        System.out.println(
//                "Received Message: " + message
//                        + "from partition: " + partition);
//    }
//
////    @KafkaListener(topicPartitions
////            = @TopicPartition(topic = "topicName", partitions = { "0", "1" }))
//    @KafkaListener(
//            topicPartitions = @TopicPartition(topic = "topic-1",
//                    partitionOffsets = {
//                            @PartitionOffset(partition = "0", initialOffset = "0"),
//                            @PartitionOffset(partition = "3", initialOffset = "0")}),
//            containerFactory = "partitionsKafkaListenerContainerFactory")
//    public void listenToPartition(
//            @Payload Greeting message,
//            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
//        System.out.println(
//                "Received Message: " + message
//                        + "from partition: " + partition);
//    }
//

}
