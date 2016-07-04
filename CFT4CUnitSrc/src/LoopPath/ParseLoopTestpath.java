package LoopPath;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import C.DuongKiemThu;

public class ParseLoopTestpath {
	private int typeOfTestpath = KHONG_XAC_DINH;

	public static void main(String[] args) {
	}

	public ParseLoopTestpath(DuongKiemThu testpath) {
		ArrayList<Integer> testpathIndex = testpath.getTestpathIndex();
		if (isSimpleLoop(testpathIndex))
			typeOfTestpath = SIMPLE_LOOP;
		else if (isTwoNestedLoop(testpathIndex))
			typeOfTestpath = TWO_ESTED_LOOP;
		else
			typeOfTestpath = KHONG_XAC_DINH;
	}

	private boolean isSimpleLoop(ArrayList<Integer> testpathIndex) {
		Set<Integer> tapDinhKhacNhau = layTapDinhKhacNhau(testpathIndex);
		Set<Integer> tapDinhLap = layTapDinhLap(testpathIndex);
		if (tapDinhLap.size() == 1 && tapDinhKhacNhau.size() == testpathIndex.size() - 1)
			// Tồn tại một đỉnh lặp lại và đỉnh này chỉ lặp lại duy nhất 1 lần
			return true;
		else
			return false;
	}

	private boolean isTwoNestedLoop(ArrayList<Integer> testpathIndex) {
		Set<Integer> tapDinhLap = layTapDinhLap(testpathIndex);
		Set<Integer> tapDinhKhacNhau = layTapDinhKhacNhau(testpathIndex);
		if (tapDinhLap.size() == 2 && tapDinhKhacNhau.size() == testpathIndex.size() - 2) {
			// Tồn tại hai đỉnh lặp lại và mỗi đỉnh này chỉ lặp lại duy nhất 1
			// lần
			int dinhLap1 = (int) tapDinhLap.toArray()[0];
			int dinhLap2 = (int) tapDinhLap.toArray()[1];
			// kiem tra hai vong lap nay bao nhau hay khong
			int posDinhLap1_Begin = testpathIndex.indexOf(dinhLap1);
			int posDinhLap1_End = testpathIndex.lastIndexOf(dinhLap1);
			int posDinhLap2_Begin = testpathIndex.indexOf(dinhLap2);
			int posDinhLap2_End = testpathIndex.lastIndexOf(dinhLap2);
			if (posDinhLap1_Begin < posDinhLap2_Begin && posDinhLap1_End > posDinhLap2_End) {
				// nếu vòng lặp tại đỉnh lặp 1 chứa vòng lặp tại vòng lặp 2
				return true;
			} else if (posDinhLap2_Begin < posDinhLap1_Begin && posDinhLap2_End > posDinhLap1_End) {
				// nếu vòng lặp tại đỉnh lặp 2 chứa vòng lặp tại vòng lặp 1
				return true;
			} else
				return false;
		}
		return false;
	}

	private Set<Integer> layTapDinhKhacNhau(ArrayList<Integer> testpathIndex) {
		Set<Integer> indexSet = new HashSet<Integer>();
		for (Integer i : testpathIndex) {
			indexSet.add(i);
		}
		return indexSet;
	}

	private Set<Integer> layTapDinhLap(ArrayList<Integer> testpathIndex) {
		Set<Integer> indexSet = new HashSet<Integer>();
		Set<Integer> tapDinhLap = new HashSet<Integer>();
		for (Integer i : testpathIndex) {
			if (indexSet.contains(i))
				tapDinhLap.add(i);
			else
				indexSet.add(i);
		}
		return tapDinhLap;
	}

	public int getTypeOfTestpath() {
		return typeOfTestpath;
	}

	public static final int KHONG_XAC_DINH = 0;
	public static final int SIMPLE_LOOP = 1;
	public static final int TWO_ESTED_LOOP = 2;
}
