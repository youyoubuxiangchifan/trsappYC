package com.trs.service.test;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JButton;
public class AppSwing {
	

	    public static void main(String[] args) {
	        //建立一个JFrame,JFrame的默认LayoutManager为BorderLayout
	        JFrame f=new JFrame("BorderLayout");
	        JButton btn=new JButton("BorderLayout.NORTH");
	        f.add(btn,BorderLayout.NORTH);
	        btn=new JButton("BorderLayout.SOUTH");
	        f.setLayout(new BorderLayout(10,10));
	        f.add(btn,BorderLayout.SOUTH);
	        btn=new JButton("BorderLayout.EAST");
	        f.add(btn,BorderLayout.EAST);
	        btn=new JButton("BorderLayout.West");
	        f.add(btn,BorderLayout.WEST);
	        btn=new JButton("BorderLayout.CENTER");
	        f.add(btn,BorderLayout.CENTER);
	        f.pack();
	        f.setVisible(true);
	        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    }

}
