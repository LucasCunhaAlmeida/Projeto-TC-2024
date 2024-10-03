import java.util.ArrayList;

public class EstadoAFN extends Estado{

    private ArrayList<Integer> label;

    public EstadoAFN(Estado estado) {
        super(estado);
        this.label = new ArrayList<Integer>();
        // TODO Auto-generated constructor stub

    }

    public ArrayList<Integer> getLabel() {
        return label;
    }

    public void setLabel(ArrayList<Integer> label) {
        this.label = label;
    }

}