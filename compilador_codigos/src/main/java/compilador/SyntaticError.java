package compilador;
public class SyntaticError extends AnalysisError
{
    public SyntaticError(String msg, int position, int line)
	 {
        super(msg, position, line);
    }

    public SyntaticError(String msg)
    {
        super(msg);
    }
    
}
