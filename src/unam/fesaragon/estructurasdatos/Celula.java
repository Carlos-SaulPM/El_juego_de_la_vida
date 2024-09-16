package unam.fesaragon.estructurasdatos;

public class Celula {
    private boolean estaVivo;
    private int columna;
    private int fila;

    public Celula(boolean estaVivo, int columna, int fila) {
        this.estaVivo = estaVivo;
        this.columna = columna;
        this.fila = fila;
    }

    public Celula(int columna, int fila) {
        this.columna = columna;
        this.fila = fila;
        this.estaVivo = false;
    }

    public Celula() {
        this.estaVivo=false;
    }

    public boolean isEstaVivo() {
        return estaVivo;
    }

    public void setEstaVivo(boolean estaVivo) {
        this.estaVivo = estaVivo;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    @Override
    public String toString() {
        return estaVivo ? "█" : "□";

    }
}
