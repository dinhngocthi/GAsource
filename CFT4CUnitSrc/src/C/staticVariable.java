package C;

import java.util.ArrayList;

public class staticVariable {
	
	public static ArrayList<String> infeasiblePaticalTestpath = new ArrayList<String>();
	// for test

	public static class Test {

		public static ArrayList<DuongKiemThu> pathList = new ArrayList<DuongKiemThu>();

		public static void reset() {
			pathList = new ArrayList<DuongKiemThu>();
		}
	}

	public static class LoopTestpath {
		public static ArrayList<DuongKiemThu> pathList = new ArrayList<DuongKiemThu>();

		public static void reset() {
			pathList = new ArrayList<DuongKiemThu>();
		}
	}
	//

	public static class Statement {

		public static ArrayList<DuongKiemThu> pathList = new ArrayList<DuongKiemThu>();
		public static String danhSachKe = new String();
		public static String NodeElements = new String();
		public static String NodeRelations = new String();
		public static String dsk = new String();

		public static void reset() {
			pathList = new ArrayList<DuongKiemThu>();
			danhSachKe = "";
			NodeElements = "";
			NodeRelations = "";
			dsk = "";
		}
	}

	public static class Branch {

		public static ArrayList<DuongKiemThu> pathList = new ArrayList<DuongKiemThu>();
		public static String danhSachKe = new String();
		public static String NodeElements = new String();
		public static String NodeRelations = new String();
		public static String dsk = new String();

		public static void reset() {
			pathList = new ArrayList<DuongKiemThu>();
			danhSachKe = "";
			NodeElements = "";
			NodeRelations = "";
			dsk = "";
		}
	}

	public static class SubCondition {

		public static ArrayList<DuongKiemThu> pathList = new ArrayList<DuongKiemThu>();

		public static String danhSachKe = new String();
		public static String NodeElements = new String();
		public static String NodeRelations = new String();
		public static String dsk = new String();

		public static void reset() {
			pathList = new ArrayList<DuongKiemThu>();
			danhSachKe = "";
			NodeElements = "";
			NodeRelations = "";
			dsk = "";
		}
	}

	public static class PossiblePath {

		public static ArrayList<DuongKiemThu> pathList = new ArrayList<DuongKiemThu>();
		public static String danhSachKe = new String();
		public static String NodeElements = new String();
		public static String NodeRelations = new String();
		public static String dsk = new String();

		public static void reset() {
			pathList = new ArrayList<DuongKiemThu>();
			danhSachKe = "";
			NodeElements = "";
			NodeRelations = "";
			dsk = "";
		}
	}

	public static class AllPath {

		public static ArrayList<DuongKiemThu> pathList = new ArrayList<DuongKiemThu>();
		public static String danhSachKe = new String();
		public static String NodeElements = new String();
		public static String NodeRelations = new String();
		public static String dsk = new String();

		public static void reset() {
			pathList = new ArrayList<DuongKiemThu>();
			danhSachKe = "";
			NodeElements = "";
			NodeRelations = "";
			dsk = "";
		}
	}

	public static class PossiblePathSubCondition {

		public static ArrayList<DuongKiemThu> pathList = new ArrayList<DuongKiemThu>();
		public static String danhSachKe = new String();
		public static String NodeElements = new String();
		public static String NodeRelations = new String();
		public static String dsk = new String();

		public static void reset() {
			pathList = new ArrayList<DuongKiemThu>();
			danhSachKe = "";
			NodeElements = "";
			NodeRelations = "";
			dsk = "";
		}
	}

	public static class AllPathSubCondition {

		public static ArrayList<DuongKiemThu> pathList = new ArrayList<DuongKiemThu>();
		public static String danhSachKe = new String();
		public static String NodeElements = new String();
		public static String NodeRelations = new String();
		public static String dsk = new String();

		public static void reset() {
			pathList = new ArrayList<DuongKiemThu>();
			danhSachKe = "";
			NodeElements = "";
			NodeRelations = "";
			dsk = "";
		}
	}

	public static class Paramater {

		public static int defaultRowOfArray = 8;
		public static int defaultColumnOfArray = 4;
		public static int CC = -1;
		public static String variableOfTC = "";
		public static String Smt_Lib_path_lib = "", Smt_Lib_path_file = "";
		public static String source = "";
		public static int canTren = 10;// default
		public static int canDuoi = -10;// default
		public static int maxLoop = 500;// default
		public static int depth = 1;// default
		// public static String Z3PATH = "C:\\z3\\bin\\z3";

		public static void reset() {
			// do something else
		}
	}

	public static class Loop {

		public static final int SIMPLE_LOOP = 0;
		public static final int NESTED_LOOP = 1;
		public static final int CONCATENATED_LOOP = 2;
		public static final int NOT_DEFINED_LOOP = -1;
	}

	public static class Constant {

		public static final String VO_NGHIEM = "Luôn luôn vô nghiệm";
		public static final String CHUA_TIM_THAY_NGHIEM = "Chưa tìm thấy nghiệm";
	}

	public static void reset() {
		Statement.reset();
		Branch.reset();
		SubCondition.reset();
		Paramater.reset();
		AllPath.reset();
		LoopTestpath.reset();
		PossiblePathSubCondition.reset();
		PossiblePath.reset();
		infeasiblePaticalTestpath = new ArrayList<String>();
	}
}
