import java.util.ArrayList;

public class EstadoAFN extends Estado{

    private ArrayList<Integer> label;

    public EstadoAFN(Estado estado) {
        super(estado);
        this.label = new ArrayList<>();
    }

    public ArrayList<Integer> getLabel() {
        return label;
    }

    public void setLabel(ArrayList<Integer> label) {
        this.label = label;
    }

    public void setLabel(int id) {
        // Adiciona o ID ao label apenas se ele ainda não estiver presente para evitar duplicações.
        if (!label.contains(id)) {
            label.add(id);
        }
    }

    // Basicamente esse método só vai checar se existe já algum id igual no ArrayList.
    public boolean jaVisitado(int id) {
        return label.contains(id);
    }
}