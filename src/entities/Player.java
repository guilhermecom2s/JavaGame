package entities;


import static utilz.Constants.PlayerConstants.GetSpriteAmount;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.*;
import static utilz.Constants.Directions.*;
import static utilz.Constants.Projectiles.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Playing;
import main.Game;
import objects.Projectile;
import utilz.LoadSave;

public class Player extends Entity{
	private BufferedImage[][]animations;
	private boolean moving = false, attacking = false;
	private int facingDir = LEFT;
	private boolean left, right,jump;
	private int[][] lvlData;
	private float xDrawOffset = 10 * Game.SCALE; //10
	private float yDrawOffset = 7 * Game.SCALE; //7
	private ArrayList <Projectile> playerProjectiles = new ArrayList<>();
	
	//Jumping/Gravity
	private float jumpSpeed = -2.5f * Game.SCALE;//negative because we going up
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;//in case the player hits the roof
	
	//StatusBarUI
	private BufferedImage statusBarImg;
	
	//Projectile
	private BufferedImage ballImg;
	
	private int statusBarWidth = (int) (192 * Game.SCALE);
	private int statusBarHeight = (int) (58 * Game.SCALE);
	private int statusBarX = (int) (10 * Game.SCALE);
	private int statusBarY = (int) (10 * Game.SCALE);
	
	private int healthBarWidth = (int) (150 * Game.SCALE);
	private int healthBarHeight = (int) (4 * Game.SCALE);
	private int healthBarXStart = (int) (34 * Game.SCALE);
	private int healthBarYStart = (int) (14 * Game.SCALE);
	
	private int healthWidth = healthBarWidth;

	private int flipX = 0;
	private int flipW = 1;
	
	private boolean attackChecked, shotChecked;
	private Playing playing;
	
	private int tileY = 0;
	
	public Player(float x, float y, int width, int height, Playing playing) {//putting Playing in the constructor grants me access to it
		super(x, y, width, height);
		this.playing = playing;
		this.state = IDLE;
		this.maxHealth = 100;
		this.currentHealth = maxHealth;
		this.walkSpeed = Game.SCALE*1.0f;
		loadAnimations();
		initHitbox(44,56);
		initAttackBox();
	}
	
	public void setSpawn(Point spawn) {
		this.x = spawn.x;
		this.y = spawn.y;
		hitbox.x = x;
		hitbox.y = y;
	}
	
	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int) (20*Game.SCALE), (int) (20*Game.SCALE));
		
	}


	public void update() {	
		updateHealthBar();
		
		if(currentHealth <= 0) {
			playing.setGameOver(true);
			return;
		}

		updateAttackBox();
		
		updatePos();
		if(moving) {
			checkPotionTouched();
			checkSpikesTouched();
			tileY = (int) (hitbox.y / Game.TILES_SIZE);
		}
		if(attacking) {
			checkAttack();
			if(getAniIndex() == 2 && getAniTick() == 0)
				shoot();
			
		}

		updateAnimationTick();		
		setAnimation();		
		
		updateProjectiles(lvlData);
	}
	
	private void shoot() {
		//TODO: fazer com que seja possivel atirar para a esquerda
		int dir = 1;

		if(facingDir == LEFT) {
			dir = -1;
			playerProjectiles.add(new Projectile((int) hitbox.x - 18, (int) hitbox.y + 38, dir));
		}
		else	
			playerProjectiles.add(new Projectile((int) hitbox.x + 18, (int) hitbox.y + 38, dir));
	}
	

	private void updateProjectiles(int[][] lvlData) {
		for(Projectile p : playerProjectiles)
			if(p.isActive()) {
				
			p.updatePos();
			
			if(playing.checkEnemyShot(p.getHitbox()) || playing.checkObjectShot(p.getHitbox()))
				p.setActive(false);			
			else if(IsProjectileHittingLevel(p, lvlData))
				p.setActive(false);				
			}	
	}

	private void checkSpikesTouched() {
		playing.checkSpikesTouched(this);
		
	}

	private void checkPotionTouched() {
		playing.checkPotionTouched(hitbox);
		
	}

	
	
	private void checkAttack() {
		if(attackChecked || aniIndex != 1)
			return;
		attackChecked = true;	
		playing.checkEnemyHit(attackBox);
		playing.checkObjectHit(attackBox);

	}


	private void updateAttackBox() {
		if(right) {
			attackBox.x = hitbox.x + hitbox.width + (int) (Game.SCALE * 10);//wherever the x hitbox of the player goes attackBox will follow
		}else if(left) {
			attackBox.x = hitbox.x - hitbox.width - (int) (Game.SCALE * 10);
		}
		attackBox.y = hitbox.y + (Game.SCALE * 10);
	}


	private void updateHealthBar() {
		healthWidth = (int)((currentHealth / (float)maxHealth) * healthBarWidth);
		
	}


	public void render(Graphics g, int lvlOffset) {
		g.drawImage(animations[state][aniIndex],
				(int)(hitbox.x - xDrawOffset)- lvlOffset + flipX,
				(int)(hitbox.y - yDrawOffset), 
				width * flipW, height, null);
		//drawHitbox(g, lvlOffset);
		//drawAttackBox(g, lvlOffset);
		drawUI(g);
		drawProjectiles(g, lvlOffset);
	}
	
	private void drawProjectiles(Graphics g, int lvlOffset) {
		for(Projectile p : playerProjectiles)
			if(p.isActive())
				g.drawImage(ballImg, (int) (p.getHitbox().x - lvlOffset), (int)(p.getHitbox().y), CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT, null);
	
	}

	private void drawUI(Graphics g) {
		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
		g.setColor(Color.red);
		g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
	}


	private void updateAnimationTick() {
		aniTick++;
		if(aniTick >= ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= GetSpriteAmount(state)) {
				aniIndex = 0;
				attacking = false;//finaliza a animação de ataque
				attackChecked = false;
			}
			
		}
		
	}
	
	private void setAnimation() {		
		int startAni = state;
		
		if(moving)
			state = RUNNING;
		else
			state = IDLE;
		
		if(inAir) {
			if(airSpeed < 0)
				state = JUMP;
			else 
				state = FALLING;
		}
		
		if(attacking) {
			state = ATTACK;
			if(startAni != ATTACK) {
				aniIndex = 1;
				aniTick = 0;
				return;
			}
		}
		
		if(startAni != state)
			resetAniTick();
	}
	
	private void resetAniTick() {//com isso ele n começa uma ani de onde ele parou a anterior
		aniTick = 0;
		aniIndex = 0;		
	}


	private void updatePos() {	
		moving = false;//vc n anda até que uma dessas seja verdadeira foi escrito assim pra um cancelar o outro
		
		if (jump)
			jump();
		
		if(!inAir)
			if((!left && !right) || (right && left))
				return;
		
		float xSpeed = 0;
			
		if (left) {
			xSpeed -= walkSpeed;
			flipX = width;
			flipW = -1;
			facingDir = LEFT;
		}
		
		if (right) { 
			xSpeed += walkSpeed;
			flipX = 0;
			flipW = 1;
			facingDir = RIGHT;
		}
		
		if(!inAir) 
			if(!IsEntityOnFloor(hitbox, lvlData)) 
				inAir = true;

		
		if(inAir) {			
			if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
				hitbox.y += airSpeed;
				airSpeed += GRAVITY;
				updateXPos(xSpeed);
			}else {
				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed, lvlData); //arrumar depois para o tamanho certo da vaquinha
				if(airSpeed > 0)
					resetInAir();
				else 
					airSpeed = fallSpeedAfterCollision;
				updateXPos(xSpeed);
			}
			
		}else 
			updateXPos(xSpeed);			
		moving = true;
	}		
	
	private void jump() {
		if(inAir)
			return;
		inAir = true;
		airSpeed = jumpSpeed;	
	}


	private void resetInAir() {
		inAir = false;
		airSpeed = 0;	
	}


	private void updateXPos(float xSpeed) {
		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
			hitbox.x += xSpeed;
	} else {
		hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed, lvlData); //arrumar depois com o tamanho da vaquinha
		}
			
	}
	
	public void changeHealth(int value) {//garantindo que n vai pra mais q o max nem pra menos que 0
		currentHealth += value;
		
		if(currentHealth <= 0) {
			currentHealth = 0;
			//gameOver();
		}else if(currentHealth >= maxHealth)
			currentHealth = maxHealth;
	}
	
	public void kill() {
		currentHealth=0;		
	}
	
	public void changePower(int value) {
		System.out.println("Added power!");
		//TODO: change when the power bar is added
	}
	
	
	private void loadAnimations() {
		
			BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS); // decidir qual sprite pegar
			
			animations=new BufferedImage[7][7];
			
			for(int j=0;j<animations.length;j++)
				for(int i =0;i<animations[j].length;i++) 
					animations[j][i]=img.getSubimage(i*32,j*32,32,32);		
			
			statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
			
			ballImg = LoadSave.GetSpriteAtlas(LoadSave.CANNON_BALL);
				
	}
	
	public void loadLvlData(int [][] lvlData) {
		this.lvlData = lvlData;
		if(!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
	}

	public void resetDirBooleans() {//não esta funcionando
		left = false;
		right = false;
	}
	
	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public boolean isLeft() {
		return left;
	}


	public void setLeft(boolean left) {
		this.left = left;
	}


	public boolean isRight() {
		return right;
	}


	public void setRight(boolean right) {
		this.right = right;
	}

	
	public void setJump(boolean jump) {
		this.jump = jump;
	}


	public void resetAll() {
		resetDirBooleans();
		inAir = false;
		attacking = false;
		moving = false;
		state = IDLE;
		currentHealth = maxHealth;
		playerProjectiles.clear();
		
		hitbox.x = x;
		hitbox.y = y;
		
		if(!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;		
	}

	public int getTileY() {
		return tileY;
	}
	
	public int getHeight() {
		return height;
	}
	
}
