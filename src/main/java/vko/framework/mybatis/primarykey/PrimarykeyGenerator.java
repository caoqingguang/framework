package vko.framework.mybatis.primarykey;
/**
 * 主键生成 接口
 * @author caoqingguang
 *
 */
public interface PrimarykeyGenerator {
	Long  generateId(String dbName, String tableName);
}