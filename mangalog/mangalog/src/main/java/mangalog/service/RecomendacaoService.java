package mangalog.service;

import java.util.ArrayList;
import mangalog.model.Genero;
import mangalog.model.Obra;
import mangalog.model.ProgressoDeLeitura;
import mangalog.model.Usuario;

public class RecomendacaoService {

    // Sugere uma obra do gênero favorito que o usuário ainda não começou.
    public Obra sugerirProximaObra(Usuario usuario, ArrayList<Obra> catalogo) {
        String favorito = usuario.generoFavorito();
        if (favorito.isEmpty()) {
            return null;
        }

        for (Obra obra : catalogo) {
            if (jaEstaNoProgresso(usuario, obra)) {
                continue;
            }
            for (Genero genero : obra.getGeneros()) {
                if (genero.getNome().equals(favorito)) {
                    return obra;
                }
            }
        }
        return null;
    }

    private boolean jaEstaNoProgresso(Usuario usuario, Obra obra) {
        for (ProgressoDeLeitura progresso : usuario.getProgressos()) {
            if (progresso.getObra() == obra) {
                return true;
            }
        }
        return false;
    }
}
