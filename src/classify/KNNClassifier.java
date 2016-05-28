package classify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class KNNClassifier {
	List<Data> trainList;
	List<Data> testList;
	int k;

	public static void main(String[] args) {
		KNNClassifier classifier = new KNNClassifier("data/iris.2D.train.arff", 2);
		classifier.predict("data/iris.2D.test.arff");
	}

	public KNNClassifier(String trainFilePath, int k) {
		trainList = readDataSet(trainFilePath);
		this.k = k;
	}

	private class Data {
		double[] data;
		String label;

		public Data(double[] data, String label) {
			this.data = data;
			this.label = label;
		}
	}

	public double getDist(Data testData, Data trainData) {
		double dist = 0;
		double sum = 0;
		for (int i = 0; i < testData.data.length; i++) {
			sum += (testData.data[i] - trainData.data[i])
					* (testData.data[i] - trainData.data[i]);
		}
		dist = Math.sqrt(sum);
		return dist;
	}

	public List<Data> readDataSet(String path) {
		List<Data> dataList = new ArrayList<Data>();
		File file = new File(path);
		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferReader = new BufferedReader(fileReader);
			String lineString = "";
			while ((lineString = bufferReader.readLine()) != null) {
				if (lineString.startsWith("@data")) {
					while ((lineString = bufferReader.readLine()) != null) {
						String[] rows = lineString.trim().split(",");

						String label = rows[rows.length - 1];
						double[] dataSet = new double[rows.length - 1];
						for (int i = 0; i < dataSet.length; i++) {
							dataSet[i] = Double.valueOf(rows[i]);
						}
						Data data = new Data(dataSet, label);
						dataList.add(data);
					}
				}
			}
			bufferReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dataList;
	}

	public String classify(Data data) {
		double[] distances = new double[testList.size()];
		String label = "";

		Map<String, Integer> voteMap = new HashMap<String, Integer>();

		// initial distances
		for (int i = 0; i < distances.length; i++) {
			distances[i] = getDist(data, trainList.get(i));
		}

		for (int i = 0; i < k; i++) {
			double min = Integer.MAX_VALUE;
			int minIndex = -1;
			for (int j = 0; j < trainList.size(); j++) {
				if (distances[j] < min) {
					min = distances[j];
					minIndex = j;
				}
			}
			if (minIndex == -1) {
				System.out.println("minIndex error");
				System.exit(0);
			}
			distances[minIndex] = Integer.MAX_VALUE;// min set MAX
			String tempLabel = trainList.get(minIndex).label;
			if (voteMap.containsKey(tempLabel)) {
				voteMap.put(tempLabel, voteMap.get(tempLabel) + 1);
			} else {
				voteMap.put(tempLabel, 1);
			}
		}

		int maxCount = 0;
		Iterator<String> iter = voteMap.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			if (voteMap.get(key) > maxCount) {
				maxCount = voteMap.get(key);
				label = key;
			}
		}

		return label;
	}

	public void predict(String testPath) {
		testList = readDataSet(testPath);
		int correctCount = 0;

		for (int i = 0; i < testList.size(); i++) {
			String predictLabel = classify(testList.get(i));
			System.out.println("predict label is: " + predictLabel
					+ " -- correct label is: " + testList.get(i).label);
			if (predictLabel.equals(testList.get(i).label)) {
				correctCount++;
			}
		}
		System.out.println("正确度:" + (correctCount * 1.0 / testList.size()));
	}

}
