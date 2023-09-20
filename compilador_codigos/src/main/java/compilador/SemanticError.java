package compilador;

public class SemanticError extends AnalysisError
{
    public SemanticError(String msg, int position, int line)
	 {
        super(msg, position, line);
    }

    public SemanticError(String msg)
    {
        super(msg);
    }
}
