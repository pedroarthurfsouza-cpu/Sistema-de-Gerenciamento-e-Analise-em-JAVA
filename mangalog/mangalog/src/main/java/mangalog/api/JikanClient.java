package mangalog.api;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import com.google.gson.Gson;

import mangalog.model.Autor;
import mangalog.model.Genero;
import mangalog.model.Obra;
import mangalog.model.TipoObra;

public class JikanClient {

    public String buscarBruto(String termo, String tipo) throws Exception {
        HttpClient client = HttpClient.newHttpClient(); // Cria um Client HTTP, para acessar a api

        String termoCodificado = URLEncoder.encode(termo, StandardCharsets.UTF_8);
        String url = "https://api.tenrai.org/v1/manga?q=" + termoCodificado;
        if (tipo != null) {
            url += "&type=" + tipo;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public ArrayList<Obra> buscarObras(String termo, String tipo) throws Exception {
        String json = buscarBruto(termo, tipo);
        Gson gson = new Gson();
        JikanRespostaDTO resposta = gson.fromJson(json, JikanRespostaDTO.class);

        ArrayList<Obra> obras = new ArrayList<>();
        for (JikanMangaDTO dto : resposta.data) {
            obras.add(converter(dto));
        }
        return obras;
    }

    private Obra converter(JikanMangaDTO dto) {
        String nomeAutor = dto.authors.isEmpty() ? "Desconhecido" : dto.authors.get(0).name;
        Autor autor = new Autor("Desconhecida", nomeAutor);

        TipoObra tipo = converterTipo(dto.type);

        Obra obra = new Obra(dto.title, tipo, autor, dto.chapters);
        obra.setSinopse(dto.synopsis);
        obra.setIdExterno(String.valueOf(dto.mal_id));

        for (GeneroDTO g : dto.genres) {
            obra.getGeneros().add(new Genero(g.name));
        }

        return obra;
    }

    private TipoObra converterTipo(String tipoApi) {
        if (tipoApi == null) return TipoObra.OUTRO;
        switch (tipoApi) {
            case "Manga": return TipoObra.MANGA;
            case "Manhwa": return TipoObra.MANHWA;
            default: return TipoObra.OUTRO;
        }
    }
}