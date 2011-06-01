/*
 * COPYRIGHT INFORMATION
 * 
 * Developed by ALTernative + F4ntastic FOUR
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 * 
 */
package controllers;

import java.io.File;
import play.*;
import play.mvc.*;

import java.util.*;

import models.*;
import models.generate.Document;

import models.generate.DocumentGenerator;
import play.Play;
import play.data.validation.Required;
import play.mvc.results.Redirect;

public class Application extends Controller
{

    public static void index()
    {
        render();
    }

    @Before
    static void setConnectedUser()
    {
        if (Security.isConnected())
        {
            User user = User.find("email_", Security.connected()).first();
            if (user != null)
            {
                renderArgs.put("user", user);
            }
        }
    }

    public static void upload(String name, String tags, String description, File template, Boolean isHidden)
    {
        String upload = request.params.get("upload");
        Boolean success = false;

        String user = Security.connected();

        System.out.println("ISHIDDEN: " + request.params.get("isHidden"));

        String hidden = request.params.get("isHidden");

        if (hidden == null)
        {
            isHidden = false;
        }
        else
        {
            isHidden = true;
        }


        if (upload != null)
        {
            validation.clear();
            validation.required(name).message("Please insert a name.");
            validation.required(template).message("Please select a file.");

        }

        if (template != null)
        {
            List<String> tagList = null;
            tagList = createTags(tags);

            String error = Template.upload(name, description, template, user, isHidden);
            if (error == null)
            {
                Template uploaded = Template.find("name_", name).first();
                uploaded.tagItWith(tagList);
                success = true;

                for (Tag item : uploaded.tags)
                {
                    System.out.println("Counter: " + uploaded.tags.size());
                    System.out.println("Tags: " + item);
                }

            }
            else
            {
                Errors.displayInlineError(1, "Template has to be a plain-text file (encoded in UTF-8).", "../Application/upload");
            }
        }

        render(name, description, template, success);

    }

    public static List<String> createTags(String fullString)
    {
        String[] alltags = fullString.split(" ");


        List<String> tagList = new ArrayList<String>();
        tagList.addAll(Arrays.asList(alltags));
        return tagList;
    }

    public static void showAllTemplates()
    {

        // Template Test

        /*Template.deleteAll();       
        Template template = new Template("example", "example.txt", "niemand2", new Date(123456l), "Example config", 15000);
        template.calculateForm();      
        
        template.save();*/

        try
        {

            List<Template> all_templates = Template.findAll();

            render(all_templates);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            render("Application/upload.html");
        }

    }

    public static void showSingleTemplate(long id)
    {
        Template template = Template.findById(id);

        render(template);
    }

    public static void simpleLink()
    {
        String applicationPath = Play.applicationPath.getAbsolutePath();
        File templateFile = new File(applicationPath + "/data/test/SimpleDocument.txt");
        Map<String, String> replaceMap = new HashMap<String, String>();
        replaceMap.put("%name%", "John");


        DocumentGenerator generator = new DocumentGenerator(templateFile, replaceMap);
        models.generate.Document document = generator.create();
        String path = "/public/tmp/" + document.getFile().getName();
        render(path);
    }

    public static void selectedTemplate(Long id)
    {
        Template loadedTemplate = Template.findById(id);
        render(loadedTemplate);
    }

    public static void register()
    {
        render();
    }

    public static void insertionComplete()
    {
        //Template template = Template.findById(id);

        Map<String, String[]> map = request.params.all();
        Template template = Template.findById(Long.decode(map.get("ID")[0]));

        template.doMap(map);

        Iterator iterator = template.templates_.keySet().iterator();

        while (iterator.hasNext())
        {
            String key = (String) iterator.next();
            System.out.println("Key: " + key + " value: " + template.templates_.get(key));
        }

        DocumentGenerator generator = new DocumentGenerator(new File(Play.applicationPath.getAbsolutePath() + "/public/templates/" + template.filename_), template.getTemplates_());

        Document document = generator.create();

        System.out.println(document);
        System.out.println(document.getFile());
        System.out.println(document.getFile().getAbsolutePath());
        System.out.println(document.getContent());

//      document.getFile().delete();

        String path1 = document.getFile().getAbsolutePath();
        String playPath = Play.applicationPath.getAbsolutePath();



        path1 = path1.replaceAll("\\\\", "/");
        playPath = playPath.replaceAll("\\\\", "/");


        //template.pathToFilledFile = document.getFile().getAbsolutePath().replaceAll(Play.applicationPath.getAbsolutePath(), "");
        template.pathToFilledFile = path1.replaceAll(playPath, "");
        //template.pathToFilledFile = "public/tmp/1305726987921/test123.txt";

        render(template);



    }

    public static void generatePdf(String temp)
    {
        String path = Play.applicationPath.toString() + temp;
        path = path.replaceAll("\\\\", "/");
        File tex = new File(path);
        File dest = new File(tex.getParent());
        if (Helper.texToPdf(tex, dest))
        {
            String[] files = tex.getParent().split("tmp");
            System.out.println(files[1]);
            String name = tex.getName().substring(0, tex.getName().lastIndexOf("."));

            System.out.println("PDF successfully!");
            redirect("/public/tmp" + files[1] + "/" + name + ".pdf");
        }
        else
        {
            System.out.println("PDF createn failed!");
        }
    }
}
