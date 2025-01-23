import java.io.*;

class EssaiGraphe {
    public static void main(String [] args) {
        FileInputStream f;
        Graphe g = null;

        try {
            f = new FileInputStream(args[0]);
            g = new Graphe(f);
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println("Le degré du sommet 1 de ce graphe est:"+g.Degre(3));
        System.out.println("Le graphe "+(g.EstCouplage() ? "est" : "n'est pas")+" un couplage");

    }
}
