package entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Playing;
import levels.Level;
import utilz.LoadSave;
import static utilz.Constants.EnemyConstants.*;

public class EnemyManager {

	private Playing playing;
	private BufferedImage[][] cachorro1Arr;
	private ArrayList <Cachorro1> cachorros1 = new ArrayList<>();
	
	public EnemyManager(Playing playing) {
		this.playing = playing;
		loadEnemyImgs();
		
	}
	
	public void LoadEnemies(Level level) {
		cachorros1 = level.getCachorro1();
		System.out.println("Quantidade de cachorros" + cachorros1.size());
	}

	public void update(int[][] lvlData, Player player) {
		boolean isAnyActive=false;
		for(Cachorro1 c : cachorros1)
			if(c.isActive()){ //only update is it is active
			c.update(lvlData, player);
			isAnyActive=true;
			}
		if(!isAnyActive)
			playing.setLevelCompleted(true);
	}
	
	
	public void draw(Graphics g, int xLvlOffset) {
		drawCachorros1(g, xLvlOffset);
	}
	
	private void drawCachorros1(Graphics g, int xLvlOffset) {
		for(Cachorro1 c : cachorros1)
		if(c.isActive()){
			g.drawImage(cachorro1Arr[c.getState()][c.getAniIndex()], 
					(int) c.getHitbox().x - xLvlOffset - CACHORRO1_DRAWOFFSET_X + c.flipX(), 
					(int)c.getHitbox().y - CACHORRO1_DRAWOFFSET_Y, 
					CACHORRO1_WIDTH * c.flipW(), CACHORRO1_HEIGHT, null);
			//c.drawHitbox(g, xLvlOffset);
			//c.drawAttackBox(g, xLvlOffset);
		}
	}
	
	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		for(Cachorro1 c : cachorros1)
			if(c.isActive())
				if(attackBox.intersects(c.getHitbox())) {//those boxes overlaps
					c.hurt(10);//the number is the damage delt by the player
					return;//I hit one enemy at a time
				}
	}
	
	public boolean checkEnemyShot(Rectangle2D.Float attackBox) {
		for(Cachorro1 c : cachorros1)
			if(c.isActive())
				if(attackBox.intersects(c.getHitbox())) {//those boxes overlaps
					c.hurt(10);//the number is the damage delt by the player
					return true;//I hit one enemy at a time
				}
		return false;
	}
	
	
	private void loadEnemyImgs() {
		cachorro1Arr = new BufferedImage[5][6];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CACHORRO1_SPRITE);
		for(int j = 0; j < cachorro1Arr.length; j++)
			for(int i = 0; i < cachorro1Arr[j].length; i++)
				cachorro1Arr[j][i] = temp.getSubimage(i * CACHORRO1_WIDTH_DEFAULT, j * CACHORRO1_HEIGHT_DEFAULT, CACHORRO1_WIDTH_DEFAULT, CACHORRO1_HEIGHT_DEFAULT);
		
	}
	
	public void resetAllEnemies() {
		for(Cachorro1 c : cachorros1)
			c.resetEnemy();
	}
	
}
