package Objetuak;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.Pantaila;
import Main.Sarrera;

public class Bala extends Objetua {
	private Pantaila p;
	private Sarrera s;

	private int indarra=1;
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

	
	public int getIndarra() {
		return indarra;
	}
	public void setIndarra(int indarra) {
		this.indarra = indarra;
	}
	
	
	public Bala(Pantaila p, Sarrera s) {
		super();
		this.p = p;
		this.s = s;
		
	}
	public void sortu(Espaziontzia espaziontzia) {
		super.setXpos(espaziontzia.getXpos()+espaziontzia.getZabalera());
		super.setYpos(espaziontzia.getYpos()+(int)(espaziontzia.getAltuera()*(0.4)));
		super.setAbiadura(2);
		try { 
			super.setIrudia(ImageIO.read(new FileInputStream("res/Bala .png"))); 
		}catch(IOException e) {
			e.printStackTrace();
		}
		super.setAltuera(super.getIrudia().getHeight()*2);
		super.setZabalera(super.getIrudia().getWidth()*2);
	}

}
