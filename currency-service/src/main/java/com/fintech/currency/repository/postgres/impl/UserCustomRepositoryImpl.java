package com.fintech.currency.repository.postgres.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import com.fintech.currency.dto.postgres.UserProjection;
import com.fintech.currency.repository.postgres.UserCustomRepository;

import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {
	private final DatabaseClient databaseClient;

	@Override
	public Flux<UserProjection> findByFilters(String name, String email, String phone, 
			LocalDate dateOfBirth, 
			int page,
			int size) {
		String base = """
	            SELECT u.id, u.name, u.date_of_birth, e.email, p.phone_number 
	            FROM users u
	            LEFT JOIN email_data e ON u.id = e.user_id
	            LEFT JOIN phone_data p ON u.id = p.user_id
	            /*WHERE_CONDITIONS*/
	            OFFSET :offset LIMIT :limit
	        """;

	        List<String> conditions = new ArrayList<>();
	        Map<String, Object> params = new HashMap<>();
	        
	        if (name != null) {
	            conditions.add("u.name ILIKE :name");	            
	        }
	        
	        if (email != null) {
	            conditions.add("e.email = :email");
	            params.put("email", email);
	        }
	        
	        if (phone != null) {
	            conditions.add("p.phone_number = :phone");
	            params.put("phone", phone);
	        }
	        
	        if (dateOfBirth != null) {
	            conditions.add("u.date_of_birth > :dateOfBirth");
	            params.put("dateOfBirth", dateOfBirth);
	        }
	        
	        String whereClause = conditions.isEmpty() ? "" : "WHERE " + String.join(" AND ", conditions);
	        String finalSql = base.replace("/*WHERE_CONDITIONS*/", whereClause);

	        int offset = page * size;
	        
	        DatabaseClient.GenericExecuteSpec spec = databaseClient.sql(finalSql)
	                .bind("offset", offset)
	                .bind("limit", size);
	        
	        for (Map.Entry<String, Object> entry : params.entrySet()) {
	            spec = spec.bind(entry.getKey(), entry.getValue());
	        }

	        if (name != null) {
	            spec = spec.bind("name", name + "%");
	        }

	        return spec.map(this::mapRow).all();

	}
	
	private UserProjection mapRow(Row row, RowMetadata metadata) {
        return new UserProjection(
                row.get("id", UUID.class),
                row.get("name", String.class),
                row.get("email", String.class),
                row.get("phone_number", String.class),
                row.get("date_of_birth", LocalDate.class)
        );
    }

}
