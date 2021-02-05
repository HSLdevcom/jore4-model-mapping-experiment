package fi.hsl.transmodel;


import fi.hsl.transmodel.model.jore.JoreImportContext;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class MockJoreContextBuilderTest {

    @Test
    public void canBuild() {
        final JoreImportContext ctx = MockJoreContextBuilder.ctx();

        assertThat(ctx,
                   is(notNullValue()));
    }
}
