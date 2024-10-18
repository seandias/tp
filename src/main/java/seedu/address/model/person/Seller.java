package seedu.address.model.person;

import java.util.Set;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.tag.Tag;


public class Seller extends Person{
    public Seller(Name name, Phone phone, Email email, Set<Tag> tags, Appointment appointment, Property property) {
        super(name, phone, email, tags, appointment, property);
    }
    public boolean hasListing() {
        return false;
    }
}
