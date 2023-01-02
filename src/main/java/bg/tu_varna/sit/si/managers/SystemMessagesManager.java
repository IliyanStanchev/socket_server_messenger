package bg.tu_varna.sit.si.managers;

import bg.tu_varna.sit.si.models.ChatMessage;
import bg.tu_varna.sit.si.models.User;
import bg.tu_varna.sit.si.services.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.lang.Math.random;


public class SystemMessagesManager {

    static final String DEFAULT_MESSAGE = "I can't answer that. Try out questions like: What day is today, What is the time, What is the weather, What is the temperature";
    private enum Weather {
        COLD,
        WARM,
        SUNNY,
        CLOUDY,
        RAINY,
        SNOWY
    }
    private enum SystemMessages{

        WHAT_DAY_IS_TODAY("What day is today")
        , WHATS_THE_TIME("What is the time")
        , WHATS_THE_WEATHER("What is the weather")
        , WHATS_THE_TEMPERATURE("What is the temperature");

        SystemMessages(String question) {
            this.question = question;
        }

        private String question;
    };
    public static ChatMessage getSystemMessage(ChatMessage message) {

        ChatMessage systemMessage = new ChatMessage();
        systemMessage.setContent(DEFAULT_MESSAGE);

        for( SystemMessages systemMessages : SystemMessages.values()){
            if(message.getContent().contains(systemMessages.question)){

                String answer = answer(systemMessages);
                systemMessage.setContent(answer);
            }
        }

        systemMessage.setCreationDate(LocalDateTime.now());
        User systemUser = UserService.getSystemUser();
        systemMessage.setSender(systemUser);
        systemMessage.setChat(message.getChat());

        return systemMessage;
    }

    private static String answer(SystemMessages systemMessages) {

        switch (systemMessages){
            case WHAT_DAY_IS_TODAY:
                return "Today is " + LocalDate.now();
            case WHATS_THE_TIME:
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                return "The time is " + dateTimeFormatter.format(now);
            case WHATS_THE_WEATHER:
                return "The weather is " + Weather.values()[(int) (random() * Weather.values().length)];
            case WHATS_THE_TEMPERATURE:
                final int min = -15;
                final int max = 40;
                int temperature = (int) (random() * (max - min + 1) + min);
                return "The temperature is " + temperature + " degrees";
        }

        return null;
    }
}
