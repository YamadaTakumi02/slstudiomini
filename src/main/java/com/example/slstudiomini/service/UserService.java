package com.example.slstudiomini.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.slstudiomini.exception.MyUniqueConstraintViolationException;
import com.example.slstudiomini.model.Authority;
import com.example.slstudiomini.model.User;
import com.example.slstudiomini.repository.AuthorityRepository;
import com.example.slstudiomini.repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class UserService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    // public List<User> findAllUsers() {
    //     return userRepository.findAll();
    // }

    public List<User> findAllUsers() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> user = cq.from(User.class);
    
        // 条件を定義
        Predicate isEnabled = cb.equal(user.get("enabled"), true); // enabledがtrue
    
        // Authorityのリレーションを取得
        Join<User, Authority> authorityJoin = user.join("authorities"); // "authorities"はUserエンティティのフィールド名
    
        // authorityがROLE_USERの条件
        Predicate roleUser = cb.equal(authorityJoin.get("authority"), "ROLE_USER");
    
        // 条件を結合
        cq.select(user)
          .where(cb.and(isEnabled, roleUser));
    
        // クエリを実行し、結果を取得
        return entityManager.createQuery(cq).getResultList();
    }

    //Userデータをセーブするタイミングで有効化とハッシュ化、ロールの追加を行う
    public User addEnableStudentAndHashPassword(User user) {
        
        User uniqueUser = userRepository.findByUsername(user.getUsername());
        // 既に存在する場合は自作Exceptionをスロー
        if ( uniqueUser != null ) {
            throw new MyUniqueConstraintViolationException("既に存在するユーザーです");
        }
        // 有効化
        user.setEnabled(true);
        // ハッシュ化するクラスの準備
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // ハッシュ化
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        // パスワードの詰め直し
        user.setPassword(hashedPassword);

        Authority authority = authorityRepository.findByAuthority("ROLE_USER")
            .orElseThrow(() -> new EntityNotFoundException("Authority Not Found with name=USER"));

        user.setAuthorities(Set.of(authority));

        return userRepository.save(user);
    }
}
