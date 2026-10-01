package mangalog.model;

import java.time.LocalDate;

public class ProgressoDeLeitura {

    private Obra obra;
    private int capituloAtual;
    private StatusLeitura status;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public ProgressoDeLeitura(Obra obra) {
        this.obra = obra;
        this.capituloAtual = 0;
        this.status = StatusLeitura.LENDO;
        this.dataInicio = LocalDate.now();
    }

    public Obra getObra() {
        return obra;
    }

    public void setObra(Obra obra) {
        this.obra = obra;
    }

    public int getCapituloAtual() {
        return capituloAtual;
    }

    public void setCapituloAtual(int capituloAtual) {
        boolean totalConhecido = obra.getTotalCapitulos() > 0;
        if (capituloAtual < 0 || (totalConhecido && capituloAtual > obra.getTotalCapitulos())) {
            throw new IllegalArgumentException("Capítulo atual inválido.");
        }
        this.capituloAtual = capituloAtual;
        if (totalConhecido && capituloAtual == obra.getTotalCapitulos()) {
            this.status = StatusLeitura.CONCLUIDO;
            this.dataFim = LocalDate.now();
        }
    }

    public StatusLeitura getStatus() {
        return status;
    }

    public void setStatus(StatusLeitura status) {
        this.status = status;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

}
