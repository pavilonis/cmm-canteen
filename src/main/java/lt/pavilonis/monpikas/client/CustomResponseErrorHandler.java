package lt.pavilonis.monpikas.client;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

public class CustomResponseErrorHandler extends DefaultResponseErrorHandler {
   @Override
   public void handleError(ClientHttpResponse response) throws IOException {
      // check for expected status codes
      if (isExpected(response.getStatusCode())) {
         return;
      }
      super.handleError(response);
   }

   @Override
   public boolean hasError(ClientHttpResponse response) throws IOException {
      // check for expected status codes
      if (isExpected(response.getStatusCode())) {
         return false;
      }
      return super.hasError(response);
   }

   private boolean isExpected(HttpStatus statusCode) {
      return (statusCode == HttpStatus.ACCEPTED
            || statusCode == HttpStatus.ALREADY_REPORTED
            || statusCode == HttpStatus.FORBIDDEN
            || statusCode == HttpStatus.BAD_REQUEST
            || statusCode == HttpStatus.NOT_FOUND
            || statusCode == HttpStatus.INTERNAL_SERVER_ERROR);
   }
}
