<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="t"  tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="cads" uri="http://uk.gov.dwp.carersallowance/functions" %> 

<!DOCTYPE html>
<t:mainPage>
    <t:pageContent errors="${validationErrors}" backLink="${previousPage}">
        <c:if test="${not empty breakshospital || not empty breaksrepite || not empty breaksother}">
            <t:panel id="breaks">
                <h2 class="heading-medium" id="summary-heading"><t:message code="breaks.summary.title" /></h2>
                <table id="summary-table">
                    <tr>
                        <th><t:message code="breaks.summary.who" /></th>
                        <th><t:message code="breaks.summary.where" /></th>
                        <th><t:message code="breaks.summary.from" /></th>
                        <th><t:message code="breaks.summary.to" /></th>
                        <th></th>
                    </tr>
                    
                    <c:forEach items="${breakshospital}" var="careBreak">
                        <tr id="break_${careBreak['breakshospital_id']}" class="data-table">
                            <p>${hospitalBreakWhoInHospital}</p>
                            <c:if test="${careBreak['hospitalBreakWhoInHospital']=='Carer'}">
                                <td><t:message code="breaks.summary.you" /></td>
                                <td><t:message code="breaks_hospital.text" /></td>
                                <td>
                                    ${cads:dateOffset(careBreak['hospitalBreakCarerHospitalStartDate_day'],
                                                               careBreak['hospitalBreakCarerHospitalStartDate_month'],
                                                               careBreak['hospitalBreakCarerHospitalStartDate_year'],
                                                               'd MMMMMMMMMM yyyy',
                                                               '')}
                                </td>
                                <td>
                                    <c:if test="${not empty careBreak['hospitalBreakCarerHospitalEndDate_day']}">
                                        ${cads:dateOffset(careBreak['hospitalBreakCarerHospitalEndDate_day'],
                                                                   careBreak['hospitalBreakCarerHospitalEndDate_month'],
                                                                   careBreak['hospitalBreakCarerHospitalEndDate_year'],
                                                                   'd MMMMMMMMMM yyyy',
                                                                   '')}
                                    </c:if>
                                    <c:if test="${empty careBreak['hospitalBreakCarerHospitalEndDate_day']}">
                                        <t:message code="breaks.summary.not.ended" />
                                    </c:if>
                                </td>
                            </c:if>
                            <c:if test="${careBreak['hospitalBreakWhoInHospital']=='Caree'}">
                                <td>${careeFirstName} ${careeSurname}</td>
                                <td><t:message code="breaks_hospital.text" /></td>
                                <td>
                                    ${cads:dateOffset(careBreak['hospitalBreakCareeHospitalStartDate_day'],
                                                               careBreak['hospitalBreakCareeHospitalStartDate_month'],
                                                               careBreak['hospitalBreakCareeHospitalStartDate_year'],
                                                               'd MMMMMMMMMM yyyy',
                                                               '')}
                                </td>
                                <td>
                                    <c:if test="${not empty careBreak['hospitalBreakCareeHospitalEndDate_day']}">
                                        ${cads:dateOffset(careBreak['hospitalBreakCareeHospitalEndDate_day'],
                                                                   careBreak['hospitalBreakCareeHospitalEndDate_month'],
                                                                   careBreak['hospitalBreakCareeHospitalEndDate_year'],
                                                                   'd MMMMMMMMMM yyyy',
                                                                   '')}
                                    </c:if>
                                    <c:if test="${empty careBreak['hospitalBreakCareeHospitalEndDate_day']}">
                                        <t:message code="breaks.summary.not.ended" />
                                    </c:if>
                                </td>
                            </c:if>
                            <td class="actions">
                                <button type="submit" id="changerowbreakshospital_${careBreak['breakshospital_id']}" class="actionButton" name="changeSubFormRecord" value="breakshospital_${careBreak['breakshospital_id']}" aria-label="<t:message code="change.button" />"><t:message code="change" /></button>
                                <button type="button" id="deleterowbreakshospital_${careBreak['breakshospital_id']}" class="actionButton" name="deleteBreaksHospital" value="${careBreak['breakshospital_id']}" onclick="window.showPanel('deleterowhospital_${careBreak['breakshospital_id']}', true)" aria-label="<t:message code="delete.button" />"><t:message code="delete" /></button>
                            </td>
                        </tr>
                        <t:hiddenWarning id="deleterowhospital_${careBreak['breakshospital_id']}" outerClass="delete-confirm prompt breaks-prompt validation-summary" triggerId="deleterowbreakshospital_${careBreak['breakshospital_id']}" triggerValue="deleterowbreakshospital_${careBreak['breakshospital_id']}}" trwrap="true" colspanvalue="5">
                            <td colspan="5">
                                <p class="normalMsg">&nbsp;&nbsp;&nbsp;<t:message code="delete.break.message" /></p>
                                &nbsp;&nbsp;&nbsp;<button type="button" class="button row secondary" id="noDeleteButtonHospital" value="${careBreak['breakshospital_id']}" onclick="window.hidePanel('deleterowhospital_${careBreak['breakshospital_id']}', true)" aria-label="<t:message code="no.delete.label" />" ><t:message code="no.delete" /></button>
                                &nbsp;&nbsp;&nbsp;<button type="submit" class="button row warning" id="yesDeleteButtonHospital" name="deleteSubFormRecord" value="breakshospital_${careBreak['breakshospital_id']}" aria-label="<t:message code="yes.delete.label" />" ><t:message code="yes.delete" /></button>
                            </td>
                        </t:hiddenWarning>
                    </c:forEach>
                    <c:forEach items="${breaksrespite}" var="careBreak">
                        <tr id="break_${careBreak['breaksrespite_id']}">
                            <c:if test="${careBreak['respiteBreakWhoInRespite']=='Carer'}">
                                <td><t:message code="breaks.summary.you" /></td>
                                <td><t:message code="breaks_respite.text" /></td>
                                <td>
                                    ${cads:dateOffset(careBreak['respiteBreakCarerRespiteStartDate_day'],
                                                               careBreak['respiteBreakCarerRespiteStartDate_month'],
                                                               careBreak['respiteBreakCarerRespiteStartDate_year'],
                                                               'd MMMMMMMMMM yyyy',
                                                               '')}
                                </td>
                                <td>
                                    <c:if test="${not empty careBreak['respiteBreakCarerRespiteEndDate_day']}">
                                        ${cads:dateOffset(careBreak['respiteBreakCarerRespiteEndDate_day'],
                                                               careBreak['respiteBreakCarerRespiteEndDate_month'],
                                                               careBreak['respiteBreakCarerRespiteEndDate_year'],
                                                               'd MMMMMMMMMM yyyy',
                                                               '')}
                                    </c:if>
                                    <c:if test="${empty careBreak['respiteBreakCarerRespiteEndDate_day']}">
                                        <t:message code="breaks.summary.not.ended" />
                                    </c:if>
                                </td>
                            </c:if>
                            <c:if test="${careBreak['respiteBreakWhoInRespite']=='Caree'}">
                                <td>${careeFirstName} ${careeSurname}</td>
                                <td><t:message code="breaks_respite.text" /></td>
                                <td>
                                    ${cads:dateOffset(careBreak['respiteBreakCareeRespiteStartDate_day'],
                                                               careBreak['respiteBreakCareeRespiteStartDate_month'],
                                                               careBreak['respiteBreakCareeRespiteStartDate_year'],
                                                               'd MMMMMMMMMM yyyy',
                                                               '')}
                                </td>
                                <td>
                                    <c:if test="${not empty careBreak['respiteBreakCareeRespiteEndDate_day']}">
                                        ${cads:dateOffset(careBreak['respiteBreakCareeRespiteEndDate_day'],
                                                                   careBreak['respiteBreakCareeRespiteEndDate_month'],
                                                                   careBreak['respiteBreakCareeRespiteEndDate_year'],
                                                                   'd MMMMMMMMMM yyyy',
                                                                   '')}
                                    </c:if>
                                    <c:if test="${empty careBreak['respiteBreakCareeRespiteEndDate_day']}">
                                        <t:message code="breaks.summary.not.ended" />
                                    </c:if>
                                </td>
                            </c:if>
                            <td class="actions">
                                <button type="submit" id="changerowbreaksrespite_${careBreak['breaksrespite_id']}" class="actionButton" name="changeSubFormRecord" value="breaksrespite_${careBreak['breaksrespite_id']}" aria-label="<t:message code="change.button" />"><t:message code="change" /></button>
                                <button type="button" id="deleterowbreaksrespite_${careBreak['breaksrespite_id']}" class="actionButton" name="deleteBreaksRespite" value="${careBreak['breaksrespite_id']}" onclick="window.showPanel('deleterespite_${careBreak['breaksrespite_id']}', true)" aria-label="<t:message code="delete.button" />"><t:message code="delete" /></button>
                            </td>
                            <t:hiddenWarning id="deleterespite_${careBreak['breaksrespite_id']}" outerClass="prompt breaks-prompt validation-summary" triggerId="deleterowbreaksrespite_${careBreak['breaksrespite_id']}" triggerValue="deletebreaksrespite_${careBreak['breaksrespite_id']}" trwrap="true" colspanvalue="5">
                                <td colspan="5">
                                    <p class="normalMsg">&nbsp;&nbsp;&nbsp;<t:message code="delete.break.message" /></p>
                                    &nbsp;&nbsp;&nbsp;<button type="button" class="button row secondary" id="noDeleteButtonRespite" value="${careBreak['breaksrespite_id']}" onclick="window.hidePanel('deleterespite_${careBreak['breaksrespite_id']}', true)" aria-label="<t:message code="no.delete.label" />" ><t:message code="no.delete" /></button>
                                    &nbsp;&nbsp;&nbsp;<button type="submit" class="button row warning" id="yesDeleteButtonRespite" name="deleteSubFormRecord" value="breaksrespite_${careBreak['breaksrespite_id']}" aria-label="<t:message code="yes.delete.label" />" ><t:message code="yes.delete" /></button>
                                </td>
                            </t:hiddenWarning>
                        </tr>
                    </c:forEach>
                    <c:forEach items="${breaksother}" var="careBreak">
                        <tr id="break_${careBreak['breaksother_id']}">
                            <td><t:message code="breaks.summary.you" /></td>
                            <td><t:message code="breaks.summary.other" /></td>
                            <td>
                                ${cads:dateOffset(careBreak['careeSomewhereElseStartDate_day'],
                                                           careBreak['careeSomewhereElseStartDate_month'],
                                                           careBreak['careeSomewhereElseStartDate_year'],
                                                           'd MMMMMMMMMM yyyy',
                                                           '')}
                            </td>
                            <td>
                                <c:if test="${not empty careBreak['careeSomewhereElseEndDate_day']}">
                                    ${cads:dateOffset(careBreak['careeSomewhereElseEndDate_day'],
                                                               careBreak['careeSomewhereElseEndDate_month'],
                                                               careBreak['careeSomewhereElseEndDate_year'],
                                                               'd MMMMMMMMMM yyyy',
                                                               '')}
                                </c:if>
                                <c:if test="${empty careBreak['careeSomewhereElseEndDate_day']}">
                                    <t:message code="breaks.summary.not.ended" />
                                </c:if>
                            </td>
                            <td class="actions">
                                <button type="submit" id="changerowbreaksother_${careBreak['breaksother_id']}" class="actionButton" name="changeSubFormRecord" value="${careBreak['breaksother_id']}" aria-label="<t:message code="change.button" />"><t:message code="change" /></button>
                                <button type="button" id="deleterowbreaksother_${careBreak['breaksother_id']}" class="actionButton" name="deleteBreaksOther" value="${careBreak['breaksother_id']}" onclick="window.showPanel('deleteother_${careBreak['breaksother_id']}', true)" aria-label="<t:message code="delete.button" />"><t:message code="delete" /></button>
                            </td>
                            <t:hiddenWarning id="deleteother_${careBreak['breaksother_id']}" outerClass="prompt breaks-prompt validation-summary" triggerId="deleterowbreaksother_${careBreak['breaksother_id']}" triggerValue="deleterowbreaksother_${careBreak['breaksother_id']}" trwrap="true" colspanvalue="5">
                                <td colspan="5">
                                    <p class="normalMsg">&nbsp;&nbsp;&nbsp;<t:message code="delete.break.message" /></p>
                                    &nbsp;&nbsp;&nbsp;<button type="button" class="button row secondary" id="noDeleteButtonOther" value="${careBreak['breaksother_id']}" onclick="window.hidePanel('deleteother_${careBreak['breaksother_id']}', true)" aria-label="<t:message code="no.delete.label" />" ><t:message code="no.delete" /></button>
                                    &nbsp;&nbsp;&nbsp;<button type="submit" class="button row warning" id="yesDeleteButtonOther" name="deleteSubFormRecord" value="breaksother_${careBreak['breaksother_id']}" aria-label="<t:message code="yes.delete.label" />" ><t:message code="yes.delete" /></button>
                                </td>
                            </t:hiddenWarning>
                        </tr>
                    </c:forEach>
                </table>
            </t:panel>
        </c:if>

        <t:group name="moreBreaksInCareResidenceGroup">
            <t:checkbox name="breaks_hospital" outerClass="no-class" />
            <t:checkbox name="breaks_respite" outerClass="no-class" />
            <t:checkbox name="breaks_none" outerClass="no-class" checkedValue="none" />
        </t:group>
        <t:yesnofield name="moreBreaksInCare" />
    </t:pageContent>
</t:mainPage>


