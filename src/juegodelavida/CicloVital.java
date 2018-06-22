/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package juegodelavida;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author IAFIGLIOLA
 */
public class CicloVital extends Thread{

    private javax.swing.JPanel cuerpo;   // Cuadro de texto en la interfaz donde se imprime el estado actual del presidente
    private GridBagConstraints ubicacion;
    private JuegoDeLaVida juego;
    private boolean test;
    
    public CicloVital(javax.swing.JPanel cuerpo){
        this.cuerpo = cuerpo;
        ubicacion = new GridBagConstraints();
        juego = new JuegoDeLaVida();
        juego.configuracionEstandar(50,100);
        test = true;
        this.construirGrid();
    }
    
    public void construirGrid(){
        for(int i=0;i<juego.getAncho();i++){
            for(int j=0;j<juego.getLargo();j++){
                ubicacion.gridx=j;
                ubicacion.gridy=i;
                ubicacion.gridheight=1;
                ubicacion.gridwidth=1;
                cuerpo.add(juego.getField()[i][j], ubicacion);
                cuerpo.updateUI();
            }
        }
    }
    
    public void pintar(){
        for(int i=0;i<juego.getAncho();i++){
            for(int j=0;j<juego.getLargo();j++){
                if(juego.getField()[i][j].isEstaViva()){
                    System.out.println("true");
                    juego.getField()[i][j].setBackground(Color.red);
                }else{
                    if(test){
                        juego.getField()[i][j].setBackground(Color.white);
                    }else{
                        juego.getField()[i][j].setBackground(Color.blue);
                    }
                }
            }
        }
        test = !test;
        this.cuerpo.updateUI();
    }
    
    @Override
       public void run(){
        while(true){
            System.out.println("tan");
            try { 
                for(int i=0;i<juego.getAncho();i++){
                    for(int j=0;j<juego.getLargo();j++){
                        this.juego.verificarAdyacencia(i,j);
                    }
                }   
                this.juego.cambiarGeneracion();
                //this.juego.imprimirField();
                //this.pintar();
                System.out.println("Pintar");
                for(int i=0;i<juego.getAncho();i++){
                    for(int j=0;j<juego.getLargo();j++){
                        if(juego.getField()[i][j].isEstaViva()){
                            System.out.println("true");
                            juego.getField()[i][j].setBackground(Color.red);
                        }else{
                            juego.getField()[i][j].setBackground(Color.white);
                        }
                    }
                }
        test = !test;
        this.cuerpo.updateUI();
        
                Thread.sleep(250);
            } catch (InterruptedException ex) {
                Logger.getLogger(CicloVital.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            
    }
    
}
