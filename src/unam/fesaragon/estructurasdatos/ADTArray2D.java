package unam.fesaragon.estructurasdatos;

public class ADTArray2D<T> {
    private int re;
    private int col;
    private T array[][];

//  Constructor de la clase
    public ADTArray2D(int re, int col) {
        this.re = re;
        this.col = col;
        this.array = (T[][]) new Object[re][col];
    }
//  Metodo limpiar
    public void clear(T dato) {
        for (int i = 0; i < re; i++) {
            for (int j = 0; j < col; j++) {
                this.array[i][j] = dato;
            }
        }
    }

//  Colocar item en la coordenada específica
    public void set_item(int renglon, int columna, T dato){
        this.array[renglon][columna] = dato;
    }

//   Obtener item en la coordenada específica
    public T get_item(int renglon, int columna){
        return this.array[renglon][columna];
    }

    public int getRow() {
        return re;
    }

    public int getCol() {
        return col;
    }

    @Override
    public String toString() {
        String matriz= "";
        for (T[] elemento : this.array) {
            for (T el : elemento) {
                matriz= matriz+el+" ";
            }
            matriz+="\n";
        }
        return matriz;
    }
}
