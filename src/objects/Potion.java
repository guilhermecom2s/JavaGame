package objects;

import main.Game;

public class Potion extends GameObject{
	
	private float hoverOffset;
	private int maxHoverOffset, hoverDir = 1;//with one he goes down first

	public Potion(int x, int y, int objType) {
		super(x, y, objType);
		doAnimation = true; //it's false by default
		initHitbox(7, 14);
		xDrawOffset = (int)(3 * Game.SCALE);
		yDrawOffset = (int)(2 * Game.SCALE);
		
		maxHoverOffset = (int)(10 * Game.SCALE);
	}

	public void update() {
		updateAnimationTick();
		updateHover();
	}

	private void updateHover() {
		hoverOffset += (0.075f * Game.SCALE * hoverDir);
		
		if(hoverOffset >= maxHoverOffset)
			hoverDir = -1;
		else if(hoverOffset < 0)
			hoverDir = 1;
		
		hitbox.y = y + hoverOffset;
		
	}
	
}
