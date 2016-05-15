package com.asu.aton.ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ShortCutView extends JPanel implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7170991421186777536L;
	
	JLabel pen_img = null;
	JLabel title = null;
	MenuList list = null;
	MenuList listright = null;
	JLabel penbb = null;
	JLabel penba = null;
	int mouse_clicked = 0;
	
	public ShortCutView (Container dad1){
		this.setBackground(Color.WHITE);
		this.setLayout(null);
		this.addMouseListener(this);
	}
	
	public void loadView(){
		title = new JLabel();
		this.add(title);
		title.setText("Short Cut Key");
		title.setSize(300,50);
		
		int x_title = (this.getWidth() - title.getWidth()) / 2;
		int y_title = 40;
		title.setLocation(x_title, y_title);
		
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setVerticalAlignment(JLabel.TOP);
		title.setFont(new Font("HelveticaNeue-UltraLight",Font.PLAIN, 30));
		
		
		pen_img = new JLabel();
		ImageIcon icon = new ImageIcon("src/image/bigpen.png");
		Image bimg = MyImage.resizeImage(icon.getImage(), 60);
		icon.setImage(bimg);
		pen_img.setText("");
		pen_img.setIcon(icon);
		pen_img.setSize(icon.getIconWidth(), icon.getIconHeight());
		int x_pen = (this.getWidth() - pen_img.getWidth()) / 2;
		int y_pen = y_title + title.getHeight() + 30;
		pen_img.setLocation(x_pen, y_pen);
		
		penbb = new JLabel();
		ImageIcon icon1 = new ImageIcon("src/image/penb2.png");
		Image bimg1 = MyImage.resizeImage(icon1.getImage(), 22);
		icon1.setImage(bimg1);
		penbb.setIcon(icon1);
		penbb.setText("");
		penbb.setSize(23, 50);
		penbb.setLocation(x_pen + 19, y_pen + 136);
		penbb.setVisible(false);
		
		penba = new JLabel();
		ImageIcon icon2 = new ImageIcon("src/image/penb1.png");
		Image bimg2 = MyImage.resizeImage(icon2.getImage(), 22);
		icon2.setImage(bimg2);
		penba.setIcon(icon2);
		penba.setText("");
		penba.setSize(23, 50);
		penba.setLocation(x_pen + 19, y_pen + 99);
		penba.setVisible(false);
		
		this.add(penba);
		this.add(penbb);
		this.add(pen_img);
		
		int LIST_MARGIN = 80;
		
		String[] s0 = {"space", "double click","asdf"};
		list = new MenuList(3,s0);
		this.add(list);
		list.setSize(list.ITEM_WIDTH, 200);
		list.setLocation(pen_img.getX() - list.getWidth() - LIST_MARGIN, pen_img.getY() + 50);
		
		String[] s = {"space", "double click"};
		listright = new MenuList(2,s);
		this.add(listright);
		listright.setSize(list.ITEM_WIDTH, 200);
		listright.setLocation(pen_img.getX() + pen_img.getWidth() + LIST_MARGIN, pen_img.getY() + 50);
		
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		/*
		if(e.getButton() == e.BUTTON1){
			penba.setVisible(true);
			penbb.setVisible(false);
		}
		else if(e.getButton() == e.BUTTON3){
		    penba.setVisible(false);
		    penbb.setVisible(true);
		}
		*/
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getButton() == e.BUTTON1)
			penba.setVisible(true);
		else if(e.getButton() == e.BUTTON3)
			penbb.setVisible(true);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		penba.setVisible(false);
		penbb.setVisible(false);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
