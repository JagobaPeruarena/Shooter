package Objetuak;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.Pantaila;
import Main.Sarrera;

public class Espaziontzia extends Objetua{
	private Pantaila p;
	private Sarrera s;
	private int HP = 1;
	private int Cooldown=120;
	private int dmg=1;
	private boolean active=false;
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Espaziontzia(Pantaila p, Sarrera s) {
		super();
		this.p = p;
		this.s = s;
		
		
	}
	
	public int getDmg() {
		return dmg;
	}

	public void setDmg(int dmg) {
		this.dmg = dmg;
	}

	
	
	public int getHP() {
		return HP;
	}

	public void setHP(int hP) {
		HP = hP;
	}

	public Pantaila getP() {
		return p;
	}
	public void setP(Pantaila p) {
		this.p = p;
	}
	public Sarrera getS() {
		return s;
	}
	public void setS(Sarrera s) {
		this.s = s;
	}
	public int getCooldown() {
		return Cooldown;
	}
	public void setCooldown(int cooldown) {
		Cooldown = cooldown;
	}
	public void enemieSortu() {
		super.setXpos(100);
		super.setYpos(100);
		super.setAltuera(16);
		super.setZabalera(18);
		super.setAbiadura(1);
		
		try { 
			super.setIrudia(ImageIO.read(new FileInputStream("res/wasp.png"))); 
		}catch(IOException e) {
			e.printStackTrace();
		}
		super.setAltuera(super.getIrudia().getHeight()*2);
		super.setZabalera(super.getIrudia().getWidth()*2);
	}
	
	public void Sortu() {
		super.setXpos(100);
		super.setYpos(100);
		super.setAltuera(16);
		super.setZabalera(18);
		super.setAbiadura(2);
		
		try { 
			super.setIrudia(ImageIO.read(new FileInputStream("res/Espaziontzia.png"))); 
		}catch(IOException e) {
			e.printStackTrace();
		}
		super.setAltuera(37);
		super.setZabalera(38);
	}
	

	
}
