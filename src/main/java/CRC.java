public interface CRC {
    /**
     * Définit le polynome générateur
     *
     * @param generateur le polynome générateur
     */
    void setPolynomeGenerateur(Polynome generateur);

    /**
     * Modifie le polynome pour que la division par le polynome générateur donne un
     * reste nul
     *
     * @param donnees le polynome représentant les données
     */
    void metAJour(Polynome donnees) throws NonBinaireException;

    /**
     * Vérifie que la division du polynome reçu par le polynome générateur est nulle
     *
     * @param recu le polynome reçu
     * @return true si le reste est nul
     */
    boolean verifie(Polynome recu) throws NonBinaireException;
}