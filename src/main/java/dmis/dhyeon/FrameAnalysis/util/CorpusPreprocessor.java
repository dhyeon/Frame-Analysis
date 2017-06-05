package dmis.dhyeon.FrameAnalysis.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import dmis.dhyeon.FrameAnalysis.parse.ConllParser;

public class CorpusPreprocessor {

	OpenNLPSentenceDetector sentDetect;
	ConllParser parser;

	public CorpusPreprocessor() {

		// sentence tokenization
		sentDetect = new OpenNLPSentenceDetector();
		parser = new ConllParser();

	}
	public final static String dataPath ="data/";
	public final static String dataPath_paperqa = "paperqa_test_1000";
	public final static String dataPath_cbt = "cbt_test_1000";
	public final static String dataPath_cnn = "cnn_test_1000";
	public final static String dataPath_wdw = "wdw_test_1000";
	public final static String dataPath_race = "race_test_1000";
	public final static String dataPath_squad = "squad_test_1000";

	public static void main(String[] args) {

		CorpusPreprocessor cPreprocessor = new CorpusPreprocessor();

		//cPreprocessor.processFiles(dataPath_deceptive_ott);
		//cPreprocessor.processFiles(dataPath+dataPath_cbt);
		//cPreprocessor.processFiles(dataPath+dataPath_cnn);
		//cPreprocessor.processFiles(dataPath+dataPath_wdw);
		//cPreprocessor.processFiles(dataPath+dataPath_race);
		cPreprocessor.processFiles(dataPath+dataPath_squad);
		
	}

	public void processFiles(String dataPath) {

		Set<File> files = (Set<File>) listFileTree(new File(dataPath));

		BufferedWriter bWriter;
		BufferedReader bReader;
		int files_length = files.size();
		int file_count = 1;
		for (File file : files) {
			
			System.out.println("processing..."+file_count+" of "+files_length+"...fileName:"+file.getName());
			file_count += 1;
			String tmpConllPath = file.getPath().replace("data", "data\\conll");
			// System.out.println(tmpConllPath);

			try {
				bReader = new BufferedReader(new FileReader(new File(file.getPath())));
				String[] sentences = sentDetect.detector(bReader.readLine());

				File newConllFile = new File(tmpConllPath);

				// create directory if not exists
				newConllFile.getParentFile().mkdirs();
				bWriter = new BufferedWriter(new FileWriter(newConllFile));

				// split sentences for parsing
				for (String sentence : sentences) {

					// do parse for each sentence ad CONLL format
					bWriter.write(parser.doParseinConllFormat(sentence));
					bWriter.write('\n');
				}

				bWriter.close();
			} catch (Exception e) {

			}

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

}
