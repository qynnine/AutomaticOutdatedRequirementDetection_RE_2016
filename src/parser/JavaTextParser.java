package parser;

import org.eclipse.jdt.core.dom.*;
import parser.type.JField;
import parser.type.JMethod;
import util._;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by niejia on 15/2/10.
 */
public class JavaTextParser {

    private String outputDir;

    private final CompilationUnit root;
    private TypeDeclaration typeDec;
    /*
    Struct elements in a java file
     */
    private String className;
    private String classDoc;
    private List<JMethod> jMethodList;
    private List<JField> jFieldList;

    private String strContent;


    // For convenient export parsed file
    private Map<String, String> methodNameContentMap;
    private String classContent;

    public JavaTextParser(String path, String exportPath) {

        String javaSource = _.readFile(path);
        this.outputDir = exportPath;

        this.strContent = extractStrContent(javaSource);

        ASTParser parsert = ASTParser.newParser(AST.JLS3);
        parsert.setSource(javaSource.toCharArray());
        this.root = (CompilationUnit) parsert.createAST(null);

        // types represent all class in this file, includes public and non-public class
        List types = root.types();

        if (!types.isEmpty()) {
            // types.get(0) is the first class in this file, in most case is the public class
            this.typeDec = (TypeDeclaration) types.get(0);

            className = typeDec.getName().toString();

            if (typeDec.getJavadoc() != null) {
                classDoc = typeDec.getJavadoc().toString();
            } else {
                classDoc = "";
            }

            exportParsedClass();
        } else {
            System.out.println(("No Class exists in this java file"));
            return;
        }
    }

    private void parseClass() {
//        System.out.printf("Parsing %s...\n", className);

        jFieldList = new ArrayList<>();

        for (FieldDeclaration field : typeDec.getFields()) {
            String type = field.getType().toString();

            for (Object fragment : field.fragments()) {
                JField jf = new JField();
                jf.setTypeName(field.getType().toString());

                String fieldName = fragment.toString();
                if (fieldName.endsWith("=null")) {
                    fieldName = fieldName.split("=null")[0];
                }
                jf.setFieldName(fieldName);
                jFieldList.add(jf);
            }
        }

//        System.out.printf("%d fields in %s.\n", jFieldList.size(), className);

        parseMethod();

        StringBuilder sb = new StringBuilder();
        sb.append(className);
        sb.append(" ");

        for (JField field : jFieldList) {
            sb.append(field.getFieldName());
            sb.append(" ");
        }

        for (JMethod method : jMethodList) {
            sb.append(method.getMethodName());
            sb.append(" ");
            for (String p : method.getParaNameList()) {
                sb.append(p);
                sb.append(" ");
            }
        }

        sb.append("\n");

        for (JMethod method : jMethodList) {
            sb.append(method.getDoc());
            sb.append(" ");
        }

        sb.append(classDoc);
        sb.append("\n");

//        sb.append(strContent);

//        System.out.println(sb);
        classContent = sb.toString();
    }

    private void parseMethod() {

        methodNameContentMap = new LinkedHashMap<>();
        // is this check required ?
        PackageDeclaration packetDec = root.getPackage();
        if (packetDec == null) {
            _.abort("PackageDeclaration is null");
        }

        jMethodList = new ArrayList<>();

        for (MethodDeclaration method : typeDec.getMethods()) {
            JMethod jm = new JMethod();
            jm.setClassName(className);
            jm.setMethodName(method.getName().toString());
            Javadoc doc = method.getJavadoc();
            // if the method doc doesn't exist
            if (doc != null) {
                jm.setDoc(doc.toString());
            } else {
                jm.setDoc("");
            }

            for (Object obj : method.parameters()) {
                SimpleName paraName = ((SingleVariableDeclaration) obj).getName();
                jm.addParaName(paraName.toString());
            }
//            System.out.println(jm);
            methodNameContentMap.put(className + "#" + jm.getMethodName(), jm.toString());
            jMethodList.add(jm);
        }

//        System.out.printf("%d methods in %s.\n", jMethodList.size(), className);
    }

    public void exportParsedClass() {

        parseClass();

        if (classContent == null) _.abort("Class is not parsed.");
        File classDir = new File(outputDir);
        classDir.mkdirs();
        String filePath = classDir.getPath() + "/" + className + ".java";
        _.writeFile(classContent.toString(), filePath);
    }

    private String extractStrContent(String input) {
        StringBuffer sb = new StringBuffer();

        Pattern p = Pattern.compile("\\\".*\\\"");
        Matcher m = p.matcher(input);

        while (m.find()) {
            String content = m.group().trim();
            sb.append(content.substring(1, content.length()-1));
            sb.append(" ");
        }
        return sb.toString();
    }
}


