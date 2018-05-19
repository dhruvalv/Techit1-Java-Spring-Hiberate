package techit.rest.error;

public class RestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    RestError error;

    public RestException( int statusCode, String message )
    {
        super( message );
        this.error = new RestError( statusCode, message );
    }

    public RestError getError()
    {
        return error;
    }

    public void setError( RestError error )
    {
        this.error = error;
    }

}
