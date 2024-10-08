import java.util.ArrayList;

public class Conversor {

    private ArrayList<Estado> lstEstadosInicial = new ArrayList<Estado>();
    private ArrayList<Transicao> lstTransicoesInicial = new ArrayList<Transicao>();
    private ArrayList<String> alfabeto = new ArrayList<String>();
    private ArrayList<Estado> finais = new ArrayList<Estado>();
    private ArrayList<EstadoAFN> lstEstadosProcessados = new ArrayList<EstadoAFN>();
    private Estado inicial;

    /**
     * Construtor que recebe os dois ArrayList, um para os estados
     * e o outro para transições, para
     * realizar os restantes das operações.
     */
    public Conversor(ArrayList<Estado> estados, ArrayList<Transicao> transicoes, ArrayList<String> alfabeto) {
        this.lstEstadosInicial = estados;
        this.lstTransicoesInicial = transicoes;
        this.alfabeto = alfabeto;
    }

    /**
     * gerarAFN() serve para converter as listas iniciais para AFD.
     * Esse AFD será armazenadao nas listas finais.
     * Essas listas finais estão conectadas por referência com as listas finais da classe Controlador
     */
    public void gerarAFN(ArrayList<EstadoAFN> lstEstadosFinal, ArrayList<Transicao> lstTransicoesFinal) {

        getEstadoInicialEFinais();

        EstadoAFN novoInicial = new EstadoAFN(inicial);

        // Chama o método recursivo para rastrear todos os estados alcançáveis com transições vazias.
        pegarNovoInicial(novoInicial, inicial);

        // Antes de colocar no ArrayList de estados já processados, modificamos o atributo inicial,
        // pois o inicial do AFN continua sendo inicial no AFD.
        novoInicial.setInicial(true);
        lstEstadosProcessados.add(novoInicial);

        /**
         * Para cada estado que está guardado primeiramente no novo inicial,
         * temos que ver para onde ele está indo com cada simbolo do alfabeto.
         */
        for (EstadoAFN e: lstEstadosProcessados){
            for (String a: alfabeto){
                estadosAlcancaveisComSimbolos(e,a);
            }
        }

        System.out.println("Alfabeto no conversor: ");
        for (String a: alfabeto){
            System.out.println(a);
        }

        System.out.println("------------------- AFD Convertido ------------------");
        for (EstadoAFN estadoAFN : lstEstadosProcessados) {
            System.out.println("Labels desse novo estado\n");
            for (int label : estadoAFN.getLabel()) {
                System.out.println(label);
            }
        }
    }

    /**
     * Este Método que serve para encontrar o estado inicial e os estados finais do autômato
     */
    private void getEstadoInicialEFinais() {
        for (Estado e: lstEstadosInicial){
            if (e.isInicial()) {
                inicial = e;
            }

            if(e.isFim()){
                finais.add(e);
            }

        }
    }

    //Brincadeira boa
    /*private EstadoAFN pegarNovoInicial(Estado EstadoAnalisar){// Isso eu posso passar o inicial global

        // Criando um EstadoAFN para guardar o label do novo inicial.
        EstadoAFN estado = new EstadoAFN(inicial);

        // O antigo inicial sempre vai estar no novo inicial.
        estado.setLabel(inicial.getId());

        // Aqui vamos percorrer o ArrayList de transições que está no objeto "inicial".
        for(Transicao t: inicial.getLstTransicoes()){

            if (t.getSimbolo() == "") {

                estado.setLabel(t.getPara().getId());
                pegarNovoInicial(t.getPara());
            }
        }

        System.out.println("Novo inical: ");

        for (int t: estado.getLabel()){
            System.out.println(t);
        }

        return estado;
    }*/

    private void pegarNovoInicial(EstadoAFN estadoAtual, Estado estadoAnalisar) {

        // Verifica se o estado já foi visitado
        if (estadoAtual.jaVisitado(estadoAnalisar.getId())) {
            return;
        }

        estadoAtual.setLabel(estadoAnalisar.getId());

        // Percorre todas as transições do estado atual
        for (Transicao t : estadoAnalisar.getLstTransicoes()) {

            // Verifica se a transição é uma transição vazia.
            if (t.getSimbolo() == "") {
                // Chamada recursiva para o estado destino
                pegarNovoInicial(estadoAtual, t.getPara());
            }
        }

    }

    /**
     *
     *
     * @param estadoAtual É o estado que queremos verificar as transições.
     * @param simbolo É o simbolo da transição que queremos saber para qual
     *               estado está indo.
     */

    // Está errado!!!
    private void estadosAlcancaveisComSimbolos(EstadoAFN estadoAtual, String simbolo){

        for (Transicao t: estadoAtual.getLstTransicoes()){
            System.out.println("Processando transição do estado: " + estadoAtual.getId() + " com o símbolo: " + t.getSimbolo());
            // Verificando se essa transição atual da repetição é a mesma do símbolo dos parâmetros.
            if (t.getSimbolo().equals(simbolo)){
                System.out.println("Comparando símbolos: " + t.getSimbolo() + " com " + simbolo);

                Estado estadoAux = t.getPara();

                EstadoAFN estadoAFNAux = new EstadoAFN(estadoAux);
                if (!verificarSeJaExisteEstado(estadoAFNAux)){
                    System.out.println("Verificando estado com label: " + estadoAFNAux.getLabel());

                    lstEstadosProcessados.add(estadoAFNAux);
                    estadosAlcancaveisComSimbolos(estadoAFNAux, simbolo);
                }
            }

        }

    }

    /**
     * Este método serve para verificar se já existe algum estado igual ao que
     * precisa gerar. Para não existir duplicatas desnecessarias.
     *
     * @param estadoAnalisar O estado que eu quero ver se já existe.
     *
     * @return Se existir algum estado igual retorna verdadeiro, senão retorna
     * falso.
     */
    private boolean verificarSeJaExisteEstado(EstadoAFN estadoAnalisar){

        // Percorre a lista de estados já criados.
        for (EstadoAFN e: lstEstadosProcessados){
            // Compara pegando os dois ArraysList que estão dentro dos objetos de uma só vez.
            if (e.getLabel().equals(estadoAnalisar.getLabel())){
                return true;
            }
        }
        return false;
    }

    /**
     * Este método serve para gerar os novos finais do AFD convertido.
     */
    public void gerarNovosFinais() {
        for (EstadoAFN estadoAFN : lstEstadosProcessados) {
            for (Estado estado : finais) {
                // Verifica se o estado final original foi visitado por este estado AFN
                if (estadoAFN.jaVisitado(estado.getId())) {
                    // Marca o estado AFN como final se ele contém o id de um estado final original
                    estadoAFN.setFim(true);
                }
            }
        }
    }

    /**
     * Esse método precisa ser alimentado com o ArrayList de estados.
     */
    public void deixarAFDCompleto() {
        // Criar o estado consumidor
        Estado estadoConsumidor = new Estado("Consumidor", -1, 300, 300, false, false);

        // Adicionar o estado consumidor à lista de estados
        lstEstadosInicial.add(estadoConsumidor);

        // Iterar sobre todos os estados
        for (Estado estado : lstEstadosInicial) {
            // Para cada estado, verificamos se ele possui transições para todos os símbolos do alfabeto
            for (String simbolo : alfabeto) {
                boolean temTransicao = false;

                // Verificar se já existe uma transição para o símbolo atual
                for (Transicao transicao : estado.getLstTransicoes()) {
                    if (transicao.getSimbolo().equalsIgnoreCase(simbolo)) {
                        temTransicao = true;
                        break; // Já tem transição para o símbolo, não precisa adicionar
                    }
                }

                // Se não houver transição para o símbolo atual, criamos uma nova transição para o estado consumidor
                if (!temTransicao) {
                    Transicao novaTransicao = new Transicao(estado, estadoConsumidor, simbolo);
                    estado.getLstTransicoes().add(novaTransicao);
                }
            }
        }

        // Agora colocamos as transições do alfabeto para o proprio consumidor.
        for (String simbolo : alfabeto) {

            Transicao transicaoParaConsumidor = new Transicao(estadoConsumidor, estadoConsumidor, simbolo);

            // Adiciona essa transição ao ArrayList de transições do estado consumidor.
            estadoConsumidor.getLstTransicoes().add(transicaoParaConsumidor);
        }

    }

}