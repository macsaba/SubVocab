/**
  * This class represents a term, with its properties.
  * 
  * @name SubVocab
  * @author Csaba Marosi
  */
package subvocab;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import frames.MainFrame;
import frames.NewSrtDialog;

public class SubVocab {
	
	
	private static String sub = "";											//The subtitle without any format
	private static int indexOfTerm = 0;										//The term's index, which is on the screen
	private static final String IO_DATES = "IODates";
	public static ArrayList<Term> currentTerms = new ArrayList<>();			
	private static ArrayList<Term> newSrtTerms = new ArrayList<Term>();
	private static ArrayList<Term> allTerms = new ArrayList<>();			
	private static ArrayList<String> subParts = new ArrayList<>();			//every string margined with space(ex.: This is the file. subParts := {"This", "is", "the", "file."})
	private static ArrayList<Integer> wordsIndex = new ArrayList<>();		//the positions of the words in 'words' ArrayList 
	private static ArrayList<String> words = new ArrayList<>();				//contains the new words
	private static ArrayList<String> knownWords = new ArrayList<>();		//contains the known words
	private static ArrayList<String> lines = new ArrayList<String>();
	private static ArrayList<TextBlock> blocks = new ArrayList<TextBlock>();
	private static final String PATTERN = "\\d\\d:\\d\\d:\\d\\d,\\d\\d\\d";	//the pattern of .srt file's time stap
	public static String pathDb = ""; 									
	public static String nameDb = "";
	public static String pathSourceFile = "";
	public static String nameSourceFile = "";
	private static String filmTag = "";
	public static Preferences prefs = Preferences.userNodeForPackage(subvocab.SubVocab.class).node(IO_DATES);
	
    public static void main(String[] args) throws SQLException, BackingStoreException {
    	    	
    	//prefs.clear();
    	/*load the saved variables from pref*/
    	pathSourceFile = prefs.get("PATH_FILE", "");
    	nameSourceFile = prefs.get("FILE_NAME", "");
    	pathDb = prefs.get("PATH_DB", "");
    	nameDb = prefs.get("DB_NAME", "");
    	filmTag = nameSourceFile;
    	
    	
    	
		/*query database*/

    	if(pathDb.equals("") || nameDb.equals("")){
    		try {
    			nameDb = "db1.accdb";
    			pathDb = new File(".").getCanonicalPath();
				prefs.put("PATH_DB", pathDb);
		    	prefs.put("DB_NAME", nameDb);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
    	}

		File f = new File(pathDb+"/"+nameDb);
		if(!f.exists()){
			createDb(nameDb, pathDb);
		}

    
		knownWords = queryDb(pathDb+"/"+nameDb);
		
		/*read subtitle*/
		
		if(pathSourceFile.equals("") || nameSourceFile.equals("")){
			JFileChooser chooser = new JFileChooser("");
		    FileNameExtensionFilter filter = new FileNameExtensionFilter("*.srt", "srt");
		    chooser.setFileFilter(filter);
		    int returnVal = chooser.showOpenDialog(null);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
	            pathSourceFile = chooser.getSelectedFile().getParent();
	            nameSourceFile = chooser.getSelectedFile().getName();
	            filmTag = nameSourceFile;
		    }else{
		    	System.exit(0);
		    }		
		}
		
		/*set the source of the file and db, to the textfields on the main frame*/
		
		MainFrame.textDataBase.setText(pathDb + "\\" + nameDb);
		MainFrame.textSource.setText(pathSourceFile + "\\" + nameSourceFile);
		
		sub = readFile(pathSourceFile+"\\"+nameSourceFile);
		lines = readFileLines(pathSourceFile+"\\"+nameSourceFile);
		lines.set(0, "1");
		
		/*create blocks from the read text*/
		
		blocks.add(new TextBlock());
		blocks.get(0).setStartIndex(0);
		int startIndex = 0;
		
		for(int i = 0, k = 0; i < lines.size() - 1; i++){
			if(lines.get(i + 1).matches(PATTERN + " --> " + PATTERN)){
				startIndex +=blocks.get(k).size(); 
				blocks.add(new TextBlock());
				k++;
				blocks.get(k).setStartIndex(startIndex);
				blocks.get(k).addLine(lines.get(i));
			}else{
				blocks.get(k).addLine(lines.get(i));
			}
		}
		
		/*release sub to words*/
		
		getWords(sub);
				
		/*GUI*/
		MainFrame window = new MainFrame();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		MainFrame.textTerm.setText(words.get(indexOfTerm));
		MainFrame.textContext.setText(makeContextOf(indexOfTerm));
		indexOfTerm++;
		MainFrame.textFieldCountAskedTerm.setText("1");
		MainFrame.textFieldCountAllTerms.setText(Integer.toString(words.size()));
		
		/*Events*/
		
		MainFrame.btnIKnow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(words.size() <= indexOfTerm){
					JOptionPane.showMessageDialog(null,
						    "There is no other term left.");
				}else{
					Term t = new Term(MainFrame.textTerm.getText(), MainFrame.textDefinition.getText(), false, filmTag, 
							wordsIndex.get(indexOfTerm - 1));
					currentTerms.add(t);
					currentTerms.addAll(t.getAltTerms());
					newSrtTerms.add(t);
					MainFrame.textDefinition.setText("");
					MainFrame.textTerm.setText(words.get(indexOfTerm));	
					MainFrame.textContext.setText(makeContextOf(indexOfTerm));
					indexOfTerm++;
					MainFrame.textFieldCountAskedTerm.setText(Integer.toString(indexOfTerm));
				}
			}
		});
		
		MainFrame.btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(words.size() <= indexOfTerm){
					JOptionPane.showMessageDialog(null,
						    "There is no other term left.");					
				}else{
					currentTerms.add(new Term(MainFrame.textTerm.getText(), MainFrame.textDefinition.getText(), true, filmTag, 
							wordsIndex.get(indexOfTerm - 1)));
					newSrtTerms.add(new Term(MainFrame.textTerm.getText(), MainFrame.textDefinition.getText(), true, filmTag,
							wordsIndex.get(indexOfTerm - 1)));
					MainFrame.textDefinition.setText("");
					MainFrame.textTerm.setText(words.get(indexOfTerm));
					MainFrame.textContext.setText(makeContextOf(indexOfTerm));
					indexOfTerm++;
					MainFrame.textFieldCountAskedTerm.setText(Integer.toString(indexOfTerm));
				}
			}
		});
		MainFrame.textDefinition.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = -5041378929264028265L;
				@Override
				public void actionPerformed(ActionEvent e) {
					if(words.size() <= indexOfTerm){
						JOptionPane.showMessageDialog(null,
							    "There is no other term left.");
					}else{
						Term t = new Term(MainFrame.textTerm.getText(), MainFrame.textDefinition.getText(), false, filmTag,
								wordsIndex.get(indexOfTerm - 1));
						currentTerms.add(t);
						currentTerms.addAll(t.getAltTerms());
						newSrtTerms.add(t);
						MainFrame.textDefinition.setText("");
						MainFrame.textTerm.setText(words.get(indexOfTerm));	
						MainFrame.textContext.setText(makeContextOf(indexOfTerm));
						indexOfTerm++;
						MainFrame.textFieldCountAskedTerm.setText(Integer.toString(indexOfTerm));
					}
				}
			});
		
		/*Menu events*/
		
		MainFrame.mntmSaveToDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(currentTerms.size()!=0){
					saveToDb(currentTerms, pathDb+"/"+nameDb);
				}
			}
		});
		
		MainFrame.mntmOpenSource.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				/*Save*/
				if(!currentTerms.isEmpty()){			
			        int confirm = JOptionPane.showOptionDialog(
			             null, "Do you want to save before you change the source file?", 
			             "Change Confirmation", JOptionPane.YES_NO_OPTION, 
			             JOptionPane.QUESTION_MESSAGE, null, null, null);
			        if (confirm == 0) {			//Ha yes
			        	saveToDb(currentTerms, pathDb+"/"+nameDb);
			        }
		    	}
				
				/*File browser*/
				
				JFileChooser chooser = new JFileChooser();
				if(!pathSourceFile.equals("")){
					chooser = new JFileChooser(pathSourceFile);
				}
			    FileNameExtensionFilter filter = new FileNameExtensionFilter("*.srt", "srt");
			    chooser.setFileFilter(filter);
			    int returnVal = chooser.showOpenDialog(null);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			            pathSourceFile = chooser.getSelectedFile().getParent();
			            nameSourceFile = chooser.getSelectedFile().getName();
			            filmTag = nameSourceFile;
			            /*read subtitle*/
			    		
			    		sub = readFile(pathSourceFile+"\\"+nameSourceFile);
			    		lines = readFileLines(pathSourceFile+"\\"+nameSourceFile);
			    		lines.set(0, "1");
			    		
			    		allTerms.clear();
			    		currentTerms.clear();
			    		
			    		/*release sub to words*/

			    		getWords(sub);

			    		indexOfTerm = 0;
			    		MainFrame.textFieldCountAskedTerm.setText("1");
			    		MainFrame.textFieldCountAllTerms.setText(Integer.toString(words.size()));
			    		MainFrame.textTerm.setText(words.get(indexOfTerm));
			    		MainFrame.textContext.setText(makeContextOf(indexOfTerm));
			    		indexOfTerm++;
			    }
			    
			    /*create blocks again, according to the new file*/
			    
			    blocks.clear();
			    blocks.add(new TextBlock());
				blocks.get(0).setStartIndex(0);
				int startIndex = 0;
				
				for(int i = 0, k = 0; i < lines.size() - 1; i++){
					if(lines.get(i + 1).matches(PATTERN + " --> " + PATTERN)){
						startIndex +=blocks.get(k).size(); 
						blocks.add(new TextBlock());
						k++;
						blocks.get(k).setStartIndex(startIndex);
						blocks.get(k).addLine(lines.get(i));
					}else{
						blocks.get(k).addLine(lines.get(i));
					}
				}

				MainFrame.textSource.setText(pathSourceFile + "\\" + nameSourceFile);				
			}
		});
		
		MainFrame.mntmChangeDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				/*Browser*/
				
				JFileChooser chooser = new JFileChooser();
				if(!pathDb.equals("")){
					chooser = new JFileChooser(pathDb);
				}
			    FileNameExtensionFilter filter = new FileNameExtensionFilter("*.mdb, *.accdb", "mdb", "accdb");
			    chooser.setFileFilter(filter);
			    
			    int returnVal = chooser.showOpenDialog(null);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			            
			    	String prevPath = pathDb;
			    	String prevName = nameDb;
			    	pathDb = chooser.getSelectedFile().getParent();
			        nameDb = chooser.getSelectedFile().getName();
			            
				    prefs.put("PATH_DB", pathDb);
				    prefs.put("DB_NAME", nameDb);
				    
				    try {
						knownWords = queryDb(pathDb+"/"+nameDb);
						getWords(sub);
						indexOfTerm = 0;
						MainFrame.textFieldCountAllTerms.setText(Integer.toString(words.size()));
						MainFrame.textTerm.setText(words.get(indexOfTerm));
						MainFrame.textContext.setText(makeContextOf(indexOfTerm));
			    		indexOfTerm++;

					} catch (SQLException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, "There were some problem in connection the selected database.");
						pathDb = prevPath;
				        nameDb = prevName;
				            
					    prefs.put("PATH_DB", pathDb);
					    prefs.put("DB_NAME", nameDb);
					}
				    
				    	
			    }
			    MainFrame.textDataBase.setText(pathDb + "\\" + nameDb);			    
			}
		});
		
		MainFrame.mntmNewDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				if(!pathDb.equals("")){
					chooser = new JFileChooser(pathDb);
				}
			    FileNameExtensionFilter filter = new FileNameExtensionFilter("*.accdb", "accdb");
			    chooser.setFileFilter(filter);
			    int returnVal = chooser.showSaveDialog(null);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	String path = chooser.getSelectedFile().getParent();
			    	String name = chooser.getSelectedFile().getName();
			    	if(!name.endsWith(".accdb")){
			    		StringTokenizer stk = new StringTokenizer(name, ".");
			    		name = stk.nextToken()+".accdb";
			    	}
			    	createDb(name, path);
				    	
			    }
			}
		});
		
		MainFrame.mntmCreateSrt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					NewSrtDialog dialog = new NewSrtDialog(pathSourceFile, "SubRip_" + nameSourceFile);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
					dialog.addWindowListener(new WindowAdapter() {
						public void windowClosed(WindowEvent e)
						  {
							if(dialog.getIsOk()){
								String[] tags = dialog.getTags();								
								createSrt(dialog.getFileName(), dialog.getPath() + "\\", tags[0], tags[1]);
							}
						  }
					});	
				} catch (Exception t) {
					t.printStackTrace();
				}
			}
		});
		
		MainFrame.mntmPortToQuizlet.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String q = "SELECT * FROM words WHERE toStudy = yes AND filmTag = '" + filmTag + "';";
		    	String path = "";
		    	String name = "";
		    	int[] arr = {2,3};
		    	ArrayList<String> al = new ArrayList<String>();
		    	
		    	/*Browser*/
		    	
				JFileChooser chooser = new JFileChooser(pathSourceFile);
			    FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt", "txt");
			    chooser.setFileFilter(filter);
			    int returnVal = chooser.showSaveDialog(null);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	path = chooser.getSelectedFile().getParent();
			    	name = chooser.getSelectedFile().getName();
			    	if(!name.endsWith(".txt")){
			    		StringTokenizer stk = new StringTokenizer(name, ".");
			    		name = stk.nextToken()+".txt";
			    	}
			    }
			    
			    if(path != "" && name != ""){
				    
				    /*db query*/

					try {
						al = queryDb(pathDb + "\\" + nameDb, q, arr);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					
					/*create file*/
					
					try {
				   		 
						File file = new File(path + "\\" + name);
					    
						if (file.createNewFile()){
							System.out.println("File is created!");
						}else{
							System.out.println("File already exists.");
						} 
						//write file
						PrintWriter pw = new PrintWriter(file);
						for(String line : al){
							pw.println(line);
						}
						
						pw.close();
						
				    }catch (IOException e2) {
				    	e2.printStackTrace();
					}
			    }
			}
		});
	
	}
    
    /**
     * Creates a new srt file, which contains the definitons of the words.
     * @param name the name of the new file
     * @param path the path of the new file
     * @param startTag the func. puts this string before the definition
     * @param endTag the func. puts this string after the definition 
     */
    
    private static void createSrt(String name, String path, String startTag, String endTag){
    	int i = 0;
    	@SuppressWarnings("unchecked")
		ArrayList<TextBlock> blocks2 = (ArrayList<TextBlock>) blocks.clone();
    	
    	//put the definitions to every blocks
		for (Term term : newSrtTerms) {
			if(term.isToStudy() && term.getPosition() != -1){
				for(; term.getPosition() > blocks2.get(i+1).getStartIndex(); i++);
				blocks2.get(i).insertAfter(startTag + term.getDefinition() + endTag, term.getPosition());
			}
		} 
		try {
   		 
			File file = new File(path + "\\" + name);
		    
			if (file.createNewFile()){
				System.out.println("File is created!");
			}else{
				System.out.println("File already exists.");
			} 
			//write file
			PrintWriter pw = new PrintWriter(file);
			String l = "";
			for(TextBlock currentBlock : blocks2){
				while((l = currentBlock.nextLine()) != null){
					pw.println(l);
				}
				currentBlock.seek(0);
			}
			
			pw.close();
			
	    }catch (IOException e) {
	    	e.printStackTrace();
		}

		blocks2.clear();
		newSrtTerms.clear();
		
    }
    
    /**
     * Gets the words from a string. (Remove the special characters, every word only once.)
     */
    
    private static void getWords(String sub){
    	words.clear();
    	subParts.clear();
    	wordsIndex.clear();
    
    	String word = "";								//auxiliary variable
    	int absoluteIndex = 0;							//loop variable
		StringTokenizer stk = new StringTokenizer(sub, " ");
		
		while (stk.hasMoreTokens()) {
			word = stk.nextToken();
			subParts.add(word);
			
			/*remove special characters*/
			
			while(".?!,\"".contains(word.substring(word.length() - 1, word.length())))	
			{
				word = word.substring(0, word.length() - 1);
				if(word.length()==0){
					break;			
				}
			}
						
			if(word.length()!=0)
			{
				while(".?!,\"".contains(word.substring(0, 1)))	
					{
						word = word.substring(0, word.length() - 1);
						if(word.length() == 0){
							break;
						}
					}
			}
			
			/*remove 's */
			
			if(word.length() != 0){
				if(word.endsWith("'s")){
					word = word.substring(0, word.length() - 2);
				}
			}
			
			word = word.toLowerCase();			
			
			/*add to the list*/
			
			if(word.length() != 0)
			{
				if( ( !containsOneOf(word, "0123456789-'<>") )
						&& !words.contains(word) 
						&& !knownWords.contains(word)){ 	/*if it doesn't contain one of the characters above, and doesn't contained by the lists*/ 
	        	 	words.add(word);			//add to the list
	        	 	wordsIndex.add(Integer.valueOf(absoluteIndex));
				}
			}
			absoluteIndex++;
	    }    	
    }
    
    /**
     * Returns true, if s1 contains one of the character of s2.
     */
    public static boolean containsOneOf(String s1, String s2)
	{
		boolean contains = false;
		for(int i = 0; i< s2.length();i++)
		{
			if (s1.contains( Character.toString(s2.charAt(i)) ))
			{
				contains = true;
				break;
			}
		}
		return contains;
	}

    
    /**
     * Reads a file to a string.
     */
	private static String readFile(String path){
		
		String line=""; 
		String text = "";		
		
		try{
			RandomAccessFile raf = new RandomAccessFile(path, "r");
			while((line=raf.readLine())!=null){
				text += line+" ";
			}
			raf.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}
	
	private static ArrayList<String> readFileLines (String path){
		
		ArrayList<String> lines = new ArrayList<String>();
		String line = "";
		
		try{
			RandomAccessFile raf = new RandomAccessFile(path, "r");
			while((line=raf.readLine())!=null){
				lines.add(line);
			}
			raf.close();
		}catch (IOException e) {
		    e.printStackTrace();
        }
		
		return lines;
	}
	
	/*Database query functions*/
	
	private static ArrayList<String> queryDb(String path) throws SQLException{
		ArrayList<String> al = new ArrayList<>();
        Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+path);        
        Statement s = conn.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM Words WHERE toStudy = no;");
        while (rs.next()) {
            al.add(rs.getString(2));
        }
        rs.close();
        s.close();
        conn.close();
		return al;
	}	
	
	private static ArrayList<String>  queryDb (String path, String q, int[] columns) throws SQLException{
		ArrayList<String> al = new ArrayList<String>();
        Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+path);
        Statement s = conn.createStatement();
        ResultSet rs = s.executeQuery(q);
        String str = "";
        
        while (rs.next()) {
        	for(int c : columns){
        		str += rs.getString(c) + ";";
        	}
        	al.add(str.substring(0, str.length() - 1));
        	str = "";
        }
        rs.close();
        s.close();
        conn.close();
		return al;
	}
	
	private static void queryDb (String path, String q){
        Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:ucanaccess://"+path);
			Statement s = conn.createStatement();
	        s.execute(q); 
	        s.close();
	        conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
	}
	
	private static void createDb(String name, String path){
		File database = new File(path+"/"+name);
		String q = "CREATE TABLE words (id Counter, term text, definition text, toStudy YesNo, filmTag text )";
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
		} catch (ClassNotFoundException e1) {
		
			e1.printStackTrace();
		}
		Connection cn;
		try {
			cn = DriverManager.getConnection("jdbc:ucanaccess://" + database.getAbsolutePath()+";newdatabaseversion=V2010");
			Statement s = cn.createStatement();
	        s.execute(q);
			s.close();
			cn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void saveToDb(ArrayList<Term> al,String pathDb){
		String q = "";
				
		for(int i = 0; i<al.size();i++)
		{
			q += al.get(i).toQueryString() +" ";
		}		
		
		queryDb(pathDb, q);
		allTerms.addAll(al);
		al.clear();
	}
	
	/**
	 * Returns a sentence, which contains the word with the given index.
	 */
	
	private static String makeContextOf(int index){
		String context = "";
		int originalIndex = wordsIndex.get(index).intValue();
		int startIndex = 0;
		int stopIndex = 0;
		int j = originalIndex;
		
		/*Look for the end index. Go word by word, until we find a sentence ending punctuation.*/
		
		while(j < subParts.size())
		{
			if (subParts.get(j).endsWith(".") || subParts.get(j).endsWith("\"") || subParts.get(j).endsWith("?") 
				|| subParts.get(j).endsWith("!")){
				stopIndex = j;
				break;
			}
			j++;
		}
		
		j = originalIndex;
		
		/*look for the start index*/
		
		while(j > 0)
		{
			if (subParts.get(j - 1).endsWith(".") || subParts.get(j - 1).endsWith("\"") || subParts.get(j - 1).endsWith("?") 
				|| subParts.get(j - 1).endsWith("!") || subParts.get(j).startsWith("\"")){
				startIndex = j;
				break;
			}
			j--;
		}
		
		/*create the sentece*/
		try{
			
			for( ; startIndex <= stopIndex ; startIndex++){
				if(!subParts.get(startIndex).matches(PATTERN) && !subParts.get(startIndex+1).matches(PATTERN)
					&& !subParts.get(startIndex).equals("-->") && !subParts.get(startIndex).matches("<.+>")){
				context += subParts.get(startIndex)+" ";
				}
			}
		}catch(IndexOutOfBoundsException e){
			if(subParts.size() != (startIndex + 1)){
				throw e;
			}
		}	
		return context;
	}
		
}
