package Graphics;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class WindowsHandle
{

    private JFrame frmResult;
    private Canvas panel = new Canvas();
    private JTextArea stringList;
    private JScrollPane scrollPane_1;

    public void alert(Object str)
    {
        javax.swing.JOptionPane.showMessageDialog(null, str);
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    WindowsHandle window = new WindowsHandle();
                    window.frmResult.setVisible(true);
                    window.frmResult.setExtendedState(Frame.MAXIMIZED_BOTH);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public WindowsHandle()
    {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize()
    {
        frmResult = new JFrame();
        frmResult.setTitle("Result");
        frmResult.setBounds(100, 100, 752, 458);
        frmResult.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JScrollPane scrollPane = new JScrollPane();

        JLabel lblTestpathList = new JLabel("TestNode List");

        JButton btnShowResult = new JButton("Show result");
        btnShowResult.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                String text = stringList.getText();
                panel.createGraphFromNodeList(text);

            }
        });

        scrollPane_1 = new JScrollPane();
        GroupLayout groupLayout = new GroupLayout(frmResult.getContentPane());
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
                groupLayout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                                groupLayout.createParallelGroup(Alignment.LEADING).addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 358, GroupLayout.PREFERRED_SIZE).addComponent(lblTestpathList))
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(btnShowResult).addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE))
                        .addContainerGap()));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(
                groupLayout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblTestpathList).addComponent(btnShowResult))
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addGroup(
                                groupLayout.createParallelGroup(Alignment.LEADING).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
                                        .addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)).addContainerGap()));
        panel.setPreferredSize(new Dimension(345, 360));
        scrollPane_1.setViewportView(panel);
        panel.setBackground(Color.WHITE);
        panel.setLayout(null);

        stringList = new JTextArea();
        scrollPane.setViewportView(stringList);
        stringList
                .setText("0#Bat dau ham#true=1#false=1\n1#a==b#true=2#false=3\n2#return 0;#true=8#false=8\n3#int x = 0;#true=4#false=4\n4#a = b - 2;#true=5#false=5\n5#(a==b)||(c==d)#true=6#false=7\n6#x = 1;#true=7#false=7\n7#return 1/x;#true=8#false=8\n8#Ket thuc ham#true=-1#false=-1");
        stringList.setLineWrap(true);

        frmResult.getContentPane().setLayout(groupLayout);

    }
}
