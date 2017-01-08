package main;

import frames.JFrameMain;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import sudoku.Solver;
import sudoku.Sudoku;

public class Main {

    public static void main(String[] args) throws IOException {

//        SOLVER ALGORYTHM
//
//        Sudoku sudoku = new Sudoku(new File("./sudoku.txt"));
//        Solver solver = new Solver(sudoku);
//        solver.solver();
//        ArrayList<Sudoku> list = solver.solutions();

        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                System.out.println(info);
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        JFrameMain frame = new JFrameMain();
        frame.setVisible(true);

        System.out.println("DONE");
    }

}
