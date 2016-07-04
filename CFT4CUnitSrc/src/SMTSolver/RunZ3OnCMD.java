package SMTSolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Chạy file smt-lib trên cmd sử dụng SMT-Solver Z3
 * 
 * @author anhanh
 * 
 */
public class RunZ3OnCMD 
{
	private String smtSolver;
	private String Smt_Lib_path_file;
	private String result;

	public RunZ3OnCMD(String smtSolver, String Smt_Lib_path_file) throws IOException, InterruptedException 
	{
		//this.smtSolver = smtSolver;
	    if (smtSolver.contains("Z3"))
	        this.smtSolver = smtSolver + " -smt2 ";
	    else if (smtSolver.contains("yices"))
	        this.smtSolver = smtSolver + " ";

		this.Smt_Lib_path_file = Smt_Lib_path_file;
		result = "";
		run();
	}

	private void run() throws IOException, InterruptedException 
	{
		//Process p = Runtime.getRuntime().exec(smtSolver + " -smt2 " + Smt_Lib_path_file);
		Process p = Runtime.getRuntime().exec(smtSolver + Smt_Lib_path_file);
		while (p.isAlive()) 
		{
			Thread.sleep(10);
		}
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while ((line = in.readLine()) != null) 
		{
			result += line + "\n";
		}
	}

	public String getOutput() 
	{
		return result;
	}

	public static void main(String[] args) throws IOException, InterruptedException 
	{
		RunZ3OnCMD r = new RunZ3OnCMD("D:/PhD/SymbolicExecution/tool/SPF/z3/bin/Z3", "C:/CFT4CUnit/he-rang-buoc2.smt2");
		System.out.println(r.getOutput());
	}
}
