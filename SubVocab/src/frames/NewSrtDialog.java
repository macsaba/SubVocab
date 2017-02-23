/**
  * On this dialog, we can set the properties of creating a new srt file.
  * 
  * @name NewSrtDialog
  * @author Csaba Marosi
  */
package frames;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListCellRenderer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class NewSrtDialog extends JDialog {
	
	private static final long serialVersionUID = -5635229719027597098L;
	private static final String nodeName = "colorsNode";
	private JPanel buttonPane;
	private JTextField textFieldExample;
	private JTextField textFieldFormat1;
	private JTextField textFieldFormat2;
	private ColorItem[] colors={new ColorItem("default", Color.black),
								new ColorItem(Color.black),
								new ColorItem(Color.white), 
								new ColorItem(Color.red), 
								new ColorItem(Color.blue), 
								new ColorItem(Color.green)};
	private Map<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();
	private JCheckBox checkDefault = new JCheckBox("Use default settings");
	private JCheckBox checkBold = new JCheckBox("Bold");
	private JCheckBox checkItalic = new JCheckBox("Italic");
	private JCheckBox checkUnderlined = new JCheckBox("Underlined");
	private JComboBox<ColorItem> comboBoxColor;
	private JButton btnSave = new JButton("...");
	private boolean isOk = false;
	private JTextField textFieldSave;
	private String path = "";
	private String fileName = "";
	private Preferences prefs = Preferences.userNodeForPackage(frames.NewSrtDialog.class).node(nodeName);
	
	public NewSrtDialog(String _path, String _name) {
		this.path = _path;
		this.fileName = _name;
		setColorsFromPref();
		comboBoxColor = new JComboBox<ColorItem>(colors);
		/*initialize*/
		
		getContentPane().setFont(new Font("Arial", Font.PLAIN, 12));
		setBounds(100, 100, 306, 454);
		setResizable(false);
		/*Ok and Cancel buttons, and their event handlers.*/
		{
			buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
						isOk = true;
						saveColorsToPref();
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
						saveColorsToPref();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
		
		/*labels, separators*/
		
		JLabel lblExample = new JLabel("Example:");
		lblExample.setFont(new Font("Arial", Font.PLAIN, 12));
		
		JLabel lblColor = new JLabel("Color:");
		lblColor.setEnabled(false);
		lblColor.setFont(new Font("Arial", Font.PLAIN, 12));
		
		JLabel lblSaveTo = new JLabel("Save to:");
		lblSaveTo.setFont(new Font("Arial", Font.PLAIN, 12));
		
		JLabel lblFormat = new JLabel("Format:");
		
		JLabel lblDefinition = new JLabel("+ definition +");
		
		JSeparator separator1 = new JSeparator();
		JSeparator separator2 = new JSeparator();
		
		/*Buttons*/
		
		JButton btnSetRgb = new JButton("Set RGB");
		
		/*textfields*/
		
		textFieldExample = new JTextField();
		textFieldExample.setEditable(false);
		textFieldExample.setFont(new Font("Arial", Font.PLAIN, 12));
		textFieldExample.setColumns(10);
		textFieldExample.setText("(= definition )");
		map.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
		map.put(TextAttribute.POSTURE, TextAttribute.POSTURE_REGULAR);
		map.put(TextAttribute.UNDERLINE, -1 );		//-1 means no underline
		map.put(TextAttribute.SIZE, 12);
		textFieldExample.setFont(textFieldExample.getFont().deriveFont(map));
		
		textFieldFormat1 = new JTextField();
		textFieldFormat1.setFont(new Font("Arial", Font.PLAIN, 12));
		textFieldFormat1.setColumns(10);
		textFieldFormat1.setText("(= ");
		
		textFieldFormat2 = new JTextField();
		textFieldFormat2.setFont(new Font("Arial", Font.PLAIN, 12));
		textFieldFormat2.setColumns(10);
		textFieldFormat2.setText(" )");
		
		textFieldSave = new JTextField();
		textFieldSave.setFont(new Font("Arial", Font.PLAIN, 12));
		textFieldSave.setColumns(10);
		textFieldSave.setText(path + "\\" + fileName);
		
		
		textFieldFormat1.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				textFieldExample.setText(textFieldFormat1.getText() + "definition"
						+ textFieldFormat2.getText());
			}			
			@Override
			public void insertUpdate(DocumentEvent e) {
				textFieldExample.setText(textFieldFormat1.getText() + "definition"
						+ textFieldFormat2.getText());				
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				textFieldExample.setText(textFieldFormat1.getText() + "definition"
						+ textFieldFormat2.getText());				
			}
		});
		textFieldFormat2.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				textFieldExample.setText(textFieldFormat1.getText() + "definition"
						+ textFieldFormat2.getText());
			}			
			@Override
			public void insertUpdate(DocumentEvent e) {
				textFieldExample.setText(textFieldFormat1.getText() + "definition"
						+ textFieldFormat2.getText());				
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				textFieldExample.setText(textFieldFormat1.getText() + "definition"
						+ textFieldFormat2.getText());				
			}
		});
				
		/*checkBoxes*/
		/*
		 *Makes all settings disabled, sets format to (=definition), 
		 */
		
		checkDefault.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean isEnabled = !checkDefault.isSelected();
				checkBold.setEnabled(isEnabled);
				checkItalic.setEnabled(isEnabled);
				btnSetRgb.setEnabled(isEnabled);
				checkUnderlined.setEnabled(isEnabled);
				comboBoxColor.setEnabled(isEnabled);
				lblColor.setEnabled(isEnabled);
				
				if(checkDefault.isSelected()){
					textFieldExample.setText("(= definition )");
					map.replace(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
					map.replace(TextAttribute.POSTURE, TextAttribute.POSTURE_REGULAR);
					map.replace(TextAttribute.UNDERLINE, -1 );		//-1 means no underline
					map.replace(TextAttribute.SIZE, 12);
					textFieldExample.setForeground(new Color(0, 0, 0));
					textFieldExample.setFont(textFieldExample.getFont().deriveFont(map));					
				}else{
					if(checkBold.isSelected()){
						map.replace(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
					}else{
						map.replace(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
					}
					if(checkItalic.isSelected()){
						map.replace(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
					}else{
						map.replace(TextAttribute.POSTURE, TextAttribute.POSTURE_REGULAR);
					}
					if(checkUnderlined.isSelected()){
						map.replace(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
					}else{
						map.replace(TextAttribute.UNDERLINE, -1 );		//-1 means no underline
					}

					textFieldExample.setForeground(((ColorItem) comboBoxColor.getSelectedItem()).getColor());
					textFieldExample.setFont(textFieldExample.getFont().deriveFont(map));
					
				}
			}
		});
		checkDefault.setFont(new Font("Arial", Font.PLAIN, 12));
		checkDefault.setSelected(true);		
		
		//Bold
		
		checkBold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(checkBold.isSelected()){
					map.replace(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
					textFieldExample.setFont(textFieldExample.getFont().deriveFont(map));
				}else{
					map.replace(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
					textFieldExample.setFont(textFieldExample.getFont().deriveFont(map));
				}
			}
		});
		checkBold.setEnabled(false);
		checkBold.setFont(new Font("Arial", Font.BOLD, 12));
		
		//Italic
		
		checkItalic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(checkItalic.isSelected()){
					map.replace(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
					textFieldExample.setFont(textFieldExample.getFont().deriveFont(map));
				}else{
					map.replace(TextAttribute.POSTURE, TextAttribute.POSTURE_REGULAR);
					textFieldExample.setFont(textFieldExample.getFont().deriveFont(map));
				}
			}
		});
		checkItalic.setEnabled(false);
		checkItalic.setFont(new Font("Arial", Font.ITALIC, 12));
		
		//underlined
		
		checkUnderlined.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(checkUnderlined.isSelected()){
					map.replace(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
					textFieldExample.setFont(textFieldExample.getFont().deriveFont(map));
				}else{
					map.replace(TextAttribute.UNDERLINE, -1 );		//-1 means no underline
					textFieldExample.setFont(textFieldExample.getFont().deriveFont(map));
				}
			}
		});
		checkUnderlined.setEnabled(false);
		checkUnderlined.setFont(new Font("Arial", Font.PLAIN, 12));
		
		/*others*/
		
		//combobox
		
		comboBoxColor.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				textFieldExample.setForeground(((ColorItem) comboBoxColor.getSelectedItem()).getColor());
				comboBoxColor.setForeground(((ColorItem) comboBoxColor.getSelectedItem()).getInvertedColor());
			}
		});
		comboBoxColor.setForeground(Color.WHITE) ;
		comboBoxColor.setEnabled(false);
		comboBoxColor.setFont(new Font("Arial", Font.BOLD, 12));
		comboBoxColor.setMaximumRowCount(5);
		comboBoxColor.setPreferredSize(new Dimension(50,20));
		comboBoxColor.setRenderer(new MyCellRenderer());
		
		//new Color
		
		btnSetRgb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					RGBDialog dialog = new RGBDialog();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
					dialog.addWindowListener(new WindowAdapter() {
						public void windowClosed(WindowEvent e)
						  {
							if(dialog.isOk()){
								ColorItem item = new ColorItem(dialog.getColor());
								comboBoxColor.addItem(item);
								comboBoxColor.setSelectedItem(item);
							}
						  }
					});				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnSetRgb.setEnabled(false);
		btnSetRgb.setFont(new Font("Arial", Font.PLAIN, 12));
		
		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser = new JFileChooser(path);

			    FileNameExtensionFilter filter = new FileNameExtensionFilter("*.srt", "SubRip");
			    chooser.setFileFilter(filter);
			    int returnVal = chooser.showSaveDialog(null);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	path = chooser.getSelectedFile().getParent();
			    	fileName = chooser.getSelectedFile().getName();
			    	System.out.println("asdasdfasdfasdfas");
			    	if(!fileName.endsWith(".srt")){
			    		StringTokenizer stk = new StringTokenizer(fileName, ".");
			    		fileName = stk.nextToken()+".srt";
			    	}				    	
			    }
				
			}
		});
		btnSave.setFont(new Font("Arial", Font.PLAIN, 12));
		
		/*layout*/
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(checkDefault)
						.addComponent(lblFormat)
						.addComponent(textFieldFormat1, GroupLayout.PREFERRED_SIZE, 280, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblDefinition)
						.addComponent(textFieldFormat2, GroupLayout.PREFERRED_SIZE, 280, GroupLayout.PREFERRED_SIZE)
						.addComponent(textFieldExample)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(lblColor)
								.addComponent(comboBoxColor, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnSetRgb, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE))
							.addGap(35)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(checkBold)
								.addComponent(checkUnderlined)
								.addComponent(checkItalic)))
						.addComponent(separator1, GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
						.addComponent(lblExample)
						.addComponent(separator2, GroupLayout.PREFERRED_SIZE, 280, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblSaveTo)
						.addComponent(buttonPane, GroupLayout.PREFERRED_SIZE, 280, GroupLayout.PREFERRED_SIZE)
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(textFieldSave, GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnSave)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblExample)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textFieldExample, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(checkDefault)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblColor)
						.addComponent(checkBold))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(comboBoxColor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSetRgb)
							.addGap(18)
							.addComponent(lblFormat))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(checkItalic)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(checkUnderlined)))
					.addGap(9)
					.addComponent(textFieldFormat1, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblDefinition)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textFieldFormat2, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(separator2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblSaveTo)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textFieldSave, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSave))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(buttonPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(34))
		);
		getContentPane().setLayout(groupLayout);
	}
	
	/**
	 * ColorItem is a class to store an item in the color list. It can have a special name
	 * or default the name, which is the hex code of the color.
	 */
	
	class ColorItem{
		
		private String name;
		private Color color;
		private String hex;
		
		/*If the constructor doesn't have a name argument, it will be the hex value of the color.
		 * You can set the color: 	- with a String value, which represents the hex value of the color
		 * 							- with 3 integers which represent the Red, Green, Blue component of the color
		 * 							- with Color object */
		public ColorItem(String h){
			hex = h;
			name = "#" + hex;
			
			int r = Integer.parseInt(h.substring(0,2), 16);
			int g = Integer.parseInt(h.substring(2,4), 16);
			int b = Integer.parseInt(h.substring(4,6), 16);
			color = new Color(r, g, b);
		}
		public ColorItem(String name, String h){
			hex = h;
			this.name = name;
			
			int r = Integer.parseInt(h.substring(0,2), 16);
			int g = Integer.parseInt(h.substring(2,4), 16);
			int b = Integer.parseInt(h.substring(4,6), 16);
			color = new Color(r, g, b);
		}
		
		public ColorItem(String name, int r, int g, int b){
			color = new Color(r, g, b);
			this.name = name;

			String red =  Integer.toHexString(r);
			String green =  Integer.toHexString(g);
			String blue =  Integer.toHexString(b);
			red = red.length() > 1 ? red : "0" + red;
			green = green.length() > 1 ? green : "0" + green;
			blue = blue.length() > 1 ? blue : "0" + blue;
			
			hex = red + green + blue;
		}
		public ColorItem(String name, Color c){
			color = c;
			this.name = name;
			
			int r = c.getRed();
			int b = c.getBlue();
			int g = c.getGreen();
			String red =  Integer.toHexString(r);
			String green =  Integer.toHexString(g);
			String blue =  Integer.toHexString(b);
			red = red.length() > 1 ? red : "0" + red;
			green = green.length() > 1 ? green : "0" + green;
			blue = blue.length() > 1 ? blue : "0" + blue;
			
			hex = red + green + blue;
		}
		public ColorItem(int r, int g, int b){
			color = new Color(r, g, b);
			String red =  Integer.toHexString(r);
			String green =  Integer.toHexString(g);
			String blue =  Integer.toHexString(b);
			red = red.length() > 1 ? red : "0" + red;
			green = green.length() > 1 ? green : "0" + green;
			blue = blue.length() > 1 ? blue : "0" + blue;
			
			hex = red + green + blue;
			name = "#" + hex;
		}
		public ColorItem(Color c){
			color = c;
			int r = c.getRed();
			int b = c.getBlue();
			int g = c.getGreen();
			String red =  Integer.toHexString(r);
			String green =  Integer.toHexString(g);
			String blue =  Integer.toHexString(b);
			red = red.length() > 1 ? red : "0" + red;
			green = green.length() > 1 ? green : "0" + green;
			blue = blue.length() > 1 ? blue : "0" + blue;
			
			hex = red + green + blue;
			name = "#" + hex;
		} 
		
		public String getName(){
			return name;
		}
		
		public Color getColor(){
			return color;
		}
		public String getHex(){
			return hex;
		}
		
		/*returns the inverse of this color*/
		
		public Color getInvertedColor(){
			int neg = 0xFFFFFF - Integer.parseInt(hex, 16);
			return (new Color(neg));
		}
	}
	
	/*Saves the colors, created by the user, to the Pref*/

	public void saveColorsToPref(){
		try {
			prefs.clear();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		ColorItem c;
		for(int i = 0; i < comboBoxColor.getItemCount(); i++){
			c = comboBoxColor.getItemAt(i);
			prefs.put(c.getName(), c.getHex());
		}
	}
	
	/*Sets the colors, created by the user, from the Pref*/
	
	public void setColorsFromPref(){
		try {
			String[] keys = prefs.keys();
			if(keys.length != 0){
				colors = new ColorItem[keys.length];
				int i = 0;
				for(String key : keys){
						colors[i] = new ColorItem(key, prefs.get(key, ""));
						i++;
				}
			}
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}		
	}
	
	/*This class renders the ComboBox's celss. (Sets the colors and names.)*/
	class MyCellRenderer extends JButton implements ListCellRenderer<ColorItem> {  
	    
		private static final long serialVersionUID = -7720291097058050741L;
		public MyCellRenderer() {  
	         setOpaque(true); 

	     }
	     boolean b=false;
	    @Override
	    public void setBackground(Color bg) {
	         if(!b)
	         {
	             return;
	         }

	        super.setBackground(bg);
	    }
	     public Component getListCellRendererComponent(  
	         JList<? extends ColorItem> list,  
	         ColorItem value,  
	         int index,  

	         boolean isSelected,  
	         boolean cellHasFocus)  
	     {  

	         b=true;
	         setText(((ColorItem)value).getName());
	         setBackground(((ColorItem)value).getColor());
	         setFont(new Font("Ariel", Font.BOLD, 12));
	         setForeground(((ColorItem)value).getInvertedColor());
	         setHorizontalAlignment(RIGHT);

	         b=false;
	         return this;  
	     }
	
	}
	
	/*Concats and returns the starting, and the ending strings. (Bold, Italic, color, etc. tags)*/
	public String[] getTags(){
		String[] s =  {textFieldFormat1.getText(), textFieldFormat2.getText()};
		if(checkDefault.isSelected()){
			return s;
		}
		else{
			if(checkBold.isSelected()){
				s[0] = "<b>" + s[0];
				s[1] = s[1] + "</b>";
			}
			if(checkItalic.isSelected()){
				s[0] = "<i>" + s[0];
				s[1] = s[1] + "</i>";
			}
			if(checkUnderlined.isSelected()){
				s[0] = "<u>" + s[0];
				s[1] = s[1] + "</u>";
			}
			if(!((ColorItem) comboBoxColor.getSelectedItem()).getName().equals("default")){
				s[0] = "<font color=\"" + ((ColorItem)comboBoxColor.getSelectedItem()).getName() + "\">" + s[0];
				s[1] = s[1] + "</font>";
			}
			return s;
		}
	}
	public boolean getIsOk(){
		return isOk;
	}
	
	public String getFileName(){
		return fileName;
	}
	
	public String getPath(){
		return path;
	}
}
