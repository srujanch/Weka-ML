package com.optum.healthcare;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class HeartDisease {
	@Id
	@GeneratedValue
	int id;
	String name;
	Double male;
	int age;
	Double education;
	Double currentSmoker;
	Double cigsPerDay;
	Double BPMeds;
	Double prevalentStroke;
	Double prevalentHyp;
	Double diabetes;
	Double totChol;
	Double sysBP;
	Double diaBP;
	Double BMI;
	Double heartRate;
	Double glucose;
	Double predictedValue;
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Double getPredictedValue() {
		return predictedValue;
	}
	public void setPredictedValue(Double predictedValue) {
		this.predictedValue = predictedValue;
	}
	public Double getMale() {
		return male;
	}
	public void setMale(Double male) {
		this.male = male;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Double getEducation() {
		return education;
	}
	public void setEducation(Double education) {
		this.education = education;
	}
	public Double getCurrentSmoker() {
		return currentSmoker;
	}
	public void setCurrentSmoker(Double currentSmoker) {
		this.currentSmoker = currentSmoker;
	}
	public Double getCigsPerDay() {
		return cigsPerDay;
	}
	public void setCigsPerDay(Double cigsPerDay) {
		this.cigsPerDay = cigsPerDay;
	}
	public Double getBPMeds() {
		return BPMeds;
	}
	public void setBPMeds(Double bPMeds) {
		BPMeds = bPMeds;
	}
	public Double getPrevalentStroke() {
		return prevalentStroke;
	}
	public void setPrevalentStroke(Double prevalentStroke) {
		this.prevalentStroke = prevalentStroke;
	}
	public Double getPrevalentHyp() {
		return prevalentHyp;
	}
	public void setPrevalentHyp(Double prevalentHyp) {
		this.prevalentHyp = prevalentHyp;
	}
	public Double getDiabetes() {
		return diabetes;
	}
	public void setDiabetes(Double diabetes) {
		this.diabetes = diabetes;
	}
	public Double getTotChol() {
		return totChol;
	}
	public void setTotChol(Double totChol) {
		this.totChol = totChol;
	}
	public Double getSysBP() {
		return sysBP;
	}
	public void setSysBP(Double sysBP) {
		this.sysBP = sysBP;
	}
	public Double getDiaBP() {
		return diaBP;
	}
	public void setDiaBP(Double diaBP) {
		this.diaBP = diaBP;
	}
	public Double getBMI() {
		return BMI;
	}
	public void setBMI(Double bMI) {
		BMI = bMI;
	}
	public Double getHeartRate() {
		return heartRate;
	}
	public void setHeartRate(Double heartRate) {
		this.heartRate = heartRate;
	}
	public Double getGlucose() {
		return glucose;
	}
	public void setGlucose(Double glucose) {
		this.glucose = glucose;
	}

	
	
}
