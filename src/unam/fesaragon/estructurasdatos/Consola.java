package unam.fesaragon.estructurasdatos;

import java.util.Random;

public class Consola {
    // Metodo para inicializar la cuadrícula con estados aleatorios
    private static ADTArray2D<Celula> inicializarCuadricula(int filas, int columnas) {
        ADTArray2D<Celula> cuadricula = new ADTArray2D<>(filas, columnas);
        Random random = new Random();
        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < columnas; col++) {
                boolean estadoInicial = random.nextBoolean();
                cuadricula.set_item(fila, col, new Celula(estadoInicial, col, fila));
            }
        }
        return cuadricula;
    }

    private static void imprimirCuadricula(ADTArray2D<Celula> cuadricula) {
        for (int fila = 0; fila < cuadricula.getRow(); fila++) {
            for (int col = 0; col < cuadricula.getCol(); col++) {
                Celula celula = cuadricula.get_item(fila, col);
                System.out.print(cuadricula.get_item(fila, col) + " ");
            }
            System.out.println();
        }
    }

    private static void esperar(int milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void iniciar(int filas, int columnas, int generaciones) {
        ADTArray2D<Celula> cuadricula = inicializarCuadricula(filas, columnas);
        JuegoDeLaVida juego = new JuegoDeLaVida(cuadricula);

        for (int i = 0; i < generaciones; i++) {
            System.out.println("Generación " + (i + 1) + ":");
            imprimirCuadricula(juego.getGeneracionActual());
            juego.actualizarGeneracion();
            esperar(500);
        }
    }
}
