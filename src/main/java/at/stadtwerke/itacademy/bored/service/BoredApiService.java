package at.stadtwerke.itacademy.bored.service;

import at.stadtwerke.itacademy.bored.client.BoredApiClient;
import at.stadtwerke.itacademy.bored.model.Activity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Objects;
import java.util.Optional;

@Service
public class BoredApiService {

    private final BoredApiClient boredApiClient;

    public BoredApiService(BoredApiClient boredApiClient) {
        this.boredApiClient = boredApiClient;
    }

    public String getActivity() {
        try {
            Optional<Activity> activity = boredApiClient.getSimpleActivity().blockOptional();
            return activity.map(this::createActivityBlock).orElse("Error: BoredApi didn't send an activity.");
        } catch (WebClientResponseException error) {
            return "Web Client Error: Try again later. - " + error.getStatusCode();
        }
    }

    public String getActivity(String type) {
        try {
            Optional<Activity> activity = boredApiClient.getActivityByType(type).blockOptional();
            return activity.map(this::createActivityBlock).orElse("Error: BoredApi didn't send an activity.");
        } catch (WebClientResponseException error) {
            return "Web Client Error: Try again later. - " + error.getStatusCode();
        }
    }

    private String createActivityBlock(Activity activity) {
        if (activity.getActivity() == null || activity.getActivity().equals("")) {
            return "Error: The activity String is empty.";
        }
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
        if (!Objects.equals(activity.getLink(), "") && !Objects.equals(activity.getLink(), null)) {
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
        try {
            Optional<Activity> activity = boredApiClient.getSimpleActivity().blockOptional();
            if (activity.isPresent()) {
                if (activity.get().getActivity() == null || activity.get().getActivity().equals("")) {
                    return "Error: The activity String is empty.";
                }
                return "Activity Idea: " + activity.get().getActivity();
            }
            else {
                return "Error: BoredApi didn't send an activity.";
            }
        } catch (WebClientResponseException error) {
            return "Web Client Error: Try again later. - " + error.getStatusCode();
        }
    }

}
