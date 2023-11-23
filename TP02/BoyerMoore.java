import java.io.RandomAccessFile;
import java.util.Scanner;

class Hash
{
    public int  [] pos;

    public Hash ()
    {
        this.pos = new int  [256];
    }

    public int getPos(char search)
    {
        return(this.pos[search % 256]);
    }

    public void badChar (String frase)
    {

        for(int i = 0; i < pos.length; i++)
        {
            this.pos[i] = -1;
        }

        for(int i = 0; i < frase.length() - 1; i++)
        {
            pos[frase.charAt(i) % 256] = i;
        }

    }

}

public class BoyerMoore 
{

	public void pesquisa () 
	{
		try 
        {
            Scanner sc = new Scanner(System.in);

            System.out.print("\nNome do Arquivo: ");
            String path   = sc.nextLine();
            RandomAccessFile ra = new RandomAccessFile ("./" + path, "r");

            String frase = ra.readLine();

            System.out.print("\nFrase para pesquisar: ");
            String palavra = sc.nextLine();

            pesquisa(frase, palavra);
        } 
        catch (Exception e) 
        
        {
            System.out.println("ERRO: ARQUIVO NÃO ENCONTRADO");
        }


	}

    public static void pesquisa (String frase, String palavra)
    {

        Hash tabela = new Hash();
        
        tabela.badChar(palavra);

        int o = palavra.length() - 1;
        int i = palavra.length() - 1;
            
        while(i < frase.length())
        {

            if(palavra.charAt(o) == frase.charAt(i))
            {
                o--;
                i--;

                if(o == 1)
                {
                    System.out.println("ACHOU");
                    break;
                }

            }
            else
            {
                int tmp = tabela.getPos(frase.charAt(i));

                if(tmp < 0)
                {
                    i = i + palavra.length();
                    o = palavra.length() - 1;
                }
                else
                {
                    i = i + (o - tabela.getPos(frase.charAt(i)));
                    o = palavra.length() - 1;
                }

            }

        }

    }
    
}
