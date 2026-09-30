package mangalog.api;

import java.util.ArrayList;

public class JikanMangaDTO {
    public int mal_id;
    public String title;
    public int chapters;
    public String type;
    public String synopsis;
    public ArrayList<PessoaDTO> authors;
    public ArrayList<GeneroDTO> genres;
}
