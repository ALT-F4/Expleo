/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.eclipse.jdt.core.dom.ThisExpression;

/**
 *
 * @author benedikt
 */
public class SearchHandler
{
    private static SearchHandler instance_;
    private SearchHandler()
    {
        
    }
    
    public static SearchHandler getInstance()
    {
        if(instance_ == null)
        {
            instance_ = new SearchHandler();
        }
        
        return instance_;
    }
    
    public HashSet<Template> search(String searchkey)
    {
        System.out.println("SearchString:" + searchkey);
        List<Template> t = new ArrayList<Template>();
        
        //templates = Template.find("SELECT DISTINCT * FROM Template as tp, Tag as t WHERE tp.name_ = ? OR t.name = ? ", search, search).fetch();
        
        t = Template.find("byName_Like", "%"+searchkey+"%").fetch();
        
        t.addAll(Template.findTaggedWith(searchkey));
        
        HashSet<Template> templates = new HashSet<Template>(t);
        
        System.out.println("Anzahl gefunden hash: " + t.size());       
        System.out.println("Anzahl gefunden: " + templates.size());
        
        return templates;

    }
    
}
