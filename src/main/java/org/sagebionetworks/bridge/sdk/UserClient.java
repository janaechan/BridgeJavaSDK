package org.sagebionetworks.bridge.sdk;

import java.util.List;

import org.joda.time.DateTimeZone;
import org.sagebionetworks.bridge.sdk.models.ResourceList;
import org.sagebionetworks.bridge.sdk.models.holders.GuidCreatedOnVersionHolder;
import org.sagebionetworks.bridge.sdk.models.holders.IdentifierHolder;
import org.sagebionetworks.bridge.sdk.models.schedules.Schedule;
import org.sagebionetworks.bridge.sdk.models.schedules.ScheduledActivity;
import org.sagebionetworks.bridge.sdk.models.subpopulations.SubpopulationGuid;
import org.sagebionetworks.bridge.sdk.models.surveys.Survey;
import org.sagebionetworks.bridge.sdk.models.surveys.SurveyAnswer;
import org.sagebionetworks.bridge.sdk.models.surveys.SurveyResponse;
import org.sagebionetworks.bridge.sdk.models.upload.UploadRequest;
import org.sagebionetworks.bridge.sdk.models.upload.UploadSession;
import org.sagebionetworks.bridge.sdk.models.upload.UploadValidationStatus;
import org.sagebionetworks.bridge.sdk.models.users.ConsentSignature;
import org.sagebionetworks.bridge.sdk.models.users.DataGroups;
import org.sagebionetworks.bridge.sdk.models.users.ExternalIdentifier;
import org.sagebionetworks.bridge.sdk.models.users.SharingScope;
import org.sagebionetworks.bridge.sdk.models.users.UserProfile;

public interface UserClient {

    /**
     * Retrieve the UserProfile associated with the currently signed in account.
     *
     * @return UserProfile
     */
    public UserProfile getProfile();

    /**
     * Update the UserProfile associated with the currently signed in account.
     *
     * @param profile
     *      changed profile object that will be used to update UserProfile info on Bridge.
     */
    public void saveProfile(UserProfile profile);
    
    /**
     * Add an external participant identifier for the current user. This identifier may be provided by some studies in
     * order to collate research data collected through Bridge with external sources of information about the
     * participant. Bridge does not verify the uniqueness of this identifier. The identifier may be updated, but it
     * cannot be removed (if necessary, set it to a deleted value like "N/A").
     * 
     * @param identifier
     *      a unique identifier for this participant in this study, generated by an external authority
     */
    public void addExternalUserIdentifier(ExternalIdentifier identifier);

    /**
     * Consent to research.
     *
     * @param subpopGuid
     *          The subpopulation of the study that the user is consenting to participate int.
     * @param signature
     *          Name, birthdate, and optionally signature image, of consenter's signature.
     * @param scope
     *          Scope of sharing for this consent
     */
    public void consentToResearch(SubpopulationGuid subpopGuid, ConsentSignature signature, SharingScope scope);

    /**
     * Returns the user's consent signature, which includes the name, birthdate, and signature image.
     *
     * @param subpopGuid
     * @return consent signature
     */
    public ConsentSignature getConsentSignature(SubpopulationGuid subpopGuid);
    
    /**
     * Email the signed consent agreement to the participant's email address.
     * 
     * @param subpopGuid
     */
    public void emailConsentSignature(SubpopulationGuid subpopGuid);

    /**
     * Change (stop, resume) the sharing of data for this participant.
     * @param sharing
     */
    public void changeSharingScope(SharingScope sharing);
    
    /**
     * Withdraw user's consent to participate in research. The user will no longer be able to submit 
     * data to the server without receiving an error response from the server (ConsentRequiredException).
     * @param subpopGuid
     * @param reason
     */
    public void withdrawConsentToResearch(SubpopulationGuid subpopGuid, String reason);
    
    /**
     * Get all schedules associated with a study.
     *
     * @return List<Schedule>
     */
    public ResourceList<Schedule> getSchedules();

    /**
     * Update the list of data groups associated with this user.
     * @param dataGroups
     */
    public void updateDataGroups(DataGroups dataGroups);
    
    /**
     * Get the data groups associated with this user.
     * @return
     */
    public DataGroups getDataGroups();
    
    /**
     * Get a survey version with a GUID and a createdOn timestamp.
     *
     * @param keys
     *
     * @return Survey
     */
    public Survey getSurvey(GuidCreatedOnVersionHolder keys);
    
    /**
     * Get the most recently published survey available for the provided survey GUID.
     * @param guid
     * 
     * @return Survey
     */
    public Survey getSurveyMostRecentlyPublished(String guid);
    
    /**
     * Submit a list of SurveyAnswers to a particular survey. An identifier for the survey response will be 
     * auto-generated and returned by the server.
     *
     * @param keys
     *            The survey that the answers will be added to.
     * @param answers
     *            The answers to add to the survey.
     * @return GuidHolder A holder storing the GUID of the survey.
     */
    public IdentifierHolder submitAnswersToSurvey(GuidCreatedOnVersionHolder keys, List<SurveyAnswer> answers);

    /**
     * Submit a list of SurveyAnswers to a particular survey, using a specified identifier
     * for the survey response (the value should be a unique string, like a GUID, that 
     * has not been used for any prior submissions).
     *
     * @param keys
     *            The survey that the answers will be added to.
     * @param identifier
     *            A unique string to identify this set of survey answers as originating
     *            from the same run of a survey
     * @param answers
     *            The answers to add to the survey.
     * @return IdentifierHolder A holder storing the GUID of the survey.
     */
    public IdentifierHolder submitAnswersToSurvey(GuidCreatedOnVersionHolder keys, String identifier, List<SurveyAnswer> answers);
    
    /**
     * Get the survey response associated with the identifier string.
     *
     * @param identifier
     *            The identifier for this SurveyResponse
     * @return SurveyResponse
     */
    public SurveyResponse getSurveyResponse(String identifier);

    /**
     * Add a list of SurveyAnswers to a SurveyResponse.
     *
     * @param identifier
     *            The identifier for the response that answers will be added (the response must already exist).
     * @param answers
     *            The answers that will be added to the response.
     */
    public void addAnswersToResponse(String identifier, List<SurveyAnswer> answers);

    /**
     * Request an upload session from the user.
     *
     * @param request
     *            the request object Bridge uses to create the Upload Session.
     * @return UploadSession
     */
    public UploadSession requestUploadSession(UploadRequest request);

    /**
     * Upload a file using the requested UploadSession. Closes the upload after it's done.
     *
     * @param session
     *            The session used to upload.
     * @param fileName
     *            File to upload.
     */
    public void upload(UploadSession session, UploadRequest request, String fileName);

    /**
     * Gets the upload status (status and validation messages) for the given upload ID
     *
     * @param uploadId
     *         ID of the upload, obtained from the upload session
     * @return object containing upload status and validation messages
     */
    public UploadValidationStatus getUploadStatus(String uploadId);
    
    /**
     * Get the list of available or scheduled activities.
     * @param daysAhead
     *      return activities from now until the number of days ahead from now (maximum of 4 days)
     * @param timeZone
     *      the timezone the activities should use when returning scheduledOn and expiresOn dates
     * @return
     */
    public ResourceList<ScheduledActivity> getScheduledActivities(int daysAhead, DateTimeZone timeZone);
    
    /**
     * Update these activities (by setting either the startedOn or finishedOn values of each activity). 
     * The only other required value that must be set for the activity is its GUID.
     * @param scheduledActivities
     */
    public void updateScheduledActivities(List<ScheduledActivity> scheduledActivities);
}
