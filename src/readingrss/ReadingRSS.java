/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package readingrss;

 
import java.net.URL;
import java.util.Iterator;
 
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.util.ArrayList;
import java.util.Hashtable;



/**
 *
 * @author UPF
 */
public class ReadingRSS {

  public ArrayList<String> reading(String rssSite) throws Exception {

    ArrayList<String> links = new ArrayList<>();
    URL url  = new URL(rssSite);
    XmlReader reader = null;
    try {
        reader = new XmlReader(url);
        SyndFeed feed = new SyndFeedInput().build(reader);
        System.out.println("Feed Title: "+ feed.getTitle());

        for (Iterator i = feed.getEntries().iterator(); i.hasNext();) {
            SyndEntry entry = (SyndEntry) i.next();
            links.add(entry.getLink());
        }
    } finally {
        if (reader != null)
            reader.close();
    }
    return links;
  }
}
        
    
