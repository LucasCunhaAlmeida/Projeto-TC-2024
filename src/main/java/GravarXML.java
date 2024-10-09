import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;

public class GravarXML {

    private Document documento;
    private Element automatonElemento;
    public GravarXML () throws ParserConfigurationException {
        // Cria a estrutura do documento XML
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        documento = documentBuilder.newDocument();

        // Elemento root "a raiz do  XML" <structure>
        Element raiz = documento.createElement("structure");
        documento.appendChild(raiz);

        documento.createComment("Created with JFLAP 7.1.");

        // Adiciona o tipo do autômato <type>fa</type>
        Element tipo = documento.createElement("type");
        tipo.appendChild(documento.createTextNode("fa"));
        raiz.appendChild(tipo);

        // Cria o elemento <automaton> que conterá os estados e transições
        automatonElemento = documento.createElement("automaton");
        raiz.appendChild(automatonElemento);

        comentarioDaListaDeEstados();
    }

    public void comentarioDaListaDeEstados(){
        // Adiciona o comentário antes dos estados
        automatonElemento.appendChild(documento.createComment("!--The list of states.--"));
    }

    public void comentarioDaListaDeTransicoes(){
        // Adiciona o comentário antes das transições
        automatonElemento.appendChild(documento.createComment("!--The list of transitions.--"));
    }

    // Método para adicionar estado com rótulos
    public boolean adicionarEstado(int id, String nome, double x, double y, ArrayList<Integer> labels,
                                   boolean isInitial, boolean isFinal) {
        Element estado = documento.createElement("state");
        estado.setAttribute("id", String.valueOf(id));
        estado.setAttribute("name", nome);

        Element posX = documento.createElement("x");
        posX.appendChild(documento.createTextNode(String.valueOf(x)));
        estado.appendChild(posX);

        Element posY = documento.createElement("y");
        posY.appendChild(documento.createTextNode(String.valueOf(y)));
        estado.appendChild(posY);

        // Adiciona a tag <label> com os valores do ArrayList
        if (labels != null && !labels.isEmpty()) {
            Element label = documento.createElement("label");
            StringBuilder labelValue = new StringBuilder();
            for (int i = 0; i < labels.size(); i++) {
                labelValue.append(labels.get(i));
                if (i < labels.size() - 1) {
                    labelValue.append(","); // Adiciona vírgula entre os valores
                }
            }
            label.appendChild(documento.createTextNode(labelValue.toString()));
            estado.appendChild(label);
        }

        // Adiciona a tag <initial> se o estado for inicial
        if (isInitial) {
            Element initial = documento.createElement("initial");
            estado.appendChild(initial);
        }

        // Adiciona a tag <final> se o estado for final
        if (isFinal) {
            Element finalElement = documento.createElement("final");
            estado.appendChild(finalElement);
        }

        // Adiciona o estado ao elemento automaton
        automatonElemento.appendChild(estado);

        return true;
    }

    // Método para adicionar uma transição
    public void adicionarTransicao(EstadoAFN de, EstadoAFN para, String simbolo) {
        Element transition = documento.createElement("transition");

        Element from = documento.createElement("from");
        from.appendChild(documento.createTextNode(String.valueOf(de)));
        transition.appendChild(from);

        Element to = documento.createElement("to");
        to.appendChild(documento.createTextNode(String.valueOf(para)));
        transition.appendChild(to);

        Element read = documento.createElement("read");
        read.appendChild(documento.createTextNode(simbolo));
        transition.appendChild(read);

        // Adiciona a transição ao elemento automaton
        automatonElemento.appendChild(transition);
    }

    // Método para salvar o XML em arquivo
    public void salvarXML(String nomeArquivo) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource domSource = new DOMSource(documento);
        StreamResult streamResult = new StreamResult(new File(nomeArquivo));

        transformer.transform(domSource, streamResult);
    }
}
