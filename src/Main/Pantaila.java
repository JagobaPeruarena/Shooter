package Main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore.TrustedCertificateEntry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Objetuak.Bala;
import Objetuak.Boss;
import Objetuak.Espaziontzia;
import Objetuak.Objetua;

public class Pantaila extends JPanel implements Runnable {
	// Joku klasikoetan 16 biteko karratuak sortzen dira,
	// eta karratu kopuru bat ezarri pantailan.
	public final int spriteTamainaOriginala = 16;
	final int eskala = 3;
	public final int spriteTamaina = spriteTamainaOriginala * eskala;

	final int pantailaKarratuakAltuera = 14;
	final int pantailaKarratuakZabalera = 24;
	final int pantailaAltuera = spriteTamaina * pantailaKarratuakAltuera;
	final int pantailaZabalera = spriteTamaina * pantailaKarratuakZabalera;

	// Segunduko zenbat aldiz aldatuko den programa
	final int FPS = 20;
	Double drawInterval = (double) (100000000 / FPS);

	// Gure irudiaren kokapena eta tamaina
	private int x = 100;
	private int y = 100;
	private int diametroa = 50;

	// Erabiltzailearen ekintzak entzuteko
	Sarrera s = new Sarrera();

	// Haria sortzeko (2. mailako PSP asignaturan sakontzen da)
	Thread jolastu;
	Bala bala;
	Sarrera sarrera = new Sarrera();
	Objetua object = new Objetua();
	Random random = new Random();
	
	Espaziontzia espaziontzia = new Espaziontzia(this, sarrera);
	
	ArrayList<Bala> balak = new ArrayList<Bala>();
	int disparoCooldown= espaziontzia.getCooldown();
	
	
	ArrayList<Espaziontzia> enemies = new ArrayList<Espaziontzia>();
	int enemiecooldown = 0;
	int enemiestokill=5;
	int killcount=0;
	int enemiesEspawned;
	
	
	boolean win=false;
	boolean lost=false;
	boolean stop=false;
	int lvl=1;
	int j = 0;
	
	
	String textSc="Score: ";
	String Tscore="";
	int score=0;
	
	String texttime="Time until next wave:";
	String timerSC="";
	int timed=0;
	
	
	Boss boss = new Boss();
	int barravidatotal = 520;
	int posbarravida = 270;
	ArrayList<Espaziontzia> minionboss = new ArrayList<Espaziontzia>();
	int top=0;
	int bot = 99;
	int wait=1200;
	int minionspaw=0;
	boolean change=true;
	int midup=1;
	int middown=1;
	
	public Pantaila() {
		this.setPreferredSize(new Dimension(pantailaZabalera, pantailaAltuera));
		this.setBackground(Color.white);
		this.setDoubleBuffered(true);
		this.addKeyListener(s);
		this.setFocusable(true);
	}

	// Haria sortzen du
	public void asiJolasten() {
		jolastu = new Thread(this);
		jolastu.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics g2 = (Graphics) g;
		// Ship Repaint
		g2.drawImage(espaziontzia.getIrudia(), (int) Math.round(espaziontzia.getXpos()),
				(int) Math.round(espaziontzia.getYpos()), (int) Math.round(espaziontzia.getAltuera()),
				(int) Math.round(espaziontzia.getZabalera()), null);		
		g2.drawString(""+lvl, 14, 14);
		g2.drawString(Tscore, 54, 14);
		if (timed>0) {  g2.drawString(timerSC, pantailaZabalera/2, pantailaAltuera/4);
		g2.drawString(texttime, (pantailaZabalera/2)-50, (pantailaAltuera/4)-16);
		}
		for (int i = 0; i < balak.size(); i++) {
			g2.drawImage(balak.get(i).getIrudia(), (int) Math.round(balak.get(i).getXpos()),
					(int) Math.round(balak.get(i).getYpos()), (int) Math.round(balak.get(i).getAltuera()),
					(int) Math.round(balak.get(i).getZabalera()), null);
		}
		for (int i = 0; i < enemies.size(); i++) {
			g2.drawImage(enemies.get(i).getIrudia(), (int) Math.round(enemies.get(i).getXpos()),
					(int) Math.round(enemies.get(i).getYpos()), (int) Math.round(enemies.get(i).getAltuera()),
					(int) Math.round(enemies.get(i).getZabalera()), null);
		}
		if (boss.isActive()) {
			g2.drawImage(boss.getBarravida(), posbarravida,
					pantailaAltuera-150, barravidatotal,
					125, null);
			g2.drawImage(boss.getBordevida(),(int)(pantailaZabalera/4),
					pantailaAltuera-150, 500,
					125, null);
			for (int i = 0; i < minionboss.size(); i++) {
				g2.drawImage(minionboss.get(i).getIrudia(), (int) Math.round(minionboss.get(i).getXpos()),
						(int) Math.round(minionboss.get(i).getYpos()), (int) Math.round(minionboss.get(i).getAltuera()),
						(int) Math.round(minionboss.get(i).getZabalera()), null);
			}
			
		
		g2.drawImage(boss.getIrudia(), (int) Math.round(boss.getXpos()),
				(int) Math.round(boss.getYpos()), (int) Math.round(boss.getAltuera()),
				(int) Math.round(boss.getZabalera()), null);
		
		}
		g2.dispose();
	}

	// Hemen dago jokaren logika guztia
	@Override
	public void run() {
		double nextDrawTime = System.nanoTime() + drawInterval;
		espaziontzia.Sortu();
		arerioasortu();
		
		while (jolastu != null) // Haria itxi arte
		{

			// 1) Kalkulatu aurreko framearekiko aldaketak
			kalkulatu();

			// 2) Margotu (PaintComponenti deitzen dio)
			
				
			
				repaint();

				// Geratzen den denbora klakulatu eta lo egin bitartean
				try {
					double remainingTime = nextDrawTime - System.nanoTime();
					remainingTime = remainingTime / 10000000;
					if (remainingTime < 0)
						remainingTime = 0;
					Thread.sleep((long) remainingTime);
					nextDrawTime = nextDrawTime + drawInterval;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			
			
		}
	}

	private void arerioasortu() {
		//Etsailak sortu
		for (int i = 0; i < 105; i++) {
			Espaziontzia enemie = new Espaziontzia(this, s);
			enemie.enemieSortu();
			enemie.setXpos(pantailaZabalera);
			enemie.setYpos(random.nextInt(pantailaAltuera - enemie.getAltuera()-1));
			enemie.setAbiadura(0);
			enemies.add(enemie);

		}

	}
	private void arerioaBOSS(int lvl) {
		//Etsailak sortu
		int posicionINT=5;
		for (int i = 0; i < lvl*10; i++) {
			Espaziontzia enemie = new Espaziontzia(this, s);
			enemie.enemieSortu();
			enemie.setXpos(pantailaZabalera);
			
			enemie.setYpos(posicionINT);
			enemie.setAbiadura(0);
			try {
				enemie.setIrudia(ImageIO.read(new FileInputStream("res/enemigo.png")));
			} catch (Exception e) {
				// TODO: handle exception
			}
			minionboss.add(enemie);
			posicionINT+=pantailaAltuera/(lvl*10);
		}

	}

	public void kalkulatu() {
		if (s.gora && espaziontzia.getYpos() > 0)
			espaziontzia.setYpos(espaziontzia.getYpos() - espaziontzia.getAbiadura());
		else if (s.behera && (espaziontzia.getYpos() + espaziontzia.getAltuera()) < pantailaAltuera)
			espaziontzia.setYpos(espaziontzia.getYpos() + espaziontzia.getAbiadura());
		if (s.eskubi && (espaziontzia.getXpos() + espaziontzia.getZabalera()) < pantailaZabalera)
			espaziontzia.setXpos(espaziontzia.getXpos() + espaziontzia.getAbiadura());
		else if (s.ezker && espaziontzia.getXpos() > 0)
			espaziontzia.setXpos(espaziontzia.getXpos() - espaziontzia.getAbiadura());
		if (s.tiro) {
			tiroEgin(balak);
		}
		
		
		if (disparoCooldown > 0) {

			disparoCooldown--;

		}
		if (timed>0) {
			timed--;
		}
		if ((score==0 || score == 6000 ) && !boss.isActive()) {
			
			boss.bossSortu();
			boss.setActive(true);
			boss.setHP(lvl*40);
			arerioaBOSS(lvl);
			top=0;
			bot=minionboss.size()-1;
			minionspaw=0;
			wait=1200;
			if (lvl==3) {
				midup=bot/2-1;
				middown=bot/2;
			}
			
		}
		
		
		if (enemiestokill<=killcount && !boss.isActive() || (enemiesEspawned==0 && killcount>2)  ) {
			stop=true;
			enemiecooldown=2400;
			s.releaseAll();
			repaint();
			timed=2400;
			String[] options = {"Attack Speed UP","Damage UP"};
			int selection = -2;
			selection = JOptionPane.showOptionDialog(null, "Choose Upgrade! 12s","Upgrade", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,options[1]);
			System.out.println("Enter");
			
			if(selection == 0){
				espaziontzia.setCooldown(espaziontzia.getCooldown()/2);
				
			}else if(selection == 1){			
				espaziontzia.setDmg(espaziontzia.getDmg()*2);
				
			}else if(selection == -1) {
				System.exit(0);
			}
			stageup();

		}
		else if ( enemiesEspawned<enemiestokill && !stop) {
			if (enemiecooldown <= 0) {
				j = random.nextInt(enemies.size());
				if (!enemies.get(j).isActive()) {
					enemies.get(j).setHP(lvl);
					enemies.get(j).setAbiadura(-1);
					enemies.get(j).setActive(true);
					enemiecooldown = 240;
					enemiesEspawned++;
				}
				
			} else if (!stop) {
				
			
				enemiecooldown--;
			}

		} 
		if (!stop) {
			for (int i = 0; i < balak.size(); i++) {
				balak.get(i).setXpos(balak.get(i).getXpos() + balak.get(i).getAbiadura());
				if ((balak.get(i).getXpos() + balak.get(i).getZabalera()) > pantailaZabalera-1) {
					balak.remove(i);
				}
				collisionBallak();

			}
			for (int i = 0; i < enemies.size(); i++) {
				enemies.get(i).setXpos(enemies.get(i).getXpos() + enemies.get(i).getAbiadura());
				if (enemies.get(i).getXpos() < 0) {
					enemies.remove(i);
					lost=true;
					
					//Colision jugador y enemigo
				}else if ((espaziontzia.getXpos() + espaziontzia.getZabalera()) >= enemies.get(i).getXpos()&& espaziontzia.getYpos() < (enemies.get(i).getYpos() + enemies.get(i).getAltuera()) && (espaziontzia.getYpos() + espaziontzia.getAltuera() > (enemies.get(i).getYpos()))) {
					enemies.remove(i);
					lost=true;
				}
			}
		}
		
		
		
		
		//Mientras el Jefe este activo
		if (boss.isActive()) {
			
			if(lvl==1) {
				
				if (wait==0 && (minionspaw<minionboss.size())) {
					minionboss.get(top).setAbiadura(-1);
					minionboss.get(bot).setAbiadura(-1);
					wait=420;
					top++;
					bot--;
					minionspaw+=2;
				}else {
					wait--;
				}
			}
			if(lvl==3) {
				
				if (wait==0 && (minionspaw<minionboss.size())) {
					if (change) {
						minionboss.get(top).setAbiadura(-1);
						minionboss.get(bot).setAbiadura(-1);
						top++;
						bot--;
						change=false;
					}else {
						minionboss.get(midup).setAbiadura(-1);
						minionboss.get(middown).setAbiadura(-1);
						middown++;
						midup--;
						change=true;
					}
					
					wait=420;
					
					minionspaw+=2;
				}else {
					wait--;
				}
			}
			
			//Movimiento de las naves del jefe
			for (int i = 0; i < minionboss.size(); i++) {
				minionboss.get(i).setXpos(minionboss.get(i).getXpos() + minionboss.get(i).getAbiadura());
				
				
				if(lvl==1) {
					
					if (minionboss.get(i).getXpos() < 0) {
						
						minionboss.get(i).setAbiadura(1);
					}else if (minionboss.get(i).getXpos() >= pantailaZabalera && minionboss.get(i).getAbiadura()==1) {
							
							minionboss.get(i).setAbiadura(-1);
					};
							
				}else if (lvl==3) {
					
					if (minionboss.get(i).getXpos() < 0) {
						
						minionboss.get(i).setXpos(pantailaZabalera);
					}
					
				}
				
						
					
					//Colision jugador y enemigo
				 if ((espaziontzia.getXpos() + espaziontzia.getZabalera()) >= minionboss.get(i).getXpos()&& (minionboss.get(i).getXpos()+minionboss.get(i).getZabalera())>=espaziontzia.getXpos()   && espaziontzia.getYpos() < (minionboss.get(i).getYpos() + minionboss.get(i).getAltuera()) && (espaziontzia.getYpos() + espaziontzia.getAltuera() > (minionboss.get(i).getYpos()))) {
					minionboss.remove(i);
					lost=true;
				}
			}
			if (espaziontzia.getXpos()+espaziontzia.getZabalera()==boss.getXpos()) {
				lost=true;
			}
		
		}
		if (enemies.size()==0) {
			win=true;
		}
		if (win) {
			jolastu = null;
			System.out.println("The END");
			JOptionPane.showMessageDialog(getComponentPopupMenu(), "\tCONGRATULATIONS");
		}
		if (lost) {
			jolastu = null;
			System.out.println("The END");
			JOptionPane.showMessageDialog(getComponentPopupMenu(), "\tGAME OVER");
		}
		
		Tscore=""+score;
		timerSC=""+timed/180;
		
		

	}
	
	
	//cambio de nivel
	private void stageup() {
		//Sigue sin funcionar
		stop=false;
		score+=1000;
		enemiestokill=enemiestokill+5;
		killcount=0;
		enemiesEspawned=0;
		
		lvl++;
		System.out.println("Next level");
//		for (int i = 0; i < enemies.size(); i++) {
//			enemies.get(i).setXpos(pantailaZabalera);
//					}
		
		
	}

	private void collisionBallak() {
		ArrayList<Bala> balakKendu = new ArrayList<Bala>();
		ArrayList<Espaziontzia> etsaiakKendu = new ArrayList<Espaziontzia>();
		ArrayList<Espaziontzia> etsaiakKenduboss = new ArrayList<Espaziontzia>();
		for (int i = 0; i < balak.size(); i++) {
			for (int j = 0; j < enemies.size(); j++) {
				if ((balak.get(i).getXpos() + balak.get(i).getZabalera()) >= enemies.get(j).getXpos()
						&& balak.get(i).getYpos() < (enemies.get(j).getYpos() + enemies.get(j).getAltuera())
						&& (balak.get(i).getYpos() + balak.get(i).getAltuera()) > (enemies.get(j).getYpos())) {
					balak.get(i).setIndarra(balak.get(i).getIndarra() - 1);
					enemies.get(j).setHP(enemies.get(j).getHP() - espaziontzia.getDmg());
					if (enemies.get(j).getHP() <= 0) {
						etsaiakKendu.add(enemies.get(j));
					}
					if (balak.get(i).getIndarra() <= 0) {
						balakKendu.add(balak.get(i));
					}
					
				}
				
			}

		}
		
		
		if (boss.isActive()) {
			for (int j = 0; j < balak.size(); j++) {
				for (int k = 0; k < minionboss.size(); k++) {
					if ((balak.get(j).getXpos() + balak.get(j).getZabalera()) >= minionboss.get(k).getXpos() && (minionboss.get(k).getXpos()+minionboss.get(k).getZabalera())>=balak.get(j).getXpos()
							&& balak.get(j).getYpos() < (minionboss.get(k).getYpos() + minionboss.get(k).getAltuera())
							&& (balak.get(j).getYpos() + balak.get(j).getAltuera()) > (minionboss.get(k).getYpos())) {
						balak.get(j).setIndarra(balak.get(j).getIndarra() - 1);
						
						if (balak.get(j).getIndarra() <= 0) {
							balakKendu.add(balak.get(j));
						}
						
					}
					
				}
				if (balak.get(j).getXpos() + balak.get(j).getZabalera() >= boss.getXpos()) {
					balak.get(j).setIndarra(balak.get(j).getIndarra() - 1);
					
					if (balak.get(j).getIndarra() <= 0) {
						balakKendu.add(balak.get(j));
						
					}
					for (Bala balaK : balakKendu) {
						if (lvl==1) {
						barravidatotal-=barravidatotal/(boss.getHP()*espaziontzia.getDmg());
						boss.setHP(boss.getHP()-espaziontzia.getDmg());
						posbarravida+=espaziontzia.getDmg()+2;
						}else {
							barravidatotal-=barravidatotal/(boss.getHP()*espaziontzia.getDmg());
							boss.setHP(boss.getHP()-espaziontzia.getDmg());
							posbarravida+=espaziontzia.getDmg()+(7-lvl);
						}
						balak.remove(balaK);
						System.out.println("Balas removed");
						
					}
				}
				
			}
			if (boss.getHP()==0) {
				minionboss.clear();
				boss.setActive(false);
				posbarravida=280;
				barravidatotal=520;
				score+=1000;
			}
			 
		}
		
		
		
		
		for (Espaziontzia espaziontziaK : etsaiakKendu) {
			enemies.remove(espaziontziaK);
			System.out.println("pi");
			score+=100;
			killcount++;
			
			
		}
		for (Espaziontzia espaziontziaK : etsaiakKenduboss) {
			minionboss.remove(espaziontziaK);
			System.out.println("pum");
				
			
		}
		
		for (Bala balaK : balakKendu) {
			balak.remove(balaK);
			
		}

	}

	private void tiroEgin(ArrayList<Bala> balaK) {

		if (disparoCooldown <= 0) {
			Bala bala = new Bala(this, s);
			bala.sortu(espaziontzia);
			disparoCooldown=espaziontzia.getCooldown();
			balak.add(bala);
			
		}

	}

}
