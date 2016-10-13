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
	 */
	protected ArrayList<String> runOuterSimpleLoop() {
		int[] diemLapVongLapTrong = layPhamViVongLapTrong(indexList);
		ArrayList<String> outerSimpleLoop = XoaDiemLapThoatKhoiVongLapTrong(vertexList, diemLapVongLapTrong[1]);
		return outerSimpleLoop;
	}

	/**
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
	 * 
	 * @param vertexList
	 * @return
	 */
	protected ArrayList<String> xoaDiemLapThoatKhoiVongLapNgoai(ArrayList<String> vertexList, int endOuterLoopRange) {
		vertexList.remove(endOuterLoopRange + 1);// +1 do vertexList ﾄ妥｣ thﾃｪm cﾃ｢u
													// l盻㌻h gﾃ｡n cho bi蘯ｿn 盻� trﾆｰ盻嫩
													// ﾄ妥ｳ
		innerSimpleLoopIndex.remove(endOuterLoopRange);
		return vertexList;
	}

	/**
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
	 * L蘯･y kho蘯｣ng l蘯ｷp vﾃｲng l蘯ｷp trong
	 * 
	 * @param indexList
	 *            danh sﾃ｡ch ﾄ黛ｻ穎h nﾃｪu th盻ｩ t盻ｱ th盻ｱc hi盻㌻ cﾃ｡c cﾃ｢u l盻㌻h
	 * @return int[] [0]: ﾄ訴盻ノ ﾄ訴 vﾃ�o vﾃｲng l蘯ｷp trong. [1]: ﾄ訴盻ノ thoﾃ｡t kh盻淑 vﾃｲng
	 *         l蘯ｷp trong
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
