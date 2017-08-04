package model;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Holds all 26 tokens of the player using a Map
 * @author olivia
 *
 */
public class PlayerMap {
	
	//map that holds tokens, using their name as the key
	private Map<Character,Token> tokens;
	
	/**
	 * Creates map and tokens
	 * @param upper
	 */
	public PlayerMap(boolean upper){
		tokens = new HashMap<Character,Token>();
		//create all 24 tokens
		initTokens(upper);
	}
	
	/**
	 * Hard codes all 24 tokens bc that what marco said to do
	 */
	public void initTokens(boolean upper){
		Character c;
		tokens.put(c = 'a', new Token(c,c+"-#--",upper));
		tokens.put(c = 'b', new Token(c,c+"-.--",upper));
		tokens.put(c = 'c', new Token(c,c+"####",upper));
		tokens.put(c = 'd', new Token(c,c+"-.#.",upper));
		tokens.put(c = 'e', new Token(c,c+"....",upper));
		tokens.put(c = 'f', new Token(c,c+"-##-",upper));
		tokens.put(c = 'g', new Token(c,c+"----",upper));
		tokens.put(c = 'h', new Token(c,c+"-.##",upper));
		tokens.put(c = 'i', new Token(c,c+".#..",upper));
		tokens.put(c = 'j', new Token(c,c+"-#-#",upper));
		tokens.put(c = 'k', new Token(c,c+"-#.-",upper));
		tokens.put(c = 'l', new Token(c,c+"-...",upper));
		tokens.put(c = 'm', new Token(c,c+"-##.",upper));
		tokens.put(c = 'n', new Token(c,c+".##.",upper));
		tokens.put(c = 'o', new Token(c,c+"-.-#",upper));
		tokens.put(c = 'p', new Token(c,c+"-.#-",upper));
		tokens.put(c = 'q', new Token(c,c+"-..#",upper));
		tokens.put(c = 'r', new Token(c,c+"-#.#",upper));
		tokens.put(c = 's', new Token(c,c+".#.#",upper));
		tokens.put(c = 't', new Token(c,c+"-.-.",upper));
		tokens.put(c = 'u', new Token(c,c+"-..-",upper));
		tokens.put(c = 'v', new Token(c,c+"-#..",upper));
		tokens.put(c = 'w', new Token(c,c+"-###",upper));
		tokens.put(c = 'x', new Token(c,c+".###",upper));
	}
	
	
}
