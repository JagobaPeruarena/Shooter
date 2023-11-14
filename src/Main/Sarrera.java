package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Sarrera implements KeyListener
{
	//Baiezkoa badute
	public boolean gora, behera, ezker, eskubi, tiro;

	@Override
	public void keyTyped(KeyEvent e) {}
	
	//Botoa sakatzean
	public void keyPressed(KeyEvent e) 
	{
		int botoiKodea = e.getKeyCode();
		if (botoiKodea == KeyEvent.VK_W)  gora = true;
		if (botoiKodea == KeyEvent.VK_S	) behera = true;
		if (botoiKodea == KeyEvent.VK_A)  ezker = true;
		if (botoiKodea == KeyEvent.VK_D)  eskubi = true;
		if (botoiKodea == KeyEvent.VK_SPACE) tiro = true;
	}

	//Botoa askatzean
	@Override
	public void keyReleased(KeyEvent e) 
	{
		int botoiKodea = e.getKeyCode();
		if (botoiKodea == KeyEvent.VK_W)  gora = false;
		if (botoiKodea == KeyEvent.VK_S	) behera = false;
		if (botoiKodea == KeyEvent.VK_A)  ezker = false;
		if (botoiKodea == KeyEvent.VK_D)  eskubi = false;
		if (botoiKodea == KeyEvent.VK_SPACE) tiro = false;
	}
	
	//Botoaiak kendu
	public void releaseAll(){
		gora=false;
		behera=false;
		ezker=false;
		eskubi=false;
		tiro=false;
		
	}
}
