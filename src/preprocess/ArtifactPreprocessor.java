package preprocess;

import util.AppConfigure;

import java.util.Vector;

/**
 * Created by niejia on 15/2/22.
 */
public class ArtifactPreprocessor {

//    private static final String stopwordsPath = "data/stopwords/stop-words_english_1_en.txt";
//    private static final String stopwordsPath = "data/stopwords/retro_stopwords.txt";

    public static String handlePureTextFile(String str) {
        str = CleanUp.chararctorClean(str);
        str = CleanUp.lengthFilter(str, 3);
        str = CleanUp.tolowerCase(str);
        str = Snowball.stemming(str);
        str = Stopwords.remover(str, AppConfigure.Stopwords);
        return str;
    }

    public static String handleJavaFile(String str) {
        str = CleanUp.chararctorClean(str);
        str = CamelCase.split(str);
        str = SentenceSplitter.process(str);
        str = CleanUp.lengthFilter(str, 3);
        str = CleanUp.tolowerCase(str);
        str = Snowball.stemming(str);
        str = Stopwords.remover(str, AppConfigure.Stopwords);
        return str;
    }


    public static String handleJSEPCodeChange(String str) {
//        str = CleanUp.chararctorClean(str);
//        // warning sentence he camelcase的顺序问题
//        str = SentenceSplitter.process(str);
//        str = CamelCase.split(str);
////        str = CleanUp.lengthFilter(str, 3);
//        str = CleanUp.tolowerCase(str);
////        str = Snowball.stemming(str);
//        str = Stopwords.remover(str, AppConfigure.JSEPCommonWord);
        str = CleanUp.chararctorClean(str);
        str = splitWords(str);
        str = Stopwords.remover(str, AppConfigure.JSEPCommonWord);

        return str;
    }

    public static String handleJSEPDoc(String str) {
        str = CleanUp.chararctorClean(str);
//        str = splitWords(str);
        str = Stopwords.remover(str, AppConfigure.JSEPCommonWord);
        return str;
    }


    public static String handleMethodBody(String str) {
        str = CleanUp.chararctorClean(str);
        str = CamelCase.split(str);
        str = CleanUp.tolowerCase(str);
        str = Stopwords.remover(str, AppConfigure.JavaKeywords);

        return str;
    }

    private static String splitWords(String str) {
        Vector<String> vector = new Vector<String>();
        String tempStr = new String();
        Boolean tempCharBeforeIsUpperCase = false;
        Boolean charAfterIsUpperCase = false;
        Boolean charAfterIsLetter = false;
        for (int i = 0; i < str.length(); i++) {
            Character tempChar = str.charAt(i);
            if (i > 0) {
                if (i + 1 < str.length()) {
                    Character charAfter = str.charAt(i + 1);
                    charAfterIsUpperCase = charAfter.isUpperCase(charAfter);
                    charAfterIsLetter = charAfter.isLetter(charAfter);
                }
                Character charBefore = str.charAt(i - 1);
                tempCharBeforeIsUpperCase = charBefore.isUpperCase(charBefore);
            }

            if (!tempChar.isLetter(tempChar)) {
                if (tempStr.length() > 1) {
                    if (Settings.stemmingOn) {
                        Settings.stemmerEN.setCurrent(tempStr);
                        Settings.stemmerEN.stem();
                        vector.add(Settings.stemmerEN.getCurrent());
                    } else {
                        vector.add(tempStr);
                    }
                    tempStr = "";
                }
            } else if ((tempChar.isLowerCase(tempChar) || tempStr.length() < 2 || (tempCharBeforeIsUpperCase && (charAfterIsUpperCase || !charAfterIsLetter)))) {
                if (tempStr.length() == 0) {
                    tempChar = tempChar.toLowerCase(tempChar);
                    tempStr = tempChar.toString();
                } else {
                    tempChar = tempChar.toLowerCase(tempChar);
                    tempStr = tempStr.concat(tempChar.toString());
                }
            } else {
                if (Settings.stemmingOn) {
                    Settings.stemmerEN.setCurrent(tempStr);
                    Settings.stemmerEN.stem();
                    vector.add(Settings.stemmerEN.getCurrent());
                } else {
                    vector.add(tempStr);
                }
                tempStr = "";
                tempChar = tempChar.toLowerCase(tempChar);
                tempStr = tempChar.toString();
            }
        }
        if (!tempStr.equalsIgnoreCase("")) {
            if (Settings.stemmingOn) {
                Settings.stemmerEN.setCurrent(tempStr);
                Settings.stemmerEN.stem();
                vector.add(Settings.stemmerEN.getCurrent());
            } else {
                vector.add(tempStr);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (String s : vector) {
            sb.append(s);
            sb.append(" ");
        }
        return sb.toString();

        // The following code was used to get the elements names (without split and without package name) for Exp 5-A
		/*String term =str;
		if (str.contains(".")){
			term = str.substring(str.lastIndexOf(".")+1);
		}else
			term =str;

		term = term.replaceAll("<", " ");
		term = term.replaceAll(">", " ");
		vector.add(term);
		System.out.println("############### TERM #################");
		System.out.println("########TERM :"+term);
		return vector;*/
    }


}