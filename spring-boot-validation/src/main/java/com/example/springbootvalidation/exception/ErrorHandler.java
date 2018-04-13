package com.example.springbootvalidation.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.lang.reflect.Field;
import java.util.Locale;

@ControllerAdvice
@AllArgsConstructor
@Slf4j
public class ErrorHandler {

    private MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAll(Exception ex, WebRequest request){
        ApiError err = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "An error has occurred.", ex.getLocalizedMessage());

        BindingResult result = getBindingResult(ex);
        if( result != null && result.getAllErrors() != null ){
            for(DefaultMessageSourceResolvable e : result.getAllErrors()){
                interpolate(e);
            }
        }

        if( result.getFieldErrors() != null ){
            err.setFieldErrors(result.getFieldErrors());
        }

        if( result.getGlobalErrors() != null ){
            err.setObjectErrors(result.getGlobalErrors());
        }

        return new ResponseEntity(err, new HttpHeaders(), err.getStatus());
    }

    /**
     * Interpolate message.
     *
     * @param e
     */
    private void interpolate(DefaultMessageSourceResolvable e) {
        String message = messageSource.getMessage(e, Locale.getDefault());
        if( message != null ){
            setDefaultMessage(e, message);
        }
        printDebug(e, message);
    }

    /**
     * Use reflection to get bindingResult.
     *
     * @param ex
     * @return
     */
    private BindingResult getBindingResult(Exception ex) {
        try{
            Field bindingResult = ReflectionUtils.findField(ex.getClass(), "bindingResult");
            if( bindingResult != null ){
                ReflectionUtils.makeAccessible(bindingResult);
                return (BindingResult)bindingResult.get(ex);
            }
        }catch(Exception exx){
            exx.printStackTrace();
        }
        return null;
    }

    /**
     * Use reflection to set default message.
     *
     * @param e
     * @param message
     */
    private void setDefaultMessage(DefaultMessageSourceResolvable e, String message) {
        try{
            Field defaultMessage = ReflectionUtils.findField(DefaultMessageSourceResolvable.class, "defaultMessage");
            ReflectionUtils.makeAccessible(defaultMessage);
            defaultMessage.set(e, message);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Pretty print debug.
     *
     * @param e
     * @param message
     */
    private void printDebug(DefaultMessageSourceResolvable e, String message) {
        if( log.isDebugEnabled() ){
            if( e instanceof FieldError){
                FieldError fe = (FieldError)e;
                log.debug("object={}, field={}, defaultMessage={}, arguments={} " +
                        "codes={} resolvedMessage={}", fe.getObjectName(), fe.getField(), e.getDefaultMessage(), prettyOutput(e.getArguments()), e.getCodes(), message);
            }else if( e instanceof ObjectError){
                ObjectError oe = (ObjectError)e;
                log.debug("object={}, defaultMessage={}, arguments={} " +
                        "codes={} resolvedMessage={}", oe.getObjectName(),  e.getDefaultMessage(), prettyOutput(e.getArguments()), e.getCodes(), message);
            }
            else{
                log.debug("defaultMessage={}, arguments={} " +
                        "codes={} resolvedMessage={}", e.getDefaultMessage(), prettyOutput(e.getArguments()), e.getCodes(), message);
            }
        }
    }

    /**
     * Helper function to pretty up the output.
     * @param a
     * @return
     */
    private String[] prettyOutput(Object a[]) {
        String[] codeStr = null;
        if( a != null ){
            codeStr = new String[a.length];
            for(int i=0; i < a.length; i++){
                codeStr[i] = a[i] instanceof DefaultMessageSourceResolvable ? ((DefaultMessageSourceResolvable)a[i]).getDefaultMessage()
                        : (a[i] != null ? a[i].toString() : null);
            }
        }
        return codeStr;
    }

}
