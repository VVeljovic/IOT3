using CsvHelper.Configuration;
using CsvHelper;
using Filter;
using NATS.Client;
using System.Globalization;
using System.Text;
using Newtonsoft.Json;
using uPLibrary.Networking.M2Mqtt;
using uPLibrary.Networking.M2Mqtt.Messages;
IConnection connection = new ConnectionFactory().CreateConnection("nats://nats:4222");
string sub1 = "dashboard";
string lastDate = "";
List<AirQualityData> airQualityList = new List<AirQualityData>();
int numberOfElements = -1;
 void Subscribe()
{
    string clientId = Guid.NewGuid().ToString();
    MqttClient mqttClient = new MqttClient("mosquitto", 1883, false, null, null, MqttSslProtocols.None);

    mqttClient.MqttMsgPublishReceived += Client_MqttMsgPublishReceived; 

    try
    {
        if (!mqttClient.IsConnected)
        {
            mqttClient.Connect(clientId);
        }
        mqttClient.Subscribe(new string[] { "eKuiperTopic"}, new byte[] { MqttMsgBase.QOS_LEVEL_AT_LEAST_ONCE });
        Console.WriteLine($"Subscribed to topic 'eKuiperTopic'");
    }
    catch (Exception ex)
    {
        Console.WriteLine($"Error: {ex.Message}");
    }
}
Subscribe();
 void Client_MqttMsgPublishReceived(object sender, MqttMsgPublishEventArgs e)
{

   
        
        string message = Encoding.UTF8.GetString(e.Message);
        AirQualityData airQualityData = JsonConvert.DeserializeObject<AirQualityData>(message);
    if (lastDate == "")
    {
        lastDate = airQualityData.Date;
    }
    if (airQualityData.Date != lastDate)
        {
        sendToNatsTopic();
        lastDate = airQualityData.Date;
        airQualityList.Clear();
    }
     
       airQualityList.Add(airQualityData);
    
        
    
}
void sendToNatsTopic()
{

    var aggregatedData = new
    {
        Date = airQualityList[0].DateTime.Date,
        Average_CO = airQualityList.Average(d => d.CO_GT),
        Average_PT08S1_CO = airQualityList.Average(d => d.PT08_S1_CO),
        Average_NMHC_GT = airQualityList.Average(d => d.NMHC_GT),
        Average_C6H6_GT = airQualityList.Average(d => d.C6H6_GT),
        Average_PT08S2_NMHC = airQualityList.Average(d => d.PT08_S2_NMHC),
        Average_NOx_GT = airQualityList.Average(d => d.NOx_GT),
        Average_PT08S3_NOx = airQualityList.Average(d => d.PT08_S3_NOx),
        Average_NO2_GT = airQualityList.Average(d => d.NO2_GT),
        Average_PT08S4_NO2 = airQualityList.Average(d => d.PT08_S4_NO2),
        Average_PT08S5_O3 = airQualityList.Average(d => d.PT08_S5_O3),
        Average_Temperature = airQualityList.Average(d => d.T),
        Average_RelativeHumidity = airQualityList.Average(d => d.RH),
        Average_AbsoluteHumidity = airQualityList.Average(d => d.AH)
    };

    connection.Publish(sub1, Encoding.UTF8.GetBytes(JsonConvert.SerializeObject(aggregatedData)));

    Console.WriteLine(aggregatedData);
}