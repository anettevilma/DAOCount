package test;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TestMethodLineNumberNew {
	
	static CountDTO ctx = new CountDTO();
	

	public static void main(String[] args) throws  IOException
			 {
		String projectPath = "C:\\Users\\MXP8137\\OneDrive - The Home Depot\\Desktop\\ParmMgmtPurgeBatchRedux-Migration_Tomcat7";
		final CountDTO ct = new CountDTO();
		Files.walk(Paths.get(projectPath)).forEach(filePath -> {
			if (Files.isRegularFile(filePath)) {
				String srcRoot = filePath.toString();
				File a = new File(srcRoot);

				String fileName = a.getName();
				if (fileName.endsWith(".java")) {

					ct.setCount(ct.getCount() + 1);
					File src = new File(srcRoot);
					try {
				       getMethodLineNumbers(src);

					} catch (Exception e) {
						System.out.println("Exception in class: "+fileName+" ::: "+e.getMessage());
					}
				}
			}
		});

		System.out.println("Number of Classes ==== " + ct.getCount());

		System.out.println("DAO One Count === "+ctx.getDaoOneCnt());
		System.out.println("DAO Two Count === "+ctx.getDaoTwoCnt());
		System.out.println("JDBC Count === "+ctx.getJdbcCnt());

		
	}

	private static void getMethodLineNumbers(File src) throws ParseException,
			IOException {
		CompilationUnit cu = JavaParser.parse(src);
		new MethodVisitor().visit(cu, (Object)src.getName());
	}

	/**
	 * Simple visitor implementation for visiting MethodDeclaration nodes.
	 */
	private static class MethodVisitor extends VoidVisitorAdapter {
		@Override
		public void visit(MethodDeclaration m, Object arg) {

			String line = "";
			try {
					BlockStmt body = m.getBody();
					
					List<Statement> stmts = body.getStmts();
					
					if(null != stmts) {
						
						for(Statement st: stmts) {
							line = st.toString();
							
							
							if(null != line && !line.isEmpty()) {
								if(line.contains("BasicDAO.getResult") || line.contains("BasicDAO.getObject")|| 
										line.contains("BasicDAO.insertObject") || line.contains("BasicDAO.updateObject") ||
										line.contains("BasicDAO.deleteObject") || line.contains("new UniversalConnectionHandler") || 
										line.contains("addDAOConnection(")) {
									ctx.setDaoOneCnt(ctx.getDaoOneCnt()+1);
								}
								
								if(line.contains("DAO.useJNDI(") || line.contains("DAO.upsert(")|| 
										line.contains("DAO.select(") || line.contains("DAO.update(") || 
										line.contains("DAO.insert(")) {
									ctx.setDaoTwoCnt(ctx.getDaoTwoCnt()+1);
								}
								
								
								if(line.contains(".createStatement(") || line.contains(".prepareStatement(")){
									ctx.setJdbcCnt(ctx.getJdbcCnt()+1);
								}
								
								
							}
							
							
							
						}
					}
			}catch (Exception e) {
				System.out.println("Error::: "+arg.toString()+": "+m.getName()+" = "+e.getMessage());
			}
		}
	}

	/*public static void countLines(String filename, CountDTO ct) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			String line;
			int lineNo=0;
			
			String prev = "";
			
			while ((line = br.readLine()) != null) {
				
				
				//public void handleQuery(
				//new UniversalConnectionHandler
				if(line.contains("BasicDAO.getResult") || line.contains("BasicDAO.getObject")|| 
						line.contains("BasicDAO.insertObject") || line.contains("BasicDAO.updateObject") ||
						line.contains("BasicDAO.deleteObject") || line.contains("new UniversalConnectionHandler")) {
					ct.setDaoOneCnt(ct.getDaoOneCnt()+1);
				} else if(null != prev && prev.trim() != "" && 
						(prev.trim().equalsIgnoreCase("BasicDAO.") || prev.trim().equalsIgnoreCase("BasicDAO")) && 
						(line.startsWith(".getResult") || line.startsWith(".getObject")|| 
								line.startsWith(".insertObject") || line.startsWith(".updateObject") ||
								line.startsWith(".deleteObject")|| line.startsWith("getResult") || line.contains("getObject")|| 
								line.startsWith("insertObject") || line.startsWith("updateObject") ||
								line.startsWith("deleteObject"))) {
					ct.setDaoOneCnt(ct.getDaoOneCnt()+1);
				}
				
				if(line.contains("DAO.useJNDI(") || line.contains("DAO.upsert(")|| 
						line.contains("DAO.select(") || line.contains("DAO.update(") || 
						line.contains("DAO.insert(")) {
					ct.setDaoTwoCnt(ct.getDaoTwoCnt()+1);
				} else if(null != prev && prev.trim() != "" && 
						(prev.trim().equalsIgnoreCase("DAO.") || prev.trim().equalsIgnoreCase("DAO")) && 
						(line.startsWith(".useJNDI") || line.startsWith(".upsert")|| 
								line.startsWith(".select") || line.startsWith(".update") ||
								line.startsWith(".insert")|| line.startsWith("useJNDI") || line.contains("upsert")|| 
								line.startsWith("select") || line.startsWith("update") ||
								line.startsWith("insert"))){
					ct.setDaoTwoCnt(ct.getDaoTwoCnt()+1);
				}
				
				
				if(line.contains(".createStatement(") || line.contains(".prepareStatement(")){
					ct.setJdbcCnt(ct.getJdbcCnt()+1);
				}
				
				prev = line;
			}
			
		} finally {
			br.close();
		}
	}*/
}
