package mangalog.service;

import java.util.ArrayList;

import mangalog.model.Avaliacao;
import mangalog.model.Genero;
import mangalog.model.Obra;
import mangalog.model.ProgressoDeLeitura;
import mangalog.model.Usuario;

public class RecomendacaoService {

    // Descobre o gênero favorito do usuário contando quantas vezes cada gênero
    // aparece nas obras que ele avaliou. Em caso de empate, fica o primeiro
    // gênero que bateu a maior contagem.
    //
    // OBS: essa é uma decisão de design que tomei — "favorito" aqui conta
    // TODAS as avaliações, não só as notas altas. Se o time quiser considerar
    // só notas 4-5 como "gostou", é só trocar a condição no laço abaixo.
    public Genero generoFavorito(Usuario usuario) {
        ArrayList<String> nomesContados = new ArrayList<>();
        ArrayList<Integer> contagens = new ArrayList<>();
        ArrayList<Genero> generosUnicos = new ArrayList<>();

        ArrayList<Avaliacao> avaliacoes = usuario.getAvaliacoes();
        for (int i = 0; i < avaliacoes.size(); i++) {
            Obra obra = avaliacoes.get(i).getObra();
            ArrayList<Genero> generosDaObra = obra.getGeneros();
            for (int j = 0; j < generosDaObra.size(); j++) {
                Genero genero = generosDaObra.get(j);
                int indiceExistente = nomesContados.indexOf(genero.getNome());
                if (indiceExistente == -1) {
                    nomesContados.add(genero.getNome());
                    contagens.add(1);
                    generosUnicos.add(genero);
                } else {
                    contagens.set(indiceExistente, contagens.get(indiceExistente) + 1);
                }
            }
        }

        if (generosUnicos.isEmpty()) {
            return null;
        }

        int indiceMaisFrequente = 0;
        for (int i = 1; i < contagens.size(); i++) {
            if (contagens.get(i) > contagens.get(indiceMaisFrequente)) {
                indiceMaisFrequente = i;
            }
        }
        return generosUnicos.get(indiceMaisFrequente);
    }

    // Sugere a próxima obra: acha o gênero favorito do usuário e procura, no
    // catálogo, uma obra desse gênero que ele ainda não começou a ler.
    public Obra sugerirProximaObra(Usuario usuario, ArrayList<Obra> catalogo) {
        Genero favorito = generoFavorito(usuario);
        if (favorito == null) {
            return null; // usuário ainda não avaliou nada, não dá pra saber o gosto dele
        }

        for (int i = 0; i < catalogo.size(); i++) {
            Obra obra = catalogo.get(i);
            if (jaEstaNoProgresso(usuario, obra)) {
                continue;
            }
            ArrayList<Genero> generosDaObra = obra.getGeneros();
            for (int j = 0; j < generosDaObra.size(); j++) {
                if (generosDaObra.get(j).getNome().equals(favorito.getNome())) {
                    return obra;
                }
            }
        }
        return null; // não achou nenhuma obra nova desse gênero
    }

    private boolean jaEstaNoProgresso(Usuario usuario, Obra obra) {
        ArrayList<ProgressoDeLeitura> progressos = usuario.getProgressos();
        for (int i = 0; i < progressos.size(); i++) {
            if (progressos.get(i).getObra() == obra) {
                return true;
            }
        }
        return false;
    }
}
