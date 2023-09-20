package compilador;



public class AnalysisError extends Exception
{
    private int position;
    private int line;

    public AnalysisError(String msg, int position, int line)
    {
        super(msg);
        this.position = position;
        this.line = line;
    }

    public AnalysisError(String msg)
    {
        super(msg);
        this.position = -1;
        this.line = -1;
    }

    public int getPosition()
    {
        return position;
    }

    public int getLine()
    {
        return line;
    }

    public String toString()
    {
        return "Erro na linha "+line+ " - " + this.getMessage(); // + ", @ "+position;  // Erro na linha X - @ simbolo invalido
    }
}
