package Graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class CanvasPhuCap3 extends JPanel {

	private static final long serialVersionUID = 1L;
	protected ArrayList<Node> nodeList = new ArrayList<Node>();
	private boolean fullscreen;
	private Container previousContentPane;
	private CanvasPhuCap3 cloneCanvas;

	final private int paddingX = 80;
	final private int marginX = 150;
	final private int paddingY = 40;
	final private int marginY = 60;

	public CanvasPhuCap3() {
		super();

		this.setLayout(null);
		this.setFocusable(true);
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				CanvasPhuCap3.this.requestFocusInWindow();
				if (!e.isConsumed() && e.getClickCount() == 2) {
					e.consume();
					if (fullscreen) {
						exitFullScreen();
					} else {
						goFullScreen();
					}
				}
			}
		});
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (fullscreen && e.getKeyCode() == 27) {
					exitFullScreen();
				}
			}
		});
	}

	public void createGraphFromNodeList(String nodeStrList) {
		String[] nodeStrs = nodeStrList.split("\n");

		this.setLayout(null);
		this.removeAll();
		nodeList.clear();

		for (int i = 0; i < nodeStrs.length; i++) {
			String nodeData[] = nodeStrs[i].split("#");
			String trueIdStr = nodeData[2].substring(nodeData[2].indexOf("=") + 1);
			String falseIdStr = nodeData[3].substring(nodeData[3].indexOf("=") + 1);
			Node node = new Node(Integer.valueOf(nodeData[0]), nodeData[1], Integer.valueOf(trueIdStr),
					Integer.valueOf(falseIdStr), this);
			nodeList.add(node);
		}

		/**
		 * *** Sap xep va hien thi cac node trong Canvas ****
		 */
		int maxCondition = 0;

		for (int i = 0; i < nodeList.size(); i++) {
			if (nodeList.get(i).isCondition()) {
				maxCondition++;
			}
		}

		Node firstNode = nodeList.get(0);
		int minX = paddingX + maxCondition * marginX - firstNode.getWidth() / 2;
		firstNode.setLocation(minX, paddingY - firstNode.getHeight() / 2);

		for (int i = 0; i < nodeList.size(); i++) {
			Node node = nodeList.get(i);
			this.add(node);
			int x = node.getX() + node.getWidth() / 2;
			int y = node.getY() + node.getHeight() / 2;
			Node trueNode = this.getNodeById(node.getTrueId());
			Node falseNode = this.getNodeById(node.getFalseId());

			if (trueNode == null) {
				continue;
			}
			if (node.isCondition()) {
				if (!(trueNode.isLocationSet() && trueNode.isCondition())) {
					trueNode.setLocation(x - marginX - trueNode.getWidth() / 2, y + marginY - trueNode.getHeight() / 2);
				}
				if (!(falseNode.isLocationSet() && falseNode.isCondition())) {
					falseNode.setLocation(x + marginX - falseNode.getWidth() / 2,
							y + marginY - falseNode.getHeight() / 2);
				}
			} else {
				if (!(trueNode.isLocationSet() && trueNode.isCondition())) {
					trueNode.setLocation(x - trueNode.getWidth() / 2, y + marginY - trueNode.getHeight() / 2);
				}
			}
		}

		// Keo ve sat le ben trai
		for (int i = 1; i < nodeList.size(); i++) {
			if (nodeList.get(i).getX() < minX) {
				minX = nodeList.get(i).getX();
			}
		}
		int changeX = minX - paddingX;
		for (int i = 0; i < nodeList.size(); i++) {
			Node node = nodeList.get(i);
			node.setLocation(node.getX() - changeX, node.getY());
		}

		/**
		 * **********************************************
		 */
		this.repaint();
	}

	public void updateLineColorWhenClick(String testpath) {
		for (int i = 0; i < nodeList.size(); i++) {
			nodeList.get(i).setTrueSelected(false);
			nodeList.get(i).setFalseSelected(false);
		}
		testpath = "(Bat dau ham)#" + testpath + "#(Ket thuc ham)";

		String[] testNodes = testpath.split("#");
		int nextId = 0;
		for (int i = 0; i < testNodes.length - 1; i++) {
			String testNode = testNodes[i];
			Node node = this.getNodeById(nextId);

			if (!node.isCondition() || testNode.charAt(0) != '!') {
				node.setTrueSelected(true);
				nextId = node.getTrueId();
			} else {
				node.setFalseSelected(true);
				nextId = node.getFalseId();
			}

		}
		this.repaint();
	}

	public void resetAllLineColor() {
		for (int i = 0; i < nodeList.size(); i++) {
			nodeList.get(i).setTrueSelected(false);
			nodeList.get(i).setFalseSelected(false);
		}
		this.repaint();
	}

	public void showSttSelectedTestpath(String selectedTestpath) {
		for (int i = 0; i < nodeList.size(); i++) {
			nodeList.get(i).resetStt();
		}
		selectedTestpath = "(Bat dau ham)#" + selectedTestpath + "#(Ket thuc ham)";
		String[] testNodes = selectedTestpath.split("#");
		int nextId = 0;

		for (int i = 0; i < testNodes.length; i++) {
			String testNode = testNodes[i];
			Node node = this.getNodeById(nextId);

			node.addStt(i);
			if (!node.isCondition() || testNode.charAt(0) != '!') {
				nextId = node.getTrueId();
			} else {
				nextId = node.getFalseId();
			}

		}
		this.repaint();
	}

	public void disableSttSelectedTestpath(String selectedTestpath) {
		selectedTestpath = "(Bat dau ham)#" + selectedTestpath + "#(Ket thuc ham)";
		String[] testNodes = selectedTestpath.split("#");
		int nextId = 0;

		for (int i = 0; i < testNodes.length; i++) {
			String testNode = testNodes[i];
			Node node = this.getNodeById(nextId);

			node.resetStt();
			if (!node.isCondition() || testNode.charAt(0) != '!') {
				nextId = node.getTrueId();
			} else {
				nextId = node.getFalseId();
			}

		}
		this.repaint();
	}

	public void goFullScreen() {
		if (fullscreen) {
			return;
		}
		fullscreen = true;
		Window w = SwingUtilities.windowForComponent(this);
		JFrame frame = new JFrame();

		if (w instanceof JFrame) {
			previousContentPane = ((JFrame) w).getContentPane();
		}
		frame.setUndecorated(true);
		frame.getGraphicsConfiguration().getDevice().setFullScreenWindow(frame);

		frame.setContentPane(this.getCloneCanvas());
		frame.revalidate();
		frame.repaint();
		frame.setVisible(true);
		this.requestFocusInWindow();
	}

	public void exitFullScreen() {
		if (!fullscreen) {
			return;
		}
		fullscreen = false;
		SwingUtilities.windowForComponent(this).dispose();
		Window w = SwingUtilities.windowForComponent(cloneCanvas);
		JFrame frame = (JFrame) w;

		for (int i = 0; i < nodeList.size(); i++) {
			nodeList.get(i).setCanvas(cloneCanvas);
			cloneCanvas.add(nodeList.get(i));
		}
		cloneCanvas.fullscreen = false;
		frame.setContentPane(previousContentPane);
		frame.revalidate();
		frame.repaint();
		frame.setVisible(true);
	}

	private Container getCloneCanvas() {
		JScrollPane scrollPane = new JScrollPane();
		CanvasPhuCap3 c = new CanvasPhuCap3();

		c.cloneCanvas = this;
		c.setLayout(getLayout());
		c.setBackground(getBackground());
		c.fullscreen = fullscreen;
		c.nodeList = nodeList;
		c.previousContentPane = previousContentPane;
		for (int i = 0; i < nodeList.size(); i++) {
			nodeList.get(i).setCanvas(c);
			c.add(nodeList.get(i));
		}

		scrollPane.setViewportView(c);
		return scrollPane;
	}

	public int getSizeWidthCanvas() {
		// chua can code
		return 0;
	}

	public int getSizeHeightCanvas() {
		// chua can code
		return 0;
	}

	private Node getNodeById(int id) {
		for (int i = 0; i < nodeList.size(); i++) {
			if (nodeList.get(i).getNodeId() == id) {
				return nodeList.get(i);
			}
		}
		return null;
	}

	private Point generateStrPos(int x1, int y1, int x2, int y2, boolean leftPrefer) {
		double anpha = Math.atan((x2 - x1) * 1.0 / Math.abs(y2 - y1));
		int d = 38, r = 6;
		double anpha1 = Math.acos(1 - r * r / 2.0 / d / d);
		boolean smallAngle = Math.abs(anpha) <= Math.PI / 4;
		boolean rightSide = (smallAngle && !leftPrefer) || (!smallAngle && anpha < 0);
		double anpha2 = anpha + anpha1 * (rightSide ? 1 : -1);
		int x = x1 + (int) (d * Math.sin(anpha2));
		int y = y1 + (int) (d * Math.cos(anpha2));
		return new Point(x - 2, y + 4);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int x1, y1, x2, y2;
		int d = 12; // Chieu dai mui ten
		int h = 5; // Chieu cao mui ten
		double D, xh, yh, a, b, A, B, C, delta, xm, ym, xn, yn;
		Graphics2D g2 = (Graphics2D) g;

		g2.setStroke(new BasicStroke((float) 2.5));
		g2.setFont(new Font("TimesRoman", Font.BOLD, 12));
		for (int i = 0; i < nodeList.size(); i++) {
			Node source = nodeList.get(i);

			/**
			 * Ve duong noi giua hai nut*
			 */
			ArrayList<Integer> targets = new ArrayList<Integer>(2);
			int trueId = source.getTrueId();
			int falseId = source.getFalseId();

			if (trueId != -1) {
				targets.add(source.getTrueId());
			}
			if (falseId != -1 && falseId != trueId) {
				targets.add(falseId);
			}
			for (int j = 0; j < targets.size(); j++) {
				Node target = this.getNodeById(targets.get(j));
				Color color;

				if (targets.size() == 1) {
					color = source.isTrueSelected() ? myColor.SELECTED : myColor.DEFAULT;
				} else if (j == 0) {
					color = source.isTrueSelected() ? myColor.SELECTED : myColor.TRUE;
				} else {
					color = source.isFalseSelected() ? myColor.SELECTED : myColor.FALSE;
				}
				g2.setColor(color);

				x1 = source.getX() + source.getWidth() / 2;
				y1 = source.getY() + source.getHeight();
				x2 = target.getX() + target.getWidth() / 2;
				y2 = target.getY();
				Point conditionStr = new Point();

				if (source.isCondition()) {
					conditionStr = generateStrPos(x1, y1, x2, y2, j == 0);
				}
				boolean rightSide = x2 > x1;

				if (y2 > y1) {
					double angle = Math.atan((y2 - y1) * 1.0 / Math.abs(x2 - x1));
					if (angle < Math.PI / 8) {
						y2 = y2 + target.getHeight() / 2;
						x2 = target.getX() + (rightSide ? 0 : target.getWidth());
						if (source.isCondition()) {
							conditionStr = generateStrPos(x1, y1, x2, y2, j == 0);
						}
					}
				} else {
					int gap = 25;
					int nearSide = target.getX() + (rightSide ? 0 : target.getWidth());
					int distance = Math.abs(nearSide - (x1 + source.getWidth() / 2 * (rightSide ? 1 : -1)));
					boolean outOfPadding = (source.getX() + source.getWidth() < target.getX()
							|| target.getX() + target.getWidth() < source.getX()) && distance > gap + 1;

					if (outOfPadding && target.getY() >= source.getY()) {
						x1 = source.getX() + (rightSide ? source.getWidth() : 0);
						y1 = source.getY() + source.getHeight() / 2;
						x2 = target.getX() + (rightSide ? 0 : target.getWidth());
						y2 = target.getY() + target.getHeight() / 2;
						if (source.isCondition()) {
							conditionStr = generateStrPos(x1, y1, x2, y2, j == 0);
						}
					} else {
						g2.drawLine(x1, y1, x1, y1 + gap);
						if (source.isCondition()) {
							conditionStr = generateStrPos(x1, y1, x1, y1 + gap, j == 0);
						}
						int tmp;
						if (outOfPadding) {
							tmp = x2 + (target.getWidth() / 2 + gap) * (rightSide ? -1 : 1);
						} else {
							tmp = target.getX() + (rightSide ? target.getWidth() + gap : -gap);
						}
						g2.drawLine(x1, y1 + gap, tmp, y1 + gap);
						x1 = tmp;
						y2 = y2 + target.getHeight() / 2;
						g2.drawLine(x1, y1 + gap, x1, y2);
						y1 = y2;
						x2 = target.getX() + (rightSide ^ outOfPadding ? target.getWidth() : 0);
					}
				}

				// Ve mui ten o duong thang cuoi
				D = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
				xh = ((D - d) * x2 + d * x1) / D;
				yh = ((D - d) * y2 + d * y1) / D;
				if (y2 == y1) {
					xm = xn = xh;
					ym = yh - h;
					yn = yh + h;
				} else {
					a = (x1 - x2) * 1.0 / (y2 - y1);
					b = -a * xh + yh;
					A = 1 + a * a;
					B = a * (b - yh) - xh;
					C = xh * xh + (b - yh) * (b - yh) - h * h;
					delta = B * B - A * C;
					if (delta < 0) {
						continue;
					}

					xm = (-B + Math.sqrt(delta)) / A;
					xn = (-B - Math.sqrt(delta)) / A;
					ym = a * xm + b;
					yn = a * xn + b;
				}
				int[] xpoints = { x2, (int) xm, (int) xn };
				int[] ypoints = { y2, (int) ym, (int) yn };

				if (source.isCondition()) {
					g2.drawString(j == 0 ? "T" : "F", (int) conditionStr.getX(), (int) conditionStr.getY());
				}
				g2.draw(new Line2D.Float(x1, y1, x2, y2));
				g2.fillPolygon(xpoints, ypoints, 3);
			} // Ket thuc ve duong noi

			/**
			 * Ve so thu tu nut*
			 */
			if (!source.getStt().isEmpty()) {
				int x = source.getX() + source.getWidth();
				int y = source.getY();
				String str = source.getStt();
				int lbr = (str.length() - str.replace(", ", "").length()) / 2;
				int w = 7 * str.length() - 8 * lbr - 1;

				g2.setColor(Color.YELLOW);
				g2.fillOval(x, y - 17, w + 8, 17);
				g2.setColor(Color.BLACK);
				g2.drawString(source.getStt(), x + 4, y - 4);

			}
		}
		if (!nodeList.isEmpty()) {
			int maxX = 0, maxY = 0, x, y;
			for (int i = 0; i < nodeList.size(); i++) {
				Node node = nodeList.get(i);

				x = node.getX() + node.getWidth();
				y = node.getY() + node.getHeight();
				if (x > maxX) {
					maxX = x;
				}
				if (y > maxY) {
					maxY = y;
				}
			}
			this.setPreferredSize(new Dimension(maxX + paddingX, maxY + paddingY));
			this.revalidate();
		}
	}
}
