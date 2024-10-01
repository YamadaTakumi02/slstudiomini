package com.example.slstudiomini.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import jakarta.persistence.EntityNotFoundException;
//@ControllerAdviceを記述したクラスにエラーが送られる
@ControllerAdvice
public class GlobalExceptionHanlder {
    //ログを記録
    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHanlder.class);


    @ExceptionHandler(DataIntegrityViolationException.class)
    public Object handleDataIntegrityViolationException(Exception ex) {
        ModelAndView mav = new ModelAndView("error/custom-error");
        mav.addObject("errorMessage", "ユニーク性違反でエラーが出た可能性があります");
        return mav;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public Object handleEntityNotFoundException(Exception ex) {
        ModelAndView mav = new ModelAndView("error/custom-error");
        mav.addObject("errorMessage", "存在しないコースです");
        return mav;
    }

    //全ての例外タイプを補足
    @ExceptionHandler(Exception.class)
    public Object handleAllExceptions(Exception ex) {
        ModelAndView mav = new ModelAndView("error/custom-error");
        mav.addObject("errorMessage", "開発者の恥です。ごめんなさい。");
        return mav;
    }
    
    @ExceptionHandler(MyUniqueConstraintViolationException.class)
    public Object handleMyUniqueConstraintViolationException(Exception ex) {
    ModelAndView mav = new ModelAndView("error/custom-error");
    mav.addObject("errorMessage", ex.getMessage());
    return mav;
    }
    //自作クラスMyLessonNotFoundExceptionから送られたメッセージを出力
    @ExceptionHandler(MyLessonNotFoundException.class)
    public Object handleMyLessonNotFoundException(MyLessonNotFoundException ex) {
        
        //ログを出力
        logger.error("不正なLessonへのアクセスが行われました。エラー詳細:" + ex.getMessage());
        //ログレベル設定
        // logger.info();
        // logger.warn();
        // logger.debug();

        ModelAndView mav = new ModelAndView("error/custom-error");
        mav.addObject("errorMessage", ex.getMessage());
        return mav;
    }
}
