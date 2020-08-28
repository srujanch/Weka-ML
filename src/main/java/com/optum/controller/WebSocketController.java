package com.optum.controller;


import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.Gson;
import com.optum.email.WekaEmailClassifier;
import com.optum.healthcare.Diabetic;
import com.optum.healthcare.DiabeticsClassificaton;
import com.optum.healthcare.HealthCareCostPredictor;
import com.optum.healthcare.HeartDisease;
import com.optum.healthcare.HeartDiseaseClassification;
import com.optum.healthcare.InsuraceCost;

import weka.classifiers.Evaluation;


@Controller
public class WebSocketController {

	private static final Logger log = Logger.getLogger(WebSocketController.class.getClass().getName());
	
	@Autowired
	private SimpMessageSendingOperations messagingTemplate;
	
	
	@Autowired
	HeartDiseaseClassification heartDiseaseClassification;
	
	@Autowired
	DiabeticsClassificaton diabeticsClassificaton;
	
	@Autowired
	WekaEmailClassifier wekaEmailClassifier;
	
	
	@Autowired
	HealthCareCostPredictor healthCareCostPredictor;

	@MessageMapping("/predict")
	@SendTo("/topic/reply")
	public String predictController(@Payload String message) throws Exception {

		HeartDisease obj = new HeartDisease();
		String res = "";
		try {
			obj.setMale(getValue(message, "male"));
			obj.setAge(Integer.parseInt(new Gson().fromJson(message, Map.class).get("age").toString()));
			obj.setEducation(getValue(message, "education"));
			obj.setCurrentSmoker(getValue(message, "currentSmoker"));
			obj.setCigsPerDay(getValue(message, "cigsPerDay"));
			obj.setBPMeds(getValue(message, "bpmeds"));
			obj.setPrevalentStroke(getValue(message, "prevalentStroke"));
			obj.setPrevalentHyp(getValue(message, "prevalentHyp"));
			obj.setDiabetes(getValue(message, "diabetes"));
			obj.setTotChol(getValue(message, "totChol"));
			obj.setSysBP(getValue(message, "sysBP"));
			obj.setDiaBP(getValue(message, "diaBP"));
			obj.setBMI(getValue(message, "BMI"));
			obj.setHeartRate(getValue(message, "heartRate"));
			obj.setGlucose(getValue(message, "glucose"));

			Map<String, Object> map = heartDiseaseClassification.process(obj);
			messagingTemplate.convertAndSend("/topic/reply", (String)map.get("result"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	@MessageMapping("/diabeticPredcition")
	@SendTo("/topic/reply")
	public String diabeticPredictController(@Payload String message) throws Exception {

		Diabetic obj = new Diabetic();
		String res = "";
		try {
			obj.setNoOfPregencies(Integer.parseInt(new Gson().fromJson(message, Map.class).get("noOfPregencies").toString()));
			obj.setGlucose(Integer.parseInt(new Gson().fromJson(message, Map.class).get("glucose").toString()));
			obj.setBp(Integer.parseInt(new Gson().fromJson(message, Map.class).get("bp").toString()));
			obj.setThickness(Integer.parseInt(new Gson().fromJson(message, Map.class).get("thickness").toString()));
			obj.setInsulin(Integer.parseInt(new Gson().fromJson(message, Map.class).get("insulin").toString()));
			obj.setBmi(getValue(message, "bmi"));
			obj.setPedigree(getValue(message, "pedigree"));
			obj.setAge(Integer.parseInt(new Gson().fromJson(message, Map.class).get("age").toString()));
			
		
			Map<String, Object> map = diabeticsClassificaton.process(obj);
			messagingTemplate.convertAndSend("/topic/reply", (String)map.get("result"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	@MessageMapping("/classifyText")
	@SendTo("/topic/reply")
	public String classifyText(@Payload String message) throws Exception {

		InsuraceCost obj = new InsuraceCost();
		String res = "";
		try {
			System.out.println("Inside classifyText");
			String result = wekaEmailClassifier.classifyText(new Gson().fromJson(message, Map.class).get("emailbody").toString());
			messagingTemplate.convertAndSend("/topic/reply", result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	
	@MessageMapping("/costPredcition")
	@SendTo("/topic/replyForCost")
	public String costPredcition(@Payload String message) throws Exception {

		InsuraceCost obj = new InsuraceCost();
		String res = "";
		try {
		
			
			obj.setAge(Integer.parseInt(new Gson().fromJson(message, Map.class).get("age").toString()));
			obj.setBmi(getValue(message, "bmi"));
			obj.setChildren(Integer.parseInt(new Gson().fromJson(message, Map.class).get("children").toString()));
			obj.setSex(Integer.parseInt(new Gson().fromJson(message, Map.class).get("sex").toString()));
			obj.setSmoker(Integer.parseInt(new Gson().fromJson(message, Map.class).get("smoker").toString()));
		
			Map<String, Object> map = healthCareCostPredictor.process(obj);
			messagingTemplate.convertAndSend("/topic/replyForCost", (String)map.get("result"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	
	private Double getValue(String message,String attr) {
		String str  = new Gson().fromJson(message, Map.class).get(attr).toString();
		if(null!=str && !str.isEmpty()) {
		return Double.parseDouble(str);
		}
		return new Double(0);
	}
	
	@MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

}
