import java.util.Scanner;
import java.util.Random;

public class PacmanS1 {
    // Tamaños del tablero de juego
    private static final int FILAS_PEQUENO = 5;
    private static final int COLUMNAS_PEQUENO = 6;
    private static final int FILAS_GRANDE = 10;
    private static final int COLUMNAS_GRANDE = 10;
    
    // Variables dependen del tamaño que el usuario elija
    private static int filasActuales;
    private static int columnasActuales;
    private static String[][] tablero;
    
    // Variables al empezar el juego
    private static char tamaño = ' '; 
    private static String nombreJugador = "";
    
    // --- CORRECCIÓN: Se agregó 'static' a estas variables para que no den error ---
    private static int puntos = 0;
    private static int vidas = 3;
    private static boolean jugando = true;
    
    // Localización del pacman (Para saber dónde está siempre)
    private static int pacmanFila;
    private static int pacmanCol;
    
    // Variables de los personajes
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
        System.out.println("ELIJA LAS SIGUIENTES OPCIONES");  
        System.out.println("\nElija 1 para crear el tablero (CONFIGURACION DEL  TABLERO)");
        System.out.println("Elija 2 para ver los puntos (HISTORIAL DE PUNTOS)");
        System.out.println("Elija 3 para salir ");
        
        opcionInicio = sc.nextInt();
        opcionElegida(opcionInicio);

        System.out.println("\nGracias por jugar PAC-MAN :,V");
    }

    public static void opcionElegida(int opcionInicio){
        int numeroPremios;
        int numeroParedes;
        int numeroTrampas;
        
        switch (opcionInicio) {
            case 1:
                sc.nextLine(); 
                System.out.println("\nNOMBRE DEL JUGADOR:");
                nombreJugador = sc.nextLine();
                System.out.println("\n/////////////////////////////////////////"); 
                System.out.println("ESCOJE EL TAMANO DEL TABLERO CON LA LETRA");
                System.out.println("/////////////////////////////////////////");
                System.out.println("P. TABLERO PEQUENO 5X6");
                System.out.println("G. TABLERO GRANDE 10X10");
                tamaño = sc.next().toUpperCase().charAt(0); 

                while(tamaño != 'P' && tamaño != 'G'){
                    System.out.println("COLOCAR UNA LETRA VALIDA");
                    tamaño = sc.next().toUpperCase().charAt(0);
                }

                if (tamaño == 'P'){
                    filasActuales = FILAS_PEQUENO;
                    columnasActuales = COLUMNAS_PEQUENO;
                }else{
                    filasActuales = FILAS_GRANDE;
                    columnasActuales = COLUMNAS_GRANDE;
                }
              
                numeroPremios = asignarCantidades("PREMIOS [0] (MIN:1)", 0.4);
                int numEspeciales = asignarCantidades("PREMIOS [$] (MIN:1)", 0.1); // Agregado para el $
                numeroParedes = asignarCantidades("PAREDES [X] (MIN:1)", 0.2);
                numeroTrampas = asignarCantidades("FANTASMAS [@](MIN:1)", 0.2);
             
                System.out.println("\nGENERANDO TABLERO...");
     
                tablero = new String [filasActuales][columnasActuales];
                for (int i = 0; i < filasActuales; i++){
                    for (int j = 0; j < columnasActuales; j++){
                        tablero[i][j] = " ";
                    }
                }

                colocarElemento(tablero, premio, numeroPremios);
                colocarElemento(tablero, premio_especial, numEspeciales);
                colocarElemento(tablero, pared, numeroParedes);
                colocarElemento(tablero, fantasma, numeroTrampas);
                
                System.out.println("TABLERO DE "+filasActuales+"X"+columnasActuales+" CREADO!!!, A JUGAR");
                imprimirTablero(tablero);
                 
                //GENERACION DEL PERSONAJE
                int personajeFila;
                int personajeColumna;
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
                pacmanFila = personajeFila - 1;
                pacmanCol = personajeColumna - 1;
                tablero[pacmanFila][pacmanCol] = pacman;

                //TODO LO QUE ESTA EN ESTE BUCLE CONFIGURA EL JUEGO
                jugando = true;
                while (jugando) {
                    System.out.println("\nJUGADOR: " + nombreJugador + " | PUNTOS: " + puntos + " | VIDAS: " + vidas);
                    imprimirTablero(tablero);
                    System.out.print("Mover (8:Arriba, 5:Abajo, 4:Izq, 6:Der, F:Pausa): ");
                    char tecla = sc.next().charAt(0);
                    
                    //ESTE CICLO NOS INDICA LA CONFIGURACION DE LA PAUSA Y SU TECLA
                    if (tecla == 'F') {
                        System.out.println("1. Continuar | 2. Salir al menu");
                        int pausa = sc.nextInt();
                        
                        if (pausa == 2) {
                            jugando = false;
                        }
                    } else {
                        movimientoPacman(tecla); // MOVIEMIENTOPACMAN ES UNA CLASE 
                    }
                    //ESTE CICLO NOS INDICA CUANDO EL USUARIO HA PERDIDO
                    if (vidas <= 0) {
                        System.out.println("--- JUEGO TERMINADO :( ---");
                        jugando = false;
                    }
                }
                break;
                // SE CONFIGURA EL HISTORIAL DE PUNTOS
            case 2:
                System.out.println("--------------------------------------");
                System.out.println("NOMBRE DEL JUGADOR: " + nombreJugador);
                System.out.println("PUNTOS TOTALES: " + puntos);
                System.out.println("--------------------------------------");
                break;
                //ESTA CLASE NOS SACA DEL JUEGO
            case 3:
                System.exit(0);
                break;
            default:
                System.out.println("Opción inválida. Intenta de nuevo");
        }
    }

    //MOVIEMINTO DEL PACMAN
    public static void movimientoPacman(char tecla) {
        int moverFila = pacmanFila;
        int moverColumna = pacmanCol;
        //CONFIGURA LAS TECLAS DE MOVIEMIENTO DEL PACMAN
        if (tecla == '8') moverFila--;
        else if (tecla == '5') moverFila++;
        else if (tecla == '4') moverColumna--;
        else if (tecla == '6') moverColumna++;
        else return;

        //BORDEN INFINITOS (SE VA DE UN LADO Y REGRESA A OTRO)
        if (moverFila < 0) moverFila = filasActuales - 1;
        if (moverFila >= filasActuales) moverFila = 0;
        if (moverColumna < 0) moverColumna = columnasActuales - 1;
        if (moverColumna >= columnasActuales) moverColumna = 0;

        //CHOQUE CON PAREDES OSEA LAS X
        if (tablero[moverFila][moverColumna].equals(pared)) {
            System.out.println("¡PARED!");
            return;
        } 
        //DITRIBUCION DE PREMIOS (0 , $)Y FANTASMAS @
        if (tablero[moverFila][moverColumna].equals(premio)){ puntos += 10;
            System.out.println("PREMIO SIMPLE +10 PUNTOS");
        }else if (tablero[moverFila][moverColumna].equals(premio_especial)){ puntos += 25;
              System.out.println("!!!PREMIO ESPECIAL !!! +25 PUNTOS");
        }else if (tablero[moverFila][moverColumna].equals(fantasma)) {
            vidas--;
            System.out.println("PERDISTE UNA VIDA! :c");
        }

        // MOVER EN EL ARREGLO
        tablero[pacmanFila][pacmanCol] = " ";
        pacmanFila = moverFila;
        pacmanCol = moverColumna;
        tablero[pacmanFila][pacmanCol] = pacman;
    }
    //IMPRESION DEL TABLERO 
    public static void imprimirTablero(String[][] tablero) {
        for (int j = 0; j < columnasActuales + 2; j++) System.out.print("--");
        System.out.println();
        for (int i = 0; i < filasActuales; i++) {
            System.out.print("|");
            for (int j = 0; j < columnasActuales; j++) {
                System.out.print(tablero[i][j] + " ");
            }
            System.out.println("|");
        }
        for (int j = 0; j < columnasActuales + 2; j++) System.out.print("--");
        System.out.println();
    }
    // VERIFICA SI LA POSICION DEL PACMAN ES CORRECTA 
    public static boolean verificacionPosicionPersonaje(int x, int y, String[][] tablero){
        if(x >= 1 && x <= filasActuales && y >= 1 && y <= columnasActuales){
            if(tablero[x-1][y-1].equals(" ")){
                return true;
            }
        }
        return false;
    }
    //ESTE METODO ADIGANA LA CANTIDAD DE LOS OBJETOS DEPENDE DEL TABLERO
    public static int asignarCantidades(String objeto, double porcentaje){
        boolean bandera = false;
        int cantidad = 0;
        int maxItems = (int)(filasActuales * columnasActuales * porcentaje);
        while(bandera == false){
            System.out.print("CANTIDAD DE "+objeto + " (MAX:"+ maxItems+"): " ) ;
            cantidad = sc.nextInt();
            if (cantidad >= 1 && cantidad <= maxItems) {
                bandera = true;
            } else {
                System.out.println("ERROR!!!. FUERA DE RANGO.");
            }  
        }
        return cantidad;
    }
    //ESTA CLASE NOS INDICA COMO SE COLOCAN LOS OBJETOS DE FORMA ALEATORIA
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