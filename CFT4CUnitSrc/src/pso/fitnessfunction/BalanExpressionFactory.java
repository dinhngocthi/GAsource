package pso.fitnessfunction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BalanExpressionFactory {
	private static List<String> filePaths = new ArrayList<String>();
        private static List<String> fileInformations = new ArrayList<String>();
	private static List<BalanExpressionBulk> balanExpressionBulks = new ArrayList<BalanExpressionBulk>();
	
	private static int getFilePathIndex(String filePath) {
		for (int i = 0; i < filePaths.size(); i++) {
			if (filePaths.get(i).equals(filePath)) {
				return i;
			}
		}
		return -1;
	}
	
	public static int count() {
		return filePaths.size();
	}
	
	public static BalanExpressionBulk getBalanExpressionBulk(String filePath) throws BalanException {
		synchronized (balanExpressionBulks) {
                        int index = getFilePathIndex(filePath);
                        if (index == -1) {
                                File file = new File(filePath);
                                if (!file.exists() || !file.isFile()) {
                                        throw new BalanException("Expression file " + filePath + " not found");
                                }
                                BalanExpressionBulk balanExpressionBulk = new BalanExpressionBulk(file);
                                filePaths.add(filePath);
                                String fileInfo = file.lastModified() + "-" + file.length();
                                fileInformations.add(fileInfo);
                                balanExpressionBulks.add(balanExpressionBulk);
                                return balanExpressionBulk;
                        } else {
                                File file = new File(filePath);
                                if (!file.exists() || !file.isFile()) {
                                        throw new BalanException("Expression file " + filePath + " not found");
                                }
                                String lastInfo = fileInformations.get(index);
                                String currentInfo = file.lastModified() + "-" + file.length();
                                if (!currentInfo.equals(lastInfo)) {
                                        fileInformations.set(index, currentInfo);
                                        BalanExpressionBulk balanExpressionBulk = new BalanExpressionBulk(file);
                                        balanExpressionBulks.set(index, balanExpressionBulk);
                                }
                        }
                        return balanExpressionBulks.get(index);
		}
	}
}
