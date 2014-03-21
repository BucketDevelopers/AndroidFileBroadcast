package com.common.methods;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.res.AssetManager;
import android.util.Log;
 
public class XmlParser{
    String fname;
    String fpath;
    Document doc ;
    XMLDOMParser parser = new XMLDOMParser();
    AssetManager asset_manager;
    static Map <String,String> hm = new HashMap<String,String>();
    ArrayList<String> retList = new ArrayList<String>();
    // XML node names
    static final String NODE_FILE = "file";
    static final String NODE_NAME = "name";
    static final String NODE_PATH = "path";
    static File xmlPath;
	
	public XmlParser(File file){
		xmlPath = file;
		File xmlFile = new File(xmlPath,"list.xml");
		InputStream stream;
		try {
			if (xmlFile.createNewFile()){
	//			xmlFile.
				FileWriter fw = new FileWriter(xmlFile.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><list><file><name>some.mp3</name></file></list>");
				bw.close();
				stream = new FileInputStream(xmlFile);
				doc = parser.getDocument(stream);
		    
	 
			}
			else{
				stream = new FileInputStream(xmlFile);
				doc = parser.getDocument(stream);
				NodeList nodeList = doc.getElementsByTagName(NODE_FILE);

			        for (int i = 0; i < nodeList.getLength(); i++) {
			            Element e = (Element) nodeList.item(i);
			            fname= parser.getValue(e, NODE_NAME).toString();
			            fpath = parser.getValue(e, NODE_PATH).toString();
			            hm.put(fname,fpath);
			        }

			    
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> fileList(){
		 // Get elements by name file
        NodeList nodeList = doc.getElementsByTagName(NODE_FILE);

        for (int i = 0; i < nodeList.getLength(); i++) {
            Element e = (Element) nodeList.item(i);
            fname= parser.getValue(e, NODE_NAME).toString();
            fpath = parser.getValue(e, NODE_PATH).toString();
            hm.put(fname,fpath);
            retList.add(fname);
            Log.d("msg","hello");
        }
		return retList;
	}
	static public String getFilePath(String name)
	{
		return hm.get(name);
	}
	public void addFile(String fname,String fpath)
	{
		NodeList rootList = doc.getElementsByTagName("list");
		Node root = rootList.item(0);
		Element newFile = doc.createElement("file");
		Element newFname = doc.createElement("name");
		Element newFpath = doc.createElement("path");
		
		newFile.appendChild(newFname);
		newFname.appendChild(doc.createTextNode(fname));
		
		
		newFile.appendChild(newFpath);
		newFpath.appendChild(doc.createTextNode(fpath));
		
		root.appendChild(newFile);
		try {
			Transformer tFormer = TransformerFactory.newInstance().newTransformer();
         	tFormer.setOutputProperty(OutputKeys.METHOD, "xml");
         	Source source = new DOMSource(doc);
         	Log.d("source",source.toString());
         	File xfl= new File(xmlPath,"list.xml");
           	Result result = new StreamResult(xfl);
         
			tFormer.transform(source, result);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hm.put(fname, fpath);
	}
	static public Map<String,String> getFileMap()
	{
		return hm;
	}
	static public void checkXml(File fpath,String fname)
	{
		File xfl= new File(fpath,fname);
		if(!(xfl.exists()))
     	{
     		try{
     			xfl.createNewFile();
     			FileWriter fw = new FileWriter(xfl.getAbsoluteFile());
     			BufferedWriter bw = new BufferedWriter(fw);
     			bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><list></list>");
				bw.close();
     		}
     		catch(IOException e1){
     			e1.printStackTrace();
     		}
     	}
	}
}