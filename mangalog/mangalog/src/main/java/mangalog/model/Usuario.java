package mangalog.model;
import java.util.ArrayList;

public class Usuario {
private String nome;
    ArrayList<Avaliacao> avaliacoes;
    ArrayList<ProgressoDeLeitura> progressos;
    ArrayList<ListaDeLeitura> listas;
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public ArrayList<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }
    public void setAvaliacoes(ArrayList<Avaliacao> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }
    public ArrayList<ProgressoDeLeitura> getProgressos() {
        return progressos;
    }
    public void setProgressos(ArrayList<ProgressoDeLeitura> progressos) {
        this.progressos = progressos;
    }
    public ArrayList<ListaDeLeitura> getListas() {
        return listas;
    }
    public void setListas(ArrayList<ListaDeLeitura> listas) {
        this.listas = listas;
    }
}


