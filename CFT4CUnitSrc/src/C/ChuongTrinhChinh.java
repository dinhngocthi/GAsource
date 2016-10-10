package C;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import SMTSolver.ConvertToSmtLibv2;
import SMTSolver.RunZ3OnCMD;

/**
 * 
 */
public class ChuongTrinhChinh
{
    private String fileInput;
    private int objectcall;

    public static void main(String[] args) throws Exception
    {
        //ChuongTrinhChinh c = new ChuongTrinhChinh("E:\\PhD\\CFT4CUnit\\CFT4CUnit\\Vi Du\\SelectionSort.c");
        ChuongTrinhChinh c = new ChuongTrinhChinh("D:/Thi.DN/PhD/GA/GASource/CFT4CUnitSrc/src/sample/tA2008_Triangle.c");
        c.run();
        c.initPathListID(3);
    }

    public ChuongTrinhChinh(String fileInput)
    {
        this.fileInput = fileInput;
        objectcall = 0;
    }

    public void run() throws Exception
    {
        staticVariable.reset();
        normalizeFunctionSource ns = new normalizeFunctionSource(Utils.getContentFile(fileInput));
        staticVariable.Paramater.source = ns.getNormalizeCode();
        staticVariable.Paramater.variableOfTC = staticVariable.Paramater.source.substring(staticVariable.Paramater.source.indexOf("(") + 1,
                                                                                          staticVariable.Paramater.source.indexOf(")"));
        getNode12();
        getNode3();
        getDanhSachKe123();
    }

    /**
     * 
     * @throws Exception
     */
    private void getNode12() throws Exception
    {
        getGraphC c = new getGraphC();
        c.runFromSource(staticVariable.Paramater.source + "");

        staticVariable.Statement.NodeElements = c.getListNodeDescription().replaceAll(";{0,1}\\s*#true=.*\\n", "\n");
        staticVariable.Statement.NodeRelations = c.getListNodeDescription().replace(";", "");

        staticVariable.Branch.NodeElements = staticVariable.Statement.NodeElements;
        staticVariable.Branch.NodeRelations = staticVariable.Statement.NodeRelations;

        staticVariable.AllPath.NodeElements = staticVariable.Statement.NodeElements;
        staticVariable.AllPath.NodeRelations = staticVariable.Statement.NodeRelations;
    }

    /**
     * 
     * 
     * @throws Exception
     */
    private void getNode3() throws Exception
    {
        getGraphC c = new getGraphC();
        c.runFromSource(staticVariable.Paramater.source);
        ArrayList<Node> mnNode = new ArrayList<>();
        String c_output = c.getListNodeDescription();
        String[] c_output_arr = c_output.split("\n");
        for (String c_output_arr1 : c_output_arr)
        {
            String[] item = c_output_arr1.split("#");
            Node n = new Node(Integer.parseInt(item[0]), item[1], Integer.parseInt(item[2].replace("true=", "")), 
            		Integer.parseInt(item[3].replace("false=", "")));
            mnNode.add(n);            
        }
        String expectOutput = new String();
        int newRootIndex = staticVariable.Statement.NodeRelations.split("\n").length - 2;
        for (int i = 0; i < mnNode.size(); i++)
        {
            String cNode = mnNode.get(i).getData();
            if (cNode.contains("&&") || cNode.contains("||"))
            {
                String oldNode = staticVariable.Statement.NodeRelations.split("\n")[i];
                String oldNodebranchTrue = "#" + oldNode.split("#")[2];
                String oldNodebranchFalse = "#" + oldNode.split("#")[3];
                analysisComplexCondition p = new analysisComplexCondition();
                p.setNewRootIndex(newRootIndex);
                p.run("(" + cNode + ")");
                String tmp = p.getResult().replaceAll("\n" + (newRootIndex + 1) + "#", "\n" + i + "#");
                tmp = tmp.replaceAll("#true=" + (newRootIndex + 1) + "#", "#true=" + i + "#");
                tmp = tmp.replaceAll("#false=" + (newRootIndex + 1) + "\n", "#false=" + i + "\n");
                tmp = tmp.replaceAll("#true=" + (newRootIndex + p.getResult().split("\n").length - 1), oldNodebranchTrue);
                tmp = tmp.replaceAll("#false=" + (newRootIndex + p.getResult().split("\n").length - 1), oldNodebranchFalse);
                //
                String[] subGraph = tmp.split("\n");
                for (int j = 1; j <= subGraph.length - 2; j++)
                {
                    expectOutput = expectOutput + subGraph[j] + "\n";
                }
                newRootIndex += tmp.split("\n").length - 1;
            }
            else
            {
                expectOutput = expectOutput + mnNode.get(i).idNode + "#" + mnNode.get(i).data + "#true=" + mnNode.get(i).true_id + "#false="
                        + mnNode.get(i).false_id + "\n";
            }
        }

        String lineCurent = "";
        for (String ele1 : expectOutput.split("\n"))
        {
            String[] ee = ele1.split("#");
            lineCurent = lineCurent + ee[0] + "#" + ee[1] + "\n";
        }
        //
        staticVariable.SubCondition.NodeRelations = expectOutput.replaceAll(";", "");
        staticVariable.SubCondition.NodeElements = lineCurent.replaceAll(";", "");
        staticVariable.AllPathSubCondition.NodeRelations = staticVariable.SubCondition.NodeRelations;
        staticVariable.AllPathSubCondition.NodeElements = staticVariable.SubCondition.NodeElements;
        //
        //System.out.println(staticVariable.AllPathSubCondition.NodeRelations);
        //System.out.println(staticVariable.AllPathSubCondition.NodeElements);
    }

    /**
     * 
     * @param indexList
     * @param dsk
     * @param nodeElementsMap
     * @return
     */
    private String getTestpath(ArrayList<Integer> indexList, Map<String, String[]> dsk, Map<String, String> nodeElementsMap)
    {
        String output = new String();
        for (int iCount = 1; iCount < indexList.size() - 1; iCount++)
        {
            int idCurrent = indexList.get(iCount);
            int idNext = indexList.get(iCount + 1);
            for (Object o : dsk.keySet())
            {
                String key = o.toString();
                String[] value = dsk.get(o);
                if (key.equals(idCurrent + ""))
                {
                    if (value[0].equals(idNext + ""))
                    {
                        output += "(" + nodeElementsMap.get(idCurrent + "") + ")#";
                        break;
                    }
                    else if (value[1].equals(idNext + ""))
                    {
                        output += "!(" + nodeElementsMap.get(idCurrent + "") + ")#";
                        break;
                    }
                }
            }
        }
        return output.substring(0, output.lastIndexOf("#"));
    }

    /**
     * 
     * @throws Exception
     */
    private void getDanhSachKe123() throws Exception
    {
        getGraphC c = new getGraphC();
        c.runFromSource(staticVariable.Paramater.source);
        ArrayList<Node> mnNode = new ArrayList<>();
        String c_output = c.getListNodeDescription();
        String[] c_output_arr = c_output.split("\n");
        for (String c_output_arr1 : c_output_arr)
        {
            String[] item = c_output_arr1.split("#");
            Node n = new Node(Integer.parseInt(item[0]), item[1], 
                    Integer.parseInt(item[2].replace("true=", "")), 
                    Integer.parseInt(item[3].replace("false=", "")));
            mnNode.add(n);
        }
        // xuat danh sach ke phu cap 1,2
        String danhsachke = new String();
        danhsachke = "-1 0\n";
        for (Node mnNode1 : mnNode)
        {
            Node n = mnNode1;
            if (n.true_id == n.false_id)
            {
                danhsachke += n.idNode + " " + n.true_id + "\n";
            }
            else
            {
                danhsachke += n.idNode + " " + n.true_id + " " + n.false_id + "\n";
            }
        }
        staticVariable.Statement.danhSachKe = danhsachke;
        staticVariable.Branch.danhSachKe = danhsachke;
        staticVariable.AllPath.danhSachKe = danhsachke;

        // xuat danh sach ke phu cap 3
        String danhSachKe3 = new String(staticVariable.SubCondition.NodeRelations);
        danhSachKe3 = Pattern.compile("#(.*)true=").matcher(danhSachKe3).replaceAll(" ");
        danhSachKe3 = Pattern.compile("#false=").matcher(danhSachKe3).replaceAll(" ");
        staticVariable.SubCondition.danhSachKe = "-1 0 0\n" + danhSachKe3;

        staticVariable.AllPathSubCondition.danhSachKe = staticVariable.SubCondition.danhSachKe;
        //System.out.println(staticVariable.AllPathSubCondition.danhSachKe);
    }

    /**
     * 
     * @param danhsachke
     *            . For example: 0(BatDauHam),10(KetThucHam)<br/>
     * <br/>
     *            Ex:-1 0<br/>
     *            0 1<br/>
     *            1 2<br/>
     *            2 3 8<br/>
     *            3 4<br/>
     *            4 5 7<br/>
     *            5 6<br/>
     *            6 7<br/>
     *            7 2<br/>
     *            8 9 11<br/>
     *            9 10<br/>
     *            10 -1<br/>
     *            11 10<br/>
     * @return a map object contains key (the first vertex in each line), and
     *         connected vertex (1 or 2) : first vertex is default to true, the
     *         other is false.
     */
    private Map<String, String[]> getDskObject(String danhsachke)
    {
        Map<String, String[]> dsk = new HashMap<>();
        for (String s : danhsachke.split("\n"))
        {
            String[] itemList = s.split(" ");
            switch (itemList.length)
            {
            case 3:
                if (itemList[1].equals(itemList[2]))
                {
                    dsk.put(itemList[0], new String[]
                    { itemList[1] });
                }
                else
                {
                    dsk.put(itemList[0], new String[]
                    { itemList[1], itemList[2] });
                }
                break;
            case 2:
                dsk.put(itemList[0], new String[]
                { itemList[1] });
                break;
            }
        }
        return dsk;
    }

    /**
     * 
     * @param nodeelement
     * @return
     */
    private Map<String, String> getNodeObject(String nodeelement)
    {
        Map<String, String> nodeElementsMap = new HashMap<>();
        for (String s : nodeelement.split("\n"))
        {
            nodeElementsMap.put(s.split("#")[0], s.split("#")[1]);
        }
        return nodeElementsMap;
    }

    /**
     * Get all test paths from source code
     * @param danhsachke
     * @param nodeElements
     * @return
     * @throws Exception
     */
    public ArrayList<ArrayList<Integer>> getAllPaths(String danhsachke, String nodeElement) throws Exception
    {
        getAllPaths geter = new getAllPaths(danhsachke, nodeElement);
        return geter.getPathList();
    }
    
    // D.N.Thi for testing
    private int[] pathListID;
    private getAllPaths geterTest;
    private double[][] disMatrix; 
    private int totalTargetPaths; // current numbers of target paths
    private ArrayList<ArrayList<VertexTF>> targetPaths;
    private ArrayList<String> equalCondList;

    public void initPathListID(int loop) throws Exception
    {
        staticVariable.Paramater.depth = loop; // interation loop
        geterTest = new getAllPaths(staticVariable.Statement.danhSachKe, staticVariable.AllPath.NodeElements);
        ArrayList<ArrayList<Vertex>> getOutput = geterTest.getOutput();
        
        totalTargetPaths = getOutput.size();
        pathListID = new int[totalTargetPaths];
        for (int i = 0; i < totalTargetPaths; i++)
            pathListID[i] = 1;
        
        // Create equal condition list for generatenewPop adjust
        equalCondList = new ArrayList<String>();
        for (int i = 0; i < totalTargetPaths; i++)
        {
            //System.out.print("Path " + i + ": ");
            ArrayList<Vertex> path = getOutput.get(i);
            int pathSize = path.size();
            int k = 0;
            for (k = 0; k < pathSize; k++)
            {
                Vertex vertex = path.get(k); 
                if (vertex.getTrueVertexId() != vertex.getFalseVertexId())
                {
                    Vertex vertexTmp = path.get(k+1);
                    if (vertex.statement.contains("=="))
                    {                        
                        if (vertex.getTrueVertexId() == vertexTmp.getId())
                        {
                            // TRUE branch
                            if (!equalCondList.contains(vertex.statement))
                            	equalCondList.add(vertex.statement);
                        }
                    }
                    else if (vertex.statement.contains("!="))
                    {                        
                        if (vertex.getFalseVertexId() == vertexTmp.getId())
                        {
                            // FALSE branch
                            String stm = vertex.statement.replace("!=", "==");
                            if (!equalCondList.contains(stm))
                                equalCondList.add(stm);
                        }
                    }
                }
            }
        }
        
        System.out.print("Equal condition list: ");
        for (int i = 0; i < equalCondList.size(); i++)
        	System.out.print("[" + equalCondList.get(i) + "] ");
        System.out.println();


        // for smt solver
        for (int i = 0; i < totalTargetPaths; i++)
        {
            ArrayList<Vertex> path = getOutput.get(i);
            int pathSize = path.size();
            int k = 0;
            for (k = 0; k < pathSize; k++)
            {
                Vertex vertex = path.get(k); 
                if (vertex.getTrueVertexId() != vertex.getFalseVertexId())
                {
                    //if (vertex.statement.contains("==") || vertex.statement.contains("!="))
                    if (vertex.statement.contains("=="))
                    {
                        System.out.println(vertex.statement);
                        break;
                    }
                }
            }

            // Using SMT constraint solver
            if (k < pathSize)
            {
                String smtFileName = "path" + i + ".smt2";
                PrintWriter fpSmt = new PrintWriter(smtFileName, "UTF-8");
                // z3
                //fpSmt.printf("(declare-const a Real)\n");
                //fpSmt.printf("(declare-const b Real)\n");
                //fpSmt.printf("(declare-const c Real)\n");

                // yices                
                fpSmt.printf("(set-logic QF_NRA)\n");
                fpSmt.printf("(declare-fun a () Real)\n");
                fpSmt.printf("(declare-fun b () Real)\n");
                fpSmt.printf("(declare-fun c () Real)\n");
                fpSmt.printf("(assert (> a 0))\n");
                fpSmt.printf("(assert (> b 0))\n");
                fpSmt.printf("(assert (> c 0))\n");

                for (k = 0; k < pathSize; k++)
                {
                    Vertex vertex = path.get(k); 
                    if (vertex.getTrueVertexId() != vertex.getFalseVertexId())
                    {
                        ConvertToSmtLibv2 c = new ConvertToSmtLibv2(vertex.statement);                        
                        c.run();
                        Vertex vertexTmp = path.get(k+1);
                        if (vertex.getFalseVertexId() == vertexTmp.getId())
                        {
                            // FALSE branch
                            fpSmt.printf("(assert (not " +  c.getOutput() + ") )\n");
                        }
                        else
                        {
                            // TRUE branch
                            fpSmt.printf("(assert " +  c.getOutput() + ")\n");
                        }
                    }
                }                
                fpSmt.printf("(check-sat)\n");
                fpSmt.printf("(get-model)");
                fpSmt.close();
                
                String classPath     = ChuongTrinhChinh.class.getProtectionDomain().getCodeSource().getLocation().getPath();
                //String smtSolverPath = classPath.replace("CFT4CUnitSrc/bin/", "z3/bin/Z3");  // Using smt solver Z3
                String smtSolverPath = classPath.replace("CFT4CUnitSrc/bin/", "yices/bin/yices-smt2");  // Using smt solver yices
                RunZ3OnCMD r = new RunZ3OnCMD(smtSolverPath, smtFileName);
                System.out.println("Path " + i + ": " + r.getOutput());
            }
        }

        PrintWriter fpOut;
        fpOut = new PrintWriter("TargetPaths", "UTF-8");

        System.out.println("------------Create all target paths start-------------");
/*        
        targetPaths = new ArrayList<ArrayList<VertexTF>>();
        int branchID = 1;
        ArrayList<String> branchlist = new ArrayList<String>();
        
        for (int i = 0; i < totalTargetPaths; i++ )
        {
            System.out.print("Path " + (i + 1) + ": ");
            ArrayList<Vertex> path = getOutput.get(i);
            int pathSize = path.size(); 

            ArrayList<VertexTF> pathTF = new ArrayList<VertexTF>();

            for (int k = 0; k < pathSize; k++)
            {
                Vertex vertex = path.get(k);
                if (vertex.getTrueVertexId() != vertex.getFalseVertexId())
                {
                    System.out.print("[" + vertex.getStatement() + "]");
                    VertexTF vertextf = new VertexTF();
                    int j = 0;
                    for (j = 0; j < branchlist.size(); j++)
                    {
                        if (branchlist.get(j).equals(vertex.getStatement()))
                            break;
                    }
                    if (j < branchlist.size())
                    {
                        vertextf.id = (j + 1);
                    }
                    else
                    {
                        vertextf.id = branchID;
                        branchID++;
                        branchlist.add(vertex.getStatement());
                    }

                    Vertex vertexTmp = path.get(k + 1);
                    if (vertex.getFalseVertexId() == vertexTmp.getId())
                    {
                        vertextf.decision = "F";
                        System.out.print("F ");
                    }
                    else
                    {
                        vertextf.decision = "T";
                        System.out.print("T ");
                    }
                    pathTF.add(vertextf);
                }
            }
            targetPaths.add(pathTF);
            fpOut.printf("Path " + (i + 1) + ":" + pathTF + "\n");
            System.out.println();
        }       
        fpOut.printf("\n");
*/

        totalTargetPaths = 4;
        targetPaths = new ArrayList<ArrayList<VertexTF>>();
        
        ArrayList<VertexTF> pathTF1 = new ArrayList<VertexTF>();        
        pathTF1.add(new VertexTF(1, "F"));
        targetPaths.add(pathTF1);
        
        ArrayList<VertexTF> pathTF2 = new ArrayList<VertexTF>();        
        pathTF2.add(new VertexTF(1, "T"));
        pathTF2.add(new VertexTF(2, "F"));        
        targetPaths.add(pathTF2);

        ArrayList<VertexTF> pathTF3 = new ArrayList<VertexTF>();        
        pathTF3.add(new VertexTF(1, "T"));
        pathTF3.add(new VertexTF(2, "T"));        
        pathTF3.add(new VertexTF(3, "F"));
        targetPaths.add(pathTF3);

        ArrayList<VertexTF> pathTF4 = new ArrayList<VertexTF>();        
        pathTF4.add(new VertexTF(1, "T"));
        pathTF4.add(new VertexTF(2, "T"));        
        pathTF4.add(new VertexTF(3, "T"));
        targetPaths.add(pathTF4);

        disMatrix = new double[totalTargetPaths][totalTargetPaths];
        for (int i = 0; i < totalTargetPaths; i++ )
        {
            for (int j = 0; j < totalTargetPaths; j++ )
            {        
                disMatrix[i][j] = calculatePathDistTF(targetPaths.get(i), targetPaths.get(j));
                fpOut.printf("[" + (i+1) + "->" + (j+1) + "]:");
                fpOut.printf("%2.5f; ", disMatrix[i][j]);
            }
            fpOut.printf("\n");
        }
        
        fpOut.close();
        System.out.println("------------Create all target paths end-------------");
    }
    
    private boolean isSamePath(ArrayList<VertexTF> path1, ArrayList<VertexTF> path2)
    {
        boolean ret = false;
        int len1 = path1.size();
        int len2 = path2.size();
        int i = 0;
        
        if (len1 == len2)
        {
            for (i = 0; i < len1; i++)
            {
                VertexTF vertex1 = path1.get(i);
                VertexTF vertex2 = path2.get(i);
                if ((vertex1.id == vertex2.id) && vertex1.decision.equals(vertex2.decision))
                    continue;
                else                
                    break;
            }
            ret = (i == len1); 
        }
        return ret;
    }
    
    private double[] getDistExecutedPath2TargetPaths(double x, double y, double z, ArrayList<VertexTF> executedPath)
    {
        int i = 0;
        int size = targetPaths.size();
        double[] ret = new double[2];
              
        if (totalTargetPaths == 0)  // Target paths list is empty
        {
            ret[0] = -2;
            return ret;
        }

        for (i = 0; i < size; i ++)
        {
            ArrayList<VertexTF> targetPathTmp = targetPaths.get(i);
            if (isSamePath(targetPathTmp, executedPath))
                break;
        }
        
        if (pathListID[i] == 1)
        {
            totalTargetPaths--; // hit a feasible path

            pathListID[i] = 0;
            ret[0] = (i+1);     // PathID
            ret[1] = 0;
        }
        else
        {
        	double sum = 0;
        	for (int j = 0; j < size; j ++)
        	{
        		if (i != j && pathListID[j] == 1)
        			//sum += disMatrix[i][j];
        			sum += distance(i+1, j+1, x, y, z);
        	}
            ret[0] = -1;
        	ret[1] = ((double)sum/(double)totalTargetPaths);
        }

        return ret;
    }

    private double distance(int pathid1, int pathid2, double x, double y, double z)
    {
    	double ret = 0;
    	switch (pathid1)
    	{
    		case 1:
    			switch (pathid2)
    	    	{
    	    		case 2:
    	    			ret = Math.abs(x + y - 1024) - (y - 1000); // path 1 - > path 2
    	    			break;
    	    		case 3:
    			        ret = Math.abs(x + y - 1024) + (y - 1000) - (Math.exp(z) - (Math.cos(z) - 0.95));  // path 1 - > path 3
    			        break;
    	    		case 4:
    			        ret = Math.abs(x + y - 1024) + (y - 1000) + (Math.exp(z) - (Math.cos(z) - 0.95));  // path 1 - > path 4    			        
    			        break;
    	    	}
    			break;
    		case 2:
    			switch (pathid2)
    	    	{
    	    		case 1:
    	    			ret = Math.abs(x + y - 1024) - (y - 1000); // path 1 - > path 2
    	    			break;
    	    		case 3:
    			        ret = (y - 1000) - (Math.exp(z) - (Math.cos(z) - 0.95));  // path 2 - > path 3
    			        break;
    	    		case 4:
    			        ret = (y - 1000) + (Math.exp(z) - (Math.cos(z) - 0.95));  // path 2 - > path 4    			        
    			        break;
    	    	}
    			break;
    		case 3:
    			switch (pathid2)
    	    	{
    	    		case 1:
    	    			ret = Math.abs(x + y - 1024) + (y - 1000) - (Math.exp(z) - (Math.cos(z) - 0.95));  // path 1 - > path 3
    	    			break;
    	    		case 2:
    	    			ret = (y - 1000) - (Math.exp(z) - (Math.cos(z) - 0.95));  // path 2 - > path 3
    			        break;
    	    		case 4:
    			        ret = (Math.exp(z) - (Math.cos(z) - 0.95));  // path 3 - > path 4    			        
    			        break;
    	    	}
    			break;
    		case 4:
    			switch (pathid2)
    	    	{
    	    		case 1:
    	    			ret = Math.abs(x + y - 1024) + (y - 1000) + (Math.exp(z) - (Math.cos(z) - 0.95));  // path 1 - > path 4
    	    			break;
    	    		case 2:
    	    			ret = (y - 1000) + (Math.exp(z) - (Math.cos(z) - 0.95));  // path 2 - > path 4
    			        break;
    	    		case 3:
    	    			ret = (Math.exp(z) - (Math.cos(z) - 0.95));  // path 3 - > path 4    			        
    			        break;
    	    	}
    			break;
    	}
    	return ret;
    }

    public double calculateDistTriangle(double a, double b, double c, String functionName)
    {
        double[] fitness;
        objectcall++;
        TargetFunctions targetFunction   = new TargetFunctions();        
        ArrayList<VertexTF> executedPath = new ArrayList<VertexTF>();
/*
        if (functionName.equals("tA2008_Triangle"))
            targetFunction.tA2008_Triangle(a, b, c, executedPath);
        else if (functionName.equals("QuadraticEquation2"))
            targetFunction.QuadraticEquation2(a, b, c, executedPath);
        else if (functionName.equals("triangleMansour2004"))
            targetFunction.triangleMansour2004(a, b, c, executedPath);
        else if (functionName.equals("tritypeBueno2002"))
            targetFunction.tritypeBueno2002(a, b, c, executedPath);
*/
        //targetFunction.example((int)a, (int)b, c, executedPath);

        fitness = getDistExecutedPath2TargetPaths(a, b, c, executedPath);
        if (fitness[0] == -2)
        {
            return fitness[0];
        }
        if (fitness[0] > -1)
        {
            // hit a feasible path
            System.out.println("Path " + (int)fitness[0] + ": a = " + a + " b = " + b + " c = " + c);
            //System.out.println("Path " + (int)fitness[0] + ": a = " + (int)a + " b = " + (int)b + " c = " + (int)c);
            System.out.println("Objective call: " + objectcall);
        }
        return fitness[1];
    }

    int pathnum = 0; // number of hit feasible paths

    public double calculateDistInsertionSort(double[] a, int size, String functionName)
    {
        double[] fitness;
        TargetFunctions targetFunction   = new TargetFunctions();        
        ArrayList<VertexTF> executedPath = new ArrayList<VertexTF>();
/*        
        if (functionName.equals("iA2008_InsertionSort"))
            targetFunction.InsertionSort(a, size, executedPath);
        else if (functionName.equals("GetMinMax"))
            targetFunction.getMinMax(a, size, executedPath);
        else if (functionName.equals("GetMinMaxTriangle"))
            targetFunction.mmTriangle(a, size, executedPath);
*/
        fitness = getDistExecutedPath2TargetPaths(a[0], a[1], a[2], executedPath);
        if (fitness[0] > -1)
        {
            // hit a feasible path
            pathnum++;
            System.out.print("[" + pathnum + "]");
            System.out.print("{");
            for (int i = 0; i < size; i++)
            {
                if (i < size-1)
                    System.out.format(" %1.3f, ", a[i]);
                else
                    System.out.format(" %1.3f ", a[i]);
            }
            System.out.print("}");
            System.out.println(" ===> pathID = " + (int)fitness[0]);
            System.out.println("Target paths = " + totalTargetPaths);
        }
        return fitness[1];
    }    

    public double calculateDistGreatestCommonDivisor(int a, int b)
    {
        double[] fitness = null;
        TargetFunctions targetFunction   = new TargetFunctions();        
        ArrayList<VertexTF> executedPath = new ArrayList<VertexTF>();
        
        int[] number = new int[2];
        number[0] = a;
        number[1] = b;
//        targetFunction.gcd(number, executedPath);

        //fitness = getDistExecutedPath2TargetPaths(a, b, c, executedPath);
        if (fitness[0] > -1)
        {
            // hit a feasible path
            pathnum++;
            System.out.print("[" + pathnum + "]");
            System.out.print("{" + a + ", " + b + "}");
            System.out.println(" ===> pathID = " + (int)fitness[0]);
            System.out.println("Target paths = " + totalTargetPaths);
        }
        return fitness[1];
    }    
    
    private double calculatePathDistTF(ArrayList<VertexTF> path1, ArrayList<VertexTF> path2)
    {
        int i = 0;
        int len1 = path1.size();
        int len2 = path2.size();
        int len = Math.min(len1, len2);
        for (i = 0; i < len; i++)
        {
            VertexTF vertex1 = path1.get(i);
            VertexTF vertex2 = path2.get(i);
            if ((vertex1.id == vertex2.id) && vertex1.decision.equals(vertex2.decision))
                continue;
            else                
                break;
        }
        //return ((double)(Math.max(len1, len2) - i)/(double)Math.max(len1, len2));
        return ((double)(Math.max(len1, len2) - i)/(double)len);
    }

    public boolean isGetAllFeasiblePathOK = false;
    /**
     * 
     * @param pathList
     * @return
     * @throws Exception
     */
    public ArrayList<DuongKiemThu> getAllFeasiblePath(final ArrayList<ArrayList<Integer>> pathList) throws Exception
    {
        isGetAllFeasiblePathOK = false;
        final ArrayList<DuongKiemThu> output = new ArrayList<>();
        final Map<String, String[]> dsk = getDskObject(staticVariable.AllPath.danhSachKe);
        final Map<String, String> nodeElementsMap = getNodeObject(staticVariable.AllPath.NodeElements);

        int i = 0;
        while (i < pathList.size())
        {
            // for (int i = 0; i < pathList.size(); i++) {

            ArrayList<Integer> testpathIndex = pathList.get(i);
            String testpathLuanLy = getTestpath(testpathIndex, dsk, nodeElementsMap);

            boolean maybeFeasible = true;
            String paticalTestpath = getParticalTestpath(testpathLuanLy);

            for (String listInfeasiblePaticalTestpath : staticVariable.infeasiblePaticalTestpath)
                if (listInfeasiblePaticalTestpath.contains(paticalTestpath))
                {
                    maybeFeasible = false;
                    System.out.println("find");
                    break;
                }

            if (maybeFeasible)
            {
                DuongKiemThu testpath = new DuongKiemThu(testpathLuanLy, testpathIndex, staticVariable.Paramater.variableOfTC, "#",
                        staticVariable.Paramater.Smt_Lib_path_lib, staticVariable.Paramater.Smt_Lib_path_file, "he-rang-buoc3.smt2");

                if (testpath.isFeasible())
                    output.add(testpath);
                else
                    staticVariable.infeasiblePaticalTestpath.add(paticalTestpath);
            }
            i++;
        }
        return output;
    }

    private String getParticalTestpath(String testpath)
    {
        String[] logicOperator = new String[]
        { ">", ">=", "<", "<=", "!", "&&", "||" };
        String[] vertexs = testpath.split("#");
        String output = "";
        for (int i = vertexs.length - 1; i > -0; i--)
        {
            boolean isDecision = false;

            for (String operator : logicOperator)
                if (vertexs[i].contains(operator))
                {
                    isDecision = true;
                    break;
                }

            for (int j = 0; j < i; j++)
                output += vertexs[j] + "#";
            output += vertexs[i];

        }
        return output;
    }

    /**
     * 
     * @param pathList
     * @return
     * @throws Exception
     */
    public ArrayList<DuongKiemThu> getAllFeasibleSubconditionPath(ArrayList<ArrayList<Integer>> pathList) throws Exception
    {
        ArrayList<DuongKiemThu> output = new ArrayList<>();
        Map<String, String[]> dsk = getDskObject(staticVariable.AllPathSubCondition.danhSachKe);
        Map<String, String> nodeElementsMap = getNodeObject(staticVariable.AllPathSubCondition.NodeElements);

        for (ArrayList<Integer> testpathIndex : pathList)
        {
            String testpathLuanLy = getTestpath(testpathIndex, dsk, nodeElementsMap);
            DuongKiemThu testpath = new DuongKiemThu(testpathLuanLy, testpathIndex, staticVariable.Paramater.variableOfTC, "#",
                    staticVariable.Paramater.Smt_Lib_path_lib, staticVariable.Paramater.Smt_Lib_path_file, "he-rang-buoc2.smt2");
            if (testpath.isFeasible())
            {
                output.add(testpath);
            }
            staticVariable.Test.pathList.add(testpath);// delete after testing
        }
        return output;
    }

    /**
     * 
     * @throws Exception
     */
    public void getTestpathOfStatement() throws Exception
    {
        staticVariable.Statement.pathList = (ArrayList<DuongKiemThu>) staticVariable.AllPath.pathList.clone();
        for (int i = staticVariable.Statement.pathList.size() - 1; i >= 0; i--)
        {
            DuongKiemThu testpath = staticVariable.Statement.pathList.get(i);
            if (!testpath.isFeasible() || canRemoveFromStatementTestpathList(testpath))
            {
                staticVariable.Statement.pathList.remove(testpath);
            }
        }
        //
        System.out.println("Testpath thoa man phu cau lenh:");
        for (DuongKiemThu testpath : staticVariable.Statement.pathList)
        {
            System.out.println(testpath.getTestpathIndex().toString());
            System.out.println(testpath.getTestpath().toString());
        }
    }

    private boolean canRemoveFromStatementTestpathList(DuongKiemThu testpath)
    {
        Set vertexList = new HashSet();
        for (DuongKiemThu item : staticVariable.Statement.pathList)
        {
            if (!item.equals(testpath))
            {
                for (Integer i : item.getTestpathIndex())
                {
                    vertexList.add(i);
                }
            }
        }
        int oldSize = vertexList.size();
        for (Integer i : testpath.getTestpathIndex())
        {
            vertexList.add(i);
        }
        int newSize = vertexList.size();
        return oldSize != newSize ? false : true;
    }

    /**
     * 
     * @throws Exception
     */
    public void getTestpathOfBranch() throws Exception
    {
        for (DuongKiemThu testpath : staticVariable.AllPath.pathList)
        {
            if (!canRemoveFromBranchTestpathList(testpath))
            {
                staticVariable.Branch.pathList.add(testpath);
            }
        }
        //
        System.out.println("Testpath thoa man phu nhanh:");
        for (DuongKiemThu testpath : staticVariable.Branch.pathList)
        {
            System.out.println(testpath.getTestpathIndex().toString());
            System.out.println(testpath.getTestpath().toString());
        }
    }

    private boolean canRemoveFromBranchTestpathList(DuongKiemThu testpath)
    {
        boolean isStafified = false;
        Set vertexList = new HashSet();
        for (DuongKiemThu item : staticVariable.Branch.pathList)
        {
            ArrayList<Integer> testpathIndex = item.getTestpathIndex();
            for (int i = 0; i < testpathIndex.size() - 1; i++)
            {
                vertexList.add(testpathIndex.get(i) + "->" + testpathIndex.get(i + 1));
            }
        }
        int oldSize = vertexList.size();
        for (int i = 0; i < testpath.getTestpathIndex().size() - 1; i++)
        {
            vertexList.add(testpath.getTestpathIndex().get(i) + "->" + testpath.getTestpathIndex().get(i + 1));
        }
        int newSize = vertexList.size();
        isStafified = oldSize != newSize ? false : true;
        return isStafified;
    }

    /**
     * 
     * @throws Exception
     */
    public void getTestpathOfSubCondition() throws Exception
    {
        for (DuongKiemThu testpath : staticVariable.AllPathSubCondition.pathList)
        {
            if (!canRemoveFromSubconditionTestpathList(testpath))
            {
                staticVariable.SubCondition.pathList.add(testpath);
            }
        }
        //
        System.out.println("Testpath thoa man phu dieu kien con:");
        for (DuongKiemThu testpath : staticVariable.SubCondition.pathList)
        {
            System.out.println(testpath.getTestpathIndex().toString());
            System.out.println(testpath.getTestpath().toString());
        }
    }

    private boolean canRemoveFromSubconditionTestpathList(DuongKiemThu testpath)
    {
        boolean isStafified = false;
        Set vertexList = new HashSet();
        for (DuongKiemThu item : staticVariable.SubCondition.pathList)
        {
            ArrayList<Integer> testpathIndex = item.getTestpathIndex();
            for (int i = 0; i < testpathIndex.size() - 1; i++)
            {
                vertexList.add(testpathIndex.get(i) + "->" + testpathIndex.get(i + 1));
            }
        }
        int oldSize = vertexList.size();
        for (int i = 0; i < testpath.getTestpathIndex().size() - 1; i++)
        {
            vertexList.add(testpath.getTestpathIndex().get(i) + "->" + testpath.getTestpathIndex().get(i + 1));
        }
        int newSize = vertexList.size();
        isStafified = oldSize != newSize ? false : true;
        return isStafified;
    }

    // ----------------------------------------------------------------------------------------------------
    /**
     * Get list of generated testpath for testing.
     * 
     * @param testpathLuanLy
     *            only contains a simple loop. <br/>
     *            Ex:(i = 0)#<b style="color:red;">(i &lt;n)</b> #(j =
     *            0)#!(j&gt;i)#(i++)#<b style="color:red;">!(i &lt;n)</b>
     *            #(return sum)
     * @param testpathIndex
     *            separate indexes by a space. <br/>
     *            Ex: 1 2 3 6 7 2 9
     * @return For a map item: <br/>
     *         {@link Integer}the number of loop <br/>
     *         {@link String}the respective testpath with the number of loop
     */
    public Map<Integer, String> testSimpleLoop(String testpathLuanLy, String testpathIndex) throws Exception
    {
        String[] indexList = testpathIndex.split(" ");
        for (int i = 0; i <= indexList.length - 2; i++)
        {
            for (int j = i + 1; j <= indexList.length - 1; j++)
            {
                if (indexList[i].equals(indexList[j]))
                {
                    // lay doan lap dau tien
                    int startLoop = i, endLoop = j - 1;
                    String loopString = "";
                    String[] luanlyList = testpathLuanLy.split("#");
                    for (int tmp = startLoop; tmp <= endLoop - 1; tmp++)
                    {
                        loopString += luanlyList[tmp] + "#";
                    }
                    loopString += luanlyList[endLoop];
                    // th1 : do-while lap 1 lan. ko xet den
                    StringBuffer loopString2 = new StringBuffer(loopString);
                    loopString2.insert(loopString.lastIndexOf("#") + 1, "!");
                    Map<Integer, String> outputList = new HashMap<>();
                    int[] numLoop = getNumberLoop(testpathLuanLy, startLoop);
                    // th2: do-while lap 2 lan
                    if (testpathLuanLy.contains(loopString + "#" + loopString2.toString()))
                    {
                        for (int w : numLoop)
                        {
                            if (w == 0)
                            {
                            }
                            else
                            {
                                outputList.put(w, testpathLuanLy.replace(loopString, Utils.copy(loopString, w - 1, "#")).replace("##", "#"));
                            }
                        }
                    }
                    else
                    {
                        // th3: while-do, for
                        for (int w : numLoop)
                        {
                            if (w == 0)
                            {
                                outputList.put(w, testpathLuanLy.replace(loopString + "#", Utils.copy(loopString, w, "#")));
                            }
                            else
                            {
                                outputList.put(w, testpathLuanLy.replace(loopString, Utils.copy(loopString, w, "#")));
                            }
                        }
                    }
                    return outputList;
                }
            }
        }
        return null;
    }

    /**
     * Replaces the iniValue of loop by putting iniValue after it and before
     * loop condition.<br/>
     * Copy inner loop to numLoop times
     * 
     * @param testpathLuanLy
     * @param testpathIndex
     * @param numLoop
     * <br/>
     *            Ex: <br/>
     *            index 1 is "(int i = 2)", index 11 is "(return 0)"<br/>
     *            testpathIndex="1 2 3 4 5 6 7 8 9 5 10 3 11"<br/>
     *            testpathLuanLy= "(int i = 2)#(int j,tmp)#(i <= n)#(j = i -
     *            1)#((j>=1)&&(a[j]>=a[j+1]))#(tmp = a[i])#(a[i] = a[j])#(a[j] =
     *            tmp)#(j--)#!((j>=1)&&(a[j]>=a[j+1]))#(i++)#!(i <= n)#(return
     *            0)" <br/>
     *            iniValue= "(i=4)" <br/>
     *            numLoop=2<br/>
     *            Output: (int i = 2)#(int j,tmp)# <b
     *            style="color:red;">(i=4)</b>#(i <= n)#(j = i - 1)#<b
     *            style="color:blue;">((j>=1)&&(a[j]>=a[j+1 ]))</b>#(tmp =
     *            a[i])#(a[i] = a[j])#(a[j] = tmp)#(j--)# <b
     *            style="color:blue;">((j>=1)&&(a[j]>=a[j+1]))</b>#(tmp =
     *            a[i])#(a[i] = a[j])#(a[j] = tmp)#(j--)# <b
     *            style="color:blue;">! ((j>=1)&&(a[j]>=a[j+1]))</b>
     *            #(i++)#(return 0)
     * @return @throws Exception
     */
    public Map<Integer, String> testInnerLoop(String testpathLuanLy, String testpathIndex, String loopVar) throws Exception
    {
        String[] indexList = testpathIndex.split(" ");
        int[] loopIndex = Utils.getLoopIndexForSimpleLoopAndNestedLoop(indexList);
        int startOuterLoop = loopIndex[0], endOuterLoop = loopIndex[1];

        String contentOfOuterLoopLuanLy = "", contentOfOuterLoopIndex = "";
        String[] luanlyList = testpathLuanLy.split("#");
        for (int tmp = startOuterLoop + 1; tmp <= endOuterLoop - 2; tmp++)
        {
            contentOfOuterLoopLuanLy += luanlyList[tmp] + "#";
            contentOfOuterLoopIndex += indexList[tmp] + " ";
        }
        contentOfOuterLoopLuanLy += luanlyList[endOuterLoop - 1];
        contentOfOuterLoopIndex += indexList[endOuterLoop - 1];

        Map<Integer, String> innerLoopList = testSimpleLoop(contentOfOuterLoopLuanLy, contentOfOuterLoopIndex);

        Map<Integer, String> outputList = new HashMap<>();
        String newItem = "(" + loopVar + "=" + Utils.SinhRandomSoNguyen(0, 9)// set
                // by
                // hand
                + ")";
        for (Integer numLoop : innerLoopList.keySet())
        {
            String newTestpath = testpathLuanLy.replace(contentOfOuterLoopLuanLy + "#" + luanlyList[endOuterLoop],
                    newItem + "#" + innerLoopList.get(numLoop));
            outputList.put(numLoop, newTestpath);
        }
        return outputList;
    }

    /**
     * Copy outer loop to numOfOuterLoop times, but the inner loop does not loop
     * >1 times (always is 1)
     * 
     * @param testpathLuanLy
     * @param testpathIndex
     * @param loopVar
     * @return <br/>
     *         Ex: <br/>
     *         testpathLuanLy= "(int i = 2)#(int j,tmp)#(i <= n)#(j = i -
     *         1)#((j>=1)&&(a[j]>=a[j+1]))#(tmp = a[i])#(a[i] = a[j])#(a[j] =
     *         tmp)#(j--)#!((j>=1)&&(a[j]>=a[j+1]))#(i++)#!(i <= n)#(return 0)" <br/>
     *         testpathIndex="1 2 3 4 5 6 7 8 9 5 10 3 11" <br/>
     *         loopVar=i <br/>
     *         Output:(int i = 2)#(int j,tmp)#<b style="color:red;">(i <= n)</b>
     *         #(j = i - 1)#((j>=1)&&(a[j]>=a[j+1]))#(tmp = a[i])#(a[i] =
     *         a[j])#(a[j] = tmp)#(j--)#!((j>=1)&&(a[j]>=a[j+1]))#(i++)# <b
     *         style="color:red;">(i <= n)</b>#(j = i -
     *         1)#((j>=1)&&(a[j]>=a[j+1]))#(tmp = a[i])#(a[i] = a[j])#(a[j] =
     *         tmp)#(j--)#!((j>=1)&&(a[j]>=a[j+1]))#(i++)# <b
     *         style="color:red;">!(i <= n)</b>#(return 0)
     */
    public Map<Integer, String> testOuterLoop(String testpathLuanLy, String testpathIndex, String loopVar) throws Exception
    {
        ArrayList<String> indexArrayList = Utils.convertToArrayList(testpathIndex.split(" "));
        ArrayList<String> luanlyArrayList = Utils.convertToArrayList(testpathLuanLy.split("#"));
        //
        int dem = 0;
        boolean exit = false;
        for (int i = 0; i < indexArrayList.size(); i++)
        {
            for (int j = indexArrayList.size() - 1; j > i; j--)
            {
                if (indexArrayList.get(i).equals(indexArrayList.get(j)))
                {
                    dem++;
                }
                if (dem == 2)
                {
                    indexArrayList.remove(j);
                    luanlyArrayList.remove(j);
                    exit = true;
                    break;
                }
            }
            if (exit)
            {
                break;
            }
        }
        //
        testpathIndex = testpathLuanLy = "";
        for (String item : indexArrayList)
        {
            testpathIndex += item + " ";
        }
        for (String item : luanlyArrayList)
        {
            testpathLuanLy += item + "#";
        }
        // System.out.println(testpathIndex);
        // System.out.println(testpathLuanLy);
        testpathLuanLy = testpathLuanLy.substring(0, testpathLuanLy.length() - 1);
        Map<Integer, String> outputList = testSimpleLoop(testpathLuanLy, testpathIndex);
        return outputList;
    }

    /**
     * Get number loop
     * 
     * @param testpath
     * @param posConditionLoop
     * @return a array of integer. Size the array is 4 (0,1,2,typical value) in
     *         case we dont predicate the number loops, wherea the size is 7
     *         (loop 0,1,2,typical value,n-1,n,n+1).<br/>
     *         in some case the size might be smaller than 4 or 7 because of not
     *         being enough the number loop
     * @throws Exception
     */
    public int[] getNumberLoop(String testpath, int posConditionLoop) throws Exception
    {
        String[] tpNode = testpath.split("#");
        String conditionLoop = tpNode[posConditionLoop];

        if (!conditionLoop.contains("&&") && !conditionLoop.contains("||"))
        {
            /*
             * is SINGLE CONDITION
             */
            Matcher m = Pattern.compile("(\\w+)\\s*[<>]\\s*(\\d+)").matcher(conditionLoop);
            if (m.find() && Utils.isInt(m.group(2)))
            {
                /*
                 * bound value is constant
                 */
                int can = Utils.toInt(m.group(2));
                // tim bien lap
                String iterationVar = m.group(1);
                // tim gia tri bien lap truoc khi vao vong lap
                String iniIterationVarStatement = tpNode[posConditionLoop - 1];
                System.out.println(iniIterationVarStatement);
                int iniIterationVar = 0;
                iniIterationVar = Utils.toInt(iniIterationVarStatement.substring(iniIterationVarStatement.indexOf("=") + 1,
                        iniIterationVarStatement.lastIndexOf(")")));
                // tim so lan lap
                int maxLoop = -1;
                for (int i = posConditionLoop + 1; i < testpath.split("#").length; i++)
                {
                    String nonSpaceItem = Utils.getNonSpaceString(tpNode[i]);
                    if (nonSpaceItem.indexOf("(" + iterationVar + "++)") == 0 || nonSpaceItem.indexOf("(++" + iterationVar + ")") == 0)
                    {
                        nonSpaceItem = iterationVar + "=" + iterationVar + "+1";
                    }
                    else if (nonSpaceItem.indexOf("(" + iterationVar + "--)") == 0 || nonSpaceItem.indexOf("(--" + iterationVar + ")") == 0)
                    {
                        nonSpaceItem = iterationVar + "=" + iterationVar + "-1";
                    }
                    String indexStr = iterationVar + "=" + iterationVar;
                    if (nonSpaceItem.indexOf(indexStr + "+") == 0)
                    {
                        String deltaStr = nonSpaceItem.replace(indexStr + "+", "");
                        if (Utils.isInt(deltaStr))
                        {
                            int delta = Utils.toInt(deltaStr);
                            maxLoop = (can - iniIterationVar) / delta;
                        }
                        break;
                    }
                    else if (nonSpaceItem.indexOf(indexStr + "-") == 0)
                    {
                        String deltaStr = nonSpaceItem.replace(indexStr + "-", "");
                        if (Utils.isInt(deltaStr))
                        {
                            int delta = Utils.toInt(deltaStr);
                            maxLoop = (-can + iniIterationVar) / delta;
                        }
                        break;
                    }
                    else
                    {
                        continue;
                    }
                }
                // tra ve bo lap can thiet
                if (maxLoop != -1)
                {
                    if (maxLoop == 1)
                    {
                        return new int[]
                        { 0, 1, 2 };
                    }
                    if (maxLoop == 2)
                    {
                        return new int[]
                        { 0, 1, 2, 3 };
                    }
                    if (maxLoop == 3)
                    {
                        return new int[]
                        { 0, 1, 2, 3, 4 };
                    }
                    if (maxLoop == 4)
                    {
                        return new int[]
                        { 0, 1, 2, 3, 4, 5 };
                    }
                    if (maxLoop == 5)
                    {
                        return new int[]
                        { 0, 1, 2, 3, 4, 5, 6 };
                    }
                    return new int[]
                    { 0, 1, 2, Utils.SinhRandomSoNguyen(3, maxLoop - 2), maxLoop - 1, maxLoop, maxLoop + 1 };
                }
            }
        }
        return new int[]
        { 0, 1, 2, Utils.SinhRandomSoNguyen(3, 10) };

    }

    class Node
    {

        protected String data;
        protected int idNode;
        protected int true_id;
        protected int false_id;

        protected Node(int id, String data, int true_id, int false_id)
        {
            this.idNode = id;
            this.data = data;
            this.true_id = true_id;
            this.false_id = false_id;
        }

        protected String getData()
        {

            return data;
        }

        @Override
        public String toString()
        {
            return "Node [data=" + data + ", idNode=" + idNode + ", true_id=" + true_id + ", false_id=" + false_id + "]";
        }
    }
}
