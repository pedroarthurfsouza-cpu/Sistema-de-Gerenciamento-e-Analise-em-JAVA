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

        /* 
        Se progressos estiver vazia, os laços não rodam e o método devolve "" (texto vazio). 
        Quem chamar o método vai precisar tratar isso
        */

        String melhorGenero = "";
        int maiorContagem = 0;

        HashMap<String, Integer> contagem = new HashMap<>();

        for (int i = 0; i < progressos.size(); i++) {

            for (int j = 0; j < progressos.get(i).getObra().getGeneros().size(); j++) {

                Genero generoAtual = progressos.get(i).getObra().getGeneros().get(j);

                String nome = generoAtual.getNome();

                if (contagem.get(nome) == null) {
                    contagem.put(nome, 1);

                } else {
                    contagem.put(nome, contagem.get(nome) + 1);
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

        
        // Implementação do método para determinar o gênero favorito do usuário
    }

}
