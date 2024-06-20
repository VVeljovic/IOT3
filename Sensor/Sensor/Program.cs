﻿// See https://aka.ms/new-console-template for more information
using CsvHelper;
using CsvHelper.Configuration;
using Newtonsoft.Json;
using Sensor;
using System.Globalization;
using System.Runtime.CompilerServices;
using System.Text;
using uPLibrary.Networking.M2Mqtt;
using uPLibrary.Networking.M2Mqtt.Messages;
Console.WriteLine("Hello, World!");
List<AirQualityData> airQualityDataList = new List<AirQualityData>();
void loadFromCsv()
{
    var configuration = new CsvConfiguration(CultureInfo.InvariantCulture)
    { HasHeaderRecord = false };
    string csvFilePath = "C:\\Users\\veljk\\OneDrive\\Desktop\\Cetvrta godina\\IOT2\\AirQuality.csv";
    using(var reader = new StreamReader(csvFilePath))
    using(var csv = new CsvReader(reader,CultureInfo.InvariantCulture))
    {
        while(csv.Read())
        {
            var record = csv.GetRecord<AirQualityData>();
            airQualityDataList.Add(record);
            
            
        }
    }
}
loadFromCsv();
foreach(AirQualityData airQualityData in airQualityDataList)
{
    string jsonData = JsonConvert.SerializeObject(airQualityData);
    sendToTopic(jsonData, "air_topic");
    Thread.Sleep(500);
}
sendToTopic("finish", "finish_topic");
void sendToTopic(String jsonData, String topic)
{
    string brokerAddress = "localhost";
    int port = 1883;
    string clientId = Guid.NewGuid().ToString();
    MqttClient mqttClient = new MqttClient(brokerAddress, port, false, null, null, MqttSslProtocols.None);
    try
    {
        if(!mqttClient.IsConnected)
        {
            mqttClient.Connect(clientId);
        }
        mqttClient.Publish(topic, Encoding.UTF8.GetBytes(jsonData), MqttMsgBase.QOS_LEVEL_AT_LEAST_ONCE, false);
        Console.WriteLine($"Message '{jsonData}' published to topic 'air_topic'");
    }
    catch (Exception ex)
    {
        Console.WriteLine($"Error: {ex.Message}");
    }
}
    
