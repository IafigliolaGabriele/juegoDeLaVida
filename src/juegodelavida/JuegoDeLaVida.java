/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package juegodelavida;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author IAFIGLIOLA
 */
public class JuegoDeLaVida {

    /**
     * @param args the command line arguments
     */
    
    private Celula[][] field;
    private int ancho;
    private int largo;
    private int generacion;
    private Scanner sc = new Scanner(System.in);
    private Stack<Accion> acciones = new Stack<Accion>();
    /// 1 morir, 2 nada, 3 procrear
    public void Menu(){
        int x=0,y=0;
        generacion=0;
        System.out.println("Ingrese el ancho del campo(x)");
        x = sc.nextInt();
        ancho = x;
        System.out.println("Ingrese el largo del campo(y)");
        y = sc.nextInt();
        largo=y;
        field = new Celula[x][y];
        double probability;
        for(int i=0;i<x;i++){
            for(int j=0;j<y;j++){
                field[i][j]= new Celula();
                probability = Math.random();
                if(probability<-1){
                    field[i][j].setEstaViva(true);
                }
            }
        }
        
        field[5][5].setEstaViva(true);
        field[6][5].setEstaViva(true);
        field[7][5].setEstaViva(true);
        field[5][6].setEstaViva(true);
        field[6][6].setEstaViva(true);
        field[7][6].setEstaViva(true);
        
        field[9][5].setEstaViva(true);
        field[10][5].setEstaViva(true);
        field[9][6].setEstaViva(true);
        field[10][6].setEstaViva(true);
        field[9][7].setEstaViva(true);
        field[10][7].setEstaViva(true);
        field[9][8].setEstaViva(true);
        field[10][8].setEstaViva(true);
        
        System.out.println("-------------------------- Generacion "+generacion+"-------------------------------------");
        this.imprimirField();
        System.out.println("-----------------------------------------------------------------------------------------");
        this.Vida();

    }
    
    public void Vida(){
        while(true){
            for(int i=0;i<ancho;i++){
                for(int j=0;j<largo;j++){
                    this.verificarAdyacencia(i,j);
                }
            }
            this.cambiarGeneracion();
            System.out.println("-------------------------- Generacion "+generacion+"-------------------------------------");
            this.imprimirField();
            System.out.println("-----------------------------------------------------------------------------------------");
        
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(JuegoDeLaVida.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public int verificarAdyacencia(int x,int y){
        int cantCelulasAdyacentesVivas;
        if(field[x][y].estaViva){
            cantCelulasAdyacentesVivas=-1;
        }else{
            cantCelulasAdyacentesVivas=0;
        }
        for(int i=x-1;i<=x+1;i++){
            for(int j=y-1;j<=y+1;j++){
                if(i>=0 && i<ancho && j>=0 && j<largo){
                    if(field[i][j].estaViva){
                        cantCelulasAdyacentesVivas++;
                    }
                }
            }
        }
        if(cantCelulasAdyacentesVivas==3){
            if(field[x][y].estaViva){
                return 0;
            }else{
                //nace
                acciones.push(new Accion("Nace",x,y));
                return 0;
            }
        }else if(cantCelulasAdyacentesVivas==2){
            return 0;
        }else if(cantCelulasAdyacentesVivas<2){
            // muere por soledad
            acciones.push(new Accion("Muere",x,y));
            return 0;
        }else{
            //muere por sobrepoblacion
            acciones.push(new Accion("Muere",x,y));
            return 0;
        }
    }
    
    public void cambiarGeneracion(){
        Accion accionActual;
        while(!acciones.empty()){
            accionActual = acciones.pop();
            if("Nace".equals(accionActual.nombre)){
                field[accionActual.posX][accionActual.posY].setEstaViva(true);
            }else{
                field[accionActual.posX][accionActual.posY].setEstaViva(false);
            }
        }
        generacion++;
    }
    
    public void imprimirField(){
         for(int i=0;i<ancho;i++){
            for(int j=0;j<largo;j++){
                if(field[i][j].isEstaViva()){
                    System.out.print("[#]");
                }else{
                    System.out.print("[ ]");
                }
            }
             System.out.println("");
        }
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        JuegoDeLaVida app = new JuegoDeLaVida();
        app.Menu();
    }
    
}
