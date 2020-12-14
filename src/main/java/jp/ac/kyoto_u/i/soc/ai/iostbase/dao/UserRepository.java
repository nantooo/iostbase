package jp.ac.kyoto_u.i.soc.ai.iostbase.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.ac.kyoto_u.i.soc.ai.iostbase.dao.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);
}
