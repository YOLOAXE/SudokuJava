import javax.swing.*;  
import java.io.*;

/**
* Cette classe <code>CSudoku</code> est utilisée pour le control des fichies du sudoku
*
* @version 0.1
* @author Aurelien Monpierre
*/
public class CSudoku
{
	/* (Déterminer les paramètres du sudoku.) */

    private String fsudoku;
    private int[] conteneur;
    private JFrame fenetre;
    
	/**
	* @param f On recupere la fenetre pour envoyer des message grace a JOptionPane.
	*/
    public CSudoku(JFrame f)
    {
        this.fenetre = f;
    }

	/**
	* Permets de Lire/charger un fichier afin d'obtenire une grille deja enregistrer.
	* @return retorune un tableau de byte qui contienne les informations de la grille d'une taille de 81.
	*/
    public byte[] ChargeFile()
    {          
	    JFileChooser fc = new JFileChooser(); 
	    fc.setCurrentDirectory(new java.io.File("./saves"));
	    fc.setDialogTitle("Choisir un Sudoku");
        int i = fc.showOpenDialog(null);
        byte[] result = new byte[81];

        if(i == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = fc.getSelectedFile();                
            String filepath=selectedFile.getPath(); 
            result = this.GetFile(filepath);
        }
        return result;
    }

	/**
	* Permets de Sauvegarder une grille afin de la recuperer plus tard
	* @param array prend en argument un tableau de byte de taille 81 qui contient des nombre de 0 a 9 de la grille.
	*/
    public void SaveSudoku(int[] array) 
    {
	    this.conteneur = array;
	    /* (Enregistrer les paramètres du sudoku.) */
	    int i=0;
        int increment=0;
        int[] conteneur3 = new int[100];
        int j=0;
        String str="";
        boolean result = true;

        try
        {
            /*On Crée un JFileChooser,le répertoire source du JFileChooser est le répertoire d'où est lancé notre programme
            Le bouton pour valider l'enregistrement portera la mention ENREGSITRER,Pour afficher le JFileChooser.
            Si l'utilisateur clique sur le bouton ENREGSITRER, on récupére le nom du fichier qu'il a spécifié,Si ce nom de fichier finit par .txt ou .TXT, ne rien faire et passer à a suite
            Sinon renommer le fichier pour qu'il porte l'extension .GRI*/
            JFileChooser filechoose = new JFileChooser();
            filechoose.setCurrentDirectory(new File("."));
            String approve = new String("ENREGISTRER");
            int resultatEnregistrer = filechoose.showDialog(filechoose,approve);
            if (resultatEnregistrer == JFileChooser.APPROVE_OPTION)
            {
                String monFichier= new String(filechoose.getSelectedFile().toString());
                if(!(monFichier.endsWith(".gri") || monFichier.endsWith(".GRI")))
                {
                    monFichier = monFichier+ ".gri";
                }
                { 
                    FileOutputStream fichier = new FileOutputStream(monFichier);
                    DataOutputStream ecriture = new DataOutputStream(fichier);
                    try
                    {   
                        for(j=0;j<9;j++)
                        {
                            for(i=0;i<9;i++)
                            { 
                                str += Integer.toHexString(conteneur[increment]);
                                increment++;
                            } 
                            conteneur3[j]=Integer.parseInt(str);
                            str="";
                        }
                        for(i=0;i<9;i++)
                        {
                            ecriture.writeInt(conteneur3[i]);
                        }
                    }
                    catch(IOException err)
                    {
                        System.err.println("Ecriture impossible !");
                        JOptionPane.showMessageDialog(this.fenetre,"Ecriture impossible !","Sudoku",0);
                        result = false;
                    }

                    try
                    {
                        ecriture.close();
                    }
                    catch(IOException err)
                    {
                        System.err.println("Fermeture impossible !");
                        JOptionPane.showMessageDialog(this.fenetre,"Fermeture impossible !","Sudoku",0);
                        result = false;
                    }
                }   
            }
        }
        catch(FileNotFoundException err)
        {
            System.err.println("Ouverture du fichier impossible !");
            JOptionPane.showMessageDialog(this.fenetre,"Ouverture du fichier impossible !","Sudoku",0);
            result = false;
        }
        if(result)
        {
            JOptionPane.showMessageDialog(this.fenetre,"Le fichier a ete sauvegarder.","Sauvegarde",1);
        }

    }



	/**
	* Permets de Lire/charger un fichier afin d'obtenire une grille deja enregistrer.
	* @param sudo chemin d'acces vers le fichier.
	* @return retorune un tableau de byte qui contienne les informations de la grille d'une taille de 81.
	*/
    public byte[] GetFile(String sudo)
    {
        this.fsudoku = sudo;
        int[] conteneur = new int[10];
        byte[] sudokuGrille = new byte[81]; 
        int ligne = 0;
        byte iteration = 0;
        String str1 = ""; 

        /* Lit octet par octet les information du fichier source pour
        déterminer les paramètres du sudoku. on va lire le fichier, on va lire les octets de chaque entiers
        Et donc on va lire 9 fois 4 octets */

        try
        {
            FileInputStream fichier = new FileInputStream(fsudoku);
            DataInputStream lecture = new DataInputStream(fichier);
            try
            {	
            for(ligne =0;ligne < 9; ligne++)
            {
                conteneur[ligne]=lecture.readInt();		
                //System.out.println(Integer.toString(conteneur[ligne]));
            }	

            }
            catch(IOException e)
            {
                System.err.println("Lecture impossible !");
                JOptionPane.showMessageDialog(this.fenetre,"Lecture impossible !","Sudoku",0);
            }
            try
            {
                lecture.close();
            }
            catch(IOException e)
            {
                System.err.println("Fermeture impossible !");
                JOptionPane.showMessageDialog(this.fenetre,"Fermeture impossible !","Sudoku",0);
            }
        }
        catch(FileNotFoundException e)
        {
            System.err.println("Ouverture du fichier impossible !");
            JOptionPane.showMessageDialog(this.fenetre,"Ouverture du fichier impossible !","Sudoku",0);
        }

        for(byte i = 0; i < 9;i++)
        {
            str1 = Integer.toString(conteneur[i]);
            for(byte j = 0; j < 9;j++)
            {
                if(j-(9-str1.length()) >= 0)
                {
                    sudokuGrille[iteration] = Byte.parseByte(str1.substring(j-(9-str1.length()),j-(9-str1.length())+1));
                    //System.out.println(str1.substring(j-(9-str1.length()),j-(9-str1.length())+1) + " = " + Integer.toString(iteration));
                }
                else
                {
                    sudokuGrille[iteration] = (byte)0;
                    //System.out.println(Integer.toString(sudokuGrille[iteration]) + " = " + Integer.toString(iteration));
                }
                iteration++;
            }
        }
        return sudokuGrille;
    }
    
}