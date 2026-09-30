package mangalog.service;

import java.util.ArrayList;

import mangalog.model.Avaliacao;
import mangalog.model.Genero;
import mangalog.model.Obra;
import mangalog.model.ProgressoDeLeitura;
import mangalog.model.Usuario;

public class RecomendacaoService {


    // Sugere a próxima obra: acha o gênero favorito do usuário e procura, no
    // catálogo, uma obra desse gênero que ele ainda não começou a ler.
    public Obra sugerirProximaObra(Usuario usuario, ArrayList<Obra> catalogo) {
    String favorito = usuario.generoFavorito();
    if (favorito.isEmpty()) {
        return null;
    }
    for (int i = 0; i < catalogo.size(); i++) {
        Obra obra = catalogo.get(i);
        if (jaEstaNoProgresso(usuario, obra)) continue;
        for (Genero g : obra.getGeneros()) {
            if (g.getNome().equals(favorito)) {
                return obra;
            }
        }
    }
    return null;
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
