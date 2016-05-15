package com.asu.aton.ui;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class WindowFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7687773583099900434L;
	
	private String current_id = "";
    public Map<String, JPanel> viewc;
	public WindowFrame(String s){
		super(s);
		current_id = "shortcut";
		viewc = new HashMap<String, JPanel>();
		//this.setUndecorated(true);
		this.setLocation(100, 100);
	}
	
	public void changeView(String s){
		if(s == "") return;
		System.out.println(s);
    	viewc.get(current_id).setVisible(false);
    	viewc.get(s).setVisible(true);
    	this.revalidate();
    	current_id = s;
    }
	
	public void addtoviewc(String s, JPanel p){
		viewc.put(s, p);
		p.setVisible(false);
	}
	
	public void defaultSet(){
		viewc.get(current_id).setVisible(true);
	}
}
