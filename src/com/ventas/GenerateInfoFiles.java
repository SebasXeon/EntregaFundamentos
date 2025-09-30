package com.ventas;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GenerateInfoFiles {

    static class Salesman {
        String tipoDoc;
        long numeroDoc;
        String nombre;
        String apellido;

        public Salesman(String tipoDoc, long numeroDoc, String nombre, String apellido) {
            this.tipoDoc = tipoDoc;
            this.numeroDoc = numeroDoc;
            this.nombre = nombre;
            this.apellido = apellido;
        }
    }

    public static void main(String[] args) {
        try {
            createProductsFile(10);

            List<Salesman> vendedores = createSalesManInfoFile(5);

            for (Salesman v : vendedores) {
                createSalesMenFile(20, v.nombre, v.numeroDoc);
            }

            System.out.println("Archivos generados correctamente OK");

        } catch (Exception e) {
            System.out.println("Error al generar archivos: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static void createProductsFile(int productsCount) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("data/productos.txt"));
        Random random = new Random();

        for (int i = 1; i <= productsCount; i++) {
            String id = "P" + i;
            String name = "Producto" + i;
            double price = 1000 + random.nextInt(9000);
            writer.write(id + ";" + name + ";" + price);
            writer.newLine();
        }
        writer.close();
    }


    public static List<Salesman> createSalesManInfoFile(int salesmanCount) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("data/vendedores.txt"));
        Random random = new Random();

        String[] nombres = {"Juan", "Maria", "Pedro", "Ana", "Luis", "Tatiana"};
        String[] apellidos = {"Gomez", "Lopez", "Perez", "Rodriguez", "Martinez"};

        List<Salesman> vendedores = new ArrayList<>();

        for (int i = 1; i <= salesmanCount; i++) {
            String tipoDoc = "CC";
            long numeroDoc = 10000000 + random.nextInt(90000000);
            String nombre = nombres[random.nextInt(nombres.length)];
            String apellido = apellidos[random.nextInt(apellidos.length)];

            writer.write(tipoDoc + ";" + numeroDoc + ";" + nombre + ";" + apellido);
            writer.newLine();

            vendedores.add(new Salesman(tipoDoc, numeroDoc, nombre, apellido));
        }
        writer.close();

        return vendedores;
    }


    public static void createSalesMenFile(int randomSalesCount, String name, long numeroDoc) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("data/ventas_" + name + "_" + numeroDoc + ".txt"));
        Random random = new Random();

        for (int i = 1; i <= randomSalesCount; i++) {
            String productId = "P" + (1 + random.nextInt(10));
            int cantidad = 1 + random.nextInt(5);
            writer.write(productId + ";" + cantidad);
            writer.newLine();
        }
        writer.close();
    }
}
