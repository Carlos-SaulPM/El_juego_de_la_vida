package unam.fesaragon.estructurasdatos;

public class Consola {
    // Metodo para inicializar la cuadrícula
    public static ADTArray2D<Celula> inicializarCuadricula(int filas, int columnas) {
        ADTArray2D<Celula> cuadricula = new ADTArray2D<>(filas, columnas);
        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < columnas; col++) {
                boolean estadoInicial = (fila == col) || (fila + col == columnas - 1);
                cuadricula.set_item(fila, col, new Celula(estadoInicial, col, fila));
            }
        }
        return cuadricula;
    }

    // Metodo para imprimir la cuadrícula en la consola
    public static void imprimirCuadricula(ADTArray2D<Celula> cuadricula) {
        for (int fila = 0; fila < cuadricula.getRow(); fila++) {
            for (int col = 0; col < cuadricula.getCol(); col++) {
                Celula celula = cuadricula.get_item(fila, col);
                System.out.print(cuadricula.get_item(fila, col) + " ");
            }
            System.out.println();
        }
    }

    // Metodo auxiliar para pausar la ejecución
    public static void esperar(int milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Metodo para ejecutar el juego de la vida
    public static void ejecutarJuego(int filas, int columnas, int generaciones) {
        ADTArray2D<Celula> cuadricula = inicializarCuadricula(filas, columnas);
        JuegoDeLaVida juego = new JuegoDeLaVida(cuadricula, generaciones);

        for (int i = 0; i < generaciones; i++) {
            System.out.println("Generación " + (i + 1) + ":");
            imprimirCuadricula(juego.getGeneracionActual());
            juego.actualizarGeneracion(); // Avanzar a la siguiente generación
            esperar(500); // Pausa entre generaciones
        }
    }
}
