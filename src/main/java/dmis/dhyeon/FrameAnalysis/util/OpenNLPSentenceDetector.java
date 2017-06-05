package dmis.dhyeon.FrameAnalysis.util;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;

public class OpenNLPSentenceDetector {
	private SentenceDetectorME detector;

	public OpenNLPSentenceDetector() {
		try {
			detector = new SentenceDetectorME(new SentenceModel(new FileInputStream("model/en-sent.bin")));
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String[] detector(String input) {
		return detector.sentDetect(input);
	}
}
