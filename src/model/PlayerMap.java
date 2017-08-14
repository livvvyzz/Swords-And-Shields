package model;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Holds all 24 tokens of the player using a Map
 * 
 * @author olivia
 *
 */
public class PlayerMap {

	private boolean upper;
	// map that holds tokens, using their name as the key
	private Map<Character, Token> tokens; 

	/**
	 * Creates map and tokens
	 * 
	 * @param upper
	 */
	public PlayerMap(boolean upper) {
		tokens = new HashMap<Character, Token>();
		this.upper = upper;
		// create all 24 tokens
		initTokens(upper);
	}
	
	public Token[][] getTokensArray(){
		
		Token[][] t = new Token[5][5];
		int count = 0;
		for(int i = 0; i < t.length; i++){
			for(int j = 0; j< t[i].length; j++){
				if(count == 24) break;
				char c = (char)(count + 97);
				t[i][j] = tokens.get(c);
				count++;
			}
		}
		return t;
	}
	/**
	 * Generates all 24 tokens
	 * @param upper 	true if the player is green
	 */
	public void initTokens(boolean upper) {

		// arraylist that holds possible sides (nothing, shield or sword
		ArrayList<Character> chars = new ArrayList<Character>();
		chars.add('.');
		chars.add('#');
		chars.add('-');

		// list that holds all possible permutations (3^4 = 81)
		ArrayList<String> permutation = new ArrayList<String>();
		for (Character a : chars) {
			for (Character b : chars) {
				for (Character c : chars) {
					for (Character d : chars) {
						String s = new StringBuilder().append(a).append(b).append(c).append(d).toString();
						permutation.add(s);
					}
				}
			}
		}
		
		//find all the duplicates of the tokens (eg, -.#-  --.#  #--.  .#-- are all the same, just with different rotoation
		Set<String> dupes = new HashSet<String>();
		for (String s1 : permutation) {
			for (String s2 : permutation) {
				// make sure not the same
				if (!s1.equals(s2)) {
					if (!dupes.contains(s1)) {
						if ((s1 + s1).indexOf(s2) != -1) {
							dupes.add(s2);
						}
					}
				}
			}
		}
		
		//delete all dupes from the set
		for(String d : dupes){
			permutation.remove(d);
		} 
		
		//creates all tokens
		for(int i = 0; i < 24; i++){
			char c = (char)(i + 97);
			char n = c;
			if(upper) n = Character.toUpperCase(n);
			tokens.put(c, new Token(c,n+permutation.get(i), upper));
		}

	}
	
	public Map<Character, Token> getMap(){
		return this.tokens;
	}

}
