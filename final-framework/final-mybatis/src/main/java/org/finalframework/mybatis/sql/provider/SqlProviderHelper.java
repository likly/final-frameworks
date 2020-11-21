package org.finalframework.mybatis.sql.provider;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.OgnlCache;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.finalframework.annotation.IEntity;
import org.finalframework.annotation.IQuery;
import org.finalframework.data.query.Update;
import org.finalframework.data.query.sql.AnnotationQueryProvider;
import org.finalframework.mybatis.mapper.AbsMapper;
import org.finalframework.mybatis.sql.SqlBound;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author likly
 * @version 1.0
 * @date 2020/11/18 09:48:59
 * @since 1.0
 */
@Slf4j
public final class SqlProviderHelper {

    public static final String PARAMETER_NAME_TABLE = "table";
    public static final String PARAMETER_NAME_QUERY = "query";
    public static final String PARAMETER_NAME_IGNORE = "ignore";
    public static final String PARAMETER_NAME_SELECTIVE = "selective";


    private static final String INSERT_METHOD_NAME = "insert";
    private static final String REPLACE_METHOD_NAME = "replace";
    private static final String SAVE_METHOD_NAME = "save";

    private static final String UPDATE_METHOD_NAME = "update";

    private static final String DELETE_METHOD_NAME = "delete";

    private static final String SELECT_METHOD_NAME = "select";
    private static final String SELECT_ONE_METHOD_NAME = "selectOne";
    private static final String SELECT_IDS_METHOD_NAME = "selectIds";
    private static final String SELECT_COUNT_METHOD_NAME = "selectCount";

    private static final String TRUNCATE_METHOD_NAME = "truncate";

    private static final Map<String, Class<? extends Annotation>> METHOD_ANNOTATIONS = new HashMap<>(8);
    private static final Map<String, Class<?>[]> METHOD_ARGS = new HashMap<>();

    private static final Map<String, SqlProvider> SQL_PROVIDERS = new HashMap<>();

    private static final Constructor<ProviderContext> PROVIDER_CONTEXT_CONSTRUCTOR;

    static {

        METHOD_ANNOTATIONS.put(INSERT_METHOD_NAME, InsertProvider.class);
        METHOD_ANNOTATIONS.put(REPLACE_METHOD_NAME, InsertProvider.class);
        METHOD_ANNOTATIONS.put(SAVE_METHOD_NAME, InsertProvider.class);

        METHOD_ANNOTATIONS.put(UPDATE_METHOD_NAME, InsertProvider.class);

        METHOD_ANNOTATIONS.put(DELETE_METHOD_NAME, InsertProvider.class);

        METHOD_ANNOTATIONS.put(SELECT_METHOD_NAME, SelectProvider.class);
        METHOD_ANNOTATIONS.put(SELECT_ONE_METHOD_NAME, SelectProvider.class);
        METHOD_ANNOTATIONS.put(SELECT_IDS_METHOD_NAME, SelectProvider.class);
        METHOD_ANNOTATIONS.put(SELECT_COUNT_METHOD_NAME, SelectProvider.class);

        METHOD_ANNOTATIONS.put(TRUNCATE_METHOD_NAME, UpdateProvider.class);


        /**
         * @see org.finalframework.mybatis.mapper.AbsMapper#insert(String, Class, boolean, Collection)
         * @see org.finalframework.mybatis.mapper.AbsMapper#replace(String, Class, Collection)
         * @see org.finalframework.mybatis.mapper.AbsMapper#save(String, Class, Collection)
         */
        METHOD_ARGS.put(INSERT_METHOD_NAME, new Class[]{String.class, Class.class, boolean.class, Collection.class});
        METHOD_ARGS.put(REPLACE_METHOD_NAME, new Class[]{String.class, Class.class, Collection.class});
        METHOD_ARGS.put(SAVE_METHOD_NAME, new Class[]{String.class, Class.class, Collection.class});
        /**
         * @see AbsMapper#update(String, Class, IEntity, Update, boolean, Collection, IQuery)
         */
        METHOD_ARGS.put(UPDATE_METHOD_NAME, new Class[]{String.class, Class.class, IEntity.class, Update.class, boolean.class, Collection.class, IQuery.class});
        /**
         * @see AbsMapper#delete(String, Collection, IQuery)
         */
        METHOD_ARGS.put(DELETE_METHOD_NAME, new Class[]{String.class, Collection.class, IQuery.class});

        /**
         * @see AbsMapper#select(String, Class, Collection, IQuery)
         * @see AbsMapper#selectOne(String, Class, Serializable, IQuery)
         * @see AbsMapper#selectIds(String, IQuery)
         * @see AbsMapper#selectCount(String, Collection, IQuery)
         */
        METHOD_ARGS.put(SELECT_METHOD_NAME, new Class[]{String.class, Class.class, Collection.class, IQuery.class});
        METHOD_ARGS.put(SELECT_ONE_METHOD_NAME, new Class[]{String.class, Class.class, Serializable.class, IQuery.class});
        METHOD_ARGS.put(SELECT_IDS_METHOD_NAME, new Class[]{String.class, IQuery.class});
        METHOD_ARGS.put(SELECT_COUNT_METHOD_NAME, new Class[]{String.class, Collection.class, IQuery.class});

        /**
         * @see AbsMapper#truncate(String)
         */
        METHOD_ARGS.put(TRUNCATE_METHOD_NAME, new Class[]{String.class});

        SQL_PROVIDERS.put(INSERT_METHOD_NAME, new InsertSqlProvider());
        SQL_PROVIDERS.put(REPLACE_METHOD_NAME, new InsertSqlProvider());
        SQL_PROVIDERS.put(SAVE_METHOD_NAME, new InsertSqlProvider());

        SQL_PROVIDERS.put(UPDATE_METHOD_NAME, new UpdateSqlProvider());
        SQL_PROVIDERS.put(DELETE_METHOD_NAME, new DeleteSqlProvider());

        SQL_PROVIDERS.put(SELECT_METHOD_NAME, new SelectSqlProvider());
        SQL_PROVIDERS.put(SELECT_ONE_METHOD_NAME, new SelectSqlProvider());
        SQL_PROVIDERS.put(SELECT_IDS_METHOD_NAME, new SelectIdsSqlProvider());
        SQL_PROVIDERS.put(SELECT_COUNT_METHOD_NAME, new SelectCountSqlProvider());

        SQL_PROVIDERS.put(TRUNCATE_METHOD_NAME, new TruncateSqlProvider());

        // init ProviderContext
        try {
            PROVIDER_CONTEXT_CONSTRUCTOR = ReflectionUtils.accessibleConstructor(ProviderContext.class, Class.class, Method.class, String.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private SqlProviderHelper() {
    }

    public static String xml(Class<? extends AbsMapper> mapper, String method, Map<String, Object> parameters) {
        try {
            ProviderContext providerContext = PROVIDER_CONTEXT_CONSTRUCTOR.newInstance(mapper, ReflectionUtils.findMethod(mapper, method, METHOD_ARGS.get(method)), null);
            return SQL_PROVIDERS.get(method).provide(providerContext, parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String sql(Class<? extends AbsMapper> mapper, String method, Map<String, Object> parameters) {
        Method sqlMethod = ReflectionUtils.findMethod(mapper, method, METHOD_ARGS.get(method));
        final ProviderSqlSource providerSqlSource = new ProviderSqlSource(new Configuration(), sqlMethod.getAnnotation(METHOD_ANNOTATIONS.get(method)), mapper, sqlMethod);
        final BoundSql boundSql = providerSqlSource.getBoundSql(parameters);
        return boundSql.getSql();
    }

    public static SqlBound query(Class<? extends IEntity<?>> entity, IQuery query) {
        SqlBound sqlBound = new SqlBound();
        sqlBound.setEntity(entity);
        sqlBound.setQuery(query);

        String script = String.join("", "<script>", AnnotationQueryProvider.INSTANCE.provide("query", entity, query.getClass()), "</script>");
        sqlBound.setScript(script);

        SqlSource sqlSource = new XMLLanguageDriver().createSqlSource(new Configuration(), script, Map.class);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(PARAMETER_NAME_QUERY, query);
        final BoundSql boundSql = sqlSource.getBoundSql(parameters);
        sqlBound.setSql(boundSql.getSql());

        sqlBound.setParameterMappings(
                boundSql.getParameterMappings().stream()
                        .map(item -> {
                            final SqlBound.ParameterMapping mapping = new SqlBound.ParameterMapping();
                            mapping.setProperty(item.getProperty());
                            if (!Object.class.equals(item.getJavaType())) {
                                mapping.setJavaType(item.getJavaType());
                            }
                            if (Objects.nonNull(item.getTypeHandler())) {
                                mapping.setTypeHandler((Class<? extends TypeHandler<?>>) item.getTypeHandler().getClass());
                            }
                            mapping.setExpression(item.getExpression());
                            mapping.setValue(OgnlCache.getValue(item.getProperty(), boundSql.getParameterObject()));
                            return mapping;
                        })
                        .collect(Collectors.toList())
        );

        return sqlBound;
    }

}