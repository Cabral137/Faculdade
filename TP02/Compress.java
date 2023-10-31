import java.util.Scanner;

class Compress
{

    public static void main(String[] args) 
    {
    
        System.out.println ("\n\n");
        System.out.println ("\nOpções:\n" +
                            "1 - Huffman\n" +
                            "2 - LZW\n" +
                            "0 - Sair\n");

        Scanner sc = new Scanner (System.in);

        int action = Integer.parseInt(sc.nextLine());

        System.out.print("\nNome do arquivo: ");
        String path = sc.nextLine();

        switch (action) 
        {
            case 1:
            Huffman hf = new Huffman();
            hf.comprimir(path);
            break;

            case 2:
            // LZW (arquivo);
            break;
        
            default:
            break;
        }

    }

}
