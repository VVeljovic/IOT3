using CsvHelper.Configuration;
using CsvHelper;
using Filter;
using NATS.Client;
using System.Globalization;
using System.Text;
using Newtonsoft.Json;
using uPLibrary.Networking.M2Mqtt;
using uPLibrary.Networking.M2Mqtt.Messages;
Console.WriteLine("Hello, World!");
IConnection connection = new ConnectionFactory().CreateConnection("nats://localhost:4222");
string message = "Message is: ";
string sub1 = "sub1";
string sub2 = "sub2";
List<AirQualityData> airQualityList = new List<AirQualityData>();
int numberOfElements = -1;
 void Subscribe()
{
    string clientId = Guid.NewGuid().ToString();
    MqttClient mqttClient = new MqttClient("localhost", 1883, false, null, null, MqttSslProtocols.None);

    mqttClient.MqttMsgPublishReceived += Client_MqttMsgPublishReceived; // Attach the event handler

    try
    {
        if (!mqttClient.IsConnected)
        {
            mqttClient.Connect(clientId);
        }
        mqttClient.Subscribe(new string[] { "air_topic", "finish_topic" }, new byte[] { MqttMsgBase.QOS_LEVEL_AT_LEAST_ONCE, MqttMsgBase.QOS_LEVEL_AT_LEAST_ONCE });
        Console.WriteLine($"Subscribed to topic 'ai r_topic'");
    }
    catch (Exception ex)
    {
        Console.WriteLine($"Error: {ex.Message}");
    }
}
Subscribe();
 void Client_MqttMsgPublishReceived(object sender, MqttMsgPublishEventArgs e)
{
    if (e.Topic == "air_topic")
    {
        string message = Encoding.UTF8.GetString(e.Message);
        AirQualityData airQualityData = JsonConvert.DeserializeObject<AirQualityData>(message);
        airQualityList.Add(airQualityData);
    }
    else
    {
        sendToNatsTopic();
    }
}
void sendToNatsTopic()
{
    var groupedData = airQualityList
        .GroupBy(d => d.DateTime.Date)
        .Select(g => new
        {
            Date = g.Key.Date,
            Average_CO = g.Average(d => d.CO_GT),
            Average_PT08S1_CO = g.Average(d => d.PT08_S1_CO),
            Average_NMHC_GT = g.Average(d => d.NMHC_GT),
            Average_C6H6_GT = g.Average(d => d.C6H6_GT),
            Average_PT08S2_NMHC = g.Average(d => d.PT08_S2_NMHC),
            Average_NOx_GT = g.Average(d => d.NOx_GT),
            Average_PT08S3_NOx = g.Average(d => d.PT08_S3_NOx),
            Average_NO2_GT = g.Average(d => d.NO2_GT),
            Average_PT08S4_NO2 = g.Average(d => d.PT08_S4_NO2),
            Average_PT08S5_O3 = g.Average(d => d.PT08_S5_O3),
            Average_Temperature = g.Average(d => d.T),
            Average_RelativeHumidity = g.Average(d => d.RH),
            Average_AbsoluteHumidity = g.Average(d => d.AH)
        })
        .OrderBy(g => g.Date);
    foreach (var weekData in groupedData)
    {
        connection.Publish(sub1, Encoding.UTF8.GetBytes(JsonConvert.SerializeObject(weekData)));

        Console.WriteLine(weekData);
    }
}