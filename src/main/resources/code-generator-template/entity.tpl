package ${projectBasePackage}.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Data
public class ${entityName} {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

<#list entityFields as field>
    private ${field.type} ${field.name};
</#list>

}
