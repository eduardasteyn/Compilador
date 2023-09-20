package compilador;



public class Token
{
    private int id;
    private String lexeme;
    private int position;
    private int line;

    public Token(int id, String lexeme, int position, int line)
    {
        this.id = id;
        this.lexeme = lexeme;
        this.position = position;
        this.line = line;
    }

    public final int getId()
    {
        return id;
    }

    public final String getLexeme()
    {
        return lexeme;
    }

    public final int getPosition()
    {
        return position;
    }
    
    public final int getLine()
    {
        return line;
    }

    public String toString(){
        String msg = " ";

        switch (id) {
            case 2:
            msg = line +"   "+ "identificador" + "      "+lexeme+"";
                break;
            case 3:
            msg = line +"   "+ "constante int" + "      "+lexeme+"";
            break;
            case 4:
            msg = line +"   "+ "constante float" + "    "+lexeme+"";
            break;
            case 5:
            msg = line +"   "+ "constante char" + "     "+lexeme+"";
            break;
            case 6:
            msg = line +"   "+ "constante String" + "   "+lexeme+"";
            break;
            default:
            break;
        }

        if(id >= 7 && id <= 28){   
        msg = line +"   "+ "palavra reservada" + "  "+lexeme+"";
        }
        if (id >= 29 && id <= 49) {
        msg = line +"   "+ "símbolo especial" + "   "+lexeme+"";   
        }


    return msg;
    };
}
