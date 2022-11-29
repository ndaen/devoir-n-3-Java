package Entities;

public class DatasGraph
{
    private int nb;
    private String sexe;
    private String tranche;

    public DatasGraph(int unNb, String unSexe, String uneTranche)
    {
        nb = unNb;
        sexe = unSexe;
        tranche = uneTranche;
    }

    public int getNb() {
        return nb;
    }

    public String getSexe() {
        return sexe;
    }

    public String getTranche() {
        return tranche;
    }
}
