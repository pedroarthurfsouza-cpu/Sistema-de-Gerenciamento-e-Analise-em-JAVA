package mangalog.app;

import java.util.ArrayList;
import mangalog.api.JikanClient;
import mangalog.model.Genero;
import mangalog.model.Obra;
import mangalog.persistencia.RepositorioJson;

public class Main {
    public static void main(String[] args) {
        RepositorioJson repositorio = new RepositorioJson("dados/catalogo.json");

        try {
            ArrayList<Obra> obras = repositorio.carregarCatalogo();
            System.out.println("Carregou " + obras.size() + " obras do arquivo.");
            for (Obra o : obras) {
                System.out.println("- " + o.getTitulo() + " (" + o.getTipo() + ")");
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar: " + e.getMessage());
        }
    }
}