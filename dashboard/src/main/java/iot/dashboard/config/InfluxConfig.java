package iot.dashboard.config;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxConfig {

    @Value("${influx.url}")
    private String influxUrl;

    @Value("${influx.username}")
    private String influxUserName;

    @Value("${influx.password}")
    private String influxPassword;

    @Value("${influx.token}")
    private String token;

    @Value("${influx.bucket}")
    private String bucket;

    @Value("${influx.org}")
    private String org;
    @Bean
    public InfluxDBClient buildConnection()
    {
       return InfluxDBClientFactory.create(influxUrl,token.toCharArray(),org,bucket);
    }

}
