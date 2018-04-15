package sudokusolver.matrixFiller;

public final class SudokuMatrixSolver
{
    
    protected class PairLocation
    {
        public int posRow;
        public int posCol;   
    }
    
    /// Ctor
    public SudokuMatrixSolver(final MatrixFiller matrixPuzzle)
    {
        matrixValues = new MatrixFiller(matrixPuzzle);
        numberOfAdvancesToNextCell = 0;
        numberOfBackTracksToPreviousCell = 0;
    }
    
    // - Accessors
    
    /// Check if the puzzle has been fully solved (returns the next available location)
    public boolean isFullySolved(PairLocation nextAvailableLocation)
    {
        // Initialize with the last position in case all the elements are marked
        nextAvailableLocation.posRow = matrixValues.matrixSize - 1;   
        nextAvailableLocation.posCol = matrixValues.matrixSize - 1;
        
        for (int i = 0; i < matrixValues.matrixSize; ++i)
        {
            for (int j = 0; j < matrixValues.matrixSize; ++j)
            {
                if (matrixValues.getElement(i, j) == unmarked)
                {
                    nextAvailableLocation.posRow = i;
                    nextAvailableLocation.posCol = j;
                    return false;
                }
            }
        }
        return true;
    }
    
    /// Check if a specified value is present on the specified row
    private boolean isValueFoundOnRow(int value, int row)
    {
        for (int i = 0; i < matrixValues.matrixSize; ++i)
        {
            if (matrixValues.getElement(row, i) == value)
                return true;
        }
        return false;
    }
    
    /// Check if a specified value is present on the specified column
    private boolean isValueFoundOnColumn(int value, int col)
    {
        for (int i = 0; i < matrixValues.matrixSize; ++i)
        {
            if (matrixValues.getElement(i, col) == value)
                return true;
        }
        return false;
    }
    
    /// Check if a specified value is present of the square the includes (row, col)
    private boolean isValueFoundInSquare(int value, int row, int col)
    {
        // determine the square starting point
        int rowUpperLeftStart = row - (row % 3);   // since we have 3 elements in each square
        int colUpperLeftStart = col - (col % 3);   // since we have 3 elements in each square   
        
        // check if this element is in this square
        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 3; ++j)
            {
                if (matrixValues.getElement(rowUpperLeftStart + i, colUpperLeftStart + j) == value)
                    return true;
            }
        }
        return false;
    }
    
    /// Perform all checks, if this value can be plcaed at the position (row, col)
    private boolean isPositionOkForValue(int value, int row, int col)
    {
        // callback the other methods to check if this is a valid position for the current element
        boolean rowOk    = isValueFoundOnRow(value, row) == false;
        boolean columnOk = isValueFoundOnColumn(value, col) == false;
        boolean squareOk = isValueFoundInSquare(value, row, col) == false;
        
        return columnOk && rowOk && squareOk;
    }
    
    
    /// Get the result in the form of a matrix
    public final MatrixFiller getResult()
    {
        return matrixValues;
    }

    /// Get statistics
    public String getStatistics()
    {
        return "> Performed: " + numberOfAdvancesToNextCell + " advances to the next cell\n   and " + numberOfBackTracksToPreviousCell + " backtracks to a previous cell.\n";
    }
    
    /// Solve the actual puzzle
    public boolean SolvePuzzle()
    {       
        PairLocation nextAvailableLocation = new PairLocation();
        boolean fullySolved = isFullySolved(nextAvailableLocation);
        if (fullySolved)           // puzzle already solved so no calculations needed
            return true;
        
        // if i've reached this line I'm on the first available location
        for (int i = 1; i < 10; ++i)         // check each digit
        {
            if (isPositionOkForValue(i, nextAvailableLocation.posRow, nextAvailableLocation.posCol))
            {
                ++numberOfAdvancesToNextCell;
                matrixValues.setElement(nextAvailableLocation.posRow, nextAvailableLocation.posCol, i);  // set this value temporarily until I check the entire branch
               
                if (SolvePuzzle())                                                                       // go on further on the branch
                    return true;
                
                matrixValues.setElement(nextAvailableLocation.posRow, nextAvailableLocation.posCol, unmarked);
                ++numberOfBackTracksToPreviousCell;
            }
        }
             
        return false;
    }
    
    // - Members
    private MatrixFiller matrixValues;
    private static final int unmarked = 0;
    private int numberOfAdvancesToNextCell;
    private int numberOfBackTracksToPreviousCell;
}
