package vko.framework.base.scanner;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

/**
 * 扫描获取类 和 方法
 * @author caoqingguang
 *
 */
public class ScanUtil {
	
	protected  static Logger logger = LoggerFactory.getLogger(ScanUtil.class);
	
	public static Set<Class<?>> scannerClass(String pks,Class<? extends Annotation> flag){
		Set<Class<?>> result=new HashSet<Class<?>>();
		try {
			TypeFilter filter=new AnnotationTypeFilter(flag);
			String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
							ClassUtils.convertClassNameToResourcePath(pks) + "/**/*.class";
			ResourcePatternResolver rpr = new PathMatchingResourcePatternResolver();
			Resource[] resources = rpr.getResources(pattern);
			MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(rpr);
			for (Resource resource : resources) {
				if (!resource.isReadable()) {
					continue;
				}
				
				MetadataReader reader = readerFactory.getMetadataReader(resource);
				String className = reader.getClassMetadata().getClassName();
				if(filter.match(reader, readerFactory)){
					result.add(Class.forName(className));
				}
			}
		} catch (Exception e) {
			logger.error("扫描类时出现错误,path:{},anno:{}",pks,flag,e);
		}
		return result;
	}
	
	public static Set<Method> scannerMethod(Class<?> clazz,Class<? extends Annotation> flag){
		Set<Method> set=new HashSet<Method>();
		Method[] ms=clazz.getMethods();
		for(Method m:ms){
			if(m.isAnnotationPresent(flag)){
				set.add(m);
			}
		}
		return set;
	}
}