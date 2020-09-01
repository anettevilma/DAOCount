package methodtest;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;


public class Test {
	
	static CountDTO ctx = new CountDTO();
	

	public static void main(String[] args) throws  IOException
			 {
		String projectPath = "C:\\Users\\AXV8IH3\\workspace\\OdmCompWeb";
		final CountDTO ct = new CountDTO();
	
		Files.walk(Paths.get(projectPath)).forEach(filePath -> {
			if (Files.isRegularFile(filePath)) {
				String srcRoot = filePath.toString();
				File a = new File(srcRoot);
				String fileName = a.getName();
				String actionClass;
				if(srcRoot.contains("WebContent")){
					if(fileName.endsWith(".jsp")){
						ct.setJspCount(ct.getJspCount() + 1);
					}
				}else if((!srcRoot.contains("TO"))&& (!srcRoot.contains("build"))){
									if (fileName.endsWith(".java")) {
					ct.setCount(ct.getCount() + 1);
					File src = new File(srcRoot);
					try {
				       getMethodLineNumbers(src);

					} catch (Exception e) {
						System.out.println("Exception in class: "+fileName+" ::: "+e.getMessage());
					}
				}else if(fileName.endsWith(".jsp")){
					ct.setJspCount(ct.getJspCount() + 1);
				}// Should be added as a separate pkg in the app to be analysed to get action method
				 if((fileName.contains("Action"))){
					ct.setActioncount(ct.getActioncount()+1);
					System.out.println(srcRoot);
					//int iend = fileName.indexOf(".");
					int pend = srcRoot.indexOf(".");
					String st="\\";
					int in= ordinalIndexOf(srcRoot,st,6);
					String pk= srcRoot.substring(in+1, pend);
					String pkg=pk.replace("\\", ".");
					System.out.println("pkg:"+pkg);
					if (pend!=-1)
					{
						
						//actionClass= fileName.substring(0 , iend);
					   //System.out.println("name only :"+actionClass);
					  // String str= subString+".class";
					    //System.out.println("CLass"+str);
					    try {
							Class classobj = Class.forName(pkg);
						
					    Method[] methods = classobj.getDeclaredMethods();
					    for (Method method : methods) { 
					    	  
				            String MethodName = method.getName(); 
				              
				            System.out.println("Name of the method: "
				                               + MethodName); 
				            ct.setMethodcount(ct.getMethodcount()+1);
				            
					    } 
					    System.out.println("No. Of methods:"+ct.getMethodcount());
					
					  
						}catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					     
						
			            } 
					}
				}
			}
			
		});
		


		System.out.println("Number of Classes ==== " + ct.getCount());

		System.out.println("DAO One Count === "+ctx.getDaoOneCnt());
		System.out.println("DAO Two Count === "+ctx.getDaoTwoCnt());
		System.out.println("JDBC Count === "+ctx.getJdbcCnt());
		System.out.println("JSP Count === "+ct.getJspCount());
		System.out.println("Action Count === "+ct.getActioncount());
		
		
		
	}

	private static int ordinalIndexOf(String str, String substr, int n) {
		int pos = str.indexOf(substr);
	    while (--n > 0 && pos != -1)
	        pos = str.indexOf(substr, pos + 1);
	    return pos;
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
