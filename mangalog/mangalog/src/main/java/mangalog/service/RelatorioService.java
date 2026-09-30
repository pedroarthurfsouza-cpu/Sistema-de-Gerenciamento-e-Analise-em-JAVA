package mangalog.service;

import java.util.ArrayList;
import java.util.Collections;

import mangalog.model.Avaliacao;
import mangalog.model.Obra;
import mangalog.model.ProgressoDeLeitura;
import mangalog.model.Usuario;

public class RelatorioService {

    // Nota média de UMA obra (método auxiliar usado pelos outros métodos abaixo)
    public double notaMedia(Obra obra) {
        ArrayList<Avaliacao> avaliacoes = obra.getAvaliacoes();
        if (avaliacoes.isEmpty()) {
            return 0.0;
        }
        int soma = 0;
        for (int i = 0; i < avaliacoes.size(); i++) {
            soma += avaliacoes.get(i).getNota();
        }
        return (double) soma / avaliacoes.size();
    }

    // Nota média geral do catálogo inteiro (média de todas as avaliações de todas as obras)
    public double notaMediaGeral(ArrayList<Obra> catalogo) {
        int somaTotal = 0;
        int quantidadeTotal = 0;

        for (int i = 0; i < catalogo.size(); i++) {
            ArrayList<Avaliacao> avaliacoes = catalogo.get(i).getAvaliacoes();
            for (int j = 0; j < avaliacoes.size(); j++) {
                somaTotal += avaliacoes.get(j).getNota();
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
        for (int i = 0; i < catalogo.size(); i++) {
            if (!catalogo.get(i).getAvaliacoes().isEmpty()) {
                avaliadas.add(catalogo.get(i));
            }
        }

        // Ordena da maior nota media para a menor.
        // Collections.sort precisa de um Comparator: uma função que, dadas
        // duas obras (a, b), diz qual vem primeiro. Double.compare(x, y)
        // devolve negativo se x < y, positivo se x > y, zero se forem iguais.
        // Aqui comparamos notaMedia(b) com notaMedia(a) (nessa ordem, "ao
        // contrário") para ordenar do maior para o menor, em vez do menor
        // para o maior.
        Collections.sort(avaliadas, (a, b) -> Double.compare(notaMedia(b), notaMedia(a)));

        ArrayList<Obra> top = new ArrayList<>();
        for (int i = 0; i < avaliadas.size() && i < n; i++) {
            top.add(avaliadas.get(i));
        }
        return top;
    }

    // Obra com a maior nota média (null se ninguém avaliou nada ainda)
    public Obra obraComMaiorNota(ArrayList<Obra> catalogo) {
        Obra melhor = null;
        for (int i = 0; i < catalogo.size(); i++) {
            Obra atual = catalogo.get(i);
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
        for (int i = 0; i < catalogo.size(); i++) {
            Obra atual = catalogo.get(i);
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
        ArrayList<ProgressoDeLeitura> progressos = usuario.getProgressos();
        for (int i = 0; i < progressos.size(); i++) {
            total += progressos.get(i).getCapituloAtual();
        }
        return total;
    }
}
