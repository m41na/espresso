package works.hop.presso.game.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question implements Serializable {

    @NotNull
    String content;
    @NotNull
    @NotEmpty
    QChoice[] choices;
    QClue[] clues;
    @NotNull
    String postedBy;
}
