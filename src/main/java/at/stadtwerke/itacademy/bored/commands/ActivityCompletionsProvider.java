package at.stadtwerke.itacademy.bored.commands;

import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProvider;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ActivityCompletionsProvider implements ValueProvider {

    @Override
    public List<CompletionProposal> complete(CompletionContext completionContext) {
        return Stream.of("education", "recreational", "social",
                        "diy","charity", "cooking", "relaxation",
                        "music", "busywork")
                .map(CompletionProposal::new)
                .collect(Collectors.toList());
    }
}
