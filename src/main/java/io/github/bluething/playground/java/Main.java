package io.github.bluething.playground.java;

public class Main {
    private static void printUsage() {
        var usage = """
                Usage:
                add <description>                   Add new task
                update <id> <description>           Update task description
                delete <id>                         Delete task description
                mark <id> <todo|in-progress|done>    Change task status
                list [all|todo|in-progress|done]     List tasks
                """;
        System.out.println(usage);
    }
    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }
    }
}