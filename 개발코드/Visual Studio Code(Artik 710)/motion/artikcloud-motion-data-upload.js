var webSocketUrl = "wss://api.artik.cloud/v1.1/websocket?ack=true";
var device_id = "6ff72057db6b47409c9ed7d4f27c8b01";
var device_token = "2e205150679340b9b51497f3fb0a191c";

var WebSocket = require('ws');
var isWebSocketReady = false;
var ws = null;

var fs = require('fs');

setTimeout(function() {
    sendSensorValueToArtikCloud();
}, 3000);

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
        var motiontime = fs.readFileSync('/root/final/file/txt/motion/motiontime.txt');
        var motiontimestring = motiontime.toString();
        var motionplace = fs.readFileSync('/root/final/file/txt/motion/motionplace.txt');
        var motionplacestring = motionplace.toString();
        var data = {
              "motiontime": motiontimestring, "motionplace": motionplacestring
            };
        var payload = '{"sdid":"'+device_id+'"'+ts+', "data": '+JSON.stringify(data)+', "cid":"'+getTimeMillis()+'"}';        
        console.log('Sending payload ' + payload + '\n');
        ws.send(payload, {mask: true});
        setTimeout(() => ws.terminate(), 5000);
    } catch (e) {
        console.error('Error in sending a message: ' + e.toString() +'\n');
    }    
}

function exitClosePins() {
    console.log('Exit and destroy all pins!');
    process.exit();
}

start();