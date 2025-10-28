

import module25.userservice.web.dto.UserCreateUpdateDto;
import module25.userservice.service.UserService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
    "app.kafka.user-topic=user.events",
    "kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"
})
@org.springframework.kafka.test.context.EmbeddedKafka(partitions = 1, topics = "user.events")
class UserEventPublishingIT {

  @Autowired UserService userService;
  @Autowired EmbeddedKafkaBroker broker;

  @Test
  void createsUser_andPublishesEvent() {
    Long id = userService.create(new UserCreateUpdateDto("A","a@a",20));
    var props = KafkaTestUtils.consumerProps("test-group", "true", broker);
    var cf = new DefaultKafkaConsumerFactory<String, String>(props, new StringDeserializer(), new StringDeserializer());
    var consumer = cf.createConsumer();
    broker.consumeFromAnEmbeddedTopic(consumer, "user.events");

    ConsumerRecord<String,String> record = KafkaTestUtils.getSingleRecord(consumer, "user.events");
    assertThat(record.value()).contains("USER_CREATED").contains("\"userId\":"+id).contains("\"email\":\"a@a\"");
  }
}
