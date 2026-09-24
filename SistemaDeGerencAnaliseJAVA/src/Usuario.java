import java.util.ArrayList;
public class Usuario {

private String nome;
private String email;
private int idade;
ArrayList<Leitura> leituras = new ArrayList<>();

public Usuario(String nome, String email, int idade) {
    this.nome = nome;
    this.email = email;
    this.idade = idade;
}
public String getNome() {
    return nome;
}
public void setNome(String nome) {
    this.nome = nome;
}
public String getEmail() {
    return email;
}
public void setEmail(String email) {
    this.email = email;
}
public int getIdade() {
    return idade;
}
public void setIdade(int idade) {
    this.idade = idade;
}




}
