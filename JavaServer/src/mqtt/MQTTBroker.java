package mqtt;

import logic.Log;

public class MQTTBroker
{
    private int qos;
    private String broker;
    private String clientId;

    public MQTTBroker(String clientId, String ip, int port, int qos)
    {
        this.qos = qos;
        this.broker = "tcp://"+ip+":"+port;
        this.clientId = clientId;
        Log.logmqtt.info("Created MQTT Broker with QoS: "+qos+" and broker: "+broker+" and clientId: "+clientId);
    }

    public int getQos()
    {
        return qos;
    }

    public String getBroker()
    {
        return broker;
    }

    public String getClientId()
    {
        return clientId;
    }
}
