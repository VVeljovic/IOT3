package iot.dashboard.config;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxConfig {
    //@Value("${influx.url}")
    private String influxUrl = "http://influxdb:8086";
    //@Value("${influx.username}")
    private String influxUserName="InternetOfThings";
//@Value("${influx.password}")
    private String influxPassword="veljko";
    private String token="FjhlzAOR_CAmLxUi2zovxfr7DjpQVRSV1No6sQ9vbGWYbyOH_eoWSaYyEOQaxL_G34akQZsO_qUijcy4Serujg==";
    private String bucket="AirQualityData";
    private String org="IotOrg";
    @Bean
    public InfluxDBClient buildConnection()
    {
       return InfluxDBClientFactory.create(influxUrl,token.toCharArray(),org,bucket);
    }

}
