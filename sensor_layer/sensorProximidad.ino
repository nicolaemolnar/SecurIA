int txUS = 8;
int rxUS = 9;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  pinMode(txUS, OUTPUT);
  pinMode(rxUS, INPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
  int dist;

  dist = distance(txUS,rxUS);
  Serial.print(dist);
  Serial.println(" cm.");
}

int distance(int TriggerPin, int EchoPin){
  long duration;

  digitalWrite(TriggerPin, LOW);
  delayMicroseconds(4);
  digitalWrite(TriggerPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(EchoPin, LOW);

  duration = pulseIn(EchoPin, HIGH);
  return duration*10/292/2;
}
