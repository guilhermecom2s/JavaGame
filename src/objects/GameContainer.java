package objects;
import static utilz.Constants.ObjectConstants.*;

import main.Game;

public class GameContainer extends GameObject {

	public GameContainer(int x, int y, int objType) {
		super(x, y, objType);
		createHitbox();
	}

	private void createHitbox() {	
		if(objType == BOX) {
			initHitbox(25, 18);
			
			xDrawOffset = (int)(7 * Game.SCALE);
			yDrawOffset = (int)(12 * Game.SCALE);
			
		} else {
			initHitbox(23, 25);
			
			xDrawOffset = (int)(8 * Game.SCALE);
			yDrawOffset = (int)(5 * Game.SCALE);
			
		}
		
		hitbox.y += yDrawOffset + (int)(Game.SCALE * 2);//o dois é a diferença entre o sprite e o tile do level
		hitbox.x += xDrawOffset / 2;//assim eu coloco no meio
	}
	
	public void update() {
		if (doAnimation)
			updateAnimationTick();
	}

}
