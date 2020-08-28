package com.optum.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.optum.healthcare.AlgorthimObj;
import com.optum.healthcare.Demographic;
import com.optum.healthcare.HeartDisease;
import com.optum.healthcare.HeartDiseaseClassification;
import com.optum.healthcare.PredictionCnt;
import com.optum.healthcare.Users;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import weka.classifiers.evaluation.Prediction;

@RestController
@Api(description = "Rest API", tags = "Rest API")
public class HealthController {
	
	@Autowired
	HeartDiseaseClassification heartDiseaseClassification;

	private static final Logger LOGGER = Logger.getLogger(HealthController.class.getClass().getName());
	
	@CrossOrigin
	@PostMapping(value = "/predictapi")
	@ApiOperation(value = "predict")
	public ResponseEntity<String> predict(@RequestBody HeartDisease heartDisease) throws JsonProcessingException {
	
		Map<String, Object> map = heartDiseaseClassification.process(heartDisease);
		ObjectMapper obj = new ObjectMapper();
		return new ResponseEntity<String>(obj.writeValueAsString(map),HttpStatus.OK);
	}
	
	@CrossOrigin
	@PostMapping(value = "/demographics")
	@ApiOperation(value = "demographics")
	public ResponseEntity<String> demographics() throws UnsupportedEncodingException, IOException {
	
		Map<String, Demographic> map = heartDiseaseClassification.demographics();
		ObjectMapper obj = new ObjectMapper();
		return new ResponseEntity<String>(obj.writeValueAsString(map),HttpStatus.OK);
	}
	
	@CrossOrigin
	@GetMapping(value = "/comparealgorthims")
	@ApiOperation(value = "comparealgorthims")
	public ResponseEntity<String> comparealgorthims() throws UnsupportedEncodingException, IOException {
	
		List<AlgorthimObj> list = heartDiseaseClassification.algrothims();
		ObjectMapper obj = new ObjectMapper();
		return new ResponseEntity<String>(obj.writeValueAsString(list),HttpStatus.OK);
	}
	
	
	@CrossOrigin
	@GetMapping(value = "/predictionCount")
	@ApiOperation(value = "predictionCount")
	public ResponseEntity<String> predictionCount() throws UnsupportedEncodingException, IOException {
	
		PredictionCnt p = heartDiseaseClassification.predictionCnt();
		ObjectMapper obj = new ObjectMapper();
		return new ResponseEntity<String>(obj.writeValueAsString(p),HttpStatus.OK);
	}
	
	@CrossOrigin
	@GetMapping(value = "/highprediction")
	@ApiOperation(value = "highprediction")
	public ResponseEntity<String> highprediction() throws UnsupportedEncodingException, IOException {
	
		Map<String,String> m= heartDiseaseClassification.highprediction();
		ObjectMapper obj = new ObjectMapper();
		return new ResponseEntity<String>(obj.writeValueAsString(m),HttpStatus.OK);
	}
	
	@CrossOrigin
	@GetMapping(value = "/getuserdetails")
	@ApiOperation(value = "getuserdetails")
	public ResponseEntity<String> getuserdetails() throws UnsupportedEncodingException, IOException {
	
		List<Users> list= heartDiseaseClassification.getuserdetails();
		ObjectMapper obj = new ObjectMapper();
		return new ResponseEntity<String>(obj.writeValueAsString(list),HttpStatus.OK);
	}
	
}
