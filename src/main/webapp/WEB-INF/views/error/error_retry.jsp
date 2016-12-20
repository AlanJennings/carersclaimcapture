<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

@isSaveEnabled=@{
    getBooleanProperty("saveForLaterSaveEnabled")
}
@saveHref=@{
    getStringProperty("saveForLaterUrl")
}
@contactDetails = @{
    claim.questionGroup[ContactDetails].getOrElse(ContactDetails())
}
@showSaveButton = @{
    contactDetails.wantsContactEmail match {
        case yesNo if yesNo == yes && isSaveEnabled => true
        case _ => false
    }
}
@claimSent = @{
    if (claimType(claim) == FULL_CLAIM) true
    else false
}
<t:mainPage pageTitle="error.retry">
    <div class="carers-allowance clearfix">
        <div class="grid-row main-grid">
            <article class="column-three-quarters main-content error-statement">
                <h1 class="form-title heading-large"><t:message code="error.retry" /></h1>
                <p>
                    @if(claimSent) {
                        <t:message code="error.application.not.sent" /> <br/>
                    } else {
                        <t:message code="error.changes.not.sent" /> <br/>
                    }
                    @if(showSaveButton) {
                        <a href="@url" id="Try" class="secondary" aria-label="<t:message code="try.claim.help" />" ><t:message code="error.send.it.again" /></a><br/>
                    }
                    <t:message code="error.second.time" /> <br/>
                    @if(showSaveButton) {
                        <t:message code="error.save.for.later" /> <br/>
                    } else {
                        <t:message code="error.come.back.later" /> <br/>
                    }
                </p>
                <nav class="form-steps">
                    <ul>
                        @if(showSaveButton) {
                            @form(action = controllers.save_for_later.routes.GSaveForLater.submit, 'role -> "form") {
                                @DwpCSRFFormHelper.formField
                                <li><button type="submit" id="save" name="action" class="button"><t:message code="form.save" /></button><li>
                            }
                        } else {
                            <li><button id="Try2" type="button" name="action" value="next" class="button" @views.html.ga.trackClickEvent(linkName = "Chose try again") onClick="location.href='@url'" aria-label="<t:message code="try.claim.help" />" ><t:message code="try.claim" /></button></li>
                        }
                        <li><t:message code="error.gov.link" /></li>
                    </ul>
                </nav>
            </article>
        </div>
    </div>
</t:mainPage>
