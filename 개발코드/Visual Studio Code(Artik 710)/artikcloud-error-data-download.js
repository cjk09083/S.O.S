var webSocketUrl = "wss://api.artik.cloud/v1.1/websocket?ack=true";
var device_id = "9c8deecc68ba40bb89a1635f397c5f5e";
var device_token = "c3d520f749264fb8876dba577e439b36";

var WebSocket = require('ws');
var isWebSocketReady = false;
var ws = null;

var fs = require('fs');

var shell = require('shelljs');

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
			console.log("Error Received message: " + data + '\n');
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

function handleRcvMsg(msg){
	var msgObj = JSON.parse(msg);
	if (msgObj.type != "action") return; //Early return;

	var actions = msgObj.data.actions;
	var actionName = actions[0].name;
	console.log("The received action is " + actionName);
	var actionparameters = actions[0].parameters;
	var text = actionparameters.text;
	console.log(text);
	
    if ( text == "fire" ) {
        shell.exec('sh /root/final/code/artik710-fire-error.sh');
    } else if ( text == "door" ) {
		shell.exec('sh /root/final/code/artik710-door-error.sh');
    } else if ( text == "emergency" ) {
        shell.exec('sh /root/final/code/artik710-emergency-error.sh');
    } else if ( text == "earthquake" ) {
		shell.exec('sh /root/final/code/artik710-earthquake-error.sh');
    } else if ( text == "temp" ) {
		shell.exec('sh /root/final/code/artik710-temp-error.sh');
    } else if ( text == "jpg") {
		shell.exec('sh /root/final/code/jpg-upload.sh');
	} else if ( text == "text") {
		shell.exec('sh /root/final/code/text-interpretation.sh');
	} else if ( text == "outleton") {
		shell.exec('node /root/final/code/artikcloud-outlet-on-data.js');
	} else if ( text == "outletoff" ) {
		shell.exec('node /root/final/code/artikcloud-outlet-off-data.js');
	}
}

function exitClosePins() {
	console.log('Exit and destroy all pins!');
	process.exit();
}

start();