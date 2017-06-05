package dmis.dhyeon.FrameAnalysis.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;

public class CorpusStatistics {

	public final static String dataPath_deceptive = "data/deceptive_from_MTurk";
	public final static String dataPath_deceptive_yelp = "data/deceptive_from_Yelp";
	public final static String dataPath_deceptive_employee = "data/deception_dataset_jiwei/hotel/positive/deceptive_expert";
	public final static String dataPath_truthful = "data/truthful_from_TripAdvisor";
	public final static String dataPath_truthful_yelp = "data/truthful_from_yelp";

	public CorpusStatistics() {

	}

	public static void main(String[] args) {

		CorpusStatistics cStatistics = new CorpusStatistics();

//		cStatistics.corpusStatistics(dataPath_deceptive);
		cStatistics.corpusStatistics(dataPath_truthful_yelp);
		
		
	}

	public void corpusStatistics(String path) {

		Set<File> files = (Set<File>) listFileTree(new File(path));
		
		int cnt = 0;
		
		String line;
		
		BufferedReader bReader;
		
		String[] lineChunk;
		
		int totalWord = 0;
		
		HashSet<String> wordHash = new HashSet<String>();
		
		PTBTokenizer ptbt;
		String tokenizerOptions = "";
		
		for (File fFile : files) {
			
			try {
				
			bReader = new BufferedReader(new FileReader(fFile));
			
			
				while ((line = bReader.readLine()) != null) {
					
					
//					System.out.println(line);
					
					
					ptbt = new PTBTokenizer(new StringReader(line), new CoreLabelTokenFactory(), tokenizerOptions);

					List<String> rawWords = new ArrayList<String>();

					for (CoreLabel label; ptbt.hasNext();) {
						String token = ptbt.next().toString();
						
						System.out.println(token);
						
						wordHash.add(token);
						
						// System.out.println(token);

					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		
		System.out.println(wordHash.size());

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

}
