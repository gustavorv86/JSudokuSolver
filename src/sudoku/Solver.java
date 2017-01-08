
package sudoku;

import java.util.ArrayList;
import java.util.HashMap;

public class Solver {
    
    private final ArrayList<Sudoku> sudokuList;
    private final ArrayList<Sudoku> sudokuSolutions;
    
    public Solver(Sudoku sudoku) {
        sudokuSolutions = new ArrayList();
        sudokuList = new ArrayList();
        sudokuList.add(sudoku);
    }
    
    public void solver() {
        
        while(!sudokuList.isEmpty()) {
            // coger el primer sudoku
            Sudoku first = sudokuList.get(0);
            // borrarlo de la lista
            sudokuList.remove(0);
            // comprobar si es solucion
            if(first.isSolution()) {
                sudokuSolutions.add(first);
            } else {
                // obtener la mejor posicion y sus valores
                HashMap<String, Object> map = first.getBest();
                int i = (int) map.get("i");
                int j = (int) map.get("j");
                ArrayList<Integer> values = (ArrayList<Integer>) map.get("values");
                // crear tantos sudokus nuevos como valores posibles
                // NOTA: Si values.isEmpty() el sudoku no tiene solucion
                for(int value : values) {
                    // clonar el sudoku
                    Sudoku clone = first.sudokuClone();
                    // rellenar la posivion i, j
                    clone.table[i][j] = value;
                    // insertar de nuevo en la lista
                    sudokuList.add(clone);
                }
                // comprobar si en 'sudokuList' hay soluciones
                for(int k=(sudokuList.size()-1); k>=0; k--) {
                    Sudoku sudoku = sudokuList.get(k);
                    if(sudoku.isSolution()) {
                        sudokuSolutions.add(sudoku);
                        sudokuList.remove(sudoku);
                    }
                }
            }
        }
    }
    
    public ArrayList<Sudoku> solutions() {
        return sudokuSolutions;
    }
    
}
