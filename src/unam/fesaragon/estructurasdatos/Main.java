package unam.fesaragon.estructurasdatos;

public class Main {
    public static void main(String[] args) {
        // Definir el tamaño de la cuadrícula
        int filas = 5;
        int columnas =5;

        // Crear la cuadricula de células
        ADTArray2D<Celula> cuadricula = new ADTArray2D<>(filas, columnas);

        // Inicializar las células con una X
        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < columnas; col++) {
                boolean estadoInicial = (fila == col) || (fila + col == columnas - 1);
                cuadricula.set_item(fila, col, new Celula(estadoInicial, col, fila));
            }
        }

        // Crear el juego de la vida
        int generaciones = 2; // Número de generaciones que queremos simular
        JuegoDeLaVida juego = new JuegoDeLaVida(cuadricula, generaciones);

        // Ejecutar el juego y visualizar cada generación
        for (int i = 0; i < generaciones; i++) {
            System.out.println("Generación " + (i + 1) + ":");
            imprimirCuadricula(juego.getCuadricula());
            juego.actualizarGeneracion(); // Avanzar a la siguiente generación
            esperar(900); // Pausa entre generaciones
        }
    }

    // Metodo para imprimir la cuadrícula en la consola
    public static void imprimirCuadricula(ADTArray2D<Celula> cuadricula) {
        for (int fila = 0; fila < cuadricula.getRow(); fila++) {
            for (int col = 0; col < cuadricula.getCol(); col++) {
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
}
