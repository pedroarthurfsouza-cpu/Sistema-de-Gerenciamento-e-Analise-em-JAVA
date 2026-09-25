package mangalog.model;

public class Autor {

    private String nome;
    private String nacionalidade;

    public Autor(String nacionalidade, String nome) {
        this.nacionalidade = nacionalidade;
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

}
