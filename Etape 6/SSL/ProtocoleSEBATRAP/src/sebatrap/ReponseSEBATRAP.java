package sebatrap;

import java.io.Serializable;
import requetepoolthreads.Reponse;

public class ReponseSEBATRAP implements Reponse, Serializable
{
    public final static int PAYMENT_OPERATION_OK = 200;
    public final static int PAYMENT_OPERATION_ERROR = 400;
    
    private final int codeRetour;
    
    public ReponseSEBATRAP(int cR)
    {
        codeRetour = cR;
    }
    
    @Override
    public int getCode()
    {
        return codeRetour;
    }
    
}
