package mangalog.model;

import java.time.LocalDate;

public class Avaliacao {

    private Obra obra;
    private int nota;
    private String comentario;
    private LocalDate data;

    public Avaliacao(int nota, Obra obra) {
       
        this.data = LocalDate.now();
        this.setNota(nota);
        this.obra = obra;
    }

    public Obra getObra() {
        return obra;
    }

    public void setObra(Obra obra) {
        this.obra = obra;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
         if (nota < 1 || nota > 5) {
            throw new IllegalArgumentException("Nota deve ser entre 1 e 5");
        }
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

}
