import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        //lectura del archivo de texto
        String filePath = "numeros.txt";
        ArrayList<int[]> figuras = new ArrayList<>();
        ArrayList<int[]> respuesta1 = new ArrayList<>();
        ArrayList<int[][]> respuesta2 = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] partes = line.split(",");
                int[] array = new int[3];
                for (int i = 0; i < 3; i++) {
                    array[i] = Integer.parseInt(partes[i].trim());
                }
                figuras.add(array);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long startTimeIterativo = System.nanoTime();
        respuesta1 = iterativo(figuras);
        long endTimeIterativo = System.nanoTime();
        long durationIterativo = endTimeIterativo - startTimeIterativo;
        System.out.println("Tiempo de ejecución de iterativo: " + durationIterativo + " nanosegundos");

        imprimirListaDeDos(respuesta1);

        long startTimeDividYVencer = System.nanoTime();
        respuesta2 = dividirYVencer(figuras);
        long endTimeDividYVencer = System.nanoTime();
        long durationDividYVencer = endTimeDividYVencer - startTimeDividYVencer;
        System.out.println("Tiempo de ejecución de dividYVencer: " + durationDividYVencer + " nanosegundos");
        
        imprimirArrayList(respuesta2);
    }

    public static ArrayList<int[]> iterativo(ArrayList<int[]> figuras){
        ArrayList<int[]> contornobase = new ArrayList<>();
        ArrayList<int[]> agregar = new ArrayList<>();
        for (int i = 0; i < figuras.size(); i++) {
            int[] arrayTres = figuras.get(i);
            int[] arrayDos1 = {arrayTres[0], arrayTres[2]};
            int[] arrayDos2 = {arrayTres[1], 0};
            if (i < 1) {
                contornobase.add(arrayDos1);
                contornobase.add(arrayDos2);
            } else {
                agregar.add(arrayDos1);
                agregar.add(arrayDos2);
            }
        }
        return iterar(contornobase, agregar);
    }

    public static ArrayList<int[]> iterar(ArrayList<int[]> contornobase,ArrayList<int[]> agregar){
        if (!agregar.isEmpty()) {
            int[] inicio = agregar.remove(0);
            int[] fin = agregar.remove(0);
            for (int i = 0; i < contornobase.size(); i++) {
                if(inicio[0]<=contornobase.get(i)[0]){
                    if (inicio[0]==contornobase.get(i)[0]) {
                        if(inicio[1]>contornobase.get(i)[1]){
                            contornobase.set(i, inicio);
                        }
                    } else {
                        if(inicio[1]>contornobase.get(i)[1]||i==0){
                            contornobase.add(i, inicio);
                        }
                    }
                    for (int j = i+1; j < contornobase.size(); j++) {
                        if(fin[0]<contornobase.get(j)[0]){
                            if (inicio[1]==contornobase.get(j-1)[1]&&contornobase.get(j)[1]!=0) {
                                contornobase.add(j, fin);
                                break;
                            }
                        } else if (fin[0]>contornobase.get(j)[0]) {
                            if(inicio[1]>=contornobase.get(j)[1]){
                                if (inicio[1]<contornobase.get(j-1)[1]) {
                                    int[] aux = contornobase.get(j);
                                    aux[1]=inicio[1];
                                    contornobase.set(j, aux);
                                }else {
                                    contornobase.remove(j);
                                    j--;
                                }
                            }
                            if(j==contornobase.size()-1){
                                contornobase.add(j+1, fin);
                                break;
                            }
                        } else if(j==contornobase.size()-1&&fin[0]!=contornobase.get(j)[0]){
                            contornobase.add(j+1, fin);
                            break;
                        }
                    }
                    break;
                } 
            }
            iterar(contornobase, agregar);
        }
        return contornobase;
    }

    public static ArrayList<int[][]> dividirYVencer(ArrayList<int[]> figuras){
        ArrayList<int[][]> contorno = new ArrayList<>();
        for (int[] vector : figuras) {
            int x1 = vector[0];
            int y1 = vector[1];
            int x2 = vector[2];
            int y2 = 0;
            int[][] par = {{x1, y1}, {x2, y2}};
            contorno.add(par);
        }
        mergeSort(contorno, 0, contorno.size()-1);
        return contorno;
    }

    public static void mergeSort(ArrayList<int[][]> array, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(array, left, mid);
            mergeSort(array, mid + 1, right);
            merge(array, left, mid, right);
        }
    }

    public static void merge(ArrayList<int[][]> array, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
    
        ArrayList<int[][]> L = new ArrayList<>();
        ArrayList<int[][]> R = new ArrayList<>();
    
        for (int i = 0; i < n1; i++) {
            L.add(array.get(left + i));
        }
        for (int j = 0; j < n2; j++) {
            R.add(array.get(mid + 1 + j));
        }
    
        int i = 0, j = 0;
        int k = left;
        ArrayList<int[][]> merged = new ArrayList<>();
    
        // Combinar los dos contornos
        while (i < L.size() && j < R.size()) {
            if (L.get(i)[0][0] < R.get(j)[0][0] || 
                (L.get(i)[0][0] == R.get(j)[0][0] && L.get(i)[0][1] <= R.get(j)[0][1])) {
                merged.add(L.get(i++));
            } else {
                merged.add(R.get(j++));
            }
        }
    
        // Agregar los puntos restantes
        while (i < L.size()) {
            merged.add(L.get(i++));
        }
    
        while (j < R.size()) {
            merged.add(R.get(j++));
        }
    
        // Actualizar el array original con el resultado combinado
        for (int index = 0; index < merged.size(); index++) {
            array.set(k++, merged.get(index));
            System.out.println(array);
        }
    }
    


    public static void imprimirListaDeDos(ArrayList<int[]> listaDeDos) {
        for (int[] array : listaDeDos) {
            System.out.print("(" + array[0] + ", " + array[1] + ") ");
        }
        System.out.println();
    }
    public static void imprimirArrayList(ArrayList<int[][]> lista) {
        for (int[][] par : lista) {
            System.out.print("[");
            for (int i = 0; i < par.length; i++) {
                System.out.print("(" + par[i][0] + ", " + par[i][1] + ")");
                if (i < par.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");
        }
    }
}
