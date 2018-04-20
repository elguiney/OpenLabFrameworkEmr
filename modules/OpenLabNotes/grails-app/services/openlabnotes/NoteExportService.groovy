package openlabnotes

import org.docx4j.convert.in.xhtml.XHTMLImporterImpl
import org.docx4j.convert.out.pdf.PdfConversion
import org.docx4j.convert.out.pdf.viaXSLFO.Conversion
import org.docx4j.openpackaging.packages.WordprocessingMLPackage
import org.w3c.tidy.Tidy

class NoteExportService {

    def convertDocxToPdf(wordMLpackage){
        PdfConversion c = new Conversion(wordMLpackage)
        return(c)
    }

    def convertNoteToDocx(String note){
        //first convert HTML to well formed XHTML with jtidy
        def tidy = new Tidy()

        tidy.setXHTML(true)
        tidy.setForceOutput(true)

        def br = new BufferedReader(new StringReader(note));
        def sw = new StringWriter();

        tidy.parse(br, sw)
        def sb = sw.getBuffer()
        def xhtml = sb.toString()

        br.close()
        sw.close()

        //now we use doxc4j to parse xhtml to docx
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage()

        XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(wordMLPackage)

        wordMLPackage.getMainDocumentPart().getContent().addAll(
                XHTMLImporter.convert( xhtml, null) );

        return(wordMLPackage)
    }
}
