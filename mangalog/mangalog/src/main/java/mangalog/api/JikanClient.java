package mangalog.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

import com.google.gson.Gson;

import mangalog.model.Autor;
import mangalog.model.Genero;
import mangalog.model.Obra;
import mangalog.model.TipoObra;

public class JikanClient {
    
    public String buscarBruto(String termo) throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://api.tenrai.org/v1/anime?q=" + termo + "&limit=5"))
        .GET()
        .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    public String ArrayList<Obra> buscarObras(String termo) throws Exception{
        String json = buscarBruto(termo);
        Gson gson = new Gson();
        JikanRespostaDTO resposta = gson.fromJson(json, JikanRespostaDTO.class);
        
        ArrayList<Obra> obras = new ArrayList<>();
        for(JikanMangaDTO dto : resposta.data){
            obras.add(converter(dto));
        }
        return obras;
    }

    private Obra converter(JikanMangaDTO dto) {
        String nomeAutor = dto.authors.isEmpty() ? "Desconhecido" : dto.authors.get(0).name;
        Autor autor = new Autor(nomeAutor, "Desconhecida");

        Obra obra = new Obra(dto.title, TipoObra.MANGA, autor, dto.chapters);
        obra.setSinopse(dto.synopsis);
        obra.setIdExterno(String.valueOf(dto.mal_id));

        for (GeneroDTO g : dto.genres) {
            obra.getGeneros().add(new Genero(g.nome != null ? g.nome : g.name));
        }

        return obra;
    }
}
