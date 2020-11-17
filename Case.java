import javax.swing.JComponent;
import java.awt.*;

/**
* Cette classe <code>Case</code> est utilisée pour afficher/desine les cases du sudoku elle contient plusieur etat ansi que la valeur et note de celle si.
*
* @version 0.1
* @author Mickael Albarello
*/
public class Case extends JComponent
{
    private byte state = 0;//0 = non selection, 1 = surligner, 2 selectioner.
    private byte nombre;
    private byte[] note = {0,0,0,0};
    private byte notePosition = 0;
    private byte affichage = 0;
    private byte mode = 1;//0 = mode edition, 1 = mode Jeux, 2 = mode Note.
    private boolean block = false;
    private boolean wrong = false;
  
	/**
	* @param n La valeur de la case.
	*/
	public Case(byte n) 
	{
        super();
        this.nombre = n;
    }
  	//@Override
  	protected void paintComponent(Graphics pinceau) 
  	{
    	Graphics secondPinceau = pinceau.create();
    	if (this.isOpaque()) 
    	{
      		secondPinceau.setColor(new Color(0,0,0));
      		secondPinceau.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
        Font texte = new Font("",Font.BOLD,this.getHeight()/8+this.getWidth()/8);
        Font texteNote = new Font("",Font.BOLD,this.getHeight()/10+this.getWidth()/10);
        secondPinceau.setColor(this.state == (byte)2 ? new Color(200,200,200)/*si selectionner */ : this.state == (byte)1  ? this.mode == 0 ? new Color(200,100,255) : this.mode == 2 ? new Color(200,150,150) : new Color(150,150,255) : new Color(255,255,255));//0 = non selection, 1 = surligner, 2 selectioner.
        secondPinceau.fillRect(0, 0, this.getWidth(), this.getHeight());
        secondPinceau.setColor(block ? new Color(100,100,100) : wrong ? new Color(200,0,0) : new Color(0,150,150));
        secondPinceau.setFont(texte);
        secondPinceau.drawString(this.nombre < (byte)1 || this.nombre > (byte)9 ? "" : Integer.toString(this.nombre),(int)(this.getWidth()/2.3),(int)(this.getHeight()/1.7));
        secondPinceau.setColor(new Color(150,200,150));
        secondPinceau.setFont(texteNote);
        secondPinceau.drawString(this.note[0] < (byte)1 || this.note[0] > (byte)9 ? "" : Integer.toString(this.note[0]),(int)(this.getWidth()/5.6),(int)(this.getHeight()/3.4));
        secondPinceau.drawString(this.note[1] < (byte)1 || this.note[1] > (byte)9 ? "" : Integer.toString(this.note[1]),(int)(this.getWidth()/5.6),(int)(this.getHeight()/1.2));
        secondPinceau.drawString(this.note[2] < (byte)1 || this.note[2] > (byte)9 ? "" : Integer.toString(this.note[2]),(int)(this.getWidth()/1.4),(int)(this.getHeight()/3.4));
        secondPinceau.drawString(this.note[3] < (byte)1 || this.note[3] > (byte)9 ? "" : Integer.toString(this.note[3]),(int)(this.getWidth()/1.4),(int)(this.getHeight()/1.2));
        secondPinceau.setColor(new Color(200,200,200));
        secondPinceau.drawRect(0, 0, this.getWidth(), this.getHeight());
        this.InitAffichage(secondPinceau);
    }
	
	/*
	* Permets de modifier la valeur de l'état de la case entre surligner, normal, sélectionner.
	* @param s 0 = non selection, 1 = surligner, 2 selectioner.
	*/
    public void SetState(byte s)
    {
        this.state = s;
        this.notePosition = 0;
        this.repaint();
    }

	/*
	* Permets de récupérer la valeur de l'état de la case entre surligner, normal, sélectionner.
	* @return 0 = non selection, 1 = surligner, 2 selectioner.
	*/
    public byte GetState()
    {
        return this.state;
    }

	/**
	* Permets de Modifier la valeur de la case que si elle n'est pas bloquer.
	* @param n nombre entre 0 et 9.
	*/
    public void SetNumber(byte n)
    {
        if(!block)
        {
            this.nombre = n;
            this.repaint();
        }
    }

	/**
	* Permets de récupérer la valeur de la case.
	* @return la valeur de la case.
	*/
    public byte GetNumber()
    {
        return this.nombre;
    }

	/**
	* Permets de metre a jour l'etat bloquer de la case.
	* @param b true = bloquer, false = debloquer.
	*/
    public void SetBlock(boolean b)
    {
        this.block = b;
        this.repaint();
    }

	/**
	* Permets de metre a jour l'etat bloquer de la case.
	* @param w true = la case possede une valeur fausse, false = la case possede une valeur qui n'est pas en comflit avec d'autre nombre du sudoku.
	*/
    public void SetWrongNumber(boolean w)
    {
        if(!block)
        {
            this.wrong = w;
            this.repaint();
        }
    }

	/**
	* Permets d'ajouter une plusieurs note à la case.
	* @param n la valeur en byte de la note entre 0 et 9.
	*/
    public void AddNote(byte n)
    {
        this.note[this.notePosition%4] = n;
        this.notePosition++;
        this.repaint();
    }

	/**
	* Supprime toutes les notes enregistrer sur la case.
	*/
    public void ResetNote()
    {
        for(byte i = 0; i < this.note.length;i++)
        {
            this.note[i] = 0;
        }
        this.repaint();
    }

	/**
	* @return Retourne la valeur fausse de la case.
	*/
    public boolean GetWrongNumber()
    {
        return this.wrong;
    }

	/**
	* @param a L'etat d'affichage entre 0 et 8 de la case qui permet d'afficher le cadre du sudoku.
	*/
    public void SetAffichage(byte a)
    {
        this.affichage = a;
        this.repaint();
    }

	/**
	* @param nb Change le mode entre 0 = mode edition, 1 = mode Jeux, 2 = mode Note.
	*/
    public void ChangeMode(byte nb)
    {
        this.mode = nb;
        this.repaint();
    }

	/**
	* L'etat d'affichage entre 0 et 8 de la case qui permet d'afficher le cadre du sudoku.
	* @param secondPinceau Prend en parametre le pinceau pour desine le cadre du sudoku par case.
	*/
    private void InitAffichage(Graphics secondPinceau)
    {
        secondPinceau.setColor(new Color(50,50,50));
        switch(this.affichage)
        {
            case 0://rien
                break;
            case 1://Gauche
                secondPinceau.fillRect(0, 0, this.getWidth()/14, this.getHeight());
                break;
            case 2://Haut
                secondPinceau.fillRect(0, 0, this.getWidth(), this.getHeight()/14);
                break;
            case 3:// Droite
                secondPinceau.fillRect(this.getWidth()-this.getWidth()/14, 0, this.getWidth()/14, this.getHeight());
                break;
            case 4://bas
                secondPinceau.fillRect(0,this.getHeight()-this.getHeight()/14, this.getWidth(), this.getHeight()/14);
                break;
            case 5://haut + Gauche
                secondPinceau.fillRect(0, 0, this.getWidth(), this.getHeight()/14);
                secondPinceau.fillRect(0, 0, this.getWidth()/14, this.getHeight());
                break;
            case 6://Haut + droite
                secondPinceau.fillRect(0, 0, this.getWidth(), this.getHeight()/14);
                secondPinceau.fillRect(this.getWidth()-this.getWidth()/14, 0, this.getWidth()/14, this.getHeight());
                break;
            case 7://bas + Gauche
                secondPinceau.fillRect(0, 0, this.getWidth()/14, this.getHeight());
                secondPinceau.fillRect(0,this.getHeight()-this.getHeight()/14, this.getWidth(), this.getHeight()/14);
                break;
            case 8://bas + droite
                secondPinceau.fillRect(this.getWidth()-this.getWidth()/14, 0, this.getWidth()/14, this.getHeight());
                secondPinceau.fillRect(0,this.getHeight()-this.getHeight()/14, this.getWidth(), this.getHeight()/14);
                break;
            default:
        }
    }
}