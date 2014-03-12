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
        protected Animacion animColumn; //animacion que sera utilizada para animar al Malo

	public Malo(int posX,int posY){
		super(posX,posY); //constructor
                //Se cargan las imágenes(cuadros) para la animación del malo
		Image column1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("imagenesColumna/column.gif"));
                
                //Se crea la animación
		animColumn = new Animacion();
		animColumn.sumaCuadro(column1, 100);
	}
        
        
        /**
	 * Metodo de acceso que regresa el ancho del icono 
	 * @return un objeto de la clase <code>ImageIcon</code> que es el ancho del icono.
	 */
	public int getAncho() {
		return (new ImageIcon(animColumn.getImagen())).getIconWidth();
	}
        
        /**
	 * Metodo de acceso que regresa el alto del icono 
	 * @return un objeto de la clase <code>ImageIcon</code> que es el alto del icono.
	 */
	public int getAlto() {
		return (new ImageIcon(animColumn.getImagen()).getIconHeight());
	}
        
        /**
	 * Metodo de acceso que regresa la imagen del icono 
	 * @return un objeto de la clase <code>Image</code> que es la imagen del icono.
	 */
	public Image getImagen() {
		return (new ImageIcon(animColumn.getImagen())).getImage();
	}
                
        public int getConteo() { 
            return CONTEO;
        }
        
        public void setConteo(int c) {
            CONTEO = c;
        }
        
        
}
