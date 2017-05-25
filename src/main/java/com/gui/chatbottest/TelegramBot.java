package com.gui.chatbottest;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Guilherme
 */
public final class TelegramBot {

    private final String endpoint = "https://api.telegram.org/";
    private final String token;

    public TelegramBot(String token) {
        this.token = token;
    }

    public HttpResponse<JsonNode> sendMessage(Integer chatId, String text) throws UnirestException {
        return Unirest.post(endpoint + "bot" + token + "/sendMessage")
                .field("chat_id", chatId)
                .field("text", text)
                .asJson();
    }

    public HttpResponse<JsonNode> getUpdates(Integer offset) throws UnirestException {
        return Unirest.post(endpoint + "bot" + token + "/getUpdates")
                .field("offset", offset)
                .asJson();
    }

    public void run() throws UnirestException {

        int init_id = 0;
        int last_update_id = 0; // controle das mensagens processadas
        HttpResponse<JsonNode> response;

        while (true) {

            response = getUpdates(last_update_id++);

            if (response.getStatus() == 200) {

                JSONArray responses = response.getBody().getObject().getJSONArray("result");

                if (responses.isNull(0)) {
                    continue;
                } else {
                    last_update_id = responses
                            .getJSONObject(responses.length() - 1)
                            .getInt("update_id") + 1;
                }

                for (int i = 0; i < responses.length(); i++) {

                    JSONObject message = responses.getJSONObject(i).getJSONObject("message");

                    String from = message.getJSONObject("from").getString("first_name");
                    System.out.println(from);
                    String from_last = message.getJSONObject("from").getString("last_name");
                    System.out.println(from_last);
                    int from_id = message.getJSONObject("from").getInt("id");
                    System.out.println(from_id);
                    
                    String chat_first = message.getJSONObject("chat").getString("first_name");
                    System.out.println(chat_first);
                    String chat_last = message.getJSONObject("chat").getString("last_name");
                    System.out.println(chat_last);
                    int chat_id = message.getJSONObject("chat").getInt("id");
                    System.out.println(chat_id);
                    
                    //String usuario = message.getJSONObject("chat").getString("username");
                    String texto = message.getString("text");

                    int count = last_update_id - init_id;
 
                   if (count == 1) {
                        String reply = "Olá " + texto + "\n"
                                + "Você possui as seguintes opções:\n"
                                + "/toupper {texto} - texto em maiúsculo\n"
                                + "/tolower {texto} - texto em minúsculo\n"
                                + "/invert {texto} - inverte o texto";

                        sendMessage(chat_id, reply);
                    }

                    if (texto.contains("/start")) {

                        String reply = "Oi, esse é o bot de teste do Gui.\n"
                                + "Seu chat_id é " + chat_id + "\n"
                                + "Digite seu nome para iniciar.";
                        //+ "Seu username é " + usuario;

                        sendMessage(chat_id, reply);

                        init_id = last_update_id;

                    } else if (texto.contains("/toupper")) {

                        String param = texto.substring("/toupper".length(), texto.length());
                        sendMessage(chat_id, param.toUpperCase());

                    } else if (texto.contains("/tolower")) {

                        String param = texto.substring("/tolower".length(), texto.length());
                        sendMessage(chat_id, param.toLowerCase());

                    } else if (texto.contains("/invert")) {

                        String param = texto.substring("/invert".length(), texto.length());
                        String textoInvertido = "";

                        for (int j = param.length() - 1; j >= 0; j--) {
                            textoInvertido += param.charAt(j);
                        }

                        sendMessage(chat_id, textoInvertido);
                    } else if (count != 1) {
                        String reply = "Comando inválido";
                        sendMessage(chat_id, reply);
                    }
                }
            }
        }
    }
}
