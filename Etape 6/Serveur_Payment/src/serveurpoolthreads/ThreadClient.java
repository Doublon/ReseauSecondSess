package serveurpoolthreads;

public class ThreadClient extends Thread
{
    private final SourceTaches tachesAExecuter;
    private final String nom;
    private Runnable tacheEnCours;
    
    public ThreadClient(SourceTaches st, String n)
    {
        tachesAExecuter = st;
        nom = n;
    }
    
    @Override
    public void run()
    {
        while (!isInterrupted())
        {
            try
            {
                tacheEnCours = tachesAExecuter.getTache();
            }
            catch (InterruptedException e)
            {
                System.out.println("Interruption : " + e.getMessage());
            }
            tacheEnCours.run();
        }
    }
}
