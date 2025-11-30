
package com.example.uni.exceptions;
public class ApiException extends RuntimeException {
    public ApiException(String msg) { super(msg); }
}


package com.example.uni.exceptions;
public class NotFoundException extends ApiException {
    public NotFoundException(String msg) { super(msg); }
}
