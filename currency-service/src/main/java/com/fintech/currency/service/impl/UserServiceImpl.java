package com.fintech.currency.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.fintech.currency.avro.UserEvent;
import com.fintech.currency.dto.postgres.UserDTO;
import com.fintech.currency.dto.postgres.UserProjection;
import com.fintech.currency.kafka.producer.UserKafkaProducer;
import com.fintech.currency.mapper.postgres.EmailDataMapper;
import com.fintech.currency.mapper.postgres.PhoneDataMapper;
import com.fintech.currency.mapper.postgres.UserMapper;
import com.fintech.currency.model.postgres.Account;
import com.fintech.currency.model.postgres.EmailData;
import com.fintech.currency.model.postgres.PhoneData;
import com.fintech.currency.model.postgres.User;
import com.fintech.currency.repository.postgres.AccountRepository;
import com.fintech.currency.repository.postgres.EmailDataRepository;
import com.fintech.currency.repository.postgres.PhoneDataRepository;
import com.fintech.currency.repository.postgres.UserCustomRepository;
import com.fintech.currency.repository.postgres.UserRepository;
import com.fintech.currency.service.api.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
    private final UserCustomRepository userCustomRepository;
    private final UserKafkaProducer kafkaProducer;
    private final UserMapper userMapper;
    private final PhoneDataMapper phoneMapper;
    private final EmailDataMapper emailMapper;
    
    // TODO: вынести в AccountService / EmailService / PhoneService
    private final AccountRepository accountRepository;
    private final PhoneDataRepository phoneDataRepository;
    private final EmailDataRepository emailDataRepository;
    
	@Override
	public Mono<UserDTO> getById(UUID id) {
		log.info("📌 getById started: {}", id);
        return userRepository.findById(id)
        		.map(userMapper::toDto);
	}

	@Override
	public Mono<UserDTO> create(UserDTO dto) {
		log.info("📌 create started: {}", dto);

	    User user = userMapper.toEntity(dto);	    
	    user.setCreatedAt(LocalDateTime.now());
	    
	    // Хэшируем пароль перед сохранением
	    String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
	    user.setPassword(hashed);
	    
	    return userRepository.save(user)
	            .flatMap(savedUser -> {
	                // Используем сгенерированный user_id
	                UUID userId = savedUser.getId();
	                
	                // TODO: вынести в AccountService / EmailService / PhoneService
	                Account account = new Account();
	                account.setUserId(userId);
	                account.setInitialBalance(dto.getInitialBalance());
	                account.setBalance(dto.getInitialBalance());

	                List<PhoneData> phones = dto.getPhones().stream()
	                        .map(phone -> {
	                            PhoneData p = new PhoneData();
	                            p.setUserId(userId);
	                            p.setPhoneNumber(phone.getPhoneNumber());
	                            return p;
	                        }).toList();

	                List<EmailData> emails = dto.getEmails().stream()
	                        .map(email -> {
	                            EmailData e = new EmailData();
	                            e.setUserId(userId);
	                            e.setEmail(email.getEmail());
	                            return e;
	                        }).toList();

	                return accountRepository.save(account)
	                        .thenMany(Flux.fromIterable(phones).flatMap(phoneDataRepository::save))
	                        .thenMany(Flux.fromIterable(emails).flatMap(emailDataRepository::save))
	                        .then(
	                                Mono.zip(
	                                    Mono.just(savedUser),
	                                    Mono.just(account),
	                                    Mono.just(phones),
	                                    Mono.just(emails)
	                                )
	                            );
	            })
	            .map(tuple -> {
	                User savedUser = tuple.getT1();
	                Account account = tuple.getT2();
	                List<PhoneData> phones = tuple.getT3();
	                List<EmailData> emails = tuple.getT4();

	                // создаём DTO с вложенными данными
	                UserDTO dtoWithAll = userMapper.toDto(savedUser);
	                dtoWithAll.setInitialBalance(account.getInitialBalance());
	                dtoWithAll.setPhones(phones.stream().map(phoneMapper::toDto).toList());
	                dtoWithAll.setEmails(emails.stream().map(emailMapper::toDto).toList());

	                // отправляем событие в Kafka
	                UserEvent event = userMapper.toAvro(savedUser, "created");
	                kafkaProducer.sendEvent(savedUser.getId(), event).subscribe();

	                return dtoWithAll;
	            });
	}

	@Override
	public Mono<UserDTO> update(UUID id, UserDTO dto) {
		log.info("📌 update started: id={}, dto={}", id, dto);
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("User not found: " + id)))
                .map(existing -> {
                    userMapper.updateFromDto(dto, existing);
                    
                    // Захэшировать новый пароль, если он передан
                    if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                        String hashed = BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt());
                        existing.setPassword(hashed);
                    }
                    
                    return existing;
                })
                .flatMap(userRepository::save)
                .doOnNext(updated -> {
                    UserEvent event = userMapper.toAvro(updated, "updated");
                    kafkaProducer.sendEvent(updated.getId(), event).subscribe();
                })
                .map(userMapper::toDto);
	}

	@Override
	public Flux<UserProjection> search(String name, String email, String phone, LocalDate dateOfBirth, 
			int page, int size) {
		log.info("📌 search started: name={}, email={}, phone={}, dob={}", name, email, phone, dateOfBirth);
        return userCustomRepository.findByFilters(name, email, phone, dateOfBirth, page, size);
	}

}
