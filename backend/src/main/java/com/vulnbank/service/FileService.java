package com.vulnbank.service;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.StringReader;

@Service
public class FileService {

    // VULN-ID: VB-009 | CWE-502 | Severity: CRITICAL
    // Unsafe deserialization: deserializes raw object stream from user-controlled
    // input with no class filtering, allowing remote code execution via gadget chains.
    // SAFE VERSION: use ValidatingObjectInputStream with an explicit class allowlist:
    //   ValidatingObjectInputStream vois = new ValidatingObjectInputStream(inputStream);
    //   vois.accept(SafeClass.class);
    //   Object obj = vois.readObject();
    public Object deserializeObject(InputStream inputStream) throws Exception {
        ObjectInputStream ois = new ObjectInputStream(inputStream);
        Object obj = ois.readObject();
        return obj;
    }

    // VULN-ID: VB-010 | CWE-611 | Severity: HIGH
    // XXE: XML parsed with external entity processing enabled, allowing SSRF and
    // local file disclosure via crafted DOCTYPE declarations.
    // SAFE VERSION:
    //   dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    //   dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    //   dbf.setXIncludeAware(false);
    //   dbf.setExpandEntityReferences(false);
    public Document parseXml(String xmlInput) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        dbf.setXIncludeAware(false);
        dbf.setExpandEntityReferences(false);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new InputSource(new StringReader(xmlInput)));
        return doc;
    }
}
