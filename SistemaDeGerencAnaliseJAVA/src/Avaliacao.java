
import java.time.LocalDate;

public class Avaliacao {

    private Obra obra;
    private int nota;
    private String comentario;
    private LocalDate data;

    public Avaliacao(String comentario, LocalDate data, int nota, Obra obra) {
        this.comentario = comentario;
        this.data = data;
        this.nota = nota;
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
