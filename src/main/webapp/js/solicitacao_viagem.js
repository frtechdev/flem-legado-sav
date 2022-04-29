var contLinhas = 0; 
var contLinhasTrecho = 0;
var liberacaoRetroativo = false;
var liberacaoL50 = false;
var cidadeOrigemInicial = "";
var contCidadeOrigemInicial = 0;

var quebraDiariasUm = 0.4;
var minQuebraDiariasUm = 6;
var maxQuebraDiariasUm = 12;

var quebraDiariasDois = 0.6;
var minQuebraDiariasDois = 12;
var maxQuebraDiariasDois = 24;

//SETANDO O PERCENTUAL DE DIÁRIA DO VIAJANTE
function setPercentualDiaria(){
    minQuebraDiariasUm = parseFloat($("minQuebraUm").value);
    maxQuebraDiariasUm = parseFloat($("maxQuebraUm").value);
    quebraDiariasUm = parseFloat($("qDUm").value);
    minQuebraDiariasDois = parseFloat($("minQuebraDois").value);
    maxQuebraDiariasDois = parseFloat($("maxQuebraDois").value);
    quebraDiariasDois = parseFloat($("qDDois").value);
}

function ativaInativaReserva(){
    if($("temReserva").value == 0){
        $("companhiaReserva").value = "";
        $("codigoReserva").value = "";
        $("companhiaReserva").disabled = true;
        $("codigoReserva").disabled = true;
    }else{
        $("companhiaReserva").disabled = false;
        $("codigoReserva").disabled = false;
    }
}

function validarReserva(){
    if($("temReserva").value != 0){
        /*if($("companhiaReserva").value == ""){
            alert("Para adicionar uma Reserva é necessário informar o Código e a Companhia");
            return false;
        }
        if($("codigoReserva").value == ""){
            alert("Para adicionar uma Reserva é necessário informar o Código e a Companhia");
            return false;
        }*/
    }

    return true;
}

function alterarTipoDiaria(){
    if($("tipoDiariaId").value == "1"){
        $("diariaE").disabled = false;
        $("descricaoE").disabled = false;
        $("tituloFCM").style.color = "#FF0000";
        $("tituloDesc").style.color = "#FF0000";
        $("tipoDiariaE").style.color = "#FF0000";

        setDiaria('0,00');
        calcularQtdDiariasSemValidar();
        $("qtDiaria").value = $("qtDiariaAux").value;

        $("tipoDet").style.display = "none";
        $("valorDiariaLabel1").style.display = "block";
        $("valorDiariaLabel2").style.display = "none";
    }else{
        if($("tipoDiariaId").value == "0"){
            $("diariaE").disabled = true;
            $("descricaoE").disabled = true;
            $("tituloFCM").style.color = "#DDDDDD";
            $("tituloDesc").style.color = "#DDDDDD";
            $("tipoDiariaE").style.color = "#DDDDDD";

            calcularQtdDiariasSemValidar();
            $("qtDiaria").value = $("qtDiariaAux").value;

            setDiaria(moeda2float($("valorDiariaP").value));

            $("descricaoE").value = "";
            $("diariaE").value = "0,00";

            $("tipoDet").style.display = "none";
            $("valorDiariaLabel1").style.display = "block";
            $("valorDiariaLabel2").style.display = "none";
        }else{
            $("diariaE").disabled = true;
            $("descricaoE").disabled = true;
            $("tituloFCM").style.color = "#DDDDDD";
            $("tituloDesc").style.color = "#DDDDDD";
            $("tipoDiariaE").style.color = "#DDDDDD";

            setDiaria(moeda2float($("valorDiariaP").value));
            $("totalDiarias").value = "0,00";
            $("totalAdiantamento").value = "0,00";
            $("qtDiaria").value = "0,00";

            $("descricaoE").value = "";
            $("diariaE").value = "0,00";

            $("tipoDet").style.display = "block";
            $("valorDiariaLabel1").style.display = "none";
            $("valorDiariaLabel2").style.display = "block";

            $("valorDiariaTrecho").value = $("valorDiariaP").value;

            setTotalDiariasSomadasTrecho();
        }
    }
}

function alterarTipoDiariaAgendamento(){
    if($("tipoDiariaId").value == "0"){

        calcularQtdDiariasSemValidar();
        $("qtDiaria").value = $("qtDiariaAux").value;

        setDiaria(moeda2float($("valorDiariaP").value));

        $("tipoDet").style.display = "none";
        $("valorDiariaLabel1").style.display = "block";
        $("valorDiariaLabel2").style.display = "none";
    }else{
        if($("tipoDiariaId").value == "2"){

            setDiaria(moeda2float($("valorDiariaP").value));
            $("totalDiarias").value = "0,00";
            $("totalAdiantamento").value = "0,00";
            $("qtDiaria").value = "0,00";

            $("tipoDet").style.display = "block";
            $("valorDiariaLabel1").style.display = "none";
            $("valorDiariaLabel2").style.display = "block";

            $("valorDiariaTrecho").value = $("valorDiariaP").value;

            setTotalDiariasSomadasTrecho();
        }
    }
}

function alterarTipoDiariaAgendamentoInicial(){
    calcularQtdDiariasLimite();

    if($("tipoDiariaId").value == "0"){
        $("tipoDet").style.display = "none";
        $("valorDiariaLabel1").style.display = "block";
        $("valorDiariaLabel2").style.display = "none";
    }else{
        if($("tipoDiariaId").value == "2"){
            $("tipoDet").style.display = "block";
            $("valorDiariaLabel1").style.display = "none";
            $("valorDiariaLabel2").style.display = "block";
        }
    }
}

function alterarTipoDiariaInicial(){
    if($("tipoDiariaId").value == "1"){
        $("diariaE").disabled = false;
        $("descricaoE").disabled = false;
        $("tituloFCM").style.color = "#FF0000";
        $("tituloDesc").style.color = "#FF0000";
        $("tipoDiariaE").style.color = "#FF0000";

        $("tipoDet").style.display = "none";
        $("valorDiariaLabel1").style.display = "block";
        $("valorDiariaLabel2").style.display = "none";
    }else{
        if($("tipoDiariaId").value == "0"){
            $("diariaE").disabled = true;
            $("descricaoE").disabled = true;
            $("tituloFCM").style.color = "#DDDDDD";
            $("tituloDesc").style.color = "#DDDDDD";
            $("tipoDiariaE").style.color = "#DDDDDD";

            $("descricaoE").value = "";
            $("diariaE").value = "0,00";

            $("tipoDet").style.display = "none";
            $("valorDiariaLabel1").style.display = "block";
            $("valorDiariaLabel2").style.display = "none";
        }else{
            $("diariaE").disabled = true;
            $("descricaoE").disabled = true;
            $("tituloFCM").style.color = "#DDDDDD";
            $("tituloDesc").style.color = "#DDDDDD";
            $("tipoDiariaE").style.color = "#DDDDDD";

            $("descricaoE").value = "";
            $("diariaE").value = "0,00";

            $("tipoDet").style.display = "block";
            $("valorDiariaLabel1").style.display = "none";
            $("valorDiariaLabel2").style.display = "block";
        }
    }
}

function verificarDiariasTrecho(datasS, datasR, valorDiariaTrecho){
    var form = document.forms[0];
    var i = 0;

    if(form.dataI){
        for(i; i < form.dataI.length; i++){
            datasS[i] = form.dataI[i].value + " " + form.horaI[i].value;
            datasR[i] = form.dataF[i].value + " " + form.horaF[i].value;
            valorDiariaTrecho[i] = form.valorDiariaT[i].value.replace(",", ".");
        }
    }
    datasS[i] = form.dataInicio.value + " " + form.horaInicio.value;
    datasR[i] = form.dataFim.value + " " + form.horaFim.value;
    valorDiariaTrecho[i] = form.valorDiariaTrecho.value.replace(",", ".");
}

function adicionarLinhaTrecho(){

    var dataInicio = $("dataInicio").value;
    var horaInicio = $("horaInicio").value;
    var dataFim = $("dataFim").value;
    var horaFim = $("horaFim").value;
    var qtDiariaTrecho = $("qtDiariaTrecho").value;
    var valorDiariaTrecho = $("valorDiariaTrecho").value;
    var totalDiariasTrecho = $("totalDiariasTrecho").value;
    var descricaoTrecho = $("descricaoTrecho").value;

    if($("dataSaidaPrevista").value.length < 9 || $("dataRetornoPrevista").value.length < 9 ||
       $("horaSaidaPrevista").value.length < 4 || $("horaRetornoPrevista").value.length < 4){
        alert("Informe a Programação da Viagem.");
        $("dataSaidaPrevista").focus();
        return;
    }
    if(dataInicio.length < 9 || dataFim.length < 9 ||
       horaInicio.length < 4 || horaFim.length < 4){
        alert("Informe o período.");
        $("dataInicio").focus();
        return;
    }

    var dataItinerarioI = Date.parseDateUtil(dataInicio + " 00:00:00","d/m/Y H:i:s");
    var dataItinerarioF = Date.parseDateUtil(dataFim + " 00:00:00","d/m/Y H:i:s");

    if (contLinhasTrecho == 0){
        var dataSaidaPrevista = Date.parseDateUtil($("dataSaidaPrevista").value + " 00:00:00","d/m/Y H:i:s");

        if (dateDiffInDays(dataItinerarioI, dataSaidaPrevista) != 0){
            alert('A primeira data deve ser igual à data de saída prevista.');
            return;
        }
    }
    else{
        var dataRetornoPrevista = Date.parseDateUtil($("dataRetornoPrevista").value + " 00:00:00","d/m/Y H:i:s");
        var dataF = document.getElementsByName("dataF[]");
        var horaF = document.getElementsByName("horaF[]");

        var dataAnteriorF = Date.parseDateUtil(dataF[contLinhasTrecho-1].value + " " + horaF[contLinhasTrecho-1].value + ":00","d/m/Y H:i:s");
        var dataAtualI = Date.parseDateUtil(dataInicio + " " + horaInicio + ":00","d/m/Y H:i:s");

        if (dataAnteriorF > dataAtualI){
            alert('A data inicio deve ser maior que a data fim anterior.');
            $("dataInicio").value = "";
            $("horaInicio").value = "";
            $("dataInicio").focus();
            return;
        }

        if (dataRetornoPrevista < dataItinerarioI || dataRetornoPrevista < dataItinerarioF){
            alert('A data deve ser menor ou igual à data de retorno prevista.');
            return;
        }
    }

    nomeTabela = 'trecho';
    solicitacaoOuPrestacao = "s";
    var tabela = listTables.getTableById(nomeTabela);

    if(tabela == null){
        tabela = new Table(nomeTabela);
    }

    //auxiliar para criar identificadores unicos
    contLinhasTrecho++;

    var linha = new Line(contLinhasTrecho, 'linha', tabela);

    new Column('dataI[]', dataInicio, 8, "", linha, "");
    new Column('horaI[]', horaInicio, 5, "", linha, "");
    new Column('dataF[]', dataFim, 8, "", linha, "");
    new Column('horaF[]', horaFim, 5, "", linha, "");
    new Column('qtDiariaT[]', qtDiariaTrecho, 6, "right", linha, "");
    new Column('valorDiariaT[]', valorDiariaTrecho, 6, "right", linha, "");
    new Column('totalDiariaT[]', totalDiariasTrecho, 6, "right", linha, "");
    new Column('descT[]', descricaoTrecho, 26, "", linha, "");

    //constrói a tabela adicionando a linha
    renderTable(tabela);

    setTotalDiariasSomadasTrecho();

    $("dataInicio").value = "";
    $("horaInicio").value = "";
    $("dataFim").value = "";
    $("horaFim").value = "";
    $("qtDiariaTrecho").value = "0,00";
    //$("valorDiariaTrecho").value = "0,00";
    $("totalDiariasTrecho").value = "0,00";
    $("descricaoTrecho").value = "";

}

function adicionarLinhaTrechoInicial(){

    var dataInicio = $("dataInicio").value;
    var horaInicio = $("horaInicio").value;
    var dataFim = $("dataFim").value;
    var horaFim = $("horaFim").value;
    var qtDiariaTrecho = $("qtDiariaTrecho").value;
    var valorDiariaTrecho = $("valorDiariaTrecho").value;
    var totalDiariasTrecho = $("totalDiariasTrecho").value;
    var descricaoTrecho = $("descricaoTrecho").value;

    if($("dataSaidaPrevista").value.length < 9 || $("dataRetornoPrevista").value.length < 9 ||
       $("horaSaidaPrevista").value.length < 4 || $("horaRetornoPrevista").value.length < 4){
        alert("Informe a Programação da Viagem.");
        $("dataSaidaPrevista").focus();
        return;
    }
    if(dataInicio.length < 9 || dataFim.length < 9 ||
       horaInicio.length < 4 || horaFim.length < 4){
        alert("Informe o período.");
        $("dataInicio").focus();
        return;
    }

    var dataItinerarioI = Date.parseDateUtil(dataInicio + " 00:00:00","d/m/Y H:i:s");
    var dataItinerarioF = Date.parseDateUtil(dataFim + " 00:00:00","d/m/Y H:i:s");

    if (contLinhasTrecho == 0){
        var dataSaidaPrevista = Date.parseDateUtil($("dataSaidaPrevista").value + " 00:00:00","d/m/Y H:i:s");

        if (dateDiffInDays(dataItinerarioI, dataSaidaPrevista) != 0){
            alert('A primeira data deve ser igual à data de saída prevista.');
            return;
        }
    }
    else{
        var dataRetornoPrevista = Date.parseDateUtil($("dataRetornoPrevista").value + " 00:00:00","d/m/Y H:i:s");
        var dataF = document.getElementsByName("dataF[]");
        var horaF = document.getElementsByName("horaF[]");

        var dataAnteriorF = Date.parseDateUtil(dataF[contLinhasTrecho-1].value + " " + horaF[contLinhasTrecho-1].value + ":00","d/m/Y H:i:s");
        var dataAtualI = Date.parseDateUtil(dataInicio + " " + horaInicio + ":00","d/m/Y H:i:s");

        if (dataAnteriorF > dataAtualI){
            alert('A data inicio deve ser maior que a data fim anterior.');
            $("dataInicio").value = "";
            $("horaInicio").value = "";
            $("dataInicio").focus();
            return;
        }

        if (dataRetornoPrevista < dataItinerarioI || dataRetornoPrevista < dataItinerarioF){
            alert('A data deve ser menor ou igual à data de retorno prevista.');
            return;
        }
    }

    nomeTabela = 'trecho';
    solicitacaoOuPrestacao = "s";
    var tabela = listTables.getTableById(nomeTabela);

    if(tabela == null){
        tabela = new Table(nomeTabela);
    }

    //auxiliar para criar identificadores unicos
    contLinhasTrecho++;

    var linha = new Line(contLinhasTrecho, 'linha', tabela);

    new Column('dataI[]', dataInicio, 8, "", linha, "");
    new Column('horaI[]', horaInicio, 5, "", linha, "");
    new Column('dataF[]', dataFim, 8, "", linha, "");
    new Column('horaF[]', horaFim, 5, "", linha, "");
    new Column('qtDiariaT[]', qtDiariaTrecho, 6, "right", linha, "");
    new Column('valorDiariaT[]', valorDiariaTrecho, 6, "right", linha, "");
    new Column('totalDiariaT[]', totalDiariasTrecho, 6, "right", linha, "");
    new Column('descT[]', descricaoTrecho, 26, "", linha, "");

    //constrói a tabela adicionando a linha
    renderTable(tabela);

    $("dataInicio").value = "";
    $("horaInicio").value = "";
    $("dataFim").value = "";
    $("horaFim").value = "";
    $("qtDiariaTrecho").value = "0,00";
    //$("valorDiariaTrecho").value = "0,00";
    $("totalDiariasTrecho").value = "0,00";
    $("descricaoTrecho").value = "";
}

function atualizarTipoMoeda(){
    if(DWRUtil.getValue("destino") != "EXTERIOR"){
        $("tipoDiariaE").innerHTML = "(R$)";
        $("tipoValorDiaria").innerHTML = "(R$)";
        $("tipoTotalDiarias").innerHTML = "(R$)";
        $("tipoValorAdiantamento").innerHTML = "(R$)";
        $("tipoTotalAdiantamento").innerHTML = "(R$)";
    } else{
        $("tipoDiariaE").innerHTML = "(U$)";
        $("tipoValorDiaria").innerHTML = "(U$)";
        $("tipoTotalDiarias").innerHTML = "(U$)";
        $("tipoValorAdiantamento").innerHTML = "(U$)";
        $("tipoTotalAdiantamento").innerHTML = "(U$)";
    }
}

function atualizarTipoMoedaAgendamento(){
    if(DWRUtil.getValue("destino") != "EXTERIOR"){
        $("tipoValorDiaria").innerHTML = "(R$)";
        $("tipoTotalDiarias").innerHTML = "(R$)";
        $("tipoValorAdiantamento").innerHTML = "(R$)";
        $("tipoTotalAdiantamento").innerHTML = "(R$)";
    } else{
        $("tipoValorDiaria").innerHTML = "(U$)";
        $("tipoTotalDiarias").innerHTML = "(U$)";
        $("tipoValorAdiantamento").innerHTML = "(U$)";
        $("tipoTotalAdiantamento").innerHTML = "(U$)";
    }
}

function setTotalAdiantamento(){
    var totalDiaria = moeda2float($("totalDiarias").value);
    var adiantamento = moeda2float($("valorAdiantamento").value);

    $("totalAdiantamento").value = float2moeda(totalDiaria+adiantamento);
}

function setTotalDiarias(){
    var qtDiaria = moeda2float($("qtDiaria").value);
    var valorDiaria = moeda2float($("valorDiaria").value);

    $("totalDiarias").value = float2moeda(valorDiaria*qtDiaria);
}

function setTotalDiariasTrecho(){
    var qtDiariaTrecho = moeda2float($("qtDiariaTrecho").value);
    var valorDiariaTrecho = moeda2float($("valorDiariaTrecho").value);

    $("totalDiariasTrecho").value = float2moeda(valorDiariaTrecho*qtDiariaTrecho);
}

function setTotalDiariasSomadasTrecho(){
    var ddt = document.getElementsByName("totalDiariaT[]");
    var qddt = document.getElementsByName("qtDiariaT[]");
    var total_ddt = 0;
    var total_qddt = 0;

    for(i=0;i<ddt.length;i++){
        total_ddt += moeda2float(ddt.item(i).value);
        total_qddt += moeda2float(qddt.item(i).value);
    }
    $("totalDiarias").value = float2moeda(total_ddt);
    $("qtDiaria").value = float2moeda(total_qddt);

    setTotalAdiantamento();
}

/*function setTotalDiariasSomadasTrechoInicial(){
    var qddt = document.getElementsByName("qtDiariaT[]");
    var total_qddt = 0;

    for(i=0;i<qddt.length;i++){
        total_qddt += moeda2float(qddt.item(i).value);
    }
    $("qtDiaria").value = float2moeda(total_qddt);
}*/

function setAdiantamento(){

     if($("valorAdiantamento").value == ""){
        $("valorAdiantamento").value=float2moeda(0.00);
    }
    else{
        $("valorAdiantamento").value= ($("valorAdiantamento").value).replace(".", ",");
    }

}

function setDiaria(diaria){
    $("valorDiaria").value = float2moeda(diaria);

    setTotalDiarias();
    setTotalAdiantamento();
}

function setDiariaPadrao(diaria){
    $("valorDiariaP").value = float2moeda(diaria);
    setDiaria(diaria);
}

function setarDataItinerario(){
    $('dataLinhaItinerario').value = $('dataSaidaPrevista').value;
}

function calcularQtdDiarias(){

    var qtdDiariasT = 0;
    var qtdHoras = 0;

    var dataSaidaPrevista = $("dataSaidaPrevista");
    var dataRetornoPrevista = $("dataRetornoPrevista");
    var horaSaidaPrevista = $("horaSaidaPrevista");
    var horaRetornoPrevista = $("horaRetornoPrevista");

    if (contLinhas > 0){
        alert('Ao efetuar alterações na programação da viagem deve-se refazer o itinerário.');
        for (var i=contLinhas; i>=1; i--){
            remove(i,'itinerario');
        }
        contLinhas = 0;
    }

    if (contLinhasTrecho > 0){
        if($("tipoDiariaId").value == "2"){
            alert('Ao efetuar alterações na programação da viagem deve-se refazer o detalhamento da diária.');
        }
        for (var i=contLinhasTrecho; i>=1; i--){
            remove(i,'trecho');
        }
        contLinhasTrecho = 0;
    }

    if(dataSaidaPrevista != null && dataSaidaPrevista.value != "" && dataRetornoPrevista != null && dataRetornoPrevista.value != ""
        && horaSaidaPrevista != null && horaSaidaPrevista.value != "" && horaRetornoPrevista != null && horaRetornoPrevista.value != ""){

        var dataSaida = Date.parseDateUtil(dataSaidaPrevista.value + " " + horaSaidaPrevista.value + ":00","d/m/Y H:i:s");
        var dataRetorno = Date.parseDateUtil(dataRetornoPrevista.value + " " + horaRetornoPrevista.value + ":00", "d/m/Y H:i:s");
        var hSaida = Date.parseDateUtil("01/01/2000 " + horaSaidaPrevista.value + ":00","d/m/Y H:i:s");
        var hRetorno = Date.parseDateUtil("01/01/2000 " + horaRetornoPrevista.value + ":00", "d/m/Y H:i:s");
        var hiAux = horaSaidaPrevista.value.split( ":" )[0].toString() + horaSaidaPrevista.value.split( ":" )[1].toString();
        if(hiAux.charAt(0) == '0'){hiAux = hiAux.substr(1, 3);}
        var hi = parseInt(hiAux);
        var hfAux = horaRetornoPrevista.value.split( ":" )[0].toString() + horaRetornoPrevista.value.split( ":" )[1].toString();
        if(hfAux.charAt(0) == '0'){hfAux = hfAux.substr(1, 3);}
        var hf = parseInt(hfAux);
        var complemento = 0;

        if(liberacaoRetroativo == true){
            qtdDiariasT = dateDiffInDays(dataRetorno, dataSaida);
            qtdHoras = parseFloat(dateDiffInHoursWithMinutes(hRetorno, hSaida));
            if(hf < hi){
                qtdHoras = 24-qtdHoras;
            }
            
            if(qtdHoras < minQuebraDiariasUm){
                complemento = 1;
            }
            else if(qtdHoras >= minQuebraDiariasUm && qtdHoras <= maxQuebraDiariasUm){
                qtdDiariasT = qtdDiariasT + quebraDiariasUm;
                complemento = quebraDiariasDois;
            }
            else if(qtdHoras > minQuebraDiariasDois && qtdHoras < maxQuebraDiariasDois){
                qtdDiariasT = qtdDiariasT + quebraDiariasDois;
                complemento = quebraDiariasUm;
            }

            //testa a condição do pernoite
            if(dateHasSameDay(dataSaidaPrevista.value, dataRetornoPrevista.value) == false){
                if(dateWhoMostHours(horaRetornoPrevista.value, horaSaidaPrevista.value) && dateWhoMostHours("00:00:00",horaRetornoPrevista.value)){
                    qtdDiariasT = qtdDiariasT + complemento;
                }
            }
        }else{

            if (validarDatas(dataSaida, dataRetorno)){
                qtdDiariasT = dateDiffInDays(dataRetorno, dataSaida);
                qtdHoras = parseFloat(dateDiffInHoursWithMinutes(hRetorno, hSaida));
                if(hf < hi){
                    qtdHoras = 24-qtdHoras;
                }

                if(qtdHoras < minQuebraDiariasUm){
                    complemento = 1;
                }
                else if(qtdHoras >= minQuebraDiariasUm && qtdHoras <= maxQuebraDiariasUm){
                    qtdDiariasT = qtdDiariasT + quebraDiariasUm;
                    complemento = quebraDiariasDois;
                }
                else if(qtdHoras > minQuebraDiariasDois && qtdHoras < maxQuebraDiariasDois){
                    qtdDiariasT = qtdDiariasT + quebraDiariasDois;
                    complemento = quebraDiariasUm;
                }

                //testa a condição do pernoite
                if(dateHasSameDay(dataSaidaPrevista.value, dataRetornoPrevista.value) == false){
                    if(dateWhoMostHours(horaRetornoPrevista.value, horaSaidaPrevista.value) && dateWhoMostHours("00:00:00",horaRetornoPrevista.value)){
                        qtdDiariasT = qtdDiariasT + complemento;
                    }
                }
            }
        }



        $("qtDiaria").value = float2moeda(qtdDiariasT);
        $("qtDiariaAux").value = $("qtDiaria").value;

        setTotalDiarias();
        setTotalAdiantamento();
    }
}

function calcularQtdDiariasAgendamento(){

    var qtdDiariasT = 0;
    var qtdHoras = 0;

    var dataSaidaPrevista = $("dataSaidaPrevista");
    var dataRetornoPrevista = $("dataRetornoPrevista");
    var horaSaidaPrevista = $("horaSaidaPrevista");
    var horaRetornoPrevista = $("horaRetornoPrevista");

    if (contLinhas > 0){
        alert('Ao efetuar alterações na programação da viagem deve-se refazer o itinerário.');
        for (var i=contLinhas; i>=1; i--){
            remove(i,'itinerario');
        }
        contLinhas = 0;
    }

    if (contLinhasTrecho > 0){
        if($("tipoDiariaId").value == "2"){
            alert('Ao efetuar alterações na programação da viagem deve-se refazer o detalhamento da diária.');
        }
        for (var i=contLinhasTrecho; i>=1; i--){
            remove(i,'trecho');
        }
        contLinhasTrecho = 0;
    }

    if(dataSaidaPrevista != null && dataSaidaPrevista.value != "" && dataRetornoPrevista != null && dataRetornoPrevista.value != ""
        && horaSaidaPrevista != null && horaSaidaPrevista.value != "" && horaRetornoPrevista != null && horaRetornoPrevista.value != ""){

        var dataSaida = Date.parseDateUtil(dataSaidaPrevista.value + " " + horaSaidaPrevista.value + ":00","d/m/Y H:i:s");
        var dataRetorno = Date.parseDateUtil(dataRetornoPrevista.value + " " + horaRetornoPrevista.value + ":00", "d/m/Y H:i:s");
        var hSaida = Date.parseDateUtil("01/01/2000 " + horaSaidaPrevista.value + ":00","d/m/Y H:i:s");
        var hRetorno = Date.parseDateUtil("01/01/2000 " + horaRetornoPrevista.value + ":00", "d/m/Y H:i:s");
        var hiAux = horaSaidaPrevista.value.split( ":" )[0].toString() + horaSaidaPrevista.value.split( ":" )[1].toString();
        if(hiAux.charAt(0) == '0'){hiAux = hiAux.substr(1, 3);}
        var hi = parseInt(hiAux);
        var hfAux = horaRetornoPrevista.value.split( ":" )[0].toString() + horaRetornoPrevista.value.split( ":" )[1].toString();
        if(hfAux.charAt(0) == '0'){hfAux = hfAux.substr(1, 3);}
        var hf = parseInt(hfAux);
        var complemento = 0;

        qtdDiariasT = dateDiffInDays(dataRetorno, dataSaida);
        qtdHoras = parseFloat(dateDiffInHoursWithMinutes(hRetorno, hSaida));
        if(hf < hi){
            qtdHoras = 24-qtdHoras;
        }

        if(qtdHoras < minQuebraDiariasUm){
            complemento = 1;
        }
        else if(qtdHoras >= minQuebraDiariasUm && qtdHoras <= maxQuebraDiariasUm){
            qtdDiariasT = qtdDiariasT + quebraDiariasUm;
            complemento = quebraDiariasDois;
        }
        else if(qtdHoras > minQuebraDiariasDois && qtdHoras <= maxQuebraDiariasDois){
            qtdDiariasT = qtdDiariasT + quebraDiariasDois;
            complemento = quebraDiariasUm;
        }

        //testa a condição do pernoite
        if(dateHasSameDay(dataSaidaPrevista.value, dataRetornoPrevista.value) == false){
            if(dateWhoMostHours(horaRetornoPrevista.value, horaSaidaPrevista.value) && dateWhoMostHours("00:00:00",horaRetornoPrevista.value)){
                qtdDiariasT = qtdDiariasT + complemento;
            }
        }

        $("qtDiaria").value = float2moeda(qtdDiariasT);
        $("qtDiariaAux").value = $("qtDiaria").value;

        setTotalDiarias();
        setTotalAdiantamento();
    }
}

function calcularQtdDiariasSemValidar(){

    var qtdDiariasT = 0;
    var qtdHoras = 0;

    var dataSaidaPrevista = $("dataSaidaPrevista");
    var dataRetornoPrevista = $("dataRetornoPrevista");
    var horaSaidaPrevista = $("horaSaidaPrevista");
    var horaRetornoPrevista = $("horaRetornoPrevista");

    if(dataSaidaPrevista != null && dataSaidaPrevista.value != "" && dataRetornoPrevista != null && dataRetornoPrevista.value != ""
        && horaSaidaPrevista != null && horaSaidaPrevista.value != "" && horaRetornoPrevista != null && horaRetornoPrevista.value != ""){

        var dataSaida = Date.parseDateUtil(dataSaidaPrevista.value + " " + horaSaidaPrevista.value + ":00","d/m/Y H:i:s");
        var dataRetorno = Date.parseDateUtil(dataRetornoPrevista.value + " " + horaRetornoPrevista.value + ":00", "d/m/Y H:i:s");
        var hSaida = Date.parseDateUtil("01/01/2000 " + horaSaidaPrevista.value + ":00","d/m/Y H:i:s");
        var hRetorno = Date.parseDateUtil("01/01/2000 " + horaRetornoPrevista.value + ":00", "d/m/Y H:i:s");
        var hiAux = horaSaidaPrevista.value.split( ":" )[0].toString() + horaSaidaPrevista.value.split( ":" )[1].toString();
        if(hiAux.charAt(0) == '0'){hiAux = hiAux.substr(1, 3);}
        var hi = parseInt(hiAux);
        var hfAux = horaRetornoPrevista.value.split( ":" )[0].toString() + horaRetornoPrevista.value.split( ":" )[1].toString();
        if(hfAux.charAt(0) == '0'){hfAux = hfAux.substr(1, 3);}
        var hf = parseInt(hfAux);

        var complemento = 0;

        if(liberacaoRetroativo == true){
            qtdDiariasT = dateDiffInDays(dataRetorno, dataSaida);
            qtdHoras = parseFloat(dateDiffInHoursWithMinutes(hRetorno, hSaida));
            if(hf < hi){
                qtdHoras = 24-qtdHoras;
            }

            if(qtdHoras < minQuebraDiariasUm){
                complemento = 1;
            }
            else if(qtdHoras >= minQuebraDiariasUm && qtdHoras <= maxQuebraDiariasUm){
                qtdDiariasT = qtdDiariasT + quebraDiariasUm;
                complemento = quebraDiariasDois;
            }
            else if(qtdHoras > minQuebraDiariasDois && qtdHoras <= maxQuebraDiariasDois){
                qtdDiariasT = qtdDiariasT + quebraDiariasDois;
                complemento = quebraDiariasUm;
            }

            //testa a condição do pernoite
            if(dateHasSameDay(dataSaidaPrevista.value, dataRetornoPrevista.value) == false){
                if(dateWhoMostHours(horaRetornoPrevista.value, horaSaidaPrevista.value) && dateWhoMostHours("00:00:00",horaRetornoPrevista.value)){
                    qtdDiariasT = qtdDiariasT + complemento;
                }
            }
        }else{
            if (validarDatas(dataSaida, dataRetorno)){

                qtdDiariasT = dateDiffInDays(dataRetorno, dataSaida);
                qtdHoras = parseFloat(dateDiffInHoursWithMinutes(hRetorno, hSaida));
                if(hf < hi){
                    qtdHoras = 24-qtdHoras;
                }

                if(qtdHoras < minQuebraDiariasUm){
                    complemento = 1;
                }
                else if(qtdHoras >= minQuebraDiariasUm && qtdHoras <= maxQuebraDiariasUm){
                    qtdDiariasT = qtdDiariasT + quebraDiariasUm;
                    complemento = quebraDiariasDois;
                }
                else if(qtdHoras > minQuebraDiariasDois && qtdHoras <= maxQuebraDiariasDois){
                    qtdDiariasT = qtdDiariasT + quebraDiariasDois;
                    complemento = quebraDiariasUm;
                }

                //testa a condição do pernoite
                if(dateHasSameDay(dataSaidaPrevista.value, dataRetornoPrevista.value) == false){
                    if(dateWhoMostHours(horaRetornoPrevista.value, horaSaidaPrevista.value) && dateWhoMostHours("00:00:00",horaRetornoPrevista.value)){
                        qtdDiariasT = qtdDiariasT + complemento;
                    }
                }
            }
        }

        $("qtDiaria").value = float2moeda(qtdDiariasT);
        $("qtDiariaAux").value = $("qtDiaria").value;

        setTotalDiarias();
        setTotalAdiantamento();
    }
}

function calcularQtdDiariasLimite(){

    var qtdDiariasT = 0;
    var qtdHoras = 0;

    var dataSaidaPrevista = $("dataSaidaPrevista");
    var dataRetornoPrevista = $("dataRetornoPrevista");
    var horaSaidaPrevista = $("horaSaidaPrevista");
    var horaRetornoPrevista = $("horaRetornoPrevista");

    if(dataSaidaPrevista != null && dataSaidaPrevista.value != "" && dataRetornoPrevista != null && dataRetornoPrevista.value != ""
        && horaSaidaPrevista != null && horaSaidaPrevista.value != "" && horaRetornoPrevista != null && horaRetornoPrevista.value != ""){

        var dataSaida = Date.parseDateUtil(dataSaidaPrevista.value + " " + horaSaidaPrevista.value + ":00","d/m/Y H:i:s");
        var dataRetorno = Date.parseDateUtil(dataRetornoPrevista.value + " " + horaRetornoPrevista.value + ":00", "d/m/Y H:i:s");
        var hSaida = Date.parseDateUtil("01/01/2000 " + horaSaidaPrevista.value + ":00","d/m/Y H:i:s");
        var hRetorno = Date.parseDateUtil("01/01/2000 " + horaRetornoPrevista.value + ":00", "d/m/Y H:i:s");
        var hiAux = horaSaidaPrevista.value.split( ":" )[0].toString() + horaSaidaPrevista.value.split( ":" )[1].toString();
        if(hiAux.charAt(0) == '0'){hiAux = hiAux.substr(1, 3);}
        var hi = parseInt(hiAux);
        var hfAux = horaRetornoPrevista.value.split( ":" )[0].toString() + horaRetornoPrevista.value.split( ":" )[1].toString();
        if(hfAux.charAt(0) == '0'){hfAux = hfAux.substr(1, 3);}
        var hf = parseInt(hfAux);

        var complemento = 0;

        qtdDiariasT = dateDiffInDays(dataRetorno, dataSaida);
        qtdHoras = parseFloat(dateDiffInHoursWithMinutes(hRetorno, hSaida));
        if(hf < hi){
            qtdHoras = 24-qtdHoras;
        }

        if(qtdHoras < minQuebraDiariasUm){
            complemento = 1;
        }
        else if(qtdHoras >= minQuebraDiariasUm && qtdHoras <= maxQuebraDiariasUm){
            qtdDiariasT = qtdDiariasT + quebraDiariasUm;
            complemento = quebraDiariasDois;
        }
        else if(qtdHoras > minQuebraDiariasDois && qtdHoras <= maxQuebraDiariasDois){
            qtdDiariasT = qtdDiariasT + quebraDiariasDois;
            complemento = quebraDiariasUm;
        }

        //testa a condição do pernoite
        if(dateHasSameDay(dataSaidaPrevista.value, dataRetornoPrevista.value) == false){
            if(dateWhoMostHours(horaRetornoPrevista.value, horaSaidaPrevista.value) && dateWhoMostHours("00:00:00",horaRetornoPrevista.value)){
                qtdDiariasT = qtdDiariasT + complemento;
            }
        }

        $("qtDiariaAux").value = float2moeda(qtdDiariasT);
    }
}


function calcularQtdDiariasTrecho(){
    var qtdDiariasT = 0;
    var qtdHoras = 0;

    var dataInicio = $("dataInicio");
    var dataFim = $("dataFim");
    var horaInicio = $("horaInicio");
    var horaFim = $("horaFim");

    if(dataInicio != null && dataInicio.value != "" && dataFim != null && dataFim.value != ""
        && horaInicio != null && horaInicio.value != "" && horaFim != null && horaFim.value != ""){
        
        var dataSaida = Date.parseDateUtil(dataInicio.value + " " + horaInicio.value + ":00","d/m/Y H:i:s");
        var dataRetorno = Date.parseDateUtil(dataFim.value + " " + horaFim.value + ":00", "d/m/Y H:i:s");
        var hSaida = Date.parseDateUtil("01/01/2000 " + horaInicio.value + ":00","d/m/Y H:i:s");
        var hRetorno = Date.parseDateUtil("01/01/2000 " + horaFim.value + ":00", "d/m/Y H:i:s");
        var complemento = 0;
        var hiAux = horaInicio.value.split( ":" )[0].toString() + horaInicio.value.split( ":" )[1].toString();
        if(hiAux.charAt(0) == '0'){hiAux = hiAux.substr(1, 3);}
        var hi = parseInt(hiAux);
        var hfAux = horaFim.value.split( ":" )[0].toString() + horaFim.value.split( ":" )[1].toString();
        if(hfAux.charAt(0) == '0'){hfAux = hfAux.substr(1, 3);}
        var hf = parseInt(hfAux);

        if (validarDatasTrecho(dataSaida, dataRetorno)){
            
            qtdDiariasT = dateDiffInDays(dataRetorno, dataSaida)*1;
            qtdHoras = parseFloat(dateDiffInHoursWithMinutes(hRetorno, hSaida));
            
            if(hf < hi){
                qtdHoras = 24-qtdHoras;
            }

            if(qtdHoras < minQuebraDiariasUm){
                complemento = 1;
            }
            else if(qtdHoras >= minQuebraDiariasUm && qtdHoras <= maxQuebraDiariasUm){
                qtdDiariasT = qtdDiariasT + quebraDiariasUm;
                complemento = quebraDiariasDois;
            }
            else if(qtdHoras > minQuebraDiariasDois && qtdHoras <= maxQuebraDiariasDois){
                qtdDiariasT = qtdDiariasT + quebraDiariasDois;
                complemento = quebraDiariasUm;
            }

            //testa a condição do pernoite
            if(dateHasSameDay(dataInicio.value, dataFim.value) == false){
                if(dateWhoMostHours(horaFim.value, horaInicio.value) && dateWhoMostHours("00:00:00",horaFim.value)){
                    qtdDiariasT = qtdDiariasT + complemento;
                }
            }
        }
        
        $("qtDiariaTrecho").value = float2moeda(qtdDiariasT);

        setTotalDiariasTrecho();
        setTotalDiariasSomadasTrecho();
        setTotalAdiantamento();
    }
}

function validarDatas(dataSaida, dataRetorno){
    var dataAtual = new Date().dateFormat("d/m/Y");
    var d = $("dataSaidaPrevista").value;

    if ( parseInt( d.split( "/" )[2].toString() + d.split( "/" )[1].toString() + d.split( "/" )[0].toString() ) < parseInt( dataAtual.split( "/" )[2].toString() + dataAtual.split( "/" )[1].toString() + dataAtual.split( "/" )[0].toString() ) ){
        alert('A data de saída deve ser maior ou igual que a data atual.');
        $('dataSaidaPrevista').value = '';
        $('horaSaidaPrevista').value = '';

        return false;
    }
    if (dataSaida > dataRetorno){
        alert('A data de saída deve ser menor do que a data de retorno.');
        $('dataRetornoPrevista').value = '';
        $('horaRetornoPrevista').value = '';

        return false;
     }
     

     return true;

}

function validarDatasTrecho(dataSaida, dataRetorno){
    //var dataAtual = new Date().dateFormat("d/m/Y");
    var di_aux = $("dataInicio").value + "/"+ $("horaInicio").value.replace(":", "/");
    var di = parseInt( di_aux.split( "/" )[2].toString() + di_aux.split( "/" )[1].toString() + di_aux.split( "/" )[0].toString() + di_aux.split( "/" )[3].toString() + di_aux.split( "/" )[4].toString() )
    var df_aux = $("dataFim").value + "/"+ $("horaFim").value.replace(":", "/");
    var df = parseInt( df_aux.split( "/" )[2].toString() + df_aux.split( "/" )[1].toString() + df_aux.split( "/" )[0].toString() + df_aux.split( "/" )[3].toString() + df_aux.split( "/" )[4].toString() )
    var ds_aux = $("dataSaidaPrevista").value + "/" + $("horaSaidaPrevista").value.replace(":", "/");
    var ds = parseInt( ds_aux.split( "/" )[2].toString() + ds_aux.split( "/" )[1].toString() + ds_aux.split( "/" )[0].toString() + ds_aux.split( "/" )[3].toString() + ds_aux.split( "/" )[4].toString() )
    var dr_aux = $("dataRetornoPrevista").value + "/" + $("horaRetornoPrevista").value.replace(":", "/");
    var dr = parseInt( dr_aux.split( "/" )[2].toString() + dr_aux.split( "/" )[1].toString() + dr_aux.split( "/" )[0].toString() + dr_aux.split( "/" )[3].toString() + dr_aux.split( "/" )[4].toString() )


    if(di < ds){
        alert('A data inicio deve ser igual ou maior que a data de saída.');
        $('dataInicio').value = '';
        $('horaInicio').value = '';

        return false;
    }
    if(di >= dr){
        alert('A data inicio deve ser menor que a data de retorno.');
        $('dataInicio').value = '';
        $('horaInicio').value = '';

        return false;
    }
    if(df > dr){
        alert('A data fim deve ser menor ou igual que a data de retorno.');
        $('dataFim').value = '';
        $('horaFim').value = '';

        return false;
    }
    /*if ( parseInt( d.split( "/" )[2].toString() + d.split( "/" )[1].toString() + d.split( "/" )[0].toString() ) < parseInt( dataAtual.split( "/" )[2].toString() + dataAtual.split( "/" )[1].toString() + dataAtual.split( "/" )[0].toString() ) ){
        alert('A data inicio deve ser maior ou igual que a data atual.');
        $('dataInicio').value = '';
        $('horaInicio').value = '';

        return false;
    }*/
    if (dataSaida > dataRetorno){
        alert('A data fim deve ser maior do que a data inicio.');
        $('dataFim').value = '';
        $('horaFim').value = '';

        return false;
     }


     return true;

}

function validarIntinerario(){

    if(contLinhas < 2){
        alert("Insira ao menos dois intinerários na solicitação de viagem.");
        return false;
    }

    var dataRetornoPrevista = Date.parseDateUtil($("dataRetornoPrevista").value + " 00:00:00","d/m/Y H:i:s");
    var datas = document.getElementsByName("data[]");

    var dataUltimoItinerario = Date.parseDateUtil(datas[contLinhas-1].value + " 00:00:00","d/m/Y H:i:s");

    if (dateDiffInDays(dataRetornoPrevista, dataUltimoItinerario) != 0){
        alert("A data do último itinerário deve ser igual à data de retorno prevista.");
        return false;
    }

    return true;

}

function validarTrecho(){

    if($("tipoDiariaId").value == "2"){
        if(contLinhasTrecho < 1){
            alert("Insira ao menos uma diária detalhada.");
            return false;
        }
    }

    return true;

}

function adicionarLinhaItinerario(){

    var dataLinhaItinerario = $("dataLinhaItinerario").value;
    var cidadeOrigemId = $("cidadeOrigemId")[$("cidadeOrigemId").selectedIndex].text;
    var cidadeDestinoId = $("cidadeDestinoId")[$("cidadeDestinoId").selectedIndex].text;
    var obsLinhaItinerario = $("obsLinhaItinerario").value;


    if($("dataSaidaPrevista").value.length < 9 || $("dataRetornoPrevista").value.length < 9 || 
       $("horaSaidaPrevista").value.length < 4 || $("horaRetornoPrevista").value.length < 4){
        alert("Informe a Programação da Viagem.");
        $("dataSaidaPrevista").focus();
        return;
    }

    if ((dataLinhaItinerario == null) || (dataLinhaItinerario == '') || (cidadeOrigemId == '-- Selecione --') || (cidadeDestinoId == '-- Selecione --')){
        alert('Para adicionar um itinerário é necessário preencher a data, a cidade origem e a cidade destino.');
        return;
    }

    if (cidadeOrigemId == cidadeDestinoId){
        alert('Para adicionar um itinerário é necessário que a cidade origem seja diferente da cidade destino.');
        return;
    }

    var dataItinerario = Date.parseDateUtil(dataLinhaItinerario + " 00:00:00","d/m/Y H:i:s");

    if (contLinhas == 0){
        cidadeOrigemInicial = cidadeOrigemId;
        contCidadeOrigemInicial = 1;

        var dataSaidaPrevista = Date.parseDateUtil($("dataSaidaPrevista").value + " 00:00:00","d/m/Y H:i:s");

        if (dateDiffInDays(dataItinerario, dataSaidaPrevista) != 0){
            alert('A primeira data do itinerário deve ser igual à data de saída prevista.');
            return;
        }
    }
    else{
        var dataRetornoPrevista = Date.parseDateUtil($("dataRetornoPrevista").value + " 00:00:00","d/m/Y H:i:s");
        var datas = document.getElementsByName("data[]");

        var dataItinerarioAnterior = Date.parseDateUtil(datas[contLinhas-1].value + " 00:00:00","d/m/Y H:i:s");

        if (dataItinerarioAnterior > dataItinerario){
            alert('A data do itinerário deve ser igual ou maior à última data do itinerário.');
            return;
        }

        if (dataRetornoPrevista < dataItinerario){
            alert('A data do itinerário deve ser menor ou igual à data de retorno prevista.');
            return;
        }

        var cidadesD = document.getElementsByName("cidadeD[]");
        if (cidadesD[contLinhas-1].value != cidadeOrigemId){
            alert('A cidade de origem atual deve ser igual à cidade de destino do itinerário anterior.');
            return;
        }

        if(cidadeOrigemInicial == cidadeOrigemId){
            contCidadeOrigemInicial++;
        }

        if(contCidadeOrigemInicial > 1){
            //Quando um viajante saide de seu Município para outro e volta, isso se caracteriza o final da viagem. Porém, se houver uma nova viagem 
            //na mesma solicitação, deverá obedecer os seguintes requisitos.
            
            //Ter diária detalhada
            if($("tipoDiariaId").value != "2"){
                alert("A Programação da Viagem para este tipo de solicitação não pode ultrapassar o período de 7 dias, e precisa de detalhamento das diárias!");
                return;
            }

            //Ter limite maximo de 31 dias.
            if(moeda2float($("qtDiariaAux").value) > 31){
                alert("A Programação da Viagem para este tipo de solicitação não pode ultrapassar o período de 31 dias!");
                return;
            }
        }
    }

    nomeTabela = 'itinerario';
    solicitacaoOuPrestacao = "s";
    var tabela = listTables.getTableById(nomeTabela);

    if(tabela == null){
        tabela = new Table(nomeTabela);
    }

    //auxiliar para criar identificadores unicos
    contLinhas++;

    var linha = new Line(contLinhas, 'linha', tabela);

    new Column('data[]', dataLinhaItinerario, 8, "", linha, "");
    new Column('cidadeO[]', cidadeOrigemId, 30, "", linha, "");//20
    new Column('cidadeD[]', cidadeDestinoId, 30, "", linha, "");//20
    new Column('obs[]', obsLinhaItinerario, 22, "", linha, "");

    //constrói a tabela adicionando a linha
    renderTable(tabela);

    $("dataLinhaItinerario").value = "";
    $("cidadeOrigemId").selectedIndex = $("cidadeDestinoId").selectedIndex;
    $("cidadeDestinoId").selectedIndex = 0;
    $("obsLinhaItinerario").value = "";
}

function carregarDadosEditar(){
    alterarTipoDiariaInicial();
    iniciarCidades();
    if($("tipoDiariaId").value == "2"){
        $("tipoDet").style.display = "block";
        setTrecho();
    }
    setItinerario();
}

function carregarDadosEditarAgendamento(){
    alterarTipoDiariaAgendamentoInicial();
    iniciarCidades();
    if($("tipoDiariaId").value == "2"){
        $("tipoDet").style.display = "block";
        setTrecho();
    }
    setItinerario();
}

function iniciarCidades(){
    Funcoes.obterCidadesPorDestino(DWRUtil.getValue("destino"), "cidadeOrigemId", {
        callback:setCidadesOri,
        async:false
    });
    Funcoes.obterCidadesPorDestino(DWRUtil.getValue("destino"), "cidadeDestinoId", {
        callback:setCidadesDes,
        async:false
    });
}

function obterCidades(){
    iniciarCidades();

    if($("funcViajId").value != ""){
        obterDiaria($("funcViajId").value, "PADRAO");
    }

    if (contLinhas > 0){
        alert('Ao efetuar alterações no destino deve-se refazer o itinerário.');
        for (var i=contLinhas; i>=1; i--){
            remove(i,'itinerario');
        }
        contLinhas = 0;
    }

    calcularQtdDiarias();
}

function obterCidadesConsultor(){
    iniciarCidades();

    if (contLinhas > 0){
        alert('Ao efetuar alterações no destino deve-se refazer o itinerário.');
        for (var i=contLinhas; i>=1; i--){
            remove(i,'itinerario');
        }
        contLinhas = 0;
    }
}

function setCidadesOri(str){
    $("cidOri").innerHTML = str;
}

function setCidadesDes(str){
    $("cidDes").innerHTML = str;
}

function obterDiaria(valor, tipoDiaria) {
    if(tipoDiaria == "PADRAO" || tipoDiaria == 0){
        Funcoes.obterDiaria(valor, DWRUtil.getValue("destino"), setDiariaPadrao);
    }else{
        setDiaria(moeda2float($("diariaE").value));
    }
}