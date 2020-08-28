package com.optum.healthcare;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;



import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;

public class ModelClassifierHeart {
	
	public Attribute male;
	public Attribute age;
	public Attribute education;
	public Attribute currentSmoker;
	public Attribute cigsPerDay;
	public Attribute BPMeds;
	public Attribute prevalentStroke;
	public Attribute prevalentHyp;
	public Attribute diabetes;
	public Attribute totChol;
	public Attribute sysBP;
	public Attribute diaBP;
	public Attribute BMI;
	public Attribute heartRate;
	public Attribute glucose;
	public Attribute TenYearCHD;
	
	public ArrayList attributes;
	public ArrayList classVal;
    public Instances dataRaw;



    public void saveModel(Classifier model, String modelpath) {

        try {
            SerializationHelper.write(modelpath, model);
        } catch (Exception ex) {
            Logger.getLogger(ModelClassifierHeart.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String classifiy(Instance inst, String path) {
        String result = "Not classified!!";
        Classifier cls = null;
        try {
            cls = (NaiveBayes) SerializationHelper.read(path);
            result = (String) classVal.get((int) cls.classifyInstance(inst));
        } catch (Exception ex) {
            Logger.getLogger(ModelClassifierHeart.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public Instances createInstance(double houseSize, double lotSize, double bedrooms,double granite,double bathroom,double sellingPrice) {
        dataRaw.clear();
        double[] instanceValue1 = new double[]{houseSize, lotSize, bedrooms,granite,bathroom,sellingPrice};
        dataRaw.add(new DenseInstance(1.0, instanceValue1));
        return dataRaw;
    }





    public Instances createInstance(HeartDisease obj, Instances traindataset) {
    	male = new Attribute("male");
    	age = new Attribute("age");
    	education = new Attribute("education");
    	currentSmoker = new Attribute("currentSmoker");
    	cigsPerDay = new Attribute("cigsPerDay");
    	BPMeds = new Attribute("BPMeds");
    	prevalentStroke = new Attribute("prevalentStroke");
    	prevalentHyp = new Attribute("prevalentHyp");
    	diabetes = new Attribute("diabetes");
    	totChol = new Attribute("totChol");
    	
    	sysBP = new Attribute("sysBP");
    	diaBP = new Attribute("diaBP");
    	BMI = new Attribute("BMI");
    	heartRate = new Attribute("heartRate");
    	glucose = new Attribute("glucose");
    	
    	attributes = new ArrayList();
    
        
   

        attributes.add(male);
        attributes.add(age);
        attributes.add(education);
        attributes.add(currentSmoker);
        attributes.add(cigsPerDay);
        attributes.add(BPMeds);
        attributes.add(prevalentStroke);
        attributes.add(prevalentHyp);
        attributes.add(diabetes);
        attributes.add(totChol);
        attributes.add(sysBP);
        attributes.add(diaBP);
        attributes.add(BMI);
        attributes.add(heartRate);
        attributes.add(glucose);
        classVal = new ArrayList();
        
        classVal.add("0");
        classVal.add("1");
        attributes.add(new Attribute("class", classVal));

        dataRaw = new Instances("TestInstances", attributes, 15);
        
        Instance instance = new DenseInstance(16);
        
        //System.out.println("traindataset.attribute(\"male\") "+traindataset.attribute("male"));
        instance.setValue(male, obj.getMale());
        instance.setValue(age, obj.getAge());
        instance.setValue(education, obj.getEducation());
        instance.setValue(currentSmoker, obj.getCurrentSmoker());
        instance.setValue(cigsPerDay, obj.getCigsPerDay());
        instance.setValue(BPMeds, obj.getBPMeds());
        instance.setValue(prevalentStroke, obj.getPrevalentStroke());
        instance.setValue(prevalentHyp, obj.getPrevalentHyp());
        instance.setValue(diabetes, obj.getDiabetes());
        instance.setValue(totChol, obj.getTotChol());
        instance.setValue(sysBP, obj.getSysBP());
        instance.setValue(diaBP, obj.getDiaBP());
        instance.setValue(BMI, obj.getBMI());
        instance.setValue(heartRate, obj.getHeartRate());
        instance.setValue(glucose, obj.getGlucose());
        dataRaw.setClassIndex(dataRaw.numAttributes() - 1);
        dataRaw.add(instance);
        return dataRaw;
    }
    
}
