package sudokusolver.matrixFiller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MatrixFiller
{
    
    /// Ctor
    public MatrixFiller(int squareMatrixSize)
    {
        matrixSize = squareMatrixSize; 
        matrixLinearSize = matrixSize * matrixSize;
        matrixIntegers = new int[matrixLinearSize];
    }
    
    /// Copy ctor
    public MatrixFiller(MatrixFiller rightObject)
    {
        this(rightObject.matrixSize);
        matrixIntegers = rightObject.matrixIntegers.clone();
    }
    
    // - Mutators
    
    /// Read from file
    public void parseFile(String pathFileName) throws IOException
    {
        BufferedReader inputSeq = new BufferedReader(new FileReader(pathFileName));
        String[] lineSplitsArray;
        String bufferFileLine;
        
        int i = 0, k = 0;
        
        while ((bufferFileLine = inputSeq.readLine()) != null)
        {
           lineSplitsArray = bufferFileLine.split(" ");
           for (k = 0; k < lineSplitsArray.length; ++k)
                matrixIntegers[i++] = Integer.parseInt(lineSplitsArray[k]);
        }
        inputSeq.close();
    }
    
    /// Set the value from (i,j)
    public void setElement(int lineI, int colJ, int valueCell)
    {
        matrixIntegers[lineI * matrixSize + colJ] = valueCell;
    }

    public void setElement(int offsetArray, int value)
    {
        matrixIntegers[offsetArray] = value;
    }
    
    // - Accessors
    
    /// Write the content to the specified file
    public void serializeToFile(String pathFileName) throws IOException
    {
        PrintWriter outputSeq = new PrintWriter(new BufferedWriter(new FileWriter(pathFileName)));
        
        for (int i = 0; i < matrixSize; ++i)
        {
            for (int j = 0; j < matrixSize; ++j)
            {
                if (j == matrixSize -1)
                    outputSeq.print(getElement(i, j) + "\n");
                else
                    outputSeq.print(getElement(i, j) + " ");
            }
        }
        outputSeq.close();
    }
    
    /// Get the element stored at (i, j)
    public int getElement(int lineI, int colJ)
    {
        return matrixIntegers[lineI * matrixSize + colJ];
    }
    
    /// Get the element stored at the specified linear offset
    public int getElement(int offsetArray)
    {
        return matrixIntegers[offsetArray];
    } 
    
    /// Get the size (square not linear)
    public int getSize()
    {
        return matrixSize;
    }
    
    // - Members
    protected int[] matrixIntegers; 
    protected int matrixSize;
    protected int matrixLinearSize;
}
