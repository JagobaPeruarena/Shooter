package Main;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;

import Objetuak.Espaziontzia;

public class Main 
{
	public static void main(String[] args) throws FileNotFoundException, IOException
	{
		//Lehioaren kanpoko aldea
		JFrame lehioa = new JFrame();
		lehioa.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		//lehioa.setUndecorated(true);

		lehioa.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		lehioa.setResizable(true);	
		lehioa.setTitle("Jokua");	
		
		//Pantailaren edukia
		Pantaila p = new Pantaila();
		lehioa.add(p);
		lehioa.pack();
		lehioa.setLocationRelativeTo(null);
		lehioa.setVisible(true);
	
		
		
		
		//Jokuaren logika guztia jasango du
		p.asiJolasten();
	}
}
