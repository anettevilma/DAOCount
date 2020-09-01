/**
 * 
 */
package methodtest;

/**
 * @author JXV8LW8
 *
 */
public class CountDTO {
	
	int count;
	
	int daoOneCnt;
	
	int daoTwoCnt;
	
	int jdbcCnt;
	
	int jspCount;
	int actioncount;
	int methodcount;
	
		
	
	public int getMethodcount() {
		return methodcount;
	}

	public void setMethodcount(int methodcount) {
		this.methodcount = methodcount;
	}

	public int getActioncount() {
		return actioncount;
	}

	public void setActioncount(int actioncount) {
		this.actioncount = actioncount;
	}

	public void setJdbcCnt(int jdbcCnt) {
		this.jdbcCnt = jdbcCnt;
	}
	
	public int getJdbcCnt() {
		return jdbcCnt;
	}
	
	public void setDaoOneCnt(int daoOneCnt) {
		this.daoOneCnt = daoOneCnt;
	}
	
	public int getDaoOneCnt() {
		return daoOneCnt;
	}
	
	public void setDaoTwoCnt(int daoTwoCnt) {
		this.daoTwoCnt = daoTwoCnt;
	}
	
	public int getDaoTwoCnt() {
		return daoTwoCnt;
	}
	
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public int getCount() {
		return count;
	}

	public int getJspCount() {
		return jspCount;
	}

	public void setJspCount(int jspCount) {
		this.jspCount = jspCount;
	}
	
	

}
