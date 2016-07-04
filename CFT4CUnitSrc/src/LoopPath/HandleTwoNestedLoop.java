package LoopPath;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import C.Utils;

public class HandleTwoNestedLoop {
	protected ArrayList<Integer> indexList = new ArrayList<Integer>();
	protected ArrayList<String> vertexList = new ArrayList<String>();
	protected ArrayList<String> innerSimpleLoop = new ArrayList<String>();
	protected ArrayList<String> outerSimpleLoop = new ArrayList<String>();
	protected ArrayList<Integer> innerSimpleLoopIndex = new ArrayList<Integer>();
	protected ArrayList<Integer> outerSimpleLoopIndex = new ArrayList<Integer>();

	public static void main(String[] args) {
		String testpath = "(int i,j,k)#(i=0)#(i<da)#(j=0)#(j<cb)#(t[i][j]=0)#(k=0)#!(k<ca)#(j++)#!(j<cb)#(i++)#!(i<da)";
		String index = "0 1 2 3 4 5 6 7 8 11 5 12 3 13";
		int giaTriKhoiTaoBienLapNgoai = -3;
		HandleTwoNestedLoop handler = new HandleTwoNestedLoop(testpath, index);
		System.out.println(handler.getInnerSimpleLoopInString());
		System.out.println(handler.getInnerSimpleLoopIndexInString());
		System.out.println(handler.getOuterSimpleLoopInString());
		System.out.println(handler.getOuterSimpleLoopIndexInString());
	}

	public HandleTwoNestedLoop(String testpath, String pathIndex) {
		System.out.println("-=-=-=-=-=-=-=-=-=-=");
		System.out.println(testpath);
		System.out.println(pathIndex);
		indexList = convertToindexList(pathIndex);
		vertexList = convertToVertexList(testpath);
		indexList = normalizeVertexList(indexList, vertexList);
		innerSimpleLoopIndex = (ArrayList<Integer>) indexList.clone();
		outerSimpleLoopIndex = (ArrayList<Integer>) indexList.clone();
		innerSimpleLoop = runInnerSimpleLoop();
		outerSimpleLoop = runOuterSimpleLoop();
	}

	/**
	 * Xóa bỏ những phần tử thừa trong danh sách index các đỉnh. Nguyên nhân:
	 * danh sách chỉ số đỉnh truyền vào có thể thừa đỉnh đầu tiên và đỉnh cuối
	 * cùng
	 * 
	 * @param IndexList
	 * @param vertexList
	 * @return
	 */
	private ArrayList<Integer> normalizeVertexList(ArrayList<Integer> IndexList, ArrayList<String> vertexList) {
		if (vertexList.size() == IndexList.size() - 2) {
			IndexList.remove(0);
			IndexList.remove(IndexList.size() - 1);
		}
		return IndexList;
	}

	/**
	 * Phá vỡ cấu trúc vòng lặp ngoài
	 */
	protected ArrayList<String> runInnerSimpleLoopForTest(int giaTriKhoiTaoBienLapNgoai) {
		int[] diemLapVongLapNgoai = layPhamViVongLapNgoai(indexList);
		String cauLenhDiVaoVongLapNgoai = vertexList.get(diemLapVongLapNgoai[0]);
		String bienLap = layBienLap(cauLenhDiVaoVongLapNgoai, diemLapVongLapNgoai, vertexList);
		ArrayList<String> innerSimpleLoop = chenCauLenhKhoiTaoBienLapVongLapNgoai(vertexList, diemLapVongLapNgoai[0],
				"(" + bienLap + "=" + giaTriKhoiTaoBienLapNgoai + ")");
		innerSimpleLoop = xoaDiemLapThoatKhoiVongLapNgoai(innerSimpleLoop, diemLapVongLapNgoai[1]);
		return innerSimpleLoop;
	}

	/**
	 * Phá vỡ cấu trúc vòng lặp ngoài
	 * 
	 * @return [0]: luận lý [1]:index
	 */
	protected ArrayList<String> runInnerSimpleLoop() {
		int[] diemLapVongLapNgoai = layPhamViVongLapNgoai(indexList);
		String cauLenhDiVaoVongLapNgoai = vertexList.get(diemLapVongLapNgoai[0]);
		String bienLap = layBienLap(cauLenhDiVaoVongLapNgoai, diemLapVongLapNgoai, vertexList);
		int giaTriKhoiTaoBienLapNgoai = khoiTaoGiaTriBienLap(bienLap, diemLapVongLapNgoai, vertexList);
		ArrayList<String> innerSimpleLoop = chenCauLenhKhoiTaoBienLapVongLapNgoai(vertexList, diemLapVongLapNgoai[0],
				"(" + bienLap + "=" + giaTriKhoiTaoBienLapNgoai + ")");
		innerSimpleLoop = xoaDiemLapThoatKhoiVongLapNgoai(innerSimpleLoop, diemLapVongLapNgoai[1]);
		return innerSimpleLoop;
	}

	protected ArrayList<Integer> getInnerLoopIndexTestpath(ArrayList<Integer> indexList) {
		ArrayList<Integer> output = new ArrayList<Integer>();

		return output;
	}

	/**
	 * Phá vỡ cấu trúc vòng lặp trong
	 */
	protected ArrayList<String> runOuterSimpleLoop() {
		int[] diemLapVongLapTrong = layPhamViVongLapTrong(indexList);
		ArrayList<String> outerSimpleLoop = XoaDiemLapThoatKhoiVongLapTrong(vertexList, diemLapVongLapTrong[1]);
		return outerSimpleLoop;
	}

	/**
	 * Thay thế điểm quyết định đi vào vòng lặp ngoài với câu lệnh khởi tạo
	 * 
	 * @param vertexList
	 * @param indexList
	 * @param bienLap
	 * @param giaTriKhoiTaoBienLapNgoai
	 * @return
	 */
	protected ArrayList<String> chenCauLenhKhoiTaoBienLapVongLapNgoai(ArrayList<String> vertexList, int startOuterLoop,
			String cauLenhMoi) {
		ArrayList<String> output = (ArrayList<String>) vertexList.clone();
		output.add(startOuterLoop, cauLenhMoi);
		// nen tach code duoi day thanh mot ham
		innerSimpleLoopIndex.add(startOuterLoop, 1000);
		return output;
	}

	/**
	 * Xóa điểm quyết định thoát khỏi vòng lặp ngoài
	 * 
	 * @param vertexList
	 * @return
	 */
	protected ArrayList<String> xoaDiemLapThoatKhoiVongLapNgoai(ArrayList<String> vertexList, int endOuterLoopRange) {
		vertexList.remove(endOuterLoopRange + 1);// +1 do vertexList đã thêm câu
													// lệnh gán cho biến ở trước
													// đó
		innerSimpleLoopIndex.remove(endOuterLoopRange);
		return vertexList;
	}

	/**
	 * Xóa điểm quyết định thoát khỏi vòng lặp trong
	 * 
	 * @param vertexList
	 * @return
	 */
	protected ArrayList<String> XoaDiemLapThoatKhoiVongLapTrong(ArrayList<String> vertexList, int endInnerLoopRange) {
		vertexList.remove(endInnerLoopRange);
		outerSimpleLoopIndex.remove(endInnerLoopRange);
		return vertexList;
	}

	/**
	 * Tạo cho biến lặp một giá trị khởi tạo hợp lệ
	 * 
	 * @param vertexList
	 * @param indexList
	 * @return
	 */
	protected int khoiTaoGiaTriBienLap(String bienLap, int[] diemLapVongLapNgoai, ArrayList<String> vertexList) {
		int giaTriKhoiTao = KHONG_XAC_DINH;
		int xuHuongBienLap = layXuHuongBienLap(bienLap, diemLapVongLapNgoai[0], diemLapVongLapNgoai[1], vertexList);
		int giaTriKhoiDauBienLap = layGiaTriKhoiDauBienLap(bienLap, diemLapVongLapNgoai[0], vertexList);
		if (xuHuongBienLap == TANG_LEN) {
			giaTriKhoiTao = Utils.SinhRandomSoNguyen(giaTriKhoiDauBienLap + 1, giaTriKhoiDauBienLap + 10);
		} else {
			giaTriKhoiTao = Utils.SinhRandomSoNguyen(giaTriKhoiDauBienLap - 10, giaTriKhoiDauBienLap - 1);
		}
		return giaTriKhoiTao;
	}

	/**
	 * Lấy giá trị khởi đầu biến lặp
	 * 
	 * @param bienLap
	 * @param vertexList
	 * @return
	 */
	protected int layGiaTriKhoiDauBienLap(String bienLap, int startOuterLoopRange, ArrayList<String> vertexList) {
		int giaTriKhoiDau = KHONG_XAC_DINH;
		for (int i = startOuterLoopRange; i > 0; i--)
			if (vertexList.get(i).contains(bienLap + "=")) {
				String statement = vertexList.get(i);
				statement = statement.replace(bienLap + "=", "");
				String tmp = statement.substring(1, statement.length() - 1);
				giaTriKhoiDau = Utils.toInt(tmp);
				break;
			}
		return giaTriKhoiDau;
	}

	/**
	 * Xác định biến lặp sẽ tăng hay giảm
	 * 
	 * @param bienlap
	 * @param vertexList
	 * @return
	 */
	protected int layXuHuongBienLap(String bienlap, int startOuterLoopRange, int endOuterLoopRange,
			ArrayList<String> vertexList) {
		int xuHuong = KHONG_XAC_DINH;
		for (int i = startOuterLoopRange + 1; i < endOuterLoopRange; i++)
			if (vertexList.get(i).contains(bienlap + "++"))
				xuHuong = TANG_LEN;
			else if (vertexList.get(i).contains(bienlap + "--"))
				xuHuong = GIAM_XUONG;
		return xuHuong;
	}

	/**
	 * Lấy biến lặp bằng cách phân tích câu lệnh
	 * 
	 * @param cauLenhDieuKien
	 *            câu lệnh cần phân tích
	 * @param outerLoopRange
	 *            phạm vi đoạn lặp ngoài
	 * @param vertexList
	 *            danh sách đỉnh
	 * @return
	 */
	protected String layBienLap(String cauLenhDieuKien, int[] outerLoopRange, ArrayList<String> vertexList) {
		String bienLap = XAU_KHONG_XAC_DINH;
		ArrayList<String> danhSachBien = layDanhSachBien(cauLenhDieuKien);
		for (String bien : danhSachBien)
			if (coSuThayDoiGiaTri(bien, outerLoopRange, vertexList)) {
				bienLap = bien;
				break;
			}
		return bienLap;
	}

	/**
	 * Lấy khoảng lặp vòng lặp trong
	 * 
	 * @param indexList
	 *            danh sách đỉnh nêu thứ tự thực hiện các câu lệnh
	 * @return int[] [0]: điểm đi vào vòng lặp trong. [1]: điểm thoát khỏi vòng
	 *         lặp trong
	 */
	protected int[] layPhamViVongLapTrong(ArrayList<Integer> indexList) {
		int[] innerLoopRange = new int[2];
		int[] outerLoopRange = layPhamViVongLapNgoai(indexList);
		for (int i = outerLoopRange[0] + 1; i < outerLoopRange[1] - 1; i++)
			for (int j = i + 1; j < outerLoopRange[1]; j++)
				if (indexList.get(j) == indexList.get(i)) {
					innerLoopRange[0] = i;
					innerLoopRange[1] = j;
					return innerLoopRange;
				}
		return null;
	}

	/**
	 * Lấy khoảng lặp vòng lặp ngoài
	 * 
	 * @param indexList
	 *            danh sách đỉnh nêu thứ tự thực hiện các câu lệnh
	 * @return int[] [0]: điểm đi vào vòng lặp ngoài. [1]: điểm thoát khỏi vòng
	 *         lặp ngoài
	 */
	protected int[] layPhamViVongLapNgoai(ArrayList<Integer> indexList) {
		int[] outerLoopRange = new int[2];
		for (int i = 0; i < indexList.size() - 1; i++)
			for (int j = i + 1; j < indexList.size(); j++)
				if (indexList.get(j) == indexList.get(i)) {
					outerLoopRange[0] = i;
					outerLoopRange[1] = j;
					return outerLoopRange;
				}
		return null;
	}

	/**
	 * Kiếm tra xem có sự thay đổi giá trị biến
	 * 
	 * @param bien
	 *            tên biến
	 * @param outerLoopRange
	 *            Phạm vi đoạn lặp ngoài
	 * @param vertexList
	 *            Danh sách đỉnh
	 * @return
	 */
	protected boolean coSuThayDoiGiaTri(String bien, int[] outerLoopRange, ArrayList<String> vertexList) {
		boolean isChanged = false;
		for (int i = outerLoopRange[0] + 1; i < outerLoopRange[1]; i++)
			if (coThayDoi(vertexList.get(i), bien)) {
				isChanged = true;
				break;
			}
		return isChanged;
	}

	protected boolean coThayDoi(String src, String bien) {
		if (src.contains(bien + "+=") || src.contains(bien + "-=") || src.contains(bien + "*=")
				|| src.contains(bien + "/=") || src.contains(bien + "=") || src.contains(bien + "++")
				|| src.contains(bien + "--"))
			return true;
		return false;
	}

	/**
	 * Lấy danh sách biến có trong câu lệnh
	 * 
	 * @param statement
	 * @return
	 */
	protected ArrayList<String> layDanhSachBien(String statement) {
		ArrayList<String> output = new ArrayList<String>();
		Pattern p = Pattern.compile("\\w+");
		Matcher m = p.matcher(statement);
		while (m.find()) {
			output.add(m.group(0));
		}
		return output;
	}

	/**
	 * Biến đổi sang dạng danh sách
	 * 
	 * @param indexPath
	 * @return
	 */
	protected ArrayList<Integer> convertToindexList(String indexPath) {
		ArrayList<Integer> output = new ArrayList<Integer>();
		for (String index : indexPath.split(" "))
			output.add(Integer.parseInt(index));
		return output;
	}

	/**
	 * Biến đổi sang dạng danh sách
	 * 
	 * @param indexPath
	 * @return
	 */
	protected ArrayList<String> convertToVertexList(String path) {
		ArrayList<String> output = new ArrayList<String>();
		for (String vertex : path.split("#"))
			output.add(vertex);
		return output;
	}

	public ArrayList<String> getInnerSimpleLoop() {
		return innerSimpleLoop;
	}

	public ArrayList<String> getOuterSimpleLoop() {
		return outerSimpleLoop;
	}

	public String getInnerSimpleLoopInString() {
		String output = "";
		for (String element : innerSimpleLoop)
			output += element + "#";
		output = output.substring(0, output.length() - 1);
		return output;
	}

	public String getInnerSimpleLoopIndexInString() {
		String output = "";
		for (Integer element : innerSimpleLoopIndex)
			output += element + " ";
		output = output.substring(0, output.length() - 1);
		return output;
	}

	public String getOuterSimpleLoopIndexInString() {
		String output = "";
		for (Integer element : outerSimpleLoopIndex)
			output += element + " ";
		output = output.substring(0, output.length() - 1);
		return output;
	}

	public String getOuterSimpleLoopInString() {
		String output = "";
		for (String element : outerSimpleLoop)
			output += element + "#";
		output = output.substring(0, output.length() - 1);
		return output;
	}

	/**
	 * only for testing
	 * 
	 * @param testpath
	 * @param pathIndex
	 * @param giaTriKhoiTaoBienLapNgoai
	 */
	public HandleTwoNestedLoop(String testpath, String pathIndex, int giaTriKhoiTaoBienLapNgoai) {

		indexList = convertToindexList(pathIndex);
		vertexList = convertToVertexList(testpath);
		indexList = normalizeVertexList(indexList, vertexList);
		innerSimpleLoopIndex = (ArrayList<Integer>) indexList.clone();
		outerSimpleLoopIndex = (ArrayList<Integer>) indexList.clone();
		innerSimpleLoop = runInnerSimpleLoopForTest(giaTriKhoiTaoBienLapNgoai);
		outerSimpleLoop = runOuterSimpleLoop();
	}

	protected static final String XAU_KHONG_XAC_DINH = "";
	protected static final int KHONG_XAC_DINH = -1;
	protected static final int TANG_LEN = 0;
	protected static final int GIAM_XUONG = 1;
}
