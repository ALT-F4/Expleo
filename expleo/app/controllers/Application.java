package controllers;

import java.io.File;
import play.*;
import play.mvc.*;

import java.util.*;

import models.*;
import play.data.validation.Required;

public class Application extends Controller
{
    public static void index()
    {

    render();
    }

    public static void upload(String name, String description, File template)
    {
        if (template != null)
        {
            if (Template.upload(name, description, template))
            {
                flash.success("Template successfully uploaded.", null);
            }
        }
        render();
    }

    public static void showAllTemplates()
    {
        try
        {
            List<Template> all_templates = Template.findAll();
            render(all_templates);
        }
        catch (Exception e)
        {
            render("Application/upload.html");
        }

    }

    public static void showSingleTemplate(long id)
    {
        Template template = Template.findById(id);

        render(template);
    }


  public static void selectedTemplate(Long id)
  {
    Template loadedTemplate = Template.findById(id);
 
    render(loadedTemplate);
  }

  public static void insertionComplete()
  {
    //Template template = Template.findById(id);

    Map<String,String[]> map = request.params.all();
    Template template = Template.findById(Long.decode(map.get("ID")[0]));

    template.doMap(map);

    Iterator iterator = template.templates_.keySet().iterator();

    while(iterator.hasNext())
    {
      String key = (String) iterator.next();
      System.out.println("Key: "+key+" value: "+ template.templates_.get(key));
    }

  }

}
