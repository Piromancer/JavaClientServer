package com.company.util;

import com.company.Server.Command;
import com.company.Server.Message;

import java.text.ParseException;

public class JSONParser {
    private static final String COMA_RELACEMENT = "!!%!!";

    static public String toJson(Message msg){
        StringBuilder json = new StringBuilder();
        json.append("{{\"text\"=\"");
        msg.setText(msg.getText().replaceAll(",", COMA_RELACEMENT));
        json.append(msg.getText());
        json.append("\",\"owner\"=\"");
        json.append(msg.getOwner());
        json.append("\",\"command\"=\"");
        json.append(msg.getCommand());
        json.append("\",\"creationTime\"=\"");
        json.append(msg.getCreationTime());
        json.append("\"}}");
        return json.toString();
    }

    static public Message fromJson(String msg) throws ParseException {
        String[] fields = msg.split(",");
        if(fields.length != 4) throw new ParseException("Not a JSON", -1);
        fields[0] = fields[0].substring(2);
        fields[3] = fields[3].substring(0,fields[3].length()-2);
        Message result = new Message();
        for (String temp : fields){
            if (temp.startsWith("\"text\"")) {
                String cur_str = temp.substring(7).replaceAll(COMA_RELACEMENT, ",");
                cur_str = cur_str.substring(1, cur_str.length()-1);
                result.setText(cur_str);
            }
            if (temp.startsWith("\"owner\"")) {
                String cur_str = temp.substring(8).replaceAll(COMA_RELACEMENT, ",");
                cur_str = cur_str.substring(1, cur_str.length()-1);
                result.setOwner(cur_str);
            }
            if (temp.startsWith("\"creationTime\"")) {
                String cur_str = temp.substring(15).replaceAll(COMA_RELACEMENT, ",");
                cur_str = cur_str.substring(1, cur_str.length()-1);
                result.setCreationTime(cur_str);
            }
            if (temp.startsWith("\"command\"")) {
                String cur_str = temp.substring(10).replaceAll(COMA_RELACEMENT, ",");
                cur_str = cur_str.substring(1, cur_str.length()-1);
                switch (cur_str){
                    case "SHOW": {result.setCommand(Command.SHOW); break;}
                    case "DELETE": {result.setCommand(Command.DELETE); break;}
                    case "CHANGE_LOGIN": {result.setCommand(Command.CHANGE_LOGIN); break;}
                    case "QUIT": {result.setCommand(Command.QUIT); break;}
                    case "TRANSFER_FILE": {result.setCommand(Command.TRANSFER_FILE); break;}
                    case "MESSAGE": {result.setCommand(Command.MESSAGE); break;}
                }
            }
        }
        return result;
    }
}
