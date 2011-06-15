
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.junit.*;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;

public class SearchTest extends UnitTest
{

    @Test
    public void searchTag()
    {
        // delete all Templates and Tags from Database
        for (Template item : Template.<Template>findAll())
        {
            item.delete();
        }
         
        
        Date now = new Date();
        Template t1 = new Template("template1", "tagtest.txt", "Hanni", now, "Whhoot?", 11);
        Template t2 = new Template("template2", "tagtest2.txt", "Hanni", now, "lala", 21);
        Template t3 = new Template("template3", "tagtest3.txt", "Hanni", now, "123", 1);

        List<String> tagList1 = new ArrayList();
        List<String> tagList2 = new ArrayList();
        List<String> tagList3 = new ArrayList();
        tagList1.add("t1");
        tagList1.add("findMe");
        tagList2.add("t2");
        tagList3.add("t3");
        
        t1.tagItWith(tagList1);
        t2.tagItWith(tagList2);
        t3.tagItWith(tagList3);
        
        t1.save();
        t2.save();
        t3.save();
        
        HashSet<Template> found = new HashSet<Template>();
        
        SearchHandler s = SearchHandler.getInstance();
        
        found = s.search("findMe");
        
        assertEquals("Found one Hashtag.", 1,found.size());
        
        found.clear();
        
        found = s.search("t");
        
        
        assertEquals("Found Three Hashtags.", 3,found.size());
        
        found.clear();
        
        
    }
    
}
