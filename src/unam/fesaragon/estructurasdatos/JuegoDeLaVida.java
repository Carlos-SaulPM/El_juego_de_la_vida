package unam.fesaragon.estructurasdatos;

import java.util.ArrayList;

public class JuegoDeLaVida {
    // Hecho por Carlos Saul Paz Maldonado
    private ArrayList<Coordenada> coordenadasCentro = new ArrayList<>();
    private ArrayList<Coordenada> coordenadasEsquinas = new ArrayList<>();
    private ArrayList<Coordenada> desplazamientosVecinos = new ArrayList<>();
    private ADTArray2D<Celula> generacionActual;
    private ADTArray2D<Celula> siguienteGeneracion;
    private int generacionFinal;

    public JuegoDeLaVida(ADTArray2D<Celula> generacionActual, int generacionFinal) {
        this.generacionActual = generacionActual;
        this.generacionFinal = generacionFinal;
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

        // Coordenadas de los límites centrales
        coordenadasCentro.add(new Coordenada(1, 1));
        coordenadasCentro.add(new Coordenada(1, columnas - 1));
        coordenadasCentro.add(new Coordenada(filas - 1, columnas - 1));
        coordenadasCentro.add(new Coordenada(filas - 1, 1));

        // Coordenadas de las esquinas
        coordenadasEsquinas.add(new Coordenada(0, 0)); // Esquina superior izquierda
        coordenadasEsquinas.add(new Coordenada(0, columnas)); // Esquina superior derecha
        coordenadasEsquinas.add(new Coordenada(filas, columnas)); // Esquina inferior derecha
        coordenadasEsquinas.add(new Coordenada(filas, 0)); // Esquina inferior izquierda

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
        boolean celulaEstaEnMedio = verificarCelulaEnCuadriculaCentro(celula);
        if (celulaEstaEnMedio) {
            vecinosvivos = calcVecinosVivosEnMedio(celula);
        } else {
            boolean estaEnUnaEsquina = estaEnUnaEsquina(celula);
            if (estaEnUnaEsquina) {
                vecinosvivos = calcVecinosVivosEsquina(celula);
            } else {
                vecinosvivos = calcVecinosVivosLaterales(celula);
            }
        }
        return vecinosvivos;
    }

    private boolean verificarCelulaEnCuadriculaCentro(Celula celula) {
        boolean res = false;
        if (celula.getFila() >= coordenadasCentro.get(0).getFila() &&
                celula.getFila() <= coordenadasCentro.get(3).getFila() &&
                celula.getColumna() >= coordenadasCentro.get(0).getColumna() &&
                celula.getColumna() <= coordenadasCentro.get(1).getColumna()){
            res = true;
        }
        return res;
    }

    private int calcVecinosVivosEnMedio(Celula celula) {
        int vecinosvivos = 0;
        for (Coordenada desplazamiento : desplazamientosVecinos) {
            int nuevaFila = celula.getFila() + desplazamiento.getFila();
            int nuevaColumna = celula.getColumna() + desplazamiento.getColumna();
            if (esCeldaValida(nuevaFila, nuevaColumna) &&
                    generacionActual.get_item(nuevaFila, nuevaColumna).isEstaVivo()) {
                vecinosvivos++;
            }
        }
        return vecinosvivos;
    }

    private boolean estaEnUnaEsquina(Celula celula) {
        for (Coordenada esquina : coordenadasEsquinas) {
            if (celula.getFila() == esquina.getFila() && celula.getColumna() == esquina.getColumna()) {
                return true;
            }
        }
        return false;
    }

    private int calcVecinosVivosEsquina(Celula celula) {
        int vecinosVivos = 0;
        boolean estaEnEsquinaSupIzquierda = (celula.getFila() == 0 && celula.getColumna() == 0);
        boolean estaEnEsquinaSupDerecha = (celula.getFila() == 0 && celula.getColumna() == generacionActual.getCol() - 1);
        boolean estaEnEsquinaInfDerecha = (celula.getFila() == generacionActual.getRow() - 1 && celula.getColumna() == generacionActual.getCol() - 1);
        boolean estaEnEsquinaInfIzquierda = (celula.getFila() == generacionActual.getRow() - 1 && celula.getColumna() == 0);

        if (estaEnEsquinaSupIzquierda || estaEnEsquinaSupDerecha || estaEnEsquinaInfDerecha || estaEnEsquinaInfIzquierda) {
            vecinosVivos = contarVecinosVivos(celula);
        }

        return vecinosVivos;
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


    private int calcVecinosVivosLaterales(Celula celula) {
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
        boolean res=false;
        if (fila >= 0 && fila < generacionActual.getRow() && columna >= 0 && columna < generacionActual.getCol()){
            res = true;
        }
        return res;
    }

    public ADTArray2D<Celula> getGeneracionActual() {
        return generacionActual;
    }
}
