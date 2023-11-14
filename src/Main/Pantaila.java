package Main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.security.KeyStore.TrustedCertificateEntry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Objetuak.Bala;
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
	Espaziontzia espaziontzia = new Espaziontzia(this, sarrera);
	ArrayList<Bala> balak = new ArrayList<Bala>();
	ArrayList<Espaziontzia> enemies = new ArrayList<Espaziontzia>();
	int disparoCooldown= espaziontzia.getCooldown();
	Random random = new Random();
	int enemiecooldown = 0;
	int enemiestokill=5;
	int killcount=0;
	int enemiesEspawned;
	boolean win=false;
	boolean lost=false;
	boolean stop=false;
	int lvl=1;
	int j = 0;

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
		
		
		
		if (enemiestokill<=killcount || (enemiesEspawned==0 && killcount>2)) {
			stop=true;
			enemiecooldown=1000;
			s.releaseAll();
			repaint();
			
			String[] options = {"Attack Speed UP","Damage UP"};
			int selection = -2;
			selection = JOptionPane.showOptionDialog(null, "Choose Upgrade!","Upgrade", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,options[1]);
			
			
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
				}else if ((espaziontzia.getXpos() + espaziontzia.getZabalera()) >= enemies.get(i).getXpos()&& espaziontzia.getYpos() < (enemies.get(i).getYpos() + enemies.get(i).getAltuera()) && (espaziontzia.getYpos() + espaziontzia.getAltuera() > (enemies.get(i).getYpos()))) {
					enemies.remove(i);
					lost=true;
				}
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
		
		
		
		
		

	}
	
	

	private void stageup() {
		stop=false;
		enemiestokill=enemiestokill+5;
		killcount=0;
		enemiesEspawned=0;
		enemiecooldown=0;
		lvl++;
		System.out.println("Next level");
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).setXpos(pantailaZabalera);
					}
		
		
	}

	private void collisionBallak() {
		ArrayList<Bala> balakKendu = new ArrayList<Bala>();
		ArrayList<Espaziontzia> etsaiakKendu = new ArrayList<Espaziontzia>();

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
		
		for (Espaziontzia espaziontziaK : etsaiakKendu) {
			enemies.remove(espaziontziaK);
			System.out.println("pi");
			killcount++;
			
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
