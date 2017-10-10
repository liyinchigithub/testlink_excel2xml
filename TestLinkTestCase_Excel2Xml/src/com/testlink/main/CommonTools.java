package com.testlink.main;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

//import jxl.Cell;
//import jxl.Sheet;
//import jxl.Workbook;
//import jxl.read.biff.BiffException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.web.multipart.MultipartFile;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.testlink.vo.InputVo;

public class CommonTools 
{
	public void exchangeXml(String filepath,String savepath) throws DocumentException, IOException
	{
		//����Դ�ļ�����ȡԴ�ļ�������testcase��ȡֵ���������arraylist��
		CommonTools ct = new CommonTools();
		ArrayList<InputVo> iv = ct.analysisExcel(filepath);
		Document doc = DocumentHelper.createDocument();
		Util ut = new Util();
		POIUtil pl = new POIUtil();
		//���ø��ڵ�
		Element firstpath = doc.addElement("testsuite");
		firstpath.addAttribute("name",iv.get(0).getFirstpath());
		Element node_order = firstpath.addElement("node_order");
		node_order.setText(ut.createId(0));


        //��arraylist��������в��ظ��Ķ���Ŀ¼
        ArrayList<Element> secondpathlist = new ArrayList<Element>();
        ArrayList<Element> thirdpathlist = new ArrayList<Element>();
        ArrayList<String> secondpathlistvalue = new ArrayList<String>();
        ArrayList<String> thirdpathlistvalue = new ArrayList<String>();

        //ArrayList<InputVo> iv��index
        int ivINDEX = 0;
        //ArrayList<String> secondpathlist��Index
        int secondpathlistINDEX = 0;
        //ArrayList<String> thirdpathlist ��index
        int thirdpathlistINDEX = 0;
		/**
		 * ѭ���������е�testcase����ϸ������£�
		 * 1������testcase��һ��Ŀ¼��������ͬ�ģ�ȡ��һ��vo��Firstpath���ɣ�
		 * 2������Ŀ¼�Ĵ���ϸ��ӣ�����Ŀ���ǰ�iv�����в��ظ���Ŀ¼ȡ������ΪXML�Ľڵ㣬һ����7�����������Ϸ��������崦���߼����£�
		 *    A. iv.index>1���ѭ�������inputvo.getSecondpath����secondpathlistvalue<j=0>��֤����ͬһ������Ŀ¼�������½��µĶ���xml�ڵ㣬
		 *       ���inputvo.getSecondpath������secondpathlist<j=0>��֤�����µĶ���Ŀ¼�����½��µĶ���xml�ڵ㣬
		 *       ��inputvo.getSecondpath��ֵ��secondpathlistvalue<j++>�������inputvo.getSecondpath����secondpathlistvalue<j++>���Աȣ�
		 *       ���Ұѵ�ǰ��element���浽secondpathlist�У���������Ŀ¼����ͨ��secondpathlist��ȡ����һ���ڵ��׼ȷλ��
		 *    ע�����ڶ���Ŀ¼��ͬ����һ��һ��Ŀ¼����ʵ�����һ�����Բ������������������ͬ�������ԣ��������ж���Ŀ¼��ͬ���������������ģ�������
		 *        secondpathlist<j++>��secondpathlist<i(i<j)>��ͬ�����������Ŀ¼Ҳ��
		 * 3������Ŀ¼�Ĵ���ͬ����Ŀ¼��
		 * 4��testcase�������ֶη�װ��һ����������insetSourceValueIntoTarget��ÿ��ѭ������һ�μ��ɡ�
		 */
		InputVo vifirst = iv.get(0);
        //secondpath��ֵ
        Element secondpathfirst = firstpath.addElement("testsuite");
        secondpathfirst.addAttribute("name", vifirst.getSecondpath());
        Element node_order_2_first = secondpathfirst.addElement("node_order");
        node_order_2_first.setText("10"+String.valueOf(secondpathlistINDEX+1));
        //secondpathlist��һ��Ԫ��
        secondpathlist.add(secondpathfirst);
        secondpathlistvalue.add(vifirst.getSecondpath());

        //thirdpath��ֵ
        Element thirdpath = secondpathlist.get(secondpathlistINDEX).addElement("testsuite");
        thirdpath.addAttribute("name", vifirst.getThirdpath());
        ct.insetSourceValueIntoTarget(vifirst, thirdpath,ivINDEX);
        //thirdpathlist��һ��Ԫ��
        thirdpathlist.add(thirdpath);
        thirdpathlistvalue.add(vifirst.getThirdpath());


		for (InputVo inputvo : iv)
		{
			if (ivINDEX == 0)
			{
				//do nothing
			}

            else if (ivINDEX != 0 && inputvo.getSecondpath().equals(secondpathlistvalue.get(secondpathlistINDEX)))
            {
                if(inputvo.getThirdpath().equals(thirdpathlistvalue.get(thirdpathlistINDEX)))
                {
                    ct.insetSourceValueIntoTarget(inputvo, thirdpathlist.get(thirdpathlistINDEX),ivINDEX);
                }
                else
                {
                    //thirdpath��ֵ
                    Element thirdpathnext = secondpathlist.get(secondpathlistINDEX).addElement("testsuite");
                    thirdpathnext.addAttribute("name", inputvo.getThirdpath());
                    thirdpathlist.add(thirdpathnext);
                    thirdpathlistvalue.add(inputvo.getThirdpath());
                    thirdpathlistINDEX++;
                    ct.insetSourceValueIntoTarget(inputvo, thirdpathlist.get(thirdpathlistINDEX),ivINDEX);
                }
            }
            else
            {
                Element secondpath = firstpath.addElement("testsuite");
                secondpath.addAttribute("name", inputvo.getSecondpath());
                Element node_order_2 = secondpath.addElement("node_order");
                node_order_2.setText("10"+String.valueOf(secondpathlistINDEX+2));
                secondpathlistvalue.add(inputvo.getSecondpath());
                secondpathlist.add(secondpath);
                secondpathlistINDEX++;

                Element thirdpathnext = secondpathlist.get(secondpathlistINDEX).addElement("testsuite");
                thirdpathnext.addAttribute("name", inputvo.getThirdpath());
                thirdpathlist.add(thirdpathnext);
                thirdpathlistvalue.add(inputvo.getThirdpath());
                thirdpathlistINDEX++;
                ct.insetSourceValueIntoTarget(inputvo, thirdpathlist.get(thirdpathlistINDEX),ivINDEX);
            }
            ivINDEX++;
		}
		//���Ϊ�ļ�
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("GBK");
//		format.setEncoding("UTF-8");
		File file = new File(savepath);
		XMLWriter writer = new XMLWriter(new FileOutputStream(file),format);
		writer.write(doc);
	}

	public void replaceTxtByStr(String filepath, String oldStr,String replaceStr) throws IOException
    {
        String temp = "";
        try {
            File file = new File(filepath);
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuffer buf = new StringBuffer();

            // �������ǰ�������
            for (int j = 1; (temp = br.readLine()) != null
                    && !temp.equals(oldStr); j++) {
                buf = buf.append(temp);
                buf = buf.append(System.getProperty("line.separator"));
            }

            // �����ݲ���
            buf = buf.append(replaceStr);

            // ������к��������
            while ((temp = br.readLine()) != null) {
                buf = buf.append(System.getProperty("line.separator"));
                buf = buf.append(temp);
            }

            br.close();
            FileOutputStream fos = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(fos);
            pw.write(buf.toString().toCharArray());
            pw.flush();
            pw.close();
            System.out.print("replace over..");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
	
	public ArrayList<InputVo> analysisXml(String filepath) throws DocumentException
	{
		ArrayList<InputVo> iv = new ArrayList<InputVo>();
		SAXReader reader = new SAXReader();
		Document doc = reader.read(new File(filepath));
		Element root = doc.getRootElement();
		Iterator it = root.elementIterator();
		
		while (it.hasNext())
		{
			//��ȡÿ��testcase�������ֶ�ȡֵ����ֵ��InputVo��Ȼ�����List�д��
			Element element = (Element) it.next();
			InputVo inputvo = new InputVo();
			inputvo.setFirstpath(element.elementText("firstpath"));
			inputvo.setSecondpath(element.elementText("secondpath"));
			inputvo.setThirdpath(element.elementText("thirdpath"));
			inputvo.setName(element.elementText("name"));
			inputvo.setExternalid(element.elementText("externalid"));
			inputvo.setValue(element.elementText("value"));
			inputvo.setPreconditions(element.elementText("preconditions"));
			inputvo.setActions(element.elementText("actions"));
			inputvo.setExpectedresults(element.elementText("expectedresults"));
			inputvo.setExecutiontype(element.elementText("executiontype"));
			iv.add(inputvo);
		}
		return iv;
	}
	
	public void insetSourceValueIntoTarget(InputVo inputvo,Element el,int testcaseid)
	{
		//�����ļ����������xml�ṹ
		Element testcase = el.addElement("testcase");
		//testcase xml�ṹ
		Element node_order = testcase.addElement("node_order");
		Element externalid = testcase.addElement("externalid");
		Element version = testcase.addElement("version");
		Element summary = testcase.addElement("summary");
		Element preconditions = testcase.addElement("preconditions");
		Element execution_type = testcase.addElement("execution_type");
		Element importance = testcase.addElement("importance");
		Element steps = testcase.addElement("steps");
//		Element custom_fields = testcase.addElement("custom_fields");
		//step xml�ṹ
		Element step = steps.addElement("step");
		Element step_number = step.addElement("step_number");
		Element actions = step.addElement("actions");
		Element expectedresults = step.addElement("expectedresults");
		Element execution_type2 = step.addElement("execution_type");
		//custom_field xml�ṹ
//		Element custom_field = custom_fields.addElement("custom_field");
//		Element name = custom_field.addElement("name");
//		Element value = custom_field.addElement("value");
		
		//����һ��ȫ��Ψһ��id��������Ҫ�õ�Id���ֶ�
		Util ul = new Util();
		String commonId = ul.createId(testcaseid);
		
		//��inputvo�е�ֵȡ��������Ҫ���xml�ڵ��ֵ
		testcase.addAttribute("id", commonId);
		testcase.addAttribute("name", inputvo.getName());
		node_order.setText("");
		externalid.setText(inputvo.getExternalid());
		version.setText(ul.addCDATA(commonId));
		summary.setText(ul.addCDATA(inputvo.getName()));
//		importance.setText(ul.addCDATA(inputvo.getImportance()));
		preconditions.setText(ul.addHtmlElement(inputvo.getPreconditions()));
//		System.out.print(inputvo.getExecutiontype());
		if (inputvo.getExecutiontype().equals("�ֹ�"))
        {
            execution_type.setText(ul.addCDATA("1"));
        }
        if (inputvo.getExecutiontype().equals("�Զ���"))
        {
            execution_type.setText(ul.addCDATA("2"));
        }
        else {
            execution_type.setText(ul.addCDATA("1"));
        }
//		execution_type.setText(ul.addCDATA("1"));
//		importance.setText(ul.addCDATA("2"));
        if (inputvo.getImportance().equals("��"))
        {
            importance.setText(ul.addCDATA("3"));
        }
        if (inputvo.getImportance().equals("��"))
        {
            importance.setText(ul.addCDATA("2"));
        }
        if (inputvo.getImportance().equals("��"))
        {
            importance.setText(ul.addCDATA("1"));
        }
        else {
            importance.setText(ul.addCDATA("3"));
        }
		//step�ṹ�帳ֵ
		step_number.setText("1");
		actions.setText(ul.addHtmlElement(inputvo.getActions()));
		expectedresults.setText(ul.addHtmlElement(inputvo.getExpectedresults()));

        if (inputvo.getExecutiontype().equals("�ֹ�"))
        {
            execution_type2.setText(ul.addCDATA("1"));
        }
        if (inputvo.getExecutiontype().equals("�Զ���"))
        {
            execution_type2.setText(ul.addCDATA("2"));
        }
		//execution_type2.setText(ul.addCDATA("1"));
		//custom_field�ṹ�帳ֵ
//		name.setText("testcase_level");
//		value.setText(inputvo.getValue());
	}
	
	public ArrayList<InputVo> analysisExcel(String filepath) throws  IOException
	{
		ArrayList<InputVo> iv = new ArrayList<InputVo>();
//		POIUtil poiUtil = new POIUtil();
		Workbook workbook = POIUtil.getWorkBook(filepath);
//		Workbook workbook = new XSSFWorkbook();
//		InputStream is = new FileInputStream(new File((filepath)));
//        XSSFWorkbook workbook = new XSSFWorkbook(is);
//        Workbook workbook = new Workbook(is);
//        Workbook workbook = POIUtil.getWorkBook(filepath)
//		XSSFWorkbook workbook = XSSFWorkbook.getWorkbook(new File(filepath));
		Sheet sheet = workbook.getSheetAt(0);
		for (int role = 6;role<=sheet.getLastRowNum();role++)
		{
//		    XSSFRow xssfRow = sheet.getRow(role);
            Row row = sheet.getRow(role);
            Cell firstpath = row.getCell(1);
//            Cell firstpath = sheet.get
			// ������С����excel����ģ�壬��2���ǡ���Ŀ������Ϊһ��·��
//			XSSFCell firstpath = sheet.getCell(1, role);
//            XSSFCell firstpath = xssfRow.getCell(1);
			if (POIUtil.getCellValue(firstpath)!=null&&!POIUtil.getCellValue(firstpath).equals(""))
			{
			    // ��3���ǡ�ģ�顱����Ϊ2��·��
				Cell secondpath = row.getCell(2);
//                XSSFCell secondpath = xssfRow.getCell(2);
				// ��4���ǡ���ģ�顱����Ϊ3��·��
				Cell thirdpath = row.getCell(3);
//				Cell thirdpath = sheet.getCell(2, role);
                //��5������������
				Cell name = row.getCell(4);
//                XSSFCell name = xssfRow.getCell(3);
				//��1�����������
				Cell externalid = row.getCell(0);
//                XSSFCell externalid = xssfRow.getCell(0);
//				Cell value = sheet.getCell(5, role);
                //��9�����������ȼ�
				Cell importance = row.getCell(8);
//                XSSFCell importance = xssfRow.getCell(7);
				//��С������ģ���У�û��Ԥ����������ӵ���6��
				Cell preconditions = row.getCell(5);
//                XSSFCell preconditions = xssfRow.getCell(4);
                // ��7���ǲ�������
				Cell actions = row.getCell(6);
//                XSSFCell actions = xssfRow.getCell(5);
				// ��8�����������
				Cell expectedresults = row.getCell(7);
//                XSSFCell expectedresults = xssfRow.getCell(6);
				// ��10����ִ�����ͣ����ֹ������Զ���
				Cell executiontype = row.getCell(9);
//                XSSFCell executiontype = xssfRow.getCell(11);
				InputVo inputvo = new InputVo();
//				inputvo.setFirstpath(firstpath.getContents());
				inputvo.setFirstpath(POIUtil.getCellValue(firstpath));
//				inputvo.setFirstpath(firstpath.getStringCellValue());

				inputvo.setSecondpath(POIUtil.getCellValue(secondpath));
				inputvo.setThirdpath(POIUtil.getCellValue(thirdpath));
//				inputvo.setSecondpath(secondpath.getStringCellValue());
//				inputvo.setThirdpath(thirdpath.getContents());
//				inputvo.setName(name.getStringCellValue());
				inputvo.setName(POIUtil.encodeString(name.getStringCellValue()));
//				inputvo.setName(name.getStringCellValue());
				inputvo.setExternalid(externalid.getStringCellValue());
				inputvo.setImportance(importance.getStringCellValue());
//				inputvo.setValue(value.getContents());
				inputvo.setPreconditions(preconditions.getStringCellValue());
				inputvo.setActions(POIUtil.encodeString(actions.getStringCellValue()));
//				inputvo.setActions(actions.getStringCellValue());
				inputvo.setExpectedresults(expectedresults.getStringCellValue());
				inputvo.setExecutiontype(executiontype.getStringCellValue());
				iv.add(inputvo);
			}
		}
		return iv;
	}
}
