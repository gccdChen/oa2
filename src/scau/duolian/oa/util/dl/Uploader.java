package scau.duolian.oa.util.dl;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class Uploader {

	private String Tag = "UPLOADER";
	private String urlString;// = "YOUR_ONLINE_PHP";
	HttpURLConnection conn;
	String exsistingFileName;

	private void uploadImageData(String existingFileName, String urlString) {
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		try {
			// ------------------ CLIENT REQUEST

			Log.e(Tag, "Inside second Method");

			FileInputStream fileInputStream = new FileInputStream(new File(
					exsistingFileName));

			// open a URL connection to the Servlet

			URL url = new URL(urlString);

			// Open a HTTP connection to the URL

			conn = (HttpURLConnection) url.openConnection();

			// Allow Inputs
			conn.setDoInput(true);

			// Allow Outputs
			conn.setDoOutput(true);

			// Don't use a cached copy.
			conn.setUseCaches(false);

			// Use a post method.
			conn.setRequestMethod("POST");

			conn.setRequestProperty("Connection", "Keep-Alive");

			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: post-data; name=uploadedfile;filename="
					+ exsistingFileName + "" + lineEnd);
			dos.writeBytes(lineEnd);

			Log.e(Tag, "Headers are written");

			// create a buffer of maximum size

			int bytesAvailable = fileInputStream.available();
			int maxBufferSize = 1000;
			// int bufferSize = Math.min(bytesAvailable, maxBufferSize);
			byte[] buffer = new byte[bytesAvailable];

			// read file and write it into form...

			int bytesRead = fileInputStream.read(buffer, 0, bytesAvailable);

			while (bytesRead > 0) {
				dos.write(buffer, 0, bytesAvailable);
				bytesAvailable = fileInputStream.available();
				bytesAvailable = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bytesAvailable);
			}

			// send multipart form data necesssary after file data...

			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			// close streams
			Log.e(Tag, "File is written");
			fileInputStream.close();
			dos.flush();
			dos.close();

		} catch (MalformedURLException ex) {
			Log.e(Tag, "error: " + ex.getMessage(), ex);
		}

		catch (IOException ioe) {
			Log.e(Tag, "error: " + ioe.getMessage(), ioe);
		}

		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = null;
		try {
			sp = spf.newSAXParser();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Get the XMLReader of the SAXParser we created.
		XMLReader xr = null;
		try {
			xr = sp.getXMLReader();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Create a new ContentHandler and apply it to the XML-Reader
		MyExampleHandler1 myExampleHandler = new MyExampleHandler1();
		xr.setContentHandler(myExampleHandler);

		// Parse the xml-data from our URL.
		try {
			xr.parse(new InputSource(conn.getInputStream()));
			// xr.parse(new InputSource(new java.io.FileInputStream(new
			// java.io.File("login.xml"))));
		} catch (MalformedURLException e) {
			Log.d("Net Disconnected", "NetDisconeeted");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			Log.d("Net Disconnected", "NetDisconeeted");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			Log.d("Net Disconnected", "NetDisconeeted");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Parsing has finished.

	}

	public Uploader(String existingFileName, boolean isImageUploading,
			String urlString) {

		this.exsistingFileName = existingFileName;
		this.urlString = urlString;

	}

	class MyExampleHandler1 extends DefaultHandler {
		// ===========================================================
		// Methods
		// ===========================================================

		@Override
		public void startDocument() throws SAXException {

		}

		@Override
		public void endDocument() throws SAXException {
			// Nothing to do
		}

		@Override
		public void startElement(String namespaceURI, String localName,
				String qName, Attributes atts) throws SAXException {

		}

		/**
		 * Gets be called on closing tags like: </tag>
		 */
		@Override
		public void endElement(String namespaceURI, String localName,
				String qName) throws SAXException {

		}

		/**
		 * Gets be called on the following structure: <tag>characters</tag>
		 */
		@Override
		public void characters(char ch[], int start, int length) {

		}
	}

}