DependenSee: A Dependency Parse Visualisation Tool
================================

Here's the source for DependenSee, a dependency relation visualisation tool for the Stanford parser. 

More details can be found on my [original blog post](http://chaoticity.com/dependensee-a-dependency-parse-visualisation-tool/)

Here's some sample code. 


    import edu.stanford.nlp.ling.CoreLabel;
    import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
    import edu.stanford.nlp.process.CoreLabelTokenFactory;
    import edu.stanford.nlp.process.PTBTokenizer;
    import edu.stanford.nlp.process.TokenizerFactory;
    import edu.stanford.nlp.trees.GrammaticalStructure;
    import edu.stanford.nlp.trees.GrammaticalStructureFactory;
    import edu.stanford.nlp.trees.PennTreebankLanguagePack;
    import edu.stanford.nlp.trees.Tree;
    import edu.stanford.nlp.trees.TreebankLanguagePack;
    import edu.stanford.nlp.trees.TypedDependency;
    import java.io.File;
    import java.io.StringReader;
    import java.util.Collection;
    import java.util.List;
    
    class Test {
       public static void main(String []args) throws Exception {
           String text = "A quick brown fox jumped over the lazy dog.";
            TreebankLanguagePack tlp = new PennTreebankLanguagePack();
            GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
            LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
            lp.setOptionFlags(new String[]{"-maxLength", "500", "-retainTmpSubcategories"});
            TokenizerFactory<CoreLabel> tokenizerFactory =
                    PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
            List<CoreLabel> wordList = tokenizerFactory.getTokenizer(new StringReader(text)).tokenize();
            Tree tree = lp.apply(wordList);    
            GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
            Collection<TypedDependency> tdl = gs.typedDependenciesCCprocessed(true);
            Main.writeImage(tree,tdl, "image.png",3);
      }
    }
