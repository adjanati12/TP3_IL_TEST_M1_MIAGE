import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PolyCalcImplTest {

    private PolyCalc calc;

    //Création d'une calculatrice neuve avant chaque test
    @BeforeEach
    void setUp() {
        calc = new PolyCalcImpl();
    }

    //méthodes utilitaires
    //construit un polynôme à partir de ses coeff : poly(1, 2, 3) = 3x**2 +2x + 1
    private Polynome poly(int... coeffs) {
        Polynome p = new PolynomeImpl();

        for (int i = 0; i < coeffs.length; i++) {
            p.setCoeff(i, coeffs[i]);
        }

        return p;
    }

    //construit un polynôme à partir d'une chaîne binaire : bin("101") = x**2 + 1
    private Polynome bin(String s) throws NonBinaireException {
        Polynome p = new PolynomeImpl();
        p.initialize(s);
        return p;
    }

    // place les 2 opérandes dans la calculatrice
    private void definir(Polynome a, Polynome b) {
        calc.setPolyA(a);
        calc.setPolyB(b);
    }


    //accesseurs
    //récupèration des polynômes qu'on a définis
    @Test
    void testSetEtGetPolyAEtB() {
        Polynome a = poly(1);
        Polynome b = poly(2);
        definir(a, b);
        assertSame(a, calc.getPolyA());
        assertSame(b, calc.getPolyB());
    }

    //Avant toute opération, le résultat et le reste sont nuls
    @Test
    void testResultatEtResteInitialementNuls() {
        assertTrue(calc.getResultat().estNul());
        assertTrue(calc.getReste().estNul());
    }


    //addition
    // (3x**2 + 2x + 1)+(5x + 4) = 3x**2 +7x+5
    @Test
    void testAjouter() {
        definir(poly(1, 2, 3), poly(4, 5));
        calc.ajouter();
        assertEquals(5, calc.getResultat().getCoeff(0));
        assertEquals(7, calc.getResultat().getCoeff(1));
        assertEquals(3, calc.getResultat().getCoeff(2));
    }

    // Ajouter le polynôme nul ne change rien
    @Test
    void testAjouterPolynomeNul() {
        definir(poly(1, 2, 3), poly());
        calc.ajouter();
        assertEquals("3x**2 + 2x + 1", calc.getResultat().affichage());
    }

    // Après une addition, le reste est nul
    @Test
    void testAjouterLaisseResteNul() {
        definir(poly(1), poly(1));
        calc.ajouter();
        assertTrue(calc.getReste().estNul());
    }

    @Test
    void testAjouterBinaire() throws NonBinaireException {
        definir(bin("1011"), bin("110"));
        calc.ajouterBinaire();
        assertEquals("1101", calc.getResultat().affichageBinaire());
    }

    //polynôme ajouté à lui-même donne 0 en binaire
    @Test
    void testAjouterBinaireMemePolynomeDonneNul() throws NonBinaireException {
        definir(bin("101"), bin("101"));
        calc.ajouterBinaire();
        assertTrue(calc.getResultat().estNul());
    }


    //soustraction
    // (3x**2 + 2x + 1)-(x**2 + 2x + 1) = 2x**2
    @Test
    void testSoustraire() {
        definir(poly(1, 2, 3), poly(1, 2, 1));
        calc.soustraire();
        assertEquals(2, calc.getResultat().getDegre());
        assertEquals(2, calc.getResultat().getCoeff(2));
        assertEquals("2x**2", calc.getResultat().affichage());
    }

    // 1-3 = -2 (coeff négatif)
    @Test
    void testSoustraireResultatNegatif() {
        definir(poly(1), poly(3));
        calc.soustraire();
        assertEquals(-2, calc.getResultat().getCoeff(0));
    }

    // soustraire un polynôme à lui-même donne le polynôme nul
    @Test
    void testSoustraireMemePolynomeDonneNul() {
        definir(poly(1, 2, 3), poly(1, 2, 3));
        calc.soustraire();
        assertTrue(calc.getResultat().estNul());
    }


    //multiplication
    // (x + 1)*(x + 1) = x**2 + 2x +1
    @Test
    void testMultiplier() {
        definir(poly(1, 1), poly(1, 1));
        calc.multiplier();
        assertEquals(1, calc.getResultat().getCoeff(0));
        assertEquals(2, calc.getResultat().getCoeff(1));
        assertEquals(1, calc.getResultat().getCoeff(2));
    }

    // (x + 1)*(x - 1) = x**2 - 1
    @Test
    void testMultiplierIdentiteRemarquable() {
        definir(poly(1, 1), poly(-1, 1));
        calc.multiplier();
        assertEquals(2, calc.getResultat().getDegre());
        assertEquals(-1, calc.getResultat().getCoeff(0));
        assertEquals(0, calc.getResultat().getCoeff(1));
        assertEquals(1, calc.getResultat().getCoeff(2));
    }

    //multiplier par le polynôme nul donne le polynôme nul
    @Test
    void testMultiplierParNul() {
        definir(poly(1, 2, 3), poly());
        calc.multiplier();
        assertTrue(calc.getResultat().estNul());
    }

    // (x + 1)*(x + 1) = x**2 +1 en binaire
    @Test
    void testMultiplierBinaire() throws NonBinaireException {
        definir(bin("11"), bin("11"));
        calc.multiplierBinaire();
        assertEquals("101", calc.getResultat().affichageBinaire());
    }

    // (x**2 + 1)*(x + 1) = x**3 + x**2 +x+ 1 en binaire
    @Test
    void testMultiplierBinaireDegresDifferents() throws NonBinaireException {
        definir(bin("101"), bin("11"));
        calc.multiplierBinaire();
        assertEquals("1111", calc.getResultat().affichageBinaire());
    }

    //multiplier par '1' ne change rien
    @Test
    void testMultiplierBinaireParUn() throws NonBinaireException {
        definir(bin("1011"), bin("1"));
        calc.multiplierBinaire();
        assertEquals("1011", calc.getResultat().affichageBinaire());
    }

    //division normale
    // (x**2 + 2x + 1) / (x + 1) = x + 1, avec reste 0
    @Test
    void testDiviserExacte() {
        definir(poly(1, 2, 1), poly(1, 1));
        calc.diviser();
        assertEquals("x + 1", calc.getResultat().affichage());
        assertTrue(calc.getReste().estNul());
    }

    // (x**2 + 1) / (x + 1)= x - 1, avec reste 2
    @Test
    void testDiviserAvecReste() {
        definir(poly(1, 0, 1), poly(1, 1));
        calc.diviser();
        assertEquals(1, calc.getResultat().getCoeff(1));
        assertEquals(-1, calc.getResultat().getCoeff(0));
        assertEquals(0, calc.getReste().getDegre());
        assertEquals(2, calc.getReste().getCoeff(0));
    }

    //si A est de degré inférieur à B, le quotient est nul et le reste vaut A
    @Test
    void testDiviserDegreInferieur() {
        definir(poly(1), poly(0, 1));
        calc.diviser();
        assertTrue(calc.getResultat().estNul());
        assertEquals(1, calc.getReste().getCoeff(0));
    }

    //coefficient non divisible : la division s'arrête sans boucler
    @Test
    void testDiviserCoefficientNonDivisible() {
        definir(poly(0, 1), poly(0, 2));
        calc.diviser();
        assertTrue(calc.getResultat().estNul());
        assertEquals(1, calc.getReste().getCoeff(1));
    }

    //diviser par le polynôme nul est interdit
    @Test
    void testDiviserParNul() {
        definir(poly(1, 2), poly());
        assertThrows(ArithmeticException.class, () -> calc.diviser());
    }


    //division binaire
    //exemple du site asecuritysite : 100110000 / 101 = 1011010, avec reste 10
    @Test
    void testDiviserBinaireExempleSite() throws NonBinaireException {
        definir(bin("100110000"), bin("101"));
        calc.diviserBinaire();
        assertEquals("1011010", calc.getResultat().affichageBinaire());
        assertEquals("10", calc.getReste().affichageBinaire());
    }

    //vérification : quotient * B + reste redonne A
    @Test
    void testDiviserBinaireVerificationParMultiplication() throws NonBinaireException {
        Polynome b = bin("101");
        definir(bin("100110000"), b);
        calc.diviserBinaire();
        Polynome q = calc.getResultat();
        Polynome r = calc.getReste();

        definir(q, b);
        calc.multiplierBinaire();
        definir(calc.getResultat(), r);
        calc.ajouterBinaire();
        assertEquals("100110000", calc.getResultat().affichageBinaire());
    }

    //division exacte : 101 / 101 = 1, avec reste nul
    @Test
    void testDiviserBinaireExacte() throws NonBinaireException {
        definir(bin("101"), bin("101"));
        calc.diviserBinaire();
        assertEquals("1", calc.getResultat().affichageBinaire());
        assertTrue(calc.getReste().estNul());
    }

    //si A est de degré inférieur à B, le quotient est nul et le reste vaut A
    @Test
    void testDiviserBinaireDegreInferieur() throws NonBinaireException {
        definir(bin("11"), bin("101"));
        calc.diviserBinaire();
        assertTrue(calc.getResultat().estNul());
        assertEquals("11", calc.getReste().affichageBinaire());
    }

    //message avec son CRC (exemple du sujet) est divisible par le générateur
    @Test
    void testDiviserBinaireMessageAvecCrcResteNul() throws NonBinaireException {
        definir(bin("010000010100001011"), bin("101"));
        calc.diviserBinaire();
        assertTrue(calc.getReste().estNul());
    }

    //division qui ne modifie pas les polynômes A et B
    @Test
    void testDiviserBinaireNeModifiePasLesOperandes() throws NonBinaireException {
        definir(bin("100110000"), bin("101"));
        calc.diviserBinaire();
        assertEquals("100110000", calc.getPolyA().affichageBinaire());
        assertEquals("101", calc.getPolyB().affichageBinaire());
    }

    //coeff non binaire dans A est refusé
    @Test
    void testDiviserBinaireANonBinaire() {
        definir(poly(2, 1), poly(1, 1));
        assertThrows(NonBinaireException.class, () -> calc.diviserBinaire());
    }

    //coeff non binaire dans B est refusé
    @Test
    void testDiviserBinaireBNonBinaire() {
        definir(poly(1, 1), poly(3, 1));
        assertThrows(NonBinaireException.class, () -> calc.diviserBinaire());
    }

    //diviser par le polynôme nul est interdit
    @Test
    void testDiviserBinaireParNul() {
        definir(poly(1, 1), poly());
        assertThrows(ArithmeticException.class, () -> calc.diviserBinaire());
    }


    // erreur d'operandes
    // sans A ni B, une opération est refusée
    @Test
    void testOperationSansOperandes() {
        assertThrows(IllegalStateException.class, () -> calc.ajouter());
    }

    // si A seulement, une opération est refusée
    @Test
    void testOperationSansPolyB() {
        calc.setPolyA(poly(1));
        assertThrows(IllegalStateException.class, () -> calc.multiplier());
    }
}