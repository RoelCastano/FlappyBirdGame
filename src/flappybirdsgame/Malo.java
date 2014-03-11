package flappybirdsgame;

/**
 * Clase Raton
 *
 * @author Antonio Mejorado
 * @version 1.00 2008/6/13
 */
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Malo extends Base {
        private  static int CONTEO = 0; //score
        protected Animacion animVamp; //animacion que sera utilizada para animar al Malo
        private int velocidad; //velocidad que cada objeto de la clas Malo tendra

	public Malo(int posX,int posY, int vel){
		super(posX,posY); //constructor
                velocidad = vel;
                //Se cargan las imágenes(cuadros) para la animación del malo
		Image malo1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("imagenesMalo/0.gif"));
		Image malo2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("imagenesMalo/1.gif"));
		Image malo3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("imagenesMalo/2.gif"));
		Image malo4 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("imagenesMalo/3.gif"));
		Image malo5 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("imagenesMalo/4.gif"));
                
                //Se crea la animación
		animVamp = new Animacion();
		animVamp.sumaCuadro(malo1, 100);
		animVamp.sumaCuadro(malo2, 100);
		animVamp.sumaCuadro(malo3, 100);
		animVamp.sumaCuadro(malo4, 100);
                animVamp.sumaCuadro(malo5, 100);
	}
        
        
        /**
	 * Metodo de acceso que regresa el ancho del icono 
	 * @return un objeto de la clase <code>ImageIcon</code> que es el ancho del icono.
	 */
	public int getAncho() {
		return (new ImageIcon(animVamp.getImagen())).getIconWidth();
	}
        
        /**
	 * Metodo de acceso que regresa el alto del icono 
	 * @return un objeto de la clase <code>ImageIcon</code> que es el alto del icono.
	 */
	public int getAlto() {
		return (new ImageIcon(animVamp.getImagen()).getIconHeight());
	}
        
        /**
	 * Metodo de acceso que regresa la imagen del icono 
	 * @return un objeto de la clase <code>Image</code> que es la imagen del icono.
	 */
	public Image getImagen() {
		return (new ImageIcon(animVamp.getImagen())).getImage();
	}
        
        /**
         * Metodo de acceso que regresa la velocidad del objeto Malo
         * @return un int conteniendo la velocidad del objeto
         */
        public int getVelocidad() {
            return velocidad;
        }
        
        public int getConteo() { 
            return CONTEO;
        }
        
        public void setConteo(int c) {
            CONTEO = c;
        }
        
        
}
