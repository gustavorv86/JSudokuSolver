
package sudoku;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Sudoku {
    
    public final static int CELL_EMPTY = 0;
    
    protected int[][] table = new int[9][9];
    
    public Sudoku(int[][] sudoku){
        for(int i=0; i<9; i++) {
            for(int j=0; j<9; j++) {
                table[i][j] = sudoku[i][j];
            }
        }
    }
    
    public Sudoku(File file) throws FileNotFoundException, IOException {
        int i=0, j=0;
        BufferedReader br = new BufferedReader(new FileReader(file));
        int chr;
        while((chr=br.read()) != -1){
            if('0' <= chr && chr <= '9') {
                table[i][j++] = chr - '0';
                if(j == 9) {
                    i++;
                    j=0;
                }
                if(i == 9) {
                    break;
                }
            }
        }
        br.close();
    }
    
    /**
     * Indica si se puede introducir 'value' en la posicion 'i', 'j', 
     * comprobando solo la fila.
     * @param i indice de la fila.
     * @param j indice de la columna.
     * @param value valor a comprobar.
     * @return true si se cumple la condicion.
     */
    private boolean validFile(int i, int j, int value) {
        for(int ji=0; ji<9; ji++) {
            if(ji == j) {
                continue;
            }
            if(table[i][ji] == value) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Indica si se puede introducir 'value' en la posicion 'i', 'j', 
     * comprobando solo la columna.
     * @param i indice de la fila.
     * @param j indice de la columna.
     * @param value valor a comprobar.
     * @return true si se cumple la condicion.
     */
    private boolean validColumn(int i, int j, int value) {
        for(int ii=0; ii<9; ii++) {
            if(ii == i) {
                continue;
            }
            if(i != ii && table[ii][j] == value) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Indica si se puede introducir 'value' en la posicion 'i', 'j', 
     * comprobando solo el cuadro.
     * @param i indice de la fila.
     * @param j indice de la columna.
     * @param value valor a comprobar.
     * @return true si se cumple la condicion.
     */
    private boolean validSquare(int i, int j, int value) {
        /* Establecer limites minimo y maximo del cuadrado */
        int i_min, i_max, j_min, j_max;
        if(0<=i && i<=2) {
            i_min = 0;
            i_max = 2;
        } else if (3<=i && i<=5) {
            i_min = 3;
            i_max = 5;
        } else {
            i_min = 6;
            i_max = 8;
        }
        if(0<=j && j<=2) {
            j_min = 0;
            j_max = 2;
        } else if (3<=j && j<=5) {
            j_min = 3;
            j_max = 5;
        } else {
            j_min = 6;
            j_max = 8;
        }

        for(int x=i_min; x<=i_max; x++) {
            for(int y=j_min; y<=j_max; y++) {
                if(x == i && y == j) {
                    continue;
                }
                if(table[x][y] == value) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Indica si se puede introducir 'value' en la posicion 'i', 'j'.
     * @param i indice de la fila.
     * @param j indice de la columna.
     * @param value valor a comprobar.
     * @return true si se cumple la condicion.
     */
    public boolean validValue(int i, int j, int value) {
        return validFile(i, j, value) && 
               validColumn(i, j, value) &&
               validSquare(i, j, value);
    }
    
    /**
     * Devuelve los posibles valores para la posicion i, j.
     * @param i indice de la fila.
     * @param j indice de la columna.
     * @return posibles valores.
     */
    public ArrayList<Integer> valuesIn(int i, int j) {
        ArrayList<Integer> values = new ArrayList();
        for(int val=1; val<=9; val++) {
            if(validValue(i, j, val)) {
                values.add(val);
            }
        }
        return values;
    }
    
    /**
     * Devuelve la posición con el menor numero de valores posibles y sus 
     * valores.
     * <p>
     * @return HashMap con el siguiente contenido:
     * <ul>
     * <li>
     *  (Integer) HashMap.get("i"): Indice de la fila.
     * </li>
     * <li>
     *  (Integer) HashMap.get("j"): Indice de la columna.
     * </li>
     * <li>
     *  (ArrayList&lt;Integer&gt;) HashMap.get("values"): Valores para la posicion 
     *   'i', 'j'.
     * </li>
     * </ul>
     * <p>
     * 
     */
    public HashMap<String, Object> getBest() {
        HashMap<String, Object> map = new HashMap();
        
        map.put("i", -1);
        map.put("j", -1);
        map.put("values", new ArrayList());
        
        for(int i=0; i<9; i++) {
            for(int j=0; j<9; j++) {
                // buscar los valores posibles para la posicion i, j
                if(table[i][j] == CELL_EMPTY) {
                    // solo buscamos valores en las posiciones vacias
                    ArrayList<Integer> newValues = valuesIn(i, j);
                    if(newValues.isEmpty()) {
                        // El sudoku no tiene solucion en la casilla i, j
                        map.put("i", i);
                        map.put("j", j);
                        map.put("values", new ArrayList());
                        return map;
                    }
                    // comparar con los ultimos valores guardados
                    ArrayList<Integer> oldValues = (ArrayList<Integer>) map.get("values");  
                    if(oldValues.isEmpty() || newValues.size() < oldValues.size()) {
                        map.put("i", i);
                        map.put("j", j);
                        map.put("values", newValues);
                    }
                }
            }
        }
        return map;
    }

    /**
     * Indica si todas las celdas del tablero estan rellenas.
     * @return true si se cumple la condicion.
     */
    private boolean isComplete(){
        for(int i=0; i<9; i++) {
            for(int j=0; j<9; j++) {
                if(table[i][j] == CELL_EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Comprueba si las celdas de un tablero son validas.
     * @return true si se cumple la condicion.
     */
    private boolean isValid(){
        for(int i=0; i<9; i++) {
            for(int j=0; j<9; j++) {
                int value = table[i][j];
                if(!validValue(i, j, value)){
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean isSolution(){
        // Evaluacion perezosa, comprobar primero si esta completo, mas rapido
        return isComplete() && isValid();
    }
    
    public void print() {
        int i,j;
    
        for(i=0; i<9; i++){
            if((i==3) || (i==6)){
                System.out.print("\t---------------------\n");
            }
            System.out.print("\t");
            for(j=0; j<9; j++){
                if((j==3) || (j==6)){
                    System.out.print("| ");
                }
                System.out.print(table[i][j]+" ");
            }
            System.out.print("\n");
        }
        System.out.print("\n\n");
    }
    
    public int[][] getTable(){
        return this.table;
    }
    
    /**
     * Clona un sudoku.
     * @return sudoku clonado.
     */
    public Sudoku sudokuClone() {
        return new Sudoku(table);
    }
}
