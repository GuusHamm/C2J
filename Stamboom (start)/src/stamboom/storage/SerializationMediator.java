/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stamboom.storage;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Properties;
import java.io.ObjectOutputStream;

import stamboom.domain.Administratie;

public class SerializationMediator implements IStorageMediator
{

    /**
     * bevat de bestandslocatie. Properties is een subclasse van HashTable, een
     * alternatief voor een List. Het verschil is dat een List een volgorde heeft,
     * en een HashTable een key/value index die wordt opgevraagd niet op basis van
     * positie, maar op key..
     */
    private Properties props;

    /**
     * creation of a non configured serialization mediator
     */
    public SerializationMediator()
    {
        props = null;
    }

    @Override
    public Administratie load() throws IOException
    {
        if (!isCorrectlyConfigured())
        {
            throw new RuntimeException("Serialization mediator isn't initialized correctly.");
        }
        
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        Object obj;
        Administratie admin = null;
        
        try {
            fis = new FileInputStream(props.getProperty("file"));
            ois = new ObjectInputStream(fis);
            
            while(true){
                try{
                    if ((obj = ois.readObject()) != null){
                        if(obj instanceof Administratie){
                            admin = (Administratie)obj;
                        }
                    } 
                }
                catch (EOFException e){
                    break;
                }
            }           
        }      
        catch (IOException | ClassNotFoundException e2){
            e2.printStackTrace();
        } 
        finally {
            ois.close();
            fis.close();
        }
        return admin;
    }

    @Override
    public void save(Administratie admin) throws IOException
    {
        if (!isCorrectlyConfigured())
        {
            throw new RuntimeException("Serialization mediator isn't initialized correctly.");
        }
        
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        
        try{
            fos = new FileOutputStream(props.getProperty("file"));
            oos = new ObjectOutputStream(fos);
        
            oos.writeObject(admin);  
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally{
            oos.close();
            fos.close();
        } 
    }

    /**
     * Laadt de instellingen, in de vorm van een Properties bestand, en controleert
     * of deze in de juiste vorm is.
     *
     * @param props
     * @return
     */
    @Override
    public boolean configure(Properties props)
    {
        this.props = props;
        return isCorrectlyConfigured();
    }

    @Override
    public Properties config()
    {
        return props;
    }

    /**
     * Controleert of er een geldig Key/Value paar bestaat in de Properties.
     * De bedoeling is dat er een Key "file" is, en de Value van die Key
     * een String representatie van een FilePath is (eg. C:\\Users\Username\test.txt).
     *
     * @return true if config() contains at least a key "file" and the
     * corresponding value is formatted like a file path
     */
    @Override
    public boolean isCorrectlyConfigured()
    {
        if (props == null)
        {
            return false;
        }
        return props.containsKey("file")
                && props.getProperty("file").contains(File.separator);
    }
}