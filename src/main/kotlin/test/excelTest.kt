package test

import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File

fun writeToExcel(){
    // Instantiate Excel Workbook
    val xlWb = XSSFWorkbook()
    // Instantiate Excel Worksheet
    val xlWs = xlWb.createSheet()

    val rowNumber = 0
    val colNumber = 0

    xlWs.createRow(rowNumber).createCell(colNumber).setCellValue("TEST")

    xlWs.getRow(0).createCell(1).cellFormula = "A2+B2"
    xlWs.createRow(1).createCell(0).setCellValue(8.0)
    xlWs.getRow(1).createCell(1).setCellValue(10.0)

    val outStream = File("test.xlsx").outputStream()
    xlWb.use { it.write(outStream) }
}


fun readFromExcel(){
    val inStream = File("test.xlsx").inputStream()

    // Instantiate Excel workbook using existing file:
    val xlWb = WorkbookFactory.create(inStream)

    val rowNumber = 0
    val colNumber = 0

    //Get reference to first sheet:
    val xlWs = xlWb.getSheetAt(0)

    val value = xlWs.getRow(rowNumber).getCell(colNumber)
    println(value)
}








/*

HPBF: This is one of the main components of apache poi, which stands for Horrible Publisher Format;
this component is used to read and write the files, which are MS-Publisher files specifically.

HSSF: This is another component that stands for Horrible Spreadsheet Format,
this type of component is particularly used to read and write the xls format of the MS Excel files we have.

HPSF: This is yet another component that stands for Horrible Property Set Format;
this component is basically used to extract the property of the MS Office files, which basically includes the property sets.

HSLF: This is another component that stands for Horrible Slide Layout Format;
this is basically used for the PowerPoint presentations, with the support to the operations like edit, create and read.

HDGF: This is another component that stands for Horrible Diagram Format;
this basically contains and handles the binary files; it internally contains methods
and classes to handle the MS Visio-related binary files.

POIFS: This is yet another com of apache poi, which stands for Poor Obfuscation Implementation File System;
this is considered as the basic component or the basic factor of all the poi elements we used.
If we want to read a different file type, we can use this by writing code explicitly.

HWPF: This is another component of apache poi that stands for Horrible Word Processor Format;
this component basically supports the MS word files with extension doc.

XSSF: This is another component of apache poi which stands for XML Spreadsheet Format;
this component is basically used to read the xlsx extension files of MS Excel.

XWPF: This is another component that stands for XML Word Processor Format;
this component is basically used to read and write the MS Word files with extension docx.

 */