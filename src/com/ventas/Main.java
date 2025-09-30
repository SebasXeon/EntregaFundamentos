package com.ventas;

import java.io.*;
import java.util.*;

public class Main {
    
    static class Product {
        String id;
        String name;
        double price;
        int totalSold = 0;

        public Product(String id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }
    }

    static class Salesman {
        String docType;
        String docNumber;
        String firstName;
        String lastName;
        double totalSales = 0.0;

        public Salesman(String docType, String docNumber, String firstName, String lastName) {
            this.docType = docType;
            this.docNumber = docNumber;
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String getFullName() {
            return firstName + " " + lastName;
        }
    }
    
    
    // Main method
    public static void main(String[] args) {
        try {
            Map<String, Product> products = loadProducts("data/productos.txt");

            Map<String, Salesman> salesmen = loadSalesmen("data/vendedores.txt");

            processSales("data", products, salesmen);

            generateSalesmenReport(salesmen, "reporte_vendedores.csv");

            generateProductsReport(products, "reporte_productos.csv");

            System.out.println("Reportes generados con exito OK");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Load products
    public static Map<String, Product> loadProducts(String path) throws IOException {
        Map<String, Product> products = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(";");
            if (parts.length == 3) {
                String id = parts[0];
                String name = parts[1];
                double price = Double.parseDouble(parts[2]);
                products.put(id, new Product(id, name, price));
            }
        }
        reader.close();
        return products;
    }

    // Load salesmen
    public static Map<String, Salesman> loadSalesmen(String path) throws IOException {
        Map<String, Salesman> salesmen = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(";");
            if (parts.length == 4) {
                String docType = parts[0];
                String docNumber = parts[1];
                String firstName = parts[2];
                String lastName = parts[3];
                salesmen.put(docNumber, new Salesman(docType, docNumber, firstName, lastName));
            }
        }
        reader.close();
        return salesmen;
    }

    // Process sales files
    public static void processSales(String folderPath, Map<String, Product> products, Map<String, Salesman> salesmen) throws IOException {
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.startsWith("ventas_"));

        if (files == null) return;

        for (File file : files) {
            String fileName = file.getName();
            String[] partsName = fileName.split("_");
            String salesmanId = partsName[partsName.length - 1].replace(".txt", "");

            Salesman salesman = salesmen.get(salesmanId);
            if (salesman == null) {
                System.out.println("No se encontró vendedor con ID: " + salesmanId);
                continue;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    String productId = parts[0];
                    int quantity = Integer.parseInt(parts[1]);

                    Product p = products.get(productId);
                    if (p != null) {
                        double total = quantity * p.price;
                        salesman.totalSales += total;
                        p.totalSold += quantity; // track sold quantity for report
                    }
                }
            }
            reader.close();
        }
    }

    // Generate salesmen report
    public static void generateSalesmenReport(Map<String, Salesman> salesmen, String outputFile) throws IOException {
        List<Salesman> list = new ArrayList<>(salesmen.values());
        list.sort((a, b) -> Double.compare(b.totalSales, a.totalSales));

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        for (Salesman s : list) {
            writer.write(s.getFullName() + ";" + s.totalSales);
            writer.newLine();
        }
        writer.close();
    }

    // Generate products report
    public static void generateProductsReport(Map<String, Product> products, String outputFile) throws IOException {
        List<Product> list = new ArrayList<>(products.values());
        list.sort((a, b) -> Integer.compare(b.totalSold, a.totalSold));

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        for (Product p : list) {
            writer.write(p.name + ";" + p.price + ";" + p.totalSold);
            writer.newLine();
        }
        writer.close();
    }
}
