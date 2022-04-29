package br.org.flem.sav.negocio; 

public enum Natureza {

    SERVICO("Serviço"),
    TREINAMENTO("Treinamento"),
    SERVICO_TREINAMENTO("Serviço e Treinamento");
    
    private String nome;
    
    Natureza(String nome){
        this.nome= nome;
    }
    
    public String getNome() {
        return nome;
    }
    
    public String getCodigo(){
        return this.name();
    }
}
 
