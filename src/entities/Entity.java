package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import main.Game;

public abstract class Entity { //absctract é uma classe que vc n consegue criar um objeto com

	protected float x,y;
	protected int width, height;
	protected Rectangle2D.Float hitbox;
	protected int aniTick,aniIndex;
	protected int state;
	protected float airSpeed;
	protected boolean inAir = false;
	protected int maxHealth;
	protected int currentHealth;
	protected Rectangle2D.Float attackBox;
	protected float walkSpeed = 1.0f*Game.SCALE;
	
	public Entity(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
	}
	
	protected void drawAttackBox(Graphics g, int lvlOffsetX) {
		g.setColor(Color.red);
		g.drawRect((int)attackBox.x - lvlOffsetX, (int)attackBox.y + 30, (int)attackBox.width, (int)attackBox.height);//0 +30 abaixa a hitbox
	}

	protected void drawHitbox(Graphics g, int xLvlOffset) {
		//For debuging the hitbox
		g.setColor(Color.PINK);
		g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
	}
	
	protected void initHitbox(int width, int height) {
		hitbox = new Rectangle2D.Float(x, y,(int) (width*Game.SCALE),(int) (height*Game.SCALE));
	}

	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}
	
	public int getState() {
		return state;
	}
	public int getAniIndex() {
		return aniIndex;
	}
	
	public int getAniTick() {
		return aniTick;
	}
	
}
