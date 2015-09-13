package stamboom.domain;

public class PersoonMetGeneratie
{
    // *********datavelden**************************************************
    private final String persoonsgegevens;
    private final int generatie;

    //***********************constructoren**********************************
    public PersoonMetGeneratie(String tekst, int generatie)
    {
        this.persoonsgegevens = tekst;
        this.generatie = generatie;
    }

    //**********************methoden****************************************
    public int getGeneratie()
    {
        return generatie;
    }

    public String getPersoonsgegevens()
    {
        return persoonsgegevens;
    }

    public String toString()
    {
        String value="";
        for(int i=0; i< generatie; i++)
        {
            value += "  ";
        }
        value += getPersoonsgegevens();
        return value;
    }
}
