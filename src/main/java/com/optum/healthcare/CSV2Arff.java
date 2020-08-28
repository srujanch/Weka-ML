package com.optum.healthcare;

import java.io.File;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

public class CSV2Arff {

	public final String DATASETPATH = "C:\\Users\\ckrish10\\Documents\\workspace-spring-tool-suite-4-4.3.0.RELEASE\\weka-api\\src\\main\\resources\\";

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
}