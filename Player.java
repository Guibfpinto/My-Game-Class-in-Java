package com.pintogames.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.pintogames.graphics.Spritesheet;
import com.pintogames.main.Game;
import com.pintogames.main.Sound;
import com.pintogames.world.Camera;
import com.pintogames.world.World;

public class Player extends Entity{

	public boolean right, up, left, down;
	public int right_dir = 0, left_dir = 1, up_dir = 2, down_dir =3;
	public int dir = right_dir;
	public double speed = 1.4 ;
	
	private int frames = 0, maxFrames = 5, index = 0, maxIndex =3;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage[] downPlayer;
	
	private BufferedImage playerDamage;
	
	private boolean hasGun = false;
	
	public int ammo = 0 ;
	
	public boolean isDamaged = false ;
	private int damageFrames = 0;
	
	public boolean shoot = false,mouseShoot = false;
	
	public double life = 100, maxlife = 100;
	public int mx, my;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage [4] ;
		leftPlayer = new BufferedImage [4] ;
		upPlayer = new BufferedImage [4] ;
		downPlayer = new BufferedImage [4] ;
		playerDamage = Game.spritesheet.getSprite(96, 0, 16, 16);
		for(int i =0; i < 4; i++) {
		rightPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 32, 16, 16);
		}  
		for(int i =0; i < 4; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 48, 16, 16);
			}  
		for(int i =0; i < 4; i++) {
		upPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 0, 16, 16) ;
		}
		for(int i =0; i < 4; i++) {
			downPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 16, 16, 16) ;
		}
	    } 
		
	public void tick() {
		moved = false;
		if(right && World.isFree((int)(x+ speed), this.getY())) {
			   moved = true;
		x+=speed;

		   }
	else if(left && World.isFree((int)(x- speed), this.getY())) {
		moved = true;
		dir = left_dir;
		x-=speed;
	}
	   if(up && World.isFree(this.getX(), (int)(y- speed))) {
		   moved = true;
	y-=speed;
	   }
    else if(down && World.isFree(this.getX(), (int)(y+ speed))) {
    	moved = true;
	   y+=speed;
    }
	   if(moved) {
		   frames++;
		   if(frames == maxFrames) {
			   frames = 0;
			   index++;
			   if(index > maxIndex) {
				   index = 0;
			   }
		   }
	   }
	   
	   this.checkCollisionLifePack();
	   this.checkCollisionAmmo();
	   this.checkCollisionWeapon();
	   
	   if(isDamaged) {
		   this.damageFrames++;
		   if(this.damageFrames == 8) {
			   this.damageFrames = 0;
			   isDamaged = false;
		   }
	   }
	   
	   if(shoot){
		   shoot = false ;
		   if(hasGun && ammo > 0) {
		   ammo--;
		   //Criar Bala e atirar.!

		   int dx = 0;
		   int px = 8;
		   int py = 8;
		  if(dir == right_dir) {
			  px = 19;
			  dx = 1 ;
		  }else {
               px = -8;			 
			  dx = -1 ;
		  }
		  
		BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, 0);
		  Game.bullets.add(bullet);
		  Sound.shootEffect.play();
     }
	   }
		   if(mouseShoot) {
			   mouseShoot = false ;
			   
			   if(hasGun && ammo > 0) {
			   ammo--;
			   //Criar Bala e atirar.!
               
			   
				int px = 8;
				int py = 8;
				double angle = 8;
			   if(dir == right_dir) {
				   px = 19;
				   angle = Math.atan2(my - (this.getY()+py - Camera.y) , mx - (this.getX()+px - Camera.x));
				  }else {
					  px = -8;			
					  angle = Math.atan2(my - (this.getY()+py - Camera.y) , mx - (this.getX()+px - Camera.x));
				  }
			   double dx = Math.cos(angle);
			   double dy = Math.sin(angle);
			   
			BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, dy);
			  Game.bullets.add(bullet);
			  Sound.shootEffect.play();
		   }
	   }
	   if(life <=0) {
		   //Game Over!
		   life = 0;
		 Game.gameState = "GAME_OVER";
		   Sound.gameoverEffect.play();
		    }
	   Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH );
	   Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2),0,World.HEIGHT*16 -Game.HEIGHT);
	   }
	
	   public void checkCollisionWeapon(){
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Weapon) {
			if(Entity.isColliding(this, atual)) {
		         hasGun = true;
		     
			   Game.entities.remove(atual);
			   Sound.collectitemsEffect.play();
	}
	}
	}
	}	
	
	public void checkCollisionAmmo(){
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Bullet) {
			if(Entity.isColliding(this, atual)) {
		         ammo+= 40;
		         System.out.println("Municao atual :   " + ammo);
			   Game.entities.remove(atual);
			   Sound.collectitemsEffect.play();
	}
	}
	}
	}	
	
	public void checkCollisionLifePack() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof LifePack) {
			if(Entity.isColliding(this, e)) {
				life+= 8;
			if(life >= 100)
				life= 100;
			 Game.entities.remove(i);
			  Sound.collectitemsEffect.play();
			 return;
			}
			}
		}
	}

	
	private boolean isfree(double d, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	public void render(Graphics g) {
		if(! isDamaged) {
		if(dir == right_dir) {
		g.drawImage(rightPlayer[index], this.getX() - Camera.x,this.getY() - Camera.y, null);
		if(hasGun) {
			g.drawImage(Entity.GUN_RIGHT, this.getX()+10 - Camera.x, this.getY()+4 - Camera.y, null);
		}
		}else if(dir == left_dir) {
			g.drawImage(leftPlayer[index], this.getX() - Camera.x,this.getY() - Camera.y, null);
			if(hasGun) {
				g.drawImage(Entity.GUN_LEFT, this.getX()-10  - Camera.x, this.getY()+4 - Camera.y, null);
			}
		}
		if(dir == up_dir) {
			g.drawImage(upPlayer[index], this.getX() - Camera.x,this.getY() - Camera.y, null);
			if(hasGun) {
				g.drawImage(Entity.GUN_UP, this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
			}else if(dir == down_dir) {
				g.drawImage(downPlayer[index], this.getX() - Camera.x,this.getY() - Camera.y, null);
				if(hasGun) {
					g.drawImage(Entity.GUN_DOWN, this.getX() - Camera.x, this.getY() - Camera.y, null);
				}
			}
		    }else {
		    	g.drawImage(playerDamage, this.getX() - Camera.x, this.getY() - Camera.y, null);
		    }
            }
            }