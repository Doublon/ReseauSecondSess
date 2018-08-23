package pay;

import java.io.Serializable;
import requetepoolthreads.Reponse;

public class ReponsePAY implements Reponse, Serializable
{   
    public final static int REQUEST_PAYMENT_OK = 200;
    public final static int REQUEST_PAYMENT_ERROR = 400;
    
    private final int codeRetour;
    
    public ReponsePAY(int cR)
    {
        codeRetour = cR;
    }
    
    @Override
    public int getCode()
    {
        return codeRetour;
    }
}
