<h1 id="electra-smart-home-solution">Electra Smart Home Solution</h1>
<p>This project is design for provide solution for smart home with digitizing the home equipment managements. Project based on with IoT technologies with Software development techniques.<br>
This application is provides user friendly web service based IoT device launching capability without having the advance knowledge on Arduino or NodeMCU devices</p>
<h2 id="hardware-and-software">Hardware and Software</h2>
<ul>
<li><strong>Hardware Devices</strong>
<ul>
<li>Arduino</li>
<li>NodeMCU - ESP8266 SP2101</li>
</ul>
</li>
<li><strong>Software Technologies</strong>
<ul>
<li>Java</li>
<li>Spring</li>
<li>Hibernate</li>
</ul>
</li>
<li><strong>Database Technology</strong>
<ul>
<li>MySQL</li>
</ul>
</li>
</ul>
<h2 id="components">Components</h2>
<ul>
<li><strong>Electra Node </strong>- Conector of the Home equipment with the smart home system.</li>
<li><strong>Web Service API </strong>- API layer for integrate users interaction with the system</li>
<li><strong>Electra Database </strong>- Store the system related details</li>
<li><strong>Electra Web UI</strong>- Userfriendly user interaction web interface. Project repository is [here](https://github.com/millesinghe/ElectraWeb).</li>
</ul>

<h2 id="electra-node">System Architecture</h2>
![Design](img/Architecture.png)

<h2 id="electra-node">API Layer</h2>
Server Default URL is [](http://localhost:4999)
<h3><strong>Project APIs</strong></h3>

 - GET a Project - 
	 - *URL* - http://localhost:4999/meta/project/{id}
	 - *REQUEST TYPE -*  GET
 - CREATE a Project - 
	  - *URL* -  
		  - by Id - 
		  http://localhost:4999/meta/project
					  OR
		 - by Project Name - http://localhost:4999/meta/project/byName/MilindaHouse
	  
	 - *REQUEST TYPE* - POST
	 - *CONTENT-TYPE -* application/json
	 - *DATA* - {"name":"MilindaHouse","connection_SSID":"SLT FIBRE","connection_Password":"12341234","nodesList":null}
	 
 - Edit a Project - 
 	 - *URL - http://localhost:4999/meta/project/{id}
	 - REQUEST TYPE - PUT
	 - DATA* - {"name":"MilindaHouse","connection_SSID":"SLT FIBRE","connection_Password":"19511119","ip":"192.168.8.100","nodesCount":1}

<h3><strong>Electra Node APIs</strong></h3>

 - GET an Electra Node- 
	 - *URL* - http://localhost:4999/meta/node/{id}
	 - *REQUEST TYPE -* GET
	 
 - CREATE an Electra Node- 
	  - *URL* - http://localhost:4999/meta/node
	 - *REQUEST TYPE* - POST
	 - *CONTENT-TYPE -* application/json
	 - *DATA* - {"type":"node-micro","ip":"192.168.1.2","port":5000,"project":{"id":1,"name":"MilindaHouse","connection_SSID":"SLT FIBRE","connection_Password":"19511110","ip":"192.168.8.103","nodesList":[]}}

 - Edit an Electra Node- 
 	 - *URL* - http://localhost:4999/meta/node/{id}
	 - *REQUEST TYPE* - PUT
	 - *CONTENT-TYPE* - application/json
	 - *DATA* - {"ip":"192.168.1.4","type":"node-mini","port":"5000","project":{"id":1,"name":"MilindaHouse","connection_SSID":"SLT FIBRE","connection_Password":"19511119","nodesList":[]}}

<h3><strong>Devices APIs<strong></strong></strong></h3>

 - GET a Device 
	 - *URL* - 
		  - by Id - 
		  http://localhost:4999/meta/device/{id}
					  OR
		 - by Device byName- http://localhost:4999/meta/device/byName/MilindaHouse/MilindaRoom/RoomLight
	 - *REQUEST TYPE -* GET
 - CREATE a Device 
	  - *URL* - http://localhost:4999/meta/device
	 - *REQUEST TYPE* - POST
	 - *CONTENT-TYPE -* application/json 
	 - *DATA* - {"id":1,"name":"MilindaSwitch","groupName":"MilindaRoom","type":"bulb","defaultValue":"0","project":"MilindaHouse","connectedNode":{"id":1,"type":"node-micro","ip":"192.168.1.2","port":5000,"project":{"id":1,"name":"MilindaHouse","connection_SSID":"SLT FIBRE","connection_Password":"19511110"}},"connectorSlot":"3","urlMap":{"Switch-On":"http://192.168.1.5:8081/operate/switchOn","Switch-Off":"http://192.168.1.5:8081/operate/switchOff"}}
 - Edit a Device
 	 - *URL* - http://localhost:4999/meta/device/{id}
	 - REQUEST TYPE - PUT
	 - *CONTENT-TYPE -* application/json
	 - *DATA* - {"id":1,"name":"RoomLight","groupName":"MilindaRoom","type":"DSwitch","defaultValue":"0","project":"MilindaHouse","connectedNode":{"id":1,"ip":"192.168.8.5","port":8081},"connectorSlot":"4","urlMap":{"ON":"http://192.168.8.100:8081/output/4/1/","OFF":"http://192.168.8.100:8081/output/4/0/"}}

<h3>Controller API Requests </h3>

**Request to Scan Electra Node Connected to the Existing Wi-Fi** http://localhost:4999/manage/scan/{projectName}

**Trigger a Switch/Device by REST request :**
 - *URL* - http://localhost:4999/control/switch
 - *Data* - {"isDigital":true,"value":"1","device":{"device":"RoomLight","groupName":"MilindaRoom","project":"MilindaHouse"}}

<h2> How to Setup </h2>

<h3>API Server (Spring boot)</h3>

<h3>Database Server (MySQL)</h3>


<h3>Web UI Server (React)</h3>
<!--stackedit_data:
eyJoaXN0b3J5IjpbLTE3OTE4ODMzOTMsNDI5Mzk3NjQ5LC0yOD
MzMDUzNCwtMTk4MjU2OTA3MSwtNDUxMjU2MjAxLDE2OTA4ODcz
MTksMjM4OTExMTkxLC0zNTgwODIxNTEsMTI5NjI0NTA1OSw4Nj
MyNDc4NjksLTExNTM4NTM4NywtMTQzMzcxMDMxNiwtMzMyNDU1
MzYzXX0=
-->