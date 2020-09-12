

package org.finalframework.mybatis.sql.provider;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.finalframework.mybatis.handler.PointTypeHandler;
import org.springframework.data.geo.Point;

/**
 * @author likly
 * @version 1.0
 * @date 2020-07-17 14:06:19
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Transient
public class Person extends AbsEntity {
    //    @Virtual
    public String vcolumn;
    private String name;
    private Integer age;
    @TypeHandler(PointTypeHandler.class)
    @Geometry
    private Point point;
    @Function(reader = "MAX(age) as ${column}")
    private Integer maxAge;
}
