import java.io.*;

class EssaiGraphe {
    public static void main(String [] args) {
        FileInputStream f;
        Graphe g = null;

        if (args.length < 1) {
            throw new RuntimeException("Il faut un graphe en argument !");
        }

        try {
            f = new FileInputStream(args[0]);
            g = new Graphe(f);
        } catch (Exception e) {
            System.out.println(e);
        }


        int sommet = 3;
        System.out.println("Le degré du sommet "+sommet+" de ce graphe est: "+g.Degre(sommet-1));
        System.out.println("\""+args[0]+"\" "+(g.EstCouplage() ? "est" : "n'est pas")+" un couplage");

    }
}
