package Objetuak;

import java.awt.image.BufferedImage;

public class Objetua {

	protected int Xpos;
	protected int Ypos;
	protected int Zabalera;
	protected int Altuera;
	protected int Abiadura;
	
	protected BufferedImage irudia;

	public int getXpos() {
		return Xpos;
	}

	public void setXpos(int xpos) {
		Xpos = xpos;
	}

	public int getYpos() {
		return Ypos;
	}

	public void setYpos(int ypos) {
		Ypos = ypos;
	}

	public int getZabalera() {
		return Zabalera;
	}

	public void setZabalera(int zabalera) {
		Zabalera = zabalera;
	}

	public int getAltuera() {
		return Altuera;
	}

	public void setAltuera(int altuera) {
		Altuera = altuera;
	}

	public int getAbiadura() {
		return Abiadura;
	}

	public void setAbiadura(int abiadura) {
		Abiadura = abiadura;
	}

	public BufferedImage getIrudia() {
		return irudia;
	}

	public void setIrudia(BufferedImage irudia) {
		this.irudia = irudia;
	}
	public Objetua() {
		this.Xpos=100;
		this.Ypos=100;
	}

	public Objetua(int xpos, int ypos, int zabalera, int altuera, int abiadura, BufferedImage irudia) {
		super();
		this.Xpos = xpos;
		this.Ypos = ypos;
		this.Zabalera = zabalera;
		this.Altuera = altuera;
		this.Abiadura = abiadura;
		this.irudia = irudia;
	}
	
}
