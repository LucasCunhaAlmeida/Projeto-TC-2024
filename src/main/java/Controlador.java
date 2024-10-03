

import java.io.File;
import java.util.ArrayList;

public class Controlador {

    /**
     * Essa classe serve para gerenciar todas as funcionalidades
     * referentes a leitura, conversão e gravação
     * em arquivo .jff(.xml), para realizar a conversão de um
     * AFN em AFD
     * @param file Arquivo que foi escolhido e que será lido e convertido
     * para um AFD, caso o arquivo selecionado seja um AFN
     */
    public Controlador(File file) {

        //ArrayList's que serão lidas e carregadas pelos métodos
        ArrayList<String> alfabeto = new ArrayList<String>();

        ArrayList<Estado> lstEstadosInicial = new ArrayList<>();
        ArrayList<Transicao> lstTransicoesInicial = new ArrayList<>();

        ArrayList<EstadoAFN> lstEstadosFinal = new ArrayList<>();
        ArrayList<Transicao> lstTransicoesFInal = new ArrayList<>();

        //Leitura do arquivo .xml escolhido, carregando os dados nas listas iniciais
        LeitorXML leitor = new LeitorXML(lstEstadosInicial, lstTransicoesInicial, alfabeto);

        // Caminho do arquivo selecionado na InterfaceGrafica
        leitor.lerArquivo(file.getAbsolutePath());


        Conversor conversor = new Conversor(lstEstadosInicial, lstTransicoesInicial);

//        conversor.gerarAFN(lstEstadosFinal, lstTransicoesFinal);	// Falta fazer
//
        //Criação do arquivo .xml, usando as listas finais geradas pela conversão do AFN
//        leitor.gravarArquivo(lstEstadosFinal, lstTransicoesFInal, alfabeto);	// Falta fazer


        //PARTE GRÁFICA NO CONSOLE
        System.out.println("\n\n -----Todos os Estados-----\n");

        for(Estado e : lstEstadosInicial) {
            if(e.isInicial() && !e.isFim())
                System.out.println("->" + e.getNome());
            else if(e.isInicial() && e.isFim()){
                System.out.println("->" + e.getNome() + "*");
            } else {
                if(e.isFim())
                    System.out.println(e.getNome() + "*");
                else
                    System.out.println(e.getNome());
            }
        }

        System.out.println("\n\n -----Todas as Transições-----\n");

        for(Transicao t : lstTransicoesInicial) {
            System.out.println(" ." + t.getDe().getNome() + " - " + t.getSimbolo() + " -> " + t.getPara().getNome());
        }

        System.out.println("\n\n -----Transições por estado-----");

        for(Estado e : lstEstadosInicial) {
            System.out.println("\n" + e.getNome() + ": " + e.getLstTransicoes().size() + " transições");
            for(Transicao t : e.getLstTransicoes()) {
                System.out.println(" ." + t.getDe().getNome() + " - " + t.getSimbolo() + " -> " + t.getPara().getNome());
            }
        }

    }

}
