/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stamboom.storage;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import stamboom.domain.Administratie;
import stamboom.domain.Gezin;
import stamboom.domain.Persoon;

public class DatabaseMediator implements IStorageMediator
{
    private Properties props;
    private Connection conn;

    public DatabaseMediator(Properties props)
    {
        this.props = props;
        configure(props);
    }

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

        try
        {
            initConnection();

            for (Persoon p : admin.getPersonen()){
                Statement statement = null;

                statement = conn.createStatement();
                // set timeout to 30 sec
                statement.setQueryTimeout(30);

                String ouderlijkGezin = "";

                if (p.getOuderlijkGezin()!= null){
                ouderlijkGezin = String.valueOf(p.getOuderlijkGezin().getNr());
                }


                String query = String.format("INSERT INTO `Persoon` VALUES(%d,'%s','%s','%s','%s','%s','%s','%s');",p.getNr(),p.getVoornamen(),p.getAchternaam(),p.getTussenvoegsel(),p.getGebDat().getTime().toString(),p.getGebPlaats(),p.getGeslacht().toString(),ouderlijkGezin);
                statement.executeUpdate(query);

//                if (p.getOuderlijkGezin() != null){
//                    query = String.format("UPDATE PERSOON SET ouders = %d where persoonsnummer = %d",p.getOuderlijkGezin().getNr(),p.getNr());
//                    statement.executeUpdate(query);
//                }

            }

            for (Gezin g : admin.getGezinnen()){
                Statement statement = null;

                statement = conn.createStatement();
                // set timeout to 30 sec
                statement.setQueryTimeout(30);

                String huwelijksDatum = "";
                String scheidingsDatum = "";
				String ouder2 = "";

				if (g.getOuder2()!=null){
					ouder2 = String.valueOf(g.getOuder2().getNr());
				}
                if (g.getHuwelijksdatum()!= null){
                   huwelijksDatum = g.getHuwelijksdatum().getTime().toString();
                }

                if (g.getScheidingsdatum()!= null){
                    scheidingsDatum = g.getScheidingsdatum().getTime().toString();
                }

                String query = String.format("INSERT INTO `Gezin` VALUES(%d,%s,%s,'%s','%s');",g.getNr(),g.getOuder1().getNr(),ouder2,huwelijksDatum,scheidingsDatum);
                statement.executeUpdate(query);

//                if (g.getOuder2() != null){
//                    query = String.format("UPDATE GEZIN SET ouder2 = %d where gezinsnummer = %d ;",g.getOuder2().getNr(),g.getNr());
//                    statement.executeUpdate(query);
//                }

//                if (g.getHuwelijksdatum()!= null){
//                    query = String.format("UPDATE GEZIN SET huwelijksdatum = '%s' where gezinsnummer = %d ;",g.getHuwelijksdatum().getTime().toString(),g.getNr());
//                    statement.executeUpdate(query);
//                }
//                if (g.getScheidingsdatum() != null){
//                    query = String.format("UPDATE GEZIN SET scheidingsdatum = '%s' where gezinsnummer = %d ;",g.getScheidingsdatum().getTime().toString(),g.getNr());
//                    statement.executeUpdate(query);
//                }

            }

        }
        catch (SQLException e)
        {

            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        finally
        {
           // closeConnection();

        }



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
        Class.forName(props.getProperty("driver"));

        try
        {
            // create a database connection
            conn = DriverManager.getConnection(props.getProperty("url"));
            Statement statement = conn.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.



            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `Persoon`(\n" +
                                            "\t`persoonsNummer`\tINTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                                            "\t`achternaam`\tTEXT NOT NULL,\n" +
                                            "\t`voornamen`\tTEXT NOT NULL,\n" +
                                            "\t`tussenvoegsel`\tTEXT NOT NULL,\n" +
                                            "\t`geboortedatum`\tTEXT NOT NULL,\n" +
                                            "\t`geboorteplaats`\tTEXT NOT NULL,\n" +
                                            "\t`geslacht`\tTEXT NOT NULL,\n" +
                                            "\t`ouders`\tTEXT,\n" +
                                            "\t FOREIGN KEY(ouders)\tREFERENCES gezin(gezinsnummer)\n" +
                                            ");");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS  `Gezin` (\n" +
                                            "\t`gezinsNummer`\tINTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                                            "\t`ouder1`\tINTEGER NOT NULL,\n" +
                                            "\t`ouder2`\tINTEGER ,\n" +
                                            "\t`huwelijksdatum`\t TEXT,\n" +
                                            "\t`scheidingsdatum`\tTEXT,\n" +
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
