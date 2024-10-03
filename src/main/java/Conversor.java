

import java.util.ArrayList;

public class Conversor {

    ArrayList<Estado> lstEstadosInicial = new ArrayList<>();
    ArrayList<Transicao> lstTransicoesInicial = new ArrayList<>();

    /**
     * Construtor que recebe os dois ArrayList, um para os estados
     * e o outro para transições, para
     * realizar os restantes das operações.
     */
    public Conversor(ArrayList<Estado> estados, ArrayList<Transicao> transicoes) {
        this.lstEstadosInicial = estados;
        this.lstTransicoesInicial = transicoes;
    }

    /**
     * gerarAFN() serve para converter as listas iniciais para AFD.
     * Esse AFD será armazenadao nas listas finais.
     * Essas listas finais estão conectadas por referência com as listas finais da classe Controlador
     */
    public void gerarAFN(ArrayList<EstadoAFN> lstEstadosFinal, ArrayList<Transicao> lstTransicoesFinal) {
        // TODO Auto-generated method stub

    }

}

