package mangalog.persistencia;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import mangalog.model.Obra;

public class RepositorioJson {
    private final Path arquivo;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public RepositorioJson(String caminho) {
        this.arquivo = Path.of(caminho);
    }

    public void salvarCatalogo(List<Obra> catalogo) throws Exception {
        Files.createDirectories(arquivo.getParent());
        Files.writeString(arquivo, gson.toJson(catalogo));
    }

    public ArrayList<Obra> carregarCatalogo() throws Exception {
        if (!Files.exists(arquivo)) {
            return new ArrayList<>();
        }
        Type tipo = new TypeToken<ArrayList<Obra>>() {}.getType();
        ArrayList<Obra> obras = gson.fromJson(Files.readString(arquivo), tipo);
        return obras != null ? obras : new ArrayList<>();
    }
}
