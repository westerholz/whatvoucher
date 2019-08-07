package com.whatvoucher.about;

import org.junit.Assert;
import org.junit.Test;

import com.vaadin.flow.component.html.testbench.SpanElement;
import com.whatvoucher.AbstractViewTest;
import com.whatvoucher.MainLayoutElement;
import com.whatvoucher.authentication.LoginFormElement;

public class AboutViewIT extends AbstractViewTest {

    @Test
    public void openAboutView_showsFlowVersion() {
        // given authenticated as a regular user
        $(LoginFormElement.class).first().login("user", "user");

        // when selecting "About" from the sidebar menu
        final MainLayoutElement mainElem = $(MainLayoutElement.class).first();
        mainElem.clickMenuLink("About");

        // then the view contents a span with Flow version information
        final SpanElement aboutSpan = mainElem.$(SpanElement.class).last();

        Assert.assertTrue("Expected link to admin view",
                aboutSpan.getText().contains("Flow"));
    }
}
