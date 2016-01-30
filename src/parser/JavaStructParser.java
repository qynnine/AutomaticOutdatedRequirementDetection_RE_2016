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
 * Created by niejia on 15/11/8.
 */
public class JavaStructParser {

    private String outputDir;

    private final CompilationUnit root;
    private TypeDeclaration typeDec;
    /*
    Struct elements in a java file
     */
    private String packageName;
    private String className;
    private String classDoc;
    private List<JMethod> jMethodList;
    private List<JField> jFieldList;

    private String strContent;


    // For convenient export parsed file
    private Map<String, String> methodNameContentMap;
    private String classContent;
    private ParserType type;

    public JavaStructParser(String path, String exportPath, ParserType type) {

        String javaSource = _.readFile(path);
        this.type = type;
        this.outputDir = exportPath;

        this.strContent = extractStrContent(javaSource);

        ASTParser parsert = ASTParser.newParser(AST.JLS3);
        parsert.setSource(javaSource.toCharArray());
        this.root = (CompilationUnit) parsert.createAST(null);

        packageName = root.getPackage().getName().getFullyQualifiedName();
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

            if (className.equals("SimClockDevice")) {
                System.out.println(className);
                System.out.println(classDoc);
            }


                    exportParsedClass();
        } else {
            System.out.println(("No Class exists in this java file"));
            return;
        }
    }

    /**
     * Handle field like a = new B();
     * @param s
     * @return
     */
    private String handleFiledFormat(String s) {
        boolean isContainsEqual = false;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '=') {
                isContainsEqual = true;
            }
        }

        if (isContainsEqual) {
            return s.split("=")[0];
        } else return s;
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
                jf.setFieldName(handleFiledFormat(fieldName));
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
            String myMethodName = method.getName().toString();
            if (myMethodName.equals(className)) {
                myMethodName = "<init>";
            }
            jm.setMethodName(packageName + "." + className + "." + myMethodName);
            jm.setMethodBody(method.getBody()==null?"null":method.getBody().toString());
            for (Object obj : method.parameters()) {
                SimpleName paraName = ((SingleVariableDeclaration) obj).getName();
                jm.addParaName(paraName.toString());
            }
            if (method.getJavadoc() != null) {
                jm.setDoc(method.getJavadoc().toString());
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

        if (type.equals(ParserType.MethodBody)) {
            for (JMethod m : jMethodList) {
                String filePath = classDir.getPath() + "/" + m.getMethodName() + ".txt";
                String methodBody = m.getMethodBody().equals("null") ? "" : m.getMethodBody();
                _.writeFile(methodBody + "\n", filePath);
            }
        } else if (type.equals(ParserType.MethodIdentifier)) {
            for (JMethod m : jMethodList) {
                StringBuffer sb = new StringBuffer();
//                sb.append(packageName);
                sb.append("\n");
                sb.append(m.getClassName());
                sb.append("\n");
                sb.append(extractPureMethodName(m.getMethodName()));
//                sb.append(m.getMethodName());
                sb.append("\n");
                for (String p : m.getParaNameList()) {
                    sb.append(p);
                    sb.append(" ");
                }
                sb.append("\n");
//                sb.append(ArtifactPreprocessor.handleMethodBody(m.getMethodBody().toString()));
//                sb.append((m.getMethodBody().toString()));
                sb.append("\n");
                sb.append(m.getDoc().toString());
                sb.append("\n");

                String filePath = classDir.getPath() + "/" + m.getMethodName() + ".txt";
                _.writeFile(sb.toString(), filePath);
            }
        } else if (type.equals(ParserType.ClassIdentifier)) {
            StringBuffer sb = new StringBuffer();

//            sb.append(packageName);
//            sb.append("\n");
////
//            sb.append(className);
//            sb.append("\n");

            for (JField f : jFieldList) {
                sb.append(f.getFieldName());
                sb.append(" ");
            }
            sb.append("\n");
            sb.append(classDoc);
            sb.append("\n");
//
//            for (JMethod m : jMethodList) {
//
//            }

            String filePath = classDir.getPath() + "/" + packageName + "." + className + ".txt";
            _.writeFile(sb.toString(), filePath);
        } else if (type.equals(ParserType.MethodComment)) {
            for (JMethod m : jMethodList) {
                StringBuffer sb = new StringBuffer();
                sb.append(m.getDoc().toString());
                sb.append("\n");

                String filePath = classDir.getPath() + "/" + m.getMethodName() + ".txt";
                _.writeFile(sb.toString(), filePath);
            }
        } else if (type.equals(ParserType.ClassComment)) {
            StringBuilder sb = new StringBuilder();
            sb.append(classDoc);
            String filePath = classDir.getPath() + "/" + packageName + "." + className + ".txt";
            _.writeFile(sb.toString(), filePath);
        } else if (type.equals(ParserType.ClassField)) {
            StringBuffer sb = new StringBuffer();
            for (JField f : jFieldList) {
                sb.append(f.getFieldName());
                sb.append("\n");
            }

            String filePath = classDir.getPath() + "/" + packageName + "." + className + ".txt";
            _.writeFile(sb.toString(), filePath);
        } else if (type.equals(ParserType.MethodParameter)) {
            for (JMethod m : jMethodList) {
                StringBuffer sb = new StringBuffer();
                for (String p : m.getParaNameList()) {
                    sb.append(p);
                    sb.append("\n");
                }

                String filePath = classDir.getPath() + "/" + m.getMethodName() + ".txt";
                _.writeFile(sb.toString(), filePath);
            }
        } else if (type.equals(ParserType.ClassMethod)) {

            StringBuffer sb = new StringBuffer();

            for (JMethod m : jMethodList) {
                sb.append(extractPureMethodName(m.getMethodName()));
                sb.append("\n");

            }

            String filePath = classDir.getPath() + "/" + packageName + "." + className + ".txt";
            _.writeFile(sb.toString(), filePath);
        } else if (type.equals(ParserType.ClassMethodFullName)) {
            StringBuffer sb = new StringBuffer();
            for (JMethod m : jMethodList) {
                sb.append(m.getMethodName());
                sb.append("\n");

            }

            String filePath = classDir.getPath() + "/" + packageName + "." + className + ".txt";
            _.writeFile(sb.toString(), filePath);
        }
    }

    private String extractPureMethodName(String name) {
        String tokens[] = name.split("\\.");
        return tokens[tokens.length - 1];
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

    public static void main(String[] args) {
        String path = "data/iTrust/codeStruct/AccessDAO.java";
        String outputPath = "data/iTrust/codeStruct/output";
        JavaStructParser structParser = new JavaStructParser(path, outputPath, ParserType.ClassIdentifier);
//        structParser.exportParsedClass();
    }

}
