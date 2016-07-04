import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import C.Utils;

public class GUtils {

	public static BufferedImage createImage(JPanel panel) {

		int w = panel.getWidth();
		int h = panel.getHeight();
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();
		panel.paint(g);
		return bi;
	}

	public static void updateRow(int i, String testpath, String hpt, String TC, String RO, DefaultTableModel tm) {
		tm.setValueAt(i + "", i, 0);
		tm.setValueAt(testpath, i, 1);
		tm.setValueAt(hpt, i, 2);
		tm.setValueAt(TC, i, 3);
		tm.setValueAt(RO, i, 4);
	}

	public static void updateRow(int i, String[] o, DefaultTableModel tm) {
		for (int j = 0; j < o.length; j++) {
			tm.setValueAt(o[j], i, j);
		}
	}

	public static void clearAllRowsInJTable(int numRow, int numCol, DefaultTableModel tm) {
		for (int i = 0; i < numRow; i++) {
			for (int j = 0; j < numCol; j++) {
				tm.setValueAt(" ", i, j);
			}
		}
	}

	public static void exportTableToHtml(JTable[] tableList, String[] title, File file) throws IOException {
		FileWriter out = new FileWriter(file);
		out.write("<!DOCTYPE html>\n" + "<html>\n" + "<head>\n" + "<style>\n" + "table, td, th {\n"
				+ "    border: 1px solid green;\n" + "    border-collapse:collapse;" + "}\n" + "\n" + "th {\n"
				+ "    background-color: green;\n" + "    color: white;\n" + "}\n" + "</style>\n" + "</head>");
		out.write("<body>");
		for (int k = 0; k < tableList.length; k++) {
			TableModel model = tableList[k].getModel();
			int rowMax = 0;
			for (int i = 0; i < model.getRowCount(); i++) {
				try {
					Integer.parseInt(model.getValueAt(i, 0).toString());
					rowMax = i;
				} catch (Exception e) {
					break;
				}
			}
			out.write(title[k] + "<br/>");
			out.write("<table>");
			out.write("<tr><td>STT</td><td>Testpath</td><td>Equations</td><td>Test case</td><td>Real Output</td></tr>");
			for (int i = 0; i < rowMax; i++) {
				out.write("<tr>");
				for (int j = 0; j < model.getColumnCount(); j++) {
					out.write("<td>" + model.getValueAt(i, j).toString().replace("&", " &amp; ").replace("<", " &lt; ")
							.replace(">", " &gt; ") + "</td>");
				}
				out.write("</tr>");
			}

			out.write("</table><br/><br/>");
		}
		out.write("</body></html>");
		out.close();
	}

	public static void exportJTableToExcel(JTable[] tableList, String[] title, ArrayList<String[]> titleColumn,
			File file) throws IOException {
		FileWriter out = new FileWriter(file);
		String xml = "<?xml version=\"1.0\"?>\n"
				+ "<ss:Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\"\n"
				+ " xmlns:o=\"urn:schemas-microsoft-com:office:office\"\n"
				+ " xmlns:x=\"urn:schemas-microsoft-com:office:excel\"\n"
				+ " xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\"\n"
				+ " xmlns:html=\"http://www.w3.org/TR/REC-html40\">\n";
		for (int k = 0; k < tableList.length; k++) {
			TableModel model = tableList[k].getModel();
			int rowMax = 0;
			for (int i = 0; i < model.getRowCount(); i++) {
				try {
					Integer.parseInt(model.getValueAt(i, 0).toString());
					rowMax = i + 1;
				} catch (Exception e) {
					break;
				}
			}
			// create Worksheet
			xml += " <Worksheet ss:Name=\"" + title[k] + "\">\n";
			xml += "  <Table ss:ExpandedColumnCount=\"" + model.getColumnCount() + "\" ss:ExpandedRowCount=\""
					+ (rowMax + 1) + "\" x:FullColumns=\"1\" x:FullRows=\"1\">\n";
			// set width columns
			xml += "<Column ss:AutoFitWidth=\"1\" ss:Width=\"20\"/>\n";
			for (int j = 1; j < model.getColumnCount(); j++) {
				xml += "<Column ss:AutoFitWidth=\"1\" ss:Width=\"150\"/>\n";
			}
			// add headers
			xml += "   <Row>\n";
			for (String item : titleColumn.get(k))
				xml += "    <Cell><Data ss:Type=\"String\">" + Utils.convertToHtml(item) + "</Data></Cell>\n";

			xml += "   </Row>\n";
			// add contents
			for (int i = 0; i < rowMax; i++) {
				xml += "   <Row>\n";
				for (int j = 0; j < model.getColumnCount() - 1; j++) {
					String content = Utils.convertToHtml((String) model.getValueAt(i, j)) + "";
					xml += "    <Cell><Data ss:Type=\"String\">" + content + "</Data></Cell>\n";
				}
				xml += "   </Row>\n";
			}
			// close
			xml += "  </Table>\n";
			xml += " </Worksheet>\n";
		}

		xml += "</ss:Workbook>";
		out.write(xml);
		System.out.println(xml);
		out.close();
	}
}
