package com.example.slstudiomini.exception;

import org.springframework.dao.DataIntegrityViolationException;

    public class MyUniqueConstraintViolationException extends DataIntegrityViolationException {
    //コンストラクタ 親のコンストラクタを呼ぶ
    public MyUniqueConstraintViolationException(String message) {
        super(message);
    }
}

