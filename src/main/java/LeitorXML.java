import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.ArrayList;

public class LeitorXML {

    /**
     * Está Classe LeitorXML, serve para ler os arquivos xml e
     * armazenar os dados desse automato, para uma futura mani-
     * pulação.
     */

    ArrayList<Estado> lstEstados = new ArrayList<>();
    ArrayList<Transicao> lstTrasicoes = new ArrayList<>();

    public void lerArquivo(String endereco) {
        // Verificações iniciais
        if (!verificarTipoXML(endereco) || !verificarSeXML(endereco) || !verificarXMLAutomato(endereco)) {
            return;
        }

        try {
            // Inicializa o parser
            Document doc = inicializarParser(endereco);

            // Normaliza a estrutura do XML
            normalizarDocumento(doc);

            // Lê e processa os estados
            lerEstados(doc);

            // Lê e processa as transições
            lerTransicoes(doc);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para inicializar o parser do XML
    private Document inicializarParser(String endereco) throws Exception {
        File arquivoXML = new File(endereco);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return dBuilder.parse(arquivoXML);
    }

    // Método para normalizar o documento XML
    private void normalizarDocumento(Document doc) {
        doc.getDocumentElement().normalize();
        System.out.println("Elemento raiz: " + doc.getDocumentElement().getNodeName());
    }

    // Método para ler e processar os estados
    private void lerEstados(Document doc) {
        NodeList listaDeEstados = doc.getElementsByTagName("state");

        for (int i = 0; i < listaDeEstados.getLength(); i++) {
            Node no = listaDeEstados.item(i);

            if (no.getNodeType() == Node.ELEMENT_NODE) {
                Element elemento = (Element) no;
                String id = elemento.getAttribute("id");
                String nome = elemento.getAttribute("name");
                String x = elemento.getElementsByTagName("x").item(0).getTextContent();
                String y = elemento.getElementsByTagName("y").item(0).getTextContent();
                // Verifica se existe o elemento "final" dentro deste estado
                boolean fim = elemento.getElementsByTagName("final").getLength() > 0;
                boolean inicial = elemento.getElementsByTagName("initial").getLength() > 0;


                System.out.print("Fim? " + fim + " Inicio? " + inicial + " ");
                Estado estado = new Estado(nome, Integer.parseInt(id), Double.parseDouble(x),
                        Double.parseDouble(y), fim,inicial);
                lstEstados.add(estado);

                System.out.println("State id: " + id + ", name: " + nome + ", x: " + x + ", y: " + y);
            }
        }
    }

    // Método para ler e processar as transições
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
                lstTrasicoes.add(transicao);

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
    public boolean verificarTipoXML(String endereco) {
        return endereco.toLowerCase().endsWith(".xml") || endereco.toLowerCase().endsWith(".jff");
    }

    /**
     * Esté método verifica se o xml passado está respeitando as regras da linguagem.
     * Por exemplo, se há erros de sintaxe ou tags incorretamente aninhadas e etc.
     *
     * @param endereco Esse parametro passa onde o arquivo está na máquina.
     * @return se o xml for bem formado, retorna true; senão, retorna false.
     */
    public boolean verificarSeXML(String endereco) {
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
            return true; // Se não houve exceções, o arquivo é um XML válido
        } catch (Exception e) {
            // Se alguma exceção foi lançada, o arquivo não é um XML válido
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
    public static boolean verificarXMLAutomato(String endereco) {
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

    public Estado procurarEstado(int Id) {

        for (Estado e : lstEstados) {
            if (e.getId() == Id) {
                return e;
            }
        }
        return null;
    }

}