import java.util.ArrayList;

public class Estado {

    // Atributos que estão no xml do automato
    private String nome;
    private int id, x, y;

    /**
     *  Cada estado pode varias transições saindo e entrando nele.
     *  Por isso se faz necessario um ArrayList para guardar todas
     *  essas transições.
     */
    ArrayList<Transicao> lstTransicoes = new ArrayList<>();

    // Construtor passando todos os atributos presentes no xml
    public Estado(String nome, int id, int x, int y){
        this.nome = nome;
        this.id = id;
        this.x = x;
        this.y = y;
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
