package iot.dashboard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import io.nats.client.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class DashboardApplication {
	private static final Logger logger = LoggerFactory.getLogger(DashboardApplication.class);
	private static final ObjectMapper mapper = new ObjectMapper();
	private final NatsConfig config;

	@Autowired
	public DashboardApplication(NatsConfig config) {
		this.config = config;
	}

	public static void main(String[] args) {
		SpringApplication.run(DashboardApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void subscribeToNats() throws IOException, InterruptedException {
		Dispatcher dispatcher = config.natsConnection().createDispatcher();
		Subscription subscription = dispatcher.subscribe("sub1", msg -> {
			String messageContent = new String(msg.getData(), StandardCharsets.UTF_8);
            try {
                AirQualityData airQualityData = mapper.readValue(messageContent, AirQualityData.class);
				System.out.println(airQualityData.getAveragePT08S3NOx());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            logger.info("IncomingMessage |sub1|<no reply>| " + messageContent);
		});
	}
}
