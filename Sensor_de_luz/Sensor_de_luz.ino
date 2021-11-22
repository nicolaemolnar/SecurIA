int analogPin = 0;
int lightvalue = 0;
int minLight = 512;
int bulbPin = 0;
bool detected = false;

void setup() {
  Serial.begin(9600);
  pinMode(analogPin,INPUT);
  pinMode(bulbPin,OUTPUT);  

}

void loop() {
  lightvalue = analogRead(analogPin);
  if (lightvalue <minLight && detected){
    //si el nivel de luz es inferior a mínimo establecido y se detecta algo en la puerta, encender luz
    digitalWrite(bulbPin,HIGH);
    delay(5000)
    digitalWrite(bulbPin,LOW);
  }
  
}
