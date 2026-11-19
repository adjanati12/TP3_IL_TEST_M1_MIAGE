public interface PolyCalc {
    /**
     * Définit le polynôme A
     * @param polyA le polynôme A
     */
    void setPolyA(Polynome polyA);
    /**
     * Renvoie le polynôme A
     * @return le polynôme A
     */
    Polynome getPolyA();
    /**
     * Définit le polynôme B
     * @param polyB le polynome B
     */
    void setPolyB(Polynome polyB);
    /**
     * Renvoie le polynôme B
     * @return le polynôme B
     */
    Polynome getPolyB();
    /**
     * Effectue une addition des polynomes A + B
     */
    void ajouter();
    /**
     * Ajouter binaire (utilise xor = somme sans retenue)
     */
    void ajouterBinaire();
    /**
     * Effectue une soustraction des polynomes A - B
     */
    void soustraire();
    /**
     * Effectue une multiplication des polynomes A * B
     */
    void multiplier();
    /**
     * Multiplier binaire (utilise une boucle de xor = somme sans retenue)
     */
    void multiplierBinaire();
    /**
     * Effectue une division polynomiale normale : A / B
     */
    void diviser();
    /**
     * Effectue une division polynomiale binaire sans retenue (pour les CRC) : A / B
     * @throws NonBinaireException si l'un des 2 polynômes a un coeff non binaire
     */
    void diviserBinaire() throws NonBinaireException;
    /**
     * Permet de récupérer le résultat
     * @return le résultat (ou le quotient pour la division)
     */
    Polynome getResultat();
    /**
     * Permet de récupérer le reste
     * @return le reste
     */
    Polynome getReste();
}