package mangalog.app;

import java.util.ArrayList;
import java.util.Scanner;

import mangalog.api.JikanClient;
import mangalog.model.Genero;
import mangalog.model.ListaDeLeitura;
import mangalog.model.Obra;
import mangalog.model.ProgressoDeLeitura;
import mangalog.model.Usuario;
import mangalog.persistencia.RepositorioJson;
import mangalog.service.RecomendacaoService;
import mangalog.service.RelatorioService;

public class Main {

    private static final String CAMINHO_CATALOGO = "dados/catalogo.json";

    public static void main(String[] args) {
        System.out.println("Bem-vindo ao MangaLog");

        Scanner scanner = new Scanner(System.in);
        RepositorioJson repositorio = new RepositorioJson(CAMINHO_CATALOGO);
        ArrayList<Obra> catalogo = carregarOuImportarCatalogo(repositorio, scanner);
        Usuario usuario = new Usuario("Usuario");

        RelatorioService relatorioService = new RelatorioService();
        RecomendacaoService recomendacaoService = new RecomendacaoService();

        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n--- MENU MANGALOG ---");
            System.out.println("1 - Nota media geral do catalogo");
            System.out.println("2 - Ranking top 3");
            System.out.println("3 - Obra com maior e menor nota");
            System.out.println("4 - Total de capitulos lidos");
            System.out.println("5 - Sugestao de proxima obra");
            System.out.println("6 - Avaliar uma obra");
            System.out.println("7 - Atualizar progresso de leitura");
            System.out.println("8 - Importar obras");
            System.out.println("9 - Buscar/listar catálogo");
            System.out.println("10 - Criar lista de leitura");
            System.out.println("11 - Adicionar obra a uma lista");
            System.out.println("12 - Ver minhas listas");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");

            opcao = lerOpcao(scanner);

            switch (opcao) {
                case 1:
                    System.out.printf("Nota media geral: %.2f%n", relatorioService.notaMediaGeral(catalogo));
                    break;
                case 2:
                    ArrayList<Obra> top3 = relatorioService.rankingTopN(catalogo, 3);
                    System.out.println("Top 3 obras:");
                    for (int i = 0; i < top3.size(); i++) {
                        System.out.println((i + 1) + ". " + top3.get(i).getTitulo());
                    }
                    break;
                case 3:
                    Obra melhor = relatorioService.obraComMaiorNota(catalogo);
                    Obra pior = relatorioService.obraComMenorNota(catalogo);
                    System.out.println(
                            "Maior nota: " + (melhor != null ? melhor.getTitulo() : "nenhuma avaliacao ainda"));
                    System.out.println("Menor nota: " + (pior != null ? pior.getTitulo() : "nenhuma avaliacao ainda"));
                    break;
                case 4:
                    System.out.println("Total de capitulos lidos: " + relatorioService.totalCapitulosLidos(usuario));
                    break;
                case 5:
                    Obra sugestao = recomendacaoService.sugerirProximaObra(usuario, catalogo);
                    System.out.println("Sugestao: "
                            + (sugestao != null ? sugestao.getTitulo() : "nao achei nada novo pro seu gosto ainda"));
                    break;
                case 6:
                    avaliarObra(catalogo, usuario, scanner);
                    break;
                case 7:
                    atualizarProgresso(catalogo, usuario, scanner);
                    break;
                case 8:
                    importarObras(catalogo, repositorio, scanner);
                    break;
                case 9:
                    buscarNoCatalogo(catalogo, scanner);
                    break;
                case 10:
                    criarLista(usuario, scanner);
                    break;
                case 11:
                    adicionarObraALista(catalogo, usuario, scanner);
                    break;
                case 12:
                    verListas(usuario);
                    break;
                case 0:
                    try {
                        repositorio.salvarCatalogo(catalogo);
                        System.out.println("Catalogo salvo. Ate mais!");
                    } catch (Exception e) {
                        System.out.println("Nao foi possivel salvar o catalogo: " + e.getMessage());
                    }
                    break;
                default:
                    System.out.println("Opcao invalida.");
            }
        }

        scanner.close();
    }

    // Carrega o catálogo salvo ou importa dados da API; usa dados de teste se a API falhar.
    private static ArrayList<Obra> carregarOuImportarCatalogo(RepositorioJson repositorio, Scanner scanner) {
        ArrayList<Obra> catalogo;
        try {
            catalogo = repositorio.carregarCatalogo();
        } catch (Exception e) {
            System.out.println("Nao consegui ler o catalogo salvo (" + e.getMessage() + "). Comecando vazio.");
            catalogo = new ArrayList<>();
        }

        if (!catalogo.isEmpty()) {
            return catalogo;
        }

        System.out.println("Nenhum catalogo salvo ainda.");
        System.out.print("Digite um termo de busca para importar da Jikan (ex: Naruto): ");
        String termo = scanner.nextLine().trim();

        try {
            JikanClient jikanClient = new JikanClient();
            catalogo = jikanClient.buscarObras(termo, null);
            if (catalogo.isEmpty()) {
                System.out.println("A busca nao trouxe nenhuma obra.");
            } else {
                repositorio.salvarCatalogo(catalogo);
                System.out.println(catalogo.size() + " obras importadas e salvas em " + CAMINHO_CATALOGO + ".");
            }
        } catch (Exception e) {
            System.out.println("Nao consegui importar da Jikan (" + e.getMessage() + "). Usando um catalogo de teste.");
            catalogo = criarCatalogoDeTesteFallback();
        }

        return catalogo;
    }

    // Retorna -1 quando a entrada não é um número.
    private static int lerOpcao(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Mostra o catálogo e retorna a obra escolhida, ou null se a escolha for inválida.
    private static Obra escolherObra(ArrayList<Obra> catalogo, Scanner scanner) {
        if (catalogo.isEmpty()) {
            System.out.println("O catalogo esta vazio.");
            return null;
        }

        for (int i = 0; i < catalogo.size(); i++) {
            System.out.println((i + 1) + ". " + catalogo.get(i).getTitulo());
        }
        System.out.print("Escolha o numero da obra: ");
        int escolha = lerOpcao(scanner);

        if (escolha < 1 || escolha > catalogo.size()) {
            System.out.println("Escolha invalida.");
            return null;
        }
        return catalogo.get(escolha - 1);
    }

    private static void avaliarObra(ArrayList<Obra> catalogo, Usuario usuario, Scanner scanner) {
        Obra obra = escolherObra(catalogo, scanner);
        if (obra == null) {
            return;
        }

        System.out.print("Nota (1 a 5): ");
        int nota = lerOpcao(scanner);

        System.out.print("Comentario (ou deixe em branco): ");
        String comentario = scanner.nextLine();

        try {
            usuario.avaliarObra(obra, nota, comentario);
            System.out.println("Avaliacao registrada para " + obra.getTitulo() + ".");
        } catch (IllegalArgumentException e) {
            System.out.println("Nota invalida: " + e.getMessage());
        }
    }

    private static void atualizarProgresso(ArrayList<Obra> catalogo, Usuario usuario, Scanner scanner) {
        Obra obra = escolherObra(catalogo, scanner);
        if (obra == null) {
            return;
        }

        System.out.print("Capitulo atual (0 a " + obra.getTotalCapitulos() + "): ");
        int capitulo = lerOpcao(scanner);

        ProgressoDeLeitura progresso = buscarProgresso(usuario, obra);
        if (progresso == null) {
            progresso = new ProgressoDeLeitura(obra);
            usuario.getProgressos().add(progresso);
        }

        try {
            progresso.setCapituloAtual(capitulo);
            System.out.println("Progresso atualizado: " + obra.getTitulo() + " -> capitulo " + capitulo + ".");
        } catch (IllegalArgumentException e) {
            System.out.println("Capitulo invalido: " + e.getMessage());
        }
    }

    // Procura o progresso de leitura da obra.
    private static ProgressoDeLeitura buscarProgresso(Usuario usuario, Obra obra) {
        for (ProgressoDeLeitura progresso : usuario.getProgressos()) {
            if (progresso.getObra() == obra) {
                return progresso;
            }
        }
        return null;
    }

    // Evita iniciar sem catálogo quando a API estiver indisponível.
    private static ArrayList<Obra> criarCatalogoDeTesteFallback() {
        ArrayList<Obra> catalogo = new ArrayList<>();
        Obra obra = new Obra("Obra de Teste (fallback offline)", mangalog.model.TipoObra.OUTRO,
                new mangalog.model.Autor("Desconhecida", "Desconhecido"), 10);
        catalogo.add(obra);
        return catalogo;
    }

    private static void importarObras(
            ArrayList<Obra> catalogo,
            RepositorioJson repositorio,
            Scanner scanner) {

        System.out.print("Digite um termo de busca: ");
        String termo = scanner.nextLine().trim();

        try {
            JikanClient jikanClient = new JikanClient();

            ArrayList<Obra> novasObras = jikanClient.buscarObras(termo, null);

            if (novasObras.isEmpty()) {
                System.out.println("Nenhuma obra encontrada.");
                return;
            }

            int adicionadas = 0;

            for (Obra novaObra : novasObras) {
                boolean jaExiste = false;

                for (Obra obra : catalogo) {
                    if (obra.getTitulo().equalsIgnoreCase(novaObra.getTitulo())) {
                        jaExiste = true;
                        break;
                    }
                }

                if (!jaExiste) {
                    catalogo.add(novaObra);
                    adicionadas++;
                }
            }

            repositorio.salvarCatalogo(catalogo);

            System.out.println(
                    adicionadas + " de " + novasObras.size() + " obras importadas com sucesso (" +
                            (novasObras.size() - adicionadas) + " já estavam no catálogo).");

        } catch (Exception e) {
            System.out.println(
                    "Nao foi possivel importar obras: " + e.getMessage());
        }
    }

    private static void buscarNoCatalogo(
            ArrayList<Obra> catalogo,
            Scanner scanner) {

        if (catalogo.isEmpty()) {
            System.out.println("O catalogo esta vazio.");
            return;
        }

        System.out.println("\n--- BUSCAR NO CATALOGO ---");
        System.out.println("1 - Buscar por titulo");
        System.out.println("2 - Buscar por genero");
        System.out.println("3 - Listar todas as obras");
        System.out.print("Escolha: ");

        int opcao = lerOpcao(scanner);

        switch (opcao) {
            case 1:
                buscarPorTitulo(catalogo, scanner);
                break;

            case 2:
                buscarPorGenero(catalogo, scanner);
                break;

            case 3:
                listarCatalogo(catalogo);
                break;

            default:
                System.out.println("Opcao invalida.");
        }
    }

    private static void buscarPorTitulo(
            ArrayList<Obra> catalogo,
            Scanner scanner) {

        System.out.print("Digite o titulo: ");
        String termo = scanner.nextLine().trim().toLowerCase();

        boolean encontrou = false;

        for (Obra obra : catalogo) {
            if (obra.getTitulo().toLowerCase().contains(termo)) {
                System.out.println("- " + obra.getTitulo());
                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Nenhuma obra encontrada.");
        }
    }

    private static void listarCatalogo(ArrayList<Obra> catalogo) {

        System.out.println("\n--- CATALOGO ---");

        for (int i = 0; i < catalogo.size(); i++) {
            System.out.println((i + 1) + ". " + catalogo.get(i).getTitulo());
        }
    }

    private static void buscarPorGenero(
            ArrayList<Obra> catalogo,
            Scanner scanner) {

        System.out.print("Digite o genero: ");
        String termo = scanner.nextLine().trim().toLowerCase();

        boolean encontrou = false;

        for (Obra obra : catalogo) {

            for (Genero genero : obra.getGeneros()) {
                if (genero.getNome().toLowerCase().contains(termo)) {
                    System.out.println("- " + obra.getTitulo());
                    encontrou = true;
                    break;
                }
            }
        }

        if (!encontrou) {
            System.out.println("Nenhuma obra encontrada.");
        }
    }

    private static void criarLista(Usuario usuario, Scanner scanner) {
        System.out.print("Nome da nova lista: ");
        String nome = scanner.nextLine().trim();

        if (nome.isEmpty()) {
            System.out.println("O nome nao pode ser vazio.");
            return;
        }

        usuario.getListas().add(new ListaDeLeitura(nome));
        System.out.println("Lista \"" + nome + "\" criada.");
    }

    private static void adicionarObraALista(ArrayList<Obra> catalogo, Usuario usuario, Scanner scanner) {
        ListaDeLeitura lista = escolherLista(usuario, scanner);
        if (lista == null) {
            return;
        }

        Obra obra = escolherObra(catalogo, scanner);
        if (obra == null) {
            return;
        }

        lista.getObras().add(obra);
        System.out.println(obra.getTitulo() + " adicionada a lista \"" + lista.getNome() + "\".");
    }

    private static void verListas(Usuario usuario) {
        ArrayList<ListaDeLeitura> listas = usuario.getListas();

        if (listas.isEmpty()) {
            System.out.println("Voce ainda nao tem nenhuma lista. Use a opcao 10 pra criar uma.");
            return;
        }

        for (ListaDeLeitura lista : listas) {
            ArrayList<Obra> obras = lista.getObras();
            System.out.println(lista.getNome() + " (" + obras.size() + " obra(s)):");
            for (Obra obra : obras) {
                System.out.println("  - " + obra.getTitulo());
            }
        }
    }

    // Mostra as listas e retorna a escolhida, ou null se a escolha for inválida.
    private static ListaDeLeitura escolherLista(Usuario usuario, Scanner scanner) {
        ArrayList<ListaDeLeitura> listas = usuario.getListas();

        if (listas.isEmpty()) {
            System.out.println("Voce ainda nao tem nenhuma lista. Use a opcao 10 pra criar uma.");
            return null;
        }

        for (int i = 0; i < listas.size(); i++) {
            System.out.println((i + 1) + ". " + listas.get(i).getNome());
        }
        System.out.print("Escolha o numero da lista: ");
        int escolha = lerOpcao(scanner);

        if (escolha < 1 || escolha > listas.size()) {
            System.out.println("Escolha invalida.");
            return null;
        }
        return listas.get(escolha - 1);
    }

}
