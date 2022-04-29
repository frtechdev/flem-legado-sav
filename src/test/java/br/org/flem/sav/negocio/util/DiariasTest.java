/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package br.org.flem.sav.negocio.util;

import br.org.flem.fw.service.ICargo;
import br.org.flem.fw.service.IFuncionario;
import br.org.flem.fwe.util.Data;
import br.org.flem.sav.bo.ViagemBO;
import br.org.flem.sav.dto.RelatorioDiariaSalarioColaboradoresDTO;
import br.org.flem.sav.negocio.DestinoViagem;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;

/**
 *
 * @author ILFernandes
 */


public class DiariasTest extends TestCase {

    public DiariasTest(String testName) {
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
     * Test of calculaQuantidadeDiaria method, of class Diarias.
     */
    
    
    public void testCalculaQuantidadeDiaria() throws Exception {
        System.out.println("calculaQuantidadeDiaria");
        Date dataSaida = Data.formataData("21/05/2012 10:00", "dd/MM/yyyy HH:mm");
        Date dataRetorno = Data.formataData("23/05/2012 00:01", "dd/MM/yyyy HH:mm");
        Integer matriculaFuncionario = 38;
        Diarias instance = new Diarias();
        Double expResult = 2.0;
        
   
        Double result = instance.calculaQuantidadeDiaria(dataSaida, dataRetorno, matriculaFuncionario);
        assertEquals(expResult, result);
    }
    
}
