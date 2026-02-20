import java.util.Scanner;
import java.util.Random;

public class PacmanS1 {
    //tamaños del tablero de juego
    private static final int FILAS_PEQUENO = 5;
    private static final int COLUMNAS_PEQUENO = 6;
    private static final int FILAS_GRANDE = 10;
    private static final int COLUMNAS_GRANDE = 10;
    
    //variables dependen del tamaño que el usuario elija
    private static int filasActuales;
    private static int columnasActuales;
    private static String[][] tablero;
    
    //variables al empezar el juego
    private static char tamaño = ' '; //esta almacena sola 1 letra
    private static  String nombreJugador = "";
    private int puntos = 0;
    private int vidas = 3;
    private static boolean jugando = true;
    
    //variables de los personajes
    private static final String fantasma = "@";
    private static final String premio = "0";
    private static final String premio_especial = "$";
    private static final String pared = "X";
    private static final String pacman = "<";
 
    private static Scanner sc = new Scanner(System.in);
    private static Random rand = new Random();

    public static void main(String [] args){
        System.out.println("BIENVENIDO A PAC-MAN :V");
        System.out.println("-------------------------");
        int opcionInicio;
        // \n hace salto de linea 
        System.out.println("ELIJA LAS SIGUIENTES OPCIONES");  
        System.out.println("\nElija 1 para crear el tablero (CONFIGURACION DEL  TABLERO)");
        System.out.println("Elija 2 para ver los puntos (HISTORIAL DE PUNTOS)");
        System.out.println("Elija 3 para salir ");
        
        opcionInicio = sc.nextInt();
        opcionElegida(opcionInicio);

        System.out.println("\nGracias por jugar PAC-MAN :,V");
    }
    // este proceso indica que opciones tiene el usuario en este caso son 3
    public static void opcionElegida(int opcionInicio){
        
            int numeroPremios;
            int numeroParedes;
            int numeroTrampas;
        
        switch (opcionInicio) {
                case 1:
                    sc.nextLine(); //evita que de enter automatico
                    System.out.println("\nNOMBRE DEL JUGADOR:");
                    nombreJugador = sc.nextLine();
                    //imprime las opciones del tablero en la pantalla
                    System.out.println("\n/////////////////////////////////////////"); 
                    System.out.println("ESCOJE EL TAMANO DEL TABLERO CON LA LETRA");
                    System.out.println("/////////////////////////////////////////");
                    System.out.println("P. TABLERO PEQUENO 5X6");
                    System.out.println("G. TABLERO GRANDE 10X10");
                    tamaño = sc.next().charAt(0); 

                    //Se usa While para que siga solo si es P o G
                    while(tamaño != 'P' && tamaño != 'G'){
                        System.out.println("COLOCAR UNA LETRA VALIDA");
                        tamaño = sc.next().charAt(0);
                    }

                    //se craaron variables ya que dependen de lo que escoja el usuario
                    if (tamaño == 'P'){
                      filasActuales = FILAS_PEQUENO;
                      columnasActuales = COLUMNAS_PEQUENO;
            
                    }else{
                       filasActuales = FILAS_GRANDE;
                      columnasActuales = COLUMNAS_GRANDE;
                    }
                  
                    numeroPremios = asignarCantidades("PREMIOS [0] (MIN:1)", 0.4);
                    numeroParedes = asignarCantidades("PAREDES [X] (MIN:1)", 0.2);
                    numeroTrampas = asignarCantidades("FANTASMAS [@](MIN:1)", 0.2);
                 
                    System.out.println("\nGENERANDO TABLERO...");
         
                    //se imprime el tablero en la pantalla
                    tablero = new String [filasActuales][columnasActuales];
                    
                    for (int i = 0; i < filasActuales; i++){
                        for (int j = 0; j < columnasActuales; j++){
                            tablero[i][j] = " ";
                        }
                    }

                    //Poniendo premios en la matriz
                    colocarElemento(tablero, premio, numeroPremios);
                    
                    //Poniendo las paredes en la matriz
                    colocarElemento(tablero, pared, numeroParedes);
                    
                    //Poniendo fantasmas
                    colocarElemento(tablero, fantasma, numeroTrampas);
                    
                    System.out.println("TABLERO DE "+filasActuales+"X"+columnasActuales+" CREADO!!!, A JUGAR");
                    
                    imprimirTablero(tablero);
                    System.out.println();
                    
                    int personajeFila;
                    int personajeColumna;
                    //este ciclo dice el rango para que el personaje aparesca
                    do{
                        System.out.println("POSICION PARA APARECER:");
                        System.out.print("FILAS (1 a " + filasActuales + "): ");
                        personajeFila = sc.nextInt();
                        System.out.print("COLUMNAS (1 a " + columnasActuales + "): ");
                        personajeColumna = sc.nextInt();
                        if(verificacionPosicionPersonaje(personajeFila, personajeColumna, tablero) == false){
                            System.out.println("ERROR POSICION INVALIDA, ESCOJE OTRA UBICACION!!");
                        }
                    }while(verificacionPosicionPersonaje(personajeFila, personajeColumna, tablero) == false);
                    
                    
                    System.out.println("GENERACION EXITOSA!");
                    tablero[personajeFila-1][personajeColumna-1] = pacman;
                    imprimirTablero(tablero);
                    
                    if (controles == f){
                        System.out.println("JUEGO EN PAUSA:");
                        System.out.println("1.CONTINUAR JUGANDO");
                        System.out.println("2.SALIR DEL JUEGO");
                    }

                    break;
                    // caso 2 se usa para el historial de puntos
                case 2:
                    System.out.println("--------------------------------------");
                    System.out.println("NOMBRE DEL JUGADOR: " +nombreJugador);
                    //System.out.println("PUNTOS TOTALES: ", puntos);
                    
                    System.out.println("--------------------------------------");
                    break;
                    // es la salida del juego
                case 3:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción inválida. Intenta de nuevo");
        }
    }
    
    public static void imprimirTablero(String[][] tablero) {
        // Borde superior
        for (int j = 0; j < columnasActuales + 2; j++) {
            System.out.print("--");
        }
        System.out.println();

        // Filas del tablero
        for (int i = 0; i < filasActuales; i++) {
            System.out.print("|");
            for (int j = 0; j < columnasActuales; j++) {
                System.out.print(tablero[i][j] + " ");
            }
            System.out.println("|");
        }

        // Borde inferior
        for (int j = 0; j < columnasActuales + 2; j++) {
            System.out.print("--");
        }
        System.out.println();
    }
    
    public static boolean verificacionPosicionPersonaje(int x, int y, String[][] tablero){
        // Verificamos que no se salga de la matriz antes de revisar el contenido
        if(x >= 1 && x <= filasActuales && y >= 1 && y <= columnasActuales){
            if(tablero[x-1][y-1].equals(" ")){
                return true;
            }
        }
        return false;
    }
    
    public static int asignarCantidades(String objeto, double porcentaje){
        boolean bandera = false;
        int cantidad = 0;
        int maxPremios = (int)(filasActuales * columnasActuales * porcentaje);
        //sirve para imprimir la cantidad de objetos en el tablero
        while(bandera == false){
            System.out.print("ELIJE LA CANTIDAD DE "+objeto + " (MAX:"+ maxPremios+")" ) ;
            cantidad = sc.nextInt();
              
            if (cantidad >= 1 && cantidad <= maxPremios) {
                bandera = true;
            } else {
                System.out.println("ERROR!!!. INGRESE UNA CANTIDAD QUE ESTE EN EL RANGO.");
            }  
        }
        return cantidad;
    }
    //coloca los elementos en el tablero
    public static void colocarElemento(String[][] tablero, String simbolo, int cantidad) {
        int colocados = 0;

        while (colocados < cantidad) {
            int fila = rand.nextInt(filasActuales);
            int columna = rand.nextInt(columnasActuales);

            if (tablero[fila][columna].equals(" ")) {
                tablero[fila][columna] = simbolo;
                colocados++;
            }
        }
    }

}