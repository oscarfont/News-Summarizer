package callingSUMMA;

import gate.*;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;
import gate.persist.PersistenceException;
import gate.util.GateException;
import gate.util.OffsetComparator;
import gate.util.persistence.PersistenceManager;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Loading in JAVA a SUMMA application developed with the GATE GUI
 * and showing how to summarize a document.
 * @author UPF
 */
public class CallSUMMAGapp {
  
    public ArrayList<String> callSumma(CorpusController application, String link, String coding) {
        
        ArrayList<String> newsandImage;
        int index = 0;
        
        try {         
            // Create the Document
            Document doc;
            doc=Factory.newDocument(new URL(link), coding); 
                    
            // The corpus to store the document
            Corpus corpus=Factory.newCorpus("");
   
            // set controller with corpus
            application.setCorpus(corpus);
            
            // obtain the text of the news
            newsandImage= getTextNews(doc);
            Document cleanDocument=Factory.newDocument(newsandImage.get(index));
            
            corpus.add(cleanDocument);
            
            // execute the application
            application.execute();
            
            if(cleanDocument.getContent().toString().equals("")){
                newsandImage.set(index, "");
            }else{
                newsandImage.set(index, getSummary(cleanDocument));
            }
            
            // delete resources
            Factory.deleteResource(doc);
            Factory.deleteResource(cleanDocument);
            
            return newsandImage;
            
        } catch (IOException ex) {
            Logger.getLogger(CallSUMMAGapp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ResourceInstantiationException ex) {
            Logger.getLogger(CallSUMMAGapp.class.getName()).log(Level.SEVERE, null, ex);
        } catch(GateException ge) {
            ge.printStackTrace();
        }
        
        return null;
        
    }
    // this is just an example text. You will have to figure out how to
    // get the input text to be summarizer... not difficult at all!
    public ArrayList<String> getTextNews(Document doc) {
        ArrayList<String> textandLink = new ArrayList<>();
        String text = new String();
        String line;
        AnnotationSet paras;
        AnnotationSet images;
        FeatureMap fm;
        FeatureMap fmImages;
        paras = doc.getAnnotations("Original markups").get("p");
        images=doc.getAnnotations("Original markups").get("a");
        
        // Obtain Text News
        String dc=doc.getContent().toString();
        Long start, end;
        ArrayList<Annotation> list=new ArrayList(paras);
        Collections.sort(list, new OffsetComparator());
        Annotation para;
        for(int i=1;i<list.size();i++) {
            para=list.get(i);
            fm=para.getFeatures();
            if(fm.containsValue("mce")){
                start=para.getStartNode().getOffset();
                end=para.getEndNode().getOffset();
                line=dc.substring(start.intValue(), end.intValue());
                text=text+"\n"+line;
            }
        }
        textandLink.add(text);
        // Obtain image link
        ArrayList<Annotation> listImages=new ArrayList(images);
        Collections.sort(list, new OffsetComparator());
        Annotation image;
        String linkImage = "https://www.ara.cat";
        boolean stay = true;
        for(int i=1;i<listImages.size();i++) {
            image=listImages.get(i);
            fmImages=image.getFeatures();
            if(fmImages.containsValue("mg dummy lightbox") && stay){
                  if(!fmImages.get("href").toString().contains("https://www.ara.cat")){
                     linkImage = linkImage + fmImages.get("href").toString(); 
                  }else{
                      linkImage = fmImages.get("href").toString();
                  }
                  //System.out.println(linkImage);
                  stay = false;
            }
        }
        textandLink.add(linkImage);
        
        return textandLink;   
    }
    
    
    // NOTE: we are assuming the summary is in an annotation set called
    // EXTRACT
    // and we are assuming the sentence annotations are "Sentence"
    public String getSummary(Document doc) {
        String summary="";
        String dc=doc.getContent().toString();
        AnnotationSet sentences=doc.getAnnotations("EXTRACT").get("Sentence");
        // sort the annotations
        Annotation sentence;
        Long start, end;
        ArrayList<Annotation> sentList=new ArrayList(sentences);
        Collections.sort(sentList,new OffsetComparator());
        for(int s=0;s<sentList.size();s++) {
           
            sentence=sentList.get(s);
            start=sentence.getStartNode().getOffset();
            end  =sentence.getEndNode().getOffset();
            summary=summary+dc.substring(start.intValue(), end.intValue())+"\n";
        }
        
        return summary;
    } 
              
}