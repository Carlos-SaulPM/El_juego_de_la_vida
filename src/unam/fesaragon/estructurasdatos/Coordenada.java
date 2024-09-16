package unam.fesaragon.estructurasdatos;

import java.util.ArrayList;

public class Coordenada {
    private int fila;
    private int columna;
    private static final int[][] DESPLAZAMIENTOS_VECINOS = {
            {-1, -1}, {-1, 0}, {-1, 1},  // Vecinos superiores
            {0, -1},           {0, 1},     // Vecinos laterales
            {1, -1},  {1, 0},  {1, 1}      // Vecinos inferiores
    };

    public Coordenada(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public Coordenada() {
    }

    public ArrayList<Coordenada> obtenerVecinos() {
        ArrayList<Coordenada> vecinos = new ArrayList<>();

        for (int[] desplazamiento : DESPLAZAMIENTOS_VECINOS) {
            int nuevoX = this.fila + desplazamiento[0];
            int nuevoY = this.columna + desplazamiento[1];
            vecinos.add(new Coordenada(nuevoX, nuevoY));
        }

        return vecinos;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }
}
