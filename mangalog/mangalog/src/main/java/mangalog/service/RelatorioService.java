package mangalog.service;

import java.util.ArrayList;

import mangalog.model.Avaliacao;
import mangalog.model.Obra;
import mangalog.model.ProgressoDeLeitura;
import mangalog.model.Usuario;

public class RelatorioService {

    // Calcula a nota média de uma obra.
    public double notaMedia(Obra obra) {
        return obra.calcularNotaMedia();
    }

    // Nota média geral do catálogo inteiro (média de todas as avaliações de todas as obras)
    public double notaMediaGeral(ArrayList<Obra> catalogo) {
        int somaTotal = 0;
        int quantidadeTotal = 0;

        for (Obra obra : catalogo) {
            for (Avaliacao avaliacao : obra.getAvaliacoes()) {
                somaTotal += avaliacao.getNota();
                quantidadeTotal++;
            }
        }

        if (quantidadeTotal == 0) {
            return 0.0;
        }
        return (double) somaTotal / quantidadeTotal;
    }

    // Top N obras por nota média, da maior pra menor (só entram obras já avaliadas)
    public ArrayList<Obra> rankingTopN(ArrayList<Obra> catalogo, int n) {
        ArrayList<Obra> avaliadas = new ArrayList<>();
        for (Obra obra : catalogo) {
            if (!obra.getAvaliacoes().isEmpty()) {
                avaliadas.add(obra);
            }
        }

        avaliadas.sort((a, b) -> Double.compare(notaMedia(b), notaMedia(a)));

        ArrayList<Obra> top = new ArrayList<>();
        for (int i = 0; i < avaliadas.size() && i < n; i++) {
            top.add(avaliadas.get(i));
        }
        return top;
    }

    // Obra com a maior nota média (null se ninguém avaliou nada ainda)
    public Obra obraComMaiorNota(ArrayList<Obra> catalogo) {
        Obra melhor = null;
        for (Obra atual : catalogo) {
            if (atual.getAvaliacoes().isEmpty()) {
                continue;
            }
            if (melhor == null || notaMedia(atual) > notaMedia(melhor)) {
                melhor = atual;
            }
        }
        return melhor;
    }

    // Obra com a menor nota média (null se ninguém avaliou nada ainda)
    public Obra obraComMenorNota(ArrayList<Obra> catalogo) {
        Obra pior = null;
        for (Obra atual : catalogo) {
            if (atual.getAvaliacoes().isEmpty()) {
                continue;
            }
            if (pior == null || notaMedia(atual) < notaMedia(pior)) {
                pior = atual;
            }
        }
        return pior;
    }

    // Soma o progresso do usuário em todas as obras que ele começou a ler
    public int totalCapitulosLidos(Usuario usuario) {
        int total = 0;
        for (ProgressoDeLeitura progresso : usuario.getProgressos()) {
            total += progresso.getCapituloAtual();
        }
        return total;
    }
}
