package dmis.dhyeon.FrameAnalysis.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

public class FrameNetStatistics {

	private static final String filePath = "D:\\DEV\\workspace-jee\\SemanticFrameAnalysis\\data\\FrameNetParsed\\deceptive_from_MTurk\\fold1\\d_hilton_1.txt";

	public final static String dataPath = "data/FrameNetParsed/";
	public final static String dataPath_paperqa = "paperqa_test_1000";
	public final static String dataPath_cbt = "cbt_test_1000";
	public final static String dataPath_cnn = "cnn_test_1000";
	public final static String dataPath_wdw = "wdw_test_1000";
	public final static String dataPath_race = "race_test_1000";
	public final static String dataPath_squad = "squad_test_1000";
	
	public static void main(String[] args) throws Exception {

		FrameNetStatistics fStatistics = new FrameNetStatistics();
		 fStatistics.getFrameStatistics(dataPath+dataPath_cnn);

	}

	public void getDocStatistics(String path) {

		Set<File> files = (Set<File>) listFileTree(new File(path));

		BufferedReader bReader;

		int docLength = 0;

		for (File file : files) {

			try {
				bReader = new BufferedReader(new FileReader(new File(file.getPath())));

				// System.out.println(file.getName());

				String line = bReader.readLine();

				docLength += line.length();

			} catch (Exception e) {

			}
		}

		System.out.println();
		System.out.println();

		System.out.println("------------- " + path);
		System.out.println("avg doc length " + (docLength / 400));

	}

	public void getFrameStatistics(String path) {
		int numFrames = 0;

		HashSet<String> frameNameSet = new HashSet<String>();
		HashSet<String> frameNameSet_bi = new HashSet<String>();
		HashSet<String> frameNameSetPerFile = new HashSet<String>();

		String[] frameName_arr;

		Set<File> files = (Set<File>) listFileTree(new File(path));

		HashMap<String, Integer> frameCount = new HashMap<String, Integer>();
		HashMap<String, Integer> frameCount_bi = new HashMap<String, Integer>();

		// for each FrameNet_parsed files
		for (File file : files) {

			try {

				BufferedReader bReader = new BufferedReader(new FileReader(file));

				String line;

				while ((line = bReader.readLine()) != null) {
					Object obj = JSONValue.parse(line);

					JSONObject jObject = (JSONObject) obj;
					JSONArray frames = (JSONArray) jObject.get("frames");

					Iterator it = frames.iterator();

					while (it.hasNext()) {
						JSONObject frame = (JSONObject) it.next();
						JSONObject target = (JSONObject) frame.get("target");

						String frameName = (String) target.get("name");

						// print frame name for debugging
						// System.out.println(frameName);

						frameNameSet.add(frameName);
						frameNameSetPerFile.add(frameName);

						if (frameCount.containsKey(frameName)) {
							frameCount.put(frameName, frameCount.get(frameName) + 1);
						} else {
							frameCount.put(frameName, 1);
						}

						numFrames++;
					}

					frameName_arr = frameNameSet.toArray(new String[frameNameSet.size()]);

					for (int i = 0; i < frameName_arr.length; i++) {

						if (i + 1 < frameName_arr.length) {
							String tmpBiFrame = frameName_arr[i] + " " + frameName_arr[i + 1];

							if (frameCount_bi.containsKey(tmpBiFrame)) {
								frameCount_bi.put(tmpBiFrame, frameCount_bi.get(tmpBiFrame) + 1);
							} else {
								frameCount_bi.put(tmpBiFrame, 1);
							}
						}
					}

					// System.out.println("----------------------------------");
				}

				// System.out.println(frameNameSetPerFile);
				// System.out.println("----------------------------------");

				frameNameSetPerFile.clear();

			} catch (Exception e) {
				// read error
			}

		}

		// print statistics

		System.out.println();
		System.out.println();

		System.out.println("------------- " + path);
		System.out.println("# of files " + files.size());
		System.out.println("# of frames " + numFrames);
		System.out.println("# of unique frames " + frameNameSet.size());

		System.out.println();
		System.out.println();

		// frame count

		Map<String, Integer> sortedMapDesc = sortByComparator(frameCount, false);

		int totalSum = 0;

		int sqrSum = 0;
		
		
		for (String key : sortedMapDesc.keySet()) {

			totalSum += sortedMapDesc.get(key);

			sqrSum += Math.pow(sortedMapDesc.get(key), 2);

		}
		
		

		for (String key : sortedMapDesc.keySet()) {
			System.out.println(key + "\t" + sortedMapDesc.get(key));

			totalSum += sortedMapDesc.get(key);

			sqrSum += Math.pow(sortedMapDesc.get(key), 2);

		}

		double var = (sqrSum / numFrames) - Math.pow((totalSum / numFrames), 2);
		
		
		
		System.out.println("var " + var);
		

		System.out.println();
		System.out.println("Frame bigram count");
		System.out.println();
		
		
		
		Map<String, Integer> sortedMapDesc_bi = sortByComparator(frameCount_bi, false);
		
		for (String key : sortedMapDesc_bi.keySet()) {
			System.out.println(key + "\t" + sortedMapDesc_bi.get(key));
		}
		

	}

	public static Collection<File> listFileTree(File dir) {
		Set<File> fileTree = new HashSet<File>();

		// iterate files recurssively
		for (File entry : dir.listFiles()) {
			if (entry.isFile()) {
				fileTree.add(entry);
			} else {
				fileTree.addAll(listFileTree(entry));
			}
		}
		return fileTree;
	}

	private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap, final boolean order) {

		List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortMap.entrySet());

		// Sorting the list based on values
		Collections.sort(list, new Comparator<Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				if (order) {
					return o1.getValue().compareTo(o2.getValue());
				} else {
					return o2.getValue().compareTo(o1.getValue());

				}
			}
		});

		// Maintaining insertion order with the help of LinkedList
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Entry<String, Integer> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}
}
