package seedu.address.model.person;

import java.util.Set;

import seedu.address.model.appointment.Appointment;
import seedu.address.model.tag.Tag;

public class Buyer extends Person{
    public Buyer(Name name, Phone phone, Email email, Set<Tag> tags, Appointment appointment, Property property) {
        super(name, phone, email, tags, appointment, property);
    }
}
