package mangalog.app;

import java.util.ArrayList;
import mangalog.api.JikanClient;
import mangalog.model.Genero;
import mangalog.model.Obra;

public class Main {
    public static void main(String[] args) {
        JikanClient client = new JikanClient();

        try {
            System.out.println("=== Buscando MANGÁ ===");
            ArrayList<Obra> mangas = client.buscarObras("one piece", "manga");
            imprimir(mangas);

            System.out.println("\n=== Buscando MANHWA ===");
            ArrayList<Obra> manhwas = client.buscarObras("solo leveling", "manhwa");
            imprimir(manhwas);

        } catch (Exception e) {
            System.out.println("Erro ao buscar dados da API: " + e.getMessage());
        }
    }

    private static void imprimir(ArrayList<Obra> obras) {
        for (Obra o : obras) {
            System.out.println("Título: " + o.getTitulo());
            System.out.println("Tipo: " + o.getTipo());
            System.out.println("Capítulos: " + o.getTotalCapitulos());
            System.out.println("Sinopse: " + (o.getSinopse() != null ? o.getSinopse().substring(0, Math.min(80, o.getSinopse().length())) + "..." : "sem sinopse"));
            System.out.print("Gêneros: ");
            for (Genero g : o.getGeneros()) {
                System.out.print(g.getNome() + " ");
            }
            System.out.println("\n---");
        }
    }
}