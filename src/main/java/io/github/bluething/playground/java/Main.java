package io.github.bluething.playground.java;

import java.util.Arrays;
import java.util.Optional;

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

        TaskManager taskManager = new TaskManager();
        String cmd = args[0].toLowerCase();
        try {
            switch (cmd) {
                case "add":
                    if (args.length < 2) {
                        printUsage();
                        return;
                    }
                    String desc = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                    taskManager.addTask(desc);
                    break;
                case "update":
                    if (args.length < 3) {
                        printUsage();
                        return;
                    }
                    int uid = Integer.parseInt(args[1]);
                    String newDesc = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                    taskManager.updateTask(uid, newDesc);
                    break;
                case "delete":
                    if (args.length < 2) {
                        printUsage();
                        return;
                    }
                    taskManager.deleteTask(Integer.parseInt(args[1]));
                    break;
                case "mark":
                    if (args.length < 3) {
                        printUsage();
                        return;
                    }
                    int mid = Integer.parseInt(args[1]);
                    String state = args[2].toLowerCase();
                    Status st;
                    switch (state) {
                        case "todo":
                            st = Status.TODO;
                            break;
                        case "inprogress":
                            st = Status.IN_PROGRESS;
                            break;
                        case "done":
                            st = Status.DONE;
                            break;
                        default:
                            printUsage();
                            return;
                    }
                    taskManager.markTask(mid, st);
                    break;
                case "list":
                    Optional<Status> filter = getStatus(args);
                    taskManager.listTasks(filter);
                    break;
                default:
                    printUsage();
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid task id. It must be a number.");
        }
    }

    private static Optional<Status> getStatus(String[] args) {
        Optional<Status> filter = Optional.empty();
        if (args.length == 2) {
            filter = switch (args[1].toLowerCase()) {
                case "todo" -> Optional.of(Status.TODO);
                case "inprogress" -> Optional.of(Status.IN_PROGRESS);
                case "done" -> Optional.of(Status.DONE);
                default -> Optional.empty();
            };
        }
        return filter;
    }
}