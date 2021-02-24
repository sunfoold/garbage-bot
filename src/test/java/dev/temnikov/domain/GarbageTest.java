package dev.temnikov.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import dev.temnikov.web.rest.TestUtil;

public class GarbageTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Garbage.class);
        Garbage garbage1 = new Garbage();
        garbage1.setId(1L);
        Garbage garbage2 = new Garbage();
        garbage2.setId(garbage1.getId());
        assertThat(garbage1).isEqualTo(garbage2);
        garbage2.setId(2L);
        assertThat(garbage1).isNotEqualTo(garbage2);
        garbage1.setId(null);
        assertThat(garbage1).isNotEqualTo(garbage2);
    }
}
