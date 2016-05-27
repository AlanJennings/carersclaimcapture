<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<t:mainPage>
    <t:pageContent pageTitle="Can you get Carer's Allowance?" backLink="${previousPage}">
        <t:yesnofield id="over35HoursAWeek" 
                      name="over35HoursAWeek" 
                      value="${over35HoursAWeek}"
                      label="Do you spend 35 hours or more each week caring for the person you care for?" 
                      hintBefore='<p class="form-hint">For example, cooking meals, or helping them with their shopping.</p>'
                      warningText='
                          <div id="warningover35HoursAWeek_answer" class="validation-summary" style="display: none;">
                              <p>You can&rsquo;t get Carer&rsquo;s Allowance if you care for someone less than 35 hours a week. You might still be entitled to 
                                  <a rel="external" href="https://gov.uk/carers-credit" target="_blank"> Carer&rsquo;s Credit</a>.
                              </p>                
                          </div>'
                      hasError="${errors.hasError('over35HoursAWeek')}" 
                      errorMessage="${errors.getErrorMessage('over35HoursAWeek')}" 
        />

        <t:yesnofield id="over16YearsOld" 
                      name="over16YearsOld" 
                      value="${over16YearsOld}"
                      label="Are you aged 16 or over?" 
                      warningText='
                          <div id="warningover16_answer" class="validation-summary" style="display: none;">                
                              <p>You can only get Carer&rsquo;s Allowance if you&rsquo;re 16 or over.</p>                
                          </div>'
                      hasError="${errors.hasError('over16YearsOld')}" 
                      errorMessage="${errors.getErrorMessage('over16YearsOld')}" 
        />

        <t:radiobuttons id="originCountry" 
                        name="originCountry" 
                        optionIds="GB|NI|OTHER"
                        optionValues="England, Scotland or Wales|Northern Ireland|Another country"
                        value="${originCountry}"
                        label="Which country do you live in?" 
                        warningText='
                          <div id="warningOriginCountry_answer" class="prompt breaks-prompt validation-summary">
                              <p>
                                  You can normally get Carer&rsquo;s Allowance if you live in England, Scotland or Wales. 
                                  You might get it if you live in the EEA (European Economic Area) or Switzerland, but you 
                                  must apply to find out.
                              </p>
                          </div>'
                        hasError="${errors.hasError('originCountry')}" 
                        errorMessage="${errors.getErrorMessage('originCountry')}" 
        />
    </t:pageContent>
    
    <script type="text/javascript" src="<c:url value='/assets/javascript/s_eligibility/answerNoMessage.js' />" ></script>
        <script type="text/javascript">
            $(function () {
                // window.initEvents(id, answer_yes, answer_no)
                window.initEvents("warningover35HoursAWeek_answer","over35HoursAWeek_yes", "over35HoursAWeek_no");
                window.initEvents("warningover16_answer","over16YearsOld_yes", "over16YearsOld_no");
                window.originWarning("originCountry_GB", "originCountry_NI", "originCountry_OTHER", "warningOriginCountry_answer");
                window.gaEvents("origin_GB", "origin_NI", "origin_OTHER");
            
                // trigger a ga event when errors are shown (not currently working)
                var hoursId = 'hours_answer';
                var res = $('#'+hoursId).parents('li.form-group').find('.validation-summary a').click(
                    function(){
                        trackEvent('allowance/eligibility','carers-credit','clicked');
                    });
            });
        </script>
</t:mainPage>