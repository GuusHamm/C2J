/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stamboom.storage;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import stamboom.domain.Administratie;

public class DatabaseMediator implements IStorageMediator
{

    private Properties props;
    private Connection conn;

    @Override
    public Administratie load() throws IOException
    {
        //todo opgave 4
        return null;
    }

    @Override
    public void save(Administratie admin) throws IOException
    {
        //todo opgave 4

    }

    /**
     * Laadt de instellingen, in de vorm van een Properties bestand, en controleert
     * of deze in de correcte vorm is, en er verbinding gemaakt kan worden met
     * de database.
     *
     * @param props
     * @return
     */
    @Override
    public final boolean configure(Properties props)
    {
        this.props = props;
        if (!isCorrectlyConfigured())
        {
            System.err.println("props mist een of meer keys");
            return false;
        }

        try
        {
            initConnection();
            return true;
        } catch (SQLException ex)
        {
            System.err.println(ex.getMessage());
            this.props = null;
            return false;
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            closeConnection();
        }
    }

    @Override
    public Properties config()
    {
        return props;
    }

    @Override
    public boolean isCorrectlyConfigured()
    {
        if (props == null)
        {
            return false;
        }
        if (!props.containsKey("driver"))
        {
            return false;
        }
        if (!props.containsKey("url"))
        {
            return false;
        }
        if (!props.containsKey("username"))
        {
            return false;
        }
        if (!props.containsKey("password"))
        {
            return false;
        }
        return true;
    }

    private void initConnection() throws SQLException, ClassNotFoundException
    {
        // TODO opgave 4
        Class.forName("org.sqlite.JDBC");

        Connection connection = null;
        try
        {
            // create a database connection
            connection = DriverManager.getConnection(props.getProperty("url"));
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.



            statement.executeUpdate("CREATE TABLE `Persoon` (\n" +
                                            "\t`persoonsNummer`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                                            "\t`achternaam`\tTEXT,\n" +
                                            "\t`voornamen`\tTEXT,\n" +
                                            "\t`tussenvoegsel`\tTEXT,\n" +
                                            "\t`geboortedatum`\tTEXT,\n" +
                                            "\t`geboorteplaats`\tTEXT,\n" +
                                            "\t`geslacht`\tTEXT,\n" +
                                            "\t`ouders`\tTEXT NOT NULL,\n" +
                                            "\t FOREIGN KEY(ouders)\tREFERENCES gezin(gezinsnummer)\n" +
                                            ");");
            statement.executeUpdate("CREATE TABLE `Gezin` (\n" +
                                            "\t`gezinsNummer`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                                            "\t`ouder1`\tTEXT,\n" +
                                            "\t`ouder2`\tTEXT NOT NULL,\n" +
                                            "\t`huwelijksdatum`\tTEXT,\n" +
                                            "\t`scheidingsdatum`\tTEXT NOT NULL,\n" +
                                            "\t FOREIGN KEY(ouder1)\tREFERENCES persoon(persoonsnummer)\n," +
                                            "\t FOREIGN KEY(ouder2)\tREFERENCES persoon(persoonsnummer)\n" +
                                            ");");
        }
        catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        finally
        {
            try
            {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e)
            {
                // connection close failed.
                System.err.println(e);
            }
        }

    }

    private void closeConnection()
    {
        try
        {
            conn.close();
            conn = null;
        } catch (SQLException ex)
        {
            System.err.println(ex.getMessage());
        }
    }
}
