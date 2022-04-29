/*     */ package br.org.flem.sav.dwr;
/*     */ 
/*     */ import br.org.flem.fw.persistencia.dao.legado.gem.CentroCustoDAO;
/*     */ import br.org.flem.fw.persistencia.dao.legado.gem.EntidadeDAO;
/*     */ import br.org.flem.fw.persistencia.dto.Cargo;
/*     */ import br.org.flem.fw.persistencia.dto.Usuario;
/*     */ import br.org.flem.fw.service.CentroResponsabilidade;
/*     */ import br.org.flem.fw.service.IColaborador;
/*     */ import br.org.flem.fw.service.IFuncionario;
/*     */ import br.org.flem.fw.service.impl.RHServico;
/*     */ import br.org.flem.fwe.exception.AcessoDadosException;
/*     */ import br.org.flem.fwe.exception.AplicacaoException;
/*     */ import br.org.flem.fwe.util.Data;
/*     */ import br.org.flem.fwe.util.Valores;
/*     */ import br.org.flem.sav.bo.CargoDiariaBO;
/*     */ import br.org.flem.sav.bo.CidadeBO;
/*     */ import br.org.flem.sav.bo.ContaCorrenteViagemBO;
/*     */ import br.org.flem.sav.bo.SolicitarViagemBO;
/*     */ import br.org.flem.sav.bo.UsuarioBO;
/*     */ import br.org.flem.sav.bo.ViagemBO;
/*     */ import br.org.flem.sav.bo.ViagemComAgendamentoBO;
/*     */ import br.org.flem.sav.dao.EstadoDAO;
/*     */ import br.org.flem.sav.dto.RecebimentoViagemDTO;
/*     */ import br.org.flem.sav.negocio.CargoDiaria;
/*     */ import br.org.flem.sav.negocio.ContaCorrenteViagem;
/*     */ import br.org.flem.sav.negocio.DestinoViagem;
/*     */ import br.org.flem.sav.negocio.ItemItinerario;
/*     */ import br.org.flem.sav.negocio.Viagem;
/*     */ import br.org.flem.sav.negocio.ViagemComAgendamento;
/*     */ import br.org.flem.sav.negocio.util.Cidade;
/*     */ import br.org.flem.sav.negocio.util.Diarias;
/*     */ import br.org.flem.sav.negocio.util.Estado;
/*     */ import br.org.flem.sav.negocio.util.Pais;
/*     */ import java.sql.SQLException;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.apache.commons.validator.GenericValidator;
/*     */ import org.directwebremoting.annotations.RemoteMethod;
/*     */ import org.directwebremoting.annotations.RemoteProxy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @RemoteProxy
/*     */ public class Funcoes
/*     */ {
/*     */   @RemoteMethod
/*     */   public Double obterDiaria(String referencia, String destino) {
/*  59 */     Double diaria = Double.valueOf(0.0D);
/*  60 */     CargoDiaria cargoDiaria = null;
/*     */     
/*     */     try {
/*  63 */       if (referencia.equals("Consultor")) {
/*  64 */         cargoDiaria = (new CargoDiariaBO()).obterCargoDiariaConsultor();
/*     */       }
/*  66 */       else if (GenericValidator.isInt(referencia)) {
/*  67 */         IFuncionario iFunc = (new RHServico()).obterFuncionarioPorMatricula(Integer.valueOf(referencia));
/*  68 */         cargoDiaria = (new CargoDiariaBO()).obterPorCodigo(iFunc.getCargo().getId());
/*     */       } 
/*     */       
/*  71 */       if (cargoDiaria != null) {
/*  72 */         if (DestinoViagem.valueOf(destino).equals(DestinoViagem.BAHIA)) {
/*  73 */           diaria = cargoDiaria.getValorBahia();
/*  74 */         } else if (DestinoViagem.valueOf(destino).equals(DestinoViagem.BRASIL)) {
/*  75 */           diaria = cargoDiaria.getValorBrasil();
/*     */         } else {
/*  77 */           diaria = cargoDiaria.getValorExterior();
/*     */         }
/*     */       
/*     */       }
/*     */     }
/*  82 */     catch (AplicacaoException ex) {
/*  83 */       Logger.getLogger(br.org.flem.sav.dwr.Funcoes.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
/*     */     } 
/*     */     
/*  86 */     return diaria;
/*     */   }
/*     */   
/*     */   @RemoteMethod
/*     */   public String obterCargo(String codigoCargo) {
/*  91 */     String dadosCargo = "";
/*     */     
/*  93 */     if (GenericValidator.isLong(codigoCargo)) {
/*  94 */       Cargo cargo = (new RHServico()).obterCargoById(Integer.valueOf(codigoCargo));
/*  95 */       dadosCargo = cargo.getId() + ";" + cargo.getNome();
/*     */     } 
/*     */     
/*  98 */     return dadosCargo;
/*     */   }
/*     */   
/*     */   @RemoteMethod
/*     */   public Boolean verificarDiarias(String matriculaUsuario, String dataSaidaPrevista, String horaSaidaPrevista, String dataRetornoPrevista, String horaRetornoPrevista, String qtdeDiaria, String vlDiaria, String idViagem, String destino, String tipoDiaria, String totalDiariasSomadas) {
/* 103 */     Boolean retorno = Boolean.valueOf(false);
/* 104 */     Diarias diarias = new Diarias();
/* 105 */     DestinoViagem destinoViagem = DestinoViagem.valueOf(destino);
/*     */     
/* 107 */     if (idViagem.equals("")) {
/* 108 */       idViagem = "0";
/*     */     }
/*     */     
/* 111 */     if (GenericValidator.isDate(dataSaidaPrevista, "dd/MM/yyyy", false) && 
/* 112 */       GenericValidator.isInt(idViagem)) {
/*     */       try {
/* 114 */         Date dataSaida = (new SimpleDateFormat("dd/MM/yyyy")).parse(dataSaidaPrevista);
/*     */         
/* 116 */         Double valorDiaria = Valores.desformataValor(vlDiaria);
/*     */         
/* 118 */         double qtdeDiariaViagem = Valores.desformataValor(qtdeDiaria).doubleValue();
/* 119 */         double totalDiariasSomadasDouble = Valores.desformataValor(totalDiariasSomadas).doubleValue();
/*     */         
/* 121 */         if (diarias.verificar(Integer.valueOf(matriculaUsuario), dataSaida, destinoViagem, qtdeDiariaViagem, valorDiaria.doubleValue(), Integer.valueOf(idViagem), tipoDiaria, totalDiariasSomadasDouble).booleanValue()) {
/* 122 */           if (tipoDiaria.equals("2")) {
/* 123 */             retorno = Boolean.valueOf(true);
/*     */           } else {
/* 125 */             dataSaida = (new SimpleDateFormat("dd/MM/yyyy HH:mm")).parse(dataSaidaPrevista + " " + horaSaidaPrevista);
/* 126 */             Date dataRetorno = (new SimpleDateFormat("dd/MM/yyyy HH:mm")).parse(dataRetornoPrevista + " " + horaRetornoPrevista);
/* 127 */             IFuncionario viajante = (new RHServico()).obterFuncionarioPorMatricula(Integer.valueOf(matriculaUsuario));
/*     */             
/* 129 */             retorno = diarias.verificar(viajante, dataSaida, dataRetorno, Double.valueOf(valorDiaria.doubleValue()).doubleValue(), Integer.valueOf(idViagem), null);
/*     */           }
/*     */         
/*     */         }
/*     */       }
/* 134 */       catch (Exception ex) {
/* 135 */         Logger.getLogger(br.org.flem.sav.dwr.Funcoes.class.getName()).log(Level.SEVERE, (String)null, ex);
/*     */       } 
/*     */     }
/* 138 */     return retorno;
/*     */   }
/*     */ 
/*     */   
/*     */   @RemoteMethod
/*     */   public String verificarPeriodo(Integer id_viagem, Integer matriculaV, String codigoV, String dataSaidaPrevista, String horaSaidaPrevista, String dataRetornoPrevista, String horaRetornoPrevista) throws AplicacaoException, ParseException {
/* 144 */     Viagem v = new Viagem();
/* 145 */     SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
/* 146 */     v.setDataSaidaPrevista(simpleDate.parse(dataSaidaPrevista + " " + horaSaidaPrevista));
/* 147 */     v.setDataRetornoPrevista(simpleDate.parse(dataRetornoPrevista + " " + horaRetornoPrevista));
/*     */     
/* 149 */     if (matriculaV.intValue() > 0) {
/* 150 */       v.setCodigoDominioUsuarioViajante(matriculaV);
/*     */     }
/* 152 */     if (!codigoV.equals("0")) {
/* 153 */       v.setCodigoConsultorViajante(codigoV);
/*     */     }
/* 155 */     if (id_viagem.intValue() > 0) {
/* 156 */       v.setId(id_viagem);
/*     */     }
/*     */     
/* 159 */     if ((new ViagemBO()).obterTodosPorPeriodo(v).size() > 0) {
/* 160 */       return "false";
/*     */     }
/* 162 */     return "true";
/*     */   }
/*     */   
/*     */   @RemoteMethod
/*     */   public String obterCidades(String id, String name) {
/* 167 */     StringBuilder sb = new StringBuilder();
/*     */     
/*     */     try {
/* 170 */       Collection<Cidade> cidadeList = new ArrayList<Cidade>();
/*     */       
/* 172 */       switch (Integer.valueOf(id).intValue()) {
/*     */         case 0:
/* 174 */           cidadeList = (new CidadeBO()).obterBahia();
/*     */           break;
/*     */         case 1:
/* 177 */           cidadeList = (new CidadeBO()).obterBrasil();
/*     */           break;
/*     */         case 2:
/* 180 */           cidadeList = (new CidadeBO()).obterExterior();
/*     */           break;
/*     */         default:
/* 183 */           cidadeList = (new CidadeBO()).obterBahia();
/*     */           break;
/*     */       } 
/*     */       
/* 187 */       sb.append("\n<select name='").append(name).append("' id='").append(name).append("' value='' style='width: 210px;' >");
/* 188 */       sb.append("\n<option value=''>-- Selecione --</option>");
/* 189 */       for (Cidade cidade : cidadeList) {
/* 190 */         sb.append("\n<option value='").append(cidade.getId()).append("'>").append(cidade.getNome()).append("</option>");
/*     */       }
/* 192 */       sb.append("\n</select>");
/*     */     }
/* 194 */     catch (AplicacaoException ex) {
/* 195 */       Logger.getLogger(br.org.flem.sav.dwr.Funcoes.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
/*     */     } 
/*     */     
/* 198 */     return sb.toString();
/*     */   }
/*     */   
/*     */   @RemoteMethod
/*     */   public String obterCidadesPorDestino(String nome, String nameTag) {
/* 203 */     StringBuilder sb = new StringBuilder();
/*     */     
/*     */     try {
/* 206 */       Collection<Cidade> cidadeList = new ArrayList<Cidade>();
/*     */       
/* 208 */       cidadeList = (new CidadeBO()).obterPorDestino(nome);
/*     */       
/* 210 */       sb.append("\n<select name='").append(nameTag).append("' id='").append(nameTag).append("' style='width: 210px;' >");
/* 211 */       sb.append("\n<option value=''>-- Selecione --</option>");
/* 212 */       for (Cidade cidade : cidadeList) {
/* 213 */         if (nome.equals(DestinoViagem.EXTERIOR.toString())) {
/* 214 */           if (cidade.getPais() != null && cidade.getPais().getSigla() != null && !cidade.getPais().getSigla().isEmpty()) {
/* 215 */             sb.append("\n<option value='").append(cidade.getId()).append("'>").append(cidade.getNome()).append(" / ").append(cidade.getPais().getSigla()).append("</option>"); continue;
/*     */           } 
/* 217 */           if (cidade.getEstado() != null) {
/* 218 */             sb.append("\n<option value='").append(cidade.getId()).append("'>").append(cidade.getNome()).append(" / ").append(cidade.getEstado().getSigla()).append("</option>"); continue;
/*     */           } 
/* 220 */           sb.append("\n<option value='").append(cidade.getId()).append("'>").append(cidade.getNome()).append("</option>");
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 225 */         if (cidade.getEstado() != null) {
/* 226 */           sb.append("\n<option value='").append(cidade.getId()).append("'>").append(cidade.getNome()).append(" / ").append(cidade.getEstado().getSigla()).append("</option>"); continue;
/*     */         } 
/* 228 */         sb.append("\n<option value='").append(cidade.getId()).append("'>").append(cidade.getNome()).append("</option>");
/*     */       } 
/*     */ 
/*     */       
/* 232 */       sb.append("\n</select>");
/*     */     }
/* 234 */     catch (AplicacaoException ex) {
/* 235 */       Logger.getLogger(br.org.flem.sav.dwr.Funcoes.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
/*     */     } 
/*     */     
/* 238 */     return sb.toString();
/*     */   }
/*     */   
/*     */   @RemoteMethod
/*     */   public String obterDadosFuncionario(String matricula) {
/* 243 */     StringBuilder sb = new StringBuilder();
/*     */     
/*     */     try {
/* 246 */       IFuncionario iFunc = (new RHServico()).obterFuncionarioPorMatricula(Integer.valueOf(matricula));
/* 247 */       ContaCorrenteViagem ccv = (new ContaCorrenteViagemBO()).obterPorMatriculaFuncionario(matricula);
/*     */       
/* 249 */       sb.append("<tr><td width='450' colspan='3'><strong>Departamento:</strong> ").append(iFunc.getDepartamento().getNome()).append("</td></tr>");
/* 250 */       sb.append("<tr><td width='450' colspan='3'><strong>Cargo:</strong> ").append(iFunc.getCargo().getNome()).append("</td></tr>");
/* 251 */       sb.append("<tr>");
/* 252 */       if (ccv != null) {
/* 253 */         sb.append("<td width='150'><strong>Banco:</strong><br><input type='text' name='viagem.banco' value='").append(ccv.getBanco()).append("' style='border-style:none;'/></td>");
/* 254 */         sb.append("<td width='150'><strong>Agência:</strong><br><input type='text' name='viagem.agencia' value='").append(ccv.getAgencia()).append("' style='border-style:none;'/></td>");
/* 255 */         sb.append("<td width='150'><strong>Conta Corrente:</strong><br><input type='text' name='viagem.conta' value='").append(ccv.getConta()).append("' style='border-style:none;'/></td>");
/*     */       } else {
/*     */         
/* 258 */         sb.append("<td width='150'><strong>Banco:</strong><br><input type='text' name='viagem.banco' value='").append(iFunc.getBanco()).append("' style='border-style:none;'/></td>");
/* 259 */         sb.append("<td width='150'><strong>Agência:</strong><br><input type='text' name='viagem.agencia' value='").append(iFunc.getAgencia()).append("' style='border-style:none;'/></td>");
/* 260 */         sb.append("<td width='150'><strong>Conta Corrente:</strong><br><input type='text' name='viagem.conta' value='").append(iFunc.getConta()).append("' style='border-style:none;'/></td>");
/*     */       } 
/* 262 */       sb.append("</tr>");
/*     */     }
/* 264 */     catch (AplicacaoException ex) {
/* 265 */       Logger.getLogger(br.org.flem.sav.dwr.Funcoes.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
/*     */     } 
/*     */     
/* 268 */     return sb.toString();
/*     */   }
/*     */   
/*     */   @RemoteMethod
/*     */   public String obterDadosConsultor(String codigo) {
/* 273 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 275 */     IColaborador consultor = (new EntidadeDAO()).obterConsultorPorCodigo(codigo);
/*     */     
/* 277 */     sb.append("<tr><td><strong>Banco:</strong><input type='text' name='viagem.banco' value=").append(consultor.getBanco()).append(" style='border-style:none;'/></td>");
/* 278 */     sb.append("<td><strong>Agência:</strong><input type='text' name='viagem.agencia' value=").append(consultor.getAgencia()).append(" style='border-style:none;'/></td>");
/* 279 */     sb.append("<td><strong>Conta Corrente:</strong><input type='text' name='viagem.conta' value=").append(consultor.getConta()).append(" style='border-style:none;'/></td>");
/*     */     
/* 281 */     return sb.toString();
/*     */   }
/*     */   
/*     */   @RemoteMethod
/*     */   public String obterIntinerarioViagem(String idViagem) {
/* 286 */     StringBuilder sb = new StringBuilder();
/*     */     
/*     */     try {
/* 289 */       ViagemComAgendamento viagem = (ViagemComAgendamento)(new ViagemComAgendamentoBO()).obterPorPk(Integer.valueOf(Integer.parseInt(idViagem)));
/* 290 */       SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
/*     */       
/* 292 */       for (ItemItinerario it : viagem.getItinerario().getItensItinerario()) {
/* 293 */         sb.append("<tr>");
/* 294 */         sb.append("<td><input size='8' readonly value='").append(sdf.format(it.getData())).append("'</td>");
/* 295 */         sb.append("<td><input readonly value='").append(it.getOrigem().getNome()).append("'</td>");
/* 296 */         sb.append("<td><input readonly value='").append(it.getDestino().getNome()).append("'</td>");
/* 297 */         sb.append("<td><input size='22' readonly value='").append(it.getObservacoes()).append("'</td>");
/*     */       } 
/*     */       
/* 300 */       sb.append("</tr>");
/*     */     }
/* 302 */     catch (AplicacaoException ex) {
/* 303 */       Logger.getLogger(br.org.flem.sav.dwr.Funcoes.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
/*     */     } 
/*     */     
/* 306 */     return sb.toString();
/*     */   }
/*     */   
/*     */   @RemoteMethod
/*     */   public String funcionarioPodeViajar(String matricula) {
/* 311 */     Usuario usuario = (Usuario)(new UsuarioBO()).obterPorCodigoDominio(Integer.valueOf(matricula));
/* 312 */     ArrayList<String> resposta = (new SolicitarViagemBO()).funcionarioPodeViajar(usuario);
/*     */     
/* 314 */     return (String)resposta.get(0) + ";" + (String)resposta.get(1);
/*     */   }
/*     */   
/*     */   @RemoteMethod
/*     */   public Double calculaPorcentagemViagensSalario(String matricula) {
/* 319 */     IFuncionario func = null;
/* 320 */     Double porcentagem = Double.valueOf(0.0D);
/*     */     try {
/* 322 */       RHServico rh = new RHServico();
/* 323 */       func = rh.obterFuncionarioPorMatricula(Integer.valueOf(matricula));
/* 324 */       porcentagem = (new Diarias()).calcularPorcentagem(func);
/* 325 */     } catch (Exception ex) {
/* 326 */       Logger.getLogger(br.org.flem.sav.dwr.Funcoes.class.getName()).log(Level.SEVERE, (String)null, ex);
/*     */     } 
/*     */     
/* 329 */     return porcentagem;
/*     */   }
/*     */   
/*     */   @RemoteMethod
/*     */   public Double obterSalario(String matricula) {
/* 334 */     RHServico rh = new RHServico();
/* 335 */     IFuncionario func = rh.obterFuncionarioPorMatricula(Integer.valueOf(matricula));
/* 336 */     return Double.valueOf(rh.obterSalarioPorFuncionario(func));
/*     */   }
/*     */   
/*     */   @RemoteMethod
/*     */   public Collection<CentroResponsabilidade> obterCentroResponsabilidadePorCc(String cc) {
/* 341 */     Collection<CentroResponsabilidade> lista = new ArrayList<CentroResponsabilidade>();
/* 342 */     CentroCustoDAO custoDAO = new CentroCustoDAO();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 348 */       CentroResponsabilidade centroResponsabilidade = custoDAO.obterObjCentroResponsabilidadePorCc(cc);
/* 349 */       if (centroResponsabilidade != null && centroResponsabilidade.getId() != null) {
/* 350 */         lista.add(centroResponsabilidade);
/*     */       }
/*     */     }
/* 353 */     catch (SQLException ex) {
/* 354 */       Logger.getLogger(br.org.flem.sav.dwr.Funcoes.class.getName()).log(Level.SEVERE, (String)null, ex);
/*     */     } 
/*     */     
/* 357 */     return lista;
/*     */   }
/*     */   
/*     */   @RemoteMethod
/*     */   public Collection<Estado> obterEstadosPorPais(String id) {
/* 362 */     Collection<Estado> estados = new ArrayList<Estado>();
/* 363 */     if (GenericValidator.isInt(id)) {
/* 364 */       Pais pais = new Pais();
/* 365 */       pais.setId(Long.valueOf(id));
/*     */       try {
/* 367 */         estados = (new EstadoDAO()).obterPorPais(pais);
/* 368 */       } catch (AcessoDadosException ex) {
/* 369 */         ex.printStackTrace();
/* 370 */         Logger.getLogger(br.org.flem.sav.dwr.Funcoes.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
/*     */       } 
/*     */     } 
/* 373 */     return estados;
/*     */   }
/*     */   
/*     */   @RemoteMethod
/*     */   public Boolean verificarDiariasTrecho(String idViagem, String matriculaViajante, String[] datasSaida, String[] datasRetorno, String[] valorDiariaTrecho) {
/* 378 */     Diarias diarias = new Diarias();
/* 379 */     Boolean retorno = Boolean.valueOf(true);
/* 380 */     Map<Integer, Double> map = new HashMap<Integer, Double>();
/*     */     
/*     */     try {
/* 383 */       if (datasSaida.length > 0) {
/* 384 */         IFuncionario viajante = (new RHServico()).obterFuncionarioPorMatricula(Integer.valueOf(matriculaViajante));
/* 385 */         SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
/* 386 */         Date dataS = null;
/* 387 */         Date dataR = null;
/* 388 */         int i = 0;
/*     */         
/* 390 */         for (i = 0; i < datasSaida.length - 1; i++) {
/* 391 */           dataS = new Date(format.parse(datasSaida[i]).getTime());
/* 392 */           dataR = new Date(format.parse(datasRetorno[i]).getTime());
/*     */ 
/*     */           
/* 395 */           diarias.separarDiariaPorMesAno(dataS, dataR, Double.valueOf(valorDiariaTrecho[i]), map, viajante.getCodigoDominio());
/*     */         } 
/* 397 */         dataS = new Date(format.parse(datasSaida[i]).getTime());
/* 398 */         dataR = new Date(format.parse(datasRetorno[i]).getTime());
/*     */         
/* 400 */         retorno = diarias.verificar(viajante, dataS, dataR, Double.valueOf(valorDiariaTrecho[i]).doubleValue(), Integer.valueOf(idViagem), map);
/*     */       }
/*     */     
/* 403 */     } catch (ParseException ex) {
/* 404 */       Logger.getLogger(br.org.flem.sav.dwr.Funcoes.class.getName()).log(Level.SEVERE, (String)null, ex);
/* 405 */     } catch (AplicacaoException ex) {
/* 406 */       Logger.getLogger(br.org.flem.sav.dwr.Funcoes.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
/*     */     } 
/* 408 */     return retorno;
/*     */   }
/*     */   
/*     */   @RemoteMethod
/*     */   public Boolean verificarDataAtualIgualOuSuperiorDataRetorno(String data, String hora) {
/* 413 */     boolean retorno = false;
/* 414 */     Date hoje = new Date();
/*     */     try {
/* 416 */       Date dataRetorno = Data.formataData(data + " " + hora, "dd/MM/yyyy HH:mm");
/* 417 */       if (dataRetorno.before(hoje) || dataRetorno.equals(hoje)) {
/* 418 */         retorno = true;
/*     */       }
/* 420 */     } catch (ParseException ex) {
/* 421 */       Logger.getLogger(br.org.flem.sav.dwr.Funcoes.class.getName()).log(Level.SEVERE, (String)null, ex);
/*     */     } 
/* 423 */     return Boolean.valueOf(retorno);
/*     */   }
/*     */   
/*     */   @RemoteMethod
/*     */   public RecebimentoViagemDTO verificarCentroCustoFonteDeRecurso(String[] ids, String codigoFonte) {
/* 428 */     RecebimentoViagemDTO recebimentoViagemDTO = new RecebimentoViagemDTO();
/*     */     try {
/* 430 */       Collection<Integer> idsViagens = new ArrayList<Integer>();
/* 431 */       for (String id : ids) {
/* 432 */         if (GenericValidator.isInt(id)) {
/* 433 */           idsViagens.add(Integer.valueOf(id));
/*     */         }
/*     */       } 
/* 436 */       ViagemBO viagemBO = new ViagemBO();
/* 437 */       Collection<Viagem> viagens = viagemBO.obterViagensPorIds(idsViagens);
/*     */       
/* 439 */       StringBuilder mensagem = new StringBuilder();
/* 440 */       mensagem.append("A fonte de recurso ").append(codigoFonte).append(" não pode pagar as viagens: \n");
/* 441 */       for (Viagem viagem : viagens) {
/* 442 */         if (!viagemBO.verificarCentroCustoFonteDeRecurso(viagem, codigoFonte)) {
/* 443 */           recebimentoViagemDTO.setValido(Boolean.valueOf(false));
/* 444 */           mensagem.append(viagem.getId()).append(" com centro de custo ").append(viagem.getCodigoCentroCusto()).append("\n");
/*     */         } 
/*     */       } 
/* 447 */       recebimentoViagemDTO.setMensagens(mensagem.toString());
/*     */     }
/* 449 */     catch (AplicacaoException ex) {
/* 450 */       Logger.getLogger(br.org.flem.sav.dwr.Funcoes.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
/*     */     } 
/* 452 */     return recebimentoViagemDTO;
/*     */   }
/*     */ }


/* Location:              E:\Backup SAV 09.08.2021\ROOT.war!\WEB-INF\classes\br\org\flem\sav\dwr\Funcoes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */