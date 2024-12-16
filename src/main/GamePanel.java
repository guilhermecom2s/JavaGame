package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import static utilz.Constants.PlayerConstants.*;
import static utilz.Constants.Directions.*;

import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;


public class GamePanel extends JPanel {

	private MouseInputs mouseInputs;//para colocar nos mouses	
	private Game game;

	public GamePanel(Game game) {//construtor
		mouseInputs = new MouseInputs(this);
		this.game = game;
			
		setPanelSize();
		addKeyListener(new KeyboardInputs(this));
		//mouseListener para clicks, motionListener para movimento
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
			 	
	}
	
	private void setPanelSize() {
		Dimension size= new Dimension(GAME_WIDTH,GAME_HEIGHT);
		setPreferredSize(size);
		System.out.printf("size: "+ GAME_WIDTH,GAME_HEIGHT);
		
	}
	


	public void updateGame() {

	}
	
	public void paintComponent(Graphics g){//graphics me permite desenhar, como um pincel
		super.paintComponent(g);//chamando a classe super para limpar o que tinha
		
		game.render(g);
	}	
	
	public Game getGame() {
		return game;
	}
}
