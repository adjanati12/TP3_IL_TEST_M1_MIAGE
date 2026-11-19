//calculatrice de polynômes : opérations normales et binaires (sans retenue)
public class PolyCalcImpl implements PolyCalc {

    //opérandes
    private Polynome polyA;
    private Polynome polyB;

    // Résultat de la dernière opération (quotient pour une division)
    private Polynome resultat = new PolynomeImpl();

    // Reste de la dernière division (nul pour les autres opérations)
    private Polynome reste = new PolynomeImpl();


    //les accesseurs
    //polynôme A
    public void setPolyA(Polynome polyA) {
        this.polyA = polyA;
    }
    public Polynome getPolyA() {
        return polyA;
    }

    //polynôme B
    public void setPolyB(Polynome polyB) {
        this.polyB = polyB;
    }
    public Polynome getPolyB() {
        return polyB;
    }

    // peut aussi return le quotient pour une division
    public Polynome getResultat() {
        return resultat;
    }

    //reste de la dernière division
    public Polynome getReste() {
        return reste;
    }


    // ( + et -)
    // A + B : addition des coefficients de même rang
    public void ajouter() {
        verifierOperandes();

        int max = Math.max(polyA.getDegre(), polyB.getDegre());
        Polynome r = new PolynomeImpl();

        for (int i = 0; i <= max; i++) {
            r.setCoeff(i, polyA.getCoeff(i) + polyB.getCoeff(i));
        }
        enregistrer(r, new PolynomeImpl());
    }

    // A + B sans retenue : le XOR remplace l'addition (coeff supposés 0 ou 1)
    public void ajouterBinaire() {
        verifierOperandes();

        int max = Math.max(polyA.getDegre(), polyB.getDegre());
        Polynome r = new PolynomeImpl();

        for (int i = 0; i <= max; i++) {
            r.setCoeff(i, polyA.getCoeff(i) ^ polyB.getCoeff(i));
        }
        enregistrer(r, new PolynomeImpl());
    }

    // A - B : on soustrait les coefficients de même rang
    public void soustraire() {
        verifierOperandes();

        int max = Math.max(polyA.getDegre(), polyB.getDegre());
        Polynome r = new PolynomeImpl();

        for (int i = 0; i <= max; i++) {
            r.setCoeff(i, polyA.getCoeff(i) - polyB.getCoeff(i));
        }
        enregistrer(r, new PolynomeImpl());
    }

    // multiplication
    // A * B : chaque terme de A est multiplié par chaque terme de B
    public void multiplier() {
        verifierOperandes();

        Polynome r = new PolynomeImpl();

        for (int i = 0; i <= polyA.getDegre(); i++) {
            for (int j = 0; j <= polyB.getDegre(); j++) {
                // le terme en x**i fois le terme en x**j donne un terme en x**(i+j)
                r.setCoeff(i + j, r.getCoeff(i + j) + polyA.getCoeff(i) * polyB.getCoeff(j));
            }
        }
        enregistrer(r, new PolynomeImpl());
    }

    // A * B sans retenue : pour chaque terme de B, on ajoute (XOR) A décalé
    public void multiplierBinaire() {
        verifierOperandes();

        Polynome r = new PolynomeImpl();

        for (int j = 0; j <= polyB.getDegre(); j++) {
            // seuls les termes de B non nuls comptent
            if (polyB.getCoeff(j) != 0) {
                for (int i = 0; i <= polyA.getDegre(); i++) {
                    // A décalé de j rangs, combiné par XOR au résultat
                    r.setCoeff(i + j, r.getCoeff(i + j) ^ polyA.getCoeff(i));
                }
            }
        }
        enregistrer(r, new PolynomeImpl());
    }


    // division
    // A / B avec des coefficients entiers (division tronquée)
    public void diviser() {
        verifierOperandes();
        verifierDiviseurNonNul();

        Polynome q = new PolynomeImpl();
        Polynome r = copie(polyA);
        int dB = polyB.getDegre();
        int lead = polyB.getCoeff(dB); // coeff dominant de B

        while (!r.estNul() && r.getDegre() >= dB) {
            int decalage = r.getDegre() - dB;
            int coef = r.getCoeff(r.getDegre()) / lead; // division entière

            // si le coefficient est 0, on ne peut plus avancer : on s'arrête
            if (coef == 0) break;
            q.setCoeff(decalage, q.getCoeff(decalage) + coef);

            // on retire coef * x**decalage * B du reste
            for (int i = 0; i <= dB; i++) {
                r.setCoeff(i + decalage, r.getCoeff(i + decalage) - coef * polyB.getCoeff(i));
            }
        }
        enregistrer(q, r);
    }

    // A / B sans retenue (pour les CRC) : la soustraction devient un XOR
    public void diviserBinaire() throws NonBinaireException {
        verifierOperandes();
        // lèvent NonBinaireException si un coefficient n'est pas 0 ou 1
        polyA.affichageBinaire();
        polyB.affichageBinaire();
        verifierDiviseurNonNul();

        Polynome q = new PolynomeImpl();
        Polynome r = copie(polyA);
        int dB = polyB.getDegre();

        // tant que le reste est au moins de même degré que B
        while (!r.estNul() && r.getDegre() >= dB) {
            int decalage = r.getDegre() - dB;
            // le bit du quotient à ce rang vaut 1
            q.setCoeff(decalage, 1);

            // on retire (XOR) B décalé pour annuler le terme de plus haut degré
            for (int i = 0; i <= dB; i++) {
                r.setCoeff(i + decalage, r.getCoeff(i + decalage) ^ polyB.getCoeff(i));
            }
        }
        enregistrer(q, r);
    }


    // methodes utilitaires
    // on range le résultat et le reste de l'opération
    private void enregistrer(Polynome resultat, Polynome reste) {
        this.resultat = resultat;
        this.reste = reste;
    }

    //refuse l'opération si A ou B n'a pas été défini
    private void verifierOperandes() {
        if (polyA == null || polyB == null) {
            throw new IllegalStateException("Les polynômes A et B doivent être définis");
        }
    }

    // refuse la division par le polynôme nul
    private void verifierDiviseurNonNul() {
        if (polyB.estNul()) {
            throw new ArithmeticException("Division par le polynôme nul");
        }
    }

    //copie d'un polynôme pour ne pas modifier l'original
    private static Polynome copie(Polynome p) {
        Polynome c = new PolynomeImpl();

        for (int i = 0; i <= p.getDegre(); i++) {
            c.setCoeff(i, p.getCoeff(i));
        }
        return c;
    }
}