import java.awt.event.*;

/**
* Cette classe <code>SKeyCodeEvent</code> permets de détecter les touches de l'utilisateur et d'effectuer des actions sur Le Sudoku.
*
* @version 0.1
* @author Mickael Albarello	
*/
public class SKeyCodeEvent implements KeyListener
{
    private SudokuLogique sudokuLogique;
    private CSudoku sudokuFile;

    public SKeyCodeEvent(SudokuLogique sl,CSudoku sf)
    {
        this.sudokuLogique = sl;
        this.sudokuFile = sf;
    }

    public void keyPressed(KeyEvent evenement)
    {
        byte keyCode = (byte)(evenement.getKeyChar());
        //System.out.println(Integer.toString(keyCode)); //pour trouver les KeyCodes
        if(keyCode >= 48 && keyCode <= 57)// les nombres
        {
            //System.out.println(Integer.toString(keyCode-48));
            this.sudokuLogique.SetKeyEventNumber((byte)(keyCode-48));
        }
        else if(keyCode == 114)// la touche r pour resoudre
        {
            this.sudokuLogique.ApplyAlgoBacktraking(true);
        }
        else if(keyCode == 110)// la touche n pour Nouveaux
        {
            this.sudokuLogique.InitNumberRandom();
        }
        else if(keyCode == 104)// la touche h pour help/aide
        {
            this.sudokuLogique.AideMessage();
        }
        else if(keyCode == 105)// i import 
        {
            this.sudokuLogique.UnBlockAll();
            this.sudokuLogique.SetGrille(this.sudokuFile.ChargeFile());
            this.sudokuLogique.BlockAllNumber();
        }
        else if(keyCode == 101)// e export
        {
            this.sudokuFile.SaveSudoku(this.sudokuLogique.GetGrilleInt());
        }
        else if(keyCode == 27) // echap ferme le sudoku
        {
            System.exit(0);
        }
        else if(keyCode == 109) // m edition
        {
            this.sudokuLogique.ChangeMode();
        }
        else if(keyCode == 100) // d doute
        {
            this.sudokuLogique.NoteMode();
        }
    }

    public void keyTyped(KeyEvent evenement){}
    public void keyReleased(KeyEvent evenement){}
}