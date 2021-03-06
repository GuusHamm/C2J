/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stamboom.controller;

import stamboom.domain.Administratie;
import stamboom.storage.DatabaseMediator;
import stamboom.storage.IStorageMediator;
import stamboom.storage.SerializationMediator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class StamboomController
{

    protected Administratie admin;
    private IStorageMediator storageMediator;
    /**
     * creatie van stamboomcontroller met lege administratie en onbekend
     * opslagmedium
     */
    public StamboomController()
    {
        admin = new Administratie();
        storageMediator = null;
        try
        {

            initDatabaseMedium();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Administratie getAdministratie()
    {
        return admin;
    }

    /**
     * administratie wordt leeggemaakt (geen personen en geen gezinnen)
     */
    public void clearAdministratie()
    {
        admin = new Administratie();
    }

    /**
     * administratie wordt in geserialiseerd bestand opgeslagen
     *
     * @param bestand
     * @throws IOException
     */
    public void serialize(File bestand) throws IOException
    {
        if(!bestand.exists()){
            bestand.createNewFile();
        }

        Properties props = new Properties();
        props.put("file", bestand.getAbsolutePath());
        
        storageMediator = new SerializationMediator();
        storageMediator.configure(props);
        storageMediator.save(admin);
    }

    /**
     * administratie wordt vanuit geserialiseerd bestand gevuld
     *
     * @param bestand
     * @throws IOException
     */
    public void deserialize(File bestand) throws IOException
    {
        if(!bestand.exists()){
            throw new IOException();
        }
        Properties props = new Properties();
        props.put("file", bestand.getAbsolutePath());
        
        storageMediator = new SerializationMediator();
        storageMediator.configure(props);
        admin = storageMediator.load();

    }

    private void initDatabaseMedium() throws IOException
    {
        if (!(storageMediator instanceof DatabaseMediator)) {
            Properties props = new Properties();
            try (FileInputStream in = new FileInputStream("Stamboom (start)/database.properties")) {
                props.load(in);
            }
            storageMediator = new DatabaseMediator(props);
        }
    }

    /**
     * administratie wordt vanuit standaarddatabase opgehaald
     *
     * @throws IOException
     */
    public void loadFromDatabase() throws IOException
    {
       admin = storageMediator.load();
    }

    /**
     * administratie wordt in standaarddatabase bewaard
     *
     * @throws IOException
     */
    public void saveToDatabase() throws IOException
    {
        storageMediator.dump();
        storageMediator.save(admin);

    }

}
