/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package callingSUMMA;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import gate.*;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.persist.PersistenceException;
import gate.util.GateException;
import gate.util.OffsetComparator;
import gate.util.persistence.PersistenceManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author UPF
 */
public class SummarizingRSS {
   
    public void summarizer(CorpusController application, String rssSite, ArrayList<String> Links, String coding) {
        try {
        
            URL url;
            XmlReader reader = null;
            Corpus corpus;
            CallSUMMAGapp MySummarizer = new CallSUMMAGapp();
            ArrayList<String> currentSummary = new ArrayList<>();
            String currentLink;
            int index = 0;
            
            OutputStreamWriter osw;
            File fout=new File("."+File.separator+"output"+File.separator+"ARA_Esports.html");
            String header="<!DOCTYPE html>\n" +
                    "<head>"+
                    "<meta charset=\""+ coding +"\">"+
                    "</head>"+
                    "<html>\n" +
                    "<title>ÚLTIMA HORA ESPORTS</title>\n"+
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n"+
                    "<link rel=\"stylesheet\" href=\"https://www.w3schools.com/w3css/4/w3.css\">\n"+
                    "<body>\n" +
                    "<div class=\"w3-container\">\n"+
                        "<h2 class=\"w3-panel w3-leftbar w3-border-light-blue\">"+
                            "<a href=\"https://www.ara.cat/\" target=\"_blank\">"+
                                "<img src=\"http://www.vectorlogo.es/wp-content/uploads/2016/02/logo-vector-ara-cat.jpg\" style=\"width:15%\" align=\"right\">"+
                            "</a>\n"+
                            "<a href=\"https://www.ara.cat/esports/\" target=\"_blank\">"+
                                "<img src=\"https://www.ara.cat/bbtfile/4001_20150622tEbf6P.jpg?hash=26edb2c7008f66e0971a80a106b9b2d0c5a70897\" style=\"width:60%\" align=\"left\">"+
                            "</a>\n"+
                        "</h2>\n"+
                    "</div>\n"+
                    "<div class=\"w3-content w3-display-container\" style=\"width:59.3%\">";
            
            FileOutputStream writer=new FileOutputStream(fout);
            osw=new OutputStreamWriter(writer, coding);
      
            osw.append(header+"\n");
            osw.flush();
            
            String title;
            
            try {                
                url  = new URL(rssSite);
                reader = new XmlReader(url);
                SyndFeed feed = new SyndFeedInput().build(reader);
                //  The corpus to store the document
                corpus=Factory.newCorpus("");
                int count=0;
                Iterator i=feed.getEntries().iterator();
                
                while(i.hasNext() && count<10) {        

                    currentLink = Links.get(count);
                    SyndEntry entry = (SyndEntry) i.next();

                    title=entry.getTitle();

                    currentSummary = MySummarizer.callSumma(application, currentLink, coding);

                    if(!currentSummary.get(index).equals("")){
                        addNewsSlide(osw, currentSummary.get(index+1), title, currentSummary.get(index), currentLink);
                    }
                    count++;
                }
                finishHtml(osw);
  
            } catch (IllegalArgumentException | FeedException | IOException | ResourceInstantiationException ex) {
                Logger.getLogger(SummarizingRSS.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(SummarizingRSS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    // Function to add a slide with a picture, a sumary, a header and a link to the final html file.
    public void addNewsSlide(OutputStreamWriter osw, String imageLink, String Title, String Summary, String NewsLink){
        String Slide =  "<div class=\"w3-display-container mySlides\">"+
                            "<div class=\"w3-display-bottom w3-large w3-container w3-padding-16 w3-light-blue\" align=\"center\">"+
                                "<b>" + Title + "</b>\n" +
                            "</div>\n"+
                            "<a href=\"" + NewsLink + "\" target=\"_blank\">"+
                                "<img src=\"" + imageLink + "\" width=\"800\" height=\"500\">"+
                            "</a>\n"+
                            "<div class=\"w3-display-bottom w3-large w3-container w3-padding-16 w3-light-blue\" align=\"justify\" >"+
                                "<i>" + Summary + "</i>\n" +
                                "<a href=\"" + NewsLink + "\" target=\"_blank\">"+
                                    " [+info]"+
                                "</a>\n"+
                            "</div>\n"+
                        "</div>\n";
        try {
            osw.append(Slide);
            osw.flush();
        } catch (IOException ex) {
            Logger.getLogger(SummarizingRSS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Function to end the html file, adding the buttons and scripts to make the web work.
    public void finishHtml(OutputStreamWriter osw){
        String footer = "<button class=\"w3-button w3-display-topleft w3-light-blue\" style=\"height:6.6%\" onclick=\"plusDivs(-1)\">&#10094;</button>\n"+
                        "<button class=\"w3-button w3-display-topright w3-light-blue\" style=\"height:6.6%\" onclick=\"plusDivs(1)\">&#10095;</button>\n"+

                        "</div>\n"+

                        "<script>"+
                        "var slideIndex = 1;\n"+
                        "showDivs(slideIndex);\n"+

                        "function plusDivs(n) {\n"+
                          "showDivs(slideIndex += n);\n"+
                        "}\n"+

                        "function showDivs(n) {\n"+
                          "var i;"+
                          "var x = document.getElementsByClassName(\"mySlides\");\n"+
                          "if (n > x.length) {slideIndex = 1}\n"+
                          "if (n < 1) {slideIndex = x.length}\n"+
                          "for (i = 0; i < x.length; i++) {\n"+
                             "x[i].style.display = \"none\";\n"+
                          "}\n"+
                          "x[slideIndex-1].style.display = \"block\";\n"+
                        "}\n"+
                        "</script>\n"+

                        "</body>\n"+
                        "</html>\n";
        try {
            osw.append(footer);
            osw.flush();
            osw.close();
        } catch (IOException ex) {
            Logger.getLogger(SummarizingRSS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
