package C;

import java.util.ArrayList;
import java.util.regex.Pattern;

import C.ChuongTrinhChinh.Node;

public class ParseEqualCondition 
{
	private String filename;
	
    private int[] pathListID;
    private getAllPaths geterTest;
    private double[][] disMatrix; 
    private int totalTargetPaths; // current numbers of target paths
    private ArrayList<ArrayList<VertexTF>> targetPaths;
    private ArrayList<String> equalCondList;

	ParseEqualCondition(String filename)
	{
		this.filename = filename;
	}
	
	public static void main(String[] args) throws Exception 
	{
		// TODO Auto-generated method stub
        String classPath = ParseEqualCondition.class.getProtectionDomain().getCodeSource().getLocation().getPath();

		//ParseEqualCondition ParseEqualCond = new ParseEqualCondition(classPath.replace("bin/", "src/sample/tA2008_Triangle.java"));
		ParseEqualCondition ParseEqualCond = new ParseEqualCondition(classPath.replace("bin/", "src/sample/QuadraticEquation2.java"));		
		//ParseEqualCondition ParseEqualCond = new ParseEqualCondition(classPath.replace("bin/", "src/sample/example.java"));
		ParseEqualCond.parsing();
		ParseEqualCond.initPathListID();
	}
	
	private void parsing() throws Exception
	{
        staticVariable.reset();
        normalizeFunctionSource ns = new normalizeFunctionSource(Utils.getContentFile(filename));
        staticVariable.Paramater.source = ns.getNormalizeCode();
        staticVariable.Paramater.variableOfTC = staticVariable.Paramater.source.substring(staticVariable.Paramater.source.indexOf("(") + 1,
                                                                                          staticVariable.Paramater.source.indexOf(")"));
        getNode12();
        getNode3();
        getDanhSachKe123();
	}

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
            Node node = new Node(Integer.parseInt(item[0]), item[1], Integer.parseInt(item[2].replace("true=", "")), Integer.parseInt(item[3].replace("false=", "")));            
            mnNode.add(node);
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

    private void initPathListID() throws Exception
    {
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
