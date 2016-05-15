package com.asu.aton.ui;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class MenuButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7589736782202289794L;
	
	public int MB_WIDTH = 80;
	public int MB_HEIGHT = 80;
	private String mark = null;
	public MenuButton(String imgurl, String m, int ratio){ 
		super();
		ImageIcon icon = new ImageIcon(imgurl);
		if(ratio != 0){
			Image bimg = MyImage.resizeImage(icon.getImage(), ratio);
			icon.setImage(bimg);
		}

		setIcon(icon);
		setMargin(new Insets(20,0,0,0));//将边框外的上下左右空间设置为0
		setIconTextGap(0);//将标签中显示的文本和图标之间的间隔量设置为0
		setBorderPainted(false);//不打印边框
		setBorder(null);//除去边框
		setText(null);//除去按钮的默认名称
		setFocusPainted(false);//除去焦点的框
		setContentAreaFilled(false);//除去默认的背景填充
		mark = m;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	
	public int getIconWidth(){
		return this.getIcon().getIconWidth();
	}
	
	public int getIconHeight(){
		return this.getIcon().getIconHeight();
	}
	
	public void scaleButton(int target){
		ImageIcon icon = (ImageIcon)this.getIcon();
		Image bimg = icon.getImage();
		bimg = MyImage.resizeImage(icon.getImage(), target);
		icon.setImage(bimg);
		this.setIcon(icon);
	}
	
}
