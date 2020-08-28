package com.optum.healthcare;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Component;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMOreg;
import weka.classifiers.functions.SimpleLinearRegression;
import weka.classifiers.meta.Bagging;
import weka.core.Debug;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

@Component
public class HealthCareCostPredictor {
	
	public static final String MODElPATH = "C:\\Users\\ckrish10\\srujan\\ml\\modelinsurance.bin";
	public final String DATASETPATH = "C:\\Users\\ckrish10\\srujan\\ml\\";

	public static void main(String[] args) {
		
		HealthCareCostPredictor d = new HealthCareCostPredictor();
		d.convertCSSToARFF();
		InsuraceCost obj  = new InsuraceCost();
		obj.setAge(18);
		obj.setSex(1);
		obj.setBmi(33.77);
		obj.setChildren(1);
		obj.setSmoker(0);
		//obj.setRegion("southwest");
		
		d.process(obj);
	}
	public void convertCSSToARFF() {
		try {
			// load CSV
			CSVLoader loader = new CSVLoader();
			
			System.out.println(DATASETPATH + "insurance.csv");
			System.out.println(" file exists "+new File(DATASETPATH + "insurance.csv").exists());
			loader.setSource(new File(DATASETPATH + "insurance.csv"));
			Instances data = loader.getDataSet();// get instances object

			// save ARFF
			ArffSaver saver = new ArffSaver();
			saver.setInstances(data);// set the dataset we want to convert
			// and save as ARFF
			saver.setFile(new File(DATASETPATH + "insurance.arff"));
			saver.writeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is to load the data set.
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static Instances getDataSet(String fileName) throws IOException {
		int classIdx = 1;
		ArffLoader loader = new ArffLoader();
		loader.setSource(HealthCareCostPredictor.class.getResourceAsStream("/" + fileName));
		Instances dataSet = loader.getDataSet();
		dataSet.setClassIndex(classIdx);
		return dataSet;
	}

	public Map<String, Object> process(InsuraceCost obj) {
		
	
		Map<String, Object> map = new HashMap<>();
		try {
			Instances dataset = getDataSet("insurance.arff");
			dataset.randomize(new Random(2));
			int trainSize = (int) Math.round(dataset.numInstances() * 0.8);
			int testSize = dataset.numInstances() - trainSize;
			dataset.randomize(new Debug.Random(1));// if you comment this line the accuracy of the model will be droped
			Instances traindataset = new Instances(dataset, 0, trainSize);
			Instances testdataset = new Instances(dataset, trainSize, testSize);

			traindataset.setClassIndex(traindataset.numAttributes() - 1);
			testdataset.setClassIndex(testdataset.numAttributes() - 1);

			
			LinearRegression nb = new LinearRegression();
			nb.buildClassifier(traindataset);
			
			

			Evaluation eval = new Evaluation(traindataset);
			eval.evaluateModel(nb, traindataset);
			System.out.println("Evaluation for SIMPLE LINEAR REGRESSION ALGORTHIM " + eval.toSummaryString());
			
			
			
			MultilayerPerceptron ml = new MultilayerPerceptron();
			ml.buildClassifier(traindataset);
			Evaluation eval1 = new Evaluation(traindataset);
			eval1.evaluateModel(ml, traindataset);
			System.out.println("Evaluation for MultilayerPerceptron ALGORTHIM " + eval1.toSummaryString());
		
		
		
			for (int i = 0; i < traindataset.numInstances(); i++) {
				double actualValue = traindataset.instance(i).classValue();
				Instance newInst = traindataset.instance(i);
				double predSMO = nb.classifyInstance(newInst);
				
			//	System.out.println("Inst"+newInst+ " actual "+actualValue+" pr "+predSMO);
			}
			
			HealthCostModel mg = new HealthCostModel();
			Instance inst = mg.createInstance(obj, traindataset).firstInstance();
			double classname = nb.classifyInstance(inst);
			String result = String.valueOf(classname);
			result = "Predicted price of Health insurance coverage is :"+result;
			map.put("result", String.valueOf(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

}
