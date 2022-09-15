var webSocketUrl = "wss://api.artik.cloud/v1.1/websocket?ack=true";
var device_id = "6ff72057db6b47409c9ed7d4f27c8b01";
var device_token = "2e205150679340b9b51497f3fb0a191c";

var WebSocket = require('ws');
var isWebSocketReady = false;
var ws = null;

var shell = require('shelljs');

setTimeout(function() {
    sendSensorValueToArtikCloud();
}, 30000);

function getTimeMillis(){
    return parseInt(Date.now().toString());
}

function start() {
    //Create the WebSocket connection
    isWebSocketReady = false;
    ws = new WebSocket(webSocketUrl);
    ws.on('open', function() {
        console.log("WebSocket connection is open ....");
        register();
    });
    ws.on('message', function(data) {
       console.log("Received message: " + data + '\n');
       handleRcvMsg(data);
    });
    ws.on('close', function() {
        console.log("WebSocket connection is closed ....");
        exitClosePins();
    });
}

function register(){
    console.log("Registering device on the WebSocket connection");
    try{
        var registerMessage = 
           '{"type":"register", "sdid":"'+device_id+'", "Authorization":"bearer '+device_token+'", "cid":"'+getTimeMillis()+'"}';
        console.log('Sending register message ' + registerMessage + '\n');
        ws.send(registerMessage, {mask: true});
        isWebSocketReady = true;
    }
    catch (e) {
        console.error('Failed to register messages. Error in registering message: ' 
        + e.toString());
    }    
}

function sendSensorValueToArtikCloud(){
    try{
        ts = ', "ts": '+getTimeMillis();
        var data = {
              "webcam": "StreamingOn"
            };
        var payload = '{"sdid":"'+device_id+'"'+ts+', "data": '+JSON.stringify(data)+', "cid":"'+getTimeMillis()+'"}';        
        console.log('Sending payload ' + payload + '\n');
        ws.send(payload, {mask: true});
    } catch (e) {
        console.error('Error in sending a message: ' + e.toString() +'\n');
    }    
}

function sendSensorValueToArtikCloudOff(){
    try{
        ts = ', "ts": '+getTimeMillis();
        var data = {
              "webcam": "StreamingOff"
            };
        var payload = '{"sdid":"'+device_id+'"'+ts+', "data": '+JSON.stringify(data)+', "cid":"'+getTimeMillis()+'"}';        
        console.log('Sending payload ' + payload + '\n');
        ws.send(payload, {mask: true});
    } catch (e) {
        console.error('Error in sending a message: ' + e.toString() +'\n');
    }    
}

function handleRcvMsg(msg){
	var msgObj = JSON.parse(msg);
	if (msgObj.type != "action") return; //Early return;

	var actions = msgObj.data.actions;
	var actionName = actions[0].name;
	console.log("The received action is " + actionName);
	var actionparameters = actions[0].parameters;
	var text = actionparameters.text;
	console.log(text);

    if ( text == "StreamingOff") {
        shell.exec("kill $(ps aux |awk '/ffmpeg/ {print $2}')");
        sendSensorValueToArtikCloudOff();
        setTimeout(() => ws.terminate(), 3000);
    }

}

function exitClosePins() {
    console.log('Exit and destroy all pins!');
    process.exit();
}

start();