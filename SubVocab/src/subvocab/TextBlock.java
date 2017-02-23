/**
  * This class contains a block from a SubRip (.srt) file row by row. The block's first row
  * contains the serial number of the block, the second row contains a time stamp, which shows 
  * when will the other rows appear and disappear.
  * 
  * @name TextBlock
  * @date 14.01.2016
  * @author Csaba Marosi
  */

package subvocab;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class TextBlock {
	private ArrayList<ArrayList<IndexedWord>> lines = new ArrayList<ArrayList<IndexedWord>>();
	private int startIndex;
	private int currentIndex = 0;
	private int pos = -1;
	
	/**
	 * Sets the absolute index of the first word in this block.
	 */
	
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
		currentIndex = startIndex;
	}

	/**
	 * Returns the absolute index of the first word in this block.
	 */
	
	public int getStartIndex(){
		return startIndex;
	}
	
	/**
	 * Adds a new line to the block
	 */
	
	public void addLine(String s){
		StringTokenizer stk = new StringTokenizer(s, " ");
		ArrayList<IndexedWord> newLine = new ArrayList<IndexedWord>();
		while (stk.hasMoreTokens()){
			newLine.add(new IndexedWord(currentIndex, stk.nextToken()));
			currentIndex++;
		}
		lines.add(newLine);
	}
	
	/**
	 * Returns the number of words in this block.
	 */
	
	public int size(){
		int size = 0;
		
		for(ArrayList<IndexedWord> line : lines){
			size += line.size();
		}
		return size;
	}
	
	/**
	 * Inserts a word, after the specified absolute index.
	 */
	
	public void insertAfter(String s, int absIndex){
		for(ArrayList<IndexedWord> line : lines){
			for(int i = 0; i < line.size(); i++){
				if(line.get(i).index == absIndex){
					line.add(i + 1, new IndexedWord(-1, s));
				}
			}
		}
	}
	
	/**
	 * Returns the next line in the block. By default it goes from the first line to the last,
	 * but seek function can modify the position.
	 */
	
	public String nextLine(){
		pos++;
		ArrayList<IndexedWord> line;
		String s = "";
		
		if(pos < lines.size()){
			line = lines.get(pos);
			for(IndexedWord word : line){
				s += word.word + " ";
			}
			return s.endsWith(" ") ? s.substring(0, s.length() - 1) : s;
		}else{
			return null;
		}
	}
	
	/**
	 * Sets the position of nextLine() function. It will return the line with pos.
	 * @param p the position of nextLine
	 * @return  true if p has set, false if it has failed (p was bigger than the number of lines)
	 */
	
	public boolean seek(int p){
		if(p < lines.size()){
			pos = p;
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * String with index.
	 */
	
	class IndexedWord{
		public int index;
		public String word;
		
		public IndexedWord(int index,String word){
			this.index = index;
			this.word = word;
		}
	}

}
