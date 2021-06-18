package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;

    @Autowired
    ParticipantService participantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {

        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMeeting(@PathVariable("id") Long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}
    
    @RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting) {
		if (meetingService.findById(meeting.getId()) != null) {
			return new ResponseEntity(
					"Unable to create. A meeting with ID " + meeting.getId() + " already exist.", HttpStatus.CONFLICT);
		}
		meetingService.add(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
	}

	//POST http://localhost:8080/meetings/2/participants
    @RequestMapping(value = "/{id}/participants", method = RequestMethod.POST)	
	public ResponseEntity<?> addParticipant(@PathVariable("id") Long id, @RequestBody Participant participant) {
		Participant addedParticipant = participantService.findByLogin(participant.getLogin());
		Meeting meeting = meetingService.findById(id);
		if (addedParticipant == null) {
			return new ResponseEntity("Unable to add participant " + participant.getLogin() + ". That user does not exist.", HttpStatus.NOT_FOUND);
		}	
		meeting.addParticipant(addedParticipant);
		meetingService.update(meeting);
		return new ResponseEntity("User "+ participant.getLogin()  + " was added to the meeting " + meeting.getId(), HttpStatus.CREATED);
	}

	//GET http://localhost:8080/meetings/2/participants
	@RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetingParticipants(@PathVariable("id") Long id ) {
		Meeting meeting = meetingService.findById(id);
		Collection<Participant> participants = meeting.getParticipants();
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}
	
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeeting(@PathVariable("id") Long id) {
    	Meeting meeting = meetingService.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        meetingService.delete(meeting);
        return new ResponseEntity<Meeting>(HttpStatus.NO_CONTENT);
    }
    
}
