package Graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class Node extends JLabel {
	private static final long serialVersionUID = 1L;
	private String content;
	private int nodeId;
	private int trueId;
	private int falseId;
	private JPanel canvas;
	private boolean trueSelected;
	private boolean falseSelected;
	private String sttStr;

	private int x, y;
	// private int maxX, maxY;

	public Node(int _id, String _content, int _trueId, int _falseId, JPanel _canvas) {
		super();
		Dimension size;
		int paddingW = 15;
		int paddingH = 10;

		canvas = _canvas;
		trueId = _trueId;
		falseId = _falseId;
		// setText("<html>"+_content.replace(";", "<br/>")+"</html>");
		setText(_content.replace(";", ";"));
		setNodeId(_id);
		sttStr = new String();

		size = getPreferredSize();
		size.setSize(size.getWidth() + paddingW, size.getHeight() + paddingH);
		setSize(size);
		setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		setHorizontalAlignment(SwingConstants.CENTER);
		setOpaque(true);
		setBackground(SystemColor.inactiveCaptionBorder);

		this.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int xx = e.getX();
				int yy = e.getY();
				int newX = Node.this.getX() + xx - x;
				int newY = Node.this.getY() + yy - y;

				newX = Math.max(newX, 0);
				// newX = Math.min(newX, maxX);
				newY = Math.max(newY, 0);
				// newY = Math.min(newY, maxY);
				Node.this.setLocation(newX, newY);
				Node.this.canvas.repaint();
			}
		});
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				// Dimension size = Node.this.canvas.getPreferredSize();

				x = (int) e.getPoint().getX();
				y = (int) e.getPoint().getY();
				/*
				 * maxX = (int)Math.max(size.getWidth(), canvas.getWidth()) -
				 * Node.this.getWidth(); maxY = (int)Math.max(size.getHeight(),
				 * canvas.getHeight()) - Node.this.getHeight();
				 */
				Node.this.requestFocusInWindow();
			}
		});
		this.setFocusable(true);
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int code = e.getKeyCode();

				if (code >= 37 && code <= 40) {
					int x = getX();
					int y = getY();
					int delta = e.isControlDown() ? 10 : 1;

					// e.consume();
					switch (code) {
					case 37:
						setLocation(Math.max(x - delta, 0), y);
						break;
					case 38:
						setLocation(x, Math.max(y - delta, 0));
						break;
					case 39:
						setLocation(x + delta, y);
						break;
					case 40:
						setLocation(x, y + delta);
						break;
					}
					canvas.repaint();
				}
			}
		});
	}

	public boolean equals(Node _otherNode) {
		return nodeId == _otherNode.nodeId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int id) {
		this.nodeId = id;
	}

	public int getTrueId() {
		return trueId;
	}

	public int getFalseId() {
		return falseId;
	}

	public boolean isCondition() {
		return trueId != falseId;
	}

	public boolean isLocationSet() {
		return this.getX() != 0 || this.getY() != 0;
	}

	@Override
	public String toString() {
		return content + ": " + nodeId + "\nTrue: " + trueId + "\nFalse: " + falseId + "\n";
	}

	public boolean isTrueSelected() {
		return trueSelected;
	}

	public void setTrueSelected(boolean trueSelected) {
		this.trueSelected = trueSelected;
	}

	public boolean isFalseSelected() {
		return falseSelected;
	}

	public void setFalseSelected(boolean falseSelected) {
		this.falseSelected = falseSelected;
	}

	public String getStt() {
		return sttStr;
	}

	public void addStt(int stt) {
		if (sttStr.isEmpty())
			sttStr = String.valueOf(stt);
		else
			sttStr += ", " + stt;
	}

	public void resetStt() {
		sttStr = "";
	}

	public void setCanvas(JPanel canvas) {
		this.canvas = canvas;
	}
}
