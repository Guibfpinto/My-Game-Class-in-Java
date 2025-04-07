package com.pintogames.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.pintogames.main.Game;
import com.pintogames.world.Camera;
import com.pintogames.world.World;

public class BulletShoot extends Entity{

private double dx;
private double dy;
private double spd = 30;

private int life = 30, curLife = 0;
	
	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}
		
	public void tick() {
		if(World.isFreeDynamic((int)(x+(dx*spd)), (int) (y+(dy*spd)), 3, 3)){
	x+= dx*spd;
	y+= dy*spd;
	}else {
		Game.bullets.remove(this);
		return;
	}
	curLife ++;
	if(curLife == life) {
		Game.bullets.remove(this);
		return;
	}
}

public void render(Graphics g) {
	 g.setColor(Color.yellow);
      g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, width, height);
}

}