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
    // Olá
    public void lerArquivo(String endereco){

        /**
         * Abaixo estão algumas verificações necessárias antes de
         * prosseguir com a leitura do xml. Se caso alguma chamada
         * der um retorno do tipo false, retornaremos.
         */
        if (!verificarTipoXML(endereco)){
            // Não é um arquivo xml
            return;
        } else if (!verificarSeXML(endereco)) {
            return;
        } else if (!verificarXMLAutomato(endereco)) {
            // Verificar se todos os estados tem transições, se tem um inicial e finais?
            return;
        }

        try {
            // Inicializa o parser
            File arquivoXML = new File("automato.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(arquivoXML);

            // Normaliza a estrutura do XML (opcional, mas recomendado)
            doc.getDocumentElement().normalize();

            // Imprime o elemento raiz
            System.out.println("Elemento raiz: " + doc.getDocumentElement().getNodeName());

            // Lê as tags "state"
            NodeList listaDeEstados = doc.getElementsByTagName("state");

            for (int i = 0; i < listaDeEstados.getLength(); i++) {
                Node no = listaDeEstados.item(i);

                if (no.getNodeType() == Node.ELEMENT_NODE) {
                    Element elemento = (Element) no;
                    String id = elemento.getAttribute("id");
                    String name = elemento.getAttribute("name");

                    // Lê os elementos filhos de cada "state" (x e y)
                    String x = elemento.getElementsByTagName("x").item(0).getTextContent();
                    String y = elemento.getElementsByTagName("y").item(0).getTextContent();

                    System.out.println("State id: " + id + ", name: " + name + ", x: " + x + ", y: " + y);
                }
            }

            // Lê as tags "transition"
            NodeList listaDeTransicoes = doc.getElementsByTagName("transition");

            for (int i = 0; i < listaDeTransicoes.getLength(); i++) {
                Node no = listaDeTransicoes.item(i);

                if (no.getNodeType() == Node.ELEMENT_NODE) {
                    Element elemento = (Element) no;
                    String from = elemento.getElementsByTagName("from").item(0).getTextContent();
                    String to = elemento.getElementsByTagName("to").item(0).getTextContent();
                    String read = elemento.getElementsByTagName("read").item(0).getTextContent();

                    System.out.println("Transition from: " + from + " to: " + to + " reading: " + read);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *  Esté método somente verifica se é uma extensão xml ou jff. Apenas pegando o final
     *  do endereço e vendo se bate com algum dos nomes da extensões aceitas.
     *
     *  @param endereco Esse parametro passa onde o arquivo está na máquina.
     *  @return Se o arquivo tiver a extensão .xml ou .jff, retorna true; senão, retorna false.
    */
    public boolean verificarTipoXML(String endereco){
        return endereco.toLowerCase().endsWith(".xml") || endereco.toLowerCase().endsWith(".jff");
    }

    /**
     *  Esté método verifica se o xml passado está respeitando as regras da linguagem.
     *  Por exemplo, se há erros de sintaxe ou tags incorretamente aninhadas e etc.
     *
     *  @param endereco Esse parametro passa onde o arquivo está na máquina.
     *  @return se o xml for bem formado, retorna true; senão, retorna false.
     */
    public boolean verificarSeXML(String endereco){
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
}
