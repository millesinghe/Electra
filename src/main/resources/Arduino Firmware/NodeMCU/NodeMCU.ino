#include <ArduinoJson.h>
#include <ESP8266WiFi.h>

#include <string>

#define CONN_LED 5

const char* ssid = "Airtel-E5573-639A";
const char* password = "715fgaaj";

const char* nodeId = "001";
const char* nodeType = "nodeMCU";
const int RECONNECT_INTEVAL = 1000;
const int NODE_PORT = 5000;

String arr[4];
int LEDTIME;

WiFiServer server(NODE_PORT);
WiFiClient client;
 
void setup() {

  // REGISTER THE PINMODE OF YOUR NODE ----------------------
 
  pinMode(4, OUTPUT);
  pinMode(CONN_LED, OUTPUT);

  // -------------------------------------------------------------
  Serial.begin(115200);
  // Connect to WiFi network
  Serial.println();
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);
 
  WiFi.begin(ssid, password);
  LEDTIME = 0;
  funcReconnect();
  Serial.println(LEDTIME);
}
 
void loop() 
{
  if(WiFi.status() != WL_CONNECTED)
  {
    Serial.print("Re-");
    funcReconnect();
  }
  signalIndicator();
  
  // Check if a client has connected
  client = server.available();
  if (!client) {
      return;
  }
 
  // Wait until the client sends some data
  Serial.println("new client");
  delay (1);
 
  // Read the first line of the request
  String req = client.readStringUntil('\r');
  Serial.println(req);
  client.flush();

  if (req.indexOf("/ping") != -1)  {
      const size_t CAPACITY = JSON_OBJECT_SIZE(2);
      StaticJsonDocument<CAPACITY> doc;

      // create an object
      JsonObject object = doc.to<JsonObject>();
      object["type"] = nodeType;
      object["id"] = nodeId;
      
      sendJSONResponse(doc);
  }
  else if (req.indexOf("/output") != -1) 
  { 
    // DIGITAL OUTPUT
    splitter(req,' ');

    String filterReq = arr[1];
    Serial.print("filterReq : ");
    Serial.println(filterReq);
    
//  SAMPLE URL - http://192.168.8.104:8081/output/4/0/
    splitter(filterReq,'/');

     Serial.print("Port - ");
     Serial.println(arr[2]);
     Serial.print("Value - ");
     Serial.println(arr[3]);
     
    digitalWrite(arr[2].toInt(),arr[3].toInt());
    String logger = "Send value : ";
    logger +=  arr[3];
    logger += " to port ";
    Serial.print(logger);
    Serial.println(arr[2]);
  }
  else if (req.indexOf("/analogOutput") != -1) 
  { 
    // ANALOG OUTPUT
    splitter(req,' ');

    String filterReq = arr[1];
    Serial.print("filterReq : ");
    Serial.println(filterReq);
    
//  SAMPLE URL - http://192.168.8.104:8081/analogOutput/4/0/
    splitter(filterReq,'/');
      
    analogWrite(arr[2].toInt(),arr[3].toInt());
    String logger = "Send value : ";
    logger +=  arr[3];
    logger += " to port ";
    Serial.print(logger);
    Serial.println(arr[2]);
  }
  else if (req.indexOf("/input") != -1) 
  {
    // DIGITAL INPUT
     splitter(req,' ');

    String filterReq = arr[1];
    Serial.print("filterReq : ");
    Serial.println(filterReq);
    
//  SAMPLE URL - http://192.168.8.104:8081/input/1/
    splitter(filterReq,'/');
      
    digitalRead(arr[2].toInt());
    Serial.print("Read value from Port: ");
    Serial.println(arr[2]);
  }
  else if (req.indexOf("/analogInput") != -1) 
  {
    // ANALOG INPUT
     splitter(req,' ');

    String filterReq = arr[1];
    Serial.print("filterReq : ");
    Serial.println(filterReq);
    
//  SAMPLE URL - http://192.168.8.104:8081/analogInput/1/
    splitter(filterReq,'/');
      
    analogRead(arr[2].toInt());
    Serial.print("Read value from Port: ");
    Serial.println(arr[2]);
  }
   
 
  delay(1);
  Serial.println("Client disonnected");
  Serial.println("---------");
 
}

void signalIndicator()
{  
  LEDTIME++;
  if(LEDTIME == 390000)
  {
    digitalWrite(CONN_LED,HIGH);
    return;
  }
  else if(LEDTIME == 401800)
  {
    digitalWrite(CONN_LED,LOW);
    LEDTIME = 0;
    return;
  }
}

void funcReconnect()
{  
  Serial.print("Connecting");
  while (WiFi.status() != WL_CONNECTED) 
  {
    delay(RECONNECT_INTEVAL);
    Serial.print(".");
  }
  
  Serial.println("");
  Serial.println("WiFi connected");
 
  // Start the server
  server.begin();
  Serial.print("Node started at -");
  Serial.println(WiFi.localIP());
}

void sendJSONResponse(StaticJsonDocument<500> doc)
{
    Serial.print(F("Sending: "));
    serializeJson(doc, Serial);
    Serial.println();

    // Write response headers
    client.println(F("HTTP/1.0 200 OK"));
    client.println(F("Content-Type: application/json"));
    client.println(F("Connection: close"));
    client.print(F("Content-Length: "));
    client.println(measureJsonPretty(doc));
    client.println();

  // Write JSON document
  serializeJsonPretty(doc, client);
}

void splitter(String req, char dele)
{
  int r=0, t=0;
  for (int i=0; i < req.length(); i++)
  { 
   if(req.charAt(i) == dele) 
    { 
      arr[t] = req.substring(r, i); 
      r=(i+1); 
      t++; 
    }
  }
}