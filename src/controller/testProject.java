package controller;
import static org.junit.Assert.*;
import java.util.ArrayList;
import io.Factory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * @author Jannik Niedermayer
 * @version 1.0
 * @date 28.11.2018
 */
public class testProject {

    /**
     * Konstruktor fuer die Test-Klasse testSth
     */
    public testProject() {
    }

    /**
     *  Setzt das Testgerüst fuer den Test.
     *
     * Wird vor jeder Testfall-Methode aufgerufen.
     */
    @Before
    public void setUp()
    {

    }
    /**
     * Gibt das Testgerüst wieder frei.
     *
     * Wird nach jeder Testfall-Methode aufgerufen.
     */
    @After
    public void tearDown()
    {
    }

    /**
     * testet ob der Profit richtig berechnet wird
     */


    @Test
    public void testProfitCounter() {
            assertEquals(29361.1,Factory.profitCounter(), 0.0);
        }
    }










