//Implantation de l'interface Polynome.
import java.util.Arrays;
//représentation interne : un tableau d'entiers où coeffs[i] est le coefficient du terme en x**i.
 //exemple : 3x**2 + 2x + 1  ->  coeffs = {1, 2, 3}
 //                                       (rang 0, 1, 2)
 //convention choisie : le polynôme nul a pour degré -1.

public class PolynomeImpl implements Polynome {
    //tab de coeff, indexé par le rang (puissance de x).
    //peut contenir des zéros "de tête" (ex : {1, 0, 0}), d'où le calcul du degré réel dans getDegre().
    private int[] coeffs = new int[1];

     // Le degré est le rang du plus grand coefficient non nul.
     //on parcourt le tableau depuis la fin pour le trouver.
     //on return le degré, ou -1 si le polynôme est nul
    public int getDegre() {
        for (int i = coeffs.length - 1; i >= 0; i--) {
            if (coeffs[i] != 0) return i;
        }
        return -1; // aucun coefficient non nul -> polynôme nul
    }

    //param i le rang (puissance de x)
    public int getCoeff(int i) {
        if (i < 0) throw new IllegalArgumentException("rang négatif");
        return i < coeffs.length ? coeffs[i] : 0;
        //return le coeff de x**i, ou 0 si i dépasse la taille du tab (un coeff jamais défini = 0)
    }

     // methode qui définit le coefficient de x**i. Agrandit le tableau si le rang
     // dépasse sa taille actuelle (les nouvelles cases valent 0).
    public void setCoeff(int i, int coeff) {
        if (i < 0) throw new IllegalArgumentException("rang négatif");
        if (i >= coeffs.length) coeffs = Arrays.copyOf(coeffs, i + 1);
        coeffs[i] = coeff;
    }

    // Affichage : du plus grand degré au plus petit.
     //Ex: "3x**2 + 2x + 1", "x**2 + 1", "0" pour le polynôme nul.
     // règles : les termes à coefficient 0 sont omis, et le coefficient 1
     //est omis devant x (on écrit "x" et non "1x")
    @Override
    public String affichage() {
        if (estNul()) return "0";
        StringBuilder sb = new StringBuilder();
        for (int i = getDegre(); i >= 0; i--) {
            int c = coeffs[i];
            if (c == 0) continue; // terme absent

            // Signe : " + " / " - " entre les termes, "-" collé au premier terme
            if (sb.length() > 0) sb.append(c > 0 ? " + " : " - ");
            else if (c < 0) sb.append("-");

            //coeff : omis s'il vaut 1 devant x, sauf pour le terme constant
            int abs = Math.abs(c);
            if (abs != 1 || i == 0) sb.append(abs);

            // partie variable : x pour le rang 1, x**i pour les rangs >= 2
            if (i >= 1) sb.append("x");
            if (i >= 2) sb.append("**").append(i);
        }
        return sb.toString();
    }

    public boolean estNul() {
        return getDegre() == -1;//true si coeff = null
    }

    // affichage binaire, du plus grand degré au plus petit : x**2 + 1 -> "101".
     // les 0 de tête ne sont pas affichés (le degré est le plus haut coefficient non nul).
    public String affichageBinaire() throws NonBinaireException {
        if (estNul()) return "0";
        StringBuilder sb = new StringBuilder();
        for (int i = getDegre(); i >= 0; i--) {
            if (coeffs[i] != 0 && coeffs[i] != 1)
                throw new NonBinaireException("Coefficient non binaire au rang " + i);
            //si un coeff n'est ni 0 ni 1
            sb.append(coeffs[i]);
        }
        return sb.toString();//chaine binaire '0' pour le polynome nul
    }

   //remplace le contenu du polynôme par celui d'une chaîne binaire.
   //le caractère le plus à droite est le coefficient de rang 0 :"101" -> x**2 + 1
    public void initialize(String s) throws NonBinaireException {
        if (s == null || !s.matches("[01]+"))
            throw new NonBinaireException("Chaîne non binaire : " + s);
        //si chaine=NULL, vide, ou autre chose que 0 et 1
        coeffs = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            // le caractère à la position (length-1-i) correspond au rang i
            coeffs[i] = s.charAt(s.length() - 1 - i) - '0';
        }
    }
}