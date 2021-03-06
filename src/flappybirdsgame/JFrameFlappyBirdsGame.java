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
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
//

 public class JFrameFlappyBirdsGame extends JFrame implements Runnable, KeyListener, MouseListener, MouseMotionListener
 {
	private long tiempoActual;
        boolean colisiono;              //flag de que colisiono
        int tiempoColision;             //contador para dejar la imagen colisionada
	int posX, posY;
	private static final long serialVersionUID = 1L;
	// Se declaran las variables.
	private Image dbImage;	// Imagen a proyectar	
	private Graphics dbg;	// Objeto grafico
	private Bueno babe;    // Objeto de la clase Bueno
	private Malo columna;    //Objeto de la clase Malo
	private Malo columna1;    //Objeto de la clase Malo
	private Malo columna2;    //Objeto de la clase Malo
        private LinkedList<Malo> lista; // lista para guardar los monitos malos
        private int velocidad;     //velocidad utilizada para el movimiento del flappy
        private  static int UPWARD_SPEED = 9;
        private static int GRAVITY = 2; 
        boolean pausa; // para pausa
        boolean brinca; // para checar si brinca
        boolean empieza; // empieza el juego
        boolean reinicio; // para reiniciar el juego
        boolean desaparece;
        boolean highscores;
        boolean gameOver; // cuando pierde el jugador
        boolean escucharMouse; //utilizado pra saber cuando hacerle caso al mouse
        boolean success; //utiliada para saber cuando pasa entre dos columnas
        private int contador;
        private SoundClip explosion;	//Sonido de explosion
	private SoundClip beep;	//Sonido de beep
        int score;
        private List<Integer> listaNum;
        int numMalos;
        
        private String nombreArchivo;    //Nombre del archivo.
        private String[] arr;    //Arreglo del archivo divido.
        private Vector vec;    // Objeto vector para agregar el puntaje.
 	
 	public JFrameFlappyBirdsGame() throws IOException{
 		setTitle("BEST GAME EVER");
 		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                //Carga los clips de sonido
		explosion = new SoundClip("sonidos/Explosion.wav");
		beep = new SoundClip("sonidos/beep-07.wav");
                init();
                start();
 	}
        
        public void init() throws IOException {
                setSize(350, 600);
                score = 0;
                reinicio = false;
                colisiono = false;
                success = false;
                tiempoColision = 0;
                contador = 0;
                escucharMouse = true;  // empieza escuchando el mouse
		int posX = (int) (getWidth() / 4);    // posicion en x en medio de la applet
		int posY = (int) (getHeight() /2);    // posicion en y enmedio de la applet
                velocidad = 0;
		babe = new Bueno(posX,posY);
		setBackground (Color.yellow);
                lista = new LinkedList<Malo>();  //lista encadenada para guardar malos
                pausa = false; // iniciliza la pausa como false
                brinca = false; //inizializa la booleana de brinco como false
                empieza = false; // inicio juego
                gameOver = false; // empieza false el gameOver
                desaparece = false;
                for (int i = 0; i < 3; i++) {
                    int y = -50 + (int) (Math.random() * -300);
                    int x = i * 200;
                    columna = new Malo(getWidth() + x, y);
                    lista.add(columna);
                    columna = new Malo(getWidth() + x, y + 575);
                    lista.add(columna);
                }
                
                nombreArchivo = "Puntaje.txt";
                vec = new Vector();
                leeArchivo();

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
     public void run() {
         //Guarda el tiempo actual del sistema
         tiempoActual = System.currentTimeMillis();
         while (!gameOver) {
             if (!pausa) {
                 actualiza();
                 checaColision();
             }
             repaint();    // Se actualiza el <code>Applet</code> repintando el contenido.
             try {
                 // El thread se duerme.
                 Thread.sleep(20);
             } catch (InterruptedException ex) {
                 System.out.println("Error en " + ex.toString());
             }
         }
	}
        
        /**
	 * Metodo usado para actualizar la posicion de objetos elefante y vampiro.
	 * 
	 */
	public void actualiza() {
            
               //Determina el tiempo que ha transcurrido desde que el Applet inicio su ejecución
               long tiempoTranscurrido =
                System.currentTimeMillis() - tiempoActual;
               if(gameOver){
                   escucharMouse = false;
                   score = 0;
               }
               if(empieza){
                    if(brinca){
                        velocidad = -UPWARD_SPEED;
                        brinca = false;
                    }
                    contador++;
                    if(contador>=4){

                        velocidad += (GRAVITY);
                        contador =0;
                    }

                babe.setPosY(babe.getPosY() + velocidad);

                for (int i = 0; i < lista.size(); i++) {
                    columna = lista.get(i);
                    if (score < 10){
                        columna.setPosX(columna.getPosX() - 2);
                    } else if (score >= 10 && score < 20) {
                        columna.setPosX(columna.getPosX() - 3);
                    } else {
                        columna.setPosX(columna.getPosX() - 4);
                    }
                }
            }
               
            if (reinicio) {    //si deicde reiniciar el juego
                System.out.print("si ");
                for (int i = 0; i < lista.size(); i = i + 2) {
                    columna1 = lista.get(i);
                    columna2 = lista.get(i + 1);
                    int y = -50 + (int) (Math.random() * -300);
                    int x = getWidth() + 250;
                    columna1.setPosX(x);
                    columna2.setPosX(x);
                    columna1.setPosY(y);
                    columna2.setPosY(y + 575);
                }
                score = 0;
                velocidad = 0;
                babe.setPosX(getWidth()/4);
                babe.setPosY(getHeight() / 2);
                reinicio = false;
                empieza = false;
            }
               
               //Guarda el tiempo actual
               tiempoActual += tiempoTranscurrido;
               //Actualiza la animación en base al tiempo transcurrido
               babe.animBabe.actualiza(tiempoTranscurrido);
	}
        
        /**
	 * Metodo usado para checar las colisiones del objeto elefante y vampiro
	 * con las orillas del <code>Applet</code>.
	 */
     public void checaColision() {
         
         if(babe.getPosY()<50 ){    //cuando esta en el tope de arriba ya no le hace caso al mouse
             escucharMouse = false;
         }
         else{
             if(!gameOver){
                escucharMouse = true;
             }
         }
         
         if(babe.getPosY()>(getHeight()-40)){ // cuando pega al piso
             empieza = false;
             gameOver = true;
         }
         for (int i = 0; i < lista.size(); i++) { // entre el pajaro y las colunas
               columna = lista.get(i);
               if(babe.intersecta(columna)){
                    empieza = false;
                    gameOver = true;
                }
         }
         
         for (int m = 0; m < lista.size(); m+=2) { // checa pos de columnas para sumarle al score
               columna = lista.get(m);
                if(columna.getPosX()<(getWidth()/2)-90 && columna.getPosX()>(getWidth()/2)-92){
                          score++;
                }
         }
                        
                        
         if (colisiono == true && tiempoColision <= 30) {
             tiempoColision++;
         } else {
             colisiono = false;
             tiempoColision = 0;
         }
         for (int i = 0; i < lista.size(); i=i+2) {
             columna1 = lista.get(i);
             columna2 = lista.get(i+1);
             if (columna1.getPosX()+columna.getAncho() < 0){
                int y = -50 + (int)(Math.random()*-300);
                int x = getWidth() + 250;
                columna1.setPosX(x);
                columna2.setPosX(x);
                columna1.setPosY(y);
                columna2.setPosY(y+575);
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
            if(escucharMouse)     //si esta dentro del rango permitido, deja que brinque
                brinca = true;
            if(!gameOver) {     //utilizada par empezar el movimiento del juego
                empieza = true;
            }     
            //System.out.print("hola ");
            if(gameOver) {
                 empieza = true;
                 escucharMouse = true;
                 reinicio = true;
                 gameOver = false;
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
            try {
                paint1(dbg);
            } catch (IOException ex) {
                Logger.getLogger(JFrameFlappyBirdsGame.class.getName()).log(Level.SEVERE, null, ex);
            }

		// Dibuja la imagen actualizada
		g.drawImage(dbImage, 0, 0, this);
	}
	
	/**
	 * Metodo <I>keyPressed</I> sobrescrito de la interface <code>KeyListener</code>.<P>
	 * En este metodo maneja el evento que se genera al presionar cualquier la tecla.
	 * @param e es el <code>evento</code> generado al presionar las teclas.
	 */
     public void keyPressed(KeyEvent e) {
         if (e.getKeyCode() == KeyEvent.VK_P) {
             pausa = !pausa;
         }
         if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if(escucharMouse)  {   //si esta dentro del rango permitido, deja que brinque
                brinca = true;
            }
            if(!gameOver) {
                empieza = true;     //utilizada par empezar el movimiento del juego
            }
            if(gameOver) {
                 empieza = true;
                 escucharMouse = true;
                 reinicio = true;
                 gameOver = false;
            }
         }
         if (e.getKeyCode() == KeyEvent.VK_F) {
             if (highscores){
                 highscores = false;
                 pausa = false;
             } else {
                 pausa = true;
                 highscores = true;
             }
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
	public void paint1(Graphics g) throws IOException {
            
		if (babe != null && columna != null) {
                        g.drawImage(babe.getImagen(), babe.getPosX(), babe.getPosY(), this);
                        for (int i = 0; i < lista.size(); i++) {
                            columna = lista.get(i);
                            g.drawImage(columna.getImagen(), columna.getPosX(), columna.getPosY(), this);
                        }
                        //score = vampiro.getConteo();
                        g.drawString("SCORE: " + score, 20, 40);
                        if(gameOver){
                            g.drawString("GAME OVER", getWidth()/2-40, getHeight()/2);
                            g.drawString("PRESIONA MOUSE O ESPACIO PARA REINICIAR", getWidth()/2-150, getHeight()/2 + 40);
                        }
                        if(pausa){
                            g.drawString("PAUSA",getWidth()/2-40, getHeight()/5);
                        }
                        if(desaparece && colisiono && tiempoColision<=30 && !pausa){
                            g.drawString(""+babe.getDesaparece(),babe.getPosX()+babe.getAncho(), babe.getPosY());
                        }
                        if(highscores) {
                            for (int i = 0; i < vec.size(); i++){
                                Puntaje sc = (Puntaje) vec.get(i);
                                g.drawString(sc.getNombre() + "= " + sc.getPuntaje(),getWidth()/2-40, getHeight()/4 + (i*20));
                            }
                        }
			
		} else {
			//Da un mensaje mientras se carga el dibujo	
			g.drawString("No se cargo la imagen..",20,20);
		}
		
	}
        
    /**
     * Metodo que lee a informacion de un archivo y lo agrega a un vector.
     *
     * @throws IOException
     */
    public void leeArchivo() throws IOException {
        BufferedReader fileIn;
        try {
            fileIn = new BufferedReader(new FileReader(nombreArchivo));
        } catch (FileNotFoundException e) {
            File puntos = new File(nombreArchivo);
            PrintWriter fileOut = new PrintWriter(puntos);
            fileOut.println("0, None");
            fileOut.close();
            fileIn = new BufferedReader(new FileReader(nombreArchivo));
        }
        String dato = fileIn.readLine();

        while (dato != null) {
            arr = dato.split(",");
            int num = (Integer.parseInt(arr[0]));
            String nombre = arr[1];
            vec.add(new Puntaje(num, nombre));
            dato = fileIn.readLine();

        }
        fileIn.close();
    }
}
