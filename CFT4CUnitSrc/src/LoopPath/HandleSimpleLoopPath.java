package LoopPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import C.Utils;

/**
 * 
 *
 */
public class HandleSimpleLoopPath {
	private Map<Integer, String> outputList = new HashMap();
	private ArrayList<Integer> IndexList = new ArrayList<Integer>();
	private ArrayList<String> vertexList = new ArrayList<String>();

	public static void main(String[] args) {
		String loopPath = "(int i=0,sum=0)#(i=0)#(i<n)#(sum+=i)#(i++)#!(i<n)#(return sum)";
		String loopPathIndex = "1 2 3 4 5 3 6";
		HandleSimpleLoopPath handler = new HandleSimpleLoopPath(loopPath, loopPathIndex);
		System.out.println(handler.getOutput());

	}

	public HandleSimpleLoopPath(String simpleLoopPath, String loopPathIndex) {
		IndexList = convertToIndexList(loopPathIndex);
		vertexList = convertToVertexList(simpleLoopPath);
		IndexList = normalizeVertexList(IndexList, vertexList);
		int diemLap = layDiemLap(IndexList);
		int soLanlapToiDa = laySoLanLapToiDa(vertexList.get(diemLap));
		outputList = sinhTapDuongDiMoi(soLanlapToiDa, vertexList, IndexList);
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
	 * 
	 * @param indexPath
	 * @return
	 */
	private ArrayList<String> convertToVertexList(String path) {
		ArrayList<String> output = new ArrayList<String>();
		for (String vertex : path.split("#"))
			output.add(vertex);
		return output;
	}

	/**
	 * 
	 * @param indexPath
	 * @return
	 */
	private ArrayList<Integer> convertToIndexList(String indexPath) {
		ArrayList<Integer> output = new ArrayList<Integer>();
		for (String index : indexPath.split(" "))
			output.add(Integer.parseInt(index));
		return output;
	}

	/**
	 * 
	 * @param soLanlapToiDa
	 * @param simpleLoopPath
	 * @param loopPathIndex
	 * @return
	 */
	private Map<Integer, String> sinhTapDuongDiMoi(int soLanlapToiDa, ArrayList<String> simpleLoopPath,
			ArrayList<Integer> loopPathIndex) {
		Map<Integer, String> output = new HashMap();
		int random = Utils.SinhRandomSoNguyen(3, 17);
		if (soLanlapToiDa == KHONG_XAC_DINH) {
			output.put(0, sinhDuongDiMoi(simpleLoopPath, loopPathIndex, 0));
			output.put(1, sinhDuongDiMoi(simpleLoopPath, loopPathIndex, 1));
			output.put(2, sinhDuongDiMoi(simpleLoopPath, loopPathIndex, 2));
			output.put(random, sinhDuongDiMoi(simpleLoopPath, loopPathIndex, random));
		} else {
			output.put(0, sinhDuongDiMoi(simpleLoopPath, loopPathIndex, 0));
			output.put(1, sinhDuongDiMoi(simpleLoopPath, loopPathIndex, 1));
			output.put(2, sinhDuongDiMoi(simpleLoopPath, loopPathIndex, 2));
			output.put(random, sinhDuongDiMoi(simpleLoopPath, loopPathIndex, random));
			output.put(soLanlapToiDa - 1, sinhDuongDiMoi(simpleLoopPath, loopPathIndex, soLanlapToiDa - 1));
			output.put(soLanlapToiDa, sinhDuongDiMoi(simpleLoopPath, loopPathIndex, soLanlapToiDa));
			output.put(soLanlapToiDa + 1, sinhDuongDiMoi(simpleLoopPath, loopPathIndex, soLanlapToiDa + 1));
		}
		return output;
	}

	/**
	 * 
	 * @param vertexList
	 * @param IndexList
	 * @param numLoop
	 * @return
	 */
	private String sinhDuongDiMoi(ArrayList<String> vertexList, ArrayList<Integer> IndexList, int numLoop) {
		String newPath = "";
		int[] diemLap = layDoanLap(vertexList, IndexList);
		int diemDiVaoVongLap = diemLap[0];
		int diemThoatKhoiVongLap = diemLap[1];
		String doanDau = layKhoi(0, diemDiVaoVongLap - 1, vertexList);
		String khoiLap = layKhoi(diemDiVaoVongLap, diemThoatKhoiVongLap, vertexList);
		String doanCuoi = layKhoi(diemThoatKhoiVongLap + 1, vertexList.size() - 1, vertexList);
		if (numLoop == 0) {
			newPath = doanDau + "#" + doanCuoi;
		} else
			newPath = doanDau + "#" + saoChepKhoiLap(khoiLap, numLoop) + "#" + doanCuoi;
		return newPath;
	}

	/**
	 * 
	 * @param khoiLap
	 * @param numLoop
	 * @return
	 */
	private String saoChepKhoiLap(String khoiLap, int numLoop) {
		String output = "";
		for (int i = 0; i < numLoop; i++)
			output += khoiLap + "#";
		return output.substring(0, output.lastIndexOf("#"));
	}

	/**
	 * 
	 * @param diemBatDau
	 * @param diemKetThuc
	 * @param vertexList
	 * @return
	 */
	private String layKhoi(int diemBatDau, int diemKetThuc, ArrayList<String> vertexList) {
		String output = "";
		for (int j = diemBatDau; j < diemKetThuc; j++)
			output += vertexList.get(j) + "#";
		output += vertexList.get(diemKetThuc);
		return output;
	}

	/**
	 * 
	 * @param vertexList
	 * @param IndexList
	 * @return
	 */
	private int[] layDoanLap(ArrayList<String> vertexList, ArrayList<Integer> IndexList) {
		int diemLapDauTien = -1;
		int diemLapKetThuc = -1;
		for (int i = 0; i < IndexList.size() - 1; i++)
			for (int j = i + 1; j < IndexList.size(); j++)
				if (IndexList.get(j) == IndexList.get(i)) {
					diemLapDauTien = i;
					diemLapKetThuc = j - 1;
					break;
				}
		return new int[] { diemLapDauTien, diemLapKetThuc };
	}

	private int laySoLanLapToiDa(String cauLenhDieuKien) {
		int maxIteration = 0;
		maxIteration = KHONG_XAC_DINH;//assume
		return maxIteration;
	}

	/**
	 * 
	 * @param loopPathIndex
	 * @return
	 */
	private int layDiemLap(ArrayList<Integer> loopPathIndex) {
		int diemLap = -1;
		for (int i = 0; i < loopPathIndex.size() - 1; i++)
			for (int j = i + 1; j < loopPathIndex.size(); j++)
				if (loopPathIndex.get(j) == loopPathIndex.get(i)) {
					diemLap = i;
					break;
				}
		return diemLap;
	}

	public Map<Integer, String> getOutput() {
		return outputList;
	}

	public String getOutputInString() {
		return outputList.toString().replace(", ", "\n").replace("{", "").replace("}", "");
	}

	public String getOutputAt(int numLoop) {
		return outputList.get(numLoop).toString();
	}

	private static final int KHONG_XAC_DINH = -1;
}
