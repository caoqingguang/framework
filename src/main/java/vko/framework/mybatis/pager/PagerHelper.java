package vko.framework.mybatis.pager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mybatis - 通用分页拦截器
 * @author 曹庆光
 * date 2015-12-14
 */
@Intercepts({
	@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class}),
	@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})})
public class PagerHelper implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(PagerHelper.class);

    public static final ThreadLocal<PagerModel<?>> localmodel = new ThreadLocal<PagerModel<?>>();

    /**
     * 开始分页
     * @param pageNum
     * @param pageSize
     */
    public static void setPagerModel(PagerModel<?> model) {
        localmodel.set(model);
    }

    /**
     * 结束分页并返回结果，该方法必须被调用，否则localPage会一直保存下去，直到下一次startPage
     * @return
     */
    public static PagerModel<?> clearPagerModel() {
    	PagerModel<?> model = localmodel.get();
        localmodel.remove();
        return model;
    }
    
    public static void setPagerRequest(PagerRequest request){
    	PagerModel<?> model=new PagerModel<Object>(request);
    	localmodel.set(model);
    }
    
    @SuppressWarnings("unchecked")
	public static <E> PagerResponse<E> getPagerResponse(Class<E> clazz){
    	PagerResponse<E> response=(PagerResponse<E>)localmodel.get().getResponse();
    	clearPagerModel();
    	return response;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (localmodel.get() == null) {
            return invocation.proceed();
        }
        try{
	        if (invocation.getTarget() instanceof StatementHandler) {
	            StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
	            MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
	            // 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环
	            // 可以分离出最原始的的目标类)
	            while (metaStatementHandler.hasGetter("h")) {
	                Object object = metaStatementHandler.getValue("h");
	                metaStatementHandler = SystemMetaObject.forObject(object);
	            }
	            // 分离最后一个代理对象的目标类
	            while (metaStatementHandler.hasGetter("target")) {
	                Object object = metaStatementHandler.getValue("target");
	                metaStatementHandler = SystemMetaObject.forObject(object);
	            }
	            MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
	            //分页信息if (localPage.get() != null) {
	            PagerModel<?> model = localmodel.get();
	            BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
	            // 分页参数作为参数对象parameterObject的一个属性
	            String sql = boundSql.getSql();
	            // 重写sql
	            String pageSql = buildPageSql(sql, model);
	            //重写分页sql
	            metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);
	            Connection connection = (Connection) invocation.getArgs()[0];
	            // 重设分页参数里的总页数等
	            setPageParameter(sql, connection, mappedStatement, boundSql, model);
	            // 将执行权交给下一个拦截器
	            return invocation.proceed();
	        } else if (invocation.getTarget() instanceof ResultSetHandler) {
	            Object result = invocation.proceed();
	            PagerModel<?> page = localmodel.get();
	            page.getResponse().setRows((List) result);
	            return result;
	        }
        }catch(Throwable e){
        	localmodel.get().getResponse().setSqlMsg(e.toString());
        	clearPagerModel();
        	throw e;
        }
        return null;
    }

    /**
     * 只拦截这两种类型的
     * <br>StatementHandler
     * <br>ResultSetHandler
     * @param target
     * @return
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler || target instanceof ResultSetHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 修改原SQL为分页SQL
     * @param sql
     * @param page
     * @return
     */
    private String buildPageSql(String sql, PagerModel<?> model) {
        StringBuilder pageSql = new StringBuilder(200);
        /*pageSql.append("select * from ( select temp.*, rownum row_id from ( ");
        pageSql.append(sql);
        pageSql.append(" ) temp where rownum <= ").append(page.getEndRow());
        pageSql.append(") where row_id > ").append(page.getStartRow());*/
        pageSql.append(sql);
        if(model.getOrderStr()!=null&& !"".equals(model.getOrderStr())){
        	pageSql.append(" order by ").append(model.getOrderStr());
        }
        pageSql.append(" limit ").append(model.getRowFrom()).append(",").append(model.getRowNum());
        return pageSql.toString();
    }

    /**
     * 获取总记录数
     * @param sql
     * @param connection
     * @param mappedStatement
     * @param boundSql
     * @param model
     */
    private void setPageParameter(String sql, Connection connection, MappedStatement mappedStatement,
                                  BoundSql boundSql, PagerModel<?> model) {
        // 记录总记录数
    	String sqltmp=sql.toUpperCase();
    	int i1=sqltmp.indexOf("SELECT")+6;
        int i2=sqltmp.indexOf("FROM");
        String countSql=sql.substring(0, i1)+" count(0) " + sql.substring(i2);
        PreparedStatement countStmt = null;
        ResultSet rs = null;
        try {
            countStmt = connection.prepareStatement(countSql);
            BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql,
                    boundSql.getParameterMappings(), boundSql.getParameterObject());
            setParameters(countStmt, mappedStatement, countBS, boundSql.getParameterObject());
            rs = countStmt.executeQuery();
            int totalCount = 0;
            if (rs.next()) {
                totalCount = rs.getInt(1);
            }
            model.getResponse().setTotal(totalCount);
        } catch (SQLException e) {
            logger.error("Ignore this exception", e);
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("Ignore this exception", e);
            }
            try {
                countStmt.close();
            } catch (SQLException e) {
                logger.error("Ignore this exception", e);
            }
        }
    }

    /**
     * 代入参数值
     * @param ps
     * @param mappedStatement
     * @param boundSql
     * @param parameterObject
     * @throws SQLException
     */
    private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql,
                               Object parameterObject) throws SQLException {
        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
        parameterHandler.setParameters(ps);
    }
    
    public static void main(String[] args) {
    	String sql="select * from user";
    	String sqltmp=sql.toUpperCase();
    	int i1=sqltmp.indexOf("SELECT")+6;
        int i2=sqltmp.indexOf("FROM");
        String countSql=sql.substring(0, i1)+" count(0) " + sql.substring(i2);
        System.out.println(countSql);
	}

}
