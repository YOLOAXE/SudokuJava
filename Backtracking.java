/**
* Cette classe <code>Backtracking</code> est utilisée pour resoudre une grille de sudoku.
*
* @version 0.1
* @author Mickael Albarello
*/
public class Backtracking
{
    private byte[][] grille;

    public Backtracking()
    {
        this.grille = new byte[9][9];
    }

    ///////////Algorithmes de Backtracking

	/**
	* Permets de verifier si l'indice k est absant sur la ligne.
	* @param grille La grille sur laquelle on fait le test.
	* @param k La valeur qui est testée pour savoir si elle est presente sur la ligne de la grille.
	* @param i L'indice de la ligne sur laquelle on test k.
	* @return Retourne true si la valeur du parametre k n'est pas sur la ligne de la grille sinon il retourne false.
	*/
    private boolean AbsSurLaLigne (byte grille[][],byte k,byte i)
    {   
        for(byte j = 0; j < 9; j++)//On parcour la ligne entiere.
        {
            if(grille[i][j] == k)//On test si k est sur la ligne si oui alors on return false.
            {
                return false;
            }
        }
        return true;//si k n'est pas sur la ligne alors on return true.
    }

	/**
	* Permets de verifier si l'indice k est absant sur la colonne.
	* @param grille La grille sur laquelle on fait le test.
	* @param k La valeur qui est testée pour savoir si elle est presente sur la colonne de la grille.
	* @param i L'indice de la colonne sur laquelle on test k.
	* @return Retourne true si la valeur du parametre k n'est pas sur la colonne de la grille sinon il retourne false.
	*/
    private boolean AbsSurLaColonne (byte grille[][],byte k,byte j)
    {   
        for(byte i = 0; i < 9; i++)//On parcour la ligne entiere.
        {
            if(grille[i][j] == k)//On test si k est sur la colonne si oui alors on return false.
            {
                return false;
            }
        }
        return true;//si k n'est pas sur la colonne alors on return true.
    }

	/**
	* Permets de verifier si l'indice k est absant sur le care.
	* @param grille La grille sur laquelle on fait le test.
	* @param k La valeur qui est testée pour savoir si elle est presente sur le care de la grille.
	* @param i L'indice de la ligne sur laquelle on test k.
	* @param j L'indice de la collonne sur laquelle on test k.
	* @return Retourne true si la valeur du parametre k n'est pas sur le care de la grille sinon il retourne false.
	*/
    private boolean AbsSurBloc (byte grille[][],byte k,byte i,byte j)
    {   
        byte a = (byte)(i-(i%3));//a prend la valeur de la position 0 sur la colonne du care grace au modulo.
		byte b = (byte)(j-(j%3));//b prend la valeur de la position 0 sur la ligne du care grace au modulo.

        for(i = a; i < a+3;i++)//on parcour les 3 colonnes du care.
        {
            for(j = b; j < b+3;j++)//on parcour les 3 lignes du care.
            {
                if(grille[i][j] == k)//on test si k est present sur les position du care. Si oui on return false.
                {
                    return false;
                }
            }
        }
        return true;//si k n'est pas present sur le care alors on return true.
    }

	/**
	* Pemets de Résoudre la grille en utilisant une méthode récursive.
	* @param grille Prend en parametre une grille de 9*9 pour la resoudre.
	* @param pos la position de depart "0" de la grille permet au la methode recursive de s'arrete.
	* @return Retourne true si une solution est trouver.
	*/
    private boolean Valide(byte grille[][], byte pos)
    {
        if(pos == 81)//Si on atteint la fin de la fonction recursive on return la solution.
        {
            return true;
        }
        byte i = (byte)(pos/9);//On recupere la colonne selon la position initial du debut de la fonction.
        byte j = (byte)(pos%9);//On recupere la ligne selon la position initial du debut de la fonction.
    
        if(grille[i][j] != 0)// Si le nombre sur la grille n'est pas egale a 0 alors on test la prochaine position.
        {
            return this.Valide(grille, (byte)(pos+1));
        }
        for(byte k=1;k <= 9;k++)//Si par contre le nombre est egale a zero alors on va chercher un nombre qui n'est pas present sur la ligne/colonne/care de cette position et l'attribuer.
        {
            if(this.AbsSurLaLigne(grille,k , i) && this.AbsSurLaColonne(grille, k, j) && this.AbsSurBloc(grille, k, i, j))// test si k n'est ni sur la ligne/collone/care.
            {
                grille[i][j] = k;//On attribu k a la position.
                if(this.Valide(grille, (byte)(pos+1)))//Puis on test la prochaine position san return directement car si les prochaines position sont fausse on veut pouvoir remetre la valeur a 0.
                {
                    return true;
                }
            }
        }
        grille[i][j] = 0;//On remet la valeur a zero car la solution n'est pas la bonne.
        return false;//On return false car la solution n'est pas bonne.
    }

	/**
	* Pemets de modifier un tableau de 81 bytes en tableau 9*9 pour faciliter la résolution du Sudoku.
	* @param g Prend en parametre un tableaux de byte de 81 byte.
	*/
    private void SetGrilleOneTab(byte[] g)
    {
        byte k = 0;
        for(byte i = 0; i < 9;i++)
        {
            for(byte j = 0; j < 9;j++)
            {
                this.grille[i][j] = g[k++];
            }
        }
    }

	/**
	* Pemets de Résoudre la grille en utilisant une méthode récursive.
	* @param g Prend en parametre un tableaux de byte de taille 81.
	* @return Retourne un tableaux de byte de taille 81 resolue.
	*/
    public byte[] ApplyAlgo(byte[] g)
    {
        this.SetGrilleOneTab(g);
        Valide(this.grille,(byte)0);//On commence a la position 0.
        return this.GetGrilleOneTab();
    }

	/**
	* Pemets de modifier la grille 9*9 en un tableaux de 81 byte.
	* @return Retourne un tableaux de 81 byte.
	*/
    private byte[] GetGrilleOneTab()
    {
        byte[] g = new byte[81];
        byte k = 0;
        for(byte i = 0; i < 9;i++)
        {
            for(byte j = 0; j < 9;j++)
            {
                g[k++] = this.grille[i][j];
            }
        }
        return g;
    }
    ///////////////////////
}