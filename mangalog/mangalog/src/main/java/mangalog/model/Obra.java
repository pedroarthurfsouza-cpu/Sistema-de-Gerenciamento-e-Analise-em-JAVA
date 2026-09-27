package mangalog.model;

import java.util.ArrayList;

public class Obra {
    private String titulo;
    private TipoObra tipo;
    private Autor autor;
    private ArrayList<Genero> generos = new ArrayList<>();
    private int totalCapitulos;
    private String sinopse;
    private String idExterno;
    private ArrayList<Avaliacao> avaliacoes = new ArrayList<>();

    public Obra(String titulo, TipoObra tipo, Autor autor, ArrayList<Genero> generos, int totalCapitulos) {
        this.titulo = titulo;
        this.tipo = tipo;
        this.autor = autor;
        this.generos = generos;
        this.totalCapitulos = totalCapitulos;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public TipoObra getTipo() {
        return tipo;
    }

    public void setTipo(TipoObra tipo) {
        this.tipo = tipo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public ArrayList<Genero> getGeneros() {
        return generos;
    }

    public void setGeneros(ArrayList<Genero> generos) {
        this.generos = generos;
    }

    public int getTotalCapitulos() {
        return totalCapitulos;
    }

    public void setTotalCapitulos(int totalCapitulos) {
        this.totalCapitulos = totalCapitulos;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public String getIdExterno() {
        return idExterno;
    }

    public void setIdExterno(String idExterno) {
        this.idExterno = idExterno;
    }

    public ArrayList<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(ArrayList<Avaliacao> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }

    public double calcularNotaMedia(){

        if (avaliacoes.size() > 0) {
            double somaNotas = 0;
            for (int i = 0; i < avaliacoes.size(); i++){

                somaNotas += avaliacoes.get(i).getNota();
            }
            return somaNotas / avaliacoes.size();
        }else{
            //vou deixar sem nenhum print, no APP vai ter que ter um println("Não há avaliações para essa obra");
            return 0;
            
        }
    }


    public void adicionarAvaliacao(Avaliacao avaliacao) {
        if(this.avaliacoes.isEmpty()){
            this.avaliacoes.add(avaliacao);

        }else{
           avaliacoes.set(0, avaliacao);
        }
    }
}
