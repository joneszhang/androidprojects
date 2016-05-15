package com.asu.aton.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import java.util.List;
import java.util.Map;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainBoard{
	static int DEFAULT_WIDTH = 800;
    static int DEFAULT_HEIGHT = 680;
    WindowFrame frame;
    
    public void addComponentsToPane(Container pane) {
        pane.setLayout(null);
      //load side menu
        SideMenu menu = new SideMenu(frame);
    	ShortCutView shortcut_view = new ShortCutView(pane);
    	
    	pane.add(menu);
      	//menu.setAlignmentY(Component.TOP_ALIGNMENT);
      	menu.setSize(menu.MENU_WIDTH, MainBoard.DEFAULT_HEIGHT);
      	//load short cut menu
      	pane.add(shortcut_view);
      	shortcut_view.setBounds(menu.getWidth(), 0, 
      			DEFAULT_WIDTH - menu.getWidth(), DEFAULT_HEIGHT);
      	shortcut_view.loadView();
      	//shortcut_view.setAlignmentY(Component.TOP_ALIGNMENT);
      	
      	frame.addtoviewc("shortcut", shortcut_view);
      	//load social view
      	SocialView social_view = new SocialView(frame);
      	frame.addtoviewc("social", social_view);
      	social_view.setBounds(menu.getWidth(), 0, 
      			DEFAULT_WIDTH - menu.getWidth(), DEFAULT_HEIGHT);
      	social_view.load_view();
      	pane.add(social_view);
      	
      	PaintView draw_view = new PaintView(frame);
      	//drawboard
      	frame.addtoviewc("draw", draw_view);
      	draw_view.setBounds(menu.getWidth(), 0, 
      			DEFAULT_WIDTH - menu.getWidth(), DEFAULT_HEIGHT);
      	pane.add(draw_view);
      	
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public void createAndShowGUI() {
        //Create and set up the window.
        frame = new WindowFrame("Aton");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        //Set up the content pane.
        addComponentsToPane(frame.getContentPane());

        //Size and display the window.
        //Insets insets = frame.getInsets();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	MainBoard board = new MainBoard();
                board.createAndShowGUI();
            }
        });
    }
    
}