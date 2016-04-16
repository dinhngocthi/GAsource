package C;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ConstraintsCreation.ParseTestpath;
import net.sourceforge.jeval.EvaluationException;


/**
 * Vét cạn đường đi của CFG. Độ sâu mặc định bằng 1 (tức mỗi vòng lặp chỉ đi qua
 * nhiều nhất 1 lần)
 * 
 * @author nguyenducanh
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

        //traverse(vertexList.get(0), new ArrayList<Vertex>());// D.N.Thi
        
        TraverseCFG(vertexList.get(0));
        //TraverseCFG();
/*
        // D.N.Thi create CFG by manual
        for (int i = 0; i < 8; i++)
        {
            ArrayList<Vertex> path = new ArrayList<Vertex>();
            output.add(path);
        }

        Vertex[] vertex = new Vertex[15];
        vertex[0] = new Vertex(0, 1, 1, "Bat dau ham");
        vertex[1] = new Vertex(1, 2, 2, "int i,j");
        vertex[2] = new Vertex(2, 3, 3, "i=0");
        vertex[3] = new Vertex(3, 4, 14, "i<size-1");
        vertex[4] = new Vertex(4, 5, 5, "int min=i");
        vertex[5] = new Vertex(5, 6, 6, "j=i+1");
        vertex[6] = new Vertex(6, 7, 10, "j<size");
        vertex[7] = new Vertex(7, 8, 9, "a[j]<a[min]");
        vertex[8] = new Vertex(8, 9, 9, "min=j");
        vertex[9] = new Vertex(9, 6, 6, "j++");
        vertex[10] = new Vertex(10, 11, 11, "int tem=a[i]");
        vertex[11] = new Vertex(11, 12, 12, "a[i]=a[min]");
        vertex[12] = new Vertex(12, 13, 13, "a[min]=tem]");
        vertex[13] = new Vertex(13, 3, 3, "i++");
        vertex[14] = new Vertex(14, -1, -1, "Ket thuc ham");
        
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                output.get(i).add(vertex[j]);
            }
        }
        // path 1
        output.get(0).add(vertex[9]);
        output.get(0).add(vertex[6]);
        output.get(0).add(vertex[7]);
        output.get(0).add(vertex[9]);
        output.get(0).add(vertex[6]);
        output.get(0).add(vertex[10]);
        output.get(0).add(vertex[11]);
        output.get(0).add(vertex[12]);
        output.get(0).add(vertex[13]);
        output.get(0).add(vertex[3]);
        output.get(0).add(vertex[4]);
        output.get(0).add(vertex[5]);
        output.get(0).add(vertex[6]);
        output.get(0).add(vertex[7]);
        output.get(0).add(vertex[9]);
        output.get(0).add(vertex[6]);
        output.get(0).add(vertex[10]);
        output.get(0).add(vertex[11]);
        output.get(0).add(vertex[12]);
        output.get(0).add(vertex[13]);
        output.get(0).add(vertex[3]);
        output.get(0).add(vertex[14]);
        
        //path 2
        output.get(1).add(vertex[9]);
        output.get(1).add(vertex[6]);
        output.get(1).add(vertex[7]);
        output.get(1).add(vertex[9]);
        output.get(1).add(vertex[6]);
        output.get(1).add(vertex[10]);
        output.get(1).add(vertex[11]);
        output.get(1).add(vertex[12]);
        output.get(1).add(vertex[13]);
        output.get(1).add(vertex[3]);
        output.get(1).add(vertex[4]);
        output.get(1).add(vertex[5]);
        output.get(1).add(vertex[6]);
        output.get(1).add(vertex[7]);
        output.get(1).add(vertex[8]); //★
        output.get(1).add(vertex[9]);
        output.get(1).add(vertex[6]);
        output.get(1).add(vertex[10]);
        output.get(1).add(vertex[11]);
        output.get(1).add(vertex[12]);
        output.get(1).add(vertex[13]);
        output.get(1).add(vertex[3]);
        output.get(1).add(vertex[14]);
        
        //path 3
        output.get(2).add(vertex[9]);
        output.get(2).add(vertex[6]);
        output.get(2).add(vertex[7]);
        output.get(2).add(vertex[8]); //★
        output.get(2).add(vertex[9]);
        output.get(2).add(vertex[6]);
        output.get(2).add(vertex[10]);
        output.get(2).add(vertex[11]);
        output.get(2).add(vertex[12]);
        output.get(2).add(vertex[13]);
        output.get(2).add(vertex[3]);
        output.get(2).add(vertex[4]);
        output.get(2).add(vertex[5]);
        output.get(2).add(vertex[6]);
        output.get(2).add(vertex[7]);
        output.get(2).add(vertex[9]);
        output.get(2).add(vertex[6]);
        output.get(2).add(vertex[10]);
        output.get(2).add(vertex[11]);
        output.get(2).add(vertex[12]);
        output.get(2).add(vertex[13]);
        output.get(2).add(vertex[3]);
        output.get(2).add(vertex[14]);
        
        // path 4
        output.get(3).add(vertex[9]);
        output.get(3).add(vertex[6]);
        output.get(3).add(vertex[7]);
        output.get(3).add(vertex[8]); //★
        output.get(3).add(vertex[9]);
        output.get(3).add(vertex[6]);
        output.get(3).add(vertex[10]);
        output.get(3).add(vertex[11]);
        output.get(3).add(vertex[12]);
        output.get(3).add(vertex[13]);
        output.get(3).add(vertex[3]);
        output.get(3).add(vertex[4]);
        output.get(3).add(vertex[5]);
        output.get(3).add(vertex[6]);
        output.get(3).add(vertex[7]);
        output.get(3).add(vertex[8]); //★
        output.get(3).add(vertex[9]);
        output.get(3).add(vertex[6]);
        output.get(3).add(vertex[10]);
        output.get(3).add(vertex[11]);
        output.get(3).add(vertex[12]);
        output.get(3).add(vertex[13]);
        output.get(3).add(vertex[3]);
        output.get(3).add(vertex[14]);
        
        // path 5
        output.get(4).add(vertex[8]); //★
        output.get(4).add(vertex[9]);
        output.get(4).add(vertex[6]);
        output.get(4).add(vertex[7]);
        output.get(4).add(vertex[9]);
        output.get(4).add(vertex[6]);
        output.get(4).add(vertex[10]);
        output.get(4).add(vertex[11]);
        output.get(4).add(vertex[12]);
        output.get(4).add(vertex[13]);
        output.get(4).add(vertex[3]);
        output.get(4).add(vertex[4]);
        output.get(4).add(vertex[5]);
        output.get(4).add(vertex[6]);
        output.get(4).add(vertex[7]);
        output.get(4).add(vertex[9]);
        output.get(4).add(vertex[6]);
        output.get(4).add(vertex[10]);
        output.get(4).add(vertex[11]);
        output.get(4).add(vertex[12]);
        output.get(4).add(vertex[13]);
        output.get(4).add(vertex[3]);
        output.get(4).add(vertex[14]);
        
        //path 6
        output.get(5).add(vertex[8]); //★
        output.get(5).add(vertex[9]);
        output.get(5).add(vertex[6]);
        output.get(5).add(vertex[7]);
        output.get(5).add(vertex[9]);
        output.get(5).add(vertex[6]);
        output.get(5).add(vertex[10]);
        output.get(5).add(vertex[11]);
        output.get(5).add(vertex[12]);
        output.get(5).add(vertex[13]);
        output.get(5).add(vertex[3]);
        output.get(5).add(vertex[4]);
        output.get(5).add(vertex[5]);
        output.get(5).add(vertex[6]);
        output.get(5).add(vertex[7]);
        output.get(5).add(vertex[8]); //★
        output.get(5).add(vertex[9]);
        output.get(5).add(vertex[6]);
        output.get(5).add(vertex[10]);
        output.get(5).add(vertex[11]);
        output.get(5).add(vertex[12]);
        output.get(5).add(vertex[13]);
        output.get(5).add(vertex[3]);
        output.get(5).add(vertex[14]);
        
        //path 7
        output.get(6).add(vertex[8]); //★
        output.get(6).add(vertex[9]);
        output.get(6).add(vertex[6]);
        output.get(6).add(vertex[7]);
        output.get(6).add(vertex[8]); //★
        output.get(6).add(vertex[9]);
        output.get(6).add(vertex[6]);
        output.get(6).add(vertex[10]);
        output.get(6).add(vertex[11]);
        output.get(6).add(vertex[12]);
        output.get(6).add(vertex[13]);
        output.get(6).add(vertex[3]);
        output.get(6).add(vertex[4]);
        output.get(6).add(vertex[5]);
        output.get(6).add(vertex[6]);
        output.get(6).add(vertex[7]);
        output.get(6).add(vertex[9]);
        output.get(6).add(vertex[6]);
        output.get(6).add(vertex[10]);
        output.get(6).add(vertex[11]);
        output.get(6).add(vertex[12]);
        output.get(6).add(vertex[13]);
        output.get(6).add(vertex[3]);
        output.get(6).add(vertex[14]);
        
        // path 8
        output.get(7).add(vertex[8]); //★
        output.get(7).add(vertex[9]);
        output.get(7).add(vertex[6]);
        output.get(7).add(vertex[7]);
        output.get(7).add(vertex[8]); //★
        output.get(7).add(vertex[9]);
        output.get(7).add(vertex[6]);
        output.get(7).add(vertex[10]);
        output.get(7).add(vertex[11]);
        output.get(7).add(vertex[12]);
        output.get(7).add(vertex[13]);
        output.get(7).add(vertex[3]);
        output.get(7).add(vertex[4]);
        output.get(7).add(vertex[5]);
        output.get(7).add(vertex[6]);
        output.get(7).add(vertex[7]);
        output.get(7).add(vertex[8]); //★
        output.get(7).add(vertex[9]);
        output.get(7).add(vertex[6]);
        output.get(7).add(vertex[10]);
        output.get(7).add(vertex[11]);
        output.get(7).add(vertex[12]);
        output.get(7).add(vertex[13]);
        output.get(7).add(vertex[3]);
        output.get(7).add(vertex[14]);
*/        
        // D.N.Thi
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
     * Lấy danh sách các đường kiểm thử thực thi được và không thực thi được
     * dạng số
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
     * Lấy danh sách các đường kiểm thử thực thi được dạng số
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
    
    public int getExecutionPathSelectionSort(double[] a, int size)
    {
        int ret = -1;
        int i = 0; // path ID
        double[] aTmp = new double[size];
        System.arraycopy(a, 0, aTmp, 0, 3);
        
        for (ArrayList<Vertex> path : output)
        {
            int j = 0; // vertex ID             
            int I = 0;
            int J = 0;
            int MIN = 0;
            System.arraycopy(aTmp, 0, a, 0, 3);

            for (Vertex vertex : path)
            {
                j++;
                String stm = vertex.getStatement();
                if (vertex.falseVertexId == vertex.trueVertexId)
                {
                    if (stm.equals("j=i+1"))
                    {
                        J = I + 1;
                    }
                    if (stm.equals("i++"))
                    {
                        I++;
                    }
                    if (stm.equals("j++"))
                    {
                        J++;
                    }
                    if (stm.equals("int min=i"))
                    {
                        MIN = I;
                    }
                    if (stm.equals("min=j"))
                    {
                        MIN = J;
                    }
                    if (stm.equals("int tem=a[i]"))
                    {
                    	double tmp = a[I];
                        a[I] = a[MIN];
                        a[MIN] = tmp;
                    }
                    continue;                    
                }
                else
                {                    
                    ParseTestpath parseTestpath = new ParseTestpath();
                    boolean b = false;
                    try
                    {
                        b = parseTestpath.evaluateExpressionSelectionSort(stm, a, size, I, J, MIN);
                    }
                    catch (EvaluationException EE)
                    {
                    }
                    Vertex vertexTmp = path.get(j); // get the next vertex in this path
                    if (b)
                    {                        
                        if (vertex.getTrueVertexId() == vertexTmp.getId())
                            continue;
                        else
                            break;
                    }
                    else
                    {
                        if (vertex.getFalseVertexId() == vertexTmp.getId())
                            continue;
                        else
                            break;
                    }
                }
            }
            if (path.size() == j)
            {
                ret = i;
                break;
            }
            else
            {
                i++;
            }
        }

        return ret;
    }

    public int getExecutionPathTriangle(double A, double B, double C)
    {
        int ret = -1;
        int i = 0;
        //System.out.println("getExecutionPath start");
        for (ArrayList<Vertex> path : output)
        {
            int j = 0;
            for (Vertex vertex : path)
            {
                j++;
                if (vertex.falseVertexId == vertex.trueVertexId)
                {
                    continue;
                }
                else
                {
                    // Todo: lam sao cho nay check duoc input thoa man nua la ngon
                    String temp = vertex.getStatement();
                    ParseTestpath parseTestpath = new ParseTestpath();
                    boolean b = false;
                    try
                    {
                        b = parseTestpath.evaluateExpressionTriangle(temp, A, B, C);
                    }
                    catch (EvaluationException EE)
                    {
                    }
                    Vertex vertexTmp = path.get(j); // get the next vertex in this path
                    if (b)
                    {                        
                        if (vertex.getTrueVertexId() == vertexTmp.getId())
                            continue;
                        else
                            break;
                    }
                    else
                    {
                        if (vertex.getFalseVertexId() == vertexTmp.getId())
                            continue;
                        else
                            break;
                    }
                }
            }
            if (path.size() == j)
            {
                ret = i;
                break;
            }
            else
            {
                i++;
            }
        }
        //System.out.println("A = " + A + " B = " + B + " C = " + C + " ⇒　path " + ret);
        //System.out.println("getExecutionPath end");
        return ret;
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
            if (check(myPath, v.getId()))
            //if (loop <= DEFAULT_DEPTH)
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
    
    private int sumloop2 = 0;
    private boolean check(ArrayList<Vertex> myPath, int id)
    {
        int loop1 = 0;
        int loop2 = 0;
        int[] ixloop1 = new int[DEFAULT_DEPTH];
        int[] ixloop2 = new int[DEFAULT_DEPTH * DEFAULT_DEPTH];
        for (Vertex v : myPath)                        
        {
            //if (v.getId() == id)
            if (v.getId() == 3)
            {
                ixloop1[loop1] = myPath.indexOf(v);
                loop1++;                
            }

            if (v.getId() == 6)
            {
                ixloop2[loop2] = myPath.indexOf(v);
                loop2++;
            }
        }
        
        int loop2Limit = 0;
        if (loop1 == DEFAULT_DEPTH)
        {
            sumloop2++;
        }
        for (int i = 0; i < loop1; i++)
        {
            loop2Limit += DEFAULT_DEPTH - i;
        }
        
        if ((loop1 <= DEFAULT_DEPTH) && (loop2 <= loop2Limit))
        //if (loop1 <= DEFAULT_DEPTH)
            return true;
        else        
            return false;
        /*
        if (id == 13)
            loop++;
            */
        //return (loop <= DEFAULT_DEPTH);
        //System.out.println(myPath);
        //return (loop < 1);
    }

    /**
     * Chuyển thông tin các đỉnh trong ma trận kề sang dạng ArrayList
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
     * Xây dựng đối lượng lưu thông tin đỉnh
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
    public String statement;
    public String decision;
    
    @Override
    public String toString()
    {
        if (decision == null)
        {
            return "[" + id + ", " + statement + "]";
        }
        else
        {
            return "[" + id + ", " + statement + ", " + decision + "]";
        }
    }
}