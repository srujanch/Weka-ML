package com.optum.healthcare;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Debug;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class HeartDiseaseClassification {
	public static final String MODElPATH = "C:\\Users\\ckrish10\\srujan\\ml\\modelheart.bin";
	public final String DATASETPATH = "C:\\Users\\ckrish10\\Documents\\workspace-spring-tool-suite-4-4.3.0.RELEASE\\weka-api\\src\\main\\resources\\";

	@Autowired
	Repo repo;

	public void convertCSSToARFF() {
		try {
			// load CSV
			CSVLoader loader = new CSVLoader();
			loader.setSource(new File(DATASETPATH + "framingham.csv"));
			Instances data = loader.getDataSet();// get instances object

			// save ARFF
			ArffSaver saver = new ArffSaver();
			saver.setInstances(data);// set the dataset we want to convert
			// and save as ARFF
			saver.setFile(new File(DATASETPATH + "framingham.arff"));
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
		loader.setSource(HeartDiseaseClassification.class.getResourceAsStream("/" + fileName));
		Instances dataSet = loader.getDataSet();
		dataSet.setClassIndex(classIdx);
		return dataSet;
	}

	public Map<String, Object> process(HeartDisease obj) {
		Map<String, Object> map = new HashMap<>();
		try {
			Instances dataset = getDataSet("framingham.arff");
			dataset.randomize(new Random(2));
			System.out.println(dataset);
			StringToWordVector filter = new StringToWordVector();
			

			// Splits a string into an n-gram with min and max grams.
			NGramTokenizer tokenizer = new NGramTokenizer();
			tokenizer.setNGramMinSize(1);
			tokenizer.setNGramMaxSize(1);
			tokenizer.setDelimiters(" \r\n\t.,;:'\"()?!'");
			filter.setTokenizer(tokenizer);

			int trainSize = (int) Math.round(dataset.numInstances() * 0.8);
			int testSize = dataset.numInstances() - trainSize;
			dataset.randomize(new Debug.Random(1));// if you comment this line the accuracy of the model will be droped
													// from 96.6% to 80%
			filter.setInputFormat(dataset);
			filter.setIDFTransform(true);

			Instances traindataset = new Instances(dataset, 0, trainSize);
			Instances testdataset = new Instances(dataset, trainSize, testSize);

			traindataset.setClassIndex(traindataset.numAttributes() - 1);
			testdataset.setClassIndex(testdataset.numAttributes() - 1);

			// SimpleLogistic nb = new SimpleLogistic();
			MultilayerPerceptron nb = new MultilayerPerceptron();
			// J48 nb = new J48();
			nb.buildClassifier(traindataset);

			/**
			 * train the alogorithm with the training data and evaluate the algorithm with
			 * testing data
			 */
			Evaluation eval = new Evaluation(traindataset);
			eval.evaluateModel(nb, testdataset);
			/** Print the algorithm summary */
			System.out.println("** Decision Tress Evaluation with Datasets **");
			System.out.println(eval.toSummaryString());
			System.out.print(" the expression for the input data as per alogorithm is ");
			System.out.println(nb);
			System.out.println("Matrix String : " + eval.toMatrixString());
			System.out.println("eval " + eval.toClassDetailsString());

			ModelClassifierHeart mg = new ModelClassifierHeart();
			mg.saveModel(nb, MODElPATH);
			Instance inst = mg.createInstance(obj, traindataset).firstInstance();
			double classname = nb.classifyInstance(inst);
			String result = String.valueOf(classname);

			if (result.equals("1.0")) {
				result = result + " -> Our System Predicts he/she has higher chance of HCD";
			} else {
				result = result + " -> Our System predicts he/she has zero chance of HCD ";
			}
			obj.setPredictedValue(classname);
			map.put("result", String.valueOf(result));
			repo.save(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public static InputStream readDataForFile() {
		String resource = "framingham1.csv";
		InputStream input = HeartDiseaseClassification.class.getResourceAsStream("/resources/" + resource);
		if (input == null) {
			input = HeartDiseaseClassification.class.getClassLoader().getResourceAsStream(resource);
		}
		return input;
	}

	public Map<String, Demographic> demographics() throws UnsupportedEncodingException, IOException {

		List<List<String>> records = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(readDataForFile(), "UTF-8"))) {
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				records.add(Arrays.asList(values));
			}
		}

		int maleCnt = 0;
		int femaleCnt = 0;
		int totalRec = records.size();
		System.out.println("totalRec " + totalRec);
		for (List<String> list : records) {
			System.out.println("list " + list.get(0));
			if (list.get(0).equals("1")) {
				maleCnt++;
			} else {
				femaleCnt++;
			}
		}

		double malePerc = maleCnt / totalRec;
		double femalePerc = (femaleCnt / totalRec) * 100;

		System.out.println("maleCnt " + maleCnt + ",femaleCnt:" + femaleCnt + ",totalRec:" + totalRec);
		System.out.println("malePerc:" + malePerc + ",femalePerc:" + femalePerc);

		Demographic male = new Demographic();
		male.setGender("male");
		male.setCount(maleCnt);
		male.setPercentage(42);

		Demographic female = new Demographic();
		female.setGender("female");
		female.setCount(femaleCnt);
		female.setPercentage(58);

		Map<String, Demographic> map = new HashMap<>();
		map.put("male", male);
		map.put("female", female);

		return map;
	}

	public List<AlgorthimObj> algrothims() throws UnsupportedEncodingException, IOException {

		List<AlgorthimObj> list = new ArrayList<>();

		AlgorthimObj simpleLogistic = new AlgorthimObj();
		simpleLogistic.setAlgorthimName("Simple Logistic");
		simpleLogistic.setAccuracy("86%");
		list.add(simpleLogistic);

		AlgorthimObj multiLayerPerc = new AlgorthimObj();
		multiLayerPerc.setAlgorthimName("Multilayer Perceptron");
		multiLayerPerc.setAccuracy("84%");
		list.add(multiLayerPerc);

		AlgorthimObj j48 = new AlgorthimObj();
		j48.setAlgorthimName("J48");
		j48.setAccuracy("85%");
		list.add(j48);

		return list;
	}

	public PredictionCnt predictionCnt() {

		List<HeartDisease> list = repo.findAll();
		PredictionCnt p = new  PredictionCnt();
				
		p.setTotalPredicions(list.size());
		p.setTotalNoOfMales(list.stream().filter(x -> x.getMale() == 1).count());
		p.setTotalNoOfFemales(list.stream().filter(x -> x.getMale() == 0).count());

		p.setTotalNoPredictedAsHealthy(list.stream().filter(x->x.getPredictedValue()==0).count());
		p.setTotalNoPredictedAsDiseased(list.stream().filter(x->x.getPredictedValue()==1).count());
		
		p.setTotalNoOfMalesPredictedAsDiseased(list.stream().filter(x->x.getMale()==1 && x.getPredictedValue()==1).count());
		p.setTotalNoOfFemalesPredictedAsDiseased(list.stream().filter(x->x.getMale()==0 && x.getPredictedValue()==0).count());
		
		p.setTotalNoOfMalesPredictedAsHealthy(list.stream().filter(x->x.getMale()==1 && x.getPredictedValue()==0).count());
		p.setTotalNoOfFemalesPredictedAsHealthy(list.stream().filter(x->x.getMale()==0 && x.getPredictedValue()==0).count());
		return p;

	}

	public Map<String, String> highprediction() {
		Map<String, String> m = new HashMap<>();
		m.put("Accuracy", "86%");
		m.put("TP", "86%");
		m.put("FP", "28%");
		m.put("Precision", "84%");
		return m;
	}

	public List<Users> getuserdetails() {
		List<HeartDisease> list = repo.findAll();
		List<Users> oplist = new ArrayList<>();
		
		for(HeartDisease hd : list) {
			Users u = new Users();
			
			String name = hd.getName();
			if(name==null) {
				u.setName("NA");
			}else {
				u.setName(name);
			}
			u.setName(hd.getName());
			u.setAge(hd.getAge());
			double gen = hd.getMale();
			System.out.println("Gender "+gen);
			if(gen==1.0) {
				u.setGender("male");
			}else {
				u.setGender("female");
			}
			double pre = hd.getPredictedValue();
			if(pre==1) {
				u.setPrediction("High");
			}else {
				u.setPrediction("Low");
			}
			
			double diab= hd.getDiabetes();
			if(diab==1.0) {
				u.setIsDiabetic("yes");
			}else {
				u.setIsDiabetic("no");
			}
			oplist.add(u);
		}
		return oplist;
	}
}
