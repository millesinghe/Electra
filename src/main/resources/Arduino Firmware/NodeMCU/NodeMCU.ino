#include <ArduinoJson.h>
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <ESP8266WebServer.h>
#include <EEPROM.h>

#include <string>

#define CONN_LED 5
#define WIFI_CONFIG 10

String ssid = "";
String password = "";

const char* nodeId = "001";
const char* nodeType = "nodeMCU";
const int RECONNECT_INTEVAL = 1000;
const int NODE_PORT = 5000;


int statusCode;
String arr[4];
int LEDTIME;
String st;
String content;

//Establishing Local server at port 80 whenever required
ESP8266WebServer httpServer(80);
WiFiServer server(NODE_PORT);
WiFiClient client;
 
void setup() {

  // REGISTER THE PINMODE OF YOUR NODE ----------------------
 
  //pinMode(4, OUTPUT);

  
  pinMode(LED_BUILTIN, OUTPUT);  
  pinMode(CONN_LED, OUTPUT);
  pinMode(WIFI_CONFIG, INPUT); 

  // -------------------------------------------------------------
  Serial.begin(115200); //Initialising if(DEBUG)Serial Monitor
  EEPROM.begin(512); //Initialasing EEPROM
  
  Serial.println();
  Serial.println("Disconnecting previously connected WiFi");
  WiFi.disconnect();
  delay(10);
  pinMode(LED_BUILTIN, OUTPUT);
  Serial.println();
  Serial.println();
  Serial.println("Startup");

  //---------------------------------------- Read eeprom for ssid and pass
  Serial.println("Reading EEPROM ssid");

  String esid;
  for (int i = 0; i < 32; ++i)
  {
    esid += char(EEPROM.read(i));
  }
  ssid = esid;
  
  Serial.print("SSID: ");
  Serial.println(ssid);
  Serial.println("Reading EEPROM pass");

  String epass = "";
  for (int i = 32; i < 96; ++i)
  {
    epass += char(EEPROM.read(i));
  }
  password = epass;

  WiFi.begin(esid.c_str(), epass.c_str());
  if (preconfigSetting())
  { 
    funcReconnect();
    return;
  }
  else
  {
    Serial.println("Turning the HotSpot On");
    launchWeb();
    setupAP();// Setup HotSpot
  }

  Serial.println();
  Serial.println("Waiting.");
  
  while ((WiFi.status() != WL_CONNECTED))
  {
    Serial.print(".");
    delay(100);
    httpServer.handleClient();
  }
}
 
void loop() 
{
  //Wifi Connection Reset Call
  int BTN_WIFI_CONFIG_STATE = digitalRead(WIFI_CONFIG);
  if(BTN_WIFI_CONFIG_STATE == 0)
  {
    Serial.println("Launch Wifi Configuration");
    
    Serial.println("Turning the HotSpot On");
    launchWeb();
    setupAP();// Setup HotSpot
  
  
    Serial.println();
    Serial.println("Waiting.");
    
    while ((WiFi.status() != WL_CONNECTED))
    {
      Serial.print(".");
      delay(1000);
      httpServer.handleClient();
    }
  }
   
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
    digitalWrite(LED_BUILTIN,LOW);
    digitalWrite(CONN_LED,HIGH);
    return;
  }
  else if(LEDTIME == 401800)
  {
    digitalWrite(LED_BUILTIN,HIGH);
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
  Serial.print("Connected to ");
  Serial.println(ssid);
 
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

bool preconfigSetting(void)
{
  int c = 0;
  Serial.print("Waiting for Wifi to connect");
  while ( c < 20 ) {
    if (WiFi.status() == WL_CONNECTED)
    {
      Serial.println("");
      return true;
    }
    delay(1000);
    Serial.print(".");
    c++;
  }
  Serial.println("");
  Serial.println("Unable to connect with preconfig details, Routing to AP");
  return false;
}

void launchWeb()
{
  Serial.println("");
  if (WiFi.status() == WL_CONNECTED)
    Serial.println("Currently in a WiFi");
  Serial.print("SoftAP IP: ");
  Serial.println(WiFi.softAPIP());
  createWebServer();
  // Start the httpServer
  httpServer.begin();
  Serial.println("Server started");
}

void setupAP(void)
{
  WiFi.mode(WIFI_STA);
  WiFi.disconnect();
  delay(100);
  int n = WiFi.scanNetworks();
  Serial.println("WiFi Scan done");
  if (n == 0)
    Serial.println("no networks found");
  else
  {
    Serial.print(n);
    Serial.println(" networks found");
    for (int i = 0; i < n; ++i)
    {
      // Print SSID and RSSI for each network found
      Serial.print(i + 1);
      Serial.print(": ");
      Serial.print(WiFi.SSID(i));
      Serial.print(" (");
      Serial.print(WiFi.RSSI(i));
      Serial.print(")");
      Serial.println((WiFi.encryptionType(i) == ENC_TYPE_NONE) ? " " : "*");
      delay(10);
    }
  }
  Serial.println("");
  st = "<ol>";
  for (int i = 0; i < n; ++i)
  {
    // Print SSID and RSSI for each network found
    st += "<li>";
    st += WiFi.SSID(i);
    st += (WiFi.encryptionType(i) == ENC_TYPE_NONE) ? " " : "*";
    st += "</li>";
  }
  st += "</ol>";
  delay(100);
  WiFi.softAP("Electra Config Server", "");
  Serial.println("Connect to 'Electra Config Server'");
  launchWeb();
  Serial.println("over");
}

void createWebServer()
{
 {
    httpServer.on("/", []() {

      IPAddress ip = WiFi.softAPIP();
      String ipStr = String(ip[0]) + '.' + String(ip[1]) + '.' + String(ip[2]) + '.' + String(ip[3]);
      content = "<!DOCTYPE HTML>\r\n<html>Hello from ESP8266 at ";
      content += "<form action=\"/scan\" method=\"POST\"><input type=\"submit\" value=\"scan\"></form>";
      content += ipStr;
      content += "<p>";
      content += st;
      content += "</p><form method='get' action='setting'><label>SSID: </label><input name='ssid' length=32><input name='pass' length=64><input type='submit'></form>";
      content += "</html>";
      httpServer.send(200, "text/html", content);
    });
    httpServer.on("/scan", []() {
      //setupAP();
      IPAddress ip = WiFi.softAPIP();
      String ipStr = String(ip[0]) + '.' + String(ip[1]) + '.' + String(ip[2]) + '.' + String(ip[3]);

      content = "<!DOCTYPE HTML>\r\n<html>go back";
      httpServer.send(200, "text/html", content);
    });

    httpServer.on("/setting", []() {
      String qsid = httpServer.arg("ssid");
      String qpass = httpServer.arg("pass");
      if (qsid.length() > 0 && qpass.length() > 0) {
        Serial.println("clearing eeprom");
        for (int i = 0; i < 96; ++i) {
          EEPROM.write(i, 0);
        }
        Serial.println(qsid);
        Serial.println("");
        Serial.println(qpass);
        Serial.println("");

        Serial.println("writing eeprom ssid:");
        for (int i = 0; i < qsid.length(); ++i)
        {
          EEPROM.write(i, qsid[i]);
          Serial.print("Wrote: ");
          Serial.println(qsid[i]);
        }
        Serial.println("writing eeprom pass:");
        for (int i = 0; i < qpass.length(); ++i)
        {
          EEPROM.write(32 + i, qpass[i]);
          Serial.print("Wrote: ");
          Serial.println(qpass[i]);
        }
        EEPROM.commit();

        content = "{\"Success\":\"saved to eeprom... reset to boot into new wifi\"}";
        statusCode = 200;
        ESP.reset();
      } else {
        content = "{\"Error\":\"404 not found\"}";
        statusCode = 404;
        Serial.println("Sending 404");
      }
      httpServer.sendHeader("Access-Control-Allow-Origin", "*");
      httpServer.send(statusCode, "application/json", content);

    });
  } 
}