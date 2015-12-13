package com.cameo;

import org.w3c.dom.*;
import javax.swing.*;
import javax.xml.parsers.*;
import java.net.URL;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // URL to obtain zip codes within a certain radius
        String zipURL = "https://www.zipwise.com/webservices/radius.php?" +
                "key=nton18af45ivozgj&zip=55426&radius=10&format=xml";
        ZipWorker worker = new ZipWorker(zipURL);
        worker.execute();
        Scanner s = new Scanner(System.in);
        s.nextLine();
    }
}

        class ZipWorker extends SwingWorker<Document, Void> {
            private String url;

            //Constructor - use to send data to your worker and do any initialization
            public ZipWorker(String urlRequest) { this.url = urlRequest; }

            @Override
            //This method is required. It must have the same return type as you specified in the class definition: ZipWorker extends SwingWorker<Document, Void>. This is where you'll do the time-consuming task.
            protected Document doInBackground(){
                try {
                    URL urlObject = new URL(url);
                    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    Document xmlDoc = builder.parse(urlObject.openStream());
                    return xmlDoc;
                }
                catch (Exception e) {
                    //All kinds of things can go wrong with the web request (wrong URL, no internet connection...) and
                    //with parsing the XML (malformed XML for example). A real app would do some more useful error handling.
                    System.out.println("Zip code radius request failed with exception " + e);
                    return null; //Since we have to return something
                }
            }

            @Override
            protected void done() {
                try {
                    Document xmlZipInfo = get(); //get() fetches whatever you returned from doInBackground.
                    System.out.println(xmlZipInfo);
                    if (xmlZipInfo != null) {
                        System.out.println("Here are the zip codes within the radius");
                        NodeList zipTexts = xmlZipInfo.getElementsByTagName("zip");
                        for (int x = 0; x < zipTexts.getLength(); x++) {
                            if (x >= 4) {
                                break; //stops loop after 4
                            }
                            Node node = zipTexts.item(x);
                            if (node.getNodeType() == Node.ELEMENT_NODE) {
                                Element element = (Element) node;
                                String depText = element.getFirstChild().getNodeValue();
                                //TODO how much of her code do I need here?
                                System.out.println(depText);
                            }
                        }
                    } else {
                        //xmlZipInfo was null, so there was soe kind of problem
                        System.out.println("No XML data to parse, see doInBackground's error message");
                    }
                } catch (Exception e) {
                    //Again, many things can go wrong here during parsing the XML, such as unexpected XML structure
                    System.out.println("Parsing XML failed with error " + e);
                }
            }
        }

