package com.asu.aton.ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SocialView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7641943928212368628L;
	JPanel account_view;
	JPanel list_view;
	JFrame context;
	List<MenuButton> lists;
	public SocialView(JFrame dad){
		this.setBackground(Color.white);
		this.setLayout(null);
		context = dad;
		lists = new ArrayList<MenuButton>();
	}
	
	public void load_view(){
		account_view = new JPanel();
		this.add(account_view);
		account_view.setBounds(0, 0, 
				context.getWidth() - SideMenu.MENU_WIDTH, context.getHeight() * 1 / 3);
		account_view.setBackground(Color.white);
		MenuButton aicon = new MenuButton("src/image/circle.png","",100);
		account_view.setLayout(null);
		account_view.add(aicon);
		
		int icon_width = 110;
		int icon_x = (account_view.getWidth() - icon_width) / 2;
		int icon_y = 20;
		aicon.setBounds(icon_x,icon_y,icon_width,icon_width);
		JLabel username = new JLabel("Chengmin Li");
		account_view.add(username);
		username.setSize(300,40);
		username.setLocation((account_view.getWidth() - username.getWidth()) / 2,
				icon_y + icon_width + 10);
		username.setHorizontalAlignment(JLabel.CENTER);
		username.setVerticalAlignment(JLabel.TOP);
		username.setFont(new Font("HelveticaNeue-UltraLight", Font.PLAIN, 30));
		
		JLabel email = new JLabel("mars_star@outlook.com");
		account_view.add(email);
		email.setSize(400,40);
		email.setLocation((account_view.getWidth() - email.getWidth()) / 2,
				username.getY() + username.getHeight());
		email.setHorizontalAlignment(JLabel.CENTER);
		email.setVerticalAlignment(JLabel.TOP);
		email.setFont(new Font("HelveticaNeue-UltraLight", Font.PLAIN, 30));
		
		list_view = new JPanel();
		this.add(list_view);
		list_view.setBounds(account_view.getX(), account_view.getHeight(),
				account_view.getWidth(), context.getHeight() * 2 / 3);
		Color list_back = new Color(248,248,248);
		list_view.setBackground(list_back);
		list_view.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.lightGray));
		//list_view.setLayout(new GridLayout(2,4));
		list_view.setLayout(null);
		lists.add(new MenuButton("src/image/account1.png", "a1",0));
		lists.add(new MenuButton("src/image/account2.png", "a2",0));
		lists.add(new MenuButton("src/image/account3.png", "a3",0));
		lists.add(new MenuButton("src/image/account4.png", "a4",0));
		lists.add(new MenuButton("src/image/account5.png", "a5",0));
		int column = 4;
		int line = 2;
		int offset_v = 10;
		int offset_h = 20;
		int right_dis = 30;
		int cardwidth = (MainBoard.DEFAULT_WIDTH - SideMenu.MENU_WIDTH - offset_h * 5 - right_dis) / 4;
		for(int i = 0;i < line;++i){
			for(int j = 0;j < column; ++j){
				int index = i * column + j;
				if(index >= lists.size()) continue;
				list_view.add(lists.get(index));
				lists.get(index).scaleButton(cardwidth);
				lists.get(index).setBounds((j+1) * offset_h + j * cardwidth,
						(i+1) * offset_v + i * lists.get(index).getIconHeight(),
						cardwidth, 
						lists.get(index).getIconHeight());
			}
		}
	}
}
