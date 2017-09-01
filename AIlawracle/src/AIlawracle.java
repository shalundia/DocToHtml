/*******************************************************
 * @author Zuoshan
 * @date 01/09/2017
 * A swing tool to transform docx to html,
 * search keyword in html and then italic the keyword.
 * Finally transform html back to docx.
 * 
 *******************************************************/

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

public class AIlawracle {
	
	private JPanel panel;
	private JButton setButton,setButton1,setButton2,setButton3;
	private JTextArea txtArea;
	private JScrollPane scrollPane;
	private ProcessFile pf;
	private JFrame frame ;
	private JEditorPane htmlEditor;
	
	public AIlawracle() {
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		frame = new JFrame("AI Lawracle");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//display in the middle of screen
		frame.setSize(800, 600);
		int windowWidth = frame.getWidth(); 
		int windowHeight = frame.getHeight(); 
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize(); 
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height; 
		frame.setLocation(screenWidth/2-windowWidth/2, screenHeight/2-windowHeight/2);

		panel = new JPanel();
		frame.add(panel);
		//Button
		JButton fileChooseButton = new JButton("Choose Input File");
		fileChooseButton.addActionListener(new OpenDiretor());

		setButton = new JButton("Formulation");
		setButton.setEnabled(false);
		setButton.setActionCommand("OK");
		setButton.addActionListener(new Formulation());		

		setButton1 = new JButton("Legislation");
		setButton1.setEnabled(false);
		setButton1.setActionCommand("OK");
		setButton1.addActionListener(new Legislation());	

		setButton2 = new JButton("space");
		setButton2.setEnabled(false);
		setButton2.setActionCommand("OK");
		setButton2.addActionListener(new Space());	
		
		setButton3 = new JButton("Save");
		setButton3.setEnabled(false);
		setButton3.setActionCommand("OK");
		setButton3.addActionListener(new Save());	
		
		
		htmlEditor = new JEditorPane();
		htmlEditor.setPreferredSize(new Dimension(700, 500));
		
		/*
		txtArea= new JTextArea(27,60);
		txtArea.setSize(800,500);
	    txtArea.setWrapStyleWord(true);  
	    txtArea.setLineWrap(true);*/ 
	    JScrollPane scrollPane = new JScrollPane(htmlEditor);  
	    
	    panel.add(scrollPane);
		//panel.add(htmlEditor);
		panel.add(fileChooseButton);
		panel.add(setButton);
		panel.add(setButton1);
		panel.add(setButton2);
		panel.add(setButton3);

		frame.setVisible(true);
		this.pf = new ProcessFile();
	}
	
	class OpenDiretor implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			//show choose dialog
			JFileChooser jfc = new JFileChooser();
			jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			jfc.showDialog(new JLabel(), "Choose");
			File file = jfc.getSelectedFile();
			jfc.getSelectedFile().getAbsolutePath();
			
			if(file.isDirectory()) {
				System.out.println("Directory:"+file.getAbsolutePath());
			}else if(file.isFile()) {
				System.out.println("file:"+file.getAbsolutePath());
			}
			System.out.println(jfc.getSelectedFile().getAbsolutePath());
			
			pf = new ProcessFile(jfc.getSelectedFile().getAbsolutePath());
			
			
			setButton.setEnabled(true);
			setButton1.setEnabled(true);
			setButton2.setEnabled(true);
			setButton3.setEnabled(true);
			
			try {
				htmlEditor.setPage(pf.getHtmlPath());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		}
	}
	
	class Legislation implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			ArrayList<Keyword> textStore = new ArrayList<Keyword>();
			textStore = pf.Legislation();
			String msg="";
			for(Keyword x : textStore) {
				msg+=x.getText()+"\n";
			}
			try {
				htmlEditor.setPage(pf.getHtmlPath());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			JOptionPane.showMessageDialog(null, msg, "Legislations", JOptionPane.PLAIN_MESSAGE);      
		}
	}
	class Formulation implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			ArrayList<Keyword> textStore = new ArrayList<Keyword>();
			textStore = pf.FindV();
			String msg="";
			for(Keyword x : textStore) {
				msg+=x.getText()+"\n";
			}
			pf.Italic(textStore);
			try {
				htmlEditor.setPage(pf.getHtmlPath());
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			JOptionPane.showMessageDialog(null, msg, "FindV", JOptionPane.PLAIN_MESSAGE);      
		}
	}
	
	class Space implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			ArrayList<Keyword> textStore = new ArrayList<Keyword>();
			textStore = pf.Legislation();
			String msg="";
			for(Keyword x : textStore) {
				msg+=x.getText()+"\n";
			}
			try {
				htmlEditor.setPage(pf.getHtmlPath());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			JOptionPane.showMessageDialog(null, msg, "Space", JOptionPane.PLAIN_MESSAGE);      
		}
	}
	
	class Save implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			pf.SaveOutput();
		}
	}

	
	public static void main(String[] args) {
		new AIlawracle();
	}
}
