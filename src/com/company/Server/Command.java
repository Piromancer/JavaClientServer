package com.company.Server;

public enum Command {
    DELETE,
    SHOW,
    CHANGE_LOGIN,
    QUIT,
    MESSAGE,
    TRANSFER_FILE,
    HELP,
    SHOW_ALL_NAME,
    SHOW_ALL_DATE;

    public static Command fromString(String str){
        switch (str){
            case "SHOW": return Command.SHOW;
            case "DELETE": return Command.DELETE;
            case "CHANGE_LOGIN": return Command.CHANGE_LOGIN;
            case "QUIT": return Command.QUIT;
            case "TRANSFER_FILE": return Command.TRANSFER_FILE;
            case "HELP": return Command.HELP;
            case "SHOW_ALL_NAME": return Command.SHOW_ALL_NAME;
            case "SHOW_ALL_DATE": return Command.SHOW_ALL_DATE;
            default: return Command.MESSAGE;
        }
    }
}
