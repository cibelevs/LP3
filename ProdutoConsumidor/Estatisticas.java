package ProdutoConsumidor;

public class Estatisticas {
    private int total;
    private int totalWeb;
    private int totalApi;
    private int totalMobile;


    public void verificaTipo(String nome){
        switch (nome) {
            case "API": totalApi++; break;
            case "Mobile": totalMobile++; break;
            case "Web": totalWeb++; break;
            default:
                throw new AssertionError();
        }
    }
    

    public void registraGeral(){
        total++;
    }

    public void imprimeStats(){
        System.out.println("#########################");
        System.out.println("Total geral: " + total);
        System.out.println("Total Api: " + totalApi);
        System.out.println("Total Web: " + totalWeb);
        System.out.println("Total Mobile: " + totalMobile);
        System.out.println("#########################");
    }

}
