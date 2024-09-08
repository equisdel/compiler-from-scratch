package PrimerEtapa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Automata {
    
	private int estado_actual = 0;
    private int cant_estados = 0;
    private int matrizTransicion[][];


    public Automata(String path_csv) {
        int contador_filas = -3;	// offset de filas determinado por el formato de la matriz
		int contador_columnas = 1;	// offset de columnas determinado por el formato de la matriz
        try (BufferedReader br = new BufferedReader(new FileReader(path_csv))) {
			String line;
			line = br.readLine();
			for (int c = 0; c < line.length(); c++)
				if (line.charAt(c) == ',')
					contador_columnas++;
			contador_filas++;
			while ((line = br.readLine()) != null) {
				contador_filas++;
			}
			br.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(contador_filas + " - " + contador_columnas);
        this.cant_estados = contador_filas;
		this.matrizTransicion = new int[contador_filas][contador_columnas];
		//this.matrizAccionesSemanticas = new AccionSemantica[contador_filas][contador_columnas];
    }

    protected boolean getNext(String caracter) {
        int pos;
        boolean retorno = false;
        switch (caracter) {
            case "0" : pos = 3;
            case "[1-9]": pos = 4;
            case "x": pos = 5;
            case "s": pos = 6;
            case "[abcdeghijklmnopqrtuvwyzABCDEGHIJKLMNOPQRTUVWYZ]": pos=7;
            case "[a-fA-F]": pos = 8;
            case "_": pos = 9;
            case "#" : pos = 10;
            case "[": pos = 11;
            case "]": pos = 12;
            case "+": pos = 13;
            case "-": pos = 14;
            case ".": pos = 15;
            case "=": pos = 16;
            case ":": pos = 17;
            case "!": pos = 18;
            case "<": pos = 19;
            case ">": pos = 20;
            case "*": pos = 21;
            case "(": pos = 22;
            case ")": pos = 23;
            case ",": pos = 24;
            case ";": pos = 25;
            default: { pos = 26; retorno = true; }
        }
        estado_actual = matrizTransicion[estado_actual][pos];
        if (estado_actual == cant_estados) {
            retorno = true;
        }
        return retorno;
    }

}