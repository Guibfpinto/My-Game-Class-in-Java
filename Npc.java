package com.pintogames.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.pintogames.main.Game;

public class Npc extends Entity {

	public String[] frases = new String[14];
	
	public boolean showMessage = false;
	
	public int curIndexMsg = 0;
	
	public int fraseIndex = 0;
	
	public int time = 0;
	public int maxTime = 30; 
	
	public Npc(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		frases[0] = "Ol� Bem-Vindo A Cariacica.";
		frases[1] ="Essa �rea aqui � dominada pelo Tr�fico";
		frases[2]="Os Traficantes possum Rob�s para atacarem a Pol�cia";
		frases[3]="Esses Rob�s A Partir de uma IA Consegue Reconhecer...";
		frases[4]= " quem � Policial e quem n�o �";
		frases[5]="H� Algum Tempinho Atr�s Esses Rob�s deram problema";
		frases[6]="Eles deram um problema que...";
		frases[7]="n�o consegue mais diferenciar quem � pol�cia e quem n�o �";
		frases[8]="A� eles est�o Atacando todo mundo que chega perto dele";
		frases[9]="O Tr�fico t� pagando uma Recompensa...";
		frases[10]="de R$10.000,00 para quem matar esses Rob�s";
		frases[11]="Eles Disponibilizaram um Fuzil...";
		frases[12]="contrabandeado da R�ssia para ajudar a elimin�-los";
		frases[13]="TENHA CUIDADO! E ACABE COM ELES!";
	}

	public void tick() {
		
		int xPlayer = Game.player.getX();
		int yPlayer = Game.player.getY();
		
		int xNpc = (int)x;
		int yNpc = (int)y;
		
		if(Math.abs(xPlayer - xNpc) < 20 && Math.abs(yPlayer - yNpc) < 20) {
            showMessage = true;
        }else {
        	showMessage = false;
        }
		if(showMessage) {
		
		 this.time++;
		 
        if(this.time >= this.maxTime) {
		if(curIndexMsg < frases[fraseIndex].length()) {
			curIndexMsg++;
		}else {
		if(fraseIndex < frases.length) {
				fraseIndex++;
				curIndexMsg = 0;
		}
		}
		}
	    }
	    }
	public void render(Graphics g) {
	super.render(g);	
	if(showMessage) {
		g.setColor(Color.white);
		g.fillRect(9, 9, (Game.WIDTH - 18), (Game.HEIGHT - 18));
		g.setColor(Color.blue);
		g.fillRect(10, 10, (Game.WIDTH - 20), (Game.HEIGHT - 20));
		g.setFont(new Font ("Arial",Font.BOLD,9));
		g.setColor(Color.white);
		g.drawString(frases[fraseIndex].substring(0, curIndexMsg), (int)x, (int)y);

		
	}
	}

}