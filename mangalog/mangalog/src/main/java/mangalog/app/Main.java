package mangalog.app;
import java.util.Scanner;

import mangalog.api.JikanClient;

public class Main { public static void main(String[] args) throws Exception {
        JikanClient client = new JikanClient();
        String resultado = client.buscarBruto("naruto");
        System.out.println(resultado);
    
        } 
    }
