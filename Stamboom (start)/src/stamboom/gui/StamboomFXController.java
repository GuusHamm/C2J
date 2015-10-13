/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stamboom.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import stamboom.controller.StamboomController;
import stamboom.domain.Geslacht;
import stamboom.domain.Gezin;
import stamboom.domain.Persoon;
import stamboom.util.StringUtilities;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;


/**
 * @author frankpeeters
 */
public class StamboomFXController extends StamboomController implements Initializable
{

    //MENUs en TABs
    @FXML
    MenuBar menuBar;
    @FXML
    MenuItem miNew;
    @FXML
    MenuItem miOpen;
    @FXML
    MenuItem miSave;
    @FXML
    CheckMenuItem cmDatabase;
    @FXML
    MenuItem miClose;
    @FXML
    Tab tabPersoon;
    @FXML
    Tab tabGezin;
    @FXML
    Tab tabPersoonInvoer;
    @FXML
    Tab tabGezinInvoer;

    //PERSOON
    @FXML
    ComboBox cbPersonen;
    @FXML
    TextField tfPersoonNr;
    @FXML
    TextField tfVoornamen;
    @FXML
    TextField tfTussenvoegsel;
    @FXML
    TextField tfAchternaam;
    @FXML
    TextField tfGeslacht;
    @FXML
    TextField tfGebDatum;
    @FXML
    TextField tfGebPlaats;
    @FXML
    ComboBox cbOuderlijkGezin;
    @FXML
    ListView lvAlsOuderBetrokkenBij;
    @FXML
    Button btStamboom;
    
    //GEZIN
    @FXML
    ComboBox cbGezinnen;
    @FXML
    TextField tfGezinNr;
    @FXML
    TextField tfOuder1;
    @FXML
    TextField tfOuder2;
    @FXML
    TextField tfHuwelijk;
    @FXML
    TextField tfScheiding;
    @FXML
    ListView lvKinderen;
    
    //INVOER PERSOON
    @FXML
    TextField tfVoornamenInvoer;
    @FXML
    TextField tfTussenvoegselInvoer;
    @FXML
    TextField tfAchternaamInvoer;
    @FXML
    ComboBox  cbGeslachtInvoer;
    @FXML
    TextField tfGeboortedatumInvoer;
    @FXML
    TextField tfGeboorteplaatsInvoer;
    @FXML
    ComboBox cbOuderlijkGezinInvoer;
    @FXML
    Button btOK;
    @FXML
    Button btCancel;

    //INVOER GEZIN
    @FXML
    ComboBox cbOuder1Invoer;
    @FXML
    ComboBox cbOuder2Invoer;
    @FXML
    TextField tfHuwelijkInvoer;
    @FXML
    TextField tfScheidingInvoer;
    @FXML
    Button btOKGezinInvoer;
    @FXML
    Button btCancelGezinInvoer;

    //opgave 4
    private boolean withDatabase;
    private StamboomController stamboomController;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        initComboboxes();
        
        withDatabase = false;
    }

    private void initComboboxes()
    {
        cbOuder1Invoer.setItems(admin.getPersonen());
        cbOuder2Invoer.setItems(admin.getPersonen());
        cbPersonen.setItems(admin.getPersonen());
        cbGezinnen.setItems(admin.getGezinnen());
        cbOuderlijkGezin.setItems(admin.getGezinnen());
        cbOuderlijkGezinInvoer.setItems(admin.getGezinnen());
        
        ObservableList<String> enumValues = FXCollections.observableArrayList();
        enumValues.add(Geslacht.MAN.toString().toLowerCase());
        enumValues.add(Geslacht.VROUW.toString().toLowerCase());
        cbGeslachtInvoer.setItems(enumValues);
    }

    public void selectPersoon(Event evt)
    {
        Persoon persoon = (Persoon) cbPersonen.getSelectionModel().getSelectedItem();
        showPersoon(persoon);
    }

    private void showPersoon(Persoon persoon)
    {
        if (persoon == null)
        {
            clearTabPersoon();
        } else
        {
            tfPersoonNr.setText(persoon.getNr() + "");
            tfVoornamen.setText(persoon.getVoornamen());
            tfTussenvoegsel.setText(persoon.getTussenvoegsel());
            tfAchternaam.setText(persoon.getAchternaam());
            tfGeslacht.setText(persoon.getGeslacht().toString());
            tfGebDatum.setText(StringUtilities.datumString(persoon.getGebDat()));
            tfGebPlaats.setText(persoon.getGebPlaats());
            if (persoon.getOuderlijkGezin() != null)
            {
                cbOuderlijkGezin.getSelectionModel().select(persoon.getOuderlijkGezin());
            } else
            {
                cbOuderlijkGezin.getSelectionModel().clearSelection();
            }
            lvAlsOuderBetrokkenBij.setItems(persoon.getAlsOuderBetrokkenIn());
        }
    }

    public void setOuders(Event evt)
    {
        if (tfPersoonNr.getText().isEmpty())
        {
            return;
        }
        Gezin ouderlijkGezin = (Gezin) cbOuderlijkGezin.getSelectionModel().getSelectedItem();
        if (ouderlijkGezin == null)
        {
            return;
        }

        int nr = Integer.parseInt(tfPersoonNr.getText());
        Persoon p = getAdministratie().getPersoon(nr);
        if (getAdministratie().setOuders(p, ouderlijkGezin))
        {
            showDialog("Success", ouderlijkGezin.toString()
                    + " is nu het ouderlijk gezin van " + p.getNaam());
        }

    }

    public void selectGezin(Event evt)
    {
        Gezin g = (Gezin)cbGezinnen.getSelectionModel().getSelectedItem();
        showGezin(g);
    }

    private void showGezin(Gezin gezin)
    {
        clearTabGezin();

        if (gezin == null)
        {
            return;
        }
        else
        {
            tfGezinNr.setText(gezin.getNr()+"");
            if(gezin.getOuder1()!=null)
                tfOuder1.setText(gezin.getOuder1().toString());
            if(gezin.getOuder2()!=null)
                tfOuder2.setText(gezin.getOuder2().toString());

            if(gezin.getHuwelijksdatum()!=null){
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                tfHuwelijk.setText(sdf.format(gezin.getHuwelijksdatum().getTime()));
            }
            if(gezin.getScheidingsdatum()!=null) {
                tfScheiding.setText(gezin.getScheidingsdatum().toString());
            }
            if(gezin.getKinderen().size() != 0){
                lvKinderen.setItems(gezin.getKinderen());
            }
        }
    }

    public void setHuwdatum(javafx.scene.input.KeyEvent evt)
    {
        Gezin g = (Gezin)cbGezinnen.getSelectionModel().getSelectedItem();

        if(evt.getCharacter().toString().toUpperCase()!="ENTER")
        {
            return;
        }

        if(g==null)
        {
            JOptionPane.showMessageDialog(null, "Geen gezin is geselecteerd");
        }
        else
        {
            String[] date = tfHuwelijk.getText().split("-");

            try
            {
                admin.setHuwelijk(g,
                        new GregorianCalendar(
                                Integer.parseInt(date[2]),
                                Integer.parseInt(date[1]),
                                Integer.parseInt(date[0])
                        )
                );
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, "Controleer of de datum in het correcte format is /nformat : DD-MM-YYYY");
            }
        }
    }

    public void setScheidingsdatum(javafx.scene.input.KeyEvent evt)
    {
        Gezin g = (Gezin)cbGezinnen.getSelectionModel().getSelectedItem();

        if(evt.getCharacter().toString().toUpperCase()!="ENTER")
        {
            return;
        }
        if(g==null )
        {
            JOptionPane.showMessageDialog(null, "Geen gezin is geselecteerd");
        }
        else
        {
            String[] date = tfHuwelijk.getText().split("-");

            try
            {
                admin.setScheiding(g,
                        new GregorianCalendar(
                                Integer.parseInt(date[2]),
                                Integer.parseInt(date[1]),
                                Integer.parseInt(date[0])
                        )
                );
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, "Controleer of de datum in het correcte format is /nformat : DD-MM-YYYY");
            }
        }

    }

    public void cancelPersoonInvoer(Event evt)
    {
        clearTabPersoonInvoer();

    }

    public void okPersoonInvoer(Event evt)
    {
        try {

            String voornamenString = tfVoornamenInvoer.getText();
            String[] voornamen = voornamenString.split(" ");
            String tussenvoegsel = tfTussenvoegselInvoer.getText();
            String achternaam = tfAchternaamInvoer.getText();
            Geslacht geslacht = Geslacht.valueOf(cbGeslachtInvoer.getSelectionModel().getSelectedItem().toString().toUpperCase());
            String geboortedatumString = tfGeboortedatumInvoer.getText();
            String[] date = geboortedatumString.split("-");
            GregorianCalendar geboortedatumCal = new GregorianCalendar(Integer.parseInt(date[2]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[0]));
            String geboorteplaats = tfGeboorteplaatsInvoer.getText();
            Gezin ouderlijkGezin = (Gezin) cbOuderlijkGezinInvoer.getSelectionModel().getSelectedItem();


            admin.addPersoon(geslacht, voornamen, achternaam, tussenvoegsel, geboortedatumCal, geboorteplaats, ouderlijkGezin);
            clearTabPersoonInvoer();
        } catch(Exception e){
            showDialog(null, "Er is iets misgegaan, controleer of je alles correct hebt ingevuld");
        }
    }

    public void okGezinInvoer(Event evt)
    {
        Persoon ouder1 = (Persoon) cbOuder1Invoer.getSelectionModel().getSelectedItem();
        if (ouder1 == null)
        {
            showDialog("Warning", "eerste ouder is niet ingevoerd");
            return;
        }
        Persoon ouder2 = (Persoon) cbOuder2Invoer.getSelectionModel().getSelectedItem();
        Calendar huwdatum;
        try
        {
            huwdatum = StringUtilities.datum(tfHuwelijkInvoer.getText());
        } catch (IllegalArgumentException exc)
        {
            showDialog("Warning", "huwelijksdatum :" + exc.getMessage());
            return;
        }
        Gezin g;
        if (huwdatum != null)
        {
            g = getAdministratie().addHuwelijk(ouder1, ouder2, huwdatum);
            if (g == null)
            {
                showDialog("Warning", "Invoer huwelijk is niet geaccepteerd");
            } else
            {
                Calendar scheidingsdatum;
                try
                {
                    scheidingsdatum = StringUtilities.datum(tfScheidingInvoer.getText());
                    if (scheidingsdatum != null)
                    {
                        getAdministratie().setScheiding(g, scheidingsdatum);
                    }
                } catch (IllegalArgumentException exc)
                {
                    showDialog("Warning", "scheidingsdatum :" + exc.getMessage());
                }
            }
        } else
        {
            g = getAdministratie().addOngehuwdGezin(ouder1, ouder2);
            if (g == null)
            {
                showDialog("Warning", "Invoer ongehuwd gezin is niet geaccepteerd");
            }
        }

        clearTabGezinInvoer();
    }

    public void cancelGezinInvoer(Event evt)
    {
        clearTabGezinInvoer();
    }


    public void showStamboom(Event evt)
    {
        Persoon persoon = (Persoon)cbPersonen.getSelectionModel().getSelectedItem();
        if(persoon == null){
            showDialog(null, "Geen persoon geselecteerd");
        }
        else{
            JOptionPane.showMessageDialog(null, persoon.stamboomAlsString());
        }
    }

    public void createEmptyStamboom(Event evt)
    {
        this.clearAdministratie();
        clearTabs();
    }


    public void openStamboom(Event evt)
    {
        File file = new File("admin");
        try{
            deserialize(file);
            initComboboxes();
        }catch(IOException e){
            e.printStackTrace();
        }

    }


    public void saveStamboom(Event evt)
    {
        File file = new File("admin");
        try{
            serialize(file);
        }catch(IOException e){
            e.printStackTrace();
        }

    }


    public void closeApplication(Event evt)
    {
        saveStamboom(evt);
        getStage().close();
    }


    public void configureStorage(Event evt)
    {
        withDatabase = cmDatabase.isSelected();
    }


    public void selectTab(Event evt)
    {
        Object source = evt.getSource();
        if (source == tabPersoon)
        {
            clearTabPersoon();
        } else if (source == tabGezin)
        {
            clearTabGezin();
        } else if (source == tabPersoonInvoer)
        {
            clearTabPersoonInvoer();
        } else if (source == tabGezinInvoer)
        {
            clearTabGezinInvoer();
        }
    }

    private void clearTabs()
    {
        clearTabPersoon();
        clearTabPersoonInvoer();
        clearTabGezin();
        clearTabGezinInvoer();
    }


    private void clearTabPersoonInvoer()
    {
        tfVoornamenInvoer.clear();
        tfTussenvoegsel.clear();
        tfAchternaamInvoer.clear();
        cbGeslachtInvoer.getSelectionModel().clearSelection();
        tfGeboortedatumInvoer.clear();
        tfGeboorteplaatsInvoer.clear();
        cbOuderlijkGezinInvoer.getSelectionModel().clearSelection();
    }


    private void clearTabGezinInvoer()
    {
        cbOuder1Invoer.getSelectionModel().clearSelection();
        cbOuder2Invoer.getSelectionModel().clearSelection();
        tfHuwelijkInvoer.clear();
        tfScheidingInvoer.clear();
    }

    private void clearTabPersoon()
    {
        cbPersonen.getSelectionModel().clearSelection();
        tfPersoonNr.clear();
        tfVoornamen.clear();
        tfTussenvoegsel.clear();
        tfAchternaam.clear();
        tfGeslacht.clear();
        tfGebDatum.clear();
        tfGebPlaats.clear();
        cbOuderlijkGezin.getSelectionModel().clearSelection();
        lvAlsOuderBetrokkenBij.setItems(FXCollections.emptyObservableList());
    }


    private void clearTabGezin()
    {
        cbGezinnen.getSelectionModel().clearSelection();
        tfGezinNr.clear();
        tfOuder1.clear();
        tfOuder2.clear();
        tfHuwelijk.clear();
        tfScheiding.clear();
        lvKinderen.setItems(FXCollections.emptyObservableList());

    }

    private void showDialog(String type, String message)
    {
        Stage myDialog = new Dialog(getStage(), type, message);
        myDialog.show();
    }

    private Stage getStage()
    {
        return (Stage) menuBar.getScene().getWindow();
    }

}
