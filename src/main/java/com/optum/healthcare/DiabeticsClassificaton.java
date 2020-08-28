package com.optum.healthcare;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Component;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SimpleLogistic;
import weka.core.Debug;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

@Component
public class DiabeticsClassificaton {
	
	public static final String MODElPATH = "C:\\Users\\ckrish10\\srujan\\ml\\modeldiabetic.bin";
	public final String DATASETPATH = "C:\\Users\\ckrish10\\srujan\\ml\\";

	public static void main(String[] args) {
		
		DiabeticsClassificaton d = new DiabeticsClassificaton();
		//d.convertCSSToARFF();
		Diabetic obj = new Diabetic();
		obj.setNoOfPregencies(2);
		obj.setGlucose(158);
		obj.setBp(90);
		obj.setThickness(0);
		obj.setInsulin(0);
		obj.setBmi(31.6);
		obj.setPedigree(0.8);
		obj.setAge(66);
		d.process(obj);
	}
	public void convertCSSToARFF() {
		try {
			// load CSV
			CSVLoader loader = new CSVLoader();
			
			System.out.println(DATASETPATH + "diabetes-dataset.csv");
			System.out.println(" file exists "+new File(DATASETPATH + "diabetes-dataset.csv").exists());
			loader.setSource(new File(DATASETPATH + "diabetes-dataset.csv"));
			Instances data = loader.getDataSet();// get instances object

			// save ARFF
			ArffSaver saver = new ArffSaver();
			saver.setInstances(data);// set the dataset we want to convert
			// and save as ARFF
			saver.setFile(new File(DATASETPATH + "diabetes-dataset.arff"));
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
		loader.setSource(DiabeticsClassificaton.class.getResourceAsStream("/" + fileName));
		Instances dataSet = loader.getDataSet();
		dataSet.setClassIndex(classIdx);
		return dataSet;
	}

	public Map<String, Object> process(Diabetic obj) {
		Map<String, Object> map = new HashMap<>();
		try {
			Instances dataset = getDataSet("diabetes-dataset.arff");
			dataset.randomize(new Random(2));
			int trainSize = (int) Math.round(dataset.numInstances() * 0.8);
			int testSize = dataset.numInstances() - trainSize;
			dataset.randomize(new Debug.Random(1));// if you comment this line the accuracy of the model will be droped
			Instances traindataset = new Instances(dataset, 0, trainSize);
			Instances testdataset = new Instances(dataset, trainSize, testSize);

			traindataset.setClassIndex(traindataset.numAttributes() - 1);
			testdataset.setClassIndex(testdataset.numAttributes() - 1);

			
			SimpleLogistic nb = new SimpleLogistic();
			nb.buildClassifier(traindataset);
			
			

			/**
			 * train the alogorithm with the training data and evaluate the algorithm with
			 * testing data
			 */
			Evaluation eval = new Evaluation(traindataset);
			eval.evaluateModel(nb, testdataset);
			System.out.print(" the expression for the input data as per alogorithm is ");
			System.out.println(nb);
			System.out.println("Matrix String : " + eval.toMatrixString());
			System.out.println("eval " + eval.toClassDetailsString());

			
			
			MultilayerPerceptron ml = new MultilayerPerceptron();
			ml.buildClassifier(traindataset);
			Evaluation eval1 = new Evaluation(testdataset);
			eval1.evaluateModel(ml, testdataset);
			System.out.println("Evaluation for MultilayerPerceptron ALGORTHIM " + eval1.toMatrixString());
			System.out.println("ClassDetails for MultilayerPerceptron ALGORTHIM " + eval1.toClassDetailsString());
			
		
			for (int i = 0; i < traindataset.numInstances(); i++) {
				double actualValue = traindataset.instance(i).classValue();
				Instance newInst = traindataset.instance(i);
				double predSMO = nb.classifyInstance(newInst);
				
				//System.out.println("Inst"+newInst+ " actual "+actualValue+" pr "+predSMO);
			}
			
			ModelClassifierDiabetic mg = new ModelClassifierDiabetic();
			Instance inst = mg.createInstance(obj, traindataset).firstInstance();
			double classname = nb.classifyInstance(inst);
			String result = String.valueOf(classname);

			if (result.equals("1.0")) {
				result = result + " -> Our System Predicts she has higher chance of occurence of Diabetics ";
			} else {
				result = result + " -> Our System predicts she has chance of occurence of Diabetics ";
			}
			
			map.put("result", String.valueOf(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

}
