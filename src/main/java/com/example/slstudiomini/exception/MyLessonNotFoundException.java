package com.example.slstudiomini.exception;

import jakarta.persistence.EntityNotFoundException;

public class MyLessonNotFoundException extends EntityNotFoundException {
    //コンストラクタ 親のコンストラクタを呼ぶ
    public MyLessonNotFoundException(String message) {
        super(message);
    }
}
