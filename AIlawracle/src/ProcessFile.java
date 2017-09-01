/*******************************************************
 * @author Zuoshan
 * @date 01/09/2017
 * A swing tool to transform docx to html,
 * search keyword in html and then italic the keyword.
 * Finally transform html back to docx.
 * 
 *******************************************************/

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xwpf.converter.core.XWPFConverterException;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
import org.docx4j.XmlUtils;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;

public class ProcessFile {
	private String doc;
	private String path;
	private String htmlPath;

	public ProcessFile() {
		this.doc = "";
		this.path=System.getProperty("user.dir");
		this.htmlPath=System.getProperty("user.dir");
	}

	public ProcessFile(String path){
		this.path=path;
		this.htmlPath=path.replaceAll(".docx", ".html");

		XWPFDocument Document=new XWPFDocument();
		OutputStream out = null;

		try {
			Document = new XWPFDocument(new FileInputStream(path));
			out = new FileOutputStream(new File(this.htmlPath));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XHTMLOptions options = XHTMLOptions.create();  
		options.setIgnoreStylesIfUnused(false);  
		options.setFragment(true);  

		try {
			XHTMLConverter.getInstance().convert(Document, out, options);
			XHTMLConverter.getInstance().convert(Document, baos, options);
			this.doc=baos.toString();
			out.close();
			baos.close();
		} catch (XWPFConverterException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public String getHtmlPath(){		
		return "file:"+this.htmlPath;
	}
	
	public String getText() throws FileNotFoundException{
		return this.doc;
	}
	
	public void flush() throws IOException {
		OutputStream out = new FileOutputStream(new File(this.htmlPath)); 	
		out.write(this.doc.getBytes());
		out.close();
	}
	
	public void SaveOutput(){
	
		String output = this.path.replaceAll(".docx", "_Output.docx");
		String inputfilepath = this.htmlPath;    	
		String baseURL = "file://"+inputfilepath.substring(0, inputfilepath.lastIndexOf('/'));
		try {
			//	      String stringFromFile;
			//		stringFromFile = FileUtils.readFileToString(new File(inputfilepath), "UTF-8");
			//	      String unescaped = stringFromFile;
			this.doc="<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" "
					+ "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"
					+ "\">\n<html>\n<body>"+this.doc+"\n</body>\n</html>";
			flush();
			System.out.println(this.doc);

			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();

			NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
			wordMLPackage.getMainDocumentPart().addTargetPart(ndp);
			ndp.unmarshalDefaultNumbering();		

			// Convert the XHTML, and add it into the empty docx we made
			XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(wordMLPackage);

			XHTMLImporter.setHyperlinkStyle("Hyperlink");
			wordMLPackage.getMainDocumentPart().getContent().addAll( 
					XHTMLImporter.convert(this.doc, baseURL) );
			//System.out.println("root folder:"+System.getProperty("user.dir"));
			wordMLPackage.save(new java.io.File(output) );
		} catch (Docx4JException | JAXBException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
	
	public String Space() {
		ArrayList<String> textStore = new ArrayList<String>(); // Store the all the text in original doc
		ArrayList<Integer> indexStore = new ArrayList<Integer>();//Store the index corresponds to the text position
		Pattern p = Pattern.compile("s[0-9]+[A-Z]+");
		return "sample";
	}
	
	//private List
	public ArrayList<Keyword> Legislation() {
		ArrayList<Keyword> textStore = new ArrayList<Keyword>(); // Store the all the text in original doc

		String space = "s[0-9]+[A-Z]*";
		String space2 = "section[0-9]+[A-Z]*";
		Pattern p = Pattern.compile("[A-Z]([a-z])*\\s(\\((([a-zA-Z])*|\\s)*\\)|\\s|[A-Z][a-z]*)*Act\\s[0-9]+\\s\\((Nsw|VIC|QLD|TAS|Cth|NT|ACT)\\)");
		Matcher m = p.matcher(this.doc);
		
		while(m.find()) {
			textStore.add(new Keyword(m.group(),m.start(),m.end()));
			System.out.println(m.group());
			System.out.println(m.start()+","+m.end());
		}
		return textStore;
	}
	
	//private List
	public ArrayList<Keyword> FindV() {
		//String sample="In a 1952 decision, Beauharnais v. Illinois,";//">ve In Cayuga Indian Nation of New York v. Pataki,NAACP, Inc. v. City of Niagara Falls,R. A. V. v. St. Paul ";
		ArrayList<Keyword> textStore = new ArrayList<Keyword>(); // Store the all the text in original doc
		//MaxentTagger tagger = new MaxentTagger("english-left3words-distsim.tagger");

		String capital = "[A-Z][a-zA-Z-_\u0027\u2019\u16F51\u055A\u02BC]*.?";
		String before_v="("+capital+"\\s)+"+"(of\\s)?"+"("+capital+"\\s)*";
		String after_v="("+capital+"\\s)*"+"(of\\s)?"+"("+capital+"\\s)*"+"("+capital+")";
		
		Pattern p = Pattern.compile(before_v+"v.?\\s"+after_v);
		Matcher m = p.matcher(this.doc);
		
		while(m.find()) {
			textStore.add(new Keyword(m.group(),m.start(),m.end()));
			System.out.println(m.group());
			System.out.println(m.start()+","+m.end());
		}
		return textStore;
	}
	
	public void Italic(ArrayList<Keyword> textStore) {//"<a><b><c>"
		String tmp = this.doc;
		String out = "";
		int front=0,end=0;
		
		for(Keyword x:textStore) {			
			out+=tmp.substring(front, x.getStart())+"<i>"+tmp.substring(x.getStart(),x.getEnd())+"</i>";
			front=x.getEnd();
			end=front;
		}
		out+=tmp.substring(end, tmp.length());
		this.doc=out;
		try {
			flush();   
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
