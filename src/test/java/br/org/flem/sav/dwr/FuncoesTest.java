package br.org.flem.sav.dwr;

import br.org.flem.fwe.exception.AplicacaoException;
import java.text.ParseException;
import junit.framework.TestCase;

/**
 *
 * @author mccosta
 */
public class FuncoesTest extends TestCase {

    public FuncoesTest(String testName) {
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
     * Test of verificarDiarias method, of class Funcoes.
     */
    /* public void testVerificarDiarias() {
        System.out.println("testVerificarDiarias");
        String matriculaUsuario = "2";
        String dataSaidaPrevista = "30/06/2011";
        String horaSaidaPrevista = "19:43";
        String dataRetornoPrevista = "01/07/2011";
        String horaRetornoPrevista = "16:40";
        String qtdeDiaria = "6";
        String vlDiaria = "220";
        String idViagem = "12214";
        String destino = "BRASIL";
        String tipoDiaria = "1";
        String totalDiariasSomadas = "0";//caso o tipo de diária seja '2' será considerado o valor total de diárias dessa variável
        Funcoes instance = new Funcoes();
        Boolean expResult = true;//Em 05/2011 ela ultrapassa os 50%
        Boolean result = instance.verificarDiarias(matriculaUsuario, dataSaidaPrevista, horaSaidaPrevista, dataRetornoPrevista, horaRetornoPrevista, qtdeDiaria, vlDiaria, idViagem, destino, tipoDiaria, totalDiariasSomadas);
        assertEquals(expResult, result);
    }

    public void testVerificarDiariasMat995NinaRosa() {
        System.out.println("testVerificarDiariasMat995NinaRosa");
        String matriculaUsuario = "995";
        String dataSaidaPrevista = "24/05/2011";
        String horaSaidaPrevista = "07:00";
        String dataRetornoPrevista = "26/05/2011";
        String horaRetornoPrevista = "02:00";
        String qtdeDiaria = "2,0";
        String vlDiaria = "74";
        String idViagem = "12230";
        String destino = "BAHIA";
        String tipoDiaria = "0";
        String totalDiariasSomadas = "0";//caso o tipo de diária seja '2' será considerado o valor total de diárias dessa variável
        Funcoes instance = new Funcoes();
        Boolean expResult = false;//Em 05/2011 ela ultrapassa os 50%
        Boolean result = instance.verificarDiarias(matriculaUsuario, dataSaidaPrevista, horaSaidaPrevista, dataRetornoPrevista, horaRetornoPrevista, qtdeDiaria, vlDiaria, idViagem, destino, tipoDiaria, totalDiariasSomadas);
        assertEquals(expResult, result);
    }

    public void testVerificarDiariasMat923IsabelSalviano() {
        System.out.println("testVerificarDiariasMat923IsabelSalviano");
        String matriculaUsuario = "923";
        String dataSaidaPrevista = "26/08/2011";
        String horaSaidaPrevista = "05:00";
        String dataRetornoPrevista = "31/08/2011";
        String horaRetornoPrevista = "18:00";
        String qtdeDiaria = "2,0";
        String vlDiaria = "74";
        String idViagem = "12512";
        String destino = "BAHIA";
        String tipoDiaria = "0";
        String totalDiariasSomadas = "0";//caso o tipo de diária seja '2' será considerado o valor total de diárias dessa variável
        Funcoes instance = new Funcoes();
        Boolean expResult = true;//Em 05/2011 ela ultrapassa os 50%
        Boolean result = instance.verificarDiarias(matriculaUsuario, dataSaidaPrevista, horaSaidaPrevista, dataRetornoPrevista, horaRetornoPrevista, qtdeDiaria, vlDiaria, idViagem, destino, tipoDiaria, totalDiariasSomadas);
        assertEquals(expResult, result);
    }

   public void testVerificarDiariasMat906AngelaMaria() {
        System.out.println("testVerificarDiariasMat906AngelaMaria");
        String matriculaUsuario = "906";
        String dataSaidaPrevista = "12/12/2011";
        String horaSaidaPrevista = "07:00";
        String dataRetornoPrevista = "13/12/2011";
        String horaRetornoPrevista = "05:30";
        String qtdeDiaria = "5,4";
        String vlDiaria = "83";
        String idViagem = "12888";//12888
        String destino = "BAHIA";
        String tipoDiaria = "2";
        String totalDiariasSomadas = "116,2";//caso o tipo de diária seja '2' será considerado o valor total de diárias dessa variável
        Funcoes instance = new Funcoes();//448,2
        Boolean expResult = true;
        Boolean result = instance.verificarDiarias(matriculaUsuario, dataSaidaPrevista, horaSaidaPrevista, dataRetornoPrevista, horaRetornoPrevista, qtdeDiaria, vlDiaria, idViagem, destino, tipoDiaria, totalDiariasSomadas);
        assertEquals(expResult, result);
    }*/

    public void testVerificarPeriodo() throws AplicacaoException, ParseException {
        System.out.println("testVerificarPeriodo");
        Integer id_viagem = 0;
        Integer matriculaV = 911;
        String codigoV = "0";
        String dataSaidaPrevista = "19/06/2011";
        String horaSaidaPrevista = "07:00";
        String dataRetornoPrevista = "20/06/2011";
        String horaRetornoPrevista = "02:00";
        Funcoes instance = new Funcoes();
        String expResult = "true";
        String result = instance.verificarPeriodo(id_viagem, matriculaV, codigoV, dataSaidaPrevista, horaSaidaPrevista, dataRetornoPrevista, horaRetornoPrevista);
        assertEquals(expResult, result);
    }

    /*public void testVerificarDiariasMat10192TaisSantos() {
        System.out.println("testVerificarDiariasMat10192TaisSantos");
        String matriculaUsuario = "10192";
        String dataSaidaPrevista = "03/09/2013";
        String horaSaidaPrevista = "07:00";
        String dataRetornoPrevista = "20/09/2013";
        String horaRetornoPrevista = "19:00";
        String qtdeDiaria = "6";
        String vlDiaria = "140";
        String idViagem = "";
        String destino = "BAHIA";
        String tipoDiaria = "2";
        String totalDiariasSomadas = "1285";//caso o tipo de diária seja '2' será considerado o valor total de diárias dessa variável
        Funcoes instance = new Funcoes();
        Boolean expResult = true;//Em 09/2012 ela ultrapassa os 50%
        Boolean result = instance.verificarDiarias(matriculaUsuario, dataSaidaPrevista, horaSaidaPrevista, dataRetornoPrevista, horaRetornoPrevista, qtdeDiaria, vlDiaria, idViagem, destino, tipoDiaria, totalDiariasSomadas);
        assertEquals(expResult, result);
    }
    
    public void testVerificarDiariasMat10188Edleide() {
        System.out.println("testVerificarDiariasMat10188Edleide");
        
        
        //As viagens de ID (13768,13776) foram pagas dias 1 e 5 de Fev respectivamente. 
        //Suas diárias somadas: R$ 998,16
        //Está viagem, com diária no valor de R$ 759,00, somadas com as outras ultrapassam os 50% permitidos. Result deve ser FALSE.
        //Precisou de liberação de 50%
        
        String matriculaUsuario = "10188";
        String dataSaidaPrevista = "24/02/2013";
        String horaSaidaPrevista = "13:00";
        String dataRetornoPrevista = "01/03/2013";
        String horaRetornoPrevista = "20:30";
        String qtdeDiaria = "5.4";//5.4
        String vlDiaria = "140";
        String idViagem = "";//13776
        String destino = "BAHIA";
        String tipoDiaria = "2";
        String totalDiariasSomadas = "759";//caso o tipo de diária seja '2' será considerado o valor total de diárias dessa variável
        Funcoes instance = new Funcoes();
        Boolean expResult = false;//Em 09/2012 ela ultrapassa os 50%
        Boolean result = instance.verificarDiarias(matriculaUsuario, dataSaidaPrevista, horaSaidaPrevista, dataRetornoPrevista, horaRetornoPrevista, qtdeDiaria, vlDiaria, idViagem, destino, tipoDiaria, totalDiariasSomadas);
        assertEquals(expResult, result);
    }*/

    /*public void testVerificarPeriodoREGINALDO() throws AplicacaoException, ParseException {
        System.out.println("testVerificarPeriodo");
        Integer id_viagem = 0;
        Integer matriculaV = 1069;
        String codigoV = "0";
        String dataSaidaPrevista = "30/10/2012";
        String horaSaidaPrevista = "05:00";
        String dataRetornoPrevista = "30/10/2012";
        String horaRetornoPrevista = "22:00";
        Funcoes instance = new Funcoes();
        String expResult = "false";
        String result = instance.verificarPeriodo(id_viagem, matriculaV, codigoV, dataSaidaPrevista, horaSaidaPrevista, dataRetornoPrevista, horaRetornoPrevista);
        assertEquals(expResult, result);
    }*/
}
