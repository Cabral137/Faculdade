import java.io.RandomAccessFile;
import java.util.Vector;

public class Intercal 
{

    long    filePointer;
    long [] ponteirosCaminho;

    Intercal ()
    {
        this.filePointer = 0;
        this.ponteirosCaminho = null;
    }

    public static void swap (Musica [] array, int i, int j)
    {
        Musica temp = array [i];
        array[i]    = array [j];
        array[j]    = temp;
    }

    public static int partition (Musica [] array, int high, int low)
    {

        Musica pivot = array [high];

        int i = (low - 1);

        for(int j = low; j <= high - 1; j++)
        {
            if(array[j].getID() < pivot.getID())
            {
                i++;
                swap(array, i, j);
            }
        }

        swap (array, i + 1, high);

        return(i + 1);

    }

    public static void quickSort (Musica [] array, int high, int low)
    {
        if(low < high)
        {
            int p1 = partition(array, high, low);

            quickSort(array, high, p1 - 1);
            quickSort(array, p1 + 1, low);
        }
    }

    public static void quickSort (Musica [] array)
    {
        quickSort(array, 0, array.length - 1);
    }

    public Musica [] readArray (int registros, String path)
    {
        Binario bin = new Binario();

        Musica [] array = new Musica [registros];
        Musica music = new Musica ();
        
        try 
        {
            RandomAccessFile ra = new RandomAccessFile(path, "r");
            ra.seek(filePointer);
            int contador = 0;

            while(contador < registros)
            {
                boolean lapide = ra.readBoolean();
                int tamanho = ra.readShort();
                int id = ra.readShort();

                byte [] musicArray = new byte [tamanho - 2];
                ra.read(musicArray);
                music = bin.fromByteArray(musicArray);
                music.setID(id);
                array[contador] = music;
                contador++;
            }

            filePointer = ra.getFilePointer();
            ra.close();

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        

        return (array);

    }

    public void writeCaminho (String path, Musica [] array)
    {

        try 
        {
            RandomAccessFile rf = new RandomAccessFile (path, "rw");
            Binario bin = new Binario ();
            rf.seek(rf.length());
            
            for(int i = 0; i < array.length - 1; i++)
            {
                byte [] tmp = bin.toByteArray(array[i]);

                rf.writeBoolean(false);
                rf.writeShort(tmp.length);
                rf.write(tmp);
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

    }

    public void primeiraParte (int registros, int caminhos)
    {

        try 
        {
            for(int i = 0; i < caminhos; i++)
            {
                RandomAccessFile ra = new RandomAccessFile("./ARQUIVOS/caminho" + i + ".hex", "rw");
                ra.close();
            }   

            for(int j = 0; j < 5; j++)
            {
                for(int i = 0; i < caminhos; i++)
                {
                    Musica [] array = readArray(registros, "./SpotifyMusic.hex");

                    quickSort (array);
                    String path = "./ARQUIVOS/caminho" + i + ".hex";
                    writeCaminho(path, array);
                }
            }

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

    }

    public static Musica getMenor (Musica [] array)
    {
        Musica menor = array[0];
    
        for(int i = 0; i < array.length; i++)
        {
            if(menor.getID() > array[i].getID())
            {
                menor = array[i];
            }
        }
            
        return(menor);
            
    }

    public static int quantRegistros (Vector<Musica[]> matriz)
    {
        int quant = 0;

        for(int i = 0; i < matriz.size(); i++)
        {
            quant = quant + matriz.get(i).length;
        }

        return(quant);

    }

    public static Musica [] ordenarMatriz(Vector<Musica[]> matriz)
    {
    
        Musica [] tmp  = new Musica [matriz.size()];
        Musica [] resp = new Musica [quantRegistros(matriz)]; // caminhos * numero de registros = numero total de registros
        int contador = 0;
          
        int [] ponteiros = new int [matriz.size()];
    
        for(int j = 0; j < quantRegistros(matriz); j++)
        {                
                System.out.println(quantRegistros(matriz) + "---" + tmp.length);
            for(int i = 0; i < matriz.size(); i++)
            {
                if(ponteiros[i] < matriz.get(0).length)
                {
                    tmp[i] = matriz.get(i)[ponteiros[i]];
                }
                else
                {
                    
                }          
            }

            System.out.println("\n\n");
 
            Musica tmp2 = getMenor(tmp);
            System.out.println(tmp2.getName() + "---" + tmp2.getID());
            resp[contador] = tmp2;
                
            for(int p = 0; p < tmp.length; p++)
            {
                if(tmp2.getID() == tmp[p].getID())
                {
                    ponteiros[p] = ponteiros[p] + 1;
                }
            }
    
            contador++;
    
        }
    
        return(resp);
    
    }

    public Musica [] readCaminho (int registros, String path)
    {
        Binario bin = new Binario();

        Musica [] array = new Musica [registros];
        Musica music = new Musica ();
        
        try 
        {
            RandomAccessFile ra = new RandomAccessFile("./ARQUIVOS/" + path, "r");
            ra.seek(ponteirosCaminho[Character.getNumericValue(path.charAt(7))]);
            int contador = 0;

            while(contador < registros)
            {
                boolean lapide = ra.readBoolean();
                int tamanho = ra.readShort();
                int id = ra.readShort();

                byte [] musicArray = new byte [tamanho - 2];
                ra.read(musicArray);
                music = bin.fromByteArray(musicArray);
                music.setID(id);
                array[contador] = music;
                contador++;
            }

            ponteirosCaminho[Character.getNumericValue(path.charAt(7))] = ra.getFilePointer();
            ra.close();

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        

        return (array);
    }

    public void balanceada (int registros, int caminhos)
    {

        primeiraParte (registros, caminhos);

        //INICIALIZANDO VETOR DE PONTEIROS
        ponteirosCaminho = new long [caminhos];


        
        Vector<Musica[]> array = new Vector<Musica[]>(caminhos);

        try 
        {
            for(int i = 0; i < caminhos; i++)
            {

                for(int j = 0; j < caminhos; j++)
                {
                    Musica [] tmp = readCaminho(registros/caminhos, "caminho" + j + ".hex");
                    array.add(tmp);
                }

                Musica [] tmp = ordenarMatriz (array);

                // for(int j = 0; j < tmp.length; j++)
                // {
                //     System.out.println(tmp[j].getName() + " --- " + tmp[j].getID());
                // }

                System.out.println("\n\n\n\n");

                String path = "./ARQUIVOS/caminho" + (i + caminhos) + ".hex";
                RandomAccessFile ra = new RandomAccessFile(path, "rw");
                writeCaminho(path, tmp);
            }

        } 
        catch (Exception e) 
        {
            // TODO: handle exception
        }



    }
    
}