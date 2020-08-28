package com.optum.healthcare;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;

public class ModelClassifierDiabetic {

	public Attribute noofpregenacies;
	public Attribute glucose;
	public Attribute bp;
	public Attribute thickness;
	public Attribute insulin;
	public Attribute bmi;
	public Attribute pedigree;
	public Attribute age;
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
			Logger.getLogger(ModelClassifierDiabetic.class.getName()).log(Level.SEVERE, null, ex);
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

	public Instances createInstance(Diabetic obj, Instances traindataset) {
		noofpregenacies = new Attribute("noofpregenacies");
		glucose = new Attribute("glucose");
		bp = new Attribute("bp");
		thickness = new Attribute("thickness");
		insulin = new Attribute("insulin");
		bmi = new Attribute("bmi");
		pedigree = new Attribute("pedigree");
		age = new Attribute("age");
		attributes = new ArrayList();

		attributes.add(noofpregenacies);
		attributes.add(glucose);
		attributes.add(bp);
		attributes.add(thickness);
		attributes.add(insulin);
		attributes.add(bmi);
		attributes.add(pedigree);
		attributes.add(age);
		
		
		
		
		
		
		classVal = new ArrayList();
		classVal.add("0");
		classVal.add("1");
		attributes.add(new Attribute("class", classVal));
		dataRaw = new Instances("TestInstances", attributes, 8);
		Instance instance = new DenseInstance(9);
		instance.setValue(noofpregenacies, obj.getNoOfPregencies());
		instance.setValue(glucose, obj.getGlucose());
		instance.setValue(bp, obj.getBp());
		instance.setValue(thickness, obj.getThickness());
		instance.setValue(insulin, obj.getInsulin());
		instance.setValue(bmi, obj.getBmi());
		instance.setValue(pedigree, obj.getPedigree());
		instance.setValue(age, obj.getAge());
		dataRaw.setClassIndex(dataRaw.numAttributes() - 1);
		dataRaw.add(instance);
		return dataRaw;
	}

}
