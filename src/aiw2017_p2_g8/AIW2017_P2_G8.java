/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aiw2017_p2_g8;

import callingSUMMA.SummarizingRSS;
import gate.CorpusController;
import gate.Gate;
import gate.util.persistence.PersistenceManager;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import readingrss.ReadingRSS;

/**
 *
 * @author U124283
 */
public class AIW2017_P2_G8 {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        ArrayList<String> newsLinks;
        String gappToTest = "MySUMMARIZER.gapp";
        ReadingRSS myReader = new ReadingRSS();
        String rssSite = "https://www.ara.cat/rss/esports/";
        String coding = "UTF-8";
        SummarizingRSS mySummarizer = new SummarizingRSS();
        
        try {
            Gate.init();
          
            // load the GAPP 
             CorpusController application =
            (CorpusController)
                            PersistenceManager.loadObjectFromFile(new 
                     File("./gapps"+File.separator+gappToTest));
            newsLinks = myReader.reading(rssSite);
            
            mySummarizer.summarizer(application, rssSite, newsLinks, coding);
            
            System.out.println("*********************************************");
            System.out.println("");
            System.out.println("HTML SUCCESSFULLY CREATED!");
            System.out.println("");
            System.out.println("*********************************************");
           
        } catch (InterruptedException ex) {
            Logger.getLogger(AIW2017_P2_G8.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
