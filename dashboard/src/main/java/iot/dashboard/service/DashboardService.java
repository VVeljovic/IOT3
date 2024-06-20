package iot.dashboard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.influxdb.client.DeleteApi;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.Query;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import io.nats.client.Dispatcher;
import iot.dashboard.AirQualityData;
import iot.dashboard.DashboardApplication;
import iot.dashboard.config.InfluxConfig;
import iot.dashboard.config.NatsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;

@Service
public class DashboardService {
    private NatsConfig natsConfig;
    private InfluxConfig influxConfig;
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(DashboardApplication.class);
    @Autowired
    public DashboardService(NatsConfig natsConfig, InfluxConfig influxConfig)  {
        this.natsConfig = natsConfig;
        this.influxConfig = influxConfig;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void subscribeToNats() throws IOException, InterruptedException {
        Dispatcher dispatcher = natsConfig.natsConnection().createDispatcher();
        dispatcher.subscribe("sub1", msg -> {
            String messageContent = new String(msg.getData(), StandardCharsets.UTF_8);
            try {
                AirQualityData airQualityData = mapper.readValue(messageContent, AirQualityData.class);
                insertDataToInfluxDB(airQualityData);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void insertDataToInfluxDB(AirQualityData airQualityData) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        Instant timestamp = LocalDateTime.parse(airQualityData.getDate(), formatter)
                .atOffset(ZoneOffset.UTC)
                .toInstant();
        Point point = Point.measurement("air_quality")
                .addTag("location", "Niš")
                .addField("Average_CO", airQualityData.getAverageCO())
                .addField("Average_PT08S1_CO", airQualityData.getAveragePT08S1CO())
                .addField("Average_NMHC_GT", airQualityData.getAverageNMHCGT())
                .addField("Average_C6H6_GT", airQualityData.getAverageC6H6GT())
                .addField("Average_PT08S2_NMHC", airQualityData.getAveragePT08S2NMHC())
                .addField("Average_NOx_GT", airQualityData.getAverageNOxGT())
                .addField("Average_PT08S3_NOx", airQualityData.getAveragePT08S3NOx())
                .addField("Average_NO2_GT", airQualityData.getAverageNO2GT())
                .addField("Average_PT08S4_NO2", airQualityData.getAveragePT08S4NO2())
                .addField("Average_PT08S5_O3", airQualityData.getAveragePT08S5O3())
                .addField("Average_Temperature", airQualityData.getAverageTemperature())
                .addField("Average_RelativeHumidity", airQualityData.getAverageRelativeHumidity())
                .addField("Average_AbsoluteHumidity", airQualityData.getAverageAbsoluteHumidity())
                .time(timestamp, WritePrecision.MS);
         influxConfig.buildConnection().getWriteApiBlocking().writePoint(point);
    }
}

