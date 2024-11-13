package com.ssafy.meshroom.backend.domain.kafka.admin.admiinClient;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("DuplicatedCode")
@Slf4j
@Component
public class KafkaAdminClient {

    private static String BOOTSTRAP_SERVER;

    private static AdminClient client;

    private final ApplicationContext applicationContext;

    public static DescribeTopicsResult describeTopics(String topicName) {
        return client.describeTopics(Collections.singleton(topicName));
    }

    public KafkaAdminClient(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Value("${spring.kafka.bootstrap-servers}")
    public void setBootstrapServer(String bootstrapServer) {
        BOOTSTRAP_SERVER = bootstrapServer;
    }

    @PostConstruct
    public void init() {
        if (client == null) {
            Properties properties = new Properties();
            properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
            client = AdminClient.create(properties);
        }
        createAllTopics();
    }

    public static void close() {
        if (client != null) {
            client.close();
            client = null;
        }
    }

    public static CreateTopicsResult createTopics(String topicName) {
        NewTopic newTopic = makeTopic(topicName, 1, 1);
        CreateTopicsResult topicsResult = client.createTopics(Collections.singleton(newTopic));
        topicsResult.all().whenComplete((result, exception) -> {
            if (exception != null) {
                log.error("createTopics error", exception);
            } else {
                log.info("createTopics success");
            }
        });
        return topicsResult;
    }

    public void createAllTopics() {
        String[] beanNames = applicationContext.getBeanNamesForType(NewTopic.class);
        Set<String> existingTopics;

        try {
            existingTopics = client.listTopics().names().get();
        } catch (ExecutionException | InterruptedException e) {
            log.error("Error while fetching existing topics", e);
            return;
        }

        for (String beanName : beanNames) {
            NewTopic topic = (NewTopic) applicationContext.getBean(beanName);
            if (existingTopics.contains(topic.name())) {
                log.info("Topic {} already exists, skipping creation", topic.name());
            } else {
                log.info("Topic {} added newly", topic.name());
                createTopics(topic.name());
            }
        }
    }

    public static void deleteTopics(String topicName) {
        client.deleteTopics(Collections.singleton(topicName));
    }


    public static Collection<TopicListing> getTopics() throws ExecutionException, InterruptedException {
        Collection<TopicListing> topicListings = client.listTopics().listings().get();
        if (topicListings == null || topicListings.isEmpty()) {
            log.info("No topics found");
        } else {
            for (TopicListing topicListing : topicListings) {
                log.info(String.valueOf(topicListing));
            }
        }

        return topicListings;
    }

    public static void deleteAllTopics() throws ExecutionException, InterruptedException {
        Collection<TopicListing> topics = getTopics();
        Collection<String> topicNames = new ArrayList<>();
        topics.stream().map(TopicListing::name).forEach(topicNames::add);
        DeleteTopicsResult deleteTopicsResult = client.deleteTopics(topicNames);
    }

    public static NewTopic makeTopic(String topicName, int partitionCount, int replicaCount) {
        return TopicBuilder.name(topicName).partitions(partitionCount).replicas(replicaCount).build();
    }
}
