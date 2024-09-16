package unam.fesaragon.estructurasdatos;

import java.util.ArrayList;

public class JuegoDeLaVida {
    //Hecho por Carlos Saul Paz Maldonado
    private ArrayList<Coordenada> coordenadasCentro = new ArrayList<>();
    private ArrayList<Coordenada> coordenadasEsquinas = new ArrayList<>();


//    private ArrayList<Coordenada> coordenadasBordes = new ArrayList<>();
    private ADTArray2D<Celula> cuadricula;
    private ADTArray2D<Celula> generacionActual;
    private ArrayList<Integer[]> limitesCentro = new ArrayList<>();
    private ArrayList<Integer[]> limitesExtremos = new ArrayList<>();
    private int generacionFinal;
    private final int TIEMPO_MILISEGUNDOS = 800;
    //Sentido horario
    private final int ESQUINA_SUPERIOR_IZQ = 1;
    private final int ESQUINA_SUPERIOR_DER = 2;
    private final int ESQUINA_INFERIOR_IZQ = 3;
    private final int ESQUINA_INFERIOR_DER = 4;
    //Sentido antihorario
    private final int LATERAL_SUPERIOR = 1;
    private final int LATERAL_IZQUIERDO = 2;
    private final int LATERAL_INFERIOR = 3;
    private final int LATERAL_DERECHO = 4;

    public JuegoDeLaVida(ADTArray2D<Celula> cuadricula, int generacionFinal) {
        this.cuadricula = cuadricula;
        this.generacionFinal = generacionFinal;
        this.generacionActual = new ADTArray2D<Celula>(cuadricula.getRow(), cuadricula.getCol());
        this.generacionActual.copiarEstadoDe(cuadricula);
        cargarLimites(cuadricula.getRow(), cuadricula.getCol());
        cargarCoordenadas(cuadricula.getRow(), cuadricula.getCol());
    }

    private void cargarCoordenadas(int filas, int columnas) {
        //Reducir en 1, empieza en 0 el index de la cuadricula
        filas--;
        columnas--;
        //Coordenadas de los limites centrales
        coordenadasCentro.add(new Coordenada(1,1));
        coordenadasCentro.add(new Coordenada(1,columnas-1));
        coordenadasCentro.add(new Coordenada(filas-1,columnas-1));
        coordenadasCentro.add(new Coordenada(filas-1,1));
        //Coordenadas de las esquinas
        coordenadasEsquinas.add(new Coordenada(0,0));
        coordenadasEsquinas.add(new Coordenada(0,columnas));
        coordenadasEsquinas.add(new Coordenada(filas,columnas));
        coordenadasEsquinas.add(new Coordenada(filas,0));

    }

    private void cargarLimites(int filas, int columnas) {
        //Limites Centro
        //Superiores
        limitesCentro.add(new Integer[]{1, 1});
        limitesCentro.add(new Integer[]{1, columnas - 1});
        //Inferiores
        limitesCentro.add(new Integer[]{filas - 1, 1});
        limitesCentro.add(new Integer[]{filas - 1, columnas - 1});
        //Limites extremos
        limitesExtremos.add(new Integer[]{0, 0});
        limitesExtremos.add(new Integer[]{0, columnas - 1});
        limitesExtremos.add(new Integer[]{filas - 1, 0});
        limitesExtremos.add(new Integer[]{filas - 1, columnas - 1});

    }

    public void actualizarGeneracion() {
        actualizarCelulas(generacionActual);
        cuadricula.copiarEstadoDe(generacionActual);
    }

    private void actualizarCelulas(ADTArray2D<Celula> generacionAActualizar) {
        for (int fila = 0; fila < generacionAActualizar.getRow(); fila++) {
            for (int columna = 0; columna < generacionAActualizar.getCol(); columna++) {
                actualizarSiguienteGeneracion(generacionAActualizar.get_item(fila, columna));
            }
        }
    }

    private void actualizarSiguienteGeneracion(Celula celula) {
        boolean estaViva = celula.isEstaVivo();
        int vecinosVivos = cuantosVecinosVivosTiene(celula);

        System.out.println("Celula en (" + celula.getFila() + ", " + celula.getColumna() + ") - Estado actual: " + (estaViva ? "Viva" : "Muerta") + ", Vecinos vivos: " + vecinosVivos);

        if (estaViva && (vecinosVivos < 2 || vecinosVivos > 3)) {
            celula.setEstaVivo(false);
        } else if (!estaViva && vecinosVivos == 3) {
            celula.setEstaVivo(true);
        }

        System.out.println("Nuevo estado: " + (celula.isEstaVivo() ? "Viva" : "Muerta"));
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
        if (celula.getFila()>= coordenadasCentro.getFirst().getFila() && celula.getFila()<=coordenadasCentro.get(3).getFila()){
            if (celula.getColumna() >=coordenadasCentro.getFirst().getColumna() && celula.getColumna()<= coordenadasCentro.get(1).getColumna()){
                laCelulaEstaEnElMedio = true;
            }
        }
        return laCelulaEstaEnElMedio;
    }

    private int calcVecinosVivosEnMedio(Celula celula) {
        int vecinosvivos = 0;
        int celulaFila = celula.getFila();
        int celulaColumna = celula.getColumna();
        ArrayList<Coordenada> vecinos = new Coordenada(celula.getFila(),celula.getColumna()).obtenerVecinos();
        for (Coordenada coordenada: vecinos){
            if (generacionActual.get_item(coordenada.getFila(),coordenada.getColumna()).isEstaVivo()){
                vecinosvivos++;
            }
        }
        return vecinosvivos;
    }

    private boolean estaEnUnaEsquina(Celula celula) {
        boolean laCelulaEstaEnUnaEsquina = false;
        //ESQUINA
        for (Coordenada coordenada : coordenadasEsquinas){
            if (celula.getFila() == coordenada.getFila() && celula.getColumna() == coordenada.getColumna()){
                laCelulaEstaEnUnaEsquina=true;
            }
        }
        return laCelulaEstaEnUnaEsquina;
    }

    private int calcVecinosVivosEsquina(Celula celula) {
        int fila = celula.getFila();
        int columna = celula.getColumna();
        Coordenada coordenadaDeLaEsquina = coordenadaDeLaEsquina(celula);
        int vecinosVivos = 0;
        //Superior izquierda o inferior
        if (coordenadaDeLaEsquina.getFila()==coordenadasEsquinas.getFirst().getFila() || coordenadaDeLaEsquina.getFila()==coordenadasEsquinas.get(3).getFila()){

        }
        return vecinosVivos;
    }


    private Coordenada coordenadaDeLaEsquina(Celula celula) {
        Coordenada esquinaEnLaQueEsta = new Coordenada();
        //ESQUINA
        for (Coordenada coordenada : coordenadasEsquinas){
            if (celula.getFila() == coordenada.getFila() && celula.getColumna() == coordenada.getColumna()){
                esquinaEnLaQueEsta.setFila(coordenada.getFila());
                esquinaEnLaQueEsta.setColumna(coordenada.getColumna());
            }
        }

        return esquinaEnLaQueEsta;
    }

    private int calcVecinosVivosLaterales(Celula celula) {
        int fila = celula.getFila();
        int columna = celula.getColumna();
        int lateral = obtenerElLateral(celula);
        int vecinosVivos = 0;
        int incrementoFila = 0;
        int incrementoColumna = 0;

        // Definir dirección de desplazamiento según el lateral
        if (lateral == LATERAL_SUPERIOR || lateral == LATERAL_INFERIOR) {
            // Determinar incremento de la fila, superior o inferior
            incrementoFila = (lateral == LATERAL_SUPERIOR) ? 1 : -1;

            // Verificar vecinos alrededor
            vecinosVivos += estaViva(fila, columna - 1); // izquierda
            vecinosVivos += estaViva(fila, columna + 1); // derecha
            vecinosVivos += estaViva(fila + incrementoFila, columna - 1); // arriba / abajo-izquierda
            vecinosVivos += estaViva(fila + incrementoFila, columna); // arriba/abajo
            vecinosVivos += estaViva(fila + incrementoFila, columna + 1); // arriba/abajo-derecha

        } else if (lateral == LATERAL_IZQUIERDO || lateral == LATERAL_DERECHO) {
            // Determinar incremento de la columna, superior o inferior
            incrementoColumna = (lateral == LATERAL_IZQUIERDO) ? 1 : -1;
            // Verificar vecinos alrededor
            vecinosVivos += estaViva(fila - 1, columna); // arriba
            vecinosVivos += estaViva(fila + 1, columna); // abajo
            vecinosVivos += estaViva(fila - 1, columna + incrementoColumna); // arriba-izquierda/derecha
            vecinosVivos += estaViva(fila, columna + incrementoColumna); // izquierda/derecha
            vecinosVivos += estaViva(fila + 1, columna + incrementoColumna); // abajo-izquierda/derecha
        }

        return vecinosVivos;
    }


    // Metodo auxiliar para verificar si una célula está viva
    private int estaViva(int fila, int columna) {
        if (fila >= 0 && fila < generacionActual.getRow() && columna >= 0 && columna < generacionActual.getCol()) {
            return generacionActual.get_item(fila, columna).isEstaVivo() ? 1 : 0;
        }
        return 0;
    }


    private int obtenerElLateral(Celula celula) {
        int esquinaEnLaQueEsta = 0;
        //ESQUINA
        for (int indexLimitesExtremos = 0; indexLimitesExtremos < limitesExtremos.size(); indexLimitesExtremos += 3) {
            if (celula.getFila() == limitesExtremos.get(indexLimitesExtremos)[0]) {
                if (indexLimitesExtremos == 0) {
                    esquinaEnLaQueEsta = 1;
                } else {
                    esquinaEnLaQueEsta = 3;
                }
                break;
            }
            if (celula.getColumna() == limitesExtremos.get(indexLimitesExtremos)[1]) {
                if (indexLimitesExtremos == 0) {
                    esquinaEnLaQueEsta = 2;
                } else {
                    esquinaEnLaQueEsta = 4;
                }
                break;
            }
        }
        return esquinaEnLaQueEsta;
    }

    public ADTArray2D<Celula> getCuadricula() {
        return cuadricula;
    }
}
