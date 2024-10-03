import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class LeitorXML {

    /**
     * Está Classe LeitorXML, serve para ler os arquivos xml e
     * armazenar os dados desse automato, para uma futura mani-
     * pulação.
     */

    private ArrayList<Estado> lstEstados = new ArrayList<>();
    private ArrayList<Transicao> lstTransicoes = new ArrayList<>();
    private ArrayList<String> alfabeto = new ArrayList<String>();

    // Construtor padrão
    public LeitorXML() {

    }

    // Novo construtor para "Carregar" as listas da classe Principal
    public LeitorXML(ArrayList<Estado> lstEstados, ArrayList<Transicao> lstTransicoes, ArrayList<String> alfabeto) {
        this.lstEstados = lstEstados;
        this.lstTransicoes = lstTransicoes;
        this.alfabeto = alfabeto;
    }

    /**
     * Este método serve para chamar outros métodos necessarios para ler
     * o arquixo XML, assim como as verificações minimas para prosseguir
     * com a leitura.
     *
     * @param endereco passa o endereço do arquivo que deseja fazer a operação.
     */
    public void lerArquivo(String endereco) {
        // Verificações iniciais
        if (!verificarTipoXML(endereco) || !verificarSeXML(endereco) || !verificarXMLAutomato(endereco)) {
            return;
        }

        try {
            // Inicializa o parser.
            Document doc = inicializarParser(endereco);

            // Normaliza a estrutura do XML.
            normalizarDocumento(doc);

            // Lê e processa os estados.
            lerEstados(doc);

            // Lê e processa as transições.
            lerTransicoes(doc);

            carregarTransicoesEstado();

        } catch (Exception e) {
            e.printStackTrace();
        }

        alfabeto = verificarAlfabeto();

        System.out.println("\n\n ---------- Alfabeto ----------\n\n");
        for (String a: alfabeto){
            System.out.println(a);
        }

        System.out.println("tamanho do alfabeto: " + alfabeto.size());

        if (verificaSeAfn(alfabeto)){
            System.out.println("É um AFN");
        }else{
            // Não tem mais o que fazer, pois já é um AFD.
            System.out.println("É um AFD");
        }

    }

    // Método para inicializar o parser do XML.
    private Document inicializarParser(String endereco) throws Exception {
        File arquivoXML = new File(endereco);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return dBuilder.parse(arquivoXML);
    }

    // Método para normalizar o documento XML.
    private void normalizarDocumento(Document doc) {
        doc.getDocumentElement().normalize();
        System.out.println("Elemento raiz: " + doc.getDocumentElement().getNodeName());
    }

    // Método para ler e armazenar todos os estados e colocar no ArrayList.
    private void lerEstados(Document doc) {
        // Para pegar todos os elementos que tem a tag "state".
        NodeList listaDeEstados = doc.getElementsByTagName("state");

        // Vai se repetir até que todas as tags "state" tenham sido processadas.
        for (int i = 0; i < listaDeEstados.getLength(); i++) {

            // Coloca cada item "state" para dentro de no.
            Node no = listaDeEstados.item(i);

            // Verifica se o nó atual é um elemento XML (<tag>),
            // ignorando outros tipos de nós como texto ou atributos
            if (no.getNodeType() == Node.ELEMENT_NODE) {

                // Faz um cast do nó atual para Element, permitindo acessar
                // métodos específicos de elementos XML, como id ou name.
                Element elemento = (Element) no;

                // Pegando os atributos que estão no XML e colocando dentro de String.
                String id = elemento.getAttribute("id");
                String nome = elemento.getAttribute("name");
                String x = elemento.getElementsByTagName("x").item(0).getTextContent();
                String y = elemento.getElementsByTagName("y").item(0).getTextContent();

                // Verifica se existe o elemento "final" dentro deste estado
                boolean fim = elemento.getElementsByTagName("final").getLength() > 0;
                // Verifica se existe o elemento "inicial" dentro deste estado
                boolean inicial = elemento.getElementsByTagName("initial").getLength() > 0;


                System.out.print("Fim? " + fim + " Inicio? " + inicial + " ");

                // Pegando as variaveis que estavam no "state" dessa repetição e criando um objeto Estado
                Estado estado = new Estado(nome, Integer.parseInt(id), Double.parseDouble(x),
                        Double.parseDouble(y), fim,inicial);

                lstEstados.add(estado);

                System.out.println("State id: " + id + ", name: " + nome + ", x: " + x + ", y: " + y);
            }
        }
    }

    /**
     * Este método serve para pegar todas as transições que um determinado estado
     * alcança, colocando essas transições dentro do ArrayList da classe Estado.
     * OBS: Cada estado pode ter uma serie de transições saindo dele.
     */
    private void carregarTransicoesEstado() {

        for(Estado e : lstEstados) {
            for(Transicao t : lstTransicoes) {
                if(t.getDe().getId() == e.getId()) {
                    e.getLstTransicoes().add(t);
                }
            }
        }

    }

    /**
     * Método para ler e armazenar no ArrayList todas as transições.
     * OBS: A explicação desse método é praticamente igual ao de ler os
     * estados, o que muda é só que estamos pegando todos os elementos
     * presentes nas tags "transition".
     */
    private void lerTransicoes(Document doc) {
        NodeList listaDeTransicoes = doc.getElementsByTagName("transition");

        for (int i = 0; i < listaDeTransicoes.getLength(); i++) {
            Node no = listaDeTransicoes.item(i);

            if (no.getNodeType() == Node.ELEMENT_NODE) {
                Element elemento = (Element) no;
                String de = elemento.getElementsByTagName("from").item(0).getTextContent();
                String para = elemento.getElementsByTagName("to").item(0).getTextContent();
                String simbolo = elemento.getElementsByTagName("read").item(0).getTextContent();

                Estado deEstado = procurarEstado(Integer.parseInt(de));
                Estado paraEstado = procurarEstado(Integer.parseInt(para));

                Transicao transicao = new Transicao(deEstado, paraEstado, simbolo);

                lstTransicoes.add(transicao);

                System.out.println("Transition from: " + de + " to: " + para + " reading: " + simbolo);
            }
        }
    }

    /**
     * Esté método somente verifica se é uma extensão xml ou jff. Apenas pegando o final
     * do endereço e vendo se bate com algum dos nomes da extensões aceitas.
     *
     * @param endereco Esse parametro passa onde o arquivo está na máquina.
     * @return Se o arquivo tiver a extensão .xml ou .jff, retorna true; senão, retorna false.
     */
    private boolean verificarTipoXML(String endereco) {
        return endereco.toLowerCase().endsWith(".xml") || endereco.toLowerCase().endsWith(".jff");
    }

    /**
     * Esté método verifica se o xml passado está respeitando as regras da linguagem.
     * Por exemplo, se há erros de sintaxe ou tags incorretamente aninhadas e etc.
     *
     * @param endereco Esse parametro passa onde o arquivo está na máquina.
     * @return se o xml for bem formado, retorna true; senão, retorna false.
     */
    private boolean verificarSeXML(String endereco) {
        try {
            // Cria um arquivo do tipo File
            File arquivo = new File(endereco);
            // Tenta criar um parser XML para verificar se é válido
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

            /**
             * parse(arquivo): Esse método lê o arquivo XML e tenta convertê-lo
             * em uma estrutura de dados (Document) que o Java pode manipular.
             * Se o XML estiver bem formado, ele cria um objeto Document com
             * a árvore de elementos do XML. Caso contrário, ele lança uma
             * exceção (como SAXException).
             */
            dbFactory.newDocumentBuilder().parse(arquivo);
            return true; // Se não houve exceções, o arquivo é um XML válido.
        } catch (Exception e) {
            // Se alguma exceção foi lançada, o arquivo não é um XML válido.
            return false;
        }
    }

    /**
     * Esse método verifica se esse XML que o usuário passou é um XML
     * que contém as estruturas mínimas para retirar as informações
     * de um autômato criado no JFLAP.
     *
     * @param endereco Esse parametro passa onde o arquivo está na máquina.
     * @return se o xml tiver todas as tags de um automato, retorna true;
     * ,senão, retorna false.
     */
    private boolean verificarXMLAutomato(String endereco) {
        try {
            // Carrega o arquivo XML
            File xmlFile = new File(endereco);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            // Normaliza o documento (remove nós desnecessários)
            doc.getDocumentElement().normalize();

            // Verifica se a raiz é "structure"
            if (!doc.getDocumentElement().getNodeName().equals("structure")) {
                System.out.println("O XML não contém a raiz <structure>");
                return false;
            }

            // Verifica se o elemento <type> é "fa"
            NodeList typeList = doc.getElementsByTagName("type");
            if (typeList.getLength() == 0 || !typeList.item(0).getTextContent().equals("fa")) {
                System.out.println("O XML não contém o tipo correto (<type>fa</type>)");
                return false;
            }

            // Verifica se contém o elemento <automaton>
            NodeList automatonList = doc.getElementsByTagName("automaton");
            if (automatonList.getLength() == 0) {
                System.out.println("O XML não contém o elemento <automaton>");
                return false;
            }

            System.out.println("O XML segue o formato esperado.");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Este método serve para achar no ArrayList de estados o estado
     * que foi lido na tag "transition", a partir do Id, pois na
     * classe Transicao, o atributo "de" e "para" são do tipo Estado.
     *
     * @param Id É o identificador do Estado (cada estado tem o seu,
     * sem repetição).
     *
     * @return Retorna o estado ao qual pertence o Id.
     */
    private Estado procurarEstado(int Id) {

        for (Estado e : lstEstados) {
            if (e.getId() == Id) {
                return e;
            }
        }
        return null;
    }

    /**
     * Este método verifica qual o alfabeto do automato, percorrendo o
     * ArrayList de transições, eliminando as duplicatas para sabermos
     * exatamente qual é o alfabeto usado.
     *
     * @return Retorna um ArrayList de String que contém para cada posição um
     * simbolo que está presente no automato que queremos fazer a operação.
     */
    private ArrayList<String> verificarAlfabeto(){
        ArrayList<String> simbolos = new ArrayList<>();

        // 1° percorre o ArrayList de transicões
        for (Transicao t: lstTransicoes){
            // 2° verifica se já existe algum símbolo igual no ArrayList de simbolos
            if (!verificarDuplicata(simbolos,t.getSimbolo())){
                simbolos.add(t.getSimbolo());
            }
        }
        return simbolos;
    }

    /**
     * Este metodo verifica se já existe algum símbolo no ArrayList passado que
     * é igual ao simbolo que queremos verificar.
     *
     * @return Se já existe retorna verdadeiro, senão, retorna false.
     */
    private boolean verificarDuplicata(ArrayList<String> novaLista, String simbolo) {
        for (String s : novaLista) {
            if (s.equals(simbolo)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Este método verifica se existe dentro do ArrayList que guarda os simbolos,
     * algum simbolo "", que significa o vazio.
     *
     * @param alfabeto Esse é o ArrayList que guarda os simbolos do alfabeto.
     *
     * @return Se existir alguma transição vazia("") ou mais de uma transição com o mesmo
     * símbolo, retorna verdadeiro, pois é um AFN, senão, é um AFD e retorna falso.
     */
    private boolean verificaSeAfn(ArrayList<String> alfabeto){

        // AFN com Lambda(palavra vazia).
        for (String a: alfabeto){
            if (a.equals("")){
                // A partir daqui já sabemos que se trata de um AFN.
                return true;
            }
        }

        // Essa verificação serve para saber se é um AFN sem Lambda.
        for(String s : alfabeto) {
            for(Estado e : lstEstados) {
                int cont = 0;
                for(Transicao t : e.getLstTransicoes()) {
                    if(s.equals(t.getSimbolo())) {
                        cont++;
                    }
                    if(cont == 2){
                        //Encontramos um estado onde tem duas transições com o mesmo simbolo saindo dele
                        return true;
                    }

                }
            }
        }

        return false;

    }

}