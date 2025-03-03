import java.util.*;
import java.io.*;

class Graphe {

    // Tableau de taille n, avec n le nombre de sommets du graphe.
    // Pour i allant de 0 à n-1, sommets[i] contient l'ensemble des arcs ayant pour source le noeud (i+1), dans une liste chaînée. 
    Maillon [] sommets;

    Graphe() {
        this.sommets = null;
    }

/* A finir !!!
    Graphe(Graphe graphe) {
        int nombre_sommets = graphe.nombreSommets();
        this.sommets = new Maillon[nombre_sommets];

        int i = 0; 
        while (i < nombre_sommets) {
            Maillon maillon_nouveau, maillon_origin;
            maillon_origin = graphe.sommets[i];

            while (null != maillon_origin) {
                maillon_nouveau = new Maillon();
                maillon_nouveau.suivant = null;
                Arc arc_origin = maillon_origin.arc;
                nouveau.arc = new Arc(arc_origin.numero, arc_roigin.source, arc_origin.destination, new Etiquette(arc_origin.etiquette));
                if (this.sommets[i] == null) {
                    this.sommets[i] = maillon_nouveau;
                } else {
                    maillon_nouveau.suivant = this.sommets[i];
                    this.sommets[i] = maillon_nouveau;
                }

                maillon_origin = maillon_origin.suivant;
            }
            

                nouveau.suivant = null;
                if (sommets[source] == null) {
                    sommets[source] = nouveau;
                } else {
                    courant = sommets[source];
                    while (courant.suivant != null)
                        courant = courant.suivant;
                    courant.suivant = nouveau;
                }
        }

    }
*/
    Graphe(InputStream in) throws Exception {
        lire(in);
    }

    void lire(InputStream in) throws Exception {
        Scanner s;
        int nombre_sommets;
        String specification_arc;
        String [] parties;
        int numero, source, destination, etiquette;

        s = new Scanner(in);
        nombre_sommets = s.nextInt();
        sommets = new Maillon[nombre_sommets];

        while (s.hasNext()) {
            specification_arc = s.next();
            if (!specification_arc.matches(
                        "[0-9]+/[0-9]+\\+[0-9]+/->-?[0-9]+"))
                throw new Exception("Arc mal formé : " + specification_arc);

            parties = specification_arc.split("/", 2);
            numero = Integer.valueOf(parties[0]);
            parties = parties[1].split("\\+", 2);
            source = Integer.valueOf(parties[0]) - 1;
            parties = parties[1].split("/->", 2);
            destination = Integer.valueOf(parties[0]) - 1;
            etiquette = Integer.valueOf(parties[1]);

            Maillon nouveau, courant;
            nouveau = new Maillon();
            nouveau.arc = new Arc(numero, source, destination,
                    new Etiquette(etiquette));
            nouveau.suivant = null;
            if (sommets[source] == null) {
                sommets[source] = nouveau;
            } else {
                courant = sommets[source];
                while (courant.suivant != null)
                    courant = courant.suivant;
                courant.suivant = nouveau;
            }
        }
    }

    public String toString() {
        String resultat;

        resultat = sommets.length + "\n";
        for (int i=0; i<sommets.length; i++) {
            Maillon courant;

            courant = sommets[i]; 
            while (courant != null) {
                resultat += courant.arc + "\n";
                courant = courant.suivant;
            }
        }

        return resultat;
    }



    // Retourne le nombre de sommets dans le graphe
    public int nombreSommets() {
        return sommets.length;
    }

    // Retourne le nombre d'arcs dans le graphe
    public int nombreArcs() {
        int nbArcs = 0;
        for (Maillon m: sommets) {
            while(m != null) {
                m = m.suivant;
                nbArcs++;
            }
        }
        return nbArcs;
    }


    // Cherche si un arc ayant pour source sommetSource a pour destination sommetDest
    // S'il est trouvé, le renvoie. Sinon, renvoie null.
    // Utilisé par d'autres méthodes.
    private Arc chercheArcVers(int sommetSource, int sommetDest) {
        Maillon actuel = sommets[sommetSource];
        while (actuel != null) {
            Arc candidat = actuel.arc;
            if (candidat.destination == sommetDest) return candidat;
            actuel = actuel.suivant;
        }
        return null;
    }

    // Prend en entrée sommet1 et sommet2, deux entiers correspondant à deux sommets.
    // Renvoie vrai s'il existe un arc reliant sommet1 et sommet2
    // Renvoie faux sinon
    // Renvoie également faux si un de entiers est invalide (<0 ou >= nbrSommets)
    public boolean adjacents(int sommet1, int sommet2) {
        if (sommet1 <0 || sommet2 < 0 || sommet1 >= nombreSommets() || sommet2 >=nombreSommets()) {
            return false;
        }

        // Cherche, pour les deux sommets, s'ils possèdent un arc les reliant.
        if (chercheArcVers(sommet1, sommet2) != null || chercheArcVers(sommet2, sommet1) != null) {
            return true;
        }

        // Si aucun arc ne correspond, renvoie faux
        return false;
    }

    // Prend en entrée sommet1 et sommet2, deux entiers correspondant à deux sommets.
    // Si il existe, renvoie l'arc les reliant.
    // Sinon, renvoie null.
    public Arc arcEntre(int sommet1, int sommet2) {
        if (adjacents(sommet1, sommet2)) {
            Arc candidat = chercheArcVers(sommet1, sommet2);
            if (candidat != null) return candidat;
            else return chercheArcVers(sommet2, sommet1); 
        }
        return null;
    }


    // Renvoie un tableau contenant les successeurs du sommet en entrée.
    public int[] successeurs(int sommet) {
        int successeurs[] = new int[0];
        // Si le sommet en entrée n'est pas valide, retourne un tableau vide.
        if (sommet < 0 || sommet >= nombreSommets()) return successeurs;

        // Sinon, vérifie pour tous les sommets s'ils sont adjacents, et compte le nombre trouvés.
        int nbAdjacents = 0;
        boolean adjacents[] = new boolean[nombreSommets()];
        for (int sommet2 = 0; sommet2 < nombreSommets(); sommet2++) {
            if (adjacents(sommet, sommet2)) {
                adjacents[sommet2] = true;
                nbAdjacents++;
            }
            else adjacents[sommet2] = false;
        }
        successeurs = new int[nbAdjacents];
        int indexSuccesseurs = 0;
        for (int sommet2 = 0; sommet2 < nombreSommets(); sommet2++) {
            if(adjacents[sommet2]) {
                successeurs[indexSuccesseurs] = sommet2;
                indexSuccesseurs++;
            }
        }
        return successeurs;
    }

    // Renvoie un tableau contenant les arcs du graphe
    public Arc[] arcs(){
        Arc arcs[] = new Arc[nombreArcs()];
        int index = 0;
        for (Maillon m : sommets) {
            while (m != null) {
                arcs[index] = m.arc;
                index++;
                m = m.suivant;
            }
        }
        return arcs;
    }
    

    //// Fonctions Rajoutées


    // Renvoie le degré du sommet donnée
    public int Degre(int sommet) {
        return this.successeurs(sommet).length;
    }

    // Renvoie true si le graphe est un couplage, false sinon
    public boolean EstCouplageParfait() {
        boolean retVal = true;

        int nb_sommets = this.nombreSommets(); 

        // Nombre de sommets doit etre paire
        retVal &= ( (nb_sommets % 2) == 0 );

        int i = 0;
        while ((i < nb_sommets) & retVal) {
            retVal &= this.Degre(i) == 1;   // C'est cette condition qui vérifier le "parfait"
            i++;
        }

        return retVal;
    }

    // Renvoie true si l'arc existe deja dans le graphe, false sinon
    public boolean ExisteArc(Arc arc) {
        boolean retVal = true; 

        retVal &= this.adjacents(arc.source, arc.destination);

        Arc [] arcs = this.arcs();

        int i = 0;
        while (retVal & (i < arcs.length)) {
            retVal &= (arcs[i].numero != arc.numero);
            i++;
        }

        return retVal;
    }
    
    // Supprime l'arete qui a la source sommet1 et destination sommet2
    // Precondition : l'arete existe dans le graphe
    void SupprimeArcVers(int sommet1, int sommet2) {
        Maillon ref_vers_maillon = sommets[sommet1];

        // On est sur que s'il y a qu'un seul arc, alors c'est l'arc qu'on cherche
        if (ref_vers_maillon.suivant == null) {
            sommets[sommet1] = null;
        }

        // Sinon, on cherche notre arete
        while (null != ref_vers_maillon.suivant & ref_vers_maillon.suivant.arc.source != sommet2) {
            ref_vers_maillon = ref_vers_maillon.suivant;
        }
        ref_vers_maillon.suivant = ref_vers_maillon.suivant.suivant;
    }

    // Renvoie true si l'arc donné est valide, false sinon
    public boolean EstArcValide(Arc arc) {
        boolean retVal = true;

        int source = arc.source;
        int dest = arc.destination;

        retVal &= !(source < 0 || dest < 0 || source >= nombreSommets() || dest >= nombreSommets());

        return retVal;
    }

    // Supprime l'arc entre sommet1 et sommet2 si ce dernier existe 
    public void SupprimeArc(int sommet1, int sommet2) {
        if (null != this.chercheArcVers(sommet1, sommet2)) {
            this.SupprimeArcVers(sommet1, sommet2);
        } else if (null != this.chercheArcVers(sommet2, sommet1)) {
            this.SupprimeArcVers(sommet2, sommet1);
        }
    }

    // Ajoute l'arc au graphe si ce dernier n'existe pas deja
    public void AjoutArc(Arc arc) {
        int source = arc.source;
        int dest = arc.destination;

        if (!this.ExisteArc(arc) & this.EstArcValide(arc)) {
            Maillon nouveau_maillon = new Maillon();
            nouveau_maillon.arc = new Arc(arc.numero, source, dest, arc.etiquette);
            nouveau_maillon.suivant = sommets[source];
            sommets[source] = nouveau_maillon;
        }
    }

    // Renvoie un tableau contenant les arcs du couplage parfait si il en existe, null sinon
    public Arc[] ChercheCouplageParfait() {
        return null;
    }
    public Arc[] ChercheCouplageParfait_rec() {
        return null;
    }

}
