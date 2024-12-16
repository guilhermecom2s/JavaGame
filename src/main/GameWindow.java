package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;

public class GameWindow { //eu poderia extender o JFrame
	private JFrame jframe;
	public GameWindow(GamePanel gamePanel) { //construtor
		
		jframe = new JFrame();		
			
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //para terminar o programa quando fechar
		jframe.add(gamePanel);
		jframe.setResizable(false);
		jframe.pack();
		jframe.setLocationRelativeTo(null);//para começar no meio
		jframe.setVisible(true);//para eu ver o painel, tem que ser o ultimo
		jframe.addWindowFocusListener(new WindowFocusListener() {

			@Override
			public void windowGainedFocus(WindowEvent e) {
				System.out.println("BYEEE!");
				gamePanel.getGame().windowFocusLost();
			}

			@Override
			public void windowLostFocus(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	
}
