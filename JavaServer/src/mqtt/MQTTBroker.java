package mqtt;

import logic.Log;

public class MQTTBroker
{
    private static int qos;
    private static String broker;
    private static String clientId;

    public MQTTBroker(int qos, String ip, int port, String clientId)
    {
        this.qos = qos;
        this.broker = "tcp://"+ip+":"+port;
        this.clientId = clientId;
        Log.logmqtt.info("Created MQTT Broker with QoS: "+qos+" and broker: "+broker+" and clientId: "+clientId);
    }

    public static int getQos()
    {
        return qos;
    }

    public static String getBroker()
    {
        return broker;
    }

    public static String getClientId()
    {
        return clientId;
    }
}
