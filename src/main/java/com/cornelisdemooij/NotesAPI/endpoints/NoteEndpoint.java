package com.cornelisdemooij.NotesAPI.endpoints;

import com.cornelisdemooij.NotesAPI.model.Note;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cornelisdemooij.NotesAPI.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;


@Path("note")
@Component
public class NoteEndpoint {

    @Autowired
    private NoteService noteService;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNote(@PathParam("id") long id) {
        Optional<Note> optionalNote = noteService.findById(id);
        if (optionalNote.isPresent()) {
            Note note = optionalNote.get();
            return Response.ok(note).build();
        } else {
            return Response.status(404, "No note found for id " + Long.toString(id)).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNotes() {
        Iterable<Note> notes = noteService.findAll();
        return Response.ok(notes).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response postNote(@RequestBody Note note) {
        Note result = noteService.save(note);
        if (result != null) {
            return Response.accepted(true).build();
        } else {
            return Response.serverError().build();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response putNote(@PathParam("id") long id, @RequestBody Note note) {
        Optional<Note> optionalUpdatedNote = noteService.updateById(id, note);
        if (optionalUpdatedNote.isPresent()) {
            return Response.ok(true).build();
        } else {
            return Response.serverError().build();
        }
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteNote(@PathParam("id") long id) {
        try {
            noteService.deleteById(id);
            return Response.ok().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Response.status(404, "Note with id " + Long.toString(id) + " was not found.").build();
        }
    }
}
