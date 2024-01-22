package Objetuak;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.Pantaila;
import Main.Sarrera;

public class Boss extends Objetua{
		private Pantaila p;
		private Sarrera s;
		private int HP = 20;
		private int Cooldown=120;
		private int dmg=1;
		private boolean active=false;
		private BufferedImage bordevida;
		public BufferedImage getBordevida() {
			return bordevida;
		}

		public void setBordevida(BufferedImage bordevida) {
			this.bordevida = bordevida;
		}

		public BufferedImage getBarravida() {
			return barravida;
		}

		public void setBarravida(BufferedImage barravida) {
			this.barravida = barravida;
		}
		private BufferedImage barravida;
		public boolean isActive() {
			return active;
		}

		public void setActive(boolean active) {
			this.active = active;
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
		public void bossSortu() {
			super.setXpos(800);
			super.setYpos(-150);
			super.setAltuera(16);
			super.setZabalera(16);
			super.setAbiadura(0);
			
			try { 
				super.setIrudia(ImageIO.read(new FileInputStream("res/boss.png"))); 
				setBarravida(ImageIO.read(new FileInputStream("res/barravidadentro.png")));
				setBordevida(ImageIO.read(new FileInputStream("res/barravidaentorno.png")));
			}catch(IOException e) {
				e.printStackTrace();
			}
			super.setAltuera(super.getIrudia().getHeight()*2);
			super.setZabalera(super.getIrudia().getWidth()*5);
		}
		
}
