package pa2;

class TimeOutException extends Exception
{
      public TimeOutException() {}

      public TimeOutException(String message)
      {
         super(message);
      }
 }