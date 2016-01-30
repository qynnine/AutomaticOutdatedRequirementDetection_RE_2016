package relation.io;

import relation.info.*;
import util._;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by niejia on 15/2/27.
 */
public class DataRelationIO {

    private static List<FieldMonitor> faList;
    private static List<FieldMonitor> fmList;
    private static List<FieldMonitor> ppList;


    public static DataRelationList parser(String relationDBDirPath) {

        File relationDBDirFile = new File(relationDBDirPath);
        if (!relationDBDirFile.exists()) {
            _.abort("Data Relation DB dir doesn't exist");
        }

        faList = relationParser(relationDBDirFile.getPath() + "/test1.db", RelationDBType.FieldAccess);
        fmList = relationParser(relationDBDirFile.getPath() + "/test2.db", RelationDBType.FieldModification);
        ppList = relationParser(relationDBDirFile.getPath() + "/test3.db", RelationDBType.ParameterPass);

        return getDataRelationList(faList, fmList, ppList);
    }

    private static DataRelationList getDataRelationList(List<FieldMonitor> faList, List<FieldMonitor> fmList, List<FieldMonitor> ppList) {

        List<FieldMonitor> fieldMonitorsList = new ArrayList<>();
        for (FieldMonitor fa : faList) {
            fieldMonitorsList.add(fa);
        }

        for (FieldMonitor fm : fmList) {
            fieldMonitorsList.add(fm);
        }

        DataRelationList dataRelationList = new DataRelationList();

        for (DataRelation dr : getUsageRelations(fieldMonitorsList)) {
            dataRelationList.add(dr);
        }

        for (FieldMonitor pp : ppList) {
            fieldMonitorsList.add(pp);
        }



        for (DataRelation dr : getShareFieldRelations(fieldMonitorsList)) {
            dataRelationList.add(dr);
        }
//

//        DataRelationList dataRelationList = getShareFieldRelations(fieldMonitorsList);
//        DataRelationList dataRelationList = getUsageRelations(fieldMonitorsList);

        return dataRelationList;
    }



    private static DataRelationList getUsageRelations(List<FieldMonitor> fieldMonitorsList) {
        DataRelationList dataRelationList = new DataRelationList();

        for (FieldMonitor monitor : fieldMonitorsList) {

            DataRelation dr = new DataRelation();
            dr.callerClass = getClassNameFromDBFormat(monitor.getMcSignature());
            dr.calleeClass = getClassNameFromDBFormat(monitor.getfSignature());
            dr.callerMethod = monitor.getMcSignature() + "#" + monitor.getMethodName();
            dr.calleeMethod = "none";
            dr.type = monitor.getfSignature();
            dr.hashcode = monitor.getfHashcode();
            dr.isUsage = true;
            dataRelationList.add(dr);
        }

        return dataRelationList;
    }

    private static DataRelationList getShareFieldRelations(List<FieldMonitor> fieldMonitorsList) {
        // find method access same Field
        Map<String, List<String>> methodAccessSameFieldMap = new LinkedHashMap<>();
        for (FieldMonitor monitor : fieldMonitorsList) {
            if (!monitor.getfHashcode().equals("null")
                    && !monitor.getfHashcode().equals("static")
                    && !monitor.getfHashcode().equals("primitive")) {
                String accessFieldIdentify = getClassNameFromDBFormat(monitor.getfSignature()) + "#" + monitor.getfHashcode();
//                accessFieldIdentify = DAOFactory#3387681
//                System.out.println(" accessFieldIdentify = " + accessFieldIdentify );

                String methodIdentify = getClassNameFromDBFormat(monitor.getMcSignature()) + "#" + monitor.getMethodName();

                if (!methodAccessSameFieldMap.containsKey(accessFieldIdentify)) {
                    List<String> methodIdentifyList = new ArrayList<>();
                    methodIdentifyList.add(methodIdentify);
                    methodAccessSameFieldMap.put(accessFieldIdentify, methodIdentifyList);
                } else {
                    List<String> methodIdentifyList = methodAccessSameFieldMap.get(accessFieldIdentify);
                    if (!methodIdentifyList.contains(methodIdentify)) {
                        methodIdentifyList.add(methodIdentify);
                    }
                    methodAccessSameFieldMap.put(accessFieldIdentify, methodIdentifyList);
                }
            }
        }


//        System.out.println(" methodAccessSameFieldMap = " + methodAccessSameFieldMap.size());

//        DataRelationList dataRelationList = new DataRelationList();
        DataRelationList dataRelationList = new DataRelationList();
        for (String accessFieldIdentify : methodAccessSameFieldMap.keySet()) {
//            System.out.println(" accessFieldIdentify = " + accessFieldIdentify );
            if (methodAccessSameFieldMap.get(accessFieldIdentify).size() > 1) {
                for (DataRelation dr : getDataRelationAccessListSameFiled(accessFieldIdentify, methodAccessSameFieldMap.get(accessFieldIdentify))) {
                    if (!dr.getCalleeMethod().equals(dr.getCallerMethod())) {
                        dataRelationList.add(dr);
                    }
                }
            }
        }
        return dataRelationList;
    }

    private static DataRelationList getDataRelationAccessListSameFiled(String accessFieldIdentify, List<String> methodList) {
        DataRelationList dataRelationList = new DataRelationList();

        String type = accessFieldIdentify.split("#")[0];
        String hashcode = accessFieldIdentify.split("#")[1];

        for (int i = 0; i < methodList.size(); i++) {
            for (int j = i + 1; j < methodList.size(); j++) {
                DataRelation dr = new DataRelation();

                dr.callerClass = (methodList.get(i).split("#")[0]);
                dr.calleeClass = (methodList.get(j).split("#")[0]);
                dr.callerMethod = (methodList.get(i));
                dr.calleeMethod = (methodList.get(j));
                dr.type = type;
                dr.hashcode = hashcode;
                dr.isUsage = false;
                dataRelationList.add(dr);
            }
        }

        return dataRelationList;
    }

    private static List<FieldMonitor> relationParser(String relationDBPath, RelationDBType relationDBType) {

        File relationDBFile = new File(relationDBPath);
        if (!relationDBFile.exists()) {
            _.abort("Data Relation DB file doesn't exist");
        }

        Connection con;
        Statement stmt;

        List<FieldMonitor> fieldMonitorList = new ArrayList<>();

        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:" + relationDBPath);
            con.setAutoCommit(false);

            System.out.printf("Opened %s successfully\n", relationDBFile.getName());
            stmt = con.createStatement();

            String dbFileName = "";
            if (relationDBType.equals(RelationDBType.FieldAccess)) {
                dbFileName = "fieldAccess";
            } else if (relationDBType.equals(RelationDBType.FieldModification)) {
                dbFileName = "fieldModification";
            } else if (relationDBType.equals(RelationDBType.ParameterPass)) {
                dbFileName = "parameterPass";
            }

            String sql = "SELECT * FROM " + dbFileName + ";";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                if (relationDBType.equals(RelationDBType.FieldAccess)) {
                    FieldAccess fa = new FieldAccess();
                    fa.setcSignature(rs.getString("cSignature").trim());
                    fa.setoHashcode(rs.getString("oHashcode").trim());
                    fa.setfSignature(rs.getString("fSignature").trim());
                    fa.setfHashcode(rs.getString("fHashcode").trim());
                    fa.setfName(rs.getString("fName").trim());
                    fa.setMcSignature(rs.getString("McSignature").trim());
                    fa.setMethodName(rs.getString("methodName").trim());
                    fa.setMethodSignature(rs.getString("methodSignature").trim());
                    fieldMonitorList.add(fa);
                } else if (relationDBType.equals(RelationDBType.FieldModification)) {
                    FieldModification fm = new FieldModification();
                    fm.setcSignature(rs.getString("cSignature").trim());
                    fm.setoHashcode(rs.getString("oHashcode").trim());
                    fm.setfSignature(rs.getString("fSignature").trim());
                    fm.setfHashcode(rs.getString("fHashcode").trim());
                    fm.setfName(rs.getString("fName").trim());
                    fm.setMcSignature(rs.getString("McSignature").trim());
                    fm.setMethodName(rs.getString("methodName").trim());
                    fm.setMethodSignature(rs.getString("methodSignature").trim());
                    fm.setNewValue(rs.getString("newValue").trim());

                    fieldMonitorList.add(fm);


                } else if (relationDBType.equals(RelationDBType.ParameterPass)) {
                    ParameterPass pp = new ParameterPass();
                    pp.setfSignature(rs.getString("fSignature").trim());
                    pp.setfHashcode(rs.getString("fHashcode").trim());
                    pp.setfName(rs.getString("fName").trim());
                    pp.setMcSignature(rs.getString("McSignature").trim());
                    pp.setMethodName(rs.getString("methodName").trim());
                    pp.setMethodSignature(rs.getString("methodSignature").trim());
                    fieldMonitorList.add(pp);
                }
            }

            rs.close();

            System.out.println("Table " + relationDBType.toString() + " parsed successfully");
            System.out.printf("Closed %s successfully\n", relationDBType.toString());
            stmt.close();
            con.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.printf("Read %d " + relationDBType + " field relations from data.db\n", fieldMonitorList.size());

        return fieldMonitorList;
    }


    private static String getClassNameFromDBFormat(String dbFormat) {
        String className = "";
        if (!dbFormat.endsWith("_jsp;")) {
            // Ledu/ncsu/csc/itrust/dao/DAOFactory; -> DAOFactory
            String tokens[] = dbFormat.split("/");
            className = tokens[tokens.length - 1].split(";")[0];
        } else {
            // Lorg/apache/jsp/auth/patient/viewVisitedHCPs_jsp -> auth.patient.viewVisitedHCPs_jsp
            dbFormat = dbFormat.replace("_002d", "-");
            className = dbFormat.split("/jsp/")[1].replace("/", ".").split(";")[0];
        }

//        System.out.println(" className = " + className.split("\\$")[0] );
        return className.split("\\$")[0];
    }

    public static void main(String[] args) {
        String relationDirPath = "data/exp/iTrust/relation";
        DataRelationIO dataRelationIO = new DataRelationIO();
        DataRelationList dataRelationList = dataRelationIO.parser(relationDirPath);
    }
}

