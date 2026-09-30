package mangalog.app;

import java.util.ArrayList;
import java.util.Scanner;

import mangalog.api.JikanClient;
import mangalog.model.Avaliacao;
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
                    System.out.println("Maior nota: " + (melhor != null ? melhor.getTitulo() : "nenhuma avaliacao ainda"));
                    System.out.println("Menor nota: " + (pior != null ? pior.getTitulo() : "nenhuma avaliacao ainda"));
                    break;
                case 4:
                    System.out.println("Total de capitulos lidos: " + relatorioService.totalCapitulosLidos(usuario));
                    break;
                case 5:
                    Obra sugestao = recomendacaoService.sugerirProximaObra(usuario, catalogo);
                    System.out.println("Sugestao: " + (sugestao != null ? sugestao.getTitulo() : "nao achei nada novo pro seu gosto ainda"));
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
                case 0:
                    System.out.println("Ate mais!");
                    break;
                default:
                    System.out.println("Opcao invalida.");
            }
        }

        scanner.close();
    }

    // Tenta carregar o catalogo salvo. Se nao existir nada salvo (primeira
    // execucao), pergunta um termo e importa da Jikan API, depois salva pra
    // nao precisar buscar de novo da proxima vez (o app deve rodar offline
    // na apresentacao). Se a importacao falhar (sem internet, API fora do ar),
    // cai pra um catalogo de teste pra nao travar a demonstracao.
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

    // Le a opcao do usuario e devolve -1 se ele digitar algo que nao e numero
    private static int lerOpcao(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Mostra o catalogo numerado e devolve a Obra escolhida, ou null se a
    // escolha for invalida ou o catalogo estiver vazio.
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

        try {
            Avaliacao avaliacao = new Avaliacao(nota, obra);
            obra.adicionarAvaliacao(avaliacao);
            usuario.getAvaliacoes().add(avaliacao);
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

    // Acha o progresso que o usuario ja tem pra essa obra, se existir
    private static ProgressoDeLeitura buscarProgresso(Usuario usuario, Obra obra) {
        ArrayList<ProgressoDeLeitura> progressos = usuario.getProgressos();
        for (int i = 0; i < progressos.size(); i++) {
            if (progressos.get(i).getObra() == obra) {
                return progressos.get(i);
            }
        }
        return null;
    }

    // So usado se a importacao da Jikan falhar, pra nao travar a demonstracao
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
    
            ArrayList<Obra> novasObras =
                    jikanClient.buscarObras(termo, null);
    
            if (novasObras.isEmpty()) {
                System.out.println("Nenhuma obra encontrada.");
                return;
            }
    
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
                }
            }
    
            repositorio.salvarCatalogo(catalogo);
    
            System.out.println(
                    novasObras.size() + " obras importadas com sucesso."
            );
    
        } catch (Exception e) {
            System.out.println(
                    "Nao foi possivel importar obras: " + e.getMessage()
            );
        }
    }
    
}
