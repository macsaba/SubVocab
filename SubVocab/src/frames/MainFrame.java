/**
  * This is the main frame of the application.
  * 
  * @name MainFrame
  * @date 25.01.2016
  * @author Csaba Marosi
  */
package frames;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import subvocab.SubVocab;


public class MainFrame {
	public JFrame frame;
	
	/*GUI elements, requests event handle*/
		
	public static JTextField textTerm = new JTextField();
	public static JTextField textContext = new JTextField();
	public static JTextField textDefinition = new JTextField();
	public static JTextField textSource = new JTextField();
	public static JTextField textDataBase = new JTextField();
	public static JTextField textFieldCountAskedTerm = new JTextField();
	public static JTextField textFieldCountAllTerms = new JTextField();
	public static JButton btnIKnow = new JButton("I know");
	public static JButton btnSave = new JButton("Save");
	public static JMenuItem mntmOpenSource = new JMenuItem("Open source...");
	public static JMenuItem mntmCreateSrt = new JMenuItem("Create new SubRip (*.srt)");
	public static JMenuItem mntmSaveToDatabase = new JMenuItem("Save to database");
	public static JMenuItem mntmNewDatabase = new JMenuItem("Create new database");
	public static JMenuItem mntmChangeDatabase = new JMenuItem("Change database");
	public static JMenuItem mntmAbout = new JMenuItem("Help");
	public static JMenuItem mntmPortToQuizlet = new JMenuItem("Port to Quizlet");
	public MainFrame(){
		initialize();
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 721, 505);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		/*This listener checks, if the program save before exit.*/
		
		WindowListener exitListener = new WindowAdapter() {
			
		    @Override
		    public void windowClosing(WindowEvent e) {
	
		    	SubVocab.prefs.put("PATH_FILE", SubVocab.pathSourceFile);
		    	SubVocab.prefs.put("FILE_NAME", SubVocab.nameSourceFile);
		    	SubVocab.prefs.put("PATH_DB", SubVocab.pathDb);
		    	SubVocab.prefs.put("DB_NAME", SubVocab.nameDb);
		    	
		    	if(!SubVocab.currentTerms.isEmpty()){			//ha van mit menteni
			        int confirm = JOptionPane.showOptionDialog(
			             null, "Do you want to save before exit?", 
			             "Exit Confirmation", JOptionPane.YES_NO_CANCEL_OPTION,
			             JOptionPane.QUESTION_MESSAGE, null, null, null);
			        if (confirm == 0) {			//Ha yes
			        	SubVocab.saveToDb(SubVocab.currentTerms, SubVocab.pathDb+"/"+SubVocab.nameDb);
			        	System.exit(0);
			        }else if (confirm == 1){	//Ha no
			            System.exit(0);
			        }
		    	}else{
		    		System.exit(0);
		    	}
		    }
		};
		frame.addWindowListener(exitListener);
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowOpened( WindowEvent e ){
		        textDefinition.requestFocus();
		    }
		});
		
		/*Frame elements*/
		
		textTerm.setFont(new Font("Arial", Font.BOLD, 36));
		textTerm.setHorizontalAlignment(JTextField.CENTER);
		textTerm.setColumns(10);
		
		textDefinition.setFont(new Font("Arial", Font.PLAIN, 18));
		textDefinition.setHorizontalAlignment(JTextField.CENTER);
		textDefinition.setColumns(10);
		
		textContext.setEditable(false);
		textContext.setFont(new Font("Arial", Font.ITALIC, 18));
		textContext.setHorizontalAlignment(JTextField.CENTER);
		textContext.setColumns(10);
		
		textSource.setFont(new Font("Arial", Font.PLAIN, 12));
		textSource.setEditable(false);
		textSource.setColumns(10);
		
		textDataBase.setFont(new Font("Arial", Font.PLAIN, 12));
		textDataBase.setEditable(false);
		textDataBase.setColumns(10);
		
		textFieldCountAskedTerm.setEditable(false);
		textFieldCountAskedTerm.setFont(new Font("Arial", Font.PLAIN, 12));
		textFieldCountAskedTerm.setColumns(10);
		
		textFieldCountAllTerms.setEditable(false);
		textFieldCountAllTerms.setFont(new Font("Arial", Font.PLAIN, 12));
		textFieldCountAllTerms.setColumns(10);
	
		JLabel lblTerm = new JLabel("Term:");
		lblTerm.setFont(new Font("Arial", Font.PLAIN, 12));
		
		JLabel lblDefinition = new JLabel("Definition:");
		lblDefinition.setFont(new Font("Arial", Font.PLAIN, 12));
		
		JLabel lblContext = new JLabel("Context:");
		lblContext.setFont(new Font("Arial", Font.PLAIN, 12));
		

		JLabel lblSource = new JLabel("Source:");
		lblSource.setFont(new Font("Arial", Font.PLAIN, 12));
		
		JLabel lblDatabase = new JLabel("Database:");
		lblDatabase.setFont(new Font("Arial", Font.PLAIN, 12));

		JLabel lblAsked = new JLabel("Asked:");
		lblAsked.setFont(new Font("Arial", Font.PLAIN, 12));
		
		JLabel labelPer = new JLabel("/");
		labelPer.setFont(new Font("Arial", Font.PLAIN, 12));
		
		JSeparator separator = new JSeparator();
	
		
		btnIKnow.setForeground(new Color(255, 255, 255));
		btnIKnow.setFont(new Font("Arial", Font.BOLD, 15));
		btnIKnow.setBackground(new Color(0, 102, 255));
		btnIKnow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
		        textDefinition.requestFocus();

			}
		});
		
		btnSave.setForeground(new Color(255, 255, 255));
		btnSave.setFont(new Font("Arial", Font.BOLD, 15));
		btnSave.setBackground(new Color(0, 102, 255));
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
		        textDefinition.requestFocus();
			}
		});
	
		/*Create layout*/
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(textContext, GroupLayout.DEFAULT_SIZE, 680, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(108)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblTerm)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(textTerm, GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
									.addGap(111))))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(textDefinition, GroupLayout.DEFAULT_SIZE, 680, Short.MAX_VALUE)
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addGroup(groupLayout.createSequentialGroup()
											.addComponent(btnIKnow, GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
											.addGap(27))
										.addGroup(groupLayout.createSequentialGroup()
											.addComponent(lblSource)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(textSource, GroupLayout.PREFERRED_SIZE, 268, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.RELATED)))
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(btnSave, GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
										.addGroup(groupLayout.createSequentialGroup()
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(lblDatabase)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(textDataBase, GroupLayout.PREFERRED_SIZE, 276, GroupLayout.PREFERRED_SIZE))))))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblContext))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(separator, GroupLayout.PREFERRED_SIZE, 680, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblDefinition)
					.addPreferredGap(ComponentPlacement.RELATED, 414, Short.MAX_VALUE)
					.addComponent(lblAsked)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textFieldCountAskedTerm, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(labelPer)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textFieldCountAllTerms, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
					.addGap(19))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(18)
					.addComponent(lblTerm)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(textTerm, GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
					.addGap(29)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDefinition)
						.addComponent(lblAsked)
						.addComponent(textFieldCountAskedTerm, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(labelPer)
						.addComponent(textFieldCountAllTerms, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textDefinition, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
					.addGap(12)
					.addComponent(lblContext)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textContext, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(btnSave, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnIKnow, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addGap(30)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 3, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblSource)
							.addComponent(textSource, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblDatabase)
							.addComponent(textDataBase, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)))
					.addGap(17))
		);
		frame.getContentPane().setLayout(groupLayout);
		
		/*menu*/
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(new Color(204, 204, 204));
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		mnFile.setBackground(new Color(204, 204, 204));
		menuBar.add(mnFile);
		
		JMenu mnDatabase = new JMenu("Database");
		mnDatabase.setBackground(new Color(204, 204, 204));
		menuBar.add(mnDatabase);
		
		JMenu mnHelp = new JMenu("Help");
		mnHelp.setBackground(new Color(204, 204, 204));
		menuBar.add(mnHelp);
		
		mnFile.add(mntmOpenSource);
		mnFile.add(mntmSaveToDatabase);
		mnFile.add(mntmCreateSrt);
		
		JSeparator separator_1 = new JSeparator();
		mnFile.add(separator_1);
		
		mnFile.add(mntmPortToQuizlet);
		mnDatabase.add(mntmNewDatabase);
		mnDatabase.add(mntmChangeDatabase);
		mnHelp.add(mntmAbout);
		
		}
}
