package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;


class Parser {
    String commandName;
    String[] args;
    public boolean parse(String input) {
        String[] newArgs = input.split(" ");
        commandName = newArgs[0];
        for (int i = 1; i < newArgs.length; i++) {
            args[i - 1] = newArgs[i];
        }

        switch (commandName) {
            case "echo":
            case "cat":
            case "rmdir":
            case "touch":
            case "rm":
                if (args.length == 1) {
                    return true;
                }
                break;
            case "pwd":
            case "ls":
            case "ls_r":
                if (args.length == 0) {
                    return true;
                }
                break;
            case "cd":
                if (args.length == 0 || args.length == 1) {
                    return true;
                }
                break;
            case "mkdir":
                if (args.length != 0) {
                    return true;
                }
                break;
            case "cp":
            case "cp_r":
                if (args.length == 2) {
                    return true;
                }
                break;
        }
        System.out.println("wrong command!");
        return false;
    }
    public String getCommandName() {
        return commandName;
    }
    public String[] getArgs() {
        return args;
    }
}


class Terminal {
    File path = new File(System.getProperty("user.dir"));

    public static void main(String[] args) throws Exception {
        getCommandAction();
    }

    // cd without arg
    public void cd(){
        path = new File(System.getProperty("user.home"));
        System.out.println(path);
    }

    // echo
    public void echo(String input) {
        System.out.println(input);
    }

    // pwd
    public void pwd() {
        System.out.println(System.getProperty("user.dir"));
    }

    // ls
    public void ls() {
        String[] names;
        File dir = new File(System.getProperty("user.dir"));
        names = dir.list();
        for (String name : names) {
            System.out.println(name);
        }
    }

    // ls-r
    public void lsR() {
        String[] names;
        File dir = new File(System.getProperty("user.dir"));
        names = dir.list();
        for (int i = names.length - 1; i >= 0; i--) {
            System.out.println(names[i]);
        }
    }

    // mkdir
    public void mkdir(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Enter the directory name");
        } else {
            for (int i = 0; i < args.length; i++) {
                if (!(args[i].contains("/"))) {
                    try {
                        Path path = Paths.get(args[i]);
                        Files.createDirectories(path);
                        System.out.println("Directory is created!");
                    } catch (IOException e) {
                        System.err.println("Failed to create directory!" + e.getMessage());
                    }
                } else if (args[i].contains("/")) {
                    Path path = Paths.get(args[i]);
                    // File file = new File(args[i]);
                    Files.createDirectories(path);
                    System.out.println("Directory is created!");
                }
            }
        }
    }

    // rmdir
    public void rmdir(String dir) {
        boolean check = false;
        try {
            if (dir == "*") {
                File directory = new File(dir);
                check = directory.delete();
            } else if(dir.contains(":\\")){
                File file = new File(dir);
                check = file.delete();
            }else{
                File file = new File(dir);
                check = file.delete();
            }
            if (check) {
                System.out.println("Directory Deleted");
            } else {
                System.out.println("Directory can't be deleted because it is not empty");
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    // cat
    public void cat(String[] args) throws IOException {
        if (args.length == 1) {
            File f = new File(args[0]);
            if (f.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(args[0]));
                String line = br.readLine();
                while (line != null) {
                    System.out.println(line);
                    line = br.readLine();
                }
                br.close();
            } else {
                System.out.println("File not found");
            }
        } else {
            for (int i = 0; i < args.length; i++) {
                File f = new File(args[i]);
                if (f.exists()) {
                    BufferedReader br = new BufferedReader(new FileReader(args[i]));
                    String line = br.readLine();
                    while (line != null) {
                        System.out.println(line);
                        line = br.readLine();
                    }
                    br.close();
                } else {
                    System.out.println("File doesn't exist");
                }
            }
        }
    }

    // cd with argument
    public void cd(String arg) {
        if(arg==".."){
            path = new File(path.getParent());
        }
        else {
            String currentDirectory = path.getPath()+"\\"+arg;
            File fileArg = new File(currentDirectory);
            if(fileArg.exists() && fileArg.isDirectory()){
                path = new File(currentDirectory);
            }
            else {
                System.out.println("Unknown Directory");
            }
        }
        System.out.println(path);
    }

    // touch
    public void touch(String path){
        try {
            File file = new File(path);
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("Exception Occurred:");
            e.printStackTrace();
        }
    }

    // cp
    public void cp(String aa, String bb) throws Exception {
        File a = new File(aa);
        File b = new File(bb);

        try (FileInputStream in = new FileInputStream(a); FileOutputStream out = new FileOutputStream(b)) {
            int n;
            while ((n = in.read()) != -1) {
                out.write(n);
            }
        }
        System.out.println("File Copied");
    }

    // cp-r
    public void cp_r(String directory1, String directory2) {
        try {
            Files.walk(Paths.get(directory1)).forEach(firstDir -> {
                Path secondDir = Paths.get(directory2, firstDir.toString().substring(directory1.length()));
                try {
                    if (!secondDir.toFile().exists()) {
                        Files.copy(firstDir, secondDir);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // rm
    public void rm(String file_name) {
        try {
            Files.deleteIfExists(Paths.get(String.valueOf(path),file_name));
            System.out.println("File deleted successfully");
        }
        catch (IOException e) {
            System.out.println("file not exist");
        }
    }

    // get command action
    public static void getCommandAction() throws Exception {
        Terminal terminal = new Terminal();
        Parser parser = new Parser();

        Scanner input = new Scanner(System.in);
        String userInput;
        String Command;
        String[] Argument;
        while (true) {
            System.out.print("$");
            userInput = input.nextLine();

            while (parser.parse(userInput)) {

                Command = parser.getCommandName();
                Argument = parser.getArgs();

                switch (Command) {
                    case "cd":
                        terminal.cd(Argument[0]);
                        break;
                    case "touch":
                        terminal.touch(Argument.toString());

                    case "ls":
                        terminal.ls();
                        break;

                    case "cat":
                        try {
                            terminal.cat(Argument);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "rm":
                        terminal.rm(Argument.toString());
                        break;

                    case "pwd":
                        terminal.pwd();
                        break;

                    case "echo":
                        terminal.echo(Argument.toString());
                        break;

                    case "cp":
                        terminal.cp(Argument[0], Argument[1]);
                        break;

                    case "mkdir":
                        terminal.mkdir(Argument);
                        break;

                    case "rmdir":
                        terminal.rmdir(Argument.toString());
                        break;

                }
            }
        }
    }
}