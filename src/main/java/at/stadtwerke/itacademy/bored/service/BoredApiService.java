package at.stadtwerke.itacademy.bored.service;

import at.stadtwerke.itacademy.bored.client.BoredApiClient;
import at.stadtwerke.itacademy.bored.model.Activity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BoredApiService {

    private final BoredApiClient boredApiClient;

    public BoredApiService(BoredApiClient boredApiClient) {
        this.boredApiClient = boredApiClient;
    }

    public String getActivityBlock() {
        Activity activity = boredApiClient.getSimpleActivity().block();
        String activityText = "Activity: " + activity.getActivity();
        String linkText = "Link: " + activity.getLink();
        String typeText = "Type: " + activity.getType();
        String participantsText = "Participants: " + activity.getParticipants();
        String accessibilityText = "Accessibility: " + activity.getAccessibility();
        String priceText = "Price: " + activity.getPrice();
        int maxLength = getMaxStringLength(activityText, linkText, typeText,
                                           participantsText, accessibilityText, priceText);
        StringBuilder activityBlock = new StringBuilder("/-" + "-".repeat(maxLength) + "-\\" + "\n");
        addLine(activityBlock, maxLength, "| ", activityText, " |");
        addLine(activityBlock, maxLength, "| ", typeText, " |");
        if (!Objects.equals(activity.getLink(), "")) {
            addLine(activityBlock, maxLength, "| ", linkText, " |");
        }
        addLine(activityBlock, maxLength, "| ", participantsText, " |");
        addLine(activityBlock, maxLength, "| ", accessibilityText, " |");
        addLine(activityBlock, maxLength, "| ", priceText, " |");
        addLine(activityBlock, maxLength, "\\-", "-".repeat(maxLength), "-/");
        return activityBlock.toString();
    }

    private int getMaxStringLength(String... strings) {
        int maxLength = 0;
        for (String str : strings) {
            maxLength = Math.max(maxLength, str.length());
        }
        return maxLength;
    }

    private void addLine(StringBuilder builder, int maxLength, String start, String middle, String end) {
        builder.append(start)
                .append(middle)
                .append(" ".repeat(maxLength - middle.length()))
                .append(end)
                .append("\n");
    }

    public String getActivityText() {
        Activity activity = boredApiClient.getSimpleActivity().block();
        return "Activity Idea: " + activity.getActivity();
    }

}
