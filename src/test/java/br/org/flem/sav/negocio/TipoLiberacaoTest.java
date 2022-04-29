/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.flem.sav.negocio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import junit.framework.TestCase;

/**
 *
 * @author mccosta
 */
public class TipoLiberacaoTest extends TestCase {
    
    public TipoLiberacaoTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Testa a Collection de retorno
     */
    
    
    public void testToCollection() {
        ArrayList<TipoLiberacao> tipos = new ArrayList<TipoLiberacao>(
                Arrays.asList(TipoLiberacao.PENDENCIA,
                              TipoLiberacao.RETROATIVO,
                              TipoLiberacao.LIMITE_50)
                );
        Collection result = TipoLiberacao.toCollection();
        assertEquals(tipos, result);
    }

    /**
     * Testa se retorna o tipoLiberação: PENDENCIA
     */
    
    public void testGetById_PENDENCIA() {
        Integer id = 0;
        TipoLiberacao expResult = TipoLiberacao.PENDENCIA;
        TipoLiberacao result = TipoLiberacao.getById(id);
        assertEquals(expResult, result);
    }

    /**
     * Testa se retorna o tipoLiberação: RETROATIVO
     */
    public void testGetById_RETROATIVO() {
        Integer id = 1;
        TipoLiberacao expResult = TipoLiberacao.RETROATIVO;
        TipoLiberacao result = TipoLiberacao.getById(id);
        assertEquals(expResult, result);
    }

    /**
     * Testa se retorna o tipoLiberação: RETROATIVO
     */
    public void testGetById_LIMITE_50() {
        Integer id = 2;
        TipoLiberacao expResult = TipoLiberacao.LIMITE_50;
        TipoLiberacao result = TipoLiberacao.getById(id);
        assertEquals(expResult, result);
    }
}
