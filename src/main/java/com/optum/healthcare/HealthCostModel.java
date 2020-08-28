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

public class HealthCostModel {

	public Attribute age;
	public Attribute sex;
	public Attribute bmi;
	public Attribute children;
	public Attribute smoker;
	public Attribute region;
	public Attribute charges;
	public ArrayList<Attribute> attributes;
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
			Logger.getLogger(HealthCostModel.class.getName()).log(Level.SEVERE, null, ex);
		}
		return result;
	}

	public Instances createInstance(double houseSize, double lotSize, double bedrooms, double granite, double bathroom,
			double sellingPrice) {
		dataRaw.clear();
		double[] instanceValue1 = new double[] { houseSize, lotSize, bedrooms, granite, bathroom, sellingPrice };
		dataRaw.add(new DenseInstance(1.0, instanceValue1));
		return dataRaw;
	}
	
	

	public Instances createInstance(InsuraceCost obj, Instances traindataset) {
		age = new Attribute("age");
		sex = new Attribute("sex");
		bmi = new Attribute("bmi");
		children = new Attribute("children");
		smoker = new Attribute("smoker");
		region = new Attribute("region");
		//charges = new Attribute("charges");
		attributes = new ArrayList();

		attributes.add(age);
		attributes.add(sex);
		attributes.add(bmi);
		attributes.add(children);
		attributes.add(smoker);
		attributes.add(region);
		//attributes.add(charges);
		
		FastVector fvWekaAttributesLinear = new FastVector(6); 
		fvWekaAttributesLinear.addElement(age);
		fvWekaAttributesLinear.addElement(sex);
		fvWekaAttributesLinear.addElement(bmi);
		fvWekaAttributesLinear.addElement(children);
		fvWekaAttributesLinear.addElement(smoker);
		//fvWekaAttributesLinear.addElement(charges);
		
		
		classVal = new ArrayList();
	
		fvWekaAttributesLinear.addElement(new Attribute("class", classVal));
		dataRaw = new Instances("TestInstances", fvWekaAttributesLinear, 5);
		Instance instance = new DenseInstance(5);
		instance.setValue(age, obj.getAge());
		instance.setValue(sex, obj.getSex());
		instance.setValue(bmi, obj.getBmi());
		instance.setValue(children, obj.getChildren());
		instance.setValue(smoker, obj.getSmoker());
		//instance.setValue(region, obj.getRegion());
		//instance.att
		//instance.setValue(charges, obj.getCharges());
		
		dataRaw.setClassIndex(dataRaw.numAttributes() - 1);
		dataRaw.add(instance);
		return dataRaw;
	}

}
