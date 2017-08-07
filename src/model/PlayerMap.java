package model;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Holds all 26 tokens of the player using a Map
 * 
 * @author olivia
 *
 */
public class PlayerMap {

	// map that holds tokens, using their name as the key
	private Map<Character, Token> tokens; 

	/**
	 * Creates map and tokens
	 * 
	 * @param upper
	 */
	public PlayerMap(boolean upper) {
		tokens = new HashMap<Character, Token>();
		// create all 24 tokens
		initTokens(upper);
	}
	
	public Token[][] getTokensArray(){
		
		Token[][] t = new Token[5][5];
		int count = 0;
		for(int i = 0; i < 5; i++){
			for(int j = 0; j< 5; j++){
				if(count == 24) break;
				t[i][j] = tokens.get(0);
				count++;
			}
		}
		return t;
	}
	/**
	 * Hard codes all 24 tokens bc that what marco said to do
	 */
	public void initTokens(boolean upper) {

		// make set of 24 tokens
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
		
		//find all the duplicates of the tokens
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
			tokens.put(c, new Token(c,c+permutation.get(i), upper));
		}
		/**
		 * tokens.put(c = 'a', new Token(c,c+"-#--",upper)); tokens.put(c = 'b',
		 * new Token(c,c+"-.--",upper)); tokens.put(c = 'c', new
		 * Token(c,c+"####",upper)); tokens.put(c = 'd', new
		 * Token(c,c+"-.#.",upper)); tokens.put(c = 'e', new
		 * Token(c,c+"....",upper)); tokens.put(c = 'f', new
		 * Token(c,c+"-##-",upper)); tokens.put(c = 'g', new
		 * Token(c,c+"----",upper)); tokens.put(c = 'h', new
		 * Token(c,c+"-.##",upper)); tokens.put(c = 'i', new
		 * Token(c,c+".#..",upper)); tokens.put(c = 'j', new
		 * Token(c,c+"-#-#",upper)); tokens.put(c = 'k', new
		 * Token(c,c+"-#.-",upper)); tokens.put(c = 'l', new
		 * Token(c,c+"-...",upper)); tokens.put(c = 'm', new
		 * Token(c,c+"-##.",upper)); tokens.put(c = 'n', new
		 * Token(c,c+".##.",upper)); tokens.put(c = 'o', new
		 * Token(c,c+"-.-#",upper)); tokens.put(c = 'p', new
		 * Token(c,c+"-.#-",upper)); tokens.put(c = 'q', new
		 * Token(c,c+"-..#",upper)); tokens.put(c = 'r', new
		 * Token(c,c+"-#.#",upper)); tokens.put(c = 's', new
		 * Token(c,c+".#.#",upper)); tokens.put(c = 't', new
		 * Token(c,c+"-.-.",upper)); tokens.put(c = 'u', new
		 * Token(c,c+"-..-",upper)); tokens.put(c = 'v', new
		 * Token(c,c+"-#..",upper)); tokens.put(c = 'w', new
		 * Token(c,c+"-###",upper)); tokens.put(c = 'x', new
		 * Token(c,c+".###",upper));
		 */
	}

}
