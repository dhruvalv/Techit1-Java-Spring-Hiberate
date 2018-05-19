package techit.rest.error;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    private static Logger logger = LogManager
        .getLogger( RestExceptionHandler.class );

    @ExceptionHandler(RestException.class)
    public ResponseEntity<Object> handleRestExceptions( RestException ex )
    {
        RestError error = ex.getError();
        return new ResponseEntity<Object>( ex.getError(),
            HttpStatus.valueOf( error.getStatusCode() ) );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleOtherExceptions( Exception ex )
    {
        logger.error( "General Error", ex );
        return new ResponseEntity<Object>(
            new RestError( 500, ex.getMessage() ),
            HttpStatus.INTERNAL_SERVER_ERROR );
    }

}
