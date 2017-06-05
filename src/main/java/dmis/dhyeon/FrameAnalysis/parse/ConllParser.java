package dmis.dhyeon.FrameAnalysis.parse;

import java.io.StringReader;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;

public class ConllParser {

	private static String modelPath = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
	private LexicalizedParser lp;

	public ConllParser() {

		// load parser model
		lp = LexicalizedParser.loadModel(modelPath);

	}

	public String doParseinConllFormat(String input) {
		String parsedResult = "";

		TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
		Tokenizer<CoreLabel> tok = tokenizerFactory.getTokenizer(new StringReader(input));
		List<CoreLabel> rawWords2 = tok.tokenize();

		Tree parse = lp.apply(rawWords2);

		TreebankLanguagePack tlp = lp.treebankLanguagePack(); // PennTreebankLanguagePack for English
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);

		// output as CONLL format
		parsedResult = GrammaticalStructure.dependenciesToString(gs, gs.typedDependencies(), parse, true, false);

		
		return parsedResult;
	}
	
	public static void main(String[] args) {
		
		ConllParser parser = new ConllParser();
		String output = "";
		
		output = parser.doParseinConllFormat("This is a test sentence.");
		
		System.out.println(output);
		
	}

}
