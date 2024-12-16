package entities;

import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.*;
import static utilz.Constants.*;

import main.Game;

public abstract class Enemy extends Entity{// i can not create an enemy out of enemy, it has to extends enemy
	protected int enemyType;
	protected boolean firstUpdate = true;
	protected int walkDir = LEFT;
	protected int tileY;
	protected float attackDistance = Game.TILES_SIZE;
	protected boolean active = true;//he is active as soon as we start the game
	protected boolean attackChecked;

	public Enemy(float x, float y, int width, int height, int enemyType) {
		super(x, y, width, height);
		this.enemyType = enemyType;	
		maxHealth = GetMaxHealth(enemyType);//using this method cause i might have different type of enemies
		currentHealth = maxHealth;
		walkSpeed=0.35f * Game.SCALE;
		
	}
	
	protected void firstUpdateCheck(int[][] lvlData) {
		if(!IsEntityOnFloor(hitbox, lvlData)) //se não estiver no chão
			inAir =  true;
		firstUpdate = false;//in that way I dont enter here ever again	
	}
	
	protected void updateInAir(int[][] lvlData) {
		if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
			hitbox.y += airSpeed;
			airSpeed += GRAVITY;
		}else {
			inAir = false;
			hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed, lvlData);
			tileY = (int) (hitbox.y / Game.TILES_SIZE);
		}		
	}
	
	protected void move(int[][] lvlData) {
		float xSpeed = 0;
		
		if(walkDir == LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;
		
		if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) //ou esse ou o de baixo vai ser falso em algum momento
			if(IsFloor(hitbox, xSpeed, lvlData)) {//vendo se tem chão
				hitbox.x += xSpeed;
				return;
			}
		
		changeWalkDir();//só venho pra esse se um dos dois acima for falso		
	}
	
	protected void turnTowardsPlayer(Player player) {
		if (player.hitbox.x > hitbox.x) {
			walkDir = RIGHT;
		}
		else
			walkDir = LEFT;
	}
	
	protected boolean canSeePlayer(int[][] lvlData, Player player) {
		int playerTileY = ((int) (player.getHitbox().y / Game.TILES_SIZE)) + 1;//esse mais um iguala os y, talvez cada inimigo precise de uma
		
		if (playerTileY == tileY)
			if (isPlayerInRange(player)) {
				if (IsSightClear(lvlData, hitbox, player.hitbox, tileY))
					return true;
			}

		return false;
	}
	
	protected boolean isPlayerInRange(Player player) {
		int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
		return absValue <= attackDistance * 5;
	}
	
	protected boolean isPlayerCloseForAttack(Player player) {
		int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);//sempre vai me retornar um valor absoluto, um modulo
		return absValue <= attackDistance;
	}

	protected void newState(int enemyState) {
		this.state = enemyState;
		aniTick = 0;
		aniIndex = 0;//this way I dont start in the middle of an animation
	}
	
	public void hurt(int amount) {
		currentHealth -= amount;
		if(currentHealth <= 0)
			newState(DEAD);
		else
			newState(HIT);
	}
	
	protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player) {
		if(attackBox.intersects(player.hitbox))
			player.changeHealth(-GetEnemyDmg(enemyType));//minus to decrease the health
		attackChecked = true;
	}
	
	protected void updateAnimationTick() {
		aniTick++;
		if(aniTick >= ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= GetSpriteAmount(enemyType, state)) {
				aniIndex = 0;
								
				switch(state) {
				case ATTACK, HIT -> state = IDLE;
				case DEAD -> active = false;//as soon as one of the enemies dies I dont wanna update him anymore
				}				
			}
		}
	}
	
	protected void changeWalkDir() {
		if(walkDir == LEFT)
			walkDir = RIGHT;
		else
			walkDir = LEFT;
	}
	
	public void resetEnemy() {
		hitbox.x = x;
		hitbox.y = y;
		firstUpdate = true;
		currentHealth = maxHealth;
		newState(IDLE);
		active = true;
		airSpeed = 0;
		
	}
	
	public boolean isActive() {
		return active;
	}
	
	/*public static float GetEntityYPosUnderRoofOrAboveFloor1(Rectangle2D.Float hitbox, float airSpeed) {
		int currentTile = (int) (hitbox.y / Game.TILES_SIZE);
		if (airSpeed > 0) {
			// Falling - touching floor
			int tileYPos = currentTile * Game.TILES_SIZE;
			int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
			return tileYPos + yOffset - 1;
		} else
			// Jumping
			return currentTile * Game.TILES_SIZE;

	}*/
	}
	
