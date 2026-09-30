package mangalog.model;

import java.util.ArrayList;

public class ListaDeLeitura {

    private String nome;
    private ArrayList<Obra> obras;

    public ListaDeLeitura(String nome) {
        this.nome = nome;
        this.obras = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<Obra> getObras() {
        return obras;
    }

    public void setObras(ArrayList<Obra> obras) {
        this.obras = obras;
    }

}
