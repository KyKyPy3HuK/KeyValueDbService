package com.punk_pozer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class Main {

    static boolean exit = false;
    static Scanner scanner = new Scanner(System.in);
    static KVDSConnection connection;

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
		
		// Менять адрес сервиса в конструкторе ниже
        connection = new KVDSConnection("http://localhost:8080");

        while(!exit){
            consoleInfo();
            action();
        }
    }

    public static void consoleInfo(){
        System.out.println("1 - check connection");
        System.out.println("2 - set or update");
        System.out.println("3 - get");
        System.out.println("4 - remove");
        System.out.println("5 - save dump");
        System.out.println("5s - save dump soft");
        System.out.println("6 - load dump");
        System.out.println("q - quit");
    }

    public static void action() throws IOException, InterruptedException, URISyntaxException {
        String input = scanner.next();
        input = input.toLowerCase();
        input = input.trim();
        switch (input){
            case "1":{
                System.out.println(connection.checkConnection().body());
                break;
            }
            case "2":{
                System.out.println("enter key:");
                String key = scanner.next();
                System.out.println("enter value:");
                String value = scanner.next();
                System.out.println("enter ttl, or -1 to infinite");
                long ttl = scanner.nextLong();
                if(ttl == -1){
                    System.out.println(connection.setOrUpdate(key, value).body());
                }
                else{
                    System.out.println(connection.setOrUpdate(key, value, ttl).body());
                }
                break;
            }
            case "3":{
                System.out.println("enter key:");
                String key = scanner.next();
                System.out.println(connection.getValueByKey(key));
                break;
            }
            case "4":{
                System.out.println("enter key:");
                String key = scanner.next();
                System.out.println(connection.remove(key).body());
                break;
            }
            case "5":{
                System.out.println(connection.saveDump().body());
                break;
            }
            case "5s":{
                System.out.println(connection.saveDumpSoft().body());
                break;
            }
            case "6":{
                System.out.println(connection.loadDump().body());
                break;
            }
            case "q":
                exit = true;
                System.out.println("bye");
                break;
            default:
                System.out.println("invalid input, try again");
                break;
        }
    }
}