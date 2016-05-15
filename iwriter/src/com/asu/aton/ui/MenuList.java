package com.asu.aton.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MenuList extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 993457247720668414L;
	
	private List<IButton> blist;
	public int ITEM_WIDTH = 150;
	public int ITEM_HEIGHT = 30;
	public int ITEM_MARGIN = 10;
	
	private String[] titles = null;
	
	public MenuList(int number, String[] ss){
		this.setBackground(Color.white);
		this.setLayout(null);
		blist = new ArrayList<IButton>();
		titles = new String[number];
		this.setTitles(ss);
		
		ImageIcon icon = new ImageIcon("src/image/bigbutton.png");
		Image bimg = MyImage.resizeImage(icon.getImage(), ITEM_WIDTH);
		icon.setImage(bimg);
		
		for(int i = 0;i < number; ++i){
			blist.add(new IButton(icon));
		}
		
		for(int i = 0;i < number; ++i){
			this.add(blist.get(i));
			blist.get(i).setTitle(titles[i]);
			blist.get(i).setBounds(0, (icon.getIconHeight() + ITEM_MARGIN) * i,
					icon.getIconWidth() , icon.getIconHeight());
		}
	}
	
	public void setTitles(String[] s){
		for(int i = 0;i < titles.length; ++i){
			if(i < s.length) titles[i] = s[i];
			else titles[i] = "";
		}
	}
	
	public class IButton extends JButton{
		private String title = "aa";
		public IButton(ImageIcon icon){
			this.setIcon(icon);
			setMargin(new Insets(0,0,0,0));//将边框外的上下左右空间设置为0
			setIconTextGap(0);//将标签中显示的文本和图标之间的间隔量设置为0
			//setBorderPainted(false);//不打印边框
			setBorder(null);//除去边框
			setText("");//除去按钮的默认名称
			setFocusPainted(false);//除去焦点的框
			setContentAreaFilled(false);//除去默认的背景填充
		}
		
		public void setTitle(String s){
			title = s;
		}
		
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			
			Graphics2D g2d = (Graphics2D)g;
			RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,  
                    RenderingHints.VALUE_ANTIALIAS_ON);  
			g2d.addRenderingHints(rh);  
			g2d.setFont(new Font("HelveticaNeue-UltraLight", Font.PLAIN, 20));
		    g2d.setColor(new Color(72,72,72));
			g2d.drawString(title, 30, 20);
			
		}
		
	}
	
	
}