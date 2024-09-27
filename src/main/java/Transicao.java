public class Transicao {

    private Estado de, para;
    private String simbolo;

    public Transicao(Estado de, Estado para, String simbolo) {
        this.de = de;
        this.para = para;
        this.simbolo = simbolo;
    }

    public Estado getDe() {
        return de;
    }

    public void setDe(Estado de) {
        this.de = de;
    }

    public Estado getPara() {
        return para;
    }

    public void setPara(Estado para) {
        this.para = para;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }
}
