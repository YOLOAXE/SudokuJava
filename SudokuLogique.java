import javax.swing.*;
import java.util.Random;

/**
* Cette classe <code>SudokuLogique</code> est utilisée pour le control du sudoku il va faire le lien entre les
* evenements et les case du sudoku
*
* @version 0.1
* @author Mickael Albarello
*/
public class SudokuLogique
{
    private byte currentSelectedID;
    private boolean modeEdition = false;
    private boolean modeNote = false;
    private Case[] caseGrille = new Case[81];
    private Random rd = new Random();
    private Backtracking bt = new Backtracking();
    private JFrame fenetre;
    
    private final byte[] sudokuIDAffichage = 
       {0,0,3,1,0,3,1,0,0,
        0,0,3,1,0,3,1,0,0,
        4,4,8,7,4,8,7,4,4,
        2,2,6,5,2,6,5,2,2,
        0,0,3,1,0,3,1,0,0,
        4,4,8,7,4,8,7,4,4,
        2,2,6,5,2,6,5,2,2,
        0,0,3,1,0,3,1,0,0,
        0,0,3,1,0,3,1,0,0};
    private final byte[][] sudoCareChecker = 
        {{0,1,2,9,10,11,18,19,20}
        ,{3,4,5,12,13,14,21,22,23}
        ,{6,7,8,15,16,17,24,25,26}
        ,{27,28,29,36,37,38,45,46,47}
        ,{30,31,32,39,40,41,48,49,50}
        ,{33,34,35,42,43,44,51,52,53}
        ,{54,55,56,63,64,65,72,73,74}
        ,{57,58,59,66,67,68,75,76,77}
        ,{60,61,62,69,70,71,78,79,80}};
    private final byte[] sudoCareCheckerPointeur = 
        {0,0,0,1,1,1,2,2,2,
         0,0,0,1,1,1,2,2,2,
         0,0,0,1,1,1,2,2,2,
         3,3,3,4,4,4,5,5,5,
         3,3,3,4,4,4,5,5,5,
         3,3,3,4,4,4,5,5,5,
         6,6,6,7,7,7,8,8,8,
         6,6,6,7,7,7,8,8,8,
         6,6,6,7,7,7,8,8,8};

    /**
	* @param cg Prends en argument les 81 cases du sudoku pour pouvoir modifier leur valeur.
	* @param f Prends en argument la JFrame pour pouvoir interagire avec.
	*/
    public SudokuLogique(Case[] cg,JFrame f)
    {
        this.caseGrille = cg;
        this.fenetre = f;
    }

	/**
	* Prends en argument la valeur de la case avec  laquelle on interagi pour pouvoir ensuite modifier son état d'interaction 	
	* entre sélection et non sélectionner.
	* @param csid 0 = non selection, 1 = surligner, 2 selectioner.
	*/
    public void Clicked(byte csid)
    {
        this.currentSelectedID = this.caseGrille[csid].GetState() == (byte)1 || this.caseGrille[csid].GetState() == (byte)0? csid : (byte)-1;
        for(byte i = 0; i < 81;i++)
        {
            this.caseGrille[i].SetState(this.currentSelectedID == i ? (byte)2 : (byte)0);
        }
    }

	/**
	* Prends en argument la valeur de la case avec  laquelle on interagi pour pouvoir ensuite modifier son état d'interaction surligne
	* si il n'est pas deja selectioner.
	* @param csid 0 = non selection, 1 = surligner, 2 selectioner.
	*/
    public void Entered(byte csid)
    { 
        this.caseGrille[csid].SetState(this.caseGrille[csid].GetState() == (byte)0 ? (byte)1 : (byte)2);
    }

	/**
	* Prends en argument la valeur de la case avec  laquelle on interagi pour pouvoir ensuite modifier son état d'interaction non surligne
	* si il n'est pas deja selectioner.
	* @param csid 0 = non selection, 1 = surligner, 2 selectioner.
	*/
    public void Exited(byte csid)
    {
        this.caseGrille[csid].SetState(this.caseGrille[csid].GetState() == (byte)1 ? (byte)0 : this.caseGrille[csid].GetState() == (byte)2 ? (byte)2 : (byte)0);
    }

	/**
	* Initialise l'affichage du sudoku via un tableaux de 0 a 8.
	*/
    public void InitAffichage()
    {
        for(byte i = 0; i < 81;i++)
        {
            this.caseGrille[i].SetAffichage(this.sudokuIDAffichage[i]);
        }
    }

	/**
	* Initialise une nouvelle grille qui est générée aléatoirement et qui est realisable par un humain.
	*/
    public void InitNumberRandom()
    {
        for(byte i = 0; i < 81;i++)
        {
            this.caseGrille[i].ResetNote();
            this.caseGrille[i].SetBlock(false);
            this.caseGrille[i].SetNumber((byte)(Clamp(this.rd.nextInt(80)-70,0,9)));
        }
        //this.CheckLigne((byte)1,true);
        //this.CheckColonne((byte)1,true);
        //this.CheckCare((byte)1,true);
        this.CheckSudoku(false);
        this.ApplyAlgoBacktraking(false);
        for(byte i = 0; i < 81;i++)
        {
            if(this.rd.nextInt(2) == 1)
            {
                this.caseGrille[i].SetNumber((byte)(0));
            }
            else if(!this.modeEdition)
            {
                this.caseGrille[i].SetBlock(true);
            }
        }
    }

	/**
	* Permets de Vérifier la validité de l'Intégralité du sudoku.
	* @param surligne true = ModeSuriligner,false = ModeSuppresion.
	*/
    private void CheckSudoku(boolean surligne)
    {
        for(byte i = 0; i < 81;i++)
        {
            this.CheckLigne(i,surligne);
            this.CheckColonne(i,surligne);
            this.CheckCare(i,surligne);
        }
    }

	/**
	* Permets de Vérifier la validité de l'intégralité d'une ligne du sudoku par rapport à une position dans le sudoku. elle vérifie si la case n'est pas en double.
	* @param num le numero de la case de 0 a 80 que l'on veut verifier.
	* @param surligne true = ModeSuriligner,false = ModeSuppresion.
	*/
    public void CheckLigne(byte num,boolean surligne)
    {
        if(this.caseGrille[num].GetWrongNumber())
        {
            return;
        }
        for(byte i = 0; i < 9;i++)
        {
            if(this.caseGrille[(num-num%9)+i].GetNumber() == this.caseGrille[num].GetNumber() && (num-num%9)+i != num)
            {
                if(surligne)
                {
                    this.caseGrille[(num-num%9)+i].SetWrongNumber(true);
                }
                else
                {
                    this.caseGrille[(num-num%9)+i].SetNumber((byte)0);
                }
            }
        }
    }

	/**
	* Permets de Vérifier la validité de l'intégralité d'une Colonne du sudoku par rapport à une position dans le sudoku. elle vérifie si la case n'est pas en double.
	* @param num le numero de la case de 0 a 80 que l'on veut verifier.
	* @param surligne true = ModeSuriligner,false = ModeSuppresion.
	*/
    public void CheckColonne(byte num,boolean surligne)
    {
        if(this.caseGrille[num].GetWrongNumber())
        {
            return;
        }
        for(byte i = 0; i < 9;i++)
        {
            if(this.caseGrille[num%9 + i*9].GetNumber() == this.caseGrille[num].GetNumber() && num%9 + i*9 != num)
            {
                if(surligne)
                {
                    this.caseGrille[num%9 + i*9].SetWrongNumber(true);
                }
                else
                {
                    this.caseGrille[num%9 + i*9].SetNumber((byte)0);
                }
            }
        }
    }

	/**
	* Permets de Vérifier la validité de l'intégralité d'un care du sudoku par rapport à une position dans le sudoku. elle vérifie si la case n'est pas en double.
	* @param num le numero de la case de 0 a 80 que l'on veut verifier.
	* @param surligne true = ModeSuriligner,false = ModeSuppresion.
	*/
    public void CheckCare(byte num,boolean surligne)
    {
        //num collone = ((int)((num%9)/3)+1)
        //num ligne = ((int)(((int)(num/9))/3)+1)
        //soluce = ((int)((num%9)/3)+1) *  ((int)(((int)(num/9))/3)+1)  j'ai fait pas mal de test pour trouver la solution avec de modulo et quil est le moin d'iteration possible ma solution a ete d'utiliser deux tableaux de byte.
        if(this.caseGrille[num].GetWrongNumber())
        {
            return;
        }
        /*byte numCare = (byte)((((int)((num%9)/3)+1)*((int)(((int)(num/9))/3)+1)-1));
        System.out.print(Integer.toString(num) + " = ");
        System.out.print(Integer.toString(Math.round((num%9)/3)+1) + " * ");
        System.out.print(Integer.toString(Math.round(((int)(num/9))/3)+1) + " = ");
        System.out.println(Integer.toString(   (Math.round((num%9)/3)+1) *  (Math.round(((int)(num/9))/3)+1)));*/
        for(byte i = 0; i < 9;i++)
        {
            if(this.caseGrille[this.sudoCareChecker[this.sudoCareCheckerPointeur[num]][i]].GetNumber() == this.caseGrille[num].GetNumber() && this.sudoCareChecker[this.sudoCareCheckerPointeur[num]][i] != num)
            {
                if(surligne)
                {
                    this.caseGrille[this.sudoCareChecker[this.sudoCareCheckerPointeur[num]][i]].SetWrongNumber(true);
                }
                else
                {
                    this.caseGrille[this.sudoCareChecker[this.sudoCareCheckerPointeur[num]][i]].SetNumber((byte)0);
                }
            }
        }

    }

	/**
	* Mais a jour les 81 cases Jcomponent Grace a un tableaux. 
	* @param loadNumber Tableaux de byte qui remplace les nombre de la grille du sudoku
	*/
    public void SetGrille(byte[] loadNumber)
    {
        for(byte i = 0; i < 81;i++)
        {
            this.caseGrille[i].SetNumber(loadNumber[i]);
        }
    }

	/**
	* @return retourne un tableau de byte récupérer sur les cases Jcomponent.
	*/
    public byte[] GetGrille()
    {
        byte[] saveNumber = new byte[81];
        for(byte i = 0; i < 81;i++)
        {
           saveNumber[i] =  this.caseGrille[i].GetNumber();
        }
        return saveNumber;
    }

	/**
	* @return retourne un tableau de int récupérer sur les cases Jcomponent.
	*/
    public int[] GetGrilleInt()
    {
        int[] saveNumber = new int[81];
        for(byte i = 0; i < 81;i++)
        {
           saveNumber[i] =  (int)(this.caseGrille[i].GetNumber());
        }
        return saveNumber;
    }

	/**
	* Applique l'algorithme de Backtracking pour resoudre le sudoku.
	* @param Message true = affiche un message, false = aucun message.
	*/
    public void ApplyAlgoBacktraking(boolean Message)
    {
        long startNano = System.nanoTime();
        String msgTime;
        SetGrille(bt.ApplyAlgo(GetGrille()));
        this.UncheckAll();
        this.CheckSudoku(true);
        if(Message)
        {
            long endNano = System.nanoTime();
            float delay = (float)(endNano-startNano)/1000000000;
            msgTime = "La resolution du sudoku a pris " + delay + " seconde";
            JOptionPane.showMessageDialog(this.fenetre,msgTime,"Resolution",1);
        }
    }

	/**
	* Permet de changer la valeur de la cases Jcomponent selection et d'ajoute des note a la cases en cas de doute.
	* @param n le nombre remplacer dans la cases.
	*/
    public void SetKeyEventNumber(byte n)
    {
        if(this.currentSelectedID >= 0 && this.currentSelectedID <= 81)
        {
            if(this.modeNote)
            {
                this.caseGrille[this.currentSelectedID].AddNote(n);
            }
            else
            {
                this.caseGrille[this.currentSelectedID].SetNumber(n);
                this.UncheckAll();
                this.CheckSudoku(true);
            }
        }
    }

	/**
	* Un simple message de Bienvenue qui nessecite une Jframe.
	*/
    public void MsgBienvenue()
    {
        JOptionPane.showMessageDialog(this.fenetre,"Bienvenue dans le sudoku.","Sudoku",1);
    }

	/**
	* Un simple message d'aide/Liste des touches qui nessecite une Jframe.
	*/
    public void AideMessage()
    {
        JOptionPane.showMessageDialog(this.fenetre,"Liste des Touches \n\nAide: h \nPlacer Des nombres: 0123456789 \nNouvelle Grille: n \nResoudre: r \nImporter une Grille: i\nExporter une Grille: e \nBasculer en Mode Edition: m \nBasculer en Mode Note: d \nFermer: echap","Aide",1);
    }
    
	/**
	* Permets de changer de mode entre le mode de jeux et le mode D'édition des cases.
	*/
    public void ChangeMode()
    {
        this.modeEdition = !this.modeEdition;
        byte result = this.modeEdition ? (byte)0 : (byte)1;
        for(byte i = 0; i < 81;i++)
        {
            this.caseGrille[i].ChangeMode(result);
        }
        if(this.modeEdition)
        {
            this.UnBlockAll();
            this.modeNote = false;
        }
        else
        {
            this.BlockAllNumber();
        }
    }

	/**
	* Permets de rajouter un tooltip sur le curseur de la souris pour savoir dans quel mode on se trouve.
	* @param nom Prend en parametre le non du tooltip.
	*/
    public void ChangeToolTypeName(String nom)
    {
        for(byte i = 0; i < 81;i++)
        {
            this.caseGrille[i].setToolTipText(nom);
        }
    }

	/**
	* Permets de changer de mode entre le mode de jeux et le mode Note/Doute des cases que si on ne ceux trouve pas en mode .
	*/
    public void NoteMode()
    {
        if(this.modeEdition)
        {
            return;
        }
        this.modeNote = !this.modeNote;
        byte result = this.modeNote ? (byte)2 : (byte)1;
        for(byte i = 0; i < 81;i++)
        {
            this.caseGrille[i].ChangeMode(result);
        }
    }

	/**
	* Permets d'enlever pour toute les cases Jcomponent l'etat de valeur fausse.
	*/
    private void UncheckAll()
    {
        for(byte i = 0;i < 81;i++)
        {
            this.caseGrille[i].SetWrongNumber(false);
        }
    }

	/**
	* Permets de debloquer les cases Jcomponent pour pouvoir modifier leur valeur.
	*/
    public void UnBlockAll()
    {
        for(byte i = 0; i < 81;i++)
        {
            this.caseGrille[i].SetBlock(false);
        }
    }
		
	/**
	* Permets de bloquer les cases Jcomponent.
	*/
    public void BlockAllNumber()
    {
        for(byte i = 0; i < 81;i++)
        {
            this.caseGrille[i].SetBlock(this.caseGrille[i].GetNumber() >= 1);
        }
    }

	/**
	* Permet de bloquer une valeur entre deux nombre.
	*/
    private int Clamp(int val, int min, int max) 
    {
        return Math.max(min, Math.min(max, val));
    }
}