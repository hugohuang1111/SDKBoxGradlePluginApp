package com.sdkbox.gradle.utils

import jdk.internal.org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler

import javax.xml.parsers.ParserConfigurationException
import javax.xml.parsers.SAXParser
import javax.xml.parsers.SAXParserFactory

public class Utils {

    public static List<String> findActivitys(String file) {
        List<String> activities = new ArrayList<String>()

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance()
        try {
            def manifest = new File(file)
            if (!manifest.exists()) {
                return activities
            }
            SAXParser saxParser = saxParserFactory.newSAXParser()
            saxParser.parse(new File(file), new DefaultHandler() {
                @Override
                void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws org.xml.sax.SAXException {
                    super.startElement(uri, localName, qName, attributes)

                    if ('activity'.equals(qName)) {
                        def activityName = attributes.getValue('android:name')
                        if (null != activityName && 0 != activityName.length()) {
                            activities.add(activityName)
                        }
                    }
                }

                @Override
                void endDocument() throws org.xml.sax.SAXException {
                    super.endDocument()
                }

                @Override
                void characters(char[] ch, int start, int length) throws org.xml.sax.SAXException {
                    super.characters(ch, start, length)
                }
            })
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return activities
    }
}


