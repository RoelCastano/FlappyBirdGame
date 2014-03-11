/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package flappybirdsgame;

/**
 *	Applicación JFrameHolaMundo
 *
 *	Tutorial ¿Qué es un JFrame? - Jugando con JAVA
 *
 *	@autor Jugando con JAVA
 *	@version 1.0 13/08/2010
 */

import javax.swing.JFrame;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
//

 public class JFrameFlappyBirdsGame extends JFrame implements Runnable, KeyListener, MouseListener, MouseMotionListener
 {
        private int posClicX; // posicion del mouse en x
        private int posClicY; // posicion del mouse en y
	private long tiempoActual;
        boolean colisiono;              //flag de que colisiono
        int tiempoColision;             //contador para dejar la imagen colisionada
	int posX, posY;
	private static final long serialVersionUID = 1L;
	// Se declaran las variables.
	private int direccion;    // Direccion del elefante
	private Image dbImage;	// Imagen a proyectar	
	private Graphics dbg;	// Objeto grafico
	private Bueno babe;    // Objeto de la clase Bueno
	private Malo vampiro;    //Objeto de la clase Malo
        private LinkedList<Malo> lista; // lista para guardar los monitos malos
        private int velocidad;
        boolean pausa;
        boolean desaparece;
        private SoundClip explosion;	//Sonido de explosion
	private SoundClip beep;	//Sonido de beep
        int score;
        private List<Integer> listaNum;
        int numMalos;
 	
 	public JFrameFlappyBirdsGame(){
 		setTitle("BEST GAME EVER");
 		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 		//setSize(500, 500);
                //Carga los clips de sonido
		explosion = new SoundClip("sonidos/Explosion.wav");
		beep = new SoundClip("sonidos/beep-07.wav");
                init();
                start();
 	}
        
        public void init() {
                setSize(500, 500);
                score = 0;
                colisiono = false;
                tiempoColision = 0;
		direccion = 0;
		int posX = (int) (getWidth() / 2);    // posicion en x en medio de la applet
		int posY = (int) (getHeight() /2);    // posicion en y enmedio de la applet
		babe = new Bueno(posX,posY);
		setBackground (Color.yellow);
                lista = new LinkedList<Malo>();  //lista encadenada para guardar malos
                pausa = false; // iniciliza la pausa como false
                desaparece = false;

                
                //crea una lista con los numeros 6,10 o 12
                listaNum = new ArrayList<>();
                listaNum.add(6);
                listaNum.add(10);
                listaNum.add(12);
                listaNum.add(6);
                listaNum.add(10);
                listaNum.add(12);
                Collections.shuffle(listaNum); //revuelve la lista
                
                numMalos = listaNum.get(0);// saca un numero random de la lista
                int mitadMalos = numMalos/2;


                for (int i = 0; i < mitadMalos; i++){ // primera mitad de los malos aparecen por la parte izq
                    //int posrX = (int) (Math.random() * (getWidth()));    
                    int posrY = (int) (Math.random() * (getHeight()));
                    velocidad = (int) (Math.random()*3)+3;
                    vampiro = new Malo(-2,posrY, velocidad);
                    vampiro.setPosX(vampiro.getPosX() - vampiro.getAncho());
                    vampiro.setPosY(vampiro.getPosY() - vampiro.getAlto());
                    lista.add(vampiro);
                }
                
                for (int i = mitadMalos; i < numMalos; i++){ /// segunda mitad de los malos aparecen por parte der
                    //int posrX = (int) (Math.random() * (getWidth()));    
                    int posrY = (int) (Math.random() * (getHeight()));
                    velocidad = (int) -((Math.random()*3)+3);
                    vampiro = new Malo(getWidth()+3,posrY, velocidad);
                    vampiro.setPosX(vampiro.getPosX() - vampiro.getAncho());
                    vampiro.setPosY(vampiro.getPosY() - vampiro.getAlto());
                    lista.add(vampiro);
                }
		//Pinta el fondo del Applet de color amarillo		
		setBackground(Color.white);
		addKeyListener(this);
                addMouseMotionListener(this);
                addMouseListener(this);
	}
        
        	/** 
	 * Metodo <I>start</I> sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se crea e inicializa el hilo
     * para la animacion este metodo es llamado despues del init o 
     * cuando el usuario visita otra pagina y luego regresa a la pagina
     * en donde esta este <code>Applet</code>
     * 
     */
	public void start() {
		// Declaras un hilo
		Thread th = new Thread(this);
		// Empieza el hilo
		th.start();
	}
        
        /**
         * Metodo stop sobrescrito de la clase Applet.
         * En este metodo se pueden tomar acciones para cuando se termina
         * de usar el Applet. Usualmente cuando el usuario sale de la pagina
         * en donde esta este Applet.
         */
        public void stop() {

        }

        /**
         * Metodo destroy sobrescrito de la clase Applet.
         * En este metodo se toman las acciones necesarias para cuando
         * el Applet ya no va a ser usado. Usualmente cuando el usuario
         * cierra el navegador.
         */
        public void destroy() {

        }
		
	/** 
	 * Metodo <I>run</I> sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, es un ciclo indefinido donde se incrementa
     * la posicion en x o y dependiendo de la direccion, finalmente 
     * se repinta el <code>Applet</code> y luego manda a dormir el hilo.
     * 
     */
	public void run () {
            	//Guarda el tiempo actual del sistema
                tiempoActual = System.currentTimeMillis();
		while (true) {
                        if(!pausa){
                            actualiza();
                            checaColision();
                        }
			repaint();    // Se actualiza el <code>Applet</code> repintando el contenido.
			try	{
				// El thread se duerme.
				Thread.sleep (20);
			}
			catch (InterruptedException ex)	{
				System.out.println("Error en " + ex.toString());
			}
		}
	}
        
        /**
	 * Metodo usado para actualizar la posicion de objetos elefante y vampiro.
	 * 
	 */
	public void actualiza() {
            
                switch(direccion){
                   case 1: {
                           babe.setPosY(babe.getPosY() - 2);
                           //babe.setPosX(babe.getPosX() + 5);
                           break;    //se mueve hacia arriba derecha
                   }
                   case 2: {
                           babe.setPosX(babe.getPosX() + 2);
                           //babe.setPosY(babe.getPosY() + 5);
                           
                           break;    //se mueve hacia abajo derecha	
                   }
                   case 3: {
                           babe.setPosX(babe.getPosX() - 2);
                           //babe.setPosY(babe.getPosY() + 5);
                           break;    //se mueve hacia abajo izquierda
                   }
                   case 4: {
                           //babe.setPosX(babe.getPosX() - 5);
                           babe.setPosY(babe.getPosY() + 2);
                           break;    //se mueve hacia arriba izquierda	
                   }
               }

               for (Malo vampiro : lista) {
                   vampiro.setPosX(vampiro.getPosX() + vampiro.getVelocidad());
               }


               //Determina el tiempo que ha transcurrido desde que el Applet inicio su ejecución
               long tiempoTranscurrido =
                System.currentTimeMillis() - tiempoActual;

               //Guarda el tiempo actual
               tiempoActual += tiempoTranscurrido;
               //Actualiza la animación en base al tiempo transcurrido
               babe.animBabe.actualiza(tiempoTranscurrido);
               for (Malo vampiro : lista) {
                   vampiro.animVamp.actualiza(tiempoTranscurrido);
               }
            
	}
        
        /**
	 * Metodo usado para checar las colisiones del objeto elefante y vampiro
	 * con las orillas del <code>Applet</code>.
	 */
	public void checaColision() {
                //Colision del bueno con el Applet 
		if (babe.getPosX() + babe.getAncho() > getWidth()) {
			babe.setPosX(babe.getPosX()-5);
		}
		if (babe.getPosX() < 0) {
			babe.setPosX(babe.getPosX()+5);
		}

		//Colision entre objetos
                for (Malo vampiro : lista) {
                    //Colision entre objetos
                    if( babe.intersecta(vampiro)) {
                            colisiono=true;
                            beep.play();
                            desaparece = true;
                            score++;
                            vampiro.setConteo(vampiro.getConteo()+1);
                            if(vampiro.getVelocidad() < 0){
                                    int posrY = (int) (Math.random() * (getHeight()));
                                    vampiro.setPosX(getWidth()+2);
                                    vampiro.setPosY(posrY);
                            }
                            else{
                                    int posrY = (int) (Math.random() * (getHeight()));
                                    vampiro.setPosX(-2);
                                    vampiro.setPosY(posrY);
                            }
                            
                    }
                    
                } 
                
                if (colisiono == true && tiempoColision <= 30) {
                    tiempoColision++;
                } else {
                    colisiono = false;
                    tiempoColision = 0;
                }
                
                //colision entre paredes
                for (Malo vampiro : lista) {
                    if(vampiro.getVelocidad()<0){
                            if (vampiro.getPosX() + vampiro.getAncho() < -125 ) {
                                    int posrY = (int) (Math.random() * (getHeight()));
                                    vampiro.setPosY(posrY);
                                    vampiro.setPosX(getWidth()+10);
                                    explosion.play();
                            }
                    }
                    else{
                        if ((vampiro.getPosX() + vampiro.getAncho()) > getWidth()+30) {
                                int posrY = (int) (Math.random() * (getHeight()));
                                vampiro.setPosX(-5);
                                vampiro.setPosY(posrY);
                                explosion.play();
                        }
                    }
                }

	}
        
        public void mouseClicked(MouseEvent e) { }
        
        public void mouseReleased(MouseEvent e) { }
        
        public void mouseMoved(MouseEvent e) { }
        
        public void mouseDragged(MouseEvent e) { }

        public void mouseEntered(MouseEvent e) { }

        public void mouseExited(MouseEvent e) {}

        public void mousePressed(MouseEvent e) {
                
            posClicX = e.getX();
            posClicY = e.getY();
            
            if(getWidth()/2 > posClicX && getHeight()/2 > posClicY){
                direccion = 1;
            }
            else if(getWidth()/2 < posClicX && getHeight()/2 > posClicY){
                direccion = 2;
            }
            else if(getWidth()/2 > posClicX && getHeight()/2 < posClicY){
                direccion = 3;
            }
            else if(getWidth()/2 < posClicX && getHeight()/2 < posClicY){
                direccion = 4;
            }
            
        }
		
	/**
	 * Metodo <I>update</I> sobrescrito de la clase <code>Applet</code>,
	 * heredado de la clase Container.<P>
	 * En este metodo lo que hace es actualizar el contenedor
	 * @param g es el <code>objeto grafico</code> usado para dibujar.
	 */
	public void paint(Graphics g) {
		// Inicializan el DoubleBuffer
		if (dbImage == null){
			dbImage = createImage (this.getSize().width, this.getSize().height);
			dbg = dbImage.getGraphics ();
		}

		// Actualiza la imagen de fondo.
		dbg.setColor (getBackground ());
		dbg.fillRect (0, 0, this.getSize().width, this.getSize().height);

		// Actualiza el Foreground.
		dbg.setColor (getForeground());
		paint1(dbg);

		// Dibuja la imagen actualizada
		g.drawImage(dbImage, 0, 0, this);
	}
	
	/**
	 * Metodo <I>keyPressed</I> sobrescrito de la interface <code>KeyListener</code>.<P>
	 * En este metodo maneja el evento que se genera al presionar cualquier la tecla.
	 * @param e es el <code>evento</code> generado al presionar las teclas.
	 */
        public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_UP) {    //Presiono flecha arriba
                            direccion = 1;
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {    //Presiono flecha abajo
                            direccion = 2;
                    } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {    //Presiono flecha izquierda
                            direccion = 3;
                    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {    //Presiono flecha derecha
                            direccion = 4;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_P){
                        if(pausa){
                            pausa = false;
                            direccion = 0;
                        }
                        else
                            pausa = true;
                    }
                        
        }

        /**
             * Metodo <I>keyTyped</I> sobrescrito de la interface <code>KeyListener</code>.<P>
             * En este metodo maneja el evento que se genera al presionar una tecla que no es de accion.
             * @param e es el <code>evento</code> que se genera en al presionar las teclas.
             */
        public void keyTyped(KeyEvent e){

        }

        /**
             * Metodo <I>keyReleased</I> sobrescrito de la interface <code>KeyListener</code>.<P>
             * En este metodo maneja el evento que se genera al soltar la tecla presionada.
             * @param e es el <code>evento</code> que se genera en al soltar las teclas.
             */
        public void keyReleased(KeyEvent e){
            //
        }
    
	/**
	 * Metodo <I>paint</I> sobrescrito de la clase <code>Applet</code>,
	 * heredado de la clase Container.<P>
	 * En este metodo se dibuja la imagen con la posicion actualizada,
	 * ademas que cuando la imagen es cargada te despliega una advertencia.
	 * @param g es el <code>objeto grafico</code> usado para dibujar.
	 */
	public void paint1(Graphics g) {
            
		if (babe != null && vampiro != null) {
                        g.drawImage(babe.getImagen(), babe.getPosX(), babe.getPosY(), this);
			g.drawImage(vampiro.getImagen(), vampiro.getPosX(),
                                    vampiro.getPosY(), this);
                        //pinta los vampiros en la lista
                        for (Malo vampiro : lista) {
                            g.drawImage(vampiro.getImagen(), vampiro.getPosX(), vampiro.getPosY(), this);
                        }
                        //score = vampiro.getConteo();
                        g.drawString("SCORE: " + vampiro.getConteo(), 20, 40);
                        if(pausa){
                            g.drawString(""+babe.getPausa(),babe.getPosX()+babe.getAncho(), babe.getPosY());
                        }
                        if(desaparece && colisiono && tiempoColision<=30 && !pausa){
                            g.drawString(""+babe.getDesaparece(),babe.getPosX()+babe.getAncho(), babe.getPosY());
                        }
			
		} else {
			//Da un mensaje mientras se carga el dibujo	
			g.drawString("No se cargo la imagen..",20,20);
		}
		
	}
 	 	

 }