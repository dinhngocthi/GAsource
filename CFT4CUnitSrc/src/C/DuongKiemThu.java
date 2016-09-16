package C;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import ConstraintsCreation.ParseTestpath;
import RandomSolver.SolveConstraints;
import SMTSolver.CreateSufficientSmtLib;
import SMTSolver.RunZ3OnCMD;
import SMTSolver.parseZ3Solution;

public class DuongKiemThu {

	private ArrayList<String> constraintList = new ArrayList<String>();
	private String testcase = "";
	private String testcaseInZ3 = "", testcaseInRandom = "";
	private String realOutput = "";
	private ArrayList<String> testpath = new ArrayList<>();
	private ArrayList<Integer> testpathIndex = new ArrayList<>();
	private ArrayList<String> analysisPath = new ArrayList<>();
	private int solver;
	private boolean isFeasible;
	private String smtFile = "";
	private String Smt_Lib_path_lib = "", Smt_Lib_path_file = "";
	private String smtFileName = "";

	public boolean solvingStatus = false;

	public DuongKiemThu(ArrayList<String> testpath, ArrayList<Integer> testpathIndex, String testcase,
			String Smt_Lib_path_lib, String Smt_Lib_path_file, String smtFileName) throws Exception {
		this.testcase = testcase;
		this.testpath = testpath;
		this.testpathIndex = testpathIndex;
		this.smtFileName = smtFileName;
		this.Smt_Lib_path_file = Smt_Lib_path_file;
		this.Smt_Lib_path_lib = Smt_Lib_path_lib;
		// System.out.println(getTestpathInString());
		solvingStatus = false;
		findSolutionbyZ3();
		solvingStatus = true;
		// System.out.println("--OK");
	}

	// public void setsmtFileName(String path) {
	// smtFileName = path;
	// }

	public static void main(String[] args) throws Exception {
		String testpath = "(int i,j)#(i=0)#(i=9)#(i<size-1)#(int min=i)#(j=i+1)#(j<size)#!(a[j]<a[min])#(j++)#!(j<size)#(int tem=a[i])#(a[i]=a[min])#(a[min]=tem)#(i++)";
		DuongKiemThu d = new DuongKiemThu(testpath, new ArrayList<Integer>(), "int a[],int size", "#",
				"D:/PhD/SymbolicExecution/tool/SPF/z3/bin/Z3", "C:/CFT4CUnit/", "sdf.smt2");
		System.out.println("He rang buoc:" + d.getConstraintList().toString());
		System.out.println("Solution:" + d.getTestcaseInZ3());
	}

	/**
	 * 
	 * @param testpath
	 * @param delimiter
	 * @param testcase
	 * @throws Exception
	 */
	public DuongKiemThu(String testpath, ArrayList<Integer> testpathIndex, String testcase, String delimiter,
			String Smt_Lib_path_lib, String Smt_Lib_path_file, String smtFileName) throws Exception {
		for (String item : testpath.split(delimiter)) {
			this.testpath.add(item);
		}

		this.testcase = testcase;
		this.testpathIndex = testpathIndex;
		this.Smt_Lib_path_file = Smt_Lib_path_file;
		this.Smt_Lib_path_lib = Smt_Lib_path_lib;
		this.smtFileName = smtFileName;
		// System.out.println(testpath);
		findSolutionbyZ3();
		// System.out.println("----OK");
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private void findSolutionbyRandom() throws Exception {
		ParseTestpath parser = new ParseTestpath(testpath, testcase);
		String constraints = parser.getHPTList().toLowerCase();
		SolveConstraints mySolver = new SolveConstraints(constraints, "#", testcase, 15000, -10, 10);
		testcaseInRandom = mySolver.getSolution();
		if (testcaseInRandom == null || testcaseInRandom.equals("-")) {
			isFeasible = false;
			testcaseInRandom = "unsat";
		} else {
			isFeasible = true;
		}
		solver = RANDOM;
		realOutput = "chua tinh";
		analysisPath = parser.getAnalysisProcess();
		constraintList = parser.getHPTArrayList();
		testcase = testcaseInRandom;
	}

	public void writeToFileSmt2(String path, String content) throws InterruptedException {
		File file = new File(path);
		// if file doesnt exists, then create it
		if (file.exists()) {
			file.delete();
		}
		while (true){
			try {
				file.createNewFile();
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(content);
				bw.close();
				break;
			} catch (IOException e) {
				Thread.sleep(10);
			}
		}
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private void findSolutionbyZ3() throws Exception {
		ParseTestpath parser = new ParseTestpath(testpath, testcase);
		CreateSufficientSmtLib i = new CreateSufficientSmtLib(testcase, parser.getHPTList());

		writeToFileSmt2(Smt_Lib_path_file + smtFileName, i.getOutput());
		RunZ3OnCMD r = new RunZ3OnCMD(Smt_Lib_path_lib, Smt_Lib_path_file + smtFileName);
		// update variables
		testcaseInZ3 = r.getOutput();
		testcase = new parseZ3Solution(testcaseInZ3).getSolution();
		if (testcaseInZ3.contains("unsat") || testcaseInZ3.contains("unsupported") || testcaseInZ3.contains("error"))
			isFeasible = false;
		else
			isFeasible = true;
		solver = Z3;
		realOutput = "chua tinh";
		analysisPath = parser.getAnalysisProcess();
		constraintList = parser.getHPTArrayList();
		smtFile = i.getOutput();
	}

	public boolean isFeasible() {
		return isFeasible;
	}

	public ArrayList<String> getConstraintList() {
		return constraintList;
	}

	public String getRealOutput() {
		return realOutput;
	}

	public String getSmtFile() {
		return smtFile;
	}

	public String getTestcase() {
		return testcase;
	}

	public String getTestcaseInZ3() {
		return testcaseInZ3;
	}

	public String getTestcaseInRandom() {
		return testcaseInRandom;
	}

	public ArrayList<String> getTestpath() {
		return testpath;
	}

	public String getTestpathInString() {
		String output = "";
		for (String item : testpath) {
			output += item + "#";
		}
		return output.substring(0, output.lastIndexOf("#"));
	}

	public ArrayList<Integer> getTestpathIndex() {
		return testpathIndex;
	}

	public String getTestpathIndexInString() {
		String output = "";
		for (Integer item : testpathIndex) {
			output += item + " ";
		}
		return output;
	}

	public ArrayList<String> getAnalysisPath() {
		return analysisPath;
	}

	public int getSolver() {
		return solver;
	}

	@Override
	public String toString() {
		return testpath.toString() + "\n";
	}

	public static final int Z3 = 1;
	public static final int RANDOM = 2;
}
