/**
  * This class represents a term, with its properties.
  * 
  * @name Term
  * @date 14.01.2016
  * @author Csaba Marosi
  */
package subvocab;

import java.util.ArrayList;

public class Term {
	private String term = "";
	private String definition = "";							/*definition, given by the user*/
	private String toStudyString = "false";					/*the string representation of toStudy*/
	private String filmTag = "";							/*the name of the source file*/
	private ArrayList<Term> terms = new ArrayList<Term>();	/*represents the terms, which can be composed from this term*/
	private boolean toStudy;								/*true if the user wants to learn this term*/
	private int position;									/*absolute position in the text file*/
	
	public Term( String term, String definition, boolean toStudy, int position) {
		this.term = term;
		this.definition = definition;
		this.toStudy = toStudy;
		this.position = position;
		if(toStudy){
			this.toStudyString = "yes";
		}		
	}
	public Term( String term, String definition, boolean toStudy, String filmTag, int position) {
		this.term = term;
		this.definition = definition;
		this.toStudy = toStudy;
		this.filmTag = filmTag;
		this.position = position;
		if(toStudy){
			this.toStudyString = "yes";
		}		
	}
	
	public String getTerm() {
		return term;
	}
	
	public String getDefinition() {
		return definition;
	}
	
	public int getPosition(){
		return position;
	}
	
	public boolean isToStudy() {
		return toStudy;
	}
	
	/**
	 * Returns an ArryList, which contains other forms of this term. Some of the forms are not grammatically correct
	 * but it doesn't make any mistake.*/
	
	public ArrayList<Term> getAltTerms(){
				
		/*create plurals, if it is singular*/
		
		if(term.endsWith("s") || term.endsWith("sh") || term.endsWith("ch") || term.endsWith("x") || term.endsWith("z")){
			terms.add(new Term(term + "es", definition, false, -1));
		}else if(term.endsWith("ay") || term.endsWith("ey") || term.endsWith("oy")){
			terms.add(new Term(term + "s", definition, false, -1));
		}else if(term.endsWith("y")){
			terms.add(new Term(term.substring(0, term.length() - 1) + "ies", definition, false, -1));
		}else if(term.endsWith("f") || term.endsWith("fe")){
			terms.add(new Term(term.substring(0, term.length() - 1) + "ves", definition, false, -1));
		}else{
			terms.add(new Term(term + "s", definition, false, -1));
		}
		
		/*create singular, if it is plural*/
		
		if(term.endsWith("s")){
			terms.add(new Term(term.substring(0, term.length() - 1), definition, false, -1));
		}
		if(term.endsWith("es")){
			terms.add(new Term(term.substring(0, term.length() - 2), definition, false, -1));
		}
		if(term.endsWith("ves")){
			terms.add(new Term(term.substring(0, term.length() - 3) + "f", definition, false, -1));
		}
		if(term.endsWith("ies")){
			terms.add(new Term(term.substring(0, term.length() - 3) + "y", definition, false, -1));
		}
		
		/*create second forms*/
		if(term.endsWith("e")){
			terms.add(new Term(term + "d", definition, false, -1));
		}else{
			terms.add(new Term(term + "ed", definition, false, -1));
		}
		
		if(term.endsWith("ed")){
			terms.add(new Term(term.substring(0, term.length() - 1), definition, false, -1));
		}else if(term.endsWith("d")){
			terms.add(new Term(term.substring(0, term.length() - 1), definition, false, -1));
		}
		
		return terms;
	}
		
	@Override
	public String toString() {
		return "term: "+term+", definition: "+definition;
	}
	/**
	 * Returns a string, which can be run as an SQL query. This query inserts the term, and some of its properties to a db.
	 */
	public String toQueryString(){
			return "INSERT INTO words ( term, definition, toStudy, filmTag ) VALUES ('" + term + "', '" + definition + "', " + toStudyString 
					+ ", '" + filmTag + "');";
	}
}
