package test;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class TestMethodLineNumber {
	public static int i = 1;
	public static WritableWorkbook wworkbook;
	public static WritableSheet wsheet;

	public static void main(String[] args) throws ParseException, IOException,
			RowsExceededException, WriteException {
		String projectPath = "C:\\opt\\cd";
		/*wworkbook = Workbook
				.createWorkbook(new File(
						"C:\\opt\\Grid to PCF - Supplychain\\lines of code\\CRWeb-CRWeb-TC7Migration_dao1.xls"));
		wsheet = wworkbook.createSheet("First Sheet", 0);
		Label label = new Label(0, 0, "Jarfiles");
		wsheet.addCell(label);*/
		final CountDTO ct = new CountDTO();
		Files.walk(Paths.get(projectPath)).forEach(filePath -> {
			if (Files.isRegularFile(filePath)) {
				String srcRoot = filePath.toString();
				File a = new File(srcRoot);

				String fileName = a.getName();
				// System.out.println(fileName);
				//String[] check = fileName.split(".");

				if (fileName.endsWith(".java")) {

					ct.setCount(ct.getCount() + 1);

					//long noOfLines = 0;
					try {
						 countLines(srcRoot, ct);

					} catch (Exception e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					File src = new File(srcRoot);
					String[] parts = srcRoot.split("src");
					// System.out.println(parts[1]);
					try {
						/*
						 * Label label1 = new Label(0, i, parts[1]);
						 * wsheet.addCell(label1); Label labels = new Label(1,
						 * i, "Total No of Lines"); wsheet.addCell(labels);
						 * Number labelp = new Number(2, i, noOfLines);
						 * wsheet.addCell(labelp); i++; Label label2 = new
						 * Label(0, i, "Method Name"); wsheet.addCell(label2);
						 * Label label3 = new Label(1, i, "Lines of code");
						 * wsheet.addCell(label3); Label label4 = new Label(2,
						 * i, "Start Line"); wsheet.addCell(label4); Label
						 * label5 = new Label(3, i, "End Line");
						 * wsheet.addCell(label5); i++;
						 */
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// System.out.println(src);
					try {
						//getMethodLineNumbers(src);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		System.out.println("Number of Classes ==== " + ct.getCount());
		System.out.println("Number of DAO1    ==== " + ct.getDaoOneCnt());
		System.out.println("Number of DAO2    ==== " + ct.getDaoTwoCnt());
		System.out.println("Number of JDBC    ==== " + ct.getJdbcCnt());

		/*wworkbook.write();
		wworkbook.close();*/
	}

	private static void getMethodLineNumbers(File src) throws ParseException,
			IOException {
		CompilationUnit cu = JavaParser.parse(src);
		new MethodVisitor().visit(cu, null);
	}

	/**
	 * Simple visitor implementation for visiting MethodDeclaration nodes.
	 */
	private static class MethodVisitor extends VoidVisitorAdapter {
		@Override
		public void visit(MethodDeclaration m, Object arg) {

			System.out.println(m.getName());
		}
	}

	public static void countLines(String filename, CountDTO ct) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			String line;
			int lineNo=0;
			while ((line = br.readLine()) != null) {
				
				
				if(lineNo > 242) {
				//public void handleQuery(
				//new UniversalConnectionHandler
				if(line.contains("BasicDAO.getResult") || line.contains("BasicDAO.getObject")|| 
						line.contains("BasicDAO.insertObject") || line.contains("BasicDAO.updateObject") ||
						line.contains("BasicDAO.deleteObject") || line.contains("new UniversalConnectionHandler")) {
					ct.setDaoOneCnt(ct.getDaoOneCnt()+1);
				}
				
				if(line.contains("DAO.useJNDI(") || line.contains("DAO.upsert(")|| 
						line.contains("DAO.select(") || line.contains("DAO.update(") || 
						line.contains("DAO.insert(")) {
					ct.setDaoTwoCnt(ct.getDaoTwoCnt()+1);
				}
				
				
				if(line.contains(".createStatement(") || line.contains(".prepareStatement(")){
					ct.setJdbcCnt(ct.getJdbcCnt()+1);
				}
				}
				lineNo++;
			}
			
		} finally {
			br.close();
		}
	}
}
