package entities;

import static utilz.Constants.EnemyConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.*;

import main.Game;

public class Cachorro1 extends Enemy {
	
	//AttackBox
	private int attackBoxOffsetX;

	public Cachorro1(float x, float y) {
		super(x, y, CACHORRO1_WIDTH, CACHORRO1_HEIGHT, CACHORRO1);
		initHitbox(29,30);//hitbox that will interact with the lvl
		initAttackBox();
	}
	
	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int) (20*Game.SCALE), (int) (30*Game.SCALE));
		attackBoxOffsetX = (int) (Game.SCALE * 11);//mudar esse valor, para o carangueijo era 30
	}

	public void update(int[][] lvlData, Player player) {
		updateBehavior(lvlData, player);
		updateAnimationTick();		
		updateAttackBox();
		
	}
	
	private void updateAttackBox() {
		if(walkDir == RIGHT) {
			attackBox.x = hitbox.x + attackBoxOffsetX;
		}else if(walkDir == LEFT) {
			attackBox.x = hitbox.x - attackBoxOffsetX;
		}
				
		attackBox.y = hitbox.y;
		
	}

	private void updateBehavior(int[][] lvlData, Player player) {//parte que faz o porco nao ficar voando
		if(firstUpdate)  
			firstUpdateCheck(lvlData);//assim posso usar para outros inimigos
		
		if(inAir) 
			updateInAir(lvlData);
		else {//at some point the enemy will not be in the air ever again
			switch(state) {
			case IDLE:
				newState(WALKING);
				break;
			case WALKING:					
				if(canSeePlayer(lvlData, player)) {
					turnTowardsPlayer(player);
				if(isPlayerCloseForAttack(player))
					newState(ATTACK);
				}
				move(lvlData);				
				break;
			case ATTACK:
				if(aniIndex == 0)
					attackChecked = false;
				
				if(aniIndex == 3 && !attackChecked)//check the index 3 and check only once per animation
					checkPlayerHit(attackBox, player);
				break;
			case HIT:
				break;
			
			}
		}
		
	}	
	
	public int flipX() {
		if(walkDir == RIGHT)
			return width;
		else
			return 0;
	}
	
	public int flipW() {
		if(walkDir == RIGHT)
			return -1;
		else 
			return 1;
	}
	
}
