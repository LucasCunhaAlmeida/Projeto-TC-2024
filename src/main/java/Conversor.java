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

        lstEstadosFinal.add(novoInicial);
        pegarTransicoesDasLabels(novoInicial);
        System.out.println("\n\nTransições desse novo Estado:\n\n");
        testeTransicoes(novoInicial);
        System.out.println("-----------------------------------------------------------");
        /**
         * Para cada estado que está guardado primeiramente no novo inicial,
         * temos que ver para onde ele está indo com cada simbolo do alfabeto.
         */
        // Se for a primeira vez que estamos olhando as transições, então não precisa verificar o vazio.
        // Usando uma fila para processar estados sequencialmente

        boolean primeiraVez = true;

        for (int i = 0; i < lstEstadosProcessados.size(); i++){
            EstadoAFN estadoAFN = lstEstadosProcessados.get(i);
            for (int label : estadoAFN.getLabel()) {
                for (String simbolo : alfabeto) {
                    if (primeiraVez && simbolo.equals("")) {
                        continue; // pula o símbolo vazio na primeira vez
                    } 
                    
                    estadosAlcancaveisComSimbolos(estadoAFN, simbolo);                                   
                }
            }
            primeiraVez = false;
        }


        System.out.println("\n\nEstados já convertidos\n\n");
    }


    /**
     * Este Método que serve para encontrar o estado inicial e os estados finais do autômato
     */
    private void getEstadoInicialEFinais() {
        // Para saber se existe um inicial entre os estados.
        boolean existeInicial = false;
        for (Estado e: lstEstadosInicial){
            if (e.isInicial()) {
                inicial = e;
                existeInicial = true;
            }

            if(e.isFim()){
                finais.add(e);
            }

        }

        if (!existeInicial){
            inicial = lstEstadosInicial.get(0);
            System.out.println("Não existe incial, mas esse vai ser o inicial agora: ");
            System.out.println("ID: " + inicial.getId());
        }
    }

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

    private void pegarTransicoesDasLabels(EstadoAFN estadoAFN){

        for (int label: estadoAFN.getLabel()){
            // Peguei a lista de labels.
            for (Estado estado: lstEstadosInicial){
                if (estado.getId() == label){
                    // Achamos o estado correspondente ao label.
                    for (Transicao transicao: estado.getLstTransicoes()){
                        // Pegamos todas essas transições deste estado e colocamos no estadoAFN.
                        estadoAFN.getLstTransicoes().add(transicao);
                    }
                }
            }
        }

    }

    private void estadosAlcancaveisComSimbolos(EstadoAFN estadoAtual, String simbolo) {
        // Criando um novo EstadoAFN para armazenar os estados alcançados
        EstadoAFN novoEstadoAFN = new EstadoAFN(new Estado("Teste", 1,300,300,false,false));

        // Iterando sobre as transições do estado atual
        for (Transicao t : estadoAtual.getLstTransicoes()) {

            // Verifica se a transição corresponde ao símbolo fornecido
            if (t.getSimbolo().equals(simbolo)) {
                
                if(!novoEstadoAFN.jaVisitado(t.getPara().getId())){
                    // Obtém o ID do estado de destino
                    int estadoDestinoId = t.getPara().getId();

                    // Adiciona o ID ao novo EstadoAFN
                    novoEstadoAFN.getLabel().add(estadoDestinoId);

                }
                
            }
        }

        if(!verificarSeJaExisteEstado(novoEstadoAFN)){
            // Após processar todas as transições, recupera as transições dos estados pelos IDs
            pegarTransicoesDasLabels(novoEstadoAFN);

            System.out.println("\n\nTransições desse novo Estado:\n\n");
            testeTransicoes(novoEstadoAFN);
            System.out.println("-----------------------------------------------------------");
            // Adiciona o novo EstadoAFN ao ArrayList lstEstadosProcessados
            lstEstadosProcessados.add(novoEstadoAFN);

            System.out.println("Qual é esse estado? " + novoEstadoAFN.getId());
            System.out.println("Novo EstadoAFN adicionado a lstEstadosProcessados com IDs: " + novoEstadoAFN.getLabel());
        }

    }

    public void testeTransicoes(EstadoAFN estadoAFN){
        for (Transicao transicao: estadoAFN.getLstTransicoes()){
            System.out.println("De: " + transicao.getDe().getId() + ", Para: " + transicao.getPara().getId() + ", Lendo: "
            + transicao.getSimbolo());
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
     * Este método serve para gerar os novos finais do AFD convertido a partir
     * dos antigos finais do AFN passado para converter !!!!!!!(Aparentemente etá correto).
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


}