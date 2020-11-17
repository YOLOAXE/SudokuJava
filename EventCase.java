import java.awt.event.*;

/**
* Cette classe <code>EventCase</code> est utilisée pour detecter les evenement sur une case.
*
* @version 0.1
* @author Mickael Albarello
*/
public class EventCase implements MouseListener
{
    private SudokuLogique sudokuL;
    private byte id;

	/**
	* @param sl prend en argument la logique du jeux pour pouvoir utiliser c'est fonction.
	* @param i prend un argument id qui corespond a la case qui est lier a cette eventCase.
	*/
	public EventCase(SudokuLogique sl,byte i)
	{
        this.sudokuL = sl;
        this.id = i;
	}

	/**
	* Si on clique sur la case alors elle en informe la logique du jeux. 
	*/
    public void mouseClicked(MouseEvent evenement)
    {
        sudokuL.Clicked(this.id);
    }

	/**
	* Si on rentre sur la case alors elle en informe la logique du jeux. 
	*/
    public void mouseEntered(MouseEvent evenement)
    {
        sudokuL.Entered(this.id);
    }

	/**
	* Si on sort sur la case alors elle en informe la logique du jeux. 
	*/
    public void mouseExited(MouseEvent evenement)
    {
        sudokuL.Exited(this.id);
    }

    public void mousePressed(MouseEvent evenement){}
    public void mouseReleased(MouseEvent evenement){}
}