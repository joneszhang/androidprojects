package com.asu.aton.ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SideMenu extends JPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6004849389901299860L;
	private Color back_color;
	private WindowFrame dad;
	private List<MenuButton> buttons = new ArrayList<MenuButton>();
	private MenuButton circleb;
	private String[] button_marks;
	static int MENU_WIDTH = 100;
	public SideMenu(WindowFrame dad1){
		this.dad = dad1;
		back_color = new Color(246,246,246);
		this.setBackground(back_color);
		this.setLayout(null);
		this.setBounds(0, 0, 100, dad.getHeight());
		int icon_width = this.MENU_WIDTH - 10*2;
		buttons.add(new MenuButton("src/image/left1.png", "shortcut",icon_width));
		buttons.add(new MenuButton("src/image/left2.png","",icon_width));
		buttons.add(new MenuButton("src/image/left3.png","",icon_width));
		buttons.add(new MenuButton("src/image/left4.png","draw",icon_width));
		buttons.add(new MenuButton("src/image/left5.png","",icon_width));
		buttons.add(new MenuButton("src/image/left6.png","social",icon_width));
		for(int i = 0;i < buttons.size();++i){
			this.add(buttons.get(i));
			buttons.get(i).setBounds(0,i* MENU_WIDTH, MENU_WIDTH, MENU_WIDTH);
			buttons.get(i).addActionListener(this);
		}
		circleb = new MenuButton("src/image/circle.png","",60);
		this.add(circleb);
		circleb.setBounds(buttons.get(0).getBounds());
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		for(int i = 0;i < buttons.size(); ++i){
			if(e.getSource() == buttons.get(i)){
				circleb.setBounds(buttons.get(i).getBounds());
				dad.changeView(buttons.get(i).getMark());
			}
		}
	}
}
