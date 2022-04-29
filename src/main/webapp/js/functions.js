function moeda2float(moeda){ 
    moeda = moeda.replace(".","");
    moeda = moeda.replace(",",".");
    if(moeda.length === 0)
        return 0;
    else
        return parseFloat(moeda);
}

function float2moeda(num) {
    x = 0;

    if(num<0) {
        num = Math.abs(num);
        x = 1;
    }

    if(isNaN(num)) num = "0";

    cents = Math.floor((num*100+0.5)%100);
    num = Math.floor((num*100+0.5)/100).toString();

    if(cents < 10) cents = "0" + cents;

    for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
        num = num.substring(0,num.length-(4*i+3))+'.'+num.substring(num.length-(4*i+3));

    ret = num + ',' + cents;

    if (x == 1) ret = ' - ' + ret;

    return ret;
}

function ValidarDATA(objTextBox, separador){

    if (objTextBox.value.length == 10){
        var dia = objTextBox.value.substring(0, 2);
        var sep1 = objTextBox.value.substring(2, 3);
        var mes = objTextBox.value.substring(3, 5);
        var sep2 = objTextBox.value.substring(5, 6);
        var ano = objTextBox.value.substring(6, 10);
        var alteracao = false;
        if (ano < 2000){
            ano = '2000';
            alteracao = true;
        }
        if (dia == 0){
            dia = '01';
            alteracao = true;
        }
        if (mes == 0){
            mes = '01';
            alteracao = true;
        }
        if (mes > 12){
            mes = '12';
            alteracao = true;
        }
        if (((mes == 1) || (mes == 3) || (mes == 5) || (mes == 7) || (mes == 8) || (mes == 10) || (mes == 12))
            && (dia > 31)){
            dia = '31';
            alteracao = true;
        }
        if (((mes == 4) || (mes == 6) || (mes == 9) || (mes == 11))
            && (dia > 30)){
            dia = '30';
            alteracao = true;
        }
        if (mes == 2){
            if (((ano%4) == 0) && (dia > 29)){
                dia = '29';
            }
            if (((ano%4) != 0) && (dia > 28)){
                dia = '28';
                alteracao = true;
            }
        }
        if (sep1 != separador){
            sep1 = separador;
            alteracao = true;
        }
        if (sep2 != separador){
            sep2 = separador;
            alteracao = true;
        }
        if (alteracao == true){
            objTextBox.value = dia + sep1 + mes + sep2 + ano;
            alert("A data informada foi ajustada.");
        }
    }
}

function ValidarHORA(objTextBox, separador){
    if (objTextBox.value.length == 5){
        var hora = objTextBox.value.substring(0, 2);
        var sep = objTextBox.value.substring(2, 3);
        var min = objTextBox.value.substring(3, 5);

        var alteracao = false;
        if (hora > 23){
            hora = '23';
            alteracao = true;
        }
        if (min > 59){
            min = '59';
            alteracao = true;
        }
        if (sep != separador){
            sep = separador;
            alteracao = true;
        }
        if (alteracao == true){
            objTextBox.value = hora + sep + min;
            alert("A hora informada foi ajustada.")
        }
    }
}

function selecionar_tudo(nome){
    var itens = document.getElementsByName(nome);
    var itemCheck = document.getElementsByName(nome+"_check");
    for (i=0;i<itens.length;i++){
        if(itemCheck[0].checked == true)
            itens[i].checked=true;
        else
            itens[i].checked=false
    }
}

function existeSelecionado(nome){
    var itens = document.getElementsByName(nome);

    for (i=0;i<itens.length;i++){
        if(itens[i].checked == true){
            return true;
        }
    }
    return false;
}
