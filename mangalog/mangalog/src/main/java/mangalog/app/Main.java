package mangalog.app;

public class Main { public static void main(String[] args) {
        
case 1:
    relatorioService.notaMediaGeral(catalogo);
case 2:
    relatorioService.rankingTopN(catalogo, 3);
case 3:
    relatorioService.obraComMaiorNota(catalogo);
    relatorioService.obraComMenorNota(catalogo);
case 4:
    relatorioService.totalCapitulosLidos(usuario);
case 5:
    recomendacaoService.sugerirProximaObra(usuario, catalogo);
        
    }
}