package ${projectBasePackage}.controller.api.admin;

import ${projectBasePackage}.entity.${entityName};
import com.lephix.easy.security.Roles;
import ${projectBasePackage}.service.${entityName}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.lephix.easy.utils.JPASpecUtils.OP.EQ;
import static com.lephix.easy.utils.JPASpecUtils.of;

@RestController
@RequestMapping("/api/admin/${entityName?uncap_first}")
@Secured(Roles.ADMIN)
public class ${entityName}Controller {

    @Autowired
    private ${entityName}Service ${entityName?uncap_first}Service;

    @PostMapping
    @PutMapping
    public boolean create(${entityName} entity) {
        ${entityName?uncap_first}Service.save(entity);
        return true;
    }

    @GetMapping()
    public Page<${entityName}> list(${entityName} entity, Pageable pageable) {
        List<Specification<${entityName}>> specs = new ArrayList<>();
//        specs.add(of(entity, "name", EQ, entity.getName()));

        return ${entityName?uncap_first}Service.findAll(specs, pageable);
    }

}
