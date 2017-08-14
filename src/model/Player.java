package model;
import java.awt.List;
import java.util.ArrayList;

/**
 * Represents a player in the game
 * @author olivia
 *
 */
public class Player {
	
	//name of player
	private String name;
	//players tokens
	private PlayerMap tokenSet;
	
	/**
	 * Creates a player, with name either green or yellow
	 * @param name
	 */
	public Player(String name){
		if(!name.equals("green") && !name.equals("yellow")) throw new GameError("Attempting to make player with "
				+ "wrong name");
		this.name = name;
		initPlayerMap();
	}
	
	/**
	 * initialises the player map
	 */
	public void initPlayerMap(){
		//whether or not tokens are uppercase
		boolean upper;
		if(name.equals("green")) upper = true;
		else upper = false;
		 
		//create player map
		tokenSet = new PlayerMap(upper);
	}
	
	/**
	 * Returns name of the player ( green or yellow )
	 * @return	name
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * Returns the playerMap
	 * @return 	tokenSet
	 */
	public PlayerMap getPlayerMap(){
		return this.tokenSet;
	}
	

	
	
}
