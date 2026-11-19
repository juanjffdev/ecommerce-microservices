package com.solenia.auth_service.adapter.persistence;


import com.solenia.auth_service.application.port.out.UserRepository;
import com.solenia.auth_service.domain.model.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcUserRepository(
            JdbcTemplate jdbcTemplate,
            NamedParameterJdbcTemplate namedParameterJdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    @Override
    public long save(User user){
        String sql = """
                INSERT INTO users (name,email,password,role)
                VALUES (:name, :email, :password, :role)
                """;
        //namedParameterJdbcTemplated hace el mapeo de los parametros :parametro en el sql.


        //Devuelve la cantidad de filas afectadas.

        /*  BeanProperySqlParameterSource genera una implementación de la intefaz sourceparameters
        es un envoltorio a user que devuelve un mapeo de las propiedades y valores de user para que
        que namedParemeterJdbcTemplated cambie los :parametros por los valores de user en la consulta.*/
        var params = new BeanPropertySqlParameterSource(user);//

        KeyHolder kh= new GeneratedKeyHolder(); //GeneratedKeyHolder es la clase que implemente keyholder

        /*  namedParameterJdbcTemplated hace el mapeo de los parametros :parametro en el sql.
        Devuelve la cantidad de filas afectadas.*/
        int insertedRows = namedParameterJdbcTemplate.update(sql,params,kh);

        if(insertedRows != 1){
            throw new IllegalStateException("No se pudo guardar el usuario");
        }

        Number newId = kh.getKey();

        if(newId==null){
            throw new IllegalStateException("No se obtuvo el id");
        }

        return newId.longValue();
    }

    @Override
    public int update(User user){
        String sql = """
                UPDATE users SET
                    name = :name,
                    email = :email
                WHERE id = :id
                """;

        return namedParameterJdbcTemplate.update(
                sql,
                new BeanPropertySqlParameterSource(user)
        );
    }

    @Override
    public List<User> findAll() {
        String sql = """
                SELECT id, name, email, password, role, created_at
                FROM users
                """;
        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(User.class)
        );
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql= """
                SELECT id, name, email, password, role, created_at
                FROM users
                WHERE id = ?                
                """;

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(User.class),
                id
        ).stream().findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql= """
                SELECT id, name, email, password, role, created_at
                FROM users
                WHERE email = ?                
                """;

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(User.class),
                email
        ).stream().findFirst();
    }
}
