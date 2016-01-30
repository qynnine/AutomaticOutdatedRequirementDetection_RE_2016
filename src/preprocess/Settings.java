package preprocess;


import preprocess.snowball.EnglishStemmer;

/**
 * Do all the settings for adapting keyword extracting mechanism. Set weightings
 * and so on.
 * 
 * @author Simon Kaeser, University of Zurich
 * 
 */
public class Settings {
	// to show also changes in documentation edit the "-docchanges" attribute in
	// build.xml file
	// Weighting needs to be a positive double! Define here weighting of
	// class-changes, method-changes etc.
	public static double classWeighting = 1;// 0.8;
	public static double classWeightingPrivate = 1 ;// 0.81;
	public static double methodWeighting = 1;// 0.7;
	public static double methodWeightingPrivate = 1;// 0.71;
	public static double pckgWeighting = 1; //0.6;
	public static double cstrWeighting = 1; //0.5;
	public static double fieldWeighting = 1 ;//0.4;
	public static double fieldWeightingPrivate = 1;// 0.41;
	public static double callHierarchyWeighting = 1;// 0.2;
	public static double docWeighting = 1;
	//Weighting for sub- or super-elements (e.g. methods in added classes or class of removed field)
	
	public static double subElementWeighting = 1.5;
	// define limit of levenshteinDistance for comparing of renamings
	public static int levenshteinDistanceLimit = 5;
	// define limit of levenshteinDistance if no callhierarchy exists for
	// method/field and only distance is considered
	public static int levenshteinDistanceLimitNoCallHierarchy = 2;
	// add here all words you'd like to filter out from the results; it's
	// recommended to filter out words of package destination name (e.g. net &
	// sourceforge etc.)
	public static String[] wordsToFilterOut = {//"itrust", "main", "get", "sourceforge", "net", "edu", 
		//"ncsu", "csc", "action", "loader",  "bean",
			"set", "is", "has", "@return", "@param", "@throws", "@author", "@see", "exception", "or", "that",
			"of", "for", "to", "an", "the", "be", "string", "by", "with",  "and", "what", "in", "on", "it", "its", "this", 
			"are" };
	// words are used for filtering out external(imported) package names in
	// callhierarchy
	public static String[] filterExternalPackageNames = { "java.", "org.",
			"com." };

	// amount of levels backwards to search in call hierarchy
	public static int levels = 2;
	// turn stemming of Words on or off
	public static boolean stemmingOn = false;
	// turn considering of attribute changes on or off
	public static boolean considerAttributChanges = true;
	// turn considering of method changes on or off
	public static boolean considerMethodChanges = true;
	// turn considering of constructor changes on or off
	public static boolean considerCstrChanges = false;
	// turn considering of class changes on or off
	public static boolean considerClassChanges = true;
	// turn considering of package changes on or off
	public static boolean considerPkgChanges = true;
	// turn on/off the differentiation of class signature differences
	public static boolean enableClassSignatureDifferencing = false;
	//turn on and off the filtering of duplicate keywords
	public static boolean noDuplicates = false;
	//turn on and off printing of keywordsummary-weighting per group
	public static boolean printWeighting = false;

	// do not make any changes beyond here
	public static EnglishStemmer stemmerEN = new EnglishStemmer();
	// paths to jar files, shouldn't be changed
	public static String JarVersion1Path = "examples/JarVersion1/OldVersionLab.jar";
	public static String JarVersion2Path = "examples/JarVersion2/NewVersionLab.jar";
}
