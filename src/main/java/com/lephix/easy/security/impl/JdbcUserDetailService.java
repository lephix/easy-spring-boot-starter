package com.lephix.easy.security.impl;

import com.lephix.easy.security.AbstractUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@ConditionalOnProperty(prefix = "easy", name = "security.source", havingValue = "jdbc")
@Component
@Slf4j
public class JdbcUserDetailService extends AbstractUserDetailService {

    @Autowired
    private DataSource dataSource;

    private NamedParameterJdbcTemplate namedJdbcTemplate;

    public JdbcUserDetailService() {
    }

    public JdbcUserDetailService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    private void init() {
        namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Map<String, Object> map = findByUsernameWithPassword(username);
        return new User(
                map.get("username").toString(),
                map.get("password").toString(),
                buildAuthorities(map.get("roles").toString())
        );
    }

    public Map<String, Object> findByUsername(String username) {
        return erasePassword(findByUsernameWithPassword(username));
    }

    private Map<String, Object> findByUsernameWithPassword(String username) {
        String sql = "SELECT * FROM user WHERE username=:username";
        SqlParameterSource params = new MapSqlParameterSource().addValue("username", username);
        List<Map<String, Object>> rows = namedJdbcTemplate.queryForList(sql, params);

        if (rows.size() != 1) {
            throw new UsernameNotFoundException("Incorrect username: " + username);
        } else {
            return rows.get(0);
        }
    }

    private Map<String, Object> erasePassword(Map<String, Object> map) {
        map.remove("password");
        return map;
    }


}
