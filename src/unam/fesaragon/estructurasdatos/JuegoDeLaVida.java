package unam.fesaragon.estructurasdatos;

import java.util.ArrayList;

public class JuegoDeLaVida {
    //Alumno:Carlos Saul Paz Maldonado
    private ArrayList<Coordenada> desplazamientosVecinos = new ArrayList<>();
    private ADTArray2D<Celula> generacionActual;
    private ADTArray2D<Celula> siguienteGeneracion;

    public JuegoDeLaVida(ADTArray2D<Celula> generacionActual) {
        this.generacionActual = generacionActual;
        this.siguienteGeneracion = new ADTArray2D<>(generacionActual.getRow(), generacionActual.getCol());
        cargarCoordenadas(generacionActual.getRow(), generacionActual.getCol());
        iniciarlizarCelulasDeSiguienteGeneracion();
    }

    private void iniciarlizarCelulasDeSiguienteGeneracion() {
        // Inicializar las células en la siguiente generación
        for (int fila = 0; fila < siguienteGeneracion.getRow(); fila++) {
            for (int columna = 0; columna < siguienteGeneracion.getCol(); columna++) {
                siguienteGeneracion.set_item(fila, columna, new Celula(fila, columna));
            }
        }
    }

    private void cargarCoordenadas(int filas, int columnas) {
        // Reducir en 1, empieza en 0 el index de la cuadrícula
        filas--;
        columnas--;
        // Coordenadas de los vecinos
        desplazamientosVecinos.add(new Coordenada(-1, -1));
        desplazamientosVecinos.add(new Coordenada(-1, 0));
        desplazamientosVecinos.add(new Coordenada(-1, 1));
        desplazamientosVecinos.add(new Coordenada(0, -1));
        desplazamientosVecinos.add(new Coordenada(0, 1));
        desplazamientosVecinos.add(new Coordenada(1, -1));
        desplazamientosVecinos.add(new Coordenada(1, 0));
        desplazamientosVecinos.add(new Coordenada(1, 1));

    }

    public void actualizarGeneracion() {
        actualizarCelulas(generacionActual, siguienteGeneracion);
        ADTArray2D<Celula> temp = generacionActual;
        generacionActual = siguienteGeneracion;
        siguienteGeneracion = temp;
    }

    private void actualizarCelulas(ADTArray2D<Celula> generacionAActualizar, ADTArray2D<Celula> celdaAGuardar) {
        for (int fila = 0; fila < generacionAActualizar.getRow(); fila++) {
            for (int columna = 0; columna < generacionAActualizar.getCol(); columna++) {
                actualizarSiguienteGeneracion(generacionAActualizar.get_item(fila, columna),
                        celdaAGuardar.get_item(fila, columna));
            }
        }
    }

    private void actualizarSiguienteGeneracion(Celula celula, Celula celulaAGuardar) {
        boolean estaViva = celula.isEstaVivo();
        int vecinosVivos = cuantosVecinosVivosTiene(celula);
        celulaAGuardar.setEstaVivo(false);
        if (estaViva && (vecinosVivos == 2 || vecinosVivos == 3)) {
            celulaAGuardar.setEstaVivo(true);
        } else if (!estaViva && vecinosVivos == 3) {
            celulaAGuardar.setEstaVivo(true);
        }
    }

    private int cuantosVecinosVivosTiene(Celula celula) {
        int vecinosvivos = 0;
        vecinosvivos = contarVecinosVivos(celula);
        return vecinosvivos;
    }

    private int contarVecinosVivos(Celula celula) {
        int vecinosVivos = 0;
        for (Coordenada desplazamiento : desplazamientosVecinos) {
            int nuevaFila = celula.getFila() + desplazamiento.getFila();
            int nuevaColumna = celula.getColumna() + desplazamiento.getColumna();
            if (esCeldaValida(nuevaFila, nuevaColumna) && generacionActual.get_item(nuevaFila, nuevaColumna).isEstaVivo()) {
                vecinosVivos++;
            }
        }
        return vecinosVivos;
    }

    private boolean esCeldaValida(int fila, int columna) {
        boolean res = false;
        if (fila >= 0 && fila < generacionActual.getRow() && columna >= 0 && columna < generacionActual.getCol()) {
            res = true;
        }
        return res;
    }

    public ADTArray2D<Celula> getGeneracionActual() {
        return generacionActual;
    }
}
