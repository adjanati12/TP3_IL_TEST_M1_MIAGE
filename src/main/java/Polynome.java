public interface Polynome {
    /**
     * Le degré du polynome : 1 pour x+1, 2 pour x**2+x+1
     * @return le degré
     */
    int getDegre();
    /**
     * Recupère le i-ème coefficient, pour 2x + 3, 0->3, 1->2
     * @param i le rang
     * @return le coefficient
     */
    int getCoeff(int i);
    /**
     * Définit le i-ème coefficient, pour 2x + 3, 0->3, 1->2
     * @param i le rang
     * @param coeff le coeff
     */
    void setCoeff(int i, int coeff);
    /**
     * Affiche le polynome en forme 3x**2 + 2x + 1
     * @return la chaine
     */
    String affichage();
    /**
     * Affiche le polynome sous une forme binaire pour x**2 + 1 : 101
     * @return la chaine ou null si il y a des coeffs > 1 ou < 0
     * @throws NonBinaireException si le polynome n'est pas binaire
     */
    String affichageBinaire() throws NonBinaireException;
    /**
     * Le polynome est-il nul
     * @return true s'il est nul
     */
    boolean estNul();
    /**
     * Initialise le polynome depuis une chaine binaire 101 pour x**2+1
     * @param chaineBinaire la chaine binaire
     * @throws NonBinaireException si la chaine n'est pas binaire
     */
    void initialize(String chaineBinaire) throws NonBinaireException;
}