package main;

import java.awt.Graphics;
import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;
import utilz.LoadSave;

public class Game implements Runnable{
		
	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	private final int FPS_SET = 120;//criando uma variavel global
	private final int UPS_SET = 200;
	
	private Playing playing;
	private Menu menu;

	public final static int TILES_DEFAULT_SIZE=32; //definindo o tamanho dos blocos
	public final static float SCALE=1.5f;//mudar caso queira o jogo menor
	public final static int TILES_IN_WIDTH=26; 
	public final static int TILES_IN_HEIGHT=14; 
	public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE*SCALE);
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
	
	public Game() { //construtor

		initClasses();
		
		gamePanel = new GamePanel(this);
		gameWindow =  new GameWindow(gamePanel);
		gamePanel.setFocusable(true);
		gamePanel.requestFocus();//input focus to JPanel

		startGameLoop();
			
	}
	
	private void initClasses() {		
		menu = new Menu(this);
		playing = new Playing(this);
	}

	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void update() {	
		switch(Gamestate.state) {
		case MENU:
			menu.update();
			break;
		case PLAYING:
			playing.update();
			break;
		case OPTIONS:
		case QUIT:
		default:
			System.exit(0);
			break;		
		}
	}
	
	public void render(Graphics g) {
		switch(Gamestate.state) {
		case MENU:
			menu.draw(g);	
			break;
		case PLAYING://we will only render and draw the game if we are playing
			playing.draw(g);
			break;
		default:
			break;		
		}	
	}
	
	@Override
	public void run() { //aqui é onde fica o gameloop
		
		double timePerFrame = 1000000000.0 / FPS_SET; //usando nano segundos para o loop
		double timePerUpdate = 1000000000.0 / UPS_SET;		
		long previousTime = System.nanoTime();	
		
		int frames = 0; 
		int updates = 0;
		long lastCheck = System.currentTimeMillis();
		
		double deltaU = 0;
		double deltaF = 0;
		
		while(true) {//fazendo um loop infinito			
			long currentTime = System.nanoTime();	
			
						
			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;
			
			if(deltaU >= 1) {
				update();
				updates++;
				deltaU--;
			}
			
			if(deltaF >= 1) {
				gamePanel.repaint();				
				frames++;
				deltaF--;
			}
			
			
			
			if(System.currentTimeMillis() - lastCheck >= 1000) {//para contar os fps e o ups
				lastCheck = System.currentTimeMillis();
				System.out.println("FPS: " + frames + " | UPS " + updates);
				frames = 0; 
				updates = 0;
			}
					
		}
		
	}
	
	public void windowFocusLost() {
		if(Gamestate.state == Gamestate.PLAYING)
			playing.getPlayer().resetDirBooleans();
	}
	
	public Menu getMenu() {
		return menu;
	}
	
	public Playing getPlaying() {
		return playing;
	}

	
}
