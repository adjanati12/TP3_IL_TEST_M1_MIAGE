import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PolynomeImplTest {

    private Polynome p;

    // Fixture : un polynôme neuf avant chaque test
    @BeforeEach
    void setUp() {
        p = new PolynomeImpl();
    }

    //cas nominaux

    //polynôme neuf nul et son degré vaut -1
    @Test
    void testPolynomeNeufEstNul() {
        assertTrue(p.estNul());
        assertEquals(-1, p.getDegre());
    }

    // On relit bien les coefficients qu'on a définis
    @Test
    void testSetCoeffEtGetCoeff() {
        p.setCoeff(0, 3);
        p.setCoeff(1, 2);
        assertEquals(3, p.getCoeff(0));
        assertEquals(2, p.getCoeff(1));
    }

    //le degré est le rang du plus grand coefficient non nul
    @Test
    void testGetDegre() {
        p.setCoeff(0, 1);
        p.setCoeff(2, 5);
        assertEquals(2, p.getDegre());
        assertFalse(p.estNul());
    }

    // "101" doit donner x**2 + 1
    @Test
    void testInitializeBinaire() throws NonBinaireException {
        p.initialize("101"); // x**2 + 1
        assertEquals(2, p.getDegre());
        assertEquals(1, p.getCoeff(0));
        assertEquals(0, p.getCoeff(1));
        assertEquals(1, p.getCoeff(2));
    }

    // initialize puis affichageBinaire redonne la même chaîne
    @Test
    void testAffichageBinaireAllerRetour() throws NonBinaireException {
        p.initialize("1011010");
        assertEquals("1011010", p.affichageBinaire());
    }

    // Affichage classique avec plusieurs coeff
    @Test
    void testAffichage() {
        p.setCoeff(0, 1);
        p.setCoeff(1, 2);
        p.setCoeff(2, 3);
        assertEquals("3x**2 + 2x + 1", p.affichage());
    }

    // Le coeff 1 est omis devant x
    @Test
    void testAffichageCoefficientsUnitaires() throws NonBinaireException {
        p.initialize("101"); // x**2 + 1
        assertEquals("x**2 + 1", p.affichage());
    }

    //cas limites
    // un rang jamais défini vaut 0
    @Test
    void testGetCoeffAuDelaDuDegre() throws NonBinaireException {
        p.initialize("11");
        assertEquals(0, p.getCoeff(10));
    }

    // Remettre le seul coefficient à 0 rend le polynôme nul
    @Test
    void testPolynomeNulApresMiseAZero() {
        p.setCoeff(3, 1);
        p.setCoeff(3, 0);
        assertTrue(p.estNul());
        assertEquals(-1, p.getDegre());
    }
    // "0" doit donner le polynôme nul
    @Test
    void testInitializeZero() throws NonBinaireException {
        p.initialize("0");
        assertTrue(p.estNul());
    }

    // Les '0' de tête sont ignorés
    @Test
    void testInitializeAvecZerosDeTete() throws NonBinaireException {
        p.initialize("00101");
        assertEquals(2, p.getDegre());
        assertEquals("101", p.affichageBinaire());
    }

    //nouvel initialize qui écrase l'ancien contenu
    @Test
    void testInitializeRemplaceLeContenuPrecedent() throws NonBinaireException {
        p.initialize("1111");
        p.initialize("1");
        assertEquals(0, p.getDegre());
        assertEquals("1", p.affichageBinaire());
    }
    //polynôme nul s'affiche "0" en binaire
    @Test
    void testAffichageBinairePolynomeNul() throws NonBinaireException {
        assertEquals("0", p.affichageBinaire());
    }

    //polynôme nul s'affiche "0" en forme normale
    @Test
    void testAffichagePolynomeNul() {
        assertEquals("0", p.affichage());
    }

    //cas d'erreur
//chiffre autre que 0 ou 1 est refusé
    @Test
    void testInitializeChaineNonBinaire() {
        assertThrows(NonBinaireException.class, () -> p.initialize("102"));
    }

    // Des lettres sont refusées
    @Test
    void testInitializeChaineAvecLettres() {
        assertThrows(NonBinaireException.class, () -> p.initialize("abc"));
    }

    // une chaîne vide est refusée
    @Test
    void testInitializeChaineVide() {
        assertThrows(NonBinaireException.class, () -> p.initialize(""));
    }

    // null est refusé
    @Test
    void testInitializeNull() {
        assertThrows(NonBinaireException.class, () -> p.initialize(null));
    }

    //coeff > 1 empêche l'affichage binaire
    @Test
    void testAffichageBinaireCoefficientSuperieurAUn() {
        p.setCoeff(2, 3);
        assertThrows(NonBinaireException.class, () -> p.affichageBinaire());
    }
    //coefficient négatif empêche l'affichage binaire
    @Test
    void testAffichageBinaireCoefficientNegatif() {
        p.setCoeff(1, -1);
        assertThrows(NonBinaireException.class, () -> p.affichageBinaire());
    }

    //rang négatif est refusé par setCoeff
    @Test
    void testSetCoeffRangNegatif() {
        assertThrows(IllegalArgumentException.class, () -> p.setCoeff(-1, 1));
    }
}