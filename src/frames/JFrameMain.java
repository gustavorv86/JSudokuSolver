package frames;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import sudoku.Solver;
import sudoku.Sudoku;

public class JFrameMain extends javax.swing.JFrame {

    private static final JFileChooser CHOOSER = new JFileChooser();
    static {
        CHOOSER.setMultiSelectionEnabled(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt", "text");
        CHOOSER.setFileFilter(filter);
    };
    
    private SwingWorker<Solver, Void> worker;
    
    private final JTextField[][] textCells = new JTextField[9][9];
    private ArrayList<Sudoku> sudokuSolutions;

    public JFrameMain() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/icon.png")));
        this.jLabelStatus.setText(" ");
        this.jComboBoxSolutions.removeAllItems();
        this.jComboBoxSolutions.setEnabled(false);
        this.initPanelSudoku();
    }

    private void initPanelSudoku() {
        jPanelSudoku.setLayout(new GridLayout(3, 3));
        for (int pi = 0; pi < 3; pi++) {
            for (int pj = 0; pj < 3; pj++) {

                JPanel squarePanel = new JPanel(new GridLayout(3, 3));
                squarePanel.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

                for (int si = 0; si < 3; ++si) {
                    for (int sj = 0; sj < 3; ++sj) {

                        int i = (pi * 3) + si;
                        int j = (pj * 3) + sj;

                        textCells[i][j] = new JTextField();
                        textCells[i][j].setText(" ");
                        textCells[i][j].setHorizontalAlignment(JTextField.CENTER);
                        textCells[i][j].setFont(new Font("Monospaced", Font.BOLD, 20));

                        squarePanel.add(textCells[i][j]);
                        jPanelSudoku.add(squarePanel);
                    }
                }
            }
        }
        jPanelSudoku.setPreferredSize(jPanelSudoku.getMinimumSize());
    }

    private void loadSudoku() {
        if (CHOOSER.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = CHOOSER.getSelectedFile();
            try {
                Sudoku sudoku = new Sudoku(file);
                showSudoku(sudoku);
            } catch (Exception ex) {
                Messages.showError("  Cannot load file :(  ");
            }
        }
    }

    private void showSudoku(Sudoku sudoku) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int value = sudoku.getTable()[i][j];
                if (1 <= value && value <= 9) {
                    textCells[i][j].setText(value + "");
                } else {
                    textCells[i][j].setText("");
                }
            }
        }
    }
    
    private int[][] getTable() {
        int[][] table = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                table[i][j] = 0;
                try {
                    table[i][j] = Integer.parseInt(textCells[i][j].getText());
                } catch (NumberFormatException ex) {
                    textCells[i][j].setText("");
                }
            }
        }
        return table;
    }

    private void sudokuSolver() {
        
        if(jButtonSolver.getText().equals("Solver")) {
            
            // All code inside SwingWorker runs on a seperate thread
            worker = new SwingWorker<Solver, Void>() {

                @Override
                public Solver doInBackground() {
                    jButtonSolver.setText("Abort");
                    jButtonLoad.setEnabled(false);
                    jComboBoxSolutions.setEnabled(false);
                    jLabelStatus.setText("Please wait...");
                    int[][] table = getTable();
                    Sudoku sudoku = new Sudoku(table);
                    Solver solver = new Solver(sudoku);
                    solver.solver();
                    return solver;
                }

                @Override
                public void done() {
                    jButtonSolver.setText("Solver");
                    jButtonLoad.setEnabled(true);
                    jComboBoxSolutions.setEnabled(true);
                    jLabelStatus.setText(" ");
                    try {
                        // obtener el solver
                        Solver solver = this.get();
                        sudokuSolutions = solver.solutions();
                        if(sudokuSolutions.isEmpty()) {
                            Messages.showInfo("Solution not found");
                        } else {
                            initComboBox();
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, null, ex);
                        Messages.showError("ERROR");
                    } catch (ExecutionException ex) {
                        Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, null, ex);
                        Messages.showError("ERROR");
                    }
                }
            };
            worker.execute();
        }
        
        if(jButtonSolver.getText().equals("Abort")) {
            worker.cancel(true);
            jButtonSolver.setText("Solver");
        }
    }
    
    private void initComboBox() {
        jComboBoxSolutions.removeAllItems();
        if(sudokuSolutions != null) {
            for(int i=1; i<=sudokuSolutions.size(); i++) {
                jComboBoxSolutions.addItem("Solution "+i);
            }
        }
        jComboBoxSolutions.setEnabled(jComboBoxSolutions.getItemCount()!=0);
    }
    
    private void showSudoku(int index) {
        if(sudokuSolutions != null && index < sudokuSolutions.size()) {
            showSudoku(sudokuSolutions.get(index));
        }
    }
    
    private void doExit() {
        dispose();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelSudoku = new javax.swing.JPanel();
        jButtonLoad = new javax.swing.JButton();
        jButtonSolver = new javax.swing.JButton();
        jLabelStatus = new javax.swing.JLabel();
        jPanelSolutions = new javax.swing.JPanel();
        jLabelSolutions = new javax.swing.JLabel();
        jComboBoxSolutions = new javax.swing.JComboBox<>();
        jButtonExit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SudokuSolver");
        setResizable(false);

        jPanelSudoku.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelSudoku.setMaximumSize(new java.awt.Dimension(350, 350));
        jPanelSudoku.setMinimumSize(new java.awt.Dimension(350, 350));

        javax.swing.GroupLayout jPanelSudokuLayout = new javax.swing.GroupLayout(jPanelSudoku);
        jPanelSudoku.setLayout(jPanelSudokuLayout);
        jPanelSudokuLayout.setHorizontalGroup(
            jPanelSudokuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 350, Short.MAX_VALUE)
        );
        jPanelSudokuLayout.setVerticalGroup(
            jPanelSudokuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 350, Short.MAX_VALUE)
        );

        jButtonLoad.setText("Load From File...");
        jButtonLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoadActionPerformed(evt);
            }
        });

        jButtonSolver.setText("Solver");
        jButtonSolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSolverActionPerformed(evt);
            }
        });

        jLabelStatus.setText("jLabelStatus");

        jPanelSolutions.setBorder(javax.swing.BorderFactory.createTitledBorder("Solutions"));

        jLabelSolutions.setText("Number of solutions:");

        jComboBoxSolutions.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Solution 1", "Solution 1000" }));
        jComboBoxSolutions.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxSolutionsItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanelSolutionsLayout = new javax.swing.GroupLayout(jPanelSolutions);
        jPanelSolutions.setLayout(jPanelSolutionsLayout);
        jPanelSolutionsLayout.setHorizontalGroup(
            jPanelSolutionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSolutionsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSolutionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelSolutions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBoxSolutions, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelSolutionsLayout.setVerticalGroup(
            jPanelSolutionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSolutionsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelSolutions)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jComboBoxSolutions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jButtonExit.setText("EXIT");
        jButtonExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelSudoku, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonLoad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonSolver, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelSolutions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonLoad)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonSolver)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelStatus)
                        .addGap(18, 18, 18)
                        .addComponent(jPanelSolutions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonExit))
                    .addComponent(jPanelSudoku, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLoadActionPerformed
        loadSudoku();
    }//GEN-LAST:event_jButtonLoadActionPerformed

    private void jButtonSolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSolverActionPerformed
        sudokuSolver();
    }//GEN-LAST:event_jButtonSolverActionPerformed

    private void jComboBoxSolutionsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxSolutionsItemStateChanged
        if(evt.getStateChange() == ItemEvent.SELECTED) {
            showSudoku(this.jComboBoxSolutions.getSelectedIndex());
        }
    }//GEN-LAST:event_jComboBoxSolutionsItemStateChanged

    private void jButtonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExitActionPerformed
        doExit();
    }//GEN-LAST:event_jButtonExitActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonExit;
    private javax.swing.JButton jButtonLoad;
    private javax.swing.JButton jButtonSolver;
    private javax.swing.JComboBox<String> jComboBoxSolutions;
    private javax.swing.JLabel jLabelSolutions;
    private javax.swing.JLabel jLabelStatus;
    private javax.swing.JPanel jPanelSolutions;
    private javax.swing.JPanel jPanelSudoku;
    // End of variables declaration//GEN-END:variables
}
