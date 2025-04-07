package com.pintogames.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.pintogames.entities.BulletShoot;
import com.pintogames.entities.Enemy;
import com.pintogames.entities.Entity;
import com.pintogames.entities.Npc;
import com.pintogames.entities.Player;
import com.pintogames.graphics.Spritesheet;
import com.pintogames.graphics.UI;
import com.pintogames.world.World;



public class Game  extends Canvas implements Runnable,KeyListener,MouseListener,MouseMotionListener{

	 public static JFrame  frame;
	 private Thread thread;
	 private boolean isRunning = true;
     public static final int WIDTH = 320 ;
	 public static final int HEIGHT = 320 ;
	 public static final int SCALE = 3;
	 
	 private int CUR_LEVEL = 1, MAX_LEVEL = 2;
	 
	 private BufferedImage image;
	 
	  public static List<Entity> entities;
	 
	  public static List<Enemy> enemies;
	  
	  public static List<BulletShoot> bullets;
	  
	  public static Spritesheet spritesheet;
	 
	 public static World world;
	 
	public static Player player;
	
	public static Random rand;
	
	public UI ui;
	
	public static String gameState = "MENU";
	private boolean showMessageGameOver = true;
	private int framesGameOver = 0;
	private boolean restartGame = false;
	
	public static int entrada = 1;
	public static int comecar = 2;
	public static int jogando = 3;
	public static int estado_cena = entrada;
	
	public int  timeCena = 0,maxTimeCena = 60*3;
	
	public Menu menu;
	
	public boolean saveGame = false;
	
	public int mx, my;
	 
	public Npc npc;
	
	 public Game() {
		 rand = new Random();
	   addKeyListener (this) ;
	   addMouseListener (this) ;
	   addMouseMotionListener(this);
	   //this.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
	   this.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		initFrame();
		//Inicializando objetos.
		ui = new UI();
		image = new BufferedImage(WIDTH, HEIGHT , BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity> () ;
		enemies = new ArrayList<Enemy> () ;
		bullets = new ArrayList<BulletShoot> ();
		 
		  spritesheet = new Spritesheet("/spritesheet.png"); 
		  player = new Player (0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
			 entities.add(player);
		  world = new World("/level1.png");
		  
		  Npc npc = new Npc(32,32,16,16,spritesheet.getSprite(48, 64, 16,16));
		  
		  entities.add(npc);
		  
		  menu = new Menu();
	       }
	   public void initFrame() {
			      frame = new JFrame("Zelda Brasil") ;
			     frame.add(this) ;
			    // frame.setUndecorated(true);
			    frame.setResizable(false);
			    frame.pack() ;
			    // Icone da Janela
			    Image imagem = null;
			    try {
			    imagem = ImageIO.read(getClass().getResource("/exeicon.png"));	
			    }catch (IOException e) {
			    e.printStackTrace();	
			    }
			    Toolkit toolkit = Toolkit.getDefaultToolkit();
			    Image image = toolkit.getImage(getClass().getResource("/mouseicon.png"));
			    Cursor c = toolkit.createCustomCursor(image,new Point(0,0), "img");
			    frame.setCursor(c);
			    frame.setIconImage(image);
			    frame.setAlwaysOnTop(true);
			    frame.setLocationRelativeTo(null);
			    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
			    frame.setVisible(true) ;
	            }
	            public synchronized void start() {
	                thread = new Thread(this);
	                isRunning = true;
	                thread.start();
	            }
	           public synchronized void stop() { 
	        	   isRunning = false;
	            try {
					thread.join();
				} catch (InterruptedException e) {
                e.printStackTrace();
				}
	            }
	           public static void main(String args[]) {
               Game game = new Game () ;
	           game.start();
	  }
      public void tick() {
    	  if(gameState == "NORMAL") {
    		  if(this.saveGame) {
    			  this.saveGame = false;
    			  String[] opt1 = {"level"} ;
    			  int[] opt2 = {this.CUR_LEVEL};
    			  Menu.saveGame(opt1, opt2, 10);
    			  System.out.println("Jogo Salvo!");
    	}
            this.restartGame = false;
            
            if(Game.estado_cena == Game.jogando) {
            
      for(int i = 0; i < entities.size(); i++) {
      Entity e = entities.get(i);     	  
       e.tick();
    	  }
          
      for(int i = 0; i < bullets.size(); i++) {
        	  bullets.get(i).tick();
          }
          }else{
          if(Game.estado_cena == Game.entrada) {
          if(player.getX() < 240) {
        	  player.x++;
          }else {
          Game.estado_cena = Game.comecar;	  
          }
          }else if (Game.estado_cena == Game.comecar) {
        	  timeCena++;
        	  if(timeCena == maxTimeCena) {
        		  Game.estado_cena = Game.jogando;
          }
          }
          }
          if(enemies.size() == 0) {
            //Avan�ar para o pr�ximo level!
        	  CUR_LEVEL ++;
        	  if(CUR_LEVEL > MAX_LEVEL) {
        	CUR_LEVEL = 1;	  
          }
        	  String newWorld = "level" + CUR_LEVEL + ".png";
        	  World.restartGame(newWorld);
          }
    	  }else if(gameState == "GAME_OVER") {
           this.framesGameOver++;
           if(framesGameOver == 30) {
        	   this.framesGameOver = 0;
        	   if(showMessageGameOver)
        		   this.showMessageGameOver = false;
        	   else
        		   this.showMessageGameOver = true;
          }
           if(restartGame) {
        	   this.restartGame = false;
        	   this.gameState = "NORMAL";
        	  CUR_LEVEL = 1;
        	   String newWorld = "level" + CUR_LEVEL + ".png";
         	  World.restartGame(newWorld);
          }
    	  }else if(gameState == "MENU") {
    		  menu.tick();
    	  }
          }
      public void render() {
       BufferStrategy bs = this.getBufferStrategy();
       if(bs == null) {     
       this.createBufferStrategy(3);
       return;
       }
       Graphics g = image.getGraphics();
        g.setColor(new Color(0,0 ,0));
        g.fillRect(0, 0, WIDTH, HEIGHT);
      
       /*Renderiza��o do jogo*/
       //Graphics 2D g2 = (Graphics 2D) g;
        world.render(g);
       for(int i = 0; i < entities.size(); i++) {
    	      Entity e = entities.get(i);     	  
    	    	e.render(g);
       }
       for(int i = 0; i < bullets.size(); i++) {
     	  bullets.get(i).render(g);
       }
       ui.render(g);
         /***/
       g.dispose();
       g = bs.getDrawGraphics();
       //g.drawImage(image, 0, 0, Toolkit.getDefaultToolkit().getScreenSize().width ,Toolkit.getDefaultToolkit().getScreenSize().height, null);
       g.drawImage(image, 0, 0, WIDTH*SCALE ,HEIGHT*SCALE, null);
       g.setFont(new Font("arial", Font.BOLD, 20));
       g.setColor(Color.white);
       g.drawString("Municao : " + player.ammo, 580, 25);
       if(gameState == "GAME_OVER") {
       Graphics2D g2 = (Graphics2D) g;
       g2.setColor(new Color (0, 0, 0, 100));
       g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
       g.setFont(new Font("arial", Font.BOLD, 36));
       g.setColor(Color.white);
       g.drawString("Game Over", (WIDTH*SCALE) / 2 - 70 , (HEIGHT*SCALE) / 2 - 20);
       g.setFont(new Font("arial", Font.BOLD, 32));
       if(showMessageGameOver)
       g.drawString("> Pressione ENTER para reiniciar", (WIDTH*SCALE) / 2 - 240 , (HEIGHT*SCALE) / 2 + 40);
       }else if(gameState == "MENU") {
          menu.render(g);
       }
         /*
       Graphics2D g2 = (Graphics2D) g;
       double angleMouse = Math.atan2(200+25 - my, 200+25 - mx);
       g2.rotate(angleMouse, 200+25, 200+25);
       g.setColor(Color.red);
       g.fillRect(200, 200, 50, 50);
       */
       
       if(Game.estado_cena == Game.comecar) {
    	   g.drawString("VOCE ESTA PRONTO ?", (WIDTH*SCALE) / 2 - 70 , (HEIGHT*SCALE) / 2 - 20);
       }
       
    	   bs.show();
       }
		public void run() {
		requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks ;
        double delta = 0 ;
        int frames = 0;
        double timer = System.currentTimeMillis();
        requestFocus();
	    while(isRunning) {
		long now = System.nanoTime();
        delta += (now - lastTime) / ns;
        lastTime = now ;
        if(delta >= 1) {
        	tick();
        	render();
        	frames++;
        	delta--;
        }
        if(System.currentTimeMillis() - timer >= 1000) {
        	System.out.println("FPS :"+ frames);
        	frames = 0;
        	timer += 1000;
        }
        }
	    stop();
     }
		@Override
		public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() ==  KeyEvent.VK_RIGHT) {
         player.right = true;
		}else if(e.getKeyCode() ==  KeyEvent.VK_LEFT) {
			player.left = true;
		}
        if(e.getKeyCode() ==  KeyEvent.VK_UP) {
        	player.up = true;
        	
        	if(gameState == "MENU") {
				menu.up = true;
        }
		}else if(e.getKeyCode() ==  KeyEvent.VK_DOWN) {
			player.down = true;
			
			if(gameState == "MENU") {
				menu.down = true;
		}
		}
        if(e.getKeyCode() == KeyEvent.VK_Z) {
        	player.shoot = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
        	this.restartGame = true;
        	
        	if(gameState == "MENU") {
               menu.enter = true;		
        }
        }
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
        	 gameState = "MENU";
        	 menu.pause = true;
        	 }
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
        	if(gameState == "NORMAL")
        	this.saveGame = true;
        }
		}
		@Override
		public void keyReleased(KeyEvent e) {
			  if(e.getKeyCode() ==  KeyEvent.VK_RIGHT) {
			         player.right = false;
					}else if(e.getKeyCode() ==  KeyEvent.VK_LEFT) {
						player.left = false;
					}
			        if(e.getKeyCode() ==  KeyEvent.VK_UP) {
			        	player.up = false;
					}else if(e.getKeyCode() ==  KeyEvent.VK_DOWN) {
						player.down = false;
					}
			
		}
		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mousePressed(MouseEvent e) {
		player.mouseShoot = true;
			player.mx = (e.getY() / 3) ;
			player.my = (e.getY() / 3) ;
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseMoved(MouseEvent e) {
			this.mx = e.getX();
			this.my = e.getY();
	 }
	 }