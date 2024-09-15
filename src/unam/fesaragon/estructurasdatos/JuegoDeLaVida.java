package unam.fesaragon.estructurasdatos;

import java.util.ArrayList;

public class JuegoDeLaVida {
    //Hecho por Carlos Saul Paz Maldonado
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
        this.generacionActual = new ADTArray2D<>(cuadricula.getRow(), cuadricula.getCol());
        this.generacionActual.copiarEstadoDe(cuadricula);
        cargarLimites(cuadricula.getRow(), cuadricula.getCol());
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
        limitesExtremos.add(new Integer[]{0, columnas});
        //Inferiores
        limitesExtremos.add(new Integer[]{filas, 0});
        limitesExtremos.add(new Integer[]{filas, columnas});
    }

    private void actualizarGeneracion() {
        actualizarCelulas(generacionActual);
        cuadricula.copiarEstadoDe(generacionActual);
    }

    private void actualizarCelulas(ADTArray2D<Celula> generacionAActualizar) {
        for (int columna = 0; columna < generacionAActualizar.getCol(); columna++) {
            for (int fila = 0; fila < generacionAActualizar.getRow(); fila++) {
                actualizarSiguienteGeneracion(generacionAActualizar.get_item(fila, columna));
            }
        }
    }

    private void actualizarSiguienteGeneracion(Celula celula) {
        boolean estaViva = celula.isEstaVivo();
        int vecinosVivos = cuantosVecinosVivosTiene(celula);
        boolean regla1 = regla1(celula.getFila(), celula.getColumna(), vecinosVivos);//Sobrevive si es verdadera
        if (!regla1 && estaViva){
            boolean regla2 = regla2(celula.getFila(), celula.getColumna(), vecinosVivos); //Muere por soledad
            boolean regla3 = regla3(celula.getFila(), celula.getColumna(), vecinosVivos); //Muere por sobrepoblacion
            if (regla2 || regla3){
                celula.setEstaVivo(false);
            }
        }else {
            //Regla 4
            celula.setEstaVivo(true);
        }
    }

    private boolean regla1(int fila, int columna, int vecinosVivos) {
        return false;
    }

    private boolean regla2(int fila, int columna, int vecinosVivos) {
        return false;
    }

    private boolean regla3(int fila, int columna, int vecinosVivos) {
        return false;
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
        for (int indexLimitesCentro = 0; indexLimitesCentro < limitesCentro.size(); indexLimitesCentro++) {
            if (celula.getFila() == limitesCentro.get(indexLimitesCentro)[0] && celula.getColumna() == limitesCentro.get(indexLimitesCentro)[1])
                laCelulaEstaEnElMedio = true;
        }
        return laCelulaEstaEnElMedio;
    }

    private int calcVecinosVivosEnMedio(Celula celula) {
        int vecinosvivos = 0;
        int celulaFila = celula.getFila();
        int celulaColumna = celula.getColumna();
        for (int vecinoFila = celulaFila - 1; vecinoFila <= celulaFila + 1; vecinoFila++) {
            for (int vecinoColumna = celulaColumna - 1; vecinoColumna <= celulaColumna + 1; vecinoColumna++) {
                if (vecinoFila == celulaFila && vecinoColumna == celulaColumna) continue;
                if (generacionActual.get_item(vecinoFila, vecinoColumna).isEstaVivo()) vecinosvivos++;
            }
        }
        return vecinosvivos;
    }

    private boolean estaEnUnaEsquina(Celula celula) {
        boolean laCelulaEstaEnUnaEsquina = false;
        //ESQUINA
        for (int indexLimitesExtremos = 0; indexLimitesExtremos < limitesExtremos.size(); indexLimitesExtremos++) {
            if (celula.getFila() == limitesExtremos.get(indexLimitesExtremos)[0] && celula.getColumna() == limitesExtremos.get(indexLimitesExtremos)[1])
                laCelulaEstaEnUnaEsquina = true;
        }
        return laCelulaEstaEnUnaEsquina;
    }

    private int calcVecinosVivosEsquina(Celula celula) {
        int fila = celula.getFila();
        int columna = celula.getColumna();
        int esquina = obtenerLaEsquina(celula);
        int vecinosVivos = 0;
        int orientacionIzquierdaODerecha = 1; // izquierda = 1, derecha = -1
        int orientacionArribaOAbajo = 1; // arriba = 1, abajo = -1
        //Determinar la orientacion horizontal (Izquierda o derecha)
        if (esquina == ESQUINA_SUPERIOR_DER || esquina == ESQUINA_INFERIOR_DER) {
            orientacionIzquierdaODerecha = -1;
        }
        //Determinar la orientacion vertical (Arriba o abajo)
        if (esquina == ESQUINA_INFERIOR_IZQ || esquina == ESQUINA_INFERIOR_DER) {
            orientacionArribaOAbajo = -1;
        }

        if (generacionActual.get_item(fila, columna + orientacionIzquierdaODerecha).isEstaVivo()) vecinosVivos++;
        if (generacionActual.get_item(fila + orientacionArribaOAbajo, columna + orientacionIzquierdaODerecha).isEstaVivo())
            vecinosVivos++;
        if (generacionActual.get_item(fila + orientacionArribaOAbajo, columna).isEstaVivo()) vecinosVivos++;

        return vecinosVivos;
    }

    private int obtenerLaEsquina(Celula celula) {
        int esquinaEnLaQueEsta = 0;
        //ESQUINA
        for (int indexLimitesExtremos = 0; indexLimitesExtremos < limitesExtremos.size(); indexLimitesExtremos++) {
            if (celula.getFila() == limitesExtremos.get(indexLimitesExtremos)[0] && celula.getColumna() == limitesExtremos.get(indexLimitesExtremos)[1]) {
                esquinaEnLaQueEsta = indexLimitesExtremos + 1;
                break;
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
        return generacionActual.get_item(fila, columna).isEstaVivo() ? 1 : 0;
    }

    private int obtenerElLateral(Celula celula) {
        int esquinaEnLaQueEsta = 0;
        //ESQUINA
        for (int indexLimitesExtremos = 0; indexLimitesExtremos < limitesExtremos.size(); indexLimitesExtremos += 4) {
            if (celula.getFila() == limitesExtremos.get(indexLimitesExtremos)[0]) {
                if (indexLimitesExtremos == 0) {
                    esquinaEnLaQueEsta = 1;
                } else {
                    esquinaEnLaQueEsta = 3;
                }
                break;
            }
            if (celula.getColumna() == limitesCentro.get(indexLimitesExtremos)[1]) {
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


}
