package ${projectBasePackage}.service;

import ${projectBasePackage}.entity.${entityName};
import com.lephix.easy.mvc.AbstractEasyRepository;
import com.lephix.easy.mvc.AbstractEasyService;
import ${projectBasePackage}.repository.${entityName}Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ${entityName}Service extends AbstractEasyService<${entityName}> {

    @Autowired
    private ${entityName}Repository repository;

    @Override
    public AbstractEasyRepository<${entityName}> getRepository() {
        return repository;
    }

}
