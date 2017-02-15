package C;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ConstraintsCreation.ParseTestpath;
import net.sourceforge.jeval.EvaluationException;

/**
 * 
 * @version 3
 */
public class getAllPaths
{
    /**
     * gdf
     */
    private ArrayList<Vertex> vertexList;
    private ArrayList<Vertex> myPath;
    private ArrayList<ArrayList<Vertex>> output;
    private int DEFAULT_DEPTH = staticVariable.Paramater.depth;

    private boolean isBusy = false;

    public getAllPaths(String maTranKe, String nodeElement) throws Exception
    {
        vertexList = getVertexList(maTranKe, nodeElement);
        myPath = new ArrayList<Vertex>();
        output = new ArrayList<ArrayList<Vertex>>();

        //traverse(vertexList.get(0), new ArrayList<Vertex>());
        TraverseCFG(vertexList.get(0)); // D.N.Thi
    }

    public String getOutputInString()
    {
        return output.toString();
    }

    public ArrayList<ArrayList<Vertex>> getOutput()
    {
        return output;
    }

    /**
     * 
     * @return
     */
    public ArrayList<ArrayList<Integer>> getPathList()
    {
        ArrayList<ArrayList<Integer>> pathList = new ArrayList<>();
        for (ArrayList<Vertex> item : output)
        {
            ArrayList<Integer> pathListItem = new ArrayList<Integer>();
            for (Vertex v : item)
                pathListItem.add(v.getId());
            pathList.add(pathListItem);
        }
        return pathList;
    }

    /**
     * 
     * @return
     */
    public ArrayList<ArrayList<Integer>> getFeasiblePathList()
    {
        ArrayList<ArrayList<Integer>> pathList = new ArrayList<>();
        for (ArrayList<Vertex> item : output)
        {
            ArrayList<Integer> pathListItem = new ArrayList<Integer>();
            for (Vertex v : item)
                pathListItem.add(v.getId());
            pathList.add(pathListItem);
        }
        return pathList;
    }

    public static void main(String[] args) throws Exception
    {
        String nodeElement = "0#Bat dau ham\n1#float e\n2#a==0\n3#return 0\n4#Ket thuc ham\n5#int x=0\n6#a=b-2\n7#(a==b)\n11#(c==d)\n12#(a==0)\n8#x=1\n9#e=1/x\n10#return e";
        String maTranKe = "0 1\n1 2 3\n2 1\n3 4\n4 5\n5 4 6\n6 7 8\n7 9 10\n9 -1\n10 -1\n8 11\n11 8 12\n12 -1";
        getAllPaths p = new getAllPaths(maTranKe, nodeElement);
        System.out.println(p.getPathList().toString());
    }

    private void traverse(Vertex v, ArrayList<Vertex> indexPath) throws Exception
    {
        if (v == null || v.id == -1)
        {
            output.add((ArrayList<Vertex>) myPath.clone());
        }
        else
            if (check(myPath, v.getId()))
            {
                myPath.add(v);
                if (v.getFalseVertexId() == v.getTrueVertexId())
                {
                    Vertex u = getVertex(v.getFalseVertexId());
                    traverse(u, myPath);
                }
                else
                {
                    // while (isBusy) Thread.sleep(10);
                    // ArrayList<String> falsePath = getTestpath(indexPath,
                    // FALSE_BRANCH);
                    // if (isFeasible(falsePath, getTestpathIndex(indexPath)))
                    traverse(getVertex(v.getFalseVertexId()), myPath);

                    // while (isBusy) Thread.sleep(10);
                    // ArrayList<String> truePath = getTestpath(indexPath,
                    // TRUE_BRANCH);
                    // if (isFeasible(truePath, getTestpathIndex(indexPath)))
                    traverse(getVertex(v.getTrueVertexId()), myPath);
                }
                myPath.remove(myPath.size() - 1);
            }
    }

    @SuppressWarnings("unchecked")
    private void TraverseCFG(Vertex v) throws Exception
    {
        if (v == null || v.id == -1)
        {
            output.add((ArrayList<Vertex>) myPath.clone());
        }
        else
            if (check(myPath, v.id))
            {
                myPath.add(v);
                Vertex u;
                if (v.getFalseVertexId() == v.getTrueVertexId())
                {
                    u = getVertex(v.getFalseVertexId());
                    TraverseCFG(u);
                }
                else
                {
                    u = getVertex(v.getFalseVertexId());
                    TraverseCFG(u);
                    u = getVertex(v.getTrueVertexId());
                    TraverseCFG(u);
                }
                myPath.remove(myPath.size() - 1);
            }
    }
    
    public static final int TRUE_BRANCH = 1;
    public static final int FALSE_BRANCH = 2;

    private boolean isFeasible(ArrayList<String> path, ArrayList<Integer> testpathIndex) throws Exception
    {
        // DuongKiemThu(ArrayList<String> testpath, ArrayList<Integer>
        // testpathIndex, String testcase,
        // String Smt_Lib_path_lib, String Smt_Lib_path_file, String
        // smtFileName)
        isBusy = true;

        DuongKiemThu testpath = new DuongKiemThu(path, 
                                                 testpathIndex, 
                                                 staticVariable.Paramater.variableOfTC, 
                                                 staticVariable.Paramater.Smt_Lib_path_lib, 
                                                 staticVariable.Paramater.Smt_Lib_path_file,
                                                "he-rang-buoc2.smt2");
        do
        {
            Thread.sleep(30);
        }
        while (testpath.solvingStatus != true);// dont finish

        isBusy = false;
        return testpath.isFeasible();
    }

    private ArrayList<Integer> getTestpathIndex(ArrayList<Vertex> indexPath)
    {
        ArrayList<Integer> output = new ArrayList<Integer>();
        for (Vertex v : indexPath)
            output.add(v.id);

        return output;
    }

    /**
     * 
     * @param indexPath
     *            the last vertex is a decision
     * @param nextBranch
     * @return
     */
    private ArrayList<String> getTestpath(ArrayList<Vertex> indexPath, int nextBranch)
    {
        ArrayList<String> output = new ArrayList<String>();

        for (int i = 0; i < indexPath.size() - 1; i++)
        {
            Vertex v = indexPath.get(i);

            if (v.falseVertexId == v.trueVertexId)
                output.add("(" + v.getStatement() + ")");
            else
            {
                Vertex vNext = indexPath.get(i);
                if (v.trueVertexId == vNext.id)
                    output.add("(" + v.getStatement() + ")");
                else
                    output.add("!(" + v.getStatement() + ")");
            }
        }

        Vertex finalDecision = indexPath.get(indexPath.size() - 1);
        if (nextBranch == TRUE_BRANCH)
            output.add("(" + finalDecision.getStatement() + ")");
        else
            output.add("!(" + finalDecision.getStatement() + ")");

        return output;
    }

    private Vertex getVertex(int id)
    {
        for (Vertex v : vertexList)
            if (v.getId() == id)
                return v;
        return null;
    }

    // For find min/max 
    private boolean check(ArrayList<Vertex> myPath, int id)
    {
        int count = 0;
        for (Vertex v : myPath)
            if (v.getId() == id) count++;
        
        return (count <= DEFAULT_DEPTH);
    }
    
    /**
     * 
     * @param maTranKe
     * @return
     */
    private ArrayList<Vertex> getVertexList(String maTranKe, String nodeElement)
    {
        Map<String, String> listElement = getNodeObject(nodeElement);

        ArrayList<Vertex> vertexList = new ArrayList<Vertex>();
        String[] vertexLine = maTranKe.split("\n");
        for (String line : vertexLine)
        {
            String[] item = line.split(" ");
            int id = Integer.parseInt(item[0]);
            if (id == -1)
                continue;
            int trueId = Integer.parseInt(item[1]);
            int falseId = item.length == 3 ? Integer.parseInt(item[2]) : trueId;
            vertexList.add(new Vertex(id, trueId, falseId, listElement.get(id + "")));
        }
        return vertexList;
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
}

class Vertex
{
    int id;
    String statement;
    int trueVertexId;
    int falseVertexId;

    public String getStatement()
    {
        return statement;
    }

    public void setStatement(String statement)
    {
        this.statement = statement;
    }

    Vertex(int data, int trueVertexId, int falseVertexId, String statement)
    {
        this.id = data;
        this.trueVertexId = trueVertexId;
        this.falseVertexId = falseVertexId;
        this.statement = statement; 
    }

    @Override
    public boolean equals(Object arg0)
    {
        Vertex v = (Vertex) arg0;
        if (v.getId() == id)
            return true;
        return false;
    }

    int getId()
    {
        return id;
    }

    int getTrueVertexId()
    {
        return trueVertexId;
    }

    int getFalseVertexId()
    {
        return falseVertexId;
    }

    @Override
    public String toString()
    {
        return "[" + id + ", " + statement + "]";
    }
}

class VertexTF
{
    public int id;
    //public String statement;
    public String decision;
    
    VertexTF(int idtmp, String decisiontmp)
    {
    	this.id = idtmp;
    	this.decision = decisiontmp;
    }

    @Override
    public String toString()
    {
        return "[" + id + ", " + decision + "]";
    }
}