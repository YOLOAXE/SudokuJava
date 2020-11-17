import javax.swing.*;
import java.awt.*;

public class Main
{
    public static void main(String[] args)
    {
        JFrame fenetre = new JFrame("Jeux-Sudoku");//Création de la fenêtre Jeux-sudoku.
        GridLayout sudokuGrille = new GridLayout(9,9);//GridLayout utiliser pour la disposition des JComponent cases.
        Case[] caseGrille = new Case[81];//Les 81 cases du GridLayout 9*9.
        SudokuLogique sl = new SudokuLogique(caseGrille,fenetre);//Le cerveau du programme celui qui va Gérer le fonctionnement du sudoku
        SKeyCodeEvent kce = new SKeyCodeEvent(sl,new CSudoku(fenetre));//Les evenements lier au touche.

        for(byte i = 0; i < 81;i++)//Boucle for d'initialisation des cases plus l'ajout des événements lier a ces cases.
        {
            caseGrille[i] = new Case((byte)0);
            caseGrille[i].addMouseListener(new EventCase(sl,i));   
            fenetre.add(caseGrille[i]);
        }
        sl.InitAffichage();	//Initialise l'affichage de la grille.
        sl.InitNumberRandom();	//Genere un premier sudoku fesable par un humain!!
        
        fenetre.addKeyListener(kce);	//Ajout des interations de touche sur la fenetre.
        fenetre.setBackground(new Color(50,50,50));	//Change la couleur du Background pour éviter des artefacts de couleur au moment du resize.
        fenetre.setLayout(sudokuGrille);	//Ajoute le GridLayout a la fenete.
        fenetre.setSize(600,600);	//Taille de base de la fenetre.
        fenetre.setMinimumSize(new Dimension(600,600));//Taille Minimum de la fenetre.
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.setVisible(true);//Fenetre Visible.
        sl.MsgBienvenue();// Affiche un message de bienvenue
        sl.AideMessage();// Affiche un message D'aide pour les touches a utiliser.
    }
}