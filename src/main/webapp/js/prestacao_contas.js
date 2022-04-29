var contLinhas = 0; 
var contLinhasGasto = 0;

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

function calcularQtdDiariasPrestacao(){

    var qtdDiariasT = 0;
    var qtdHoras = 0;

    var dataSaidaEfetiva = $("dataSaidaEfetiva");
    var dataRetornoEfetiva = $("dataRetornoEfetiva");
    var horaSaidaEfetiva = $("horaSaidaEfetiva");
    var horaRetornoEfetiva = $("horaRetornoEfetiva");

    if(dataSaidaEfetiva != null && dataSaidaEfetiva.value != "" && dataRetornoEfetiva != null && dataRetornoEfetiva.value != ""
        && horaSaidaEfetiva != null && horaSaidaEfetiva.value != "" && horaRetornoEfetiva != null && horaRetornoEfetiva.value != ""){
        
        var dataSaida = Date.parseDateUtil(dataSaidaEfetiva.value + " " + horaSaidaEfetiva.value + ":00","d/m/Y H:i:s");
        var dataRetorno = Date.parseDateUtil(dataRetornoEfetiva.value + " " + horaRetornoEfetiva.value + ":00", "d/m/Y H:i:s");
        var hSaida = Date.parseDateUtil("01/01/2000 " + horaSaidaEfetiva.value + ":00","d/m/Y H:i:s");
        var hRetorno = Date.parseDateUtil("01/01/2000 " + horaRetornoEfetiva.value + ":00", "d/m/Y H:i:s");
        var complemento = 0;

        if (dataSaida > dataRetorno){
            alert('A data de saída deve ser menor do que a data de retorno.');
            $('dataRetornoEfetiva').value = '';
            $('horaRetornoEfetiva').value = '';
        }
        else {
            qtdDiariasT = dateDiffInDays(dataRetorno, dataSaida);
            qtdHoras = parseFloat(dateDiffInHoursWithMinutes(hRetorno, hSaida));
            if(hRetorno < hSaida){
                qtdHoras = 24-qtdHoras;
            }

            if(qtdHoras < minQuebraDiariasUm){
                complemento = 1;
            }
            else if(qtdHoras >= minQuebraDiariasUm & qtdHoras <= maxQuebraDiariasUm){
                qtdDiariasT = qtdDiariasT + quebraDiariasUm;
                complemento = quebraDiariasDois;
            }
            else if(qtdHoras > minQuebraDiariasDois & qtdHoras <= maxQuebraDiariasDois){
                qtdDiariasT = qtdDiariasT + quebraDiariasDois;
                complemento = quebraDiariasUm;
            }

            //testa a condição do pernoite
            if(dateHasSameDay(dataSaidaEfetiva.value, dataRetornoEfetiva.value) == false){
                if(dateWhoMostHours(horaRetornoEfetiva.value, horaSaidaEfetiva.value) && dateWhoMostHours("00:00:00", horaRetornoEfetiva.value)){
                    qtdDiariasT = qtdDiariasT + complemento;
                }
            }
        }

        $("qtDiaria").value = float2moeda(qtdDiariasT);
        

        //setTotalDiarias();
        //setTotalAdiantamento();

        atualizarDiaria();

        atualizarResumo();
        
        if (contLinhas > 0){
            alert('Ao efetuar alterações na programação da viagem deve-se refazer o itinerário.');
            for (var i=contLinhas; i>=1; i--){
                remove(i,'itinerario');
            }
            contLinhas = 0;
        }

        if($("tipoDiariaNumero").value == "2"){
            alert('Ao efetuar alterações na programação da viagem deve-se refazer o período detalhado.');
            for (var i=contLinhasTrecho; i>=1; i--){
                remove(i,'trecho');
            }
            contLinhasTrecho = 0;
        }
    }
}

function calcularQtdDiariasPrestacaoSemValidar(){

    var qtdDiariasT = 0;
    var qtdHoras = 0;

    var dataSaidaEfetiva = $("dataSaidaEfetiva");
    var dataRetornoEfetiva = $("dataRetornoEfetiva");
    var horaSaidaEfetiva = $("horaSaidaEfetiva");
    var horaRetornoEfetiva = $("horaRetornoEfetiva");

    if(dataSaidaEfetiva != null && dataSaidaEfetiva.value != "" && dataRetornoEfetiva != null && dataRetornoEfetiva.value != ""
        && horaSaidaEfetiva != null && horaSaidaEfetiva.value != "" && horaRetornoEfetiva != null && horaRetornoEfetiva.value != ""){

        var dataSaida = Date.parseDateUtil(dataSaidaEfetiva.value + " " + horaSaidaEfetiva.value + ":00","d/m/Y H:i:s");
        var dataRetorno = Date.parseDateUtil(dataRetornoEfetiva.value + " " + horaRetornoEfetiva.value + ":00", "d/m/Y H:i:s");
        var hSaida = Date.parseDateUtil("01/01/2000 " + horaSaidaEfetiva.value + ":00","d/m/Y H:i:s");
        var hRetorno = Date.parseDateUtil("01/01/2000 " + horaRetornoEfetiva.value + ":00", "d/m/Y H:i:s");
        var complemento = 0;

        qtdDiariasT = dateDiffInDays(dataRetorno, dataSaida);
        qtdHoras = parseFloat(dateDiffInHoursWithMinutes(hRetorno, hSaida));
        if(hRetorno < hSaida){
            qtdHoras = 24-qtdHoras;
        }

        if(qtdHoras < minQuebraDiariasUm){
            complemento = 1;
        }
        else if(qtdHoras >= minQuebraDiariasUm & qtdHoras <= maxQuebraDiariasUm){
            qtdDiariasT = qtdDiariasT + quebraDiariasUm;
            complemento = quebraDiariasDois;
        }
        else if(qtdHoras > minQuebraDiariasDois & qtdHoras <= maxQuebraDiariasDois){
            qtdDiariasT = qtdDiariasT + quebraDiariasDois;
            complemento = quebraDiariasUm;
        }

        //testa a condição do pernoite
        if(dateHasSameDay(dataSaidaEfetiva.value, dataRetornoEfetiva.value) == false){
            if(dateWhoMostHours(horaRetornoEfetiva.value, horaSaidaEfetiva.value) && dateWhoMostHours("00:00:00", horaRetornoEfetiva.value)){
                qtdDiariasT = qtdDiariasT + complemento;
            }
        }

        $("qtDiaria").value = float2moeda(qtdDiariasT);

        atualizarDiaria();
    }
}

function validarIntinerarioPrestacao(){

    if(contLinhas < 2){
        alert("Insira ao menos dois intinerários na solicitação de viagem.");
        return false;
    }

    var dataRetornoEfetiva = Date.parseDateUtil($("dataRetornoEfetiva").value + " 00:00:00","d/m/Y H:i:s");
    var datas = document.getElementsByName("data[]");

    var dataUltimoItinerario = Date.parseDateUtil(datas[contLinhas-1].value + " 00:00:00","d/m/Y H:i:s");

    if (dateDiffInDays(dataRetornoEfetiva, dataUltimoItinerario) != 0){
        alert("A data do último itinerário deve ser igual à data de retorno Efetiva.");
        return false;
    }

    return true;

}

function setTotalAdiantamentoPrestacao(){
    var totalDiaria = moeda2float($("totalDiarias").value);
    var adiantamento = moeda2float($("valorAdiantamento").value);

    $("totalAdiantamentoConc").value = float2moeda(totalDiaria+adiantamento);
    $("totalAdiantamento").value = float2moeda(totalDiaria+adiantamento);
}

function atualizarDiaria() {
    setTotalDiarias();
    setTotalAdiantamentoPrestacao();

    atualizarResumo();
}

function adicionarLinhaItinerarioPrestacao(){

    var dataLinhaItinerario = $("dataLinhaItinerario").value;
    var cidadeOrigemId = $("cidadeOrigemId")[$("cidadeOrigemId").selectedIndex].text;
    var cidadeDestinoId = $("cidadeDestinoId")[$("cidadeDestinoId").selectedIndex].text;
    var obsLinhaItinerario = $("obsLinhaItinerario").value;

    if($("dataSaidaEfetiva").value.length < 9 || $("dataRetornoEfetiva").value.length < 9 || 
        $("horaSaidaEfetiva").value.length < 4 || $("horaRetornoEfetiva").value.length < 4){
        alert("Informe a Programação da Viagem.");
        $("dataSaidaEfetiva").focus();
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
        var dataSaidaEfetiva = Date.parseDateUtil($("dataSaidaEfetiva").value + " 00:00:00","d/m/Y H:i:s");

        if (dateDiffInDays(dataItinerario, dataSaidaEfetiva) != 0){
            alert('A primeira data do itinerário deve ser igual à data de saída Efetiva.');
            return;
        }
    }
    else{
        var dataRetornoEfetiva = Date.parseDateUtil($("dataRetornoEfetiva").value + " 00:00:00","d/m/Y H:i:s");
        var datas = document.getElementsByName("data[]");

        var dataItinerarioAnterior = Date.parseDateUtil(datas[contLinhas-1].value + " 00:00:00","d/m/Y H:i:s");

        if (dataItinerarioAnterior > dataItinerario){
            alert('A data do itinerário deve ser igual ou maior à última data do itinerário.');
            return;
        }

        if (dataRetornoEfetiva < dataItinerario){
            alert('A data do itinerário deve ser menor ou igual à data de retorno Efetiva.');
            return;
        }

        var cidadesD = document.getElementsByName("cidadeD[]");
        if (cidadesD[contLinhas-1].value != cidadeOrigemId){
            alert('A cidade de origem atual deve ser igual à cidade de destino do itinerário anterior.');
            return;
        }
    }

    nomeTabela = 'itinerario';
    solicitacaoOuPrestacao = "p";
    var tabela = listTables.getTableById(nomeTabela);

    if(tabela == null){
        tabela = new Table(nomeTabela);
    }

    contLinhas++;
    var linha = new Line(contLinhas, 'linha', tabela);

    new Column('data[]', dataLinhaItinerario, 11, "", linha, "");
    new Column('cidadeO[]', cidadeOrigemId, 25, "", linha, "");
    new Column('cidadeD[]', cidadeDestinoId, 25, "", linha, "");
    new Column('obs[]', obsLinhaItinerario, 22, "", linha, "");

    renderTable(tabela);

    $("dataLinhaItinerario").value = "";
    $("cidadeOrigemId").selectedIndex = $("cidadeDestinoId").selectedIndex;
    $("cidadeDestinoId").selectedIndex = 0;
    $("obsLinhaItinerario").value = "";
}

function adicionarLinhaGasto(){

    var gastoId = $("gastoId")[$("gastoId").selectedIndex].value;
    var gastoDesc = $("gastoId")[$("gastoId").selectedIndex].text;
    var valorGasto = $("valorGasto").value;
    var stringVazia = "";

    if ((valorGasto == null) || (valorGasto == stringVazia) || (valorGasto == "0,00") || (gastoId == stringVazia)){
        alert('Para adicionar um gasto é necessário preencher a descrição e o valor do mesmo.');
        return;
    }

    nomeTabela = 'gasto';
    var tabela = listTables.getTableById(nomeTabela);

    if(tabela == null){
        tabela = new Table(nomeTabela);
    }

    contLinhasGasto++;
    var linha = new Line(contLinhasGasto, 'linha', tabela);
    new Column('descGasto[]', gastoDesc, 15, "", linha, gastoId);
    new Column('vlrGasto[]', valorGasto, 15, "", linha, "");
    renderTable(tabela);
    atualizarTotalGastos();

    $("gastoId").selectedIndex = 0;
    $("valorGasto").value = "";
}

function setTotalAdiantamentoPrestacao(){
    var totalDiaria = moeda2float($("totalDiarias").value);
    var adiantamento = moeda2float($("valorAdiantamento").value);

    $("totalAdiantamentoConc").value = float2moeda(totalDiaria+adiantamento);
    $("totalAdiantamento").value = float2moeda(totalDiaria+adiantamento);
}

function setTotalDiariasSomadasTrechoPrestacao(){
    var ddt = document.getElementsByName("totalDiariaT[]");
    var qddt = document.getElementsByName("qtDiariaT[]");
    var total_ddt = 0;
    var total_qddt = 0;

    for(i=0;i<ddt.length;i++){
        total_ddt += moeda2float(ddt.item(i).value);
        total_qddt += moeda2float(qddt.item(i).value);
    }
    $("totalDiarias").value = float2moeda(total_ddt);//
    $("qtDiaria").value = float2moeda(total_qddt);

    setTotalAdiantamentoPrestacao();

    atualizarResumo();
}

function atualizarTotalGastos(){
    try{
        var valoresGastos = document.getElementsByName("vlrGasto[]");
        var total_gasto = 0;

        for(i=0;i<valoresGastos.length;i++){
            total_gasto += moeda2float(valoresGastos.item(i).value);
        }
    }catch(e) {
        alert(e);
    }
    $("totalR").value = float2moeda(total_gasto);
    atualizarResumo();
    
}

/*function setTotalDiariasSomadasTrechoPrestacaoInicial(){
    var qddt = document.getElementsByName("qtDiariaT[]");
    var total_qddt = 0;

    for(i=0;i<qddt.length;i++){
        total_qddt += moeda2float(qddt.item(i).value);
    }
    $("qtDiaria").value = float2moeda(total_qddt);
}*/


function alterarTipoDiariaPrestacao(){
    $("tipoDiariaNumero").value = $("tipoDiariaId").value;

    if($("tipoDiariaId").value == "1"){
        $("valorDiaria").readOnly = false;
        $("descricaoE").disabled = false;
        $("tituloFCM").style.color = "#FF0000";
        $("tituloDesc").style.color = "#FF0000";
        $("tipoDiariaE").style.color = "#FF0000";

        setDiaria('0,00');
        $("descricaoE").value = $("descricaoEOriginal").value;

        $("descricaoE").style.color = "#000000";
        calcularQtdDiariasPrestacaoSemValidar();

        $("tipoDet").style.display = "none";
        $("valorDiariaLabel1").style.display = "block";
        $("valorDiariaLabel2").style.display = "none";
    }else{
        if($("tipoDiariaId").value == "0"){
            $("valorDiaria").readOnly = true;
            $("descricaoE").disabled = true;
            $("tituloFCM").style.color = "#DDDDDD";
            $("tituloDesc").style.color = "#DDDDDD";
            $("tipoDiariaE").style.color = "#DDDDDD";

            calcularQtdDiariasPrestacaoSemValidar();
            setDiaria(moeda2float($("diariaP").value));

            //$("descricaoE").value = "";
            if($("descricaoE").value != ""){
                $("descricaoEOriginal").value = $("descricaoE").value;
                $("descricaoE").value = "";
            }

            $("tipoDet").style.display = "none";
            $("valorDiariaLabel1").style.display = "block";
            $("valorDiariaLabel2").style.display = "none";
        }else{
            $("valorDiaria").readOnly = true;
            $("descricaoE").disabled = true;
            $("tituloFCM").style.color = "#DDDDDD";
            $("tituloDesc").style.color = "#DDDDDD";
            $("tipoDiariaE").style.color = "#DDDDDD";

            setDiaria(moeda2float($("diariaP").value));
            $("totalDiarias").value = "0,00";
            $("totalAdiantamento").value = "0,00";
            $("qtDiaria").value = "0,00";

            //$("descricaoE").value = "";
            if($("descricaoE").value != ""){
                $("descricaoEOriginal").value = $("descricaoE").value;
                $("descricaoE").value = "";
            }

            $("tipoDet").style.display = "block";
            $("valorDiariaLabel1").style.display = "none";
            $("valorDiariaLabel2").style.display = "block";

            $("valorDiariaTrecho").value = $("diariaP").value;

            setTotalDiariasSomadasTrechoPrestacao();
        }
    }
}

function calcularQtdDiariasTrechoPrestacao(){

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
        //var hi = parseInt( horaInicio.value.split( ":" )[0].toString() + horaInicio.value.split( ":" )[1].toString());
        var hiAux = horaInicio.value.split( ":" )[0].toString() + horaInicio.value.split( ":" )[1].toString();
        if(hiAux.charAt(0) == '0'){hiAux = hiAux.substr(1, 3);}
        var hi = parseInt(hiAux);
        //var hf = parseInt( horaFim.value.split( ":" )[0].toString() + horaFim.value.split( ":" )[1].toString());
        var hfAux = horaFim.value.split( ":" )[0].toString() + horaFim.value.split( ":" )[1].toString();
        if(hfAux.charAt(0) == '0'){hfAux = hfAux.substr(1, 3);}
        var hf = parseInt(hfAux);

        if (validarDatasTrechoPrestacao(dataSaida, dataRetorno)){//

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

function validarDatasTrechoPrestacao(dataSaida, dataRetorno){
    //var dataAtual = new Date().dateFormat("d/m/Y");
    var d = $("dataInicio").value;
    var di_aux = d + "/"+ $("horaInicio").value.replace(":", "/");
    var di = parseInt( di_aux.split( "/" )[2].toString() + di_aux.split( "/" )[1].toString() + di_aux.split( "/" )[0].toString() + di_aux.split( "/" )[3].toString() + di_aux.split( "/" )[4].toString() )
    var df_aux = $("dataFim").value + "/"+ $("horaFim").value.replace(":", "/");
    var df = parseInt( df_aux.split( "/" )[2].toString() + df_aux.split( "/" )[1].toString() + df_aux.split( "/" )[0].toString() + df_aux.split( "/" )[3].toString() + df_aux.split( "/" )[4].toString() )
    var dr_aux = $("dataRetornoEfetiva").value + "/" + $("horaRetornoEfetiva").value.replace(":", "/");
    var dr = parseInt( dr_aux.split( "/" )[2].toString() + dr_aux.split( "/" )[1].toString() + dr_aux.split( "/" )[0].toString() + dr_aux.split( "/" )[3].toString() + dr_aux.split( "/" )[4].toString() )

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

function adicionarLinhaTrechoPrestacao(){

    var dataInicio = $("dataInicio").value;
    var horaInicio = $("horaInicio").value;
    var dataFim = $("dataFim").value;
    var horaFim = $("horaFim").value;
    var qtDiariaTrecho = $("qtDiariaTrecho").value;
    var valorDiariaTrecho = $("valorDiariaTrecho").value;
    var totalDiariasTrecho = $("totalDiariasTrecho").value;
    var descricaoTrecho = $("descricaoTrecho").value;

    if($("dataSaidaEfetiva").value.length < 9 || $("dataRetornoEfetiva").value.length < 9 ||
        $("horaSaidaEfetiva").value.length < 4 || $("horaRetornoEfetiva").value.length < 4){
        alert("Informe a Programação da Viagem.");
        $("dataSaidaEfetiva").focus();
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
        var dataSaidaPrevista = Date.parseDateUtil($("dataSaidaEfetiva").value + " 00:00:00","d/m/Y H:i:s");

        if (dateDiffInDays(dataItinerarioI, dataSaidaPrevista) != 0){
            alert('A primeira data deve ser igual à data de saída efetiva.');
            return;
        }
    }
    else{
        var dataRetornoEfetiva = Date.parseDateUtil($("dataRetornoEfetiva").value + " 00:00:00","d/m/Y H:i:s");
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

        if (dataRetornoEfetiva < dataItinerarioI || dataRetornoEfetiva < dataItinerarioF){
            alert('A data deve ser menor ou igual à data de retorno prevista.');
            return;
        }
    }

    nomeTabela = 'trecho';
    solicitacaoOuPrestacao = "p";
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

    setTotalDiariasSomadasTrechoPrestacao();

    $("dataInicio").value = "";
    $("horaInicio").value = "";
    $("dataFim").value = "";
    $("horaFim").value = "";
    $("qtDiariaTrecho").value = "0,00";
    //$("valorDiariaTrecho").value = "0,00";
    $("totalDiariasTrecho").value = "0,00";
    $("descricaoTrecho").value = "";
}

function adicionarLinhaTrechoPrestacaoInicial(){

    var dataInicio = $("dataInicio").value;
    var horaInicio = $("horaInicio").value;
    var dataFim = $("dataFim").value;
    var horaFim = $("horaFim").value;
    var qtDiariaTrecho = $("qtDiariaTrecho").value;
    var valorDiariaTrecho = $("valorDiariaTrecho").value;
    var totalDiariasTrecho = $("totalDiariasTrecho").value;
    var descricaoTrecho = $("descricaoTrecho").value;

    /*if($("dataSaidaPrevista").value.length < 9 || $("dataRetornoPrevista").value.length < 9 ||
        $("horaSaidaPrevista").value.length < 4 || $("horaRetornoPrevista").value.length < 4){*/
    if($("dataSaidaEfetiva").value.length < 9 || $("dataRetornoEfetiva").value.length < 9 ||
        $("horaSaidaEfetiva").value.length < 4 || $("horaRetornoEfetiva").value.length < 4){
        alert("Informe a Programação da Viagem.");
        $("dataSaidaEfetiva").focus();
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
        var dataSaidaPrevista = Date.parseDateUtil($("dataSaidaEfetiva").value + " 00:00:00","d/m/Y H:i:s");

        if (dateDiffInDays(dataItinerarioI, dataSaidaPrevista) != 0){
            alert('A primeira data deve ser igual à data de saída efetiva.');
            return;
        }
    }
    else{
        var dataRetornoPrevista = Date.parseDateUtil($("dataRetornoEfetiva").value + " 00:00:00","d/m/Y H:i:s");
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
    solicitacaoOuPrestacao = "p";
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

    setTotalDiariasSomadasTrechoPrestacao();

    $("dataInicio").value = "";
    $("horaInicio").value = "";
    $("dataFim").value = "";
    $("horaFim").value = "";
    $("qtDiariaTrecho").value = "0,00";
    //$("valorDiariaTrecho").value = "0,00";
    $("totalDiariasTrecho").value = "0,00";
    $("descricaoTrecho").value = "";
}

function carregarDadosPrestacao(){
    iniciarCidades();
    carregarDadosViagem();
    if($("tipoDiariaId").value == "2" || $("tipoDiariaId").value == "DETALHADA"){
        $("tipoDet").style.display = "block";
        $("valorDiariaLabel1").style.display = "none";
        $("valorDiariaLabel2").style.display = "block";

        setTrecho();
    }
    $("qtDiariaConc").value = $("qtDiaria").value;
    setItinerario();
    atualizarResumoPrevisto();
    atualizarTotalGastos();
}

function carregarDadosPrestacaoEfetivo(){
    iniciarCidades();
    carregarDadosViagem();
    if($("tipoDiariaId").value == "2" || $("tipoDiariaId").value == "DETALHADA"){
        $("tipoDet").style.display = "block";
        $("valorDiariaLabel1").style.display = "none";
        $("valorDiariaLabel2").style.display = "block";

        setTrecho();
    }
    $("qtDiariaConc").value = $("qtDiaria").value;
    setItinerario();
    setGastos();
    atualizarResumoPrevisto();
    atualizarTotalGastos();
}

function carregarDadosViagem(){
    $("qtDiariaConc").value = $("qtDiaria").value;
    $("totalDiariasConc").value = $("totalDiarias").value;
    $("totalAdiantamentoConc").value = $("totalAdiantamento").value;
}
/*
function atualizarDespesas(){
    passagens = moeda2float($("passagens").value);
    translado = moeda2float($("translado").value);
    tUrbanos = moeda2float($("tUrbanos").value);
    telServico = moeda2float($("telServico").value);

    $("totalR").value = float2moeda(passagens + translado + tUrbanos + telServico);

    atualizarResumo();
}*/

function atualizarResumoPrevisto(){
    $("vd1").value = $("totalDiariaOriginal").value;
    $("vd2").value = $("totalDiarias").value;
    $("vd3").value = float2moeda(moeda2float($("vd1").value) - moeda2float($("vd2").value));

    $("va1").value = $("valorAdiantamento").value;
    $("va2").value = $("totalR").value;
    $("va3").value = float2moeda(moeda2float($("va1").value) - moeda2float($("va2").value));

    $("tot1").value = float2moeda(moeda2float($("vd1").value) + moeda2float($("va1").value));
    $("tot2").value = float2moeda(moeda2float($("vd2").value) + moeda2float($("va2").value));
    $("tot3").value = float2moeda(moeda2float($("tot1").value) - moeda2float($("tot2").value));

    var saldoGeral = moeda2float($("tot1").value) - moeda2float($("tot2").value);

    if(saldoGeral > 0){
        $("visor").innerHTML = "À DEVOLVER:";
    }else{
        if(saldoGeral < 0){
            saldoGeral = (saldoGeral * (-1));
            $("visor").innerHTML = "À RECEBER:";
        }else{
            $("visor").innerHTML = "SALDO:";
        }
    }

    $("saldoTotal").value = float2moeda(saldoGeral);
}

function atualizarResumo(){
    $("vd2").value = $("totalDiarias").value;
    $("vd3").value = float2moeda(moeda2float($("vd1").value) - moeda2float($("vd2").value));

    $("va2").value = $("totalR").value;
    $("va3").value = float2moeda(moeda2float($("va1").value) - moeda2float($("va2").value));

    $("tot2").value = float2moeda(moeda2float($("vd2").value) + moeda2float($("va2").value));
    $("tot3").value = float2moeda(moeda2float($("tot1").value) - moeda2float($("tot2").value));

    var saldoGeral = moeda2float($("tot1").value) - moeda2float($("tot2").value);

    if(saldoGeral > 0){
        $("visor").innerHTML = "À DEVOLVER:";
    }else{
        if(saldoGeral < 0){
            saldoGeral = (saldoGeral * (-1));
            $("visor").innerHTML = "À RECEBER:";
        }else{
            $("visor").innerHTML = "SALDO:";
        }
    }

    $("saldoTotal").value = float2moeda(saldoGeral);
}