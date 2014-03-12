/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flappybirdsgame;

/**
 * Clase Puntaje
 *
 * @author Antonio Mejorado
 * @version 1.00 2008/6/13
 */
public class Puntaje {

    private int puntaje; //Guarda el valor de score
    private String nombre; //Guarda la posX del carro

    /**
     * Constructor vacio con darle valores iniciales al momento de crear el
     * objeto Puntaje
     */
    public Puntaje() {
        puntaje = 0;
        nombre = "N/A";
    }

    /**
     * Metodo constructor usado para crear el objeto
     *
     * @param puntaje es el <code>puntaje</code> del jugador.
     * @param nombre es el <code>nombrecode> del jugador.
     * 
     */
    public Puntaje(int puntaje, String nombre) {
        //Asigna los valores de los parametros al objeto Puntaje
        this.puntaje = puntaje;
        this.nombre = nombre;
    }
                                                    
    /**
     * Metodo modificador usado para cambiar el puntaje del objeto
     *
     * @param puntaje es el <code>puntaje</code> del objeto.
     */
    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    /**
     * Metodo de acceso que regresa el puntaje del objeto
     *
     * @return puntaje es el <code>puntaje</code> del objeto.
     */
    public int getPuntaje() {
        return puntaje;
    }

    /**
     * Metodo modificador usado para cambiar el puntaje del objeto
     *
     * @param puntaje es el <code>puntaje</code> del objeto.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Metodo de acceso que regresa el puntaje del objeto
     *
     * @return puntaje es el <code>puntaje</code> del objeto.
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Metodo que regresa el objeto en formato String
     *
     * @return un objeto de la clase <code>String</code>.
     */
    public String toString() {
        return "" + getPuntaje() + "," + getNombre();
    }
}
