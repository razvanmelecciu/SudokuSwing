package sudokusolver;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.filechooser.FileNameExtensionFilter;
import sudokusolver.matrixFiller.MatrixFiller;
import sudokusolver.matrixFiller.SudokuMatrixSolver;

class SudokuAppGUI extends JFrame
{

    /// Ctor
    SudokuAppGUI()
    {
        matrixPuzzle = new MatrixFiller(puzzleSize);
        initializeUI();
    }

    // - Mutators
    
    /// Create the grid
    private void createPuzzleTable()
    {
        String[] columnNames = { "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9"};

        Object[][] matrixStrings = new String[puzzleSize][puzzleSize];

        Font tableFont = new Font("Verdana", Font.PLAIN, 12);

        tablePuzzle = new JTable(matrixStrings, columnNames);
        tablePuzzle.setPreferredScrollableViewportSize(new Dimension(500, 145));
        tablePuzzle.setBounds(0, 0, 500, 200);
        tablePuzzle.setFont(tableFont);

        for (int i = 0; i < puzzleSize; ++i)
        {
            for (int j = 0; j < puzzleSize; ++j)
            {
                tablePuzzle.setValueAt(tableEmptyCell, i, j);
            }
        }

        //Create the scroll pane and add the tablePuzzle to it., Add the scroll pane to this panel.
        mainPanel.add(new JScrollPane(tablePuzzle));
    }

    /// Create the log window
    private void createLogWindow()
    {
        logWindow = new JTextArea(13, 44);
        logWindow.setText("Please hit open file to read a sudoku puzzle from a file");
        logWindow.setEditable(false);
        logWindow.setVisible(true);

        //Add the scroll pane to this panel.
        mainPanel.add(new JScrollPane(logWindow));
    }

    /// Create the buttons
    private void createButtons()
    {
        Dimension dimButton = new Dimension(90, 30);

        JButton openFileBtn    = new JButton("Open File");
        openFileBtn.setToolTipText("Open a text file that contains a puzzle");
        openFileBtn.setPreferredSize(dimButton);
        openFileBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                actionOpenFile();
            }
        });

        JButton solvePuzzleBtn = new JButton("Solve");
        solvePuzzleBtn.setToolTipText("Solve the puzzle displayed in the table");
        solvePuzzleBtn.setPreferredSize(dimButton);
        solvePuzzleBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                actionSolve();
            }
        });

        JButton saveLogBtn     = new JButton("Save");
        saveLogBtn.setToolTipText("Saves the current puzzle");
        saveLogBtn.setPreferredSize(dimButton);
        saveLogBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                actionSavePuzzle();
            }
        });

        JButton aboutBtn       = new JButton("About");
        aboutBtn.setToolTipText("About this tiny application");
        aboutBtn.setPreferredSize(dimButton);
        aboutBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                actionAbout();
            }
        });

        JButton closeBtn       = new JButton("Close app");
        closeBtn.setToolTipText("Close");
        closeBtn.setPreferredSize(dimButton);
        closeBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                actionClose();
            }
        });

        mainPanel.add(openFileBtn);
        mainPanel.add(solvePuzzleBtn);
        mainPanel.add(saveLogBtn);
        mainPanel.add(aboutBtn);
        mainPanel.add(closeBtn);
    }

    /// Create all the controls
    private void createElements()
    {
       createPuzzleTable();
       createLogWindow();
       createButtons();
    }

    /// Initialize the user interface
    private void initializeUI()
    {
        mainPanel = new JPanel();          // create my panel with flow layout

        setTitle("Sudoku Solver");
        ImageIcon appIcon = new ImageIcon("app2.png");
        setIconImage(appIcon.getImage());

        setSize(520, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        mainPanel.setSize(520, 600);
        add(mainPanel);

        createElements();
    }
    
    /// Update the table grid from the internal matrix
    private void updateTableFromMatrix(MatrixFiller matrixValues)
    {
        int currentMatrixElement = matrixEmptyCell;
        String stringMatrixElement;

        for (int i = 0; i < matrixValues.getSize(); ++i)
        {
            for (int j = 0; j < matrixValues.getSize(); ++j)
            {
                currentMatrixElement = matrixValues.getElement(i, j);
                stringMatrixElement = currentMatrixElement != matrixEmptyCell ? Integer.toString(currentMatrixElement) : tableEmptyCell;
                tablePuzzle.setValueAt(stringMatrixElement, i, j);
            }
        }
    }
    
    /// Update the matrix from the actual table grid
    private void updateMatrixFromTable()
    {
        int currentMatrixElement = matrixEmptyCell;
        String stringMatrixElement = "";

        for (int i = 0; i < matrixPuzzle.getSize(); ++i)
        {
            for (int j = 0; j < matrixPuzzle.getSize(); ++j)
            {
                stringMatrixElement = (String) tablePuzzle.getValueAt(i, j);
                if (stringMatrixElement.equals(tableEmptyCell))
                {
                    currentMatrixElement = matrixEmptyCell;
                }
                else
                {
                    currentMatrixElement = Integer.parseInt(stringMatrixElement);
                }

                matrixPuzzle.setElement(i, j, currentMatrixElement);
            }
        }
    }

    /// The open file action
    private void actionOpenFile()
    {
        JFileChooser fileOpenChoose = new JFileChooser();
        FileNameExtensionFilter filterExtension = new FileNameExtensionFilter("Sudoku puzzles", puzzleFileExtension);
        fileOpenChoose.setFileFilter(filterExtension);
        fileOpenChoose.setAcceptAllFileFilterUsed(false);

        int retCode = fileOpenChoose.showOpenDialog(this);

        if (retCode == JFileChooser.APPROVE_OPTION)
        {
            openFileName = fileOpenChoose.getSelectedFile().getName();
            openFileNamePath = fileOpenChoose.getCurrentDirectory().toString();

            try
            {
                matrixPuzzle.parseFile(openFileNamePath + "\\" + openFileName);
                updateTableFromMatrix(matrixPuzzle);

                logWindow.setText("> Opened " + openFileNamePath + "\\" + openFileName + "\n");

            }
            catch (Exception e)
            {
                System.out.println("The file is invalid");
            }
        }
    }
    
    /// Handler for the solve trigger
    private void actionSolve()
    {
        updateMatrixFromTable();
        
        logWindow.append("> Attempting to solve the current puzzle.\n");
        
        long startTime = System.currentTimeMillis();
        
        SudokuMatrixSolver solverPuzzle = new SudokuMatrixSolver(matrixPuzzle); 
        boolean solvedOk = solverPuzzle.SolvePuzzle();
        
        long estimatedTime = System.currentTimeMillis() - startTime;
        
        if (solvedOk)
        {
            logWindow.append("> The puzzle has been solved.\n");
            logWindow.append("> It took me " + estimatedTime/1000 + " seconds to backtrack the puzzle.\n");
            logWindow.append(solverPuzzle.getStatistics());
            
            int nRet = JOptionPane.YES_NO_OPTION;
            nRet = JOptionPane.showConfirmDialog(null, "Do you want to update the table view with the solution? ", "Update table", JOptionPane.INFORMATION_MESSAGE);
            
            if (nRet == JOptionPane.YES_OPTION)
            {
                updateTableFromMatrix(solverPuzzle.getResult());
                logWindow.append("> Updated the table view with the solution.\n");
            }
        }
        else
           logWindow.append("> The puzzle could not be solved.\n");
    }

    /// Handler for the save event
    private void actionSavePuzzle()
    {
        updateMatrixFromTable();
        
        JFileChooser fileOpenChoose = new JFileChooser();
        FileNameExtensionFilter filterExtension = new FileNameExtensionFilter("Sudoku puzzles", puzzleFileExtension);
        fileOpenChoose.setFileFilter(filterExtension);
        fileOpenChoose.setAcceptAllFileFilterUsed(false);
        
        int retCode = fileOpenChoose.showSaveDialog(this);

        if (retCode == JFileChooser.APPROVE_OPTION)
        {
            saveFileName = fileOpenChoose.getSelectedFile().getName();
            saveFileNamePath = fileOpenChoose.getCurrentDirectory().toString();

            try
            {
                if (saveFileName.endsWith(puzzleFileExtension))                 
                    matrixPuzzle.serializeToFile(saveFileNamePath + saveFileName);
                else
                    matrixPuzzle.serializeToFile(saveFileNamePath + saveFileName + "." + puzzleFileExtension);
            } 
            catch (Exception e) 
            {
                System.out.println("The file is invalid");
            }
            
            logWindow.setText("> Saved the puzzle to " + saveFileNamePath + saveFileName);
        }
    }

    /// Handler for the about event
    private void actionAbout()
    {
        JOptionPane.showMessageDialog(null, "This tiny application can solve any sudoku puzzle within seconds using Backtracking\n "
                                            + "Author: Razvan Melecciu", "About Sudoku Solver", JOptionPane.INFORMATION_MESSAGE);
    }

    /// Handler for the close event
    private void actionClose()
    {
        System.exit(0);
    }
    
    // - Members
    
    private JPanel mainPanel;
    private JTable tablePuzzle;
    private JTextArea logWindow;

    private static final String puzzleFileExtension = "sdk";
    private static final int puzzleSize = 9;
    private static final int matrixEmptyCell = 0;
    private static final String tableEmptyCell = "#";

    String openFileName;
    String openFileNamePath;
    String saveFileName;
    String saveFileNamePath;

    MatrixFiller matrixPuzzle;
}


public class SudokuSolverApp
{
    /// Main application launcher
    public static void main(String[] args)
    {
        String appLook = "Nimbus";
        
        UIManager.put("control", new Color(0xffffffff));

        try
        {
           for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
            {
                if (appLook.equals(info.getName()))
                {
                    UIManager.setLookAndFeel(info.getClassName());
                    UIManager.getLookAndFeelDefaults().put("ScrollBar.minimumThumbSize", new Dimension(30, 30));
                    break;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("Nimbus is not available !");
        }

        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        EventQueue.invokeLater(()->
        {
            SudokuAppGUI appInstance = new SudokuAppGUI();
            appInstance.setVisible(true);
        });
    }
}
