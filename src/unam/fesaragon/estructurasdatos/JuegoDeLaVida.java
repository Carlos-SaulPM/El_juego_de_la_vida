package unam.fesaragon.estructurasdatos;

import java.util.ArrayList;

public class JuegoDeLaVida {
    //Hecho por Carlos Saul Paz Maldonado
    private ArrayList<Coordenada> coordenadasCentro = new ArrayList<>();
    private ArrayList<Coordenada> coordenadasEsquinas = new ArrayList<>();
    private ArrayList<Coordenada> desplazamientosVecinos = new ArrayList<>();
    private ArrayList<Coordenada> coordenadaslaterales = new ArrayList<>();
    private ADTArray2D<Celula> generacionActual;
    private ADTArray2D<Celula> siguienteGeneracion;
    private int generacionFinal;
    private final int TIEMPO_MILISEGUNDOS = 800;


    public JuegoDeLaVida(ADTArray2D<Celula> generacionActual, int generacionFinal) {
        this.generacionActual = generacionActual;
        this.generacionFinal = generacionFinal;
        this.siguienteGeneracion = new ADTArray2D<Celula>(generacionActual.getRow(), generacionActual.getCol());
        cargarCoordenadas(generacionActual.getRow(), generacionActual.getCol());
    }

    private void cargarCoordenadas(int filas, int columnas) {
        //Reducir en 1, empieza en 0 el index de la cuadricula
        filas--;
        columnas--;
        //Coordenadas de los limites centrales
        coordenadasCentro.add(new Coordenada(1, 1));
        coordenadasCentro.add(new Coordenada(1, columnas - 1));
        coordenadasCentro.add(new Coordenada(filas - 1, columnas - 1));
        coordenadasCentro.add(new Coordenada(filas - 1, 1));
        //Coordenadas de las esquinas
        coordenadasEsquinas.add(new Coordenada(0, 0));
        coordenadasEsquinas.add(new Coordenada(0, columnas));
        coordenadasEsquinas.add(new Coordenada(filas, columnas));
        coordenadasEsquinas.add(new Coordenada(filas, 0));
        //Coordenadas de los vecinos
        desplazamientosVecinos.add(new Coordenada(-1, -1));
        desplazamientosVecinos.add(new Coordenada(-1, 0));
        desplazamientosVecinos.add(new Coordenada(-1, 1));
        desplazamientosVecinos.add(new Coordenada(0, -1));
        desplazamientosVecinos.add(new Coordenada(0, 1));
        desplazamientosVecinos.add(new Coordenada(1, -1));
        desplazamientosVecinos.add(new Coordenada(1, 0));
        desplazamientosVecinos.add(new Coordenada(1, 1));
        //Coordenadas de los laterales
        coordenadaslaterales.add(new Coordenada(coordenadasEsquinas.get(0).getFila(),coordenadasEsquinas.get(0).getColumna()+1));
        coordenadaslaterales.add(new Coordenada(coordenadasEsquinas.get(1).getFila(),coordenadasEsquinas.get(1).getColumna()-1));
        coordenadaslaterales.add(new Coordenada(coordenadasEsquinas.get(1).getFila()+1,coordenadasEsquinas.get(1).getColumna()));
        coordenadaslaterales.add(new Coordenada(coordenadasEsquinas.get(2).getFila()+1,coordenadasEsquinas.get(2).getColumna()));
        coordenadaslaterales.add(new Coordenada(coordenadasEsquinas.get(2).getFila(),coordenadasEsquinas.get(2).getColumna()+1));
        coordenadaslaterales.add(new Coordenada(coordenadasEsquinas.get(3).getFila(),coordenadasEsquinas.get(3).getColumna()+1));
        coordenadaslaterales.add(new Coordenada(coordenadasEsquinas.get(3).getFila()+1,coordenadasEsquinas.get(3).getColumna()));
        coordenadaslaterales.add(new Coordenada(coordenadasEsquinas.get(0).getFila()+1,coordenadasEsquinas.get(3).getColumna()));

    }


    public void actualizarGeneracion() {
        actualizarCelulas(generacionActual, siguienteGeneracion);

    }

    private void actualizarCelulas(ADTArray2D<Celula> generacionAActualizar, ADTArray2D<Celula> celdaAGuardar) {
        for (int fila = 0; fila < generacionAActualizar.getRow(); fila++) {
            for (int columna = 0; columna < generacionAActualizar.getCol(); columna++) {
                actualizarSiguienteGeneracion(generacionAActualizar.get_item(fila, columna), celdaAGuardar.get_item(fila,columna));
            }
        }
    }

    private void actualizarSiguienteGeneracion(Celula celula, Celula celulaAGuardar) {
        boolean estaViva = celula.isEstaVivo();
        int vecinosVivos = cuantosVecinosVivosTiene(celula);

        // En lugar de crear una nueva célula, modifica la existente
        if (estaViva && (vecinosVivos < 2 || vecinosVivos > 3)) {
            celulaAGuardar.setEstaVivo(false);
        } else if (!estaViva && vecinosVivos == 3) {
            celulaAGuardar.setEstaVivo(true);
        }
    }



    private int cuantosVecinosVivosTiene(Celula celula) {
        int vecinosvivos = 0;
        boolean celulaEstaEnMedio = verificarCelulaEnCuadriculaCentro(celula);
        if (celulaEstaEnMedio) {
            vecinosvivos = calcVecinosVivosEnMedio(celula);
        }
        //Celula en los lados o esquinas
        if (!celulaEstaEnMedio) {
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
        boolean laCelulaEstaEnElMedio = false;
        if (celula.getFila() >= coordenadasCentro.get(0).getFila() && celula.getFila() <= coordenadasCentro.get(3).getFila()) {
            if (celula.getColumna() >= coordenadasCentro.get(0).getColumna() && celula.getColumna() <= coordenadasCentro.get(1).getColumna()) {
                laCelulaEstaEnElMedio = true;
            }
        }
        return laCelulaEstaEnElMedio;
    }

    private int calcVecinosVivosEnMedio(Celula celula) {
        int vecinosvivos = 0;
        int celulaFila = celula.getFila();
        int celulaColumna = celula.getColumna();
        ArrayList<Coordenada> vecinos = new Coordenada(celula.getFila(), celula.getColumna()).obtenerVecinos();
        for (Coordenada coordenada : vecinos) {
            if (siguienteGeneracion.get_item(coordenada.getFila(), coordenada.getColumna()).isEstaVivo()) {
                vecinosvivos++;
            }
        }
        return vecinosvivos;
    }

    private boolean estaEnUnaEsquina(Celula celula) {
        boolean laCelulaEstaEnUnaEsquina = false;
        //ESQUINA
        for (Coordenada coordenada : coordenadasEsquinas) {
            if (celula.getFila() == coordenada.getFila() && celula.getColumna() == coordenada.getColumna()) {
                laCelulaEstaEnUnaEsquina = true;
            }
        }
        return laCelulaEstaEnUnaEsquina;
    }

    private int calcVecinosVivosEsquina(Celula celula) {
        int fila = celula.getFila();
        int columna = celula.getColumna();
        int vecinosVivos = 0;
        boolean estaArriba = fila == coordenadasEsquinas.get(0).getFila();
        if (estaArriba) {//Esquinas superiores
            boolean estaEnEsquinaSupIzquierda = (columna == coordenadasEsquinas.get(0).getColumna());
            if (estaEnEsquinaSupIzquierda) {
                vecinosVivos = vecinoVivoEn(celula, new int[]{4, 6, 7});
            }
            if (!estaEnEsquinaSupIzquierda) {//Esta en la derecha
                vecinosVivos = vecinoVivoEn(celula, new int[]{3,5,6});
            }

        }

        if (!estaArriba) {//Esquinas inferiores
            boolean estaEnEsquinaInfIzquierda = (columna == coordenadasEsquinas.get(3).getColumna());
            if (estaEnEsquinaInfIzquierda) {
                vecinosVivos = vecinoVivoEn(celula, new int[]{1, 2, 4});
            }
            if (!estaEnEsquinaInfIzquierda) {//Esta en la derecha
                vecinosVivos = vecinoVivoEn(celula, new int[]{0,1,3});
            }
        }
        return vecinosVivos;
    }

    private int vecinoVivoEn(Celula celula, int[] posicionVecino) {
        int cantidadVivos = 0;
        for (int pos : posicionVecino) {
            Coordenada coordenadaVecino = new Coordenada(
                    celula.getFila() + desplazamientosVecinos.get(pos).getFila(),
                    celula.getColumna() + desplazamientosVecinos.get(pos).getColumna()
            );
            if (generacionActual.get_item(coordenadaVecino.getFila(), coordenadaVecino.getColumna()).isEstaVivo()) {
                cantidadVivos++;
            }
        }
        return cantidadVivos;
    }


    private int calcVecinosVivosLaterales(Celula celula) {
        int lateral = obtenerElLateral(celula);
        int vecinosVivos = 0;
        for (int cincoVecinos = 0; cincoVecinos < 5; cincoVecinos++) {
            if (lateral==1 || lateral==3){//Lateral superior o inferior
                if (lateral==1){
                    vecinosVivos=vecinoVivoEn(celula, new int[]{3,4,5,6,7});
                }else{
                    vecinosVivos=vecinoVivoEn(celula, new int[]{0,1,2,3,4});
                }
            }else {//Laterales izquierdo o derecho
                if (lateral==2){
                    vecinosVivos=vecinoVivoEn(celula,new int[]{0,1,3,5,6});
                }else {
                    vecinosVivos=vecinoVivoEn(celula,new int[]{1,2,4,7,6});
                }
            }
        }

        return vecinosVivos;
    }


    private int obtenerElLateral(Celula celula) {
        int fila = celula.getFila();
        int columna = celula.getColumna();

        if (fila == 0) return 1; // Lateral superior
        if (columna == generacionActual.getCol() - 1) return 2; // Lateral derecho
        if (fila == generacionActual.getRow() - 1) return 3; // Lateral inferior
        if (columna == 0) return 4; // Lateral izquierdo

        return 0; // No está en los laterales
    }


    public ADTArray2D<Celula> getGeneracionActual() {
        return generacionActual;
    }
}
