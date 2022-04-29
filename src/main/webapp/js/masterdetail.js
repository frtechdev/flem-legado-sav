var browser = navigator.appName; 
var listTables = new ListTables();
var solicitacaoOuPrestacao = "s";

function Column(id, text, size, align, line, idObj){
    this.id = id;
    this.text = text;
    this.size = size;
    this.align = align;
    this.idObj = idObj;
    if(align.length > 0){
        this.align = "text-align:" + align + ";";
    }
    line.addColumn(this);
}

function Line(id, text, table){
    
    this.id = id;
    this.text = text;
    this.columns = new Array();
    
    this.getColumnById = function(id){     
        for(i in this.columns){
            if(this.columns[i].id == id){
                return this.columns[i];
            }
        }							
    }
    
    this.addColumn =  function(column){
        this.columns.push(column);
    }      
    
    this.url = function() {
        var link = document.createElement('a');

        if(table.id == "trecho"){
            link.setAttribute('id', "dinamic_link_trecho" + id);
        }else if(table.id == "gasto"){
            link.setAttribute('id', "dinamic_link_gasto" + id);
        }else{
            link.setAttribute('id', "dinamic_link" + id);
        }
        link.setAttribute('class', 'contrast');
        link.setAttribute('href', 'javascript:remove(\'' + id +'\',\'' + table.id + '\')');
        
        var texto = document.createTextNode('Remover');
        
        link.appendChild(texto);
        return link;
    }
    
    table.addLine(this);
}

function remove(lineId, tableId){
    if(tableId == "trecho"){
        contLinhasTrecho--;
    }else if(tableId == "gasto") {
        contLinhasGasto--;
    }else{
        contLinhas--;
    }
    var table = listTables.getTableById(tableId);
    var line = table.getLineById(lineId);
    table.removeLine(line);

    if(tableId == "trecho"){
        if(solicitacaoOuPrestacao == "s"){
            setTotalDiariasSomadasTrecho();
        }else{
            setTotalDiariasSomadasTrechoPrestacao();
        }
    }else if(tableId == "gasto") {
        atualizarTotalGastos();
    }
}

function Table(id){
    this.id = id; 
    this.lines = new Array();
    
    listTables.addTable(this);
    
    this.getLineById = function(id){
        for(i in this.lines){
            if(this.lines[i].id == id){
                return this.lines[i];
            }
        }							
    }
    
    this.addLine =  function(line){    
        line.table = this;
        this.lines.push(line);
    }
    
    this.removeLine = function(line){
        
        for(i in this.lines){	
            if(this.lines[i].id == line.id){
                this.lines.splice(i, 1);
            }
        }
        renderTable(this);
    }
}

function ListTables(){    							
    this.tables = new Array();
    this.addTable = function(table){        
        this.tables.push(table);
    }
    
    this.getTableById = function(id){                            
        for(i in this.tables){
            if(this.tables[i].id == id){                                    
                return this.tables[i];
            }
        }							
    }
}

//this function depends of DWR importing 
function renderTable(table){
    DWRUtil.removeAllRows(table.id);
    
    var cellFuncs = [ 
    function(data) {
        return data;
    }
    ];
    
    for(i in table.lines){
        
        var line = table.lines[i];
        var values = new Array();
        
        for(j in line.columns){
            var column = line.columns[j];

            values.push(""+(column.id == "descGasto[]" ? "<input name='idGasto[]' id='idGasto[]'  type='hidden' value='"+column.idObj+"'/>  " : "")+
                "<input name='" + column.id + "' id='" + column.id +
                "' value='" + column.text + "' size='" + column.size +
                "' readonly style='border-style:none; "+ column.align +"' "+
                (column.id == "vlrGasto[]" ? " class='real'" +
                    "onclick=\"javascript:{ this.readOnly = false; this.style.borderStyle = 'solid'; }\" "+
                    "onblur=\"javascript:{ this.readOnly = true; this.style.borderStyle = 'none'; atualizarTotalGastos(); }\" ": "")+" />");
        }


        values.push(line.url);
        
        DWRUtil.addRows(table.id, cellFuncs, values, {
            escapeHtml:false
        },

        {
            rowCreator : function(options) {
                var tr = document.createElement("tr");
                    
                if(browser == 'Microsoft Internet Explorer') {
                    tr.id = line.id;
                }
                else {
                    tr.setAttribute('id', line.id);
                }
                    
                return tr;
                    
            }//fim rowCreator
        })//fim addRows
    }

    //remove os links 'remover' anteriores
    if(table.id == "trecho"){
        for (var i=1; i<contLinhasTrecho; i++){
            document.getElementById("dinamic_link_trecho" + i).innerHTML = "";
        }
        if (contLinhasTrecho > 0) {
            document.getElementById("dinamic_link_trecho" + contLinhasTrecho).innerHTML = "Remover";
        }
    }else if(table.id == "gasto"){
        for (var i=1; i<contLinhasGasto; i++){
            document.getElementById("dinamic_link_gasto" + i).innerHTML = "";
        }
        if (contLinhasGasto > 0) {
            document.getElementById("dinamic_link_gasto" + contLinhasGasto).innerHTML = "Remover";
        }
        var campos = document.getElementsByName("vlrGasto[]");
        for(var i=0; i<campos.length; i++) {
            jQuery(campos[i]).maskMoney({
                symbol: "",
                decimal: ",",
                thousands: "."
            });
        }
    }else{
        for (var i=1; i<contLinhas; i++){
            document.getElementById("dinamic_link" + i).innerHTML = "";
        }
        if (contLinhas > 0) {
            document.getElementById("dinamic_link" + contLinhas).innerHTML = "Remover";
        }
    }

   
}