package mangalog.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Usuario {

    private String nome;
    private ArrayList<Avaliacao> avaliacoes;
    private ArrayList<ProgressoDeLeitura> progressos;
    private ArrayList<ListaDeLeitura> listas;

    public Usuario(String nome) {
        this.nome = nome;
        this.avaliacoes = new ArrayList<>();
        this.progressos = new ArrayList<>();
        this.listas = new ArrayList<>();
    }

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

    public String generoFavorito() {
        String melhorGenero = "";
        int maiorContagem = 0;

        HashMap<String, Integer> contagem = new HashMap<>();

        for (int i = 0; i < avaliacoes.size(); i++) {
            Avaliacao a = avaliacoes.get(i);
            if (a.getNota() >= 4) {
                ArrayList<Genero> generos = a.getObra().getGeneros();
                for (int j = 0; j < generos.size(); j++) {
                    String nome = generos.get(j).getNome();
                    contagem.put(nome, contagem.getOrDefault(nome, 0) + 1);
                }
            }
        }

        for (String genero : contagem.keySet()) {
            if (contagem.get(genero) > maiorContagem) {
                maiorContagem = contagem.get(genero);
                melhorGenero = genero;
            }
        }

        return melhorGenero;
    }

    public void avaliarObra(Obra obra, int nota, String comentario) {
        avaliacoes.removeIf(a -> a.getObra() == obra);
        Avaliacao nova = new Avaliacao(nota, obra);
        nova.setComentario(comentario);
        avaliacoes.add(nova);
        obra.adicionarAvaliacao(nova);
    }

}
