package works.hop.presso.demo.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QChoice implements Serializable {

    int ordinal;
    @NotNull String choice;
    @NotNull Boolean correct;
}
