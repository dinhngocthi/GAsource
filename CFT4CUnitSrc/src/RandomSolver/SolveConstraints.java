package RandomSolver;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import C.Utils;
import ConstraintsCreation.myJeval;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

/**
 * Giải hệ ràng buộc sử dụng kĩ thuật sinh ngẫu nhiên. Các biến trong hệ ràng
 * buộc kiểu số nguyên, số thực, biến mảng 1 chiều hoặc hai chiều. Chỉ số biến
 * mảng là một biểu thức không chứa biến mảng khác. Ví dụ: a[3] hay a[n+2] là
 * hợp lệ; a[a[2]] hay a[a[2]+1] đều không hợp lệ
 * 
 * @author anhanh
 * 
 */
public class SolveConstraints {
	private ArrayList<String> constraints = new ArrayList<String>();
	private ArrayList<Bien> danhSachBien = new ArrayList<Bien>();
	private ArrayList<Bien> danhSachThamSo = new ArrayList<Bien>();
	private String solution = "";
	private int numLoop = 0, canDuoi = 0, canTren = 0;

	public static void main(String[] args) {
		String constraints = "!(m<0)";
		String delimiter = "#";
		String danhSachBien = "int m";
		int numLoop = 2000;
		int canDuoi = -1;
		int canTren = 9;
		SolveConstraints solver = new SolveConstraints(constraints, delimiter, danhSachBien, numLoop, canDuoi, canTren);
		System.out.println(solver.getSolution());
	}

	public SolveConstraints(String constraints, String delimiter, String danhSachThamSo, int numLoop, int canDuoi,
			int canTren) {
		this.constraints = chuanHoaHeRangbuoc(constraints, delimiter);
		this.danhSachThamSo = chuanHoaDanhSachThamSo(danhSachThamSo);
		this.numLoop = numLoop;
		this.canDuoi = canDuoi;
		this.canTren = canTren;
		run();
	}

	public SolveConstraints(ArrayList<String> constraints, String danhSachThamSo, int numLoop, int canDuoi,
			int canTren) {
		this.constraints = constraints;
		this.danhSachThamSo = chuanHoaDanhSachThamSo(danhSachThamSo);
		this.numLoop = numLoop;
		this.canDuoi = canDuoi;
		this.canTren = canTren;
		run();
	}

	private void run() {
		boolean isSolution = false;
		for (int i = 0; i < numLoop; i++) {
			this.danhSachBien = layDanhSachBien(this.danhSachThamSo);
			khoiTaoGiaTriDanhSachBien(danhSachBien, canDuoi, canTren);
			isSolution = danhGiaHeRangBuoc(constraints, danhSachBien);
			if (!isSolution)
				continue;
			else
				break;
		}
		solution = isSolution ? danhSachBien.toString() : NOT_FOUND_SOLUTION;
	}

	/**
	 * Lấy danh sách biến từ danh sách tham số
	 * 
	 * @param danhSachThamSo
	 * @return
	 */
	private ArrayList<Bien> layDanhSachBien(ArrayList<Bien> danhSachThamSo) {
		ArrayList<Bien> danhSachBien = new ArrayList<>();
		for (Bien var : danhSachThamSo)
			if (var.getType() == Bien.DOUBLE || var.getType() == Bien.INT)
				danhSachBien.add(var);
		return danhSachBien;
	}

	/**
	 * Đánh giá tập biểu thức logic
	 * 
	 * @param constraints
	 *            Tập biểu thức logic có chứa biến
	 * @param danhSachBien
	 *            Giá trị các biến trong biểu thức logic
	 * @return
	 */
	private boolean danhGiaHeRangBuoc(ArrayList<String> constraints, ArrayList<Bien> danhSachBien) {
		boolean isSolution = false;
		for (String bieuThucLogic : constraints) {
			isSolution = danhGiaBieuThucLogic(bieuThucLogic, danhSachBien);
			if (isSolution)
				continue;
			else
				break;
		}
		return isSolution;
	}

	/**
	 * Khởi tạo giá trị tất cả các biến
	 * 
	 * @param danhSachBien
	 */
	private void khoiTaoGiaTriDanhSachBien(ArrayList<Bien> danhSachBien, int canDuoi, int canTren) {
		for (Bien var : danhSachBien)
			var.generateNewValue(canDuoi, canTren);
	}

	/**
	 * Đánh giá biểu thức logic đúng hay sai
	 * 
	 * @param bieuThucLogic
	 *            Biểu thức logic chứa biến
	 * @param danhSachBien
	 *            Lưu giá trị các biến
	 * @return
	 */
	private boolean danhGiaBieuThucLogic(String bieuThucLogic, ArrayList<Bien> danhSachBien) {
		do {
			bieuThucLogic = thayTheBienVoiGiaTri(bieuThucLogic, danhSachBien);
			bieuThucLogic = rutGonChiSoMang(bieuThucLogic);
			bieuThucLogic = rutGonSoThuc(bieuThucLogic);
			ArrayList<String> danhSachBienMang = layDanhSachBienMang(bieuThucLogic);
			danhSachBien = capNhatDanhSachBien(danhSachBienMang, danhSachBien);
			danhSachBien = khoiTaoGiaTriBienNull(danhSachBien, canDuoi, canTren);
		} while (bieuThucLogic.contains(ARRAY_SYMBOL));
		boolean giaTri = danhGiaBieuThucLogic(bieuThucLogic);
		return giaTri;
	}

	/**
	 * Khởi tạo giá trị cho những biến chưa có giá trị
	 * 
	 * @param danhSachBien
	 * @param canDuoi
	 * @param canTren
	 * @return
	 */
	private ArrayList<Bien> khoiTaoGiaTriBienNull(ArrayList<Bien> danhSachBien, int canDuoi, int canTren) {
		for (Bien var : danhSachBien)
			if (var.getValue() == null || var.getValue().equals(""))
				var.generateNewValue(canDuoi, canTren);
		return danhSachBien;
	}

	/**
	 * Thêm biến mảng vào danh sách biến
	 * 
	 * @param arrayVarList
	 */
	private ArrayList<Bien> capNhatDanhSachBien(ArrayList<String> danhSachBienMang, ArrayList<Bien> danhSachBien) {
		for (String bienMang : danhSachBienMang) {
			int typeVar = layKieuBienMang(bienMang, danhSachThamSo);
			Bien b = new Bien(bienMang, typeVar);
			danhSachBien.add(b);
		}
		return danhSachBien;
	}

	private int layKieuBienMang(String arrayVar, ArrayList<Bien> danhSachThamSo) {
		int kieuBien = 0;
		String tenBien = Utils.getNameOfArrayItem(arrayVar);
		for (Bien var : danhSachThamSo)
			if (var.getName().equals(tenBien)) {
				kieuBien = var.getType();
				break;
			}
		return kieuBien;
	}

	/**
	 * Đánh giá tính đúng sai của biểu thức logic
	 * 
	 * @param bieuThucLogic
	 *            Biểu thức logic không chứa biến
	 * @return
	 */
	private boolean danhGiaBieuThucLogic(String bieuThucLogic) {
		boolean giaTriLuanLy = false;
		try {
			String giaTri = new Evaluator().evaluate(bieuThucLogic.replace(" ", ""));
			giaTriLuanLy = giaTri.equals("1.0") ? true : false;
		} catch (EvaluationException e) {
			e.printStackTrace();
		}
		return giaTriLuanLy;
	}

	/**
	 * Thay thế các biến trong biểu thức logic với giá trị cụ thể
	 * 
	 * @param bieuThucLogic
	 * @param danhSachBien
	 * @return
	 */
	private String thayTheBienVoiGiaTri(String bieuThucLogic, ArrayList<Bien> danhSachBien) {
		String output = " " + bieuThucLogic + " ";
		for (Bien var : danhSachBien)
			output = output.replaceAll("\\b" + Utils.toRegex(var.getName()), var.getValue());
		return output;
	}

	/**
	 * Lấy danh sách biến mảng có trong biểu thức logic
	 * 
	 * @return
	 */
	private ArrayList<String> layDanhSachBienMang(String expression) {
		ArrayList<String> arrayItemList = new ArrayList<>();
		// do something here
		Matcher m = Pattern.compile("\\w+(\\[([^\\]])+\\])+").matcher(expression);
		while (m.find()) {
			if (!arrayItemList.contains(m.group(0)))
				arrayItemList.add(m.group(0));
		}
		return arrayItemList;
	}

	/**
	 * Tính toán giá trị chỉ số mảng nếu là một biểu thức. Ex: a[1+2] => a[3]
	 * <br/>
	 * a[1/2] => a[0] a[1+a[3+1]] =>a[1+a[4]]
	 * 
	 * @param expression
	 * @return
	 */
	private String rutGonChiSoMang(String bieuThucLogic) {
		String output = bieuThucLogic;
		output = output + "@";// to simplify regex
		Matcher m = Pattern.compile("\\[([^\\]\\[]+)\\]").matcher(output);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "[" + new myJeval().evaluate(m.group(1)) + "]");
		}
		m.appendTail(sb);
		return sb.deleteCharAt(sb.length() - 1).toString();
	}

	/**
	 * Remove unneccessary ".0" String. Ex: 1.0 => 1<br/>
	 * 2.01+3.0=>2.01+3
	 * 
	 * @param expression
	 * @return
	 */
	private String rutGonSoThuc(String expression) {
		expression = expression + "@";// to simplify regex
		Matcher m = Pattern.compile("(\\d)\\.0([^\\d])").matcher(expression);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, m.group(1) + m.group(2));
		}
		m.appendTail(sb);
		return sb.deleteCharAt(sb.length() - 1).toString();
	}

	/**
	 * Phân tich hệ ràng buộc dạng xâu về dạng danh sách liên kết
	 * 
	 * @param constraints
	 *            hệ ràng buộc dạng xâu
	 * @param delimiter
	 *            kí tự phân tách các biểu thức loogic trong hệ ràng buộc đầu
	 *            vào
	 * @return
	 */
	private ArrayList<String> chuanHoaHeRangbuoc(String constraints, String delimiter) {
		ArrayList<String> output = new ArrayList<String>();
		if (constraints.contains("#"))
			for (String bieuThucLogic : constraints.split("#"))
				output.add(bieuThucLogic);
		else
			output.add(constraints);
		return output;
	}

	/**
	 * Chuẩn hóa danh sách tham số truyền vào về dạng danh sách liên kết
	 * 
	 * @param danhSachThamSo
	 * @return
	 */
	private ArrayList<Bien> chuanHoaDanhSachThamSo(String danhSachThamSo) {
		ArrayList<Bien> variableList = new ArrayList<>();
		String[] danhSachBienList = danhSachThamSo.split(",");
		for (String khaiBao : danhSachBienList) {
			String type = khaiBao.split(" ")[0];
			if (khaiBao.contains("]")) {
				String name = khaiBao.substring(khaiBao.indexOf(" ") + 1, khaiBao.indexOf("["));
				if (khaiBao.contains("][")) // nếu là mảng hai chiều
					switch (type) {
					case "int":
						variableList.add(new Bien(name, Bien.INT_ARRAY_TWO_DIMENSION));
						break;
					case "double":
						variableList.add(new Bien(name, Bien.DOUBLE_ARRAY_TWO_DIMENSION));
						break;
					}
				else
					// nếu là mảng 1 chiều
					switch (type) {
					case "int":
						variableList.add(new Bien(name, Bien.INT_ARRAY_ONE_DIMENSION));
						break;
					case "double":
						variableList.add(new Bien(name, Bien.DOUBLE_ARRAY_ONE_DIMENSION));
						break;
					}
			} else { // nếu không phải khai báo mảng
				String name = khaiBao.split(" ")[1];
				switch (type) {
				case "int":
					variableList.add(new Bien(name, Bien.INT));
					break;
				case "double":
					variableList.add(new Bien(name, Bien.DOUBLE));
					break;
				}
			}
		}
		return variableList;
	}

	public String getSolution() {
		return solution;
	}

	private static final String ARRAY_SYMBOL = "[";
	public static final String NOT_FOUND_SOLUTION = "-";
}
