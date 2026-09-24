public class Mangas {

private String nome;
private String autor;
private String genero;
private int anoLancamento;
private int numeroVolumes;
private int numeroCapitulos;



public Mangas(String nome, String autor, String genero, int anoLancamento, int numeroVolumes, int numeroCapitulos) {
    this.nome = nome;
    this.autor = autor;
    this.genero = genero;
    this.anoLancamento = anoLancamento;
    this.numeroVolumes = numeroVolumes;
    this.numeroCapitulos = numeroCapitulos;
}
public String getNome() {
    return nome;
}
public void setNome(String nome) {
    this.nome = nome;
}
public String getAutor() {
    return autor;
}
public void setAutor(String autor) {
    this.autor = autor;
}
public String getGenero() {
    return genero;
}
public void setGenero(String genero) {
    this.genero = genero;
}
public int getAnoLancamento() {
    return anoLancamento;
}
public void setAnoLancamento(int anoLancamento) {
    this.anoLancamento = anoLancamento;
}
public int getNumeroVolumes() {
    return numeroVolumes;
}
public void setNumeroVolumes(int numeroVolumes) {
    this.numeroVolumes = numeroVolumes;
}
public int getNumeroCapitulos() {
    return numeroCapitulos;
}
public void setNumeroCapitulos(int numeroCapitulos) {
    this.numeroCapitulos = numeroCapitulos;
}




}
