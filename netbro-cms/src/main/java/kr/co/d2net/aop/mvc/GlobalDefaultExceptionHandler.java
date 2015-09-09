package kr.co.d2net.aop.mvc;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import kr.co.d2net.exception.XmlParsingException;

import org.hibernate.HibernateException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import freemarker.core.InvalidReferenceException;

//@ControllerAdvice
class GlobalDefaultExceptionHandler {
    public static final String DEFAULT_ERROR_VIEW = "error";

    
    @ExceptionHandler({ HibernateException.class})
  	public void hibernateError(Exception exception) {
  		// Nothing to do. Return value 'databaseError' used as logical view name
  		// of an error page, passed to view-resolver(s) in usual way.
  		System.out.println("GlobalDefaultExceptionHandler hibernateError " + exception.getClass().getSimpleName());
  		System.out.println("GlobalDefaultExceptionHandler hibernateError " + exception.getMessage());
  		//return "error.jsp";
  	}

 /*   @ExceptionHandler({ NullPointerException.class})
  	public ModelAndView   nullPointerError(NullPointerException exception) {
  		
    	ModelAndView mav = new ModelAndView("error");
    	mav.addObject("name", exception.getClass().getSimpleName());
        mav.addObject("message", exception.getMessage());
  		return  mav; 
  	}*/
    @ExceptionHandler({InvalidReferenceException.class})
  	public void freeMarkerError(Exception exception) {
  		// Nothing to do. Return value 'databaseError' used as logical view name
  		// of an error page, passed to view-resolver(s) in usual way.
  		System.out.println("GlobalDefaultExceptionHandler freeMarkerError " + exception.getClass().getSimpleName());
  		System.out.println("GlobalDefaultExceptionHandler freeMarkerError " + exception.getMessage());
  		//return "error.jsp";
  	}

   /* @ExceptionHandler({Exception.class})
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it - like the OrderNotFoundException example
        // at the start of this post.
        // AnnotationUtils is a Spring Framework utility class.
    	
    	 System.out.println("GlobalDefaultExceptionHandler defaultErrorHandler " + e.getClass().getSimpleName());
    	 
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
            throw e;
        
       

        // Otherwise setup and send the user to a default error-view.
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    }*/
}
