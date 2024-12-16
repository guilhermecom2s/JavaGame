package levels;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Cachorro1;
import main.Game;
import objects.Cannon;
import objects.GameContainer;
import objects.Potion;
import objects.Spike;
import utilz.HelpMethods;

import static utilz.HelpMethods.GetLevelData;
import static utilz.HelpMethods.GetCachorros1;
import static utilz.HelpMethods.GetPlayerSpawn;


public class Level {
	
	private BufferedImage img;
	private int[][] lvlData;
	private ArrayList<Cachorro1> list;
	private ArrayList<Potion> potions;
	private ArrayList<Spike> spikes;
	private ArrayList<GameContainer> containers;
	private ArrayList<Cannon> cannons;
	private int lvlTilesWide;
	private int maxTilesOffset;
	private int maxLlvlOffsetX;
	private Point playerSpawn;
	
	public Level(BufferedImage img) {
		this.img=img;
		createLevelData();
		createEnemies();
		createPotions();
		createContainers();
		createSpikes();
		createCannons();
		calclvlOffsets();
		calcPlayerSpawn();		
	}
	
	private void createCannons() {
		cannons = HelpMethods.GetCannons(img);		
	}

	private void createSpikes() {
		spikes = HelpMethods.getSpikes(img);
	}

	private void createContainers() {
		containers = HelpMethods.GetContainers(img);		
	}

	private void createPotions() {
		potions = HelpMethods.GetPotions(img);		
	}

	private void calcPlayerSpawn() {
		playerSpawn=GetPlayerSpawn(img);
		
	}

	private void createEnemies() {
		list=GetCachorros1(img);
		
	}

	private void calclvlOffsets() {
		lvlTilesWide=img.getWidth();
		maxTilesOffset=lvlTilesWide-Game.TILES_IN_WIDTH;
		maxLlvlOffsetX=Game.TILES_SIZE*maxTilesOffset;
	
	}

	private void createLevelData() {
		lvlData=GetLevelData(img);
	}

	public int getSpriteIndex(int x, int y) {
		return lvlData[y][x];				
	}
	
	public int [][] getLevelData(){
		return lvlData;
	}
	public int getLvlOffset() {
		return maxLlvlOffsetX;
	}
	public ArrayList<Cachorro1>getCachorro1(){
		return list;
	}
	public Point getPlayerSpawn() {
		return playerSpawn;
	}
	
	public ArrayList<Potion> getPotions (){
		return potions;
	}
	
	public ArrayList<GameContainer> getContainers(){
		return containers;
	}
	
	public ArrayList<Spike> getSpikes(){
		return spikes;
	}

	public ArrayList<Cannon> getCannons(){
		return cannons;
	}
}
