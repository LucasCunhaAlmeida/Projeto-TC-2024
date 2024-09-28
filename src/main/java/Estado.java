import java.util.ArrayList;

public class Estado {

    // Atributos que estão no xml do automato
    private String nome;
    private int id;
    private double x, y;
    private boolean fim, inicial;

    /**
     *  Cada estado pode varias transições saindo e entrando nele.
     *  Por isso se faz necessario um ArrayList para guardar todas
     *  essas transições.
     */
    ArrayList<Transicao> lstTransicoes = new ArrayList<>();

    // Construtor passando todos os atributos presentes no xml

    public Estado(String nome, int id, double x, double y, boolean fim, boolean inicial) {
        this.nome = nome;
        this.id = id;
        this.x = x;
        this.y = y;
        this.fim = fim;
        this.inicial = inicial;
    }

    public Estado(Estado estado) {

    }

    // Métodos getters e setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
