package iot.dashboard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.influxdb.client.DeleteApi;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.Query;
import com.influxdb.client.domain.WritePrecision;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {
    private  NatsConfig natsConfig;
    private InfluxConfig influxConfig;
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(DashboardApplication.class);
    private static List<AirQualityData> airQualityDataList = new ArrayList<>();
    @Autowired
    public DashboardService(NatsConfig natsConfig, InfluxConfig influxConfig) {
        this.natsConfig = natsConfig;
        this.influxConfig = influxConfig;
        //getAllAirQualityData();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void subscribeToNats() throws IOException, InterruptedException {
        Dispatcher dispatcher = natsConfig.natsConnection().createDispatcher();
        dispatcher.subscribe("sub1", msg -> {
            String messageContent = new String(msg.getData(), StandardCharsets.UTF_8);
            try {
                AirQualityData airQualityData = mapper.readValue(messageContent, AirQualityData.class);
                //insertDataToInfluxDB(airQualityData);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            logger.info("IncomingMessage |sub1|<no reply>| " + messageContent);
        });
    }

//    public void insertDataToInfluxDB(AirQualityData airQualityData)
//    {
//        WriteApiBlocking writeApiBlocking = influxConfig.buildConnection().getWriteApiBlocking();
//        if(airQualityData!=null) {
//            writeApiBlocking.writeMeasurement(WritePrecision.MS, airQualityData);
//            System.out.println("aa");
//        }
//        getAllAirQualityData();
//
//    }

//    public void getAllAirQualityData() {
//
//        String query = "from(bucket: \"AirQualityData\") "
//                + "|> range(start: 0) "
//                + "|> filter(fn: (r) => r[\"_measurement\"] == \"airquality\") "
//                + "|> yield()";
//
//
//        QueryApi queryApi = influxConfig.buildConnection().getQueryApi();
//        List<FluxTable> tables = queryApi.query(query);
//        System.out.println(tables.size());
//        for (FluxTable fluxTable : tables) {
//
//            List<FluxRecord> records = fluxTable.getRecords();
//            System.out.println(records.size());
//            for (FluxRecord fluxRecord : records) {
//            System.out.println(fluxRecord.getValue());
//                for (Map.Entry<String, Object> entry : fluxRecord.getValues().entrySet()) {
//                    String fieldName = entry.getKey();
//                    Object value = entry.getValue();
//                    System.out.println("Ime polja"+fieldName + ": " + value);
//                }
//
//            }
//        }
//    }
}
