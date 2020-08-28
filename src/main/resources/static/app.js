var ws;
function setConnected(connected) {
	$("#connect").prop("disabled", connected);
	$("#disconnect").prop("disabled", !connected);
	if (connected) {
		$("#conversation").show();
	} else {
		$("#conversation").hide();
	}
	$("#greetings").html("");
}

function connect() {

	//connect to stomp where stomp endpoint is exposed
	var socket = new WebSocket("ws://localhost:8080/predict");
	ws = Stomp.over(socket);

	ws.connect({}, function(frame) {
		ws.subscribe("/topic/errors", function(message) {
			alert("Error " + message.body);
		});

		ws.subscribe("/topic/reply", function(message) {
			showGreeting(message.body);
		});
	}, function(error) {
		alert("STOMP error " + error);
	});
}




function disconnect() {
	if (ws != null) {
		ws.close();
	}
	setConnected(false);
	console.log("Disconnected");
}

function sendName() {
	var data = JSON.stringify({
		'male' : $("#male").val(),
		'age' : $("#age").val(),
		'education' : $("#education").val(),
		'currentSmoker' : $("#currentSmoker").val(),
		'cigsPerDay' : $("#cigsPerDay").val(),
		'bpmeds' : $("#bpmeds").val(),
		'prevalentStroke' : $("#prevalentStroke").val(),
		'prevalentHyp' : $("#prevalentHyp").val(),
		'diabetes' : $("#diabetes").val(),
		'totChol' : $("#totChol").val(),
		'sysBP' : $("#sysBP").val(),
		'diaBP' : $("#diaBP").val(),
		'BMI' : $("#BMI").val(),
		'heartRate' : $("#heartRate").val(),
		'glucose' : $("#glucose").val()
	
		
	})
	ws.send("/app/predict", {}, data);
}

function connectForDiabetic() {
	
	//connect to stomp where stomp endpoint is exposed
	var socket = new WebSocket("ws://localhost:8080/diabeticPredcition");
	ws = Stomp.over(socket);

	ws.connect({}, function(frame) {
		ws.subscribe("/topic/errors", function(message) {
			alert("Error " + message.body);
		});

		ws.subscribe("/topic/reply", function(message) {
			showGreeting(message.body);
		});
	}, function(error) {
		alert("STOMP error " + error);
	});
}

function costConnect() {
	console.log("IN COST CONNECT");
	//connect to stomp where stomp endpoint is exposed
	var socket = new WebSocket("ws://localhost:8080/costPredcition");
	ws = Stomp.over(socket);

	ws.connect({}, function(frame) {
		ws.subscribe("/topic/errors", function(message) {
			alert("Error " + message.body);
		});

		ws.subscribe("/topic/replyForCost", function(message) {
			showCostOutcome(message.body);
		});
	}, function(error) {
		alert("STOMP error " + error);
	});
}


function sendDiabeticData() {
	var data = JSON.stringify({
		'noOfPregencies' : $("#noOfPregencies").val(),
		'glucose' : $("#glucose").val(),
		'bp' : $("#bp").val(),
		'thickness' : $("#thickness").val(),
		'insulin' : $("#insulin").val(),
		'bmi' : $("#bmi").val(),
		'pedigree' : $("#pedigree").val(),
		'age' : $("#age").val(),
	
	})
	ws.send("/app/diabeticPredcition", {}, data);
}

function sendHealthCostData() {
	var data = JSON.stringify({
		'age' : $("#age").val(),
		'sex' : $("#sex").val(),
		'bmi' : $("#bmi").val(),
		'children' : $("#children").val(),
		'smoker' : $("#smoker").val(),
		
	})
	ws.send("/app/costPredcition", {}, data);
}

function classifyText() {
	var data = JSON.stringify({
		'emailbody' : $("#emailbody").val()
	})
	ws.send("/app/classifyText", {}, data);
}


function showGreeting(message) {
	$("#greetings").append("<tr><td> " + message + "</td></tr>");
}
function showDiabeticOutCome(message) {
	$("#diabeticoutcome").append("<tr><td> " + message + "</td></tr>");
}

function showCostOutcome(message) {
	$("#costVal").append("<tr><td> " + message + "</td></tr>");
}



$(function() {
	$("form").on('submit', function(e) {
		e.preventDefault();
	});
	$("#connect").click(function() {
		connect();
	});
	
	$("#costConnect").click(function() {
		costConnect();
	});
	
	
	$("#disconnect").click(function() {
		disconnect();
	});
	$("#send").click(function() {
		sendName();
	});
	$("#senddiabetic").click(function() {
		sendDiabeticData();
	});
	$("#sendHealthCostData").click(function() {
		sendHealthCostData();
	});
	$("#classifyText").click(function() {
		classifyText();
	});
	
	
});
